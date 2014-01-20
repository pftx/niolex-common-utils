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

import static org.junit.Assert.*;

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
public class StringUtilTest extends StringUtil {

	/**
	 * Test method for {@link org.apache.niolex.commons.codec.StringUtil#utf8ByteToStr(byte[])}.
	 */
	@Test
	public void testUtf8ByteToStr() {
	    new StringUtil(){};
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
	public void testJoinStr() {
	    String b = StringUtil.join(", ", "I", "am", "Lex");
	    assertEquals(b, "I, am, Lex");
	}

	@Test
	public void testJoinStr1() {
	    String b = StringUtil.join(" ", "I", "am", "Lex");
	    assertEquals(b, "I am Lex");
	}

	@Test
	public void testJoinStrEmpty() {
	    String b = StringUtil.join(" ");
	    assertEquals(b, "");
	}

	@Test
	public void testJoinStrNull() {
	    String[] arr = null;
	    String b = StringUtil.join(arr, " ");
	    assertEquals(b, "");
	}

	@Test
	public void testJoinNull() {
	    String[] arr = null;
	    String b = StringUtil.join(" ", arr);
	    assertEquals(b, "");
	}

	@Test
	public void testJoinColNull() {
	    List<String> arr = null;
	    String b = StringUtil.join(" ", arr);
	    assertEquals(b, "");
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
	public void testJoin5() {
	    String s = "Randomly reorder the int array, with all data stay the same.";
	    String[] arr = s.split(" ");
	    List<String> sarr = new ArrayList<String>(12);
	    for (String t : arr)
	        sarr.add(t);
	    System.out.println(sarr);
	    String b = StringUtil.join(" ", sarr);
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
    public void testSplit() throws Exception {
        String[] arr = split("aaaaaaa", "aa", true);
        assertEquals(arr.length, 4);
        String b = StringUtil.join(arr, " ");
        assertEquals(b, "   a");
    }

    @Test
    public void testSplitR() throws Exception {
        String[] arr = split("aaaaaaa", "aaa", true);
        assertEquals(arr.length, 3);
        String b = StringUtil.join(arr, " ");
        assertEquals(b, "  a");
    }

    @Test
    public void testSplitLastOff() throws Exception {
        String[] arr = split("a/b/cd", "/", false);
        assertEquals(arr.length, 3);
        String b = StringUtil.join(arr, " ");
        assertEquals(b, "a b cd");
    }

    @Test
    public void testSplitLastOn() throws Exception {
        String[] arr = split("a/b/cd/", "/", false);
        assertEquals(arr.length, 3);
        String b = StringUtil.join(arr, " ");
        assertEquals(b, "a b cd");
    }

    @Test
    public void testSplitFirstOn() throws Exception {
        String[] arr = split("/a/b/cd/", "/", false);
        assertEquals(arr.length, 3);
        String b = StringUtil.join(arr, " ");
        assertEquals(b, "a b cd");
    }

    @Test
    public void testSplitRedunt() throws Exception {
        String[] arr = split("///////a/////b//cd///////", "/", false);
        assertEquals(arr.length, 3);
        String b = StringUtil.join(arr, " ");
        assertEquals(b, "a b cd");
    }

    @Test
    public void testSplitOne() throws Exception {
        String[] arr = split("//////////////////", "///", false);
        assertEquals(arr.length, 0);
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

    @Test
    public void testIsInStringStringArray() throws Exception {
        boolean b = StringUtil.isIn("Lex", "lex", "");
        assertFalse(b);
    }

    @Test
    public void testIsInStringStringArrayYes() throws Exception {
        boolean b = StringUtil.isIn("Lex", null, "Lex", "empty", "");
        assertTrue(b);
    }

    @Test
    public void testIsInStringStringArrayNull() throws Exception {
        String[] arr = null;
        boolean b = StringUtil.isIn("Lex", arr);
        assertFalse(b);
    }

    @Test
    public void testIsInStringStringArrayEmpty() throws Exception {
        boolean b = StringUtil.isIn("Lex");
        assertFalse(b);
    }

    @Test
    public void testIsInStringBooleanStringArray() throws Exception {
        boolean b = StringUtil.isIn("Lex", false, "lex", "");
        assertTrue(b);
    }

    @Test
    public void testIsInStringBooleanStringArrayYes() throws Exception {
        boolean b = StringUtil.isIn("Lex", false, null, "Lex", "empty", "");
        assertTrue(b);
    }

    @Test
    public void testIsInStringBooleanStringArrayYesAg() throws Exception {
        boolean b = StringUtil.isIn("inijoin", false, null, "ini", "jion", "iniJoin");
        assertTrue(b);
    }

    @Test
    public void testIsInStringBooleanStringArrayYesTrueAg() throws Exception {
        boolean b = StringUtil.isIn("inijoin", true, null, "ini", "jion", "iniJoin");
        assertFalse(b);
    }

    @Test
    public void testIsInStringBooleanStringArrayEmpty() throws Exception {
        boolean b = StringUtil.isIn("inijoin", false);
        assertFalse(b);
    }

    @Test
    public void testIsInStringBooleanStringArrayNull() throws Exception {
        String[] arr = null;
        boolean b = StringUtil.isIn("inijoin", false, arr);
        assertFalse(b);
    }

    @Test
    public void testContainsAny() throws Exception {
        assertFalse(StringUtil.containsAny("txt/html", "http", "ftp"));
        assertTrue(StringUtil.containsAny("txt/html", "http", "ftp", "txt"));
    }

    @Test
    public void testContainsAnyEmpty() throws Exception {
        assertFalse(StringUtil.containsAny("txt/html"));
    }

    @Test
    public void testGBK() {
        String s = "全国信息技术标准化技术委员会 GBK 汉字内码扩展规范编码表(二)";
        byte[] arr = strToGbkByte(s);
        assertEquals(59, arr.length);
        String r = gbkByteToStr(arr);
        assertEquals(s, r);
    }

    @Test
    public void testCompatible() {
        String s = "CHR(number_operand [USING charset_name])";
        byte[] ascii = strToAsciiByte(s);
        String gbk = gbkByteToStr(ascii);
        assertEquals(s, gbk);
        byte[] gbkarr = strToGbkByte(s);
        byte[] uft8ar = strToUtf8Byte(s);
        assertArrayEquals(ascii, gbkarr);
        assertArrayEquals(ascii, uft8ar);
        String utf8 = utf8ByteToStr(ascii);
        assertEquals(s, utf8);
    }

    @Test
    public void testRetrieve() throws Exception {
        List<String> list = retrieve("[Main][Debug] This is very good.", '[', ']');
        System.out.println("[retrieve]" + list);
        assertEquals("[Main, Debug]", list.toString());
    }

    @Test
    public void testRetrieveRecursive() throws Exception {
        List<String> list = retrieve("[x[a]b]", '[', ']');
        System.out.println("[retrieve]" + list);
        assertEquals("[a]", list.toString());
    }

    @Test
    public void testRetrieveRecursiveGo() throws Exception {
        List<String> list = retrieve("x-MacThai [Mac[R]Thai][Lex] Mark it.", '[', ']');
        System.out.println("[retrieve]" + list);
        assertEquals("[R, Lex]", list.toString());
    }

    @Test
    public void testRetrieve2() throws Exception {
        List<String> list = retrieve("[Main][Debug] This is very good.", "[", "]");
        System.out.println("[retrieve2]" + list);
        assertEquals("[Main, Debug]", list.toString());
    }

    @Test
    public void testRetrieve2Recursive() throws Exception {
        List<String> list = retrieve("[x[a]b]", "[", "]");
        System.out.println("[retrieve2]" + list);
        assertEquals("[a]", list.toString());
    }

    @Test
    public void testRetrieve2Recursive1() throws Exception {
        List<String> list = retrieve("[x[a].b]", "[", "].");
        System.out.println("[retrieve2]" + list);
        assertEquals("[a]", list.toString());
    }

    @Test
    public void testRetrieve2Recursive2() throws Exception {
        List<String> list = retrieve("[x[.a]b]", "[.", "]");
        System.out.println("[retrieve2]" + list);
        assertEquals("[a]", list.toString());
    }

    @Test
    public void testRetrieve2Re() throws Exception {
        List<String> list = retrieve("<b>This is big font.</b><b>Cheer UP.</b>", "<b>", "</b>");
        System.out.println("[retrieve2]" + list);
        assertEquals("[This is big font., Cheer UP.]", list.toString());
    }

    @Test
    public void testRetrieve2ReRecursive() throws Exception {
        List<String> list = retrieve("<b>This is big font.<b>Cheer UP.</b></b><b>.", "<b>", "</b>");
        System.out.println("[retrieve2]" + list);
        assertEquals("[Cheer UP.]", list.toString());
    }

    @Test
    public void testRetrieve2Fake() throws Exception {
        List<String> list = retrieve("<a]nicea]", "<a]", "a]");
        assertEquals("[nice]", list.toString());
    }

    @Test
    public void testRetrieve2Fakef() throws Exception {
        List<String> list = retrieve("<anice<a]", "<a", "<a]");
        assertEquals("[nice]", list.toString());
    }

    @Test
    public void testRetrieve2Same() throws Exception {
        List<String> list = retrieve("aaIT's Amazing!aaNiceaa", "aa", "aa");
        assertEquals("[IT's Amazing!]", list.toString());
    }

}
