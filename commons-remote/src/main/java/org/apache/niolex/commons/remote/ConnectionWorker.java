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
 * This class wraps the connections, process client commands and return results.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-7-25
 */
public class ConnectionWorker implements Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(ConnectionWorker.class);
	private static final Map<String, Executer> COMMAND_MAP = new HashMap<String, Executer>();
	private static final ThreadLocal<String> ENDL_HOLDER = new ThreadLocal<String>();
	private static String AUTH_INFO = null;

	// Add all executers here.
	static {
		addCommand("get", new Executer.Getter());
		addCommand("list", new Executer.Lister());
		addCommand("set", new Executer.Setter());
		addCommand("invoke", new Executer.Invoker());
		addCommand("mon", new Executer.InvoMonitor());
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
	 * Set authentication info to worker.
	 *
	 * @param s the authentication string
	 */
	public static final void setAuthInfo(String s) {
	    AUTH_INFO = s;
	}

	/**
	 * Get the end line character for this current connection.
	 *
	 * @return The end line character
	 */
	public static String endl() {
	    String endl = ENDL_HOLDER.get();
	    return endl == null ? "\n" : endl;
	}

	// Scan the input stream.
	private final Scanner scan;
	// Write result to this output.
	private final OutputStream out;
	// The socket connection.
	private final Socket sock;
	// Current connection number.
	private final AtomicInteger connNum;
	// The bean map.
	private final ConcurrentHashMap<String, Object> beanMap;
	// Had we authenticated this connection?
	private boolean hasAuthed = false;

	/**
	 * The main Constructor, used to instantiate connection worker.
	 *
	 * @param socket the socket to work with
	 * @param map the global bean map
	 * @param connectionNumber the connection number counter
	 * @throws IOException
	 */
	public ConnectionWorker(Socket socket, ConcurrentHashMap<String, Object> map, AtomicInteger connectionNumber)
	        throws IOException {
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
		    LOG.debug("Error occurred when execute commands.", e);
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

			Boolean b = commonProcess(comm, args);
			if (b == null) break;
			if (b == Boolean.TRUE) continue;

			// Parse tree.
			Path path = Path.parsePath(args[1]);
			if (path.getType() == Type.INVALID) {
				writeAndFlush(path.getName() + "^");
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
					writeAndFlush("Invalid Path started at " + pathIdx + "." + name);
					break;
				}
				switch(path.getType()) {
				case ARRAY:
					// Want to visit collection[array, list, set] here.
					int idx = path.getIdx();
					if (parent instanceof Collection<?>) {
						Collection<? extends Object> os = (Collection<?>) parent;
						if (os.size() <= idx) {
							writeAndFlush("Invalid Path started at "
									+ pathIdx + "." + name + " Array Out of Bound.");
							break Outter;
						}
						Iterator<? extends Object> iter = os.iterator();
						for (int i = 0; i < idx; ++i) {
							iter.next();
						}
						parent = iter.next();
					} else if (parent.getClass().isArray()) {
						if (Array.getLength(parent) <= idx) {
							writeAndFlush("Invalid Path started at " + pathIdx + "." + name + " Array Out of Bound.");
							break Outter;
						}
						parent = Array.get(parent, idx);
					} else {
						writeAndFlush("Invalid Path started at " + pathIdx + "." + name + " Not Array.");
						break Outter;
					}
					break;
				case MAP:
					// Want to visit map here.
					if (parent instanceof Map<?, ?>) {
						Map<? extends Object, ? extends Object> map = (Map<?, ?>) parent;
						if (map.size() == 0) {
							writeAndFlush("Map at " + pathIdx + "." + name + " Is Empty.");
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
								writeAndFlush("Invalid Map Key at " + pathIdx + "." + name);
								break Outter;
							}
						} else if (key instanceof Long) {
						    try {
						        long lkey = Long.parseLong(realKey);
						        parent = map.get(lkey);
						    } catch (Exception e) {
						        writeAndFlush("Invalid Map Key at " + pathIdx + "." + name);
						        break Outter;
						    }
						} else {
							writeAndFlush("This Map Key Type " + key.getClass().getSimpleName() + " at "
									+ pathIdx + "." + name + " Is Not Supported.");
							break Outter;
						}
					} else {
						writeAndFlush("Invalid Path started at " + pathIdx + "." + name + " Not Map.");
						break Outter;
					}
					break;
				}
				++pathIdx;
				path = path.next();
			}

			if (parent == null) {
			    writeAndFlush("Path Not Found.");
				continue;
			}
			if (path != null) {
				continue;
			}

			Executer ex = COMMAND_MAP.get(comm);
			ex.execute(parent, out, args);
		}

	}

	/**
	 * Process the common commands.
	 *
	 * @param comm the command name
	 * @param args the command arguments
	 * @return TRUE if command processed, FALSE if not, null if need exit
	 * @throws IOException
	 */
	protected Boolean commonProcess(String comm, String[] args) throws IOException {
	    // Change End Of Line
        if (comm.startsWith("win")) {
            ENDL_HOLDER.set("\r\n");
            writeAndFlush("End Line Changed.");
            return Boolean.TRUE;
        }
        if (comm.startsWith("lin")) {
            ENDL_HOLDER.set("\n");
            writeAndFlush("End Line Changed.");
            return Boolean.TRUE;
        }
        // Quit
        if ("quit".equals(comm) || "exit".equals(comm)) {
            writeAndFlush("Goodbye.");
            return null;
        }
        // Auth
        if ("auth".equals(comm)) {
            if (AUTH_INFO == null || (args.length == 2 && AUTH_INFO.equals(args[1]))) {
                writeAndFlush("Authenticate Success.");
                hasAuthed = true;
            } else {
                hasAuthed = false;
                writeAndFlush("Authenticate Failed.");
            }
            return Boolean.TRUE;
        }
        if (AUTH_INFO != null && !hasAuthed) {
            writeAndFlush("Please authenticate.");
            return Boolean.TRUE;
        }
        // Invalid command.
        if (!COMMAND_MAP.containsKey(comm) || args.length < 2 || args[1].length() == 0) {
            writeAndFlush("Invalid Command.");
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
	}

	/**
	 * Write the string to the output stream and automatically add an end line character at the
	 * end of the string, then flush the output stream.
	 *
	 * @param s the string to be written
	 * @throws IOException
	 */
	protected void writeAndFlush(String s) throws IOException {
	    out.write(StringUtil.strToUtf8Byte(s + endl()));
	    out.flush();
	}

}
