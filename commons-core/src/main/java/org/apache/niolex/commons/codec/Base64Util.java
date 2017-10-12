/**
 * Base64Util.java
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

/**
 * This utility encodes bytes into base64 string and vice versa. We use the MINE encoding without
 * line feeds as the default method. And we support standard MIME and URL file name safe encoding
 * too. Please choose the correct methods as your requirements.
 *
 * @version 1.0.0
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 */
public abstract class Base64Util {

    /**
     * Encode Byte Array into Base64 String using MINE encoding without line feeds.
     *
     * @param data the bytes to be encoded
     * @return the encoded base64 string
     * @throws IllegalArgumentException if the parameter is null
     */
    public static String byteToBase64(byte[] data) {
        if (data == null)
            throw new IllegalArgumentException("The parameter should not be null!");

        return CommonBase64.encode(data);
    }

    /**
     * Encode Byte Array into Base64 String using MINE encoding with line feeds every 76 chars.
     *
     * @param data the bytes to be encoded
     * @return the encoded base64 string
     * @throws IllegalArgumentException if the parameter is null
     */
    public static String byteToBase64LF(byte[] data) {
        if (data == null)
            throw new IllegalArgumentException("The parameter should not be null!");

        return CommonBase64.encode(data, true, 76, false);
    }

    /**
     * Encode Byte Array into Base64 String using URL and file name safe encoding without line feeds.
     *
     * @param data the bytes to be encoded
     * @return the encoded base64 string
     * @throws IllegalArgumentException if the parameter is null
     */
    public static String byteToBase64URL(byte[] data) {
        if (data == null)
            throw new IllegalArgumentException("The parameter should not be null!");

        return CommonBase64.encodeURL(data);
    }

    /**
     * Decode Base64 encoded string into Byte Array. We support MINE encoding with or without line feeds, Standard
     * Base64 encoding, Base64 using URL and file name safe encoding table with or without line feeds.
     * <p>
     * We try our best to recognize as many variants as possible.
     * </p>
     *
     * @param str the encoded base64 string
     * @return the original bytes
     * @throws IllegalArgumentException if the parameter is null or is not in valid base64 format
     */
    public static byte[] base64ToByte(String str) {
        if (str == null)
            throw new IllegalArgumentException("The parameter should not be null!");

        return CommonBase64.decode(str);
    }

    /**
     * Check whether the input string is Base64 format compatible.
     * 
     * @param str the input string
     * @return true if the input string is Base64 compatible, false otherwise
     */
    public static boolean isBase64(String str) {
        if (str == null)
            throw new IllegalArgumentException("The parameter should not be null!");
        return CommonBase64.isBase64(str);
    }
}
