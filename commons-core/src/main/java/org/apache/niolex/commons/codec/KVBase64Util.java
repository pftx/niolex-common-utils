/**
 * KVBase64Util.java
 *
 * Copyright 2012 The original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.niolex.commons.codec;

import org.apache.niolex.commons.bean.Pair;

/**
 * KVBase64Util translate two bytes array(the key and value) into one base64 string.
 * This is very useful if user want to pass their KV data into some kind of storage.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2012-12-27
 */
public class KVBase64Util {

    /**
     * Encode two Bytes Array(the KV) into Base64 String using URL and file name safe encoding.
     *
     * @param key the key array, 63 bytes maximum
     * @param value the value array, the maximum total size of key+value is 512 bytes
     * @return the encoded Base64 string using URL and file name safe encoding
     * @throws IllegalArgumentException any precondition not fulfilled
     */
    public static String kvToBase64(byte[] key, byte[] value) {
        if (key == null || value == null)
            throw new IllegalArgumentException("The parameter should not be null!");
        int size = key.length + value.length;
        if (key.length > 63 || size > 512) {
            throw new IllegalArgumentException("The KV is too large!");
        }

        byte data[] = new byte[(size + 3) / 3 * 3];
        int start = data.length - size;
        int first = (key.length << 2) + start;
        data[0] = (byte) first;
        System.arraycopy(key, 0, data, start, key.length);
        System.arraycopy(value, 0, data, start + key.length, value.length);
        return Base64Util.byteToBase64URL(data);
    }

    /**
     * Decode Base64 String using URL and file name safe encoding into KV Bytes Array.
     *
     * @param str the Base64 string using URL and file name safe encoding
     * @return the decoded two Bytes Array(the KV) a is key, b is value
     * @throws IllegalArgumentException any precondition not fulfilled or invalid encoding
     */
    public static Pair<byte[], byte[]> base64toKV(String str) {
        if (str == null)
            throw new IllegalArgumentException("The parameter should not be null!");
        byte data[] = Base64Util.base64ToByteURL(str);
        int first = data[0] & 0xff;
        int start = first & 0x3;
        first = first >> 2;
        byte key[] = new byte[first];
        first = data.length - first - start;
        if (first < 0) {
            throw new IllegalArgumentException("The parameter is not KV encoded!");
        }
        byte value[] = new byte[first];
        System.arraycopy(data, start, key, 0, key.length);
        System.arraycopy(data, data.length - first, value, 0, value.length);
        return new Pair<byte[], byte[]>(key, value);
    }

}
