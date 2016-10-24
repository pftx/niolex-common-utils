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
 * TripleDESCoder is a TripleDES Encoding utility class.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2011-4-21$
 */
public class TripleDESCoder extends BaseCoder {

    public static final String ALGORITHM = "DESede";
    public static final String TRANSFORMATION = ALGORITHM + "/CBC/ISO10126Padding";

    private Key secretKey;
    private IvParameterSpec ivParam;

    /**
     * Init the secret key and IV parameter by the specified Base64 encoded key.
     *
     * @param key the Base64 encoded key
     */
    @Override
    public void initKey(String key) {
        byte[] keyData = Base64Util.base64ToByte(key);
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
     * Encryption.
     *
     * @param data the data to be encrypted
     * @return the encrypted data
     * @throws Exception if necessary
     */
    @Override
    public byte[] internalEncrypt(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParam);

        return cipher.doFinal(data);
    }

    /**
     * Decryption.
     *
     * @param data the data to be decrypted
     * @return the decrypted data
     * @throws Exception if necessary
     */
    @Override
    public byte[] internalDecrypt(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParam);

        return cipher.doFinal(data);
    }

}
