/**
 * CommonBase64.java
 *
 * Copyright 2016 the original author or authors.
 *
 * We licenses this file to you under the Apache License, version 2.0
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
 * We support Standard 'base64' encoding for RFC 3548 or RFC 4648 and Standard 'base64url' with URL and Filename Safe
 * Alphabet (RFC 4648 §5 'Table 2: The "URL and Filename safe" Base 64 Alphabet').
 * <br>
 * We support line feeds at fixed length from 64 to 76(must break line at multiple of 4) and the line end with CRLF.
 * <br>
 * <br>
 * When decoding, we try our best to recognize as many variants as possible. This is good for users: it's better to
 * give them the data rather than tell them the encoding format is non-standard or malformed.
 * 
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 3.0.0
 * @since Oct 27, 2016
 */
public class CommonBase64 {

    /**
     * The Base64 standard alphabet.
     */
    public static final String STD_BASE64_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

    /**
     * The decoding table.
     */
    private static final int[] DEC_TBL = new int[128];

    /**
     * The standard encoding table.
     */
    private static final char[] ENC_STD = STD_BASE64_ALPHABET.toCharArray();

    /**
     * The URL and file name saft encoding table.
     */
    private static final char[] ENC_URL = STD_BASE64_ALPHABET.toCharArray();

    /**
     * Constant code point.
     */
    private static final int CODE_INVALID = -1;
    private static final int CODE_PADDING = -2;

    private static final char CHAR_PADDING = '=';
    private static final byte BYTE_PADDING = 0;

    static {
        // Initialize the encoding table.
        ENC_URL[62] = '-';
        ENC_URL[63] = '_';

        // Initialize the decoding table.
        Arrays.fill(DEC_TBL, CODE_INVALID);
        for (int i = 0; i < 64; ++i) {
            DEC_TBL[ENC_STD[i]] = i;
        }
        DEC_TBL['-'] = 62;
        DEC_TBL['.'] = 62;
        DEC_TBL['_'] = 63;
        DEC_TBL[','] = 63;

        DEC_TBL['='] = CODE_PADDING;
    }

    /**
     * Encode a Base64 trunk.
     * 
     * @param table the encoding table
     * @param out the output string builder
     * @param a the first byte
     * @param b the second byte
     * @param c the third byte
     */
    public static void encTrunk(char[] table, StringBuilder out, byte a, byte b, byte c) {
        int i = IntegerUtil.threeBytes(a, b, c);
        out.append(table[i >> 18]);
        out.append(table[i >> 12 & 0x3f]);
        out.append(table[i >> 6 & 0x3f]);
        out.append(table[i & 0x3f]);
    }

    /**
     * Encode the byte array tail.
     * 
     * @param table the encoding table
     * @param out the output string builder
     * @param a the first byte
     * @param b the second byte
     * @param padding whether we should pad the padding character to the end of the output
     * @param len the tail length, must be 1 or 2
     */
    public static void encTail(char[] table, StringBuilder out, byte a, byte b, boolean padding, int len) {
        int i = IntegerUtil.threeBytes(a, b, BYTE_PADDING);
        out.append(table[i >> 18]);
        out.append(table[i >> 12 & 0x3f]);
        if (len == 1) {
            if (padding) {
                out.append(CHAR_PADDING);
                out.append(CHAR_PADDING);
            }
            return;
        }
        out.append(table[i >> 6 & 0x3f]);
        if (len == 2) {
            if (padding) {
                out.append(CHAR_PADDING);
            }
            return;
        }
    }

    /**
     * Encode the bytes into Base64 string using RFC 4648. We add pad char if needed, and there is no line break in the
     * output.
     * 
     * @param bytes the bytes to be encoded
     * @return the encoded Base64 string
     */
    public static final String encode(byte[] bytes) {
        return encode(bytes, true, -1, false);
    }

    /**
     * Encode the bytes into Base64 string using RFC 4648 §5 'Table 2: The "URL and Filename safe" Base 64 Alphabet'.
     * We do not add any pad char to the output, and there is no line break in the output.
     * 
     * @param bytes the bytes to be encoded
     * @return the encoded Base64 string
     */
    public static final String encodeURL(byte[] bytes) {
        return encode(bytes, false, -1, true);
    }

