/**
 * TidyUtil.java
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
package org.apache.niolex.commons.test;

import org.apache.niolex.commons.codec.StringUtil;

/**
 * The utility to tidy string, generate formatted string, etc.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-5-27
 */
public abstract class TidyUtil {

	/**
	 * Remove the prefix at every line, from the input string.
	 *
	 * @param str the input lines, separated by "\n"
	 * @param prefix the prefix end index
	 * @return the result
	 */
	public static final String removePrefix(String str, int prefix) {
		String[] lines = StringUtil.splitLines(str, true);
		StringBuilder sb = new StringBuilder();
		for (String l : lines) {
			if (l.length() > prefix) {
				sb.append(l.substring(prefix));
			}
			sb.append('\n');
		}
		return sb.toString();
	}

	/**
	 * Generate a number of characters and append them to the string builder.
	 *
	 * @param sb the string builder used to append the generated data
	 * @param ch the character used to append
	 * @param count the number of characters to be appended
	 */
	public static final void generateChar(StringBuilder sb, final char ch, final int count) {
	    for (int i = 0; i < count; ++i) {
	        sb.append(ch);
        }
	}

	/**
	 * Generate the table separator to separate titles from data, and surround the table.
	 *
	 * @param sb the string builder used to append the generated data
	 * @param colLen the columns length
	 */
	public static final void generateSeparator(StringBuilder sb, int[] colLen) {
	    sb.append('+');
	    for (int i = 0; i < colLen.length; ++i) {
	        generateChar(sb, '-', colLen[i]);
	        sb.append('+');
	    }
	    sb.append('\n');
	}

	/**
	 * The align way for table cell.
	 *
	 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
	 * @version 1.0.0
	 * @since 2014-7-3
	 */
	public static enum Align {
	    LEFT, MIDDILE, RIGHT;
	}

	/**
	 * Generate the table cell value and append it to the string builder.
	 *
	 * @param sb the string builder used to append the generated data
	 * @param colLen the cell total length
	 * @param value the value to be used
	 * @param align the align way
	 * @return true if cell value overflow and truncated
	 */
	public static final boolean generateCell(StringBuilder sb, final int colLen, Object value, Align align) {
	    if (value == null) {
	        generateChar(sb, ' ', colLen);
	        return false;
	    }
	    String str = value.toString();
	    final int len = str.length();
	    if (len > colLen) {
	        sb.append(str.substring(0, colLen));
	        return true;
	    } else if (len == colLen) {
	        sb.append(str);
	        return false;
	    }
	    int left = 0;
	    switch (align) {
	        case LEFT:
	            left = 0;
	            break;
	        case MIDDILE:
	            left = (colLen - len) / 2;
	            break;
	        default:
	            left = colLen - len;
	            break;
	    }
	    generateChar(sb, ' ', left);
	    sb.append(str);
	    generateChar(sb, ' ', colLen - left - len);
	    return false;
	}

	/**
	 * Generate one table row and append it to the string builder.
	 *
	 * @param sb the string builder used to append the generated data
	 * @param colLen the columns length
	 * @param values the row data
	 * @param isTitle whether we are generating title
	 */
	public static final void generateRow(StringBuilder sb, int[] colLen, Object[] values, boolean isTitle) {
        Check.eq(values.length, colLen.length, "No enough col length.");
        boolean overflow = false;
        sb.append('|');
        for (int i = 0; i < values.length; ++i) {
            if (isTitle) {
                overflow = generateCell(sb, colLen[i], values[i], Align.MIDDILE);
            } else if (values[i] instanceof Number) {
                overflow = generateCell(sb, colLen[i], values[i], Align.RIGHT);
            } else {
                overflow = generateCell(sb, colLen[i], values[i], Align.LEFT);
            }
            if (overflow) sb.append('^');
            else sb.append('|');
        }
        sb.append('\n');
    }

	/**
     * Format the inputs as a table and return it as string.
     *
     * @param colLen the columns length
     * @param titles the columns titles
     * @param values the table body
     */
	public static final String generateTable(int[] colLen, String[] titles, Object ...values) {
        StringBuilder sb = new StringBuilder();
        generateSeparator(sb, colLen);
        generateRow(sb, colLen, titles, true);
        generateSeparator(sb, colLen);

        Object[] tmp = new Object[colLen.length];
        for (int i = 0; i < values.length; i += colLen.length) {
            for (int k = 0; k < colLen.length; ++k) {
                tmp[k] = (i + k < values.length) ? values[i + k] : null;
            }
            generateRow(sb, colLen, tmp, false);
        }

        generateSeparator(sb, colLen);
        return sb.toString();
    }

}
