/**
 * INetUtil.java
 *
 * Copyright 2013 the original author or authors.
 *
 * We licenses this file to you under the Apache License, version 2.0
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
package org.apache.niolex.commons.net;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import org.apache.niolex.commons.internal.IgnoreException;
import org.apache.niolex.commons.test.Check;

/**
 * Internet related utilities.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-8-9
 */
public abstract class NetUtil extends Check {

    /**
     * Get all the local Internet addresses.
     *
     * @return the result
     */
    public static final Set<InetAddress> getAllLocalAddresses() {
        Enumeration<NetworkInterface> interfaces = null;
        Set<InetAddress> set = new HashSet<InetAddress>();
        // Get All the network card interfaces
        interfaces = IgnoreException.getNetworkInterfaces();
        // iterate them
        while (interfaces.hasMoreElements()) {
            NetworkInterface ifc = interfaces.nextElement();
            if (!IgnoreException.isNetworkInterfaceUp(ifc)) {
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
     * @throws UnknownHostException - if no IP address for the host could be found.
     */
    public static final String getLocalIP() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    /**
     * Format the InetSocketAddress into IP:Port string.
     *
     * @param addr the InetSocketAddress to be formatted
     * @return the result string
     */
    public static final String inetSocketAddress2IpPort(InetSocketAddress addr) {
        String s = addr.toString();
        int idx = s.indexOf('/') + 1;
        return s.substring(idx);
    }

    /**
     * Convert the IP address from textual presentation to int.
     *
     * @param ip the IP address string in textual presentation
     * @return the IP in integer presentation
     * @throws IllegalArgumentException if Invalid IP format.
     */
    public int ipToInt(String ip) {
        notNull(ip, "The parameter ip is null.");
        String[] nums = ip.split("\\.");
        String msg = "Invalid IP format: " + ip;
        eq(nums.length, 4, msg);
        int res = 0;
        for (int i = 0; i < 4; ++i) {
            try {
                int k = Integer.parseInt(nums[i]);
                if (k < 0 || k > 255) {
                    throw new IllegalArgumentException(msg);
                }
                res = (res << 8) + k;
            } catch (Throwable t) {
                throw new IllegalArgumentException(msg, t);
            }
        }
        return res;
    }

    /**
     * Convert an IP from int to textual presentation.
     *
     * @param intIP the IP in integer presentation
     * @return the IP in textual presentation
     */
    public String intToIP(int intIP) {
        int[] nums = new int[4];
        for (int i = 3; i > -1; --i) {
            nums[i] = intIP & 0xff;
            intIP >>= 8;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; ++i) {
            sb.append(nums[i]).append('.');
        }
        sb.append(nums[3]);
        return sb.toString();
    }

}
