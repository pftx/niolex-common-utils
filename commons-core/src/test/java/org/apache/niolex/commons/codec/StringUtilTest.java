/**
 * StringUtilTest.java
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.niolex.commons.test.MockUtil;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-6-20
 */
public class StringUtilTest {

	/**
	 * Test method for {@link org.apache.niolex.commons.codec.StringUtil#utf8ByteToStr(byte[])}.
	 */
	@Test
	public void testUtf8ByteToStr() {
		byte[] a = MockUtil.randByteArray(16);
		String s = StringUtil.asciiByteToStr(a);
		System.out.println(s);
		for (Charset c : Charset.availableCharsets().values()) {
			System.out.println(c.name() + " " + c.aliases());
		}
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.codec.StringUtil#strToUtf8Byte(java.lang.String)}.
	 */
	@Test
	public void testStrToUtf8Byte() {
		String s = "初始化密钥,产生1024bit的密钥对";
		byte[] b = StringUtil.strToUtf8Byte(s);
		String c = StringUtil.utf8ByteToStr(b);
		System.out.println(c);
		assertEquals(c, s);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.codec.StringUtil#asciiByteToStr(byte[])}.
	 */
	@Test
	public void testAsciiByteToStr() {
		String s = "初始化密钥,产生1024bit的密钥对";
		byte[] b = StringUtil.strToAsciiByte(s);
		String c = StringUtil.utf8ByteToStr(b);
		System.out.println(c);
		assertNotEquals(c, s);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.codec.StringUtil#strToAsciiByte(java.lang.String)}.
	 */
	@Test
	public void testStrToAsciiByte() {
		String s = "Randomly reorder the int array, with all data stay the same.";
		byte[] b = StringUtil.strToUtf8Byte(s);
		String c = StringUtil.utf8ByteToStr(b);
		System.out.println(c);
		assertEquals(c, s);
	}

	@Test
	public void testJoin() {
		String s = "Randomly reorder the int array, with all data stay the same.";
		String[] arr = s.split("a");
		String b = StringUtil.join(arr, "a");
		assertEquals(b, s);
	}

	@Test
	public void testJoin2() {
		String s = "Randomly reorder the int array, with all data stay the same.";
		String[] arr = s.split(" ");
		List<String> sarr = new ArrayList<String>(12);
		for (String t : arr)
			sarr.add(t);
		System.out.println(sarr);
		String b = StringUtil.join(sarr, " ");
		assertEquals(b, s);
	}

	@Test
	public void testJoin3() {
		String b = StringUtil.join(new String[0], " ");
		assertEquals(b, "");
	}


	@Test
	public void testJoin4() {
		String b = StringUtil.join(new ArrayList<String>(0), " ");
		assertEquals(b, "");
	}

	@Test
	public void testConcat() {
		String b = StringUtil.concat(" ", "good", "morning");
		assertEquals(b, "good morning");
	}

	@Test
	public void testConcatNull() {
	    String b = StringUtil.concat(" ", "good", "morning", null);
	    assertEquals(b, "good morning null");
	}

    @Test
    public void testSplitLinesOkOff() throws Exception {
        String[] arr = StringUtil.splitLines("a\n\nbcd\r\nedf\rg", false);
        assertEquals(arr.length, 4);
        String b = StringUtil.join(arr, " ");
        assertEquals(b, "a bcd edf g");
    }

    @Test
    public void testSplitLinesOkOn() throws Exception {
        String[] arr = StringUtil.splitLines("a\n\nbcd\r\nedf\rg\r\n", true);
        assertEquals(arr.length, 5);
        String b = StringUtil.join(arr, " ");
        assertEquals(b, "a  bcd edf g");
    }

    @Test
    public void testSplitLinesNullOn() throws Exception {
        String[] arr = StringUtil.splitLines(null, true);
        assertEquals(arr.length, 1);
        String b = StringUtil.join(arr, " ");
        assertEquals(b, "null");
    }

    @Test
    public void testSplitLinesEmptyOn() throws Exception {
        String[] arr = StringUtil.splitLines("", true);
        assertEquals(arr.length, 1);
        String b = StringUtil.join(arr, " ");
        assertEquals(b, "");
    }

    @Test
    public void testSplitLinesBlankOn() throws Exception {
        String[] arr = StringUtil.splitLines("  ", true);
        assertEquals(arr.length, 1);
        String b = StringUtil.join(arr, " ");
        assertEquals(b, "  ");
    }

    @Test
    public void testSplitLinesLastOKOn() throws Exception {
        String[] arr = StringUtil.splitLines("This\nis\ngood\n", true);
        assertEquals(arr.length, 3);
        String b = StringUtil.join(arr, " ");
        assertEquals(b, "This is good");
    }

    @Test
    public void testSplitLinesLastOffOn() throws Exception {
        String[] arr = StringUtil.splitLines("This\nis\ngood", true);
        assertEquals(arr.length, 3);
        String b = StringUtil.join(arr, " ");
        assertEquals(b, "This is good");
    }

    @Test
    public void testSplitLinesWindowsOff() throws Exception {
        String[] arr = StringUtil.splitLines("接口\r\nChecksum\r\n\r\n中的\r\ngetValue", false);
        assertEquals(arr.length, 4);
        String b = StringUtil.join(arr, "\r\n");
        assertEquals(b, "接口\r\nChecksum\r\n中的\r\ngetValue");
    }

    @Test
    public void testSplitLinesWindowsOn() throws Exception {
        String[] arr = StringUtil.splitLines("接口\r\nChecksum\r\n\r\n中的\r\ngetValue", true);
        assertEquals(arr.length, 5);
        String b = StringUtil.join(arr, "\r\n");
        assertEquals(b, "接口\r\nChecksum\r\n\r\n中的\r\ngetValue");
    }

    @Test
    public void testSplitLinesWindowsOffl() throws Exception {
        String[] arr = StringUtil.splitLines("接口\r\nChecksum\r\n\r\n中的\r\ngetValue\r\n", false);
        assertEquals(arr.length, 4);
        String b = StringUtil.join(arr, "\r\n");
        assertEquals(b, "接口\r\nChecksum\r\n中的\r\ngetValue");
    }

    @Test
    public void testSplitLinesWindowsOnl() throws Exception {
        String[] arr = StringUtil.splitLines("接口\r\nChecksum\r\n\r\n中的\r\ngetValue\r\n", true);
        assertEquals(arr.length, 5);
        String b = StringUtil.join(arr, "\r\n");
        assertEquals(b, "接口\r\nChecksum\r\n\r\n中的\r\ngetValue");
    }

    @Test
    public void testSplitLinesMacOnl() throws Exception {
        String[] arr = StringUtil.splitLines("接口\rChecksum\r\r中的\rgetValue\r", true);
        assertEquals(arr.length, 5);
        String b = StringUtil.join(arr, "\r");
        assertEquals(b, "接口\rChecksum\r\r中的\rgetValue");
    }

    @Test
    public void testSplitLinesMacOn() throws Exception {
        String[] arr = StringUtil.splitLines("接口\rChecksum\r\r中的\rgetValue", true);
        assertEquals(arr.length, 5);
        String b = StringUtil.join(arr, "\r");
        assertEquals(b, "接口\rChecksum\r\r中的\rgetValue");
    }

    @Test
    public void testSplitLinesMacOff() throws Exception {
        String[] arr = StringUtil.splitLines("接口\rChecksum\r\r中的\rgetValue", false);
        assertEquals(arr.length, 4);
        String b = StringUtil.join(arr, "\r");
        assertEquals(b, "接口\rChecksum\r中的\rgetValue");
    }

    @Test
    public void testSplitLinesMacOffl() throws Exception {
        String[] arr = StringUtil.splitLines("接口\rChecksum\r\r中的\rgetValue\r", false);
        assertEquals(arr.length, 4);
        String b = StringUtil.join(arr, "\r");
        assertEquals(b, "接口\rChecksum\r中的\rgetValue");
    }


    @Test
    public void testSplitLinesLinuxOn() throws Exception {
        String[] arr = StringUtil.splitLines("接口\nChecksum\n\n中的\ngetValue", true);
        assertEquals(arr.length, 5);
        String b = StringUtil.join(arr, "\n");
        assertEquals(b, "接口\nChecksum\n\n中的\ngetValue");
    }

    @Test
    public void testSplitLinesLinuxOff() throws Exception {
        String[] arr = StringUtil.splitLines("接口\nChecksum\n\n中的\ngetValue", false);
        assertEquals(arr.length, 4);
        String b = StringUtil.join(arr, "\n");
        assertEquals(b, "接口\nChecksum\n中的\ngetValue");
    }

    @Test
    public void testSplitLinesLinuxOnl() throws Exception {
        String[] arr = StringUtil.splitLines("接口\nChecksum\n\n中的\ngetValue\n", true);
        assertEquals(arr.length, 5);
        String b = StringUtil.join(arr, "\n");
        assertEquals(b, "接口\nChecksum\n\n中的\ngetValue");
    }

    @Test
    public void testSplitLinesLinuxOffl() throws Exception {
        String[] arr = StringUtil.splitLines("接口\nChecksum\n\n中的\ngetValue\n", false);
        assertEquals(arr.length, 4);
        String b = StringUtil.join(arr, "\n");
        assertEquals(b, "接口\nChecksum\n中的\ngetValue");
    }

}
