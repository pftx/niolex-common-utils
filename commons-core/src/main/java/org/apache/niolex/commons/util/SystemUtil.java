/**
 * SystemUtil.java
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
package org.apache.niolex.commons.util;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * System information related utility class.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, Date: 2012-8-1
 */
public class SystemUtil {

	/**
	 * Get all the local Internet addresses.
	 * @return the result
	 */
	public static final Set<InetAddress> getAllLocalAddresses() {
		Enumeration<NetworkInterface> interfaces = null;
		Set<InetAddress> set = new HashSet<InetAddress>();
        try {
        	// Get All the network card interfaces
            interfaces = NetworkInterface.getNetworkInterfaces();
            // iterate them
            while (interfaces.hasMoreElements()) {
            	NetworkInterface ifc = interfaces.nextElement();
            	if (!ifc.isUp()) {
            		// If it's down, there is nothing we can do.
            		continue;
            	}
            	Enumeration<InetAddress> addressesOfAnInterface = ifc
            			.getInetAddresses();
            	while (addressesOfAnInterface.hasMoreElements()) {
            		InetAddress address = addressesOfAnInterface.nextElement();
            		set.add(address);
            	}
            }
        } catch (SocketException e) {/*We Don't Care*/}
        return set;
	}

	/**
	 * Returns the IP address string in textual presentation.
	 * <p>
	 * We will first get the local host name, and then get the IP
	 * address by this host name.
	 * If there are multiple addresses returned, we peek the first
	 * one to return.
	 * If local host name not found, we will use 127.0.0.1
	 *
	 * @return the IP address string
	 * @throws UnknownHostException If the local host name is invalid
	 */
	public static final String getLocalIP() throws UnknownHostException {
	    return InetAddress.getLocalHost().getHostAddress();
	}

	/**
	 * Format the InetSocketAddress into IP:Port string.
	 *
	 * @param addr the InetSocketAddress to be formatted
	 * @return the string
	 */
	public static final String inetSocketAddress2IpPort(InetSocketAddress addr) {
	    String s = addr.toString();
	    int idx = s.indexOf('/') + 1;
	    return s.substring(idx);
	}

	/**
	 * Make the current thread sleep, do not care about the exception.
	 *
	 * @param milliseconds the time to sleep in milliseconds
	 */
	public static final void sleep(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (Exception e) {/*We Don't Care*/}
	}

	/**
	 * Safely close the Closeable, without throwing the exception.
	 *
	 * @param cl the object you want to close.
	 * @return null if success, the exception if exception occurred.
	 */
	public static final IOException close(Closeable cl) {
	    try {
	        if (cl != null) cl.close();
	        return null;
	    } catch (IOException e) {
	        return e;
	    }
	}

	/**
	 * Safely close the Socket, without throwing the exception.
	 *
	 * @param cl the socket you want to close.
	 * @return null if success, the exception if exception occurred.
	 */
	public static final IOException close(Socket cl) {
	    try {
	        if (cl != null) cl.close();
	        return null;
	    } catch (IOException e) {
	        return e;
	    }
	}

	/**
	 * Try to get system property according to the specified order of property
	 * keys.
	 *
	 * @param args the specified property keys
	 * @return the first found property, null if all keys not found
	 */
	public static final String getSystemProperty(String ...args) {
	    for (String s : args) {
	        String p = System.getProperty(s);
	        if (p != null) {
	            return p;
	        }
	    }
	    return null;
	}

}
