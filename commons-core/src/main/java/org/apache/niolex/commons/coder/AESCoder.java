/**
 * AESCoder.java
 *
 * Copyright 2010 Niolex, Inc.
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

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.niolex.commons.codec.Base64Util;


/**
 * AES encoding utility class.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2010-8-31$
 */
public class AESCoder extends BaseCoder {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = ALGORITHM + "/CBC/ISO10126Padding";

    private Key secretKey;
    private IvParameterSpec ivParam;

    /**
     * 初始化密钥和IV参数
     *
     * @param key the base 64 encoded key
     */
    @Override
    public void initKey(String key) {
        byte[] keyData = Base64Util.base64toByte(key);
        ivParam = new IvParameterSpec(keyData, 0, 16);
        secretKey = new SecretKeySpec(keyData, 16, keyData.length - 16, ALGORITHM);
    }

    /**
     * 加密
     *
     * @param data the data to be encrypted
     * @return the encrypted data
     * @throws Exception if necessary
     */
    @Override
    public byte[] encrypt(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParam);

        return cipher.doFinal(data);
    }

    /**
     * 解密
     *
     * @param data the data to be decrypted
     * @return the decrypted data
     * @throws Exception if necessary
     */
    @Override
    public byte[] decrypt(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParam);

        return cipher.doFinal(data);
    }

}
