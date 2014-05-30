/**
 * SHAUtil.java
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * SHAUtil是一个用来产生SHA签名和校验SHA签名的工具类
 *
 * 目前提供的功能如下：
 * 1. public static final String sha1(String... plainTexts)
 * 对输入的字符串列表产生SHA签名
 *
 * 2. public static final boolean sha1Check(String md5, String... plainTexts)
 * 根据输入的SHA签名和字符串列表进行校验
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 */
public abstract class SHAUtil extends CipherUtil {

    /**
     * 对输入的字符串列表产生SHA签名
     *
     * @param plainTexts 用来产生SHA签名的字符串列表
     * @return 输入字符串列表的SHA签名
     * @throws NoSuchAlgorithmException 当用户的JDK不支持SHA哈希算法时
     */
    public static final String sha1(String... plainTexts) {
        MessageDigest md = getInstance("SHA");
        digest(md, plainTexts);
        byte bytes[] = md.digest();

        return Base16Util.byteToBase16(bytes);
    }

    /**
     * 根据输入的SHA签名和字符串列表进行校验
     *
     * @param sha1 用来进行校验的SHA签名
     * @param plainTexts 用来进行校验的字符串列表
     * @return 如果通过返回true，失败返回false
     * @throws NoSuchAlgorithmException 当用户的JDK不支持SHA哈希算法时
     */
    public static final boolean sha1Check(String sha1, String... plainTexts) {
        return sha1.equalsIgnoreCase(sha1(plainTexts));
    }

}
