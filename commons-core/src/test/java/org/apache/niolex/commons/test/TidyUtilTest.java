/**
 * TidyUtilTest.java
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
package org.apache.niolex.commons.test;

import static org.junit.Assert.*;

import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.file.FileUtil;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-5-27
 */
public class TidyUtilTest extends TidyUtil {

	/**
	 * Test method for {@link org.apache.niolex.commons.test.TidyUtil#removePrefix(java.lang.String, int)}.
	 */
	@Test
	public final void testRemovePrefix() {
	    new TidyUtil() {};
		String str = FileUtil.getCharacterFileContentFromClassPath("Data.txt", TidyUtilTest.class, StringUtil.UTF_8);
		str = removePrefix(str, 3);
        System.out.println("SL " + str.length());
        System.out.println(str);
        assertEquals(628, str.length());
	}

    @Test
    public void testGenerateChar() throws Exception {
        StringBuilder sb = new StringBuilder();
        generateChar(sb, '*', 7);
        assertEquals("*******", sb.toString());
    }

    @Test
    public void testGenerateChar0() throws Exception {
        StringBuilder sb = new StringBuilder();
        generateChar(sb, '*', 0);
        assertEquals("", sb.toString());
    }

    @Test
    public void testGenerateCharNeg() throws Exception {
        StringBuilder sb = new StringBuilder();
        generateChar(sb, '*', -100);
        assertEquals("", sb.toString());
    }

    @Test
    public void testGenerateSeparator() throws Exception {
        StringBuilder sb = new StringBuilder();
        generateSeparator(sb, new int[] {5, 6, 7});
        assertEquals("+-----+------+-------+\n", sb.toString());
    }

    @Test
    public void testGenerateSeparator1() throws Exception {
        StringBuilder sb = new StringBuilder();
        generateSeparator(sb, new int[] {5});
        assertEquals("+-----+\n", sb.toString());
    }

    @Test
    public void testGenerateSeparator2() throws Exception {
        StringBuilder sb = new StringBuilder();
        generateSeparator(sb, new int[] {5, 6, 5});
        assertEquals("+-----+------+-----+\n", sb.toString());
    }

    @Test
    public void testAlign() {
        assertEquals("LEFT", Align.LEFT.toString());
        assertEquals(Align.MIDDILE, Align.valueOf("MIDDILE"));
    }

    @Test
    public void testGenerateCellNull() throws Exception {
        StringBuilder sb = new StringBuilder();
        boolean flag = generateCell(sb, 8, null, Align.LEFT);
        assertEquals("        ", sb.toString());
        assertFalse(flag);
    }

    @Test
    public void testGenerateCellFull() throws Exception {
        StringBuilder sb = new StringBuilder();
        boolean flag = generateCell(sb, 8, "12346578", Align.LEFT);
        assertEquals("12346578", sb.toString());
        assertFalse(flag);
    }

    @Test
    public void testGenerateCellOverflow() throws Exception {
        StringBuilder sb = new StringBuilder();
        boolean flag = generateCell(sb, 6, "12345678", Align.LEFT);
        assertEquals("123456", sb.toString());
        assertTrue(flag);
    }

    @Test
    public void testGenerateCellLeft() throws Exception {
        StringBuilder sb = new StringBuilder();
        boolean flag = generateCell(sb, 8, 66, Align.LEFT);
        assertEquals("66      ", sb.toString());
        assertFalse(flag);
    }

    @Test
    public void testGenerateCellMid() throws Exception {
        StringBuilder sb = new StringBuilder();
        boolean flag = generateCell(sb, 8, "KK", Align.MIDDILE);
        assertEquals("   KK   ", sb.toString());
        assertFalse(flag);
    }

    @Test
    public void testGenerateCellRight() throws Exception {
        StringBuilder sb = new StringBuilder();
        boolean flag = generateCell(sb, 8, 66, Align.RIGHT);
        assertEquals("      66", sb.toString());
        assertFalse(flag);
    }

    @Test
    public void testGenerateRowTitle() throws Exception {
        StringBuilder sb = new StringBuilder();
        generateRow(sb, new int[] {7, 14}, new String[] {"MySQL's", "Best Regards"}, true);
        assertEquals("|MySQL's| Best Regards |\n", sb.toString());
    }

    @Test
    public void testGenerateRow() throws Exception {
        StringBuilder sb = new StringBuilder();
        generateRow(sb, new int[] {6, 14}, new String[] {"MySQL's", "Best Regards"}, false);
        assertEquals("|MySQL'^Best Regards  |\n", sb.toString());
    }

    @Test
    public void testGenerateRowData() throws Exception {
        StringBuilder sb = new StringBuilder();
        generateRow(sb, new int[] {10, 14}, new Object[] {5577669, "8844221"}, false);
        assertEquals("|   5577669|8844221       |\n", sb.toString());
    }

    @Test
    public void testGenerateTable() throws Exception {
        String s = generateTable(new int[] {10, 14}, new String[] {"Summary", "Hint"},
                new Object[] {5577669, "8844221", 66, "Lex go go.", 88});
        System.out.println(s);
    }

    @Test(expected=NullPointerException.class)
    public void testGenerateCell() throws Exception {
        StringBuilder sb = new StringBuilder();
        boolean flag = generateCell(sb, 8, 66, null);
        assertEquals("      66", sb.toString());
        assertFalse(flag);
    }

}
