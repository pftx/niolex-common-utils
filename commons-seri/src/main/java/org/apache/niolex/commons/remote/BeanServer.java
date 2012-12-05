/**
 * BeanServer.java
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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class will export beans to remote telnet, you can get, list and set properties.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-7-25
 */
public class BeanServer implements Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(BeanServer.class);

	// The bean map.
	private final ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<String, Object>();

	// As the name indicates.
	private ServerSocket listenerSocket;
	// As the name indicates.
    private volatile boolean isListening = false;
    // As the name indicates.
	private int port = 8597;

	/**
     * If the specified key is not already associated with a value, associate it with the given value. This is equivalent to
     *
     *   if (!map.containsKey(key))
     *       return map.put(key, value);
     *   else
     *       return map.get(key);
     *
     * except that the action is performed atomically.
     *
     * @return the previous value associated with the specified key,
     *         or <tt>null</tt> if there was no mapping for the key
     * @throws NullPointerException if the specified key or value is null
     */
	public Object putIfAbsent(String key, Object value) {
		return map.putIfAbsent(key, value);
	}

	/**
	 * Removes the key (and its corresponding value) from this map. This method does nothing if the key is not in the map.
	 *
	 * @param key
	 * @return the previous value associated with key, or null if there was no mapping for key
	 */
	public Object remove(Object key) {
		return map.remove(key);
	}

	/**
	 * Replaces the entry for a key only if currently mapped to a given value. This is equivalent to
	 *
	 *    if (map.containsKey(key) && map.get(key).equals(oldValue)) {
	 *        map.put(key, newValue);
	 *        return true;
	 *    } else return false;
	 * except that the action is performed atomically.
	 *
	 * @param key
	 * @param oldValue
	 * @param newValue
	 * @return
	 */
	public boolean replace(String key, Object oldValue, Object newValue) {
		return map.replace(key, oldValue, newValue);
	}

	/**
	 * Start this bean server to listen to telnet request.
	 * @return
	 */
	public boolean start() {
		try {
            listenerSocket = new ServerSocket(port);
            // Setting the timeout for accept method. Avoid can not be shuting
            // down since blocking thread when waiting accept.
            listenerSocket.setSoTimeout(10000);
            // Start Listening
            isListening = true;
            Thread s = new Thread(this, "BeanServer");
            s.setDaemon(true);
            s.start();
            LOG.info("BeanServer started at port " + port);
            return true;
        } catch (IOException e) {
            LOG.error("Can not start the bean server", e);
        }
		return false;
	}

	/**
	 * Stop this bean server.
	 */
	public void stop() {
		isListening = false;
	}

	/**
	 * Do the socket listening, process connection.
	 *
	 * Override super method
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
        while (isListening) {
            try {
                Socket socket = listenerSocket.accept();
                ConnectionWorker connection = new ConnectionWorker(socket, map);
                Thread c = new Thread(connection, "BeanServer.ConnectionWorker");
                c.setDaemon(true);
                c.start();
            } catch (SocketTimeoutException e) {
                // Ignore the timeout exception thrown by accept.
            } catch (IOException e) {
            	LOG.error("Can not build the connection with client.", e);
            }
        }
        LOG.info("BeanServer stoped.");
	}

	/**
	 * Set the port to listen to.
	 * @param port
	 */
	public void setPort(int port) {
		this.port = port;
	}

}
