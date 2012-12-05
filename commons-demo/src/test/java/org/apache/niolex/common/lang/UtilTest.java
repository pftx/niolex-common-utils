/**
 * UtilTest.java
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
package org.apache.niolex.common.lang;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-7-17
 */
public class UtilTest {

	private static String getLocalAddr() {
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
            	NetworkInterface ifc = interfaces.nextElement();
            	if (ifc.isLoopback() || ifc.isVirtual() || !ifc.isUp()) {
            		continue;
            	}
            	Enumeration<InetAddress> addressesOfAnInterface = ifc
            			.getInetAddresses();

            	while (addressesOfAnInterface.hasMoreElements()) {
            		InetAddress address = addressesOfAnInterface.nextElement();
            		if (address.isSiteLocalAddress()) {
            			return address.getHostAddress();
            		}
            	}
            }
        } catch (SocketException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }


	@Test
	public void tem() throws Throwable {
		System.out.println("Rem " + getLocalAddr());
		System.out.println("Rem " + InetAddress.getLocalHost().getHostAddress());
	}
}
