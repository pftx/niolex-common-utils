/**
 * TripleDESCoder.java
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

import java.security.Key;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.niolex.commons.codec.Base64Util;


/**
 * TripleDESCoder is a TripleDES Encode utility class
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2011-4-21$
 */
public class TripleDESCoder extends BaseCoder {

    /**
     * ALGORITHM 算法 <br>
     * 可替换为以下任意一种算法，同时key值的size相应改变。
     *
     * <pre>
     * DES                  key size must be equal to 56
     * DESede(TripleDES)    key size must be equal to 112 or 168
     * AES                  key size must be equal to 128, 192 or 256, but 192 and 256 bits may not be available
     * Blowfish             key size must be multiple of 8, and can only range from 32 to 448 (inclusive)
     * RC2                  key size must be between 40 and 1024 bits
     * RC4(ARCFOUR)         key size must be between 40 and 1024 bits
     * </pre>
     *
     */

    public static final String ALGORITHM = "DESede";
    public static final String TRANSFORMATION = ALGORITHM + "/CBC/ISO10126Padding";

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
        ivParam = new IvParameterSpec(keyData, 0, 8);
        try {
            // DESedeKey always length 24 bytes
            KeySpec keySpec = new DESedeKeySpec(keyData, keyData.length - 24);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            secretKey = keyFactory.generateSecret(keySpec);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to init this key.", e);
        }
    }

    /**
     * 加密
     *
     * @param data the data to be encrypted
     * @return the object
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
     * @return the object
     * @throws Exception if necessary
     */
    @Override
    public byte[] decrypt(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParam);

        return cipher.doFinal(data);
    }

}
