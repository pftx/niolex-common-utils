/**
 * RC2Coder.java
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
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.RC2ParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.niolex.commons.codec.Base64Util;


/**
 * RC2算法的加密解密实现工具类。
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2011-7-12$
 */
public class RC2Coder extends BaseCoder {

    private static final String ALGORITHM = "RC2";
    private static final String TRANSFORMATION = ALGORITHM + "/CBC/ISO10126Padding";

    private Key secretKey;
    private AlgorithmParameterSpec ivParam;

    /**
     * 初始化密钥和IV参数
     *
     * @param key the base 64 encoded key
     */
    @Override
    public void initKey(String key) {
        byte[] keyData = Base64Util.base64toByte(key);
        ivParam = new RC2ParameterSpec(128, keyData, 0);
        secretKey = new SecretKeySpec(keyData, keyData.length - 16, 16, ALGORITHM);
    }

    /**
     * 更安全的初始化密钥和IV参数
     *
     * @param key the base 64 encoded key
     * @throws Exception if necessary
     */
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
        secretKey = new SecretKeySpec(realKey, ALGORITHM);
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
