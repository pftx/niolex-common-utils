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

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.Base64Variants;

/**
 * This utility encodes bytes into base64 string and vice versa. We use the MINE encoding without
 * line feeds as the default method. And we support standard MIME and URL file name safe encoding
 * too. Please choose the correct methods as your requirements.
 *
 * @version 1.0.0
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 */
public abstract class Base64Util {

    public static final Base64Variant DEFAULT = Base64Variants.MIME_NO_LINEFEEDS;
    public static final Base64Variant URL_SAFE = Base64Variants.MODIFIED_FOR_URL;

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
        return DEFAULT.encode(data);
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
        String s = DEFAULT.encode(data);
        if (s.length() <= 76) {
            return s;
        }

        // We need add CR LF every 76 chars.
        StringBuilder sb = new StringBuilder();
        sb.append(s.substring(0, 76));
        for (int i = 76; i < s.length(); i += 76) {
            int e = i + 76;
            if (e > s.length()) {
                e = s.length();
            }

            sb.append('\r');
            sb.append('\n');
            sb.append(s.substring(i, e));
        }
        return sb.toString();
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
        return URL_SAFE.encode(data);
    }

    /**
     * Decode Base64 using MINE encoding with or without line feeds String into Byte Array.
     *
     * @param str the encoded base64 string
     * @return the original bytes
     * @throws IllegalArgumentException if the parameter is null or if input is not valid base64 encoded data
     */
    public static byte[] base64ToByte(String str) {
        if (str == null)
            throw new IllegalArgumentException("The parameter should not be null!");
        return DEFAULT.decode(str);
    }

    /**
     * Decode Base64 using URL and file name safe encoding with or without line feeds String into Byte Array.
     *
     * @param str the encoded base64 string
     * @return the original bytes
     * @throws IllegalArgumentException if the parameter is null or if input is not valid base64 encoded data
     */
    public static byte[] base64ToByteURL(String str) {
        if (str == null)
            throw new IllegalArgumentException("The parameter should not be null!");
        return URL_SAFE.decode(str);
    }

}
