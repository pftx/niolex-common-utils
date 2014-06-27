/**
 * NetUtilTest.java
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


import static org.junit.Assert.*;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Set;

import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-8-9
 */
public class NetUtilTest extends NetUtil {

    /**
     * Test method for {@link org.apache.niolex.commons.net.NetUtil#getAllLocalAddresses()}.
     * @throws Exception
     */
    @Test
    public void testGetAllLocalAddresses() throws Exception {
        Set<InetAddress> set = getAllLocalAddresses();
        for (InetAddress i : set) {
            System.out.println("ALL " + i + ", " + i.isSiteLocalAddress());
        }
        InetAddress i = InetAddress.getLocalHost();
        System.out.println("Loc " + i + ", " + i.isSiteLocalAddress());
        InetAddress test = InetAddress.getByName("localhost");
        assertTrue(set.contains(test));
    }

    @Test
    public void testGetLocalIP() throws Exception {
        InetAddress i = InetAddress.getByName("localhost");
        String s = getLocalIP();
        System.out.println(i + ":= " + s);
    }

    @Test
    public void testInetSocketAddress2IpPort() throws Exception {
        String s = inetSocketAddress2IpPort(new InetSocketAddress("1.2.3.4", 808));
        System.out.println("InetSocketAddress[1.2.3.4] = " + s);
        assertEquals("1.2.3.4:808", s);
        s = inetSocketAddress2IpPort(new InetSocketAddress("localhost", 808));
        System.out.println("InetSocketAddress[localhost] = " + s);
        assertEquals("127.0.0.1:808", s);
    }

    @Test
    public void testIpPort2InetSocketAddress() throws Exception {
        InetSocketAddress in = ipPort2InetSocketAddress("1.2.3.4:9088");
        assertEquals(9088, in.getPort());
        assertEquals("1.2.3.4", in.getAddress().getHostAddress());
    }

    @Test
    public void testIpPort2InetSocketAddressLong() throws Exception {
        InetSocketAddress in = ipPort2InetSocketAddress("10.1.2.4:88");
        assertEquals(88, in.getPort());
        assertEquals("10.1.2.4", in.getAddress().getHostAddress());
    }

    @Test
    public void testIpPort2InetSocketAddressEx1() throws Exception {
        try {
            ipPort2InetSocketAddress("10.35.2.4:0x88");
        } catch (Exception e) {
            assertTrue(e instanceof NumberFormatException);
            assertEquals("For input string: \"0x88\"", e.getMessage());
        }
    }

    @Test
    public void testIpPort2InetSocketAddressEx2() throws Exception {
        try {
            ipPort2InetSocketAddress("10.35.2.f4:0x88");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
            assertEquals("Invalid IP format: 10.35.2.f4", e.getMessage());
        }
    }

    @Test
    public void testIpPort2InetSocketAddressEx3() throws Exception {
        try {
            ipPort2InetSocketAddress("10.35.2.135");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
            assertEquals("Invalid IP:Port string - 10.35.2.135", e.getMessage());
        }
    }

    @Test
    public void testGetByAddress() throws Exception {
        try {
            getByAddress(new byte[3]);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
            assertEquals("Failed to create InetAddress.", e.getMessage());
        }
    }

    @Test
    public void testIpToInt() throws Exception {
        int i = ipToInt("1.2.3.4");
        String s = Integer.toHexString(i);
        System.out.println(s);
        assertEquals(16909060, i);
    }

    @Test
    public void testIpToIntAnother() throws Exception {
        int i = ipToInt("16.17.3.4");
        String s = Integer.toHexString(i);
        assertEquals("10110304", s);
    }

    @Test
    public void testIpToIntMax() throws Exception {
        assertEquals(-1, ipToInt("255.255.255.255"));
        assertEquals(-2, ipToInt("255.255.255.254"));
        assertEquals(-257, ipToInt("255.255.254.255"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testIpToIntGt255() throws Exception {
        ipToInt("16.17.3.256");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testIpToIntLt0() throws Exception {
        ipToInt("16.17.3.-2");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testIpToIntNotInt() throws Exception {
        ipToInt("16.17.3.a2b");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testIpToIntNull() throws Exception {
        ipToInt(null);
    }

    @Test
    public void testIntToIP() throws Exception {
        String ip = intToIP(0xf310d0a);
        assertEquals("15.49.13.10", ip);
    }

    @Test
    public void testIntToIPAnother() throws Exception {
        String ip = intToIP(0x7f000001);
        assertEquals("127.0.0.1", ip);
    }

}
