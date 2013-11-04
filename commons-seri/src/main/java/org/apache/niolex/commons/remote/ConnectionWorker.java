/**
 * ConnectionWorker.java
 *
 * Copyright 2012 Niolex, Inc.
 *
 * Niolex licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.apache.niolex.commons.remote;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.reflect.FieldUtil;
import org.apache.niolex.commons.remote.Path.Type;
import org.apache.niolex.commons.util.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class process client commands and return results.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-7-25
 */
public class ConnectionWorker implements Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(ConnectionWorker.class);
	private static final Map<String, Executer> COMMAND_MAP = new HashMap<String, Executer>();
	private static final ThreadLocal<String> ENDL_HOLDER = new InheritableThreadLocal<String>();
	private static String AUTH_INFO = null;

	// Add all executers here.
	static {
		COMMAND_MAP.put("get", new Executer.Getter());
		COMMAND_MAP.put("list", new Executer.Lister());
		COMMAND_MAP.put("set", new Executer.Setter());
		COMMAND_MAP.put("invoke", new Executer.Invoker());
		COMMAND_MAP.put("mon", new Executer.InvoMonitor());
	}

	/**
	 * Add a custom command to worker.
	 *
	 * @param key the command key
	 * @param value the command executer
	 * @return the previous value associated with key, or null if there was no mapping for key.
	 */
	public static final Object addCommand(String key, Executer value) {
	    return COMMAND_MAP.put(key, value);
	}

	/**
	 * Get the end line character for this current connection.
	 *
	 * @return The end line character
	 */
	public static String endl() {
	    return ENDL_HOLDER.get();
	}

	/**
	 * Set authentication info to worker.
	 *
	 * @param s the authentication string
	 */
	public static final void setAuthInfo(String s) {
	    AUTH_INFO = s;
	}

	// Scan input stream.
	private final Scanner scan;
	private final OutputStream out;
	private final Socket sock;
	private final AtomicInteger connNum;
	private final ConcurrentHashMap<String, Object> beanMap;
	private boolean isAuth = false;

	/**
	 * The main Constructor.
	 *
	 * @param socket the socket to work with
	 * @param map the global bean map
	 * @param connectionNumber the connection number counter
	 * @throws IOException
	 */
	public ConnectionWorker(Socket socket, ConcurrentHashMap<String, Object> map, AtomicInteger connectionNumber) throws IOException {
		scan = new Scanner(socket.getInputStream(), "UTF-8");
		out = socket.getOutputStream();
		sock = socket;
		connNum = connectionNumber;
		beanMap = map;
		LOG.info("Remote client [{}] connected.", socket.getRemoteSocketAddress());
	}

	/**
	 * The main work loop.
	 *
	 * Override super method
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
	    ENDL_HOLDER.set("\n");
		try {
			execute();
		} catch (Exception e) {
		} finally {
			scan.close();
			SystemUtil.close(out);
			SystemUtil.close(sock);
			connNum.decrementAndGet();
		}
		LOG.info("Remote client [{}] disconnected.", sock.getRemoteSocketAddress());
	}

	/**
	 * Execute the command here.
	 * The main work is to find the correct target object and execute the command on it.
	 *
	 * @throws IOException
	 */
	public void execute() throws IOException {
		while (scan.hasNextLine()) {
			final String line = scan.nextLine();
			final String[] args = line.split("\\s+", 4);
			// Empty line.
			if (args.length == 0 || args[0].length() == 0) {
				continue;
			}
			final String comm = args[0].toLowerCase();
			// Change End Of Line
			if (comm.startsWith("win")) {
				ENDL_HOLDER.set("\r\n");
				out.write(StringUtil.strToAsciiByte("End Line Changed." + endl()));
				continue;
			}
			if (comm.startsWith("lin")) {
				ENDL_HOLDER.set("\n");
				out.write(StringUtil.strToAsciiByte("End Line Changed." + endl()));
				continue;
			}
			// Quit
			if ("quit".equals(comm) || "exit".equals(comm)) {
				out.write(StringUtil.strToAsciiByte("Goodbye." + endl()));
				break;
			}
			// Auth
			if ("auth".equals(comm)) {
				if (AUTH_INFO == null || (args.length == 2 && AUTH_INFO.equals(args[1]))) {
					out.write(StringUtil.strToAsciiByte("Authenticate Success." + endl()));
					isAuth = true;
				} else {
					out.write(StringUtil.strToAsciiByte("Authenticate failed." + endl()));
				}
				continue;
			}
			if (AUTH_INFO != null && !isAuth) {
				out.write(StringUtil.strToAsciiByte("Please authenticate." + endl()));
				continue;
			}
			// Invalid command.
			if (!COMMAND_MAP.containsKey(comm) || args.length < 2 || args[1].length() == 0) {
				out.write(StringUtil.strToAsciiByte("Invalid Command." + endl()));
				continue;
			}
			// Parse tree.
			Path path = Path.parsePath(args[1]);
			if (path.getType() == Type.INVALID) {
				out.write(StringUtil.strToAsciiByte(path.getName() + "^" + endl()));
				continue;
			}
			Object parent = beanMap.get(path.getName());
			int pathIdx = 1;
			// We need to break this while loop.
			Outter:
			while (parent != null && path != null) {
				// Navigate into bean according to the path.
				String name = path.getName();
				try {
				    // For (pathIdx == 1) we already get parent from bean map.
					if (pathIdx != 1) {
						Field f = FieldUtil.getField(parent.getClass(), name);
						f.setAccessible(true);
						parent = f.get(parent);
					}
				} catch (Exception e) {
					out.write(StringUtil.strToAsciiByte("Invalid Path started at "
							+ pathIdx + "." + name + endl()));
					break;
				}
				switch(path.getType()) {
				case ARRAY:
					// Want to visit collection[array, list, set] here.
					int idx = path.getIdx();
					if (parent instanceof Collection<?>) {
						Collection<? extends Object> os = (Collection<?>) parent;
						if (os.size() <= idx) {
							out.write(StringUtil.strToAsciiByte("Invalid Path started at "
									+ pathIdx + "." + name + " Array Out of Bound." + endl()));
							break Outter;
						}
						Iterator<? extends Object> iter = os.iterator();
						for (int i = 0; i < idx; ++i) {
							iter.next();
						}
						parent = iter.next();
					} else if (parent.getClass().isArray()) {
						if (Array.getLength(parent) <= idx) {
							out.write(StringUtil.strToAsciiByte("Invalid Path started at "
									+ pathIdx + "." + name + " Array Out of Bound." + endl()));
							break Outter;
						}
						parent = Array.get(parent, idx);
					} else {
						out.write(StringUtil.strToAsciiByte("Invalid Path started at "
								+ pathIdx + "." + name + " Not Array." + endl()));
						break Outter;
					}
					break;
				case MAP:
					// Want to visit map here.
					if (parent instanceof Map<?, ?>) {
						Map<? extends Object, ? extends Object> map = (Map<?, ?>) parent;
						if (map.size() == 0) {
							out.write(StringUtil.strToAsciiByte("Map at "
									+ pathIdx + "." + name + " Is Empty." + endl()));
							break Outter;
						}
						Object key = map.keySet().iterator().next();
						String realKey = path.getKey();
						if (key instanceof String) {
							parent = map.get(realKey);
						} else if (key instanceof Integer) {
							try {
								idx = Integer.parseInt(realKey);
								parent = map.get(idx);
							} catch (Exception e) {
								out.write(StringUtil.strToAsciiByte("Invalid Map Key at "
										+ pathIdx + "." + name + endl()));
								break Outter;
							}
						} else if (key instanceof Long) {
						    try {
						        long lkey = Long.parseLong(realKey);
						        parent = map.get(lkey);
						    } catch (Exception e) {
						        out.write(StringUtil.strToAsciiByte("Invalid Map Key at "
						                + pathIdx + "." + name + endl()));
						        break Outter;
						    }
						} else {
							out.write(StringUtil.strToAsciiByte("This Map Key Type "
									+ key.getClass().getSimpleName() + " at "
									+ pathIdx + "." + name + " Is Not Supported." + endl()));
							break Outter;
						}
					} else {
						out.write(StringUtil.strToAsciiByte("Invalid Path started at "
								+ pathIdx + "." + name + " Not Map." + endl()));
						break Outter;
					}
					break;
				}
				++pathIdx;
				path = path.next();
			}
			if (parent == null) {
				out.write(StringUtil.strToAsciiByte("Path Not Found." + endl()));
				continue;
			}
			if (path != null) {
				continue;
			}
			Executer ex = COMMAND_MAP.get(comm);
			ex.execute(parent, out, args);
		}

	}

}
