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


/**
 * Translate between string and bytes.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-6-20
 */
public abstract class StringUtil {

	/**
	 * Translate UTF8 encoded byte array to String.
	 *
	 * @param data
	 * @return
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
	 * @return
	 */
	public static final byte[] strToUTF8Byte(String str) {
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
	 * @return
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
	 * @return
	 */
	public static final byte[] strToASCIIByte(String str) {
		try {
			return str.getBytes("US-ASCII");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Your JDK do not support US-ASCII", e);
		}
	}
}
