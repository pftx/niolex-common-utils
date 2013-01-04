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

import org.apache.commons.codec.binary.Base64;
import org.apache.niolex.commons.bean.Pair;

/**
 * KVBase64Util是一个用来实现在byte数组和64进制字符串之间相互转换的工具类
 * KVBase64Util自己并不实现Base64的功能，而是封装了apache的commons库里的相关功能。
 * 本类依赖于commons-codec-1.3.jar+
 *
 * 目前提供的功能如下：
 * 1. public static String kvToBase64(byte[] key, byte[] value)
 * 将KV的byte数组转换成64进制字符串
 *
 * 2. public static Pair<byte[], byte[]> base64toKV(String str)
 * 将64进制字符串转换成KV byte数组
 *
 * @see org.apache.commons.codec.binary.Base64
 * @category niolex-common-utils -> 公共库 -> 编码加密
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2012-12-27
 */
public class KVBase64Util {

    /**
     * 将KV的byte数组转换成64进制字符串
     * Encode KV Byte Array into Base64 String
     *
     * @param key 待转换的key数组，系统限制长度最大63字节
     * @param value 待转换的value数组，系统限制key+value总长度最大512字节
     * @return 转换后的64进制字符串
     * @throws IllegalStateException 假如用户的环境不支持ASCII编码
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
        return StringUtil.asciiByteToStr(Base64.encodeBase64URLSafe(data));
    }

    /**
     * 将64进制字符串转换成KV byte数组
     * Decode Base64 String into KV Byte Array
     *
     * @param str 待转换的64进制字符串
     * @return 转换后的KV byte数组,其中a是Key，b是Value
     * @throws IllegalStateException 假如用户的环境不支持ASCII编码
     */
    public static Pair<byte[], byte[]> base64toKV(String str) {
        if (str == null)
            throw new IllegalArgumentException("The parameter should not be null!");
        byte data[] = Base64.decodeBase64(StringUtil.strToAsciiByte(str));
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
