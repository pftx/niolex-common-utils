/**
 * IntegerUtil.java
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

/**
 * Encode & Decode integers.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2012-11-11$
 */
public abstract class IntegerUtil {

	/**
	 * Create an integer by the three bytes, in the order of higher bits first.
	 *
	 * @param a the highest bits
	 * @param b the mid bits
	 * @param c the lowest bits
	 * @return the result integer
	 */
	public static final int threeBytes(byte a, byte b, byte c) {
		return ((a & 0xff) << 16) + ((b & 0xff) << 8) + (c & 0xff);
	}

	/**
	 * Encode integer into three bytes, store the result into <code>arr</code>
	 *
	 * @param i the integer need to encode
	 * @param arr the array to store result
	 * @param idx the index to store result
	 */
	public static final void encThreeBytes(int i, byte[] arr, int idx) {
		arr[idx++] = (byte)(i >> 16);
		arr[idx++] = (byte)(i >> 8);
		arr[idx] = (byte)i;
	}

	/**
	 * Create an integer by the two bytes, in the order of higher bits first.
	 *
	 * @param b the mid bits
	 * @param c the lowest bits
	 * @return the result integer
	 */
	public static final int twoBytes(byte b, byte c) {
		return ((b & 0xff) << 8) + (c & 0xff);
	}

	/**
	 * Encode integer into two bytes, store the result into <code>arr</code>
	 *
	 * @param i the integer need to encode
	 * @param arr the array to store result
	 * @param idx the index to store result
	 */
	public static final void encTwoBytes(int i, byte[] arr, int idx) {
		arr[idx++] = (byte)(i >> 8);
		arr[idx] = (byte)i;
	}
}