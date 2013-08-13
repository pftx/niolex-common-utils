/**
 * IgnoreException.java
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
package org.apache.niolex.commons.internal;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * We ignore all the exceptions in this class.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-6-14
 */
public class IgnoreException {

    /**
     * Get the KeyFactory for the specific algorithm.
     *
     * @param algorithm the algorithm you want.
     * @return the algorithm KeyFactory
     * @throws IllegalStateException If Your JDK don't support this algorithm.
     */
    public static KeyFactory getKeyFactory(String algorithm) {
        try {
            return KeyFactory.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Your JDK don't support " + algorithm);
        }
    }

    /**
     * Get all the local Internet addresses. If exception occurred, we return an empty set.
     *
     * @return the result
     */
    public static final Set<InetAddress> getAllLocalAddresses() {
        Set<InetAddress> set = new HashSet<InetAddress>();
        try {
            // Get All the network card interfaces
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            // iterate them
            while (interfaces.hasMoreElements()) {
                NetworkInterface ifc = interfaces.nextElement();
                if (!isNetworkInterfaceUp(ifc)) {
                    // If it's down, there is nothing we can do.
                    continue;
                }
                Enumeration<InetAddress> addressesOfAnInterface = ifc.getInetAddresses();
                while (addressesOfAnInterface.hasMoreElements()) {
                    InetAddress address = addressesOfAnInterface.nextElement();
                    set.add(address);
                }
            }
        } catch (Exception e) {
            // We do nothing when exception occurred.
        }
        return set;
    }

    /**
     * Test whether the network interface is up or not.
     *
     * @param ifc the network interface to test
     * @return true if it's up
     */
    public static boolean isNetworkInterfaceUp(NetworkInterface ifc) {
        try {
            return ifc.isUp();
        } catch (Exception e) {
            /*We Don't Care*/
            return false;
        }
    }

    /**
     * Get the Charset by the charset name in string format.
     *
     * @param charsetName the charset name in string format.
     * @return the Charset, null if not found.
     */
    public static Charset getCharset(String charsetName) {
        try {
            return Charset.forName(charsetName);
        } catch (Exception e) {
            return null;
        }
    }

}
