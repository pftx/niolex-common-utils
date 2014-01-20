/**
 * StringUtil.java
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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.niolex.commons.internal.IgnoreException;

import com.google.common.collect.Lists;


/**
 * Translate between string and bytes.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-6-20
 */
public abstract class StringUtil extends StringUtils {

    /**
     * Eight-bit Unicode Transformation Format.
     * <p>
     * Every implementation of the Java platform is required to support this character encoding.
     *
     * @see <a href="http://docs.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     */
    public static final Charset UTF_8 = Charset.forName(CharEncoding.UTF_8);

    /**
     * Seven-bit ASCII, also known as ISO646-US, also known as the Basic Latin block of the Unicode character set.
     * <p>
     * Every implementation of the Java platform is required to support this character encoding.
     *
     * @see <a href="http://docs.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     */
    public static final Charset US_ASCII = Charset.forName(CharEncoding.US_ASCII);

    /**
     * Chinese Internal Code Specification version 1.0, also known as GBK. It's compatible with GB2312 but contains
     * more characters. It's widely used in China mainland. It's also compatible with US_ASCII.
     * <p>
     * Not every implementation of the Java platform has this charset.
     */
    public static final Charset GBK = IgnoreException.getCharset("gbk");

    /**
     * ISO Latin Alphabet No. 1, a.k.a. ISO-LATIN-1.
     * <p>
     * Every implementation of the Java platform is required to support this character encoding.
     *
     * @see <a href="http://download.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     */
    public static final Charset ISO8859_1 = Charset.forName(CharEncoding.ISO_8859_1);

	/**
	 * Translate UTF8 encoded byte array to String.
	 *
	 * @param data the binary format
	 * @return the result string
	 */
	public static final String utf8ByteToStr(byte[] data) {
	    return new String(data, UTF_8);
	}

	/**
	 * Translate String to UTF8 encoded byte array.
	 *
	 * @param str the string
	 * @return the encoded byte array
	 */
	public static final byte[] strToUtf8Byte(String str) {
	    return str.getBytes(UTF_8);
	}

	/**
	 * Translate US-ASCII encoded byte array to String.
	 *
	 * @param data the binary format
	 * @return the string
	 */
	public static final String asciiByteToStr(byte[] data) {
	    return new String(data, US_ASCII);
	}

	/**
	 * Translate String to US-ASCII encoded byte array.
	 *
	 * @param str the string
	 * @return the encoded byte array
	 */
	public static final byte[] strToAsciiByte(String str) {
	    return str.getBytes(US_ASCII);
	}

	/**
	 * Translate GBK encoded byte array to String.
	 *
	 * @param data the binary format
	 * @return the string
	 * @throws NullPointerException If your JDK dosen't support GBK
	 */
	public static final String gbkByteToStr(byte[] data) {
	    return new String(data, GBK);
	}

	/**
	 * Translate String to GBK encoded byte array.
	 *
	 * @param str the string
	 * @return the encoded byte array
	 * @throws NullPointerException If your JDK dosen't support GBK
	 */
	public static final byte[] strToGbkByte(String str) {
	    return str.getBytes(GBK);
	}

	/**
     * Join the string array into one single string by the separator.
     *
     * @param sep the separator
     * @param strs the string array
     * @return the result string
     */
	public static final String join(String sep, String... strs) {
	    return join(strs, sep);
	}

    /**
     * Concatenates all the parameters into one string by the specified separator.
     *
     * @param sep the separator
     * @param arr the string array
     * @return the result string
     */
    public static final String concat(String sep, String ...arr) {
        return join(arr, sep);
    }

	/**
	 * Join the string array into one single string by the separator.
	 *
	 * @param strs the string array
	 * @param sep the separator
	 * @return the result string
	 */
	public static final String join(String[] strs, String sep) {
		StringBuilder sb = new StringBuilder();
		if (strs == null || strs.length == 0) {
			return "";
		}
		sb.append(strs[0]);
		for (int i = 1; i < strs.length; ++i) {
			sb.append(sep).append(strs[i]);
		}
		return sb.toString();
	}

	/**
     * Join the string collection into one single string by the separator.
     *
     * @param sep the separator
     * @param strs the collection of string
     * @return the result string
     */
    public static final String join(String sep, Collection<String> strs) {
        return join(strs, sep);
    }

	/**
	 * Join the string collection into one single string by the separator.
	 *
	 * @param strs the collection of string
	 * @param sep the separator
	 * @return the result string
	 */
	public static final String join(Collection<String> strs, String sep) {
		StringBuilder sb = new StringBuilder();
		if (strs == null || strs.size() == 0) {
			return "";
		}
		Iterator<String> it = strs.iterator();
		sb.append(it.next());
		while (it.hasNext()) {
			sb.append(sep).append(it.next());
		}
		return sb.toString();
	}

