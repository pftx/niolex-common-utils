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
import junit.framework.Assert;

import org.apache.niolex.commons.codec.Base16Util;
import org.junit.Test;


public class Base16UtilTest {

    @Test
    public void testBase16() {
        byte[] a = Base16Util.base16toByte("acdefb");
        String s = Base16Util.byteToBase16(a);
        System.out.println("s => " + s);
        Assert.assertEquals("acdefb", s);
    }

    @Test
    public void testBase16_int() {
        int k = 0xacdefb82;
        byte[] b = new byte[4];
        b[0] = (byte) (k >> 24 & 0xFF);
        b[1] = (byte) (k >> 16 & 0xFF);
        b[2] = (byte) (k >> 8 & 0xFF);
        b[3] = (byte) (k & 0xFF);
        String s = Base16Util.byteToBase16(b);
        System.out.println("s => " + s);
        Assert.assertEquals("acdefb82", s);
    }

    @Test
    public void testBase16_empty() {
        byte[] a = Base16Util.base16toByte("");
        String s = Base16Util.byteToBase16(a);
        System.out.println("s => " + s);
        System.out.println("a.length => " + a.length);
        Assert.assertEquals("", s);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testBase16_invalid() {
    	Base16Util.base16toByte(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testBase16_invalid_2() {
    	Base16Util.base16toByte(null);
    }

    @Test
    public void testBase16_invalid_3() {
    	assertArrayEquals(new byte[0], Base16Util.base16toByte("abc"));
    }
}
