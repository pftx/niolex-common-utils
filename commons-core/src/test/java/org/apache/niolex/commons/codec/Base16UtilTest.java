/**
 * Base16UtilTest.java
 *
 * Copyright 2011 Niolex, Inc.
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
package org.apache.niolex.commons.codec;

import static org.junit.Assert.assertArrayEquals;

import org.apache.niolex.commons.file.FileUtil;
import org.apache.niolex.commons.file.FileUtilTest;
import org.apache.niolex.commons.test.MockUtil;
import org.apache.niolex.commons.util.SystemUtil;
import org.junit.Assert;
import org.junit.Test;

public class Base16UtilTest extends Base16Util {

    public static final String bak_byteToBase16(byte[] bytes) {
        if (bytes == null)
            throw new IllegalArgumentException("The parameter should not be null!");
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(Integer.toHexString((b & 0xF0) >> 4));
            sb.append(Integer.toHexString(b & 0x0F));
        }
        return sb.toString();
    }

    public static final byte[] bak_base16toByte(String str) {
        if (str == null)
            throw new IllegalArgumentException("The parameter should not be null!");
        if (str.length() % 2 != 0) {
            throw new IllegalArgumentException("The parameter length should be even!");
        } else {
            char[] hex = str.toCharArray();
            byte[] bytes = new byte[hex.length / 2];
            for (int i = 0, j = 0; i < hex.length; i += 2, ++j) {
                bytes[j] = (byte) ((Integer.parseInt(String.valueOf(hex[i]), 16) << 4) | (Integer.parseInt(String
                        .valueOf(hex[i + 1]), 16)));
            }
            return bytes;
        }
    }

    @Test
    public void testBase16() {
        byte[] a = Base16Util.base16ToByte("acdefb");
        String s = Base16Util.byteToBase16(a);
        System.out.println("as => " + s);
        Assert.assertEquals("acdefb", s);
    }

    @Test
    public void testBase16Max() {
        byte[] a = Base16Util.base16ToByte("ffffffff");
        String s = Base16Util.byteToBase16(a);
        System.out.println("fs => " + s);
        Assert.assertEquals("ffffffff", s);
    }

    byte[] d = FileUtil.getBinaryFileContentFromClassPath("Data.txt", FileUtilTest.class);

    @Test
    public void testSpeed() {
        long in, t1, t2;
        for (int i = 0; i < 10; ++i) {
            in = System.nanoTime();
            first(d);
            t1 = System.nanoTime() - in;
            in = System.nanoTime();
            second(d);
            t2 = System.nanoTime() - in;
            SystemUtil.println("Ratio1 : %.2f\n", ((double)t1 / t2));
        }
    }

    public void first(byte[] data) {
        bak_byteToBase16(data);
    }

    public void second(byte[] data) {
        Base16Util.byteToBase16(data);
    }

    @Test
    public void testSpeed2() {
        long in, t1, t2;
        String s = Base16Util.byteToBase16(d);
        for (int i = 0; i < 10; ++i) {
            in = System.nanoTime();
            first(s);
            t1 = System.nanoTime() - in;
            in = System.nanoTime();
            second(s);
            t2 = System.nanoTime() - in;
            SystemUtil.println("Ratio2 : %.2f\n", ((double)t1 / t2));
        }
    }

    public void first(String s) {
        for (int i = 0; i < 10; ++i) {
            bak_base16toByte(s);
        }
    }

    public void second(String s) {
        for (int i = 0; i < 10; ++i) {
            Base16Util.base16ToByte(s);
        }
    }

    @Test
    public void testBase16_int() {
        testInt(0x7cdefb82);
        testInt(0x40f71235);
        testInt(0x46789abc);
        testInt(0x4defedbc);
        testInt(0x400ef13c);
        testInt(0x92324542);
        testInt(0xfdca6592);
        testInt(0x908070ce);
    }

    public void testInt(int k) {
        byte[] b = new byte[4];
        b[0] = (byte) (k >> 24 & 0xFF);
        b[1] = (byte) (k >> 16 & 0xFF);
        b[2] = (byte) (k >> 8 & 0xFF);
        b[3] = (byte) (k & 0xFF);
        String s = Base16Util.byteToBase16(b);
        Assert.assertEquals(Integer.toHexString(k), trim0(s));
        byte[] o = Base16Util.base16ToByte(s);
        Assert.assertArrayEquals(b, o);
    }

    public String trim0(String s) {
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) != '0') return s.substring(i);
        }
        return "";
    }

    @Test
    public void testBase16_empty() {
        byte[] a = Base16Util.base16ToByte("");
        String s = Base16Util.byteToBase16(a);
        System.out.println("ps => " + s);
        System.out.println("pa.length => " + a.length);
        Assert.assertEquals("", s);
    }

    @Test
    public void testBase16_Whole() {
        for (int i = 60000; i < 65535; ++i) {
            testInt(i);
        }
    }

    @Test
    public void testBase16_invalid() {
        try {
            Base16Util.base16ToByte(null);
        } catch (IllegalArgumentException e) {
            return;
        }
        Assert.assertTrue(false);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testBase16_invalid_1() {
    	Base16Util.byteToBase16(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testBase16_invalid_20() {
        Base16Util.base16ToByte("1中2文");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testBase16_invalid_21() {
        Base16Util.base16ToByte("中1文2");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testBase16_invalid_22() {
        Base16Util.base16ToByte("12中文");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testBase16_invalid_23() {
        Base16Util.base16ToByte("中文");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testBase16_invalid_3() {
        assertArrayEquals(new byte[0], Base16Util.base16ToByte("abc"));
    }

    @Test
    public void testBase16Correct() {
        for (int i = 0; i < 1000; ++i) {
            int len = MockUtil.randInt(10, 57);
            byte[] in = MockUtil.randByteArray(len);
            String s = Base16Util.byteToBase16(in);
            byte[] out = base16ToByte(s);
            assertArrayEquals(in, out);
        }
    }
}
