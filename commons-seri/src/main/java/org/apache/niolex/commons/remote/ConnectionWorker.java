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

import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.reflect.FieldUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class process client commands and return results.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-7-25
 */
public class ConnectionWorker implements Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(ConnectionWorker.class);
	private static final Map<String, Executer> COMMAND_MAP = new HashMap<String, Executer>();

	// Add all executers here.
	static {
		COMMAND_MAP.put("get", new Executer.Getter());
		COMMAND_MAP.put("list", new Executer.Lister());
		COMMAND_MAP.put("set", new Executer.Setter());
	}

	// Scan input stream.
	private final Scanner scan;
	private final OutputStream out;
	private final Socket sock;
	private final ConcurrentHashMap<String, Object> beanMap;

	/**
	 * @param socket
	 * @param map
	 * @throws IOException
	 */
	public ConnectionWorker(Socket socket, ConcurrentHashMap<String, Object> map) throws IOException {
		scan = new Scanner(socket.getInputStream(), "UTF-8");
		out = socket.getOutputStream();
		sock = socket;
		beanMap = map;
		LOG.info("Remote client [{}] connected.", socket.getRemoteSocketAddress());
	}

	/**
	 * Override super method
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			execute();
		} catch (IOException e) {
		} finally {
			scan.close();
			try {
				out.close();
			} catch (IOException e) {}
			try {
				sock.close();
			} catch (IOException e) {}
		}
		LOG.info("Remote client [{}] disconnected.", sock.getRemoteSocketAddress());
	}

	/**
	 * Execute the command.
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
			// Quit
			if ("quit".equals(comm) || "exit".equals(comm)) {
				out.write(StringUtil.strToAsciiByte("Goodbye.\n"));
				break;
			}
			// Invalid command.
			if (!COMMAND_MAP.containsKey(comm) || args.length < 2) {
				out.write(StringUtil.strToAsciiByte("Invalid Command.\n"));
				continue;
			}
			// Parse tree.
			final String[] path = args[1].split("\\.");
			if (path.length < 1) {
				out.write(StringUtil.strToAsciiByte("Invalid Path Length.\n"));
				continue;
			}
			Object parent = beanMap.get(path[0]);
			int pathIdx = 1;
			while (parent != null && pathIdx < path.length) {
				// Navigate into bean according to the path.
				String name = path[pathIdx];
				if (name.length() == 0) {
					out.write(StringUtil.strToAsciiByte("Invalid Path started at "
							+ pathIdx + " length 0\n"));
					break;
				}
				// Check collection.
				String realName = "";
				int idx = -1;
				int st = name.indexOf('[');
				if (st > 0) {
					int et = name.indexOf(']');
					if (et < st) {
						out.write(StringUtil.strToAsciiByte("Invalid Path started at "
								+ pathIdx + "." + name + "\n"));
						break;
					}
					realName = name.substring(0, st);
					try {
						idx = Integer.parseInt(name.substring(st + 1, et));
					} catch (Exception e) {
						out.write(StringUtil.strToAsciiByte("Invalid Index at "
								+ pathIdx + "." + name + "\n"));
						break;
					}
				}
				// Check Map.
				try {
					if (idx == -1) {
						Field f = FieldUtil.getField(parent.getClass(), name);
						f.setAccessible(true);
						parent = f.get(parent);
					} else {
						Field f = FieldUtil.getField(parent.getClass(), realName);
						f.setAccessible(true);
						parent = f.get(parent);
						if (parent instanceof Collection<?>) {
							Collection<? extends Object> os = (Collection<?>) parent;
							if (os.size() <= idx) {
								out.write(StringUtil.strToAsciiByte("Invalid Path started at "
										+ pathIdx + "." + name + " Array Out of Bound.\n"));
								break;
							}
							Iterator<? extends Object> iter = os.iterator();
							for (int i = 0; i < idx; ++i) {
								iter.next();
							}
							parent = iter.next();
						} else if (parent.getClass().isArray()) {
							if (Array.getLength(parent) <= idx) {
								out.write(StringUtil.strToAsciiByte("Invalid Path started at "
										+ pathIdx + "." + name + " Array Out of Bound.\n"));
								break;
							}
							parent = Array.get(parent, idx);
						} else {
							out.write(StringUtil.strToAsciiByte("Invalid Path started at "
									+ pathIdx + "." + name + " Not Array.\n"));
							break;
						}
					}
					++pathIdx;
				} catch (Exception e) {
					out.write(StringUtil.strToAsciiByte("Invalid Path started at "
							+ pathIdx + "." + name + "\n"));
					break;
				}
			}
			if (parent == null) {
				out.write(StringUtil.strToAsciiByte("Path Not Found.\n"));
				continue;
			}
			if (pathIdx < path.length) {
				continue;
			}
			Executer ex = COMMAND_MAP.get(comm);
			ex.execute(parent, out, args);
		}

	}

}
