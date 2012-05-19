/**
 * Base16Util.java
 *
 * Copyright 2011 Baidu, Inc.
 *
 * Baidu licenses this file to you under the Apache License, version 2.0
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
 * Base16Util是一个用来实现在byte数组和16进制字符串之间相互转换的工具类
 * 
 * 目前提供的功能如下：
 * 1. public static final String byteToBase16(byte[] bytes)
 * 将byte数组转换成16进制字符串
 * 
 * 2. public static final byte[] base16toByte(String str)
 * 将16进制字符串转换成byte数组
 * 
 * @used MD5Util
 * @category veyron code -> 公共库 -> 编码加密
 * @author <a href="mailto:xiejiyun@baidu.com">Xie, Jiyun</a>
 * @version 1.0.0
 */
public class Base16Util {

    /**
     * 将byte数组转换成16进制字符串
     * 
     * @param bytes 待转换的byte数组
     * @return 转换后的16进制字符串
     * @throws IllegalArgumentException 假如byte数组是null
     */
    public static final String byteToBase16(byte[] bytes) {
        if (bytes == null)
            throw new IllegalArgumentException("The parameter should not be null!");
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(Integer.toHexString((b & 0xF0) >> 4));
            sb.append(Integer.toHexString(b & 0x0F));
        }
        return sb.toString();
    }

    /**
     * 将16进制字符串转换成byte数组
     * 
     * @param str 待转换的16进制字符串
     * @return 转换后的byte数组，假如待转换的16进制字符串不可以被2整除则返回空数组
     * @throws IllegalArgumentException 假如输入字符串是null
     * @throws NumberFormatException 假如待转换的16进制字符串含有不可以解析成16进制数字的字符
     */
    public static final byte[] base16toByte(String str) {
        if (str == null)
            throw new IllegalArgumentException("The parameter should not be null!");
        if (str.length() % 2 != 0) {
            return new byte[] {};
        } else {
            char[] hex = str.toCharArray();
            byte[] bytes = new byte[hex.length / 2];
            for (int i = 0, j = 0; i < hex.length; i += 2, ++j) {
                bytes[j] = (byte) ((Integer.parseInt(String.valueOf(hex[i]), 16) << 4) | (Integer.parseInt(String
                        .valueOf(hex[i + 1]), 16)));
            }
            return bytes;
        }
    }
    
}
