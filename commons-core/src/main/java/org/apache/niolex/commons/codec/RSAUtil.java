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

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

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
     * 用私钥解密通过公钥加密的数据
     *
     * @param data 通过公钥加密的数据
     * @param key 用来解密的私钥
     * @return 解密后的数据
     *
     * @throws NoSuchAlgorithmException 假如用户的JDK不支持RSA
     * @throws InvalidKeySpecException 假如根据privateKey生成密钥失败
     * @throws InvalidKeyException 假如输入的RSA私钥不合法
     * @throws NoSuchPaddingException 假如产生的密钥对有问题
     * @throws IllegalBlockSizeException 假如输入的加密的数据字节数不是BlockSize的整数倍
     * @throws BadPaddingException 假如输入的加密的数据填充数据错误
     * @throws ShortBufferException 如果给定的输出缓冲区太小而无法保存结果
     */
    public static byte[] decryptByPrivateKey(byte[] data, String key) throws NoSuchAlgorithmException, InvalidKeySpecException,
            InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, ShortBufferException {
        // 对密钥解密
        byte[] keyBytes = Base64Util.base64toByte(key);

        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return CipherUtil.process(cipher, 128, data);
    }

    /**
     * 用私钥解密通过公钥加密的数据
     *
     * @param data 通过公钥加密的数据
     * @param privateKey 用来解密的私钥
     * @return 解密后的数据
     *
     * @throws NoSuchAlgorithmException 假如用户的JDK不支持RSA
     * @throws InvalidKeySpecException 假如根据privateKey生成密钥失败
     * @throws InvalidKeyException 假如输入的RSA私钥不合法
     * @throws NoSuchPaddingException 假如产生的密钥对有问题
     * @throws IllegalBlockSizeException 假如输入的加密的数据字节数不是BlockSize的整数倍
     * @throws BadPaddingException 假如输入的加密的数据填充数据错误
     * @throws ShortBufferException 如果给定的输出缓冲区太小而无法保存结果
     */
    public static byte[] decryptByPrivateKey(byte[] data, Key privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException,
            InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, ShortBufferException {
        // 对数据解密
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return CipherUtil.process(cipher, 128, data);
    }

    /**
     * 用公钥解密通过私钥加密的数据
     *
     * @param data 通过私钥加密的数据
     * @param key 用来解密的公钥
     * @return 解密后的数据
     *
     * @throws NoSuchAlgorithmException 假如用户的JDK不支持RSA
     * @throws InvalidKeySpecException 假如根据privateKey生成密钥失败
     * @throws InvalidKeyException 假如输入的RSA私钥不合法
     * @throws NoSuchPaddingException 假如产生的密钥对有问题
     * @throws IllegalBlockSizeException 假如输入的加密的数据字节数不是BlockSize的整数倍
     * @throws BadPaddingException 假如输入的加密的数据填充数据错误
     * @throws ShortBufferException 如果给定的输出缓冲区太小而无法保存结果
     */
    public static byte[] decryptByPublicKey(byte[] data, String key) throws NoSuchAlgorithmException, InvalidKeySpecException,
            InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, ShortBufferException {
        // 对密钥解密
        byte[] keyBytes = Base64Util.base64toByte(key);

        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);

        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        return CipherUtil.process(cipher, 128, data);
    }

    /**
     * 用公钥加密数据
     *
     * @param data 等待加密的原始数据
     * @param key 用来加密的公钥
     * @return 加密后的数据
     *
     * @throws NoSuchAlgorithmException 假如用户的JDK不支持RSA
     * @throws InvalidKeySpecException 假如根据privateKey生成密钥失败
     * @throws InvalidKeyException 假如输入的RSA私钥不合法
     * @throws NoSuchPaddingException 假如产生的密钥对有问题
     * @throws IllegalBlockSizeException 假如输入的加密的数据字节数不是BlockSize的整数倍
     * @throws BadPaddingException 假如输入的加密的数据填充数据错误
     * @throws ShortBufferException 如果给定的输出缓冲区太小而无法保存结果
     */
    public static byte[] encryptByPublicKey(byte[] data, String key) throws NoSuchAlgorithmException, InvalidKeySpecException,
            InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, ShortBufferException {
        // 对公钥解密
        byte[] keyBytes = Base64Util.base64toByte(key);

        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return CipherUtil.process(cipher, 117, data);
    }

    /**
     * 用公钥加密数据
     *
     * @param data 等待加密的原始数据
     * @param publicKey 用来加密的公钥
     * @return 加密后的数据
     *
     * @throws NoSuchAlgorithmException 假如用户的JDK不支持RSA
     * @throws InvalidKeySpecException 假如根据privateKey生成密钥失败
     * @throws InvalidKeyException 假如输入的RSA私钥不合法
     * @throws NoSuchPaddingException 假如产生的密钥对有问题
     * @throws IllegalBlockSizeException 假如输入的加密的数据字节数不是BlockSize的整数倍
     * @throws BadPaddingException 假如输入的加密的数据填充数据错误
     * @throws ShortBufferException 如果给定的输出缓冲区太小而无法保存结果
     */
    public static byte[] encryptByPublicKey(byte[] data, Key publicKey) throws NoSuchAlgorithmException,InvalidKeySpecException,
            InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, ShortBufferException {
        // 取得公钥
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return CipherUtil.process(cipher, 117, data);
    }

    /**
     * 用私钥加密数据
     *
     * @param data 等待加密的原始数据
     * @param key 用来加密的私钥
     * @return 加密后的数据
     *
     * @throws NoSuchAlgorithmException 假如用户的JDK不支持RSA
     * @throws InvalidKeySpecException 假如根据privateKey生成密钥失败
     * @throws InvalidKeyException 假如输入的RSA私钥不合法
     * @throws NoSuchPaddingException 假如产生的密钥对有问题
     * @throws IllegalBlockSizeException 假如输入的加密的数据字节数不是BlockSize的整数倍
     * @throws BadPaddingException 假如输入的加密的数据填充数据错误
     * @throws ShortBufferException 如果给定的输出缓冲区太小而无法保存结果
     */
    public static byte[] encryptByPrivateKey(byte[] data, String key) throws NoSuchAlgorithmException, InvalidKeySpecException,
            InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, ShortBufferException {
        // 对密钥解密
        byte[] keyBytes = Base64Util.base64toByte(key);

        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        return CipherUtil.process(cipher, 117, data);
    }

    /**
     * 用私钥加密数据
     *
     * @param data 等待加密的原始数据
     * @param privateKey 用来加密的私钥
     * @return 加密后的数据
     *
     * @throws NoSuchAlgorithmException 假如用户的JDK不支持RSA
     * @throws InvalidKeySpecException 假如根据privateKey生成密钥失败
     * @throws InvalidKeyException 假如输入的RSA私钥不合法
     * @throws NoSuchPaddingException 假如产生的密钥对有问题
     * @throws IllegalBlockSizeException 假如输入的加密的数据字节数不是BlockSize的整数倍
     * @throws BadPaddingException 假如输入的加密的数据填充数据错误
     * @throws ShortBufferException 如果给定的输出缓冲区太小而无法保存结果
     */
    public static byte[] encryptByPrivateKey(byte[] data, Key privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException,
            InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, ShortBufferException {
        // 取得私钥
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        return CipherUtil.process(cipher, 117, data);
    }

}
