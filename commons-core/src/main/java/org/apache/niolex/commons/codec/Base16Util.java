/**
 * Base16Util.java
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
package org.apache.niolex.commons.codec;

import java.util.Arrays;

/**
 * This utility encodes byte array to base16(HEX) string and vice versa.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 */
public abstract class Base16Util {

    private static final char[] DIGIT_TO_CHAR = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c',
            'd', 'e', 'f' };
    private static final byte[] CHAR_TO_DIGIT = new byte[128];

    /**
     * Initialize the chars array.
     */
    static {
        Arrays.fill(CHAR_TO_DIGIT, (byte) -1);
        CHAR_TO_DIGIT['0'] = 0;
        CHAR_TO_DIGIT['1'] = 1;
        CHAR_TO_DIGIT['2'] = 2;
        CHAR_TO_DIGIT['3'] = 3;
        CHAR_TO_DIGIT['4'] = 4;
        CHAR_TO_DIGIT['5'] = 5;
        CHAR_TO_DIGIT['6'] = 6;
        CHAR_TO_DIGIT['7'] = 7;
        CHAR_TO_DIGIT['8'] = 8;
        CHAR_TO_DIGIT['9'] = 9;
        CHAR_TO_DIGIT['a'] = 10;
        CHAR_TO_DIGIT['b'] = 11;
        CHAR_TO_DIGIT['c'] = 12;
        CHAR_TO_DIGIT['d'] = 13;
        CHAR_TO_DIGIT['e'] = 14;
        CHAR_TO_DIGIT['f'] = 15;
    }

    /**
     * Encode bytes to base16(HEX) string.
     *
     * @param bytes the bytes to be encoded
     * @return the encoded base16(HEX) string
     * @throws IllegalArgumentException if the parameter is null
     */
    public static final String byteToBase16(byte[] bytes) {
        if (bytes == null)
            throw new IllegalArgumentException("The parameter should not be null!");

        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(DIGIT_TO_CHAR[(b & 0xF0) >>> 4]);
            sb.append(DIGIT_TO_CHAR[b & 0x0F]);
        }
        return sb.toString();
    }

    /**
     * Decode base16(HEX) string to bytes array.
     *
     * @param str the base16(HEX) string
     * @return the decoded bytes array
     * @throws IllegalArgumentException if the parameter is null
     * @throws NumberFormatException if the base16(HEX) string contains invalid char
     */
    public static final byte[] base16ToByte(String str) {
        if (str == null)
            throw new IllegalArgumentException("The parameter should not be null!");

        final char[] hex = str.toLowerCase().toCharArray();
        final int hexLen = str.length();
        if (hexLen % 2 != 0)
            throw new IllegalArgumentException("The parameter length should be even!");

        byte[] bytes = new byte[hexLen / 2];
        for (int i = 0, j = 0; i < hexLen; i += 2, ++j) {
            if (hex[i] > 128 || hex[i + 1] > 128) {
                throw new IllegalArgumentException("The parameter contains invalid charater!");
            }
            if (CHAR_TO_DIGIT[hex[i]] < 0 || CHAR_TO_DIGIT[hex[i + 1]] < 0) {
                throw new IllegalArgumentException("The parameter contains invalid charater!");
            }

            bytes[j] = (byte)(CHAR_TO_DIGIT[hex[i]] << 4 | CHAR_TO_DIGIT[hex[i+1]]);
        }
        return bytes;
    }

}
