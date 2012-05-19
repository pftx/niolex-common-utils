/**
 * RC2Coder.java
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
package org.apache.niolex.commons.coder;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.RC2ParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.ArrayUtils;
import org.apache.niolex.commons.codec.Base64Util;


/**
 * @author @author@ (@author-email@)
 * 
 * @version @version@, $Date: 2011-7-12$
 * 
 */
public class RC2Coder extends BaseCoder {

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

    private static final String ALGORITHM = "RC2";
    private static final String TRANSFORMATION = ALGORITHM + "/CBC/ISO10126Padding";

    private Key secretKey;
    private AlgorithmParameterSpec ivParam;

    /**
     * 转换密钥<br>
     * 
     * @param key
     * @return
     * @throws Exception
     */
    private static Key toKey(byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);

        return secretKey;
    }
    
    /**
     * 初始化密钥和IV参数
     * 
     * @param key
     * @throws Exception
     */
    public void initKey(String key) throws Exception {
        byte[] keyData = Base64Util.base64toByte(key);
        ivParam = new RC2ParameterSpec(128, keyData, 0);
        secretKey = toKey(ArrayUtils.subarray(keyData, 17, 33));
    }
    
    public void secureInitKey(String key) throws Exception {
        byte[] keyData = Base64Util.base64toByte(key);
        byte[] ivData = new byte[8];
        ivData[0] = keyData[3];
        ivData[1] = keyData[5];
        ivData[2] = keyData[7];
        ivData[3] = keyData[11];
        ivData[4] = keyData[13];
        ivData[5] = keyData[17];
        ivData[6] = keyData[19];
        ivData[7] = keyData[23];
        ivParam = new RC2ParameterSpec(128, ivData, 0);
        byte[] realKey = new byte[16];
        for (int i = 0, j = 1; i < 16; ++i, j += 3) {
            realKey[i] = (byte)(keyData[i * 2] ^ keyData[j % keyData.length]);
        }
        secretKey = toKey(realKey);
    }


    /**
     * 加密
     * 
     * @param data
     * @param key
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
     * @param key
     * @return
     * @throws Exception
     */
    public byte[] decrypt(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParam);

        return cipher.doFinal(data);
    }
}
