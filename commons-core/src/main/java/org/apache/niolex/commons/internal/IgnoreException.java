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
     * Populate all local addresses and store them into the specified set.
     *
     * @param set the set to store the results
     */
    public static void populateLocalAddresses(Set<InetAddress> set) {
        try {
            // Get All the network card interfaces
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            // iterate them
            while (interfaces.hasMoreElements()) {
                NetworkInterface nFace = interfaces.nextElement();
                if (!isNetworkInterfaceUp(nFace)) {
                    // If it's down, there is nothing we can do.
                    continue;
                }
                Enumeration<InetAddress> addresses = nFace.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    set.add(address);
                }
            }
        } catch (Exception e) {
            // We do nothing when exception occurred.
        }
    }

    /**
     * Test whether the network interface is up or not.
     *
     * @param netFace the network interface to test
     * @return true if it's up
     */
    public static boolean isNetworkInterfaceUp(NetworkInterface netFace) {
        try {
            return netFace.isUp();
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
