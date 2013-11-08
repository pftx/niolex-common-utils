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

import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.apache.commons.lang.ArrayUtils;
import org.apache.niolex.commons.util.Const;

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

	/**
	 * Format the size into string end with one letter.
	 *
	 * @param size the size need to be formatted
	 * @return the string representation
	 */
	public static final String formatSize(double size) {
	    DecimalFormat df = new DecimalFormat("#,###.##");
	    df.setRoundingMode(RoundingMode.HALF_UP);
	    if (size >= Const.T) {
	        return df.format(size / Const.T) + "T";
	    }
	    if (size >= Const.G) {
	        return df.format(size / Const.G) + "G";
	    }
	    if (size >= Const.M) {
	        return df.format(size / Const.M) + "M";
	    }
	    if (size >= Const.K) {
	        return df.format(size / Const.K) + "K";
	    }
	    return df.format(size);
	}

	/**
	 * Parse the double value from the string formatted by {@link #formatSize(double)}
	 *
	 * @param str the string to be parsed.
	 * @return the double value represented by the string argument.
	 * @throws NumberFormatException if the string does not contain a parsable double.
	 */
	public static final double fromSize(String str) {
	    final int len = str.length();
	    str = str.toUpperCase();
	    double size = 0;
	    switch(str.charAt(len - 1)) {
	        case 'T':
	            size = Const.T;
	            break;
	        case 'G':
	            size = Const.G;
                break;
            case 'M':
                size = Const.M;
                break;
            case 'K':
                size = Const.K;
                break;
            default:
                size = 1;
                break;
	    }
	    if (size != 1.0) {
	        str = str.substring(0, len - 1);
	    }
	    return size * Double.parseDouble(str);
	}

	/**
     * Check whether the target is in the argument array.
     *
     * @param target the target need be checked
     * @param args the argument array
     * @return true if found, false otherwise
     */
	public static final boolean isIn(int target, int ...args) {
	    if (ArrayUtils.isEmpty(args)) {
            return false;
        }
        for (int i : args) {
            if (target == i) return true;
        }
        return false;
	}

}
