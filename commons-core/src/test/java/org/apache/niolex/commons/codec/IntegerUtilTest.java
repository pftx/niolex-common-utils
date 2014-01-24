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
public class IntegerUtilTest extends IntegerUtil {

    @Test
    public void testFourBytes() throws Exception {
        int i = IntegerUtil.fourBytes((byte)1, (byte)3, (byte)4, (byte)5);
        assertEquals(i, 16974853);
    }

    @Test
    public void testFourBytes2() throws Exception {
        int i = IntegerUtil.fourBytes((byte)0x20, (byte)0xb7, (byte)0x6a, (byte)0x73);
        assertEquals(i, 548891251);
    }

    @Test
    public void testFourBytesArr() throws Exception {
        byte[] arr = new byte[4];
        for (int i = 0; i < 10000; i += 245) {
            encFourBytes(i, arr, 0);
            assertEquals(i, fourBytes(arr, 0));
        }
        for (int i = 65535, j = 0; i < 6553500; i += 245, ++j) {
            encFourBytes(i, arr, 0);
            assertEquals(i, fourBytes(arr, 0));
            if (j % 100 == 0)
                i += 65535;
        }
        for (int i = 16777215; i < 167772160; i += 67595) {
            encFourBytes(i, arr, 0);
            assertEquals(i, fourBytes(arr, 0));
        }
        for (byte i = -128; i < 127; ++i) {
            arr[0] = i;
            arr[1] = i;
            arr[2] = i;
            arr[3] = i;
            int j = fourBytes(arr, 0);
            for (int k = 0; k < 4; ++k) {
                assertEquals(i, (byte)(j));
                j = j >> 8;
            }
        }
        arr = toFourBytes(Integer.MAX_VALUE);
        assertEquals(127, arr[0]);
        assertEquals(-1, arr[1]);
        assertEquals(-1, arr[2]);
        assertEquals(-1, arr[3]);
        arr = toFourBytes(Integer.MIN_VALUE);
        assertEquals(-128, arr[0]);
        assertEquals(0, arr[1]);
        assertEquals(0, arr[2]);
        assertEquals(0, arr[3]);
    }

    @Test
    public void testEncFourBytes() throws Exception {
        for (int i = 548891251; i < 548956786; i += 256) {
            byte[] arr = toFourBytes(i);
            assertEquals(arr[2], (byte)(i / 256));
        }
    }

    @Test
    public void testToFourBytes() throws Exception {
        for (int i = 548891251; i < 548891507; ++i) {
            byte[] arr = toFourBytes(i);
            assertEquals(arr[3], (byte)(i % 256));
        }
    }

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

    @Test
    public void testFromSize() throws Exception {
        assertEquals(IntegerUtil.fromSize("123"), 123, 0.0000001);
        assertEquals(IntegerUtil.fromSize("1K"), 1024, 0.0000001);
        assertEquals(IntegerUtil.fromSize("1.0009765625k"), 1025, 0.0000001);
        assertEquals(IntegerUtil.fromSize("1.002K"), 1026.048, 0.0000001);
        assertEquals(IntegerUtil.fromSize("1.01K"), 1034.24, 0.0000001);
        assertEquals(IntegerUtil.fromSize("1.018K"), 1042.432, 0.0000001);
        // -- M
        assertEquals(IntegerUtil.fromSize("1M"), 1048576, 0.0000001);
        assertEquals(IntegerUtil.fromSize("1.8M"), 1887436.8, 0.0000001);
        assertEquals(IntegerUtil.fromSize("1.01049041748046875M"), 1059576, 0.0000001);
        assertEquals(IntegerUtil.fromSize("1.96M"), 2055208.96, 0.0000001);
        assertEquals(IntegerUtil.fromSize("2.97M"), 3114270.72, 0.0000001);
        // -- G
        assertEquals(IntegerUtil.fromSize("1G"), 1073741824, 0.0000001);
        assertEquals(IntegerUtil.fromSize("1.83G"), 1964947537.92, 0.0000001);
        // -- T
        assertEquals(IntegerUtil.fromSize("1T"), 1099511627776L, 0.0000001);
        assertEquals(IntegerUtil.fromSize("1.82T"), 2001111627776L, 1E8);
        assertEquals(IntegerUtil.fromSize("1.67T"), 1836123616061L, 1e8);
    }

    @Test
    public void testIsIn() throws Exception {
        assertFalse(IntegerUtil.isIn(5, 6));
        assertTrue(IntegerUtil.isIn(5, 6, 7, 5));
    }

    @Test
    public void testIsInEmpty() throws Exception {
        assertFalse(IntegerUtil.isIn(5));
    }

}
