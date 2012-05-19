/**
 * DESCoder.java
 *
 * Copyright 2010 @company@, Inc.
 *
 * @company@ licenses this file to you under the Apache License, version 2.0
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

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.niolex.commons.codec.Base64Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * DES encoding utility class.
 * 
 * @author @author@ (@author-email@)
 * 
 * @version @version@, $Date: 2010-8-31$
 * 
 */
public class DESCoder {
    private static final Logger log = LoggerFactory.getLogger(DESCoder.class);

    /**
     * ALGORITHM 算法 <br>
     * 可替换为以下任意一种算法，同时key值的size相应改变。
     * 
     * <pre>
     * DES                  key size must be equal to 56  
     * DESede(TripleDES)    key size must be equal to 112 or 168  
     * AES                  key size must be equal to 128, 192 or 256,but 192 and 256 bits may not be available  
     * Blowfish             key size must be multiple of 8, and can only range from 32 to 448 (inclusive)  
     * RC2                  key size must be between 40 and 1024 bits  
     * RC4(ARCFOUR)         key size must be between 40 and 1024 bits
     * </pre>
     * 
     * 在Key toKey(byte[] key)方法中使用下述代码 <code>SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);</code> 替换 <code>  
     * DESKeySpec dks = new DESKeySpec(key);  
     * SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);  
     * SecretKey secretKey = keyFactory.generateSecret(dks);  
     * </code>
     */

    private static final String ALGORITHM = "DES";
    private static String KEY;
    
    /**
     * 初始化密钥和IV参数
     * @param key
     * @throws Exception
     */
    public static void initKey(String key) throws Exception {
        KEY = key;
    }

    /**
     * 转换密钥<br>
     * 
     * @param key
     * @return
     * @throws Exception
     */
    private static Key toKey(byte[] key) throws Exception {
        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        SecretKey secretKey = keyFactory.generateSecret(dks);

        // 当使用其他对称加密算法时，如AES、Blowfish等算法时，用下述代码替换上述三行代码
        // SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);

        return secretKey;
    }
    
    private static Key toKey(String key) throws Exception {
        return toKey(Base64Util.base64toByte(key));
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
     * @return
     * @throws Exception
     */
    public static String genKey(String seed) throws UnsupportedEncodingException {
        log.info("The current seed is set to: " + seed);
        SecureRandom secureRandom = null;

        if (seed != null) {
            secureRandom = new SecureRandom(Base64Util.base64toByte(seed));
        } else {
            secureRandom = new SecureRandom();
        }

        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
        }
        kg.init(secureRandom);

        SecretKey secretKey = kg.generateKey();

        String curKey = Base64Util.byteToBase64(secretKey.getEncoded());
        log.info("The encripted key is: " + curKey);
        return curKey;
    }


    /**
     * 加密
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, String key) throws Exception {
        Key k = toKey(key);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, k);

        return cipher.doFinal(data);
    }

    /**
     * 解密
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, String key) throws Exception {
        Key k = toKey(key);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, k);

        return cipher.doFinal(data);
    }

    public static String encode(String... args) {
        if (args == null || args.length == 0)
            return "";
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < args.length; ++i) {
            String str = args[i];
            sb.append(str);
            if (i < args.length - 1)
                sb.append(']').append(i).append('[');
        }
        sb.append("]");
        try {
            String encoded = Base64Util.byteToBase64(encrypt(sb.toString().getBytes("UTF-8"), KEY));
            
            int i = 0;
            for (; i < encoded.length() - 1; ++i) {
                if (encoded.charAt(encoded.length() - i - 1) != '=')
                    break;
            }
            return encoded.substring(0, encoded.length() - i) + '-' + i;
        } catch (Exception e) {
            log.warn("Error occured when generate the ticket: {}.", e.getMessage());
        }
        return "";
    }
    
    public static String decode(String arg) {
        if (arg == null || arg.length() < 3)
            return "";
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(arg.substring(0, arg.length() - 2));
            int i = arg.charAt(arg.length() - 1) - '0';
            while (i-- > 0)
                sb.append('=');
            return new String(decrypt(Base64Util.base64toByte(sb.toString()), KEY), "UTF-8");
        } catch (Exception e) {
            log.warn("Error occured when decode the ticket: {}.", e.getMessage());
        }
        return "";
    }

}