	/**
	 * Split the string into tokens by the specified separator. We will preserve all the empty
	 * tokens if you need.
	 * <br>
	 * This is a replacement of {@link String#split(String)}, which is using regex.
	 *
	 * @param str the string to be split
	 * @param separator the separator
	 * @param preserveEmptyToken if set this to true, we preserve all empty tokens
	 * @return the token array
	 */
	public static final String[] split(String str, String separator, boolean preserveEmptyToken) {
	    List<String> list = new ArrayList<String>();
	    int start = 0, i = 0, len = separator.length(), total = str.length();
	    while ((i = str.indexOf(separator, start)) != -1) {
	        if (start != i || preserveEmptyToken) {
	            list.add(str.substring(start, i));
	        }
	        start = i + len;
	    }
	    if (start != total) {
	        list.add(str.substring(start, total));
	    }
	    return list.toArray(new String[list.size()]);
	}

	/**
	 * This is a platform independent line split tool. We will split a line by
	 * '\r', '\n', '\r\n', and preserve all empty lines if you need.
	 *
	 * A new line separator at the last line is not needed.
	 *
	 * @param str the string you want to split
	 * @param preserveEmptyLines if set this to true, we will preserve all empty lines
	 * @return the lines array
	 */
	public static final String[] splitLines(String str, boolean preserveEmptyLines) {
	    if (isBlank(str)) {
	        return new String[] {str};
	    }
	    int len = str.length(), start = 0;
	    List<String> list = new ArrayList<String>();
	    for (int i = 0; i < len; ++i) {
	        char ch = str.charAt(i);
	        if (ch == '\r') {
	            if (start != i || preserveEmptyLines) {
	                list.add(str.substring(start, i));
	            }
	            if (i + 1 < len && str.charAt(i + 1) == '\n') {
	                ++i;
	            }
	            start = i + 1;
	        } else if (ch == '\n') {
	            if (start != i || preserveEmptyLines) {
                    list.add(str.substring(start, i));
                }
	            start = i + 1;
	        }
	    }
	    if (start != len) {
	        // There is no new line for the last line. We add it here.
	        list.add(str.substring(start, len));
	    }
	    return list.toArray(new String[list.size()]);
	}

	/**
     * Check whether the target is in the argument array.
     *
     * @param target the target need be checked
     * @param args the argument array
     * @return true if found, false otherwise
     */
    public static final boolean isIn(String target, String ...args) {
        return isIn(target, true, args);
    }

	/**
	 * Check whether the target is in the argument array.
	 *
	 * @param target the target need be checked
	 * @param caseSensitive true if case sensitive
	 * @param args the argument array
	 * @return true if found, false otherwise
	 */
	public static final boolean isIn(String target, boolean caseSensitive, String ...args) {
	    if (ArrayUtils.isEmpty(args)) {
	        return false;
	    }
	    for (String s : args) {
	        if (caseSensitive) {
	            if (target.equals(s)) {
	                return true;
	            }
	        } else {
	            if (target.equalsIgnoreCase(s)) {
                    return true;
                }
	        }
	    }
	    return false;
	}

	/**
	 * Test whether the target contains any of the string in the argument array.
	 *
	 * @param target the target need be checked
	 * @param args the argument array
	 * @return true if found, false otherwise
	 */
	public static final boolean containsAny(String target, String ...args) {
	    if (ArrayUtils.isEmpty(args)) {
            return false;
        }
	    for (String s : args) {
	        if (target.contains(s)) {
                return true;
            }
	    }
	    return false;
	}

	/**
	 * Retrieve the list of strings in the middle of left char and right char.
	 * i.e. [Main][Debug] This is very good.
	 * User can retrieve Main and Debug out of this message.
	 * <br>
	 * For "[x[a]b]" this kind of string, we will ignore the outer part and just
	 * return "a".
	 *
	 * @param str the source string
	 * @param left the left side character
	 * @param right the right side character
	 * @return the list of strings
	 */
	public static final List<String> retrieve(String str, char left, char right) {
	    List<String> list = Lists.newArrayList();
	    /**
	     * We have two status here:
	     * 0 - left not found, need left
	     * 1 - left found, need right
	     */
	    int stat = 0;
	    for (int i = 0, idx = 0; i < str.length(); ++i) {
            if (str.charAt(i) == left) {
                stat = 1;
                idx = i + 1;
	        } else if (stat == 1 && str.charAt(i) == right) {
	            stat = 0;
	            list.add(str.substring(idx, i));
	        }
	    }
	    return list;
	}

	/**
     * Retrieve the list of strings in the middle of left string and right string.
     * i.e. [Main][Debug] This is very good.
     * User can retrieve Main and Debug out of this message.
     *
     * @param str the source string
     * @param left the left side string
     * @param right the right side string
     * @return the list of middle strings
     */
    public static final List<String> retrieve(String str, String left, String right) {
        if (left.length() == 1 && right.length() == 1) {
            return retrieve(str, left.charAt(0), right.charAt(0));
        }
        List<String> list = Lists.newArrayList();
        int l, r = 0, ll = left.length(), rl = right.length();
        while (true) {
            l = str.indexOf(left, r) + ll;
            if (l < ll) break;
            r = str.indexOf(right, l);
            if (r < 0) break;
            l = str.lastIndexOf(left, r - ll) + ll;
            list.add(str.substring(l, r));
            r += rl;
        }
        return list;
    }

}
