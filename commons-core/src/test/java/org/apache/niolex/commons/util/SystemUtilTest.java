/**
 * SystemUtilTest.java
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Set;

import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-8-1
 */
public class SystemUtilTest {

	/**
	 * Test method for {@link org.apache.niolex.commons.util.SystemUtil#getAllLocalAddresses()}.
	 * @throws Exception
	 */
	@Test
	public void testGetAllLocalAddresses() throws Exception {
		Set<InetAddress> set = SystemUtil.getAllLocalAddresses();
		for (InetAddress i : set) {
		    System.out.println(i + ", " + i.isSiteLocalAddress());
		}
		InetAddress i = InetAddress.getLocalHost();
		System.out.println(i + ", " + i.isSiteLocalAddress());
		InetAddress test = InetAddress.getByName("localhost");
		assertTrue(set.contains(test));
	}

	@Test
	public void testSleep() throws Exception {
		SystemUtil.sleep(4);
	}

	@Test
	public void testSleepErr() throws Exception {
		new SystemUtil() {};
		SystemUtil.sleep(-1);
	}

    @Test
    public void testCloseOk() throws Exception {
        SystemUtil.close(mock(Closeable.class));
        Closeable ab = null;
        SystemUtil.close(ab);
    }

    @Test
    public void testCloseSo() throws Exception {
        SystemUtil.close(mock(Socket.class));
        Socket ab = null;
        SystemUtil.close(ab);
    }

    @Test
    public void testCloseErr() throws Exception {
        Closeable ab = mock(Closeable.class);
        doThrow(new IOException("This is from x.j.y")).when(ab).close();
        assertEquals("This is from x.j.y", SystemUtil.close(ab).getMessage());
    }

    @Test
    public void testCloseSoErr() throws Exception {
        Socket ab = mock(Socket.class);
        doThrow(new IOException("This is from x.j.y")).when(ab).close();
        assertEquals("This is from x.j.y", SystemUtil.close(ab).getMessage());
    }

    @Test
    public void testInetSocketAddress2IpPort() throws Exception {
        String s = SystemUtil.inetSocketAddress2IpPort(new InetSocketAddress("1.2.3.4", 808));
        System.out.println("InetSocketAddress[1.2.3.4] = " + s);
        assertEquals("1.2.3.4:808", s);
        s = SystemUtil.inetSocketAddress2IpPort(new InetSocketAddress("localhost", 808));
        System.out.println("InetSocketAddress[localhost] = " + s);
        assertEquals("127.0.0.1:808", s);
    }

    @Test
    public void testGetSystemProperty() throws Exception {
        String s = SystemUtil.getSystemProperty("user.home", "usr.home");
        System.out.println("home = " + s);
        s = SystemUtil.getSystemProperty("usr.home", "java.CLASSPATH");
        System.out.println("home = " + s);
    }

    @Test
    public void testGetLocalIP()
     throws Exception {
        InetAddress i = InetAddress.getByName("localhost");
        String s = SystemUtil.getLocalIP();
        System.out.println(i + ", " + s);
    }

}
