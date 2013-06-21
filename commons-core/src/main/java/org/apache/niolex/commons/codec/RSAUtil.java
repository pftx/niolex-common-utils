/**
 * RSAUtil.java
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

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import javax.crypto.Cipher;

/**
 * RSAUtil is a collection of methods encryption and decryption, sign and verify using RSA algorithm.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 */
public abstract class RSAUtil extends RSAHelper {

    /**
     * Use the private Key to sign the data.
     *
     * @param data the data to be signed
     * @param privateKey the key used for sign
     * @return the signature
     * @throws IllegalStateException If Your JDK don't support RSA.
     * @throws IllegalArgumentException If the parameter key is damaged
     */
    public static String sign(byte[] data, String privateKey) {
        // 取私钥匙对象
        PrivateKey priKey = getPrivateKey(privateKey);
        return sign(data, priKey);
    }

    /**
     * Use the private Key to sign the data.
     *
     * @param data the data to be signed
     * @param privateKey the key used for sign
     * @return the signature
     * @throws IllegalArgumentException If failed to sign the data.
     */
    public static String sign(byte[] data, PrivateKey privateKey) {
        try {
            // 用私钥对信息生成数字签名
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(privateKey);
            signature.update(data);

            return Base64Util.byteToBase64(signature.sign());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to sign the data.", e);
        }
    }

    /**
     * Verifies the integrity of the data by the passed-in signature.
     *
     * @param data the data need to be verified
     * @param publicKey the public key used to do verify
     * @param sign the signature
     * @return true if success, false otherwise
     * @throws IllegalStateException If Your JDK don't support RSA.
     * @throws IllegalArgumentException If the parameter key is damaged
     */
    public static boolean verify(byte[] data, String publicKey, String sign) {
        // 取公钥匙对象
        PublicKey pubKey = getPublicKey(publicKey);
        return verify(data, pubKey, sign);
    }

    /**
     * Verifies the integrity of the data by the passed-in signature.
     *
     * @param data the data need to be verified
     * @param publicKey the public key used to do verify
     * @param sign the signature
     * @return true if success, false otherwise
     * @throws IllegalArgumentException Failed to verify the data.
     */
    public static boolean verify(byte[] data, PublicKey publicKey, String sign) {
        try {
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(publicKey);
            signature.update(data);

            // 验证签名是否正常
            return signature.verify(Base64Util.base64toByte(sign));
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to verify the data.", e);
        }
    }

    /**
     * Use RSA to decrypt the data.
     *
     * @param data the data need to be decrypted
     * @param key the key used to do decryption
     * @return the decrypted data
     * @throws IllegalArgumentException Failed to decrypt the data.
     */
    public static byte[] decrypt(byte[] data, Key key) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return CipherUtil.process(cipher, 128, data);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to decrypt the data.", e);
        }
    }

    /**
     * Use the private Key to decrypt the data.
     *
     * @param data the data to be decrypted
     * @param key the key used to do decryption
     * @return the decrypted data
     * @throws IllegalStateException If Your JDK don't support RSA.
     * @throws IllegalArgumentException If failed to decrypt the data.
     */
    public static byte[] decryptByPrivateKey(byte[] data, String key) {
        Key privateKey = getPrivateKey(key);
        return decrypt(data, privateKey);
    }

    /**
     * Use the public Key to decrypt the data.
     *
     * @param data the data to be decrypted
     * @param key the key used to do decryption
     * @return the decrypted data
     * @throws IllegalStateException If Your JDK don't support RSA.
     * @throws IllegalArgumentException If failed to decrypt the data.
     */
    public static byte[] decryptByPublicKey(byte[] data, String key) {
        Key publicKey = getPublicKey(key);
        return decrypt(data, publicKey);
    }

    /**
     * Use RSA to encrypt the data.
     *
     * @param data the data need to be encrypted
     * @param key the key used to do encryption
     * @return the encrypted data
     * @throws IllegalArgumentException Failed to encrypt the data.
     */
    public static byte[] encrypt(byte[] data, Key key) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return CipherUtil.process(cipher, 117, data);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to encrypt the data.", e);
        }
    }

    /**
     * Use the private Key to encrypt the data.
     *
     * @param data the data to be encrypted
     * @param key the key used to do encryption
     * @return the encrypted data
     * @throws IllegalStateException If Your JDK don't support RSA.
     * @throws IllegalArgumentException If failed to encrypt the data.
     */
    public static byte[] encryptByPrivateKey(byte[] data, String key) {
        Key privateKey = getPrivateKey(key);
        return encrypt(data, privateKey);
    }

    /**
     * Use the public Key to encrypt the data.
     *
     * @param data the data to be encrypted
     * @param key the key used to do encryption
     * @return the encrypted data
     * @throws IllegalStateException If Your JDK don't support RSA.
     * @throws IllegalArgumentException If failed to encrypt the data.
     */
    public static byte[] encryptByPublicKey(byte[] data, String key) {
        Key publicKey = getPublicKey(key);
        return encrypt(data, publicKey);
    }

}
