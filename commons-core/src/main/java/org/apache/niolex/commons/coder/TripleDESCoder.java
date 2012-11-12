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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.ArrayUtils;
import org.apache.niolex.commons.codec.Base64Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * TripleDESCoder is a TripleDES Encode utility class
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2011-4-21$
 */
public class TripleDESCoder extends BaseCoder {
    private static final Logger LOG = LoggerFactory.getLogger(TripleDESCoder.class);

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
     * </code>
     */

    public static final String ALGORITHM = "DESede";
    public static final String TRANSFORMATION = ALGORITHM + "/CBC/ISO10126Padding";

    private Key secretKey;
    private IvParameterSpec ivParam;

    /**
     * 初始化密钥和IV参数
     * @param key
     * @throws IOException
     */
    public void initKey(String key) throws Exception {
        byte[] keyData = Base64Util.base64toByte(key);
        ivParam = new IvParameterSpec(keyData, 0, 8);
        secretKey = toKey(ArrayUtils.subarray(keyData, 24, 48));
    }

    /**
     * 转换密钥<br>
     *
     * @param key
     * @return
     * @throws Exception
     */
    private Key toKey(byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);
        // 当使用其他对称加密算法时，如AES、Blowfish等算法时，用下述代码替换上述三行代码
        /**
        * DESKeySpec dks = new DESKeySpec(key);
        * SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        * SecretKey secretKey = keyFactory.generateSecret(dks);
        */

        return secretKey;
    }


    /**
     * 生成密钥
     *
     * @return 采用Base64加密的密钥
     * @throws Exception
     */
    public static String genKey() throws UnsupportedEncodingException {
        return genKey(null);
    }

    /**
     * 生成密钥
     *
     * @param seed
     * @return 采用Base64加密的密钥
     * @throws Exception
     */
    public static String genKey(String seed) throws UnsupportedEncodingException {
        LOG.info("The current seed is set to: " + seed);
        SecureRandom secureRandom = null;

        if (seed != null) {
            secureRandom = new SecureRandom(Base64Util.base64toByte(seed));
        } else {
            secureRandom = new SecureRandom();
        }
        secureRandom.setSeed(secureRandom.generateSeed(32));

        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Encoding Algorithm not found for: {}", ALGORITHM, e);
        }
        kg.init(secureRandom);
        kg.init(168);

        SecretKey secretKey = kg.generateKey();

        byte[] keyBytes = new byte[48];
        secureRandom.nextBytes(keyBytes);
        System.arraycopy(secretKey.getEncoded(), 0, keyBytes, 24, 24);

        String curKey = Base64Util.byteToBase64(keyBytes);
        LOG.info("The encripted key is: " + curKey);
        return curKey;
    }


    /**
     * 加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public byte[] encrypt(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParam);

        return cipher.doFinal(data);
    }


    /**
     * 解密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public byte[] decrypt(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParam);

        return cipher.doFinal(data);
    }

}
