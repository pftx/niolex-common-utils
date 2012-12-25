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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;


/**
 * Translate between string and bytes.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-6-20
 */
public abstract class StringUtil {

	/**
	 * Translate UTF8 encoded byte array to String.
	 *
	 * @param data
	 * @return the result string
	 */
	public static final String utf8ByteToStr(byte[] data) {
		try {
			return new String(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Your JDK do not support UTF-8", e);
		}
	}

	/**
	 * Translate String to UTF8 encoded byte array.
	 * @param str
	 * @return the encoded byte array
	 */
	public static final byte[] strToUtf8Byte(String str) {
		try {
			return str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Your JDK do not support UTF-8", e);
		}
	}

	/**
	 * Translate US-ASCII encoded byte array to String.
	 *
	 * @param data
	 * @return the string
	 */
	public static final String asciiByteToStr(byte[] data) {
		try {
			return new String(data, "US-ASCII");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Your JDK do not support US-ASCII", e);
		}
	}

	/**
	 * Translate String to US-ASCII encoded byte array.
	 * @param str
	 * @return the encoded byte array
	 */
	public static final byte[] strToAsciiByte(String str) {
		try {
			return str.getBytes("US-ASCII");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Your JDK do not support US-ASCII", e);
		}
	}

	/**
	 * Join the string array into one single string by the separator.
	 *
	 * @param strs
	 * @param sep
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
	 * @param strs
	 * @param sep
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
	 * Concatenates all the parameters into one string by the specified seperator.
	 *
	 * @param sep
	 * @param arr
	 * @return the result string
	 */
	public static final String concat(String sep, String ...arr) {
		return join(arr, sep);
	}

	/**
	 * This is a platform independent line split tool. We will split a line by
	 * '\r', '\n', '\r\n', and preserve all empty lines is you need.
	 *
	 * A new line separator at the last line is not needed.
	 *
	 * @param str the string you want to split
	 * @param preserveEmptyLines if set this to true, we will preserve all empty lines
	 * @return the lines array
	 */
	public static final String[] splitLines(String str, boolean preserveEmptyLines) {
	    if (StringUtils.isBlank(str)) {
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
}
