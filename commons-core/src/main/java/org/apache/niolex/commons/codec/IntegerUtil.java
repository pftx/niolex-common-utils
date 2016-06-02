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
 * Encode &amp; Decode integers.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2012-11-11$
 */
public abstract class IntegerUtil {
    
    /**
     * Create a long integer using 8 bytes from the specified byte array, start from the specified index.
     * (Most significant byte first.)
     *
     * @param arr the byte array
     * @param idx the data start index
     * @return the result long integer
     */
    public static final long eightBytes(byte[] arr, int idx) {
        long l = 0;
        
        for (int i = 0; i < 8; i++) {
            l <<= 8;
            l |= arr[idx + i] & 0xff;
        }
        
        return l;
    }
    
    /**
     * Encode the specified long integer into eight bytes. (Most significant byte first.)
     * 
     * @param l the long integer to be encoded
     * @return the eight bytes array
     */
    public static final byte[] toEightBytes(long l) {
        byte[] retVal = new byte[8];
        encEightBytes(l, retVal, 0);
        return retVal;
    }
    
    /**
     * Encode the specified long integer into eight bytes, store the result into the specified array. (Most significant byte first.)
     *
     * @param l the long integer to be encoded
     * @param arr the array used to to store the results
     * @param idx the start index to store result
     */
    public static final void encEightBytes(long l, byte[] arr, int idx) {
        for (int i = 0; i < 8; i++) {
            arr[i] = (byte) (l >> 8 * (7 - i));
        }
    }

    /**
     * Create an integer from the specified byte array, start from the specified index.
     * (Most significant byte first.)
     *
     * @param arr the byte array
     * @param idx the data start index
     * @return the result integer
     */
    public static final int fourBytes(byte[] arr, int idx) {
        return fourBytes(arr[idx], arr[idx + 1], arr[idx + 2], arr[idx + 3]);
    }

    /**
     * Create an integer from the specified four bytes, in the order of highest bits first.
     *
     * @param h the highest bits
     * @param a the second byte
     * @param b the third byte
     * @param c the lowest bits
     * @return the result integer
     */
    public static final int fourBytes(byte h, byte a, byte b, byte c) {
        return ((h & 0xff) << 24) + ((a & 0xff) << 16) + ((b & 0xff) << 8) + (c & 0xff);
    }

    /**
     * Encode the integer into four bytes. (Most significant byte first.)
     *
     * @param i the integer to be encoded
     * @return the encoded four bytes
     */
    public static final byte[] toFourBytes(int i) {
        byte[] arr = new byte[4];
        encFourBytes(i, arr, 0);
        return arr;
    }

    /**
     * Encode the integer into four bytes, store the result into the specified array. (Most significant byte first.)
     *
     * @param i the integer need to be encoded
     * @param arr the array used to to store the results
     * @param idx the start index to store result
     */
    public static final void encFourBytes(int i, byte[] arr, int idx) {
        arr[idx++] = (byte)(i >> 24);
        arr[idx++] = (byte)(i >> 16);
        arr[idx++] = (byte)(i >> 8);
        arr[idx] = (byte)i;
    }

	/**
	 * Create an integer by the specified three bytes, in the order of higher bits first.
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
	 * Encode integer into three bytes, store the result into the specified array.
	 *
	 * @param i the integer need to encode
	 * @param arr the array used to store the result
	 * @param idx the start index to store the result
	 */
	public static final void encThreeBytes(int i, byte[] arr, int idx) {
		arr[idx++] = (byte)(i >> 16);
		arr[idx++] = (byte)(i >> 8);
		arr[idx] = (byte)i;
	}

	/**
	 * Create an integer by the two bytes, in the order of higher bits first.
	 *
	 * @param b the highest bits
	 * @param c the lowest bits
	 * @return the result integer
	 */
	public static final int twoBytes(byte b, byte c) {
		return ((b & 0xff) << 8) + (c & 0xff);
	}

	/**
	 * Encode integer into two bytes, store the result into the specified array.
	 *
	 * @param i the integer need to be encoded
	 * @param arr the array used to store the result
	 * @param idx the start index to store the result
	 */
	public static final void encTwoBytes(int i, byte[] arr, int idx) {
		arr[idx++] = (byte)(i >> 8);
		arr[idx] = (byte)i;
	}

	/**
	 * Format the size into string end with one letter. We will round the result
	 * by half up, and keep two decimal places.
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
	 * @throws NumberFormatException if the string can not be parsed by our format.
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
