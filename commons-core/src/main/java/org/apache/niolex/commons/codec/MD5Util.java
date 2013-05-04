/**
 * MD5Util.java
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

import static org.apache.niolex.commons.codec.StringUtil.strToUtf8Byte;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * MD5Util是一个用来产生MD5签名和校验MD5签名的工具类
 *
 * 目前提供的功能如下：
 * 1. public static final String md5(String... plainTexts)
 * 对输入的字符串列表产生MD5签名
 *
 * 2. public static final boolean md5Check(String md5, String... plainTexts)
 * 根据输入的MD5签名和字符串列表进行校验
 *
 * @used 暂无项目使用
 * @category niolex-common-utils -> 公共库 -> 编码加密
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 */
public abstract class MD5Util {

    /**
     * 对输入的字符串列表产生MD5签名
     *
     * @param plainTexts 用来产生MD5签名的字符串列表
     * @return 输入字符串列表的MD5签名
     */
    public static final String md5(String... plainTexts) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("The runtime doesn't support MD5 algorithm.", e);
        }
        for (String plainText : plainTexts) {
        	if (plainText == null) {
        	    // This is the magic code for null string.
        		md.update((byte) 216);
        		continue;
        	}
            md.update(strToUtf8Byte(plainText));
        }
        byte bytes[] = md.digest();

        return Base16Util.byteToBase16(bytes);
    }

    /**
     * 根据输入的MD5签名和字符串列表进行校验
     *
     * @param md5 用来进行校验的MD5签名
     * @param plainTexts 用来进行校验的字符串列表
     * @return 如果通过返回true，失败返回false
     * @throws NoSuchAlgorithmException 当用户的JDK不支持MD5哈希算法时
     */
    public static final boolean md5Check(String md5, String... plainTexts) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("The runtime doesn't support MD5 algorithm.", e);
        }
        for (String plainText : plainTexts) {
        	if (plainText == null) {
        		md.update((byte) 216);
        		continue;
        	}
            md.update(strToUtf8Byte(plainText));
        }
        byte bytes[] = md.digest();

        return Base16Util.byteToBase16(bytes).equalsIgnoreCase(md5);
    }

}
