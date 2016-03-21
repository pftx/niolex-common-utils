/**
 * Coder.java
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
package org.apache.niolex.commons.coder;

import java.nio.charset.Charset;

import org.apache.niolex.commons.codec.StringUtil;

/**
 * 加密解密框架的基础接口。定义了操作二进制和操作字符串的方法。
 * The basic interface for the Coder framework.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2011-7-13$
 */
public interface Coder {

    Charset ENC = StringUtil.UTF_8;

    /**
     * 初始化密钥和IV参数
     *
     * @param key the base 64 encoded key
     */
    public void initKey(String key);

    /**
     * 加密
     *
     * @param data the data to be encrypted
     * @return the encrypted data
     * @throws Exception if necessary
     */
    public byte[] encrypt(byte[] data) throws Exception;

    /**
     * 解密
     *
     * @param data the data to be decrypted
     * @return the decrypted data
     * @throws Exception if necessary
     */
    public byte[] decrypt(byte[] data) throws Exception;

    /**
     * 加密字符串
     *
     * @param str the input string
     * @return the encoded string
     * @throws Exception if necessary
     */
    public String encode(String str) throws Exception;

    /**
     * 解密字符串
     *
     * @param str the input string
     * @return the decoded string
     * @throws Exception if necessary
     */
    public String decode(String str) throws Exception;
}
