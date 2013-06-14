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

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * We ignore all the exceptions in this class.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-6-14
 */
public class IgnoreException {

    /**
     * Get all the network interfaces. If exception occurred, we return an empty enumeration.
     *
     * @return the enumeration
     */
    public static Enumeration<NetworkInterface> getNetworkInterfaces() {
        try {
            return NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            // We return an empty enumeration.
            return  new Enumeration<NetworkInterface>() {

                @Override
                public boolean hasMoreElements() {
                    return false;
                }

                @Override
                public NetworkInterface nextElement() {
                    throw new NoSuchElementException();
                }

            };
        }
    }

    public static boolean isNetworkInterfaceUp(NetworkInterface ifc) {
        try {
            return ifc.isUp();
        } catch (SocketException e) {
            /*We Don't Care*/
            return false;
        }
    }

}