    /**
     * Encode the bytes into Base64 string.
     * 
     * @param bytes the bytes to be encoded
     * @param needPadding whether we need pad '=' at the end of the output for incomplete block
     * @param lineLength the line length for the output, must between 64 and 76, and must be multiple of 4; use -1 for
     *            no line break in the whole output string
     * @param isURL is encoding as URL and file name safe format
     * @return the encoded Base64 string
     */
    public static final String encode(byte[] bytes, boolean needPadding, int lineLength, boolean isURL) {
        char[] table = isURL ? ENC_URL : ENC_STD;
        StringBuilder sb = new StringBuilder();

        if (lineLength != -1 && (lineLength < 64 || lineLength > 76 || lineLength % 4 != 0)) {
            throw new IllegalArgumentException("Invalid line length: " + lineLength);
        }

        int len = bytes.length, i, cnt = 0;
        for (i = 0; i < len - 2; i += 3) {
            encTrunk(table, sb, bytes[i], bytes[i + 1], bytes[i + 2]);
            cnt += 4;

            if (lineLength != -1 && cnt % lineLength == 0 && i != len - 3) {
                sb.append('\r');
                sb.append('\n');
            }
        }

        if (i != len) {
            if (len - i == 1) {
                encTail(table, sb, bytes[i], BYTE_PADDING, needPadding, 1);
            } else if (len - i == 2) {
                encTail(table, sb, bytes[i], bytes[i + 1], needPadding, 2);
            }
        }

        return sb.toString();
    }

    /**
     * Check whether the input string is Base64 format compatible.
     * 
     * @param in the input string
     * @return true if the input string is Base64 compatible
     */
    public static boolean isBase64(String in) {
        try {
            tidyBase64String(in);
            return true;
        } catch (Exception ignore) {
            return false;
        }
    }

    /**
     * Tidy the input string and return the char array for Base64 decoding.
     * 
     * @param in the input string
     * @return the tidied Base64 char array
     * @throws IllegalArgumentException if the input string is not Base64 compatible
     */
    public static char[] tidyBase64String(String in) {
        StringBuilder sb = new StringBuilder();
        int code;

        for (int i = 0; i < in.length(); ++i) {
            char c = in.charAt(i);
            if (Character.isWhitespace(c)) {
                continue;
            }

            if (c > 127 || (code = DEC_TBL[c]) == CODE_INVALID) {
                throw new IllegalArgumentException(
                        "The input string is not a valid Base64 string, invalid char is [" + c + "] at " + i);
            }

            if (code == CODE_PADDING) {
                break;
            }

            sb.append(c);
        }

        int len = sb.length();
        int mod = len % 4;
        if (mod == 1) {
            throw new IllegalArgumentException("The input string is not a valid Base64 string, Incomplete block.");
        }

        char[] dst = new char[len];
        sb.getChars(0, len, dst, 0);
        return dst;
    }

    /**
     * Decode a Base64 4 chars trunk.
     * 
     * @param out the output array
     * @param odx the output index
     * @param in the input array
     * @param idx the input index
     */
    public static void decTrunk(byte[] out, int odx, char[] in, int idx) {
        int k = 0;
        for (int i = idx; i < idx + 4; ++i) {
            k = (k << 6) + DEC_TBL[in[i]];
        }
        for (int i = odx, j = 16; i < odx + 3; ++i, j -= 8) {
            out[i] = (byte) (k >> j & 0xff);
        }
    }

    /**
     * Decode the Base64 tail, which is not a complete block. The length must be 2 or 3.
     * 
     * @param out the output array
     * @param odx the output index
     * @param in the input array
     * @param idx the input index
     * @param len the tail length
     */
    public static void decTail(byte[] out, int odx, char[] in, int idx, int len) {
        int k = 0;
        for (int i = idx, j = 0; i < idx + 4; ++i, ++j) {
            k = (k << 6) + (j < len ? DEC_TBL[in[i]] : 0);
        }

        for (int i = odx, j = 16; i < odx + len - 1; ++i, j -= 8) {
            out[i] = (byte) (k >> j & 0xff);
        }
    }

    /**
     * Decode the specified Base64 compatible string into byte array.
     * 
     * @param input the input string
     * @return the decoded bytes
     * @throws IllegalArgumentException if the input string is not Base64 compatible
     */
    public static byte[] decode(String input) {
        char[] in = tidyBase64String(input);
        int len = in.length;
        int mod = len % 4;
        int outLen = len / 4 * 3 + (mod > 0 ? mod - 1 : 0);

        byte[] out = new byte[outLen];
        int i = 0, j = 0;
        for (; i < len - 3; i += 4, j += 3) {
            decTrunk(out, j, in, i);
        }
        if (i != len) {
            decTail(out, j, in, i, mod);
        }

        return out;
    }
}
