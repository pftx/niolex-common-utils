/**
 * IntegerUtilTest.java
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
package org.apache.niolex.commons.codec;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2012-11-11$
 */
public class IntegerUtilTest {

	/**
	 * Test method for {@link org.apache.niolex.commons.codec.IntegerUtil#threeBytes(byte, byte, byte)}.
	 */
	@Test
	public final void testThreeBytes() {
		int i = IntegerUtil.threeBytes((byte)3, (byte)4, (byte)5);
		assertEquals(i, 197637);
		i = IntegerUtil.threeBytes((byte)0xf4, (byte)0xf6, (byte)0xf8);
		assertEquals(i, 0xf4f6f8);
		i = IntegerUtil.threeBytes((byte)0xff, (byte)0xf6, (byte)0xf8);
		assertEquals(i, 0xfff6f8);
		i = IntegerUtil.threeBytes((byte)0xff, (byte)0xff, (byte)0xf8);
		assertEquals(i, 0xfffff8);
		i = IntegerUtil.threeBytes((byte)0xff, (byte)0xff, (byte)0xff);
		assertEquals(i, 0xffffff);
		i = IntegerUtil.threeBytes((byte)0xff, (byte)0, (byte)0);
		assertEquals(i, 0xff0000);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.codec.IntegerUtil#encThreeBytes(int, byte[], int)}.
	 */
	@Test
	public final void testEncThreeBytes1() {
		byte[] arr = new byte[3];
		IntegerUtil.encThreeBytes(0x3f6f7f8, arr, 0);
		assertEquals(arr[0], (byte)0xf6);
		assertEquals(arr[1], (byte)0xf7);
		assertEquals(arr[2], (byte)0xf8);
	}

	@Test
	public final void testEncThreeBytes2() {
		byte[] arr = new byte[3];
		IntegerUtil.encThreeBytes(0x78, arr, 0);
		assertEquals(arr[0], (byte)0x0);
		assertEquals(arr[1], (byte)0x0);
		assertEquals(arr[2], (byte)0x78);
	}

	@Test
	public final void testEncThreeBytes3() {
		byte[] arr = new byte[3];
		IntegerUtil.encThreeBytes(0x7700, arr, 0);
		assertEquals(arr[0], (byte)0x0);
		assertEquals(arr[1], (byte)0x77);
		assertEquals(arr[2], (byte)0x00);
	}

	@Test
	public final void testEncThreeBytes4() {
		byte[] arr = new byte[3];
		IntegerUtil.encThreeBytes(0xff0000, arr, 0);
		assertEquals(arr[0], (byte)0xff);
		assertEquals(arr[1], (byte)0x00);
		assertEquals(arr[2], (byte)0x00);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.codec.IntegerUtil#twoBytes(byte, byte)}.
	 */
	@Test
	public final void testTwoBytes() {
		int i = IntegerUtil.twoBytes((byte)0xf6, (byte)0xf8);
		assertEquals(i, 0xf6f8);
	}

	@Test
	public final void testTwoBytes2() {
		int i = IntegerUtil.twoBytes((byte)0x0, (byte)0x38);
		assertEquals(i, 0x38);
	}

	@Test
	public final void testTwoBytes3() {
		int i = IntegerUtil.twoBytes((byte)0xff, (byte)0x38);
		assertEquals(i, 0xff38);
	}

	@Test
	public final void testTwoBytes4() {
		int i = IntegerUtil.twoBytes((byte)0xf, (byte)0x38);
		assertEquals(i, 0xf38);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.codec.IntegerUtil#encTwoBytes(int, byte[], int)}.
	 */
	@Test
	public final void testEncTwoBytes() {
		byte[] arr = new byte[2];
		IntegerUtil.encTwoBytes(0x789ab, arr, 0);
		assertEquals(arr[0], (byte)0x89);
		assertEquals(arr[1], (byte)0xab);
	}

	@Test
	public final void testEncTwoBytes2() {
		byte[] arr = new byte[2];
		IntegerUtil.encTwoBytes(0x6, arr, 0);
		assertEquals(arr[0], (byte)0x0);
		assertEquals(arr[1], (byte)0x6);
	}

	@Test
	public final void testEncTwoBytes3() {
		byte[] arr = new byte[2];
		IntegerUtil.encTwoBytes(0x126, arr, 0);
		assertEquals(arr[0], (byte)0x1);
		assertEquals(arr[1], (byte)0x26);
	}

	@Test
	public final void testEncTwoBytes4() {
		byte[] arr = new byte[2];
		IntegerUtil.encTwoBytes(0xffff, arr, 0);
		assertEquals(arr[0], (byte)0xff);
		assertEquals(arr[1], (byte)0xff);
	}

	/**
	 * Test formatSize
	 *
	 * @throws Exception
	 */
    @Test
    public void testFormatSize() throws Exception {
        assertEquals("123", IntegerUtil.formatSize(123));
        assertEquals("1K", IntegerUtil.formatSize(1024));
        assertEquals("1K", IntegerUtil.formatSize(1025));
        assertEquals("1K", IntegerUtil.formatSize(1026));
        assertEquals("1.01K", IntegerUtil.formatSize(1034));
        assertEquals("1.01K", IntegerUtil.formatSize(1036));
        // -- M
        assertEquals("1M", IntegerUtil.formatSize(1048576));
        assertEquals("1M", IntegerUtil.formatSize(1049576));
        assertEquals("1.01M", IntegerUtil.formatSize(1059576));
        assertEquals("1.96M", IntegerUtil.formatSize(2059576));
        assertEquals("1.97M", IntegerUtil.formatSize(2060452));
        // -- G
        assertEquals("1G", IntegerUtil.formatSize(1073741824));
        assertEquals("1G", IntegerUtil.formatSize(1074741824));
        // -- T
        assertEquals("1T", IntegerUtil.formatSize(1099511627776L));
        assertEquals("1.82T", IntegerUtil.formatSize(2000511627776L));
        assertEquals("1.67T", IntegerUtil.formatSize(1831236616061L));
        String s;
        s = IntegerUtil.formatSize(1234);
        //System.out.println(s);
        assertEquals("1.21K", s);
    }

}
