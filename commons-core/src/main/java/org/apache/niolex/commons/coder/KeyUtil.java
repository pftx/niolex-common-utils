/**
 * KeyUtil.java
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

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.apache.niolex.commons.codec.Base64Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generate Key for Coders, encoded in Base64.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2011-7-14$
 */
public abstract class KeyUtil {
    private static final Logger LOG = LoggerFactory.getLogger(KeyUtil.class);

    public static final int DEFAULT_IV = 128;

    /**
     * Get the KeyGenerator of this algorithm.
     *
     * <br>
     * The algorithms supported by the default JDK:
     * 
     * <pre>
     * DES                  key size must be equal to 64
     * DESede(TripleDES)    key size must be equal to 112 or 168
     * AES                  key size must be equal to 128, 192 or 256, but 192 and 256 bits may not be available
     * Blowfish             key size must be multiple of 8, and can only range from 32 to 448 (inclusive)
     * RC2                  key size must be between 40 and 1024 bits
     * RC4(ARCFOUR)         key size must be between 40 and 1024 bits
     * </pre>
     *
     * @param algorithm the algorithm you want
     * @return the KeyGenerator or null if algorithm not found
     */
    public static KeyGenerator getKeyGenerator(String algorithm) {
        try {
            return KeyGenerator.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Algorithm not found for: " + algorithm, e);
        }
    }

    /**
     * Generate Base64 encoded KEY for the specified algorithm, IV defaults to 128bits.
     *
     * @param algorithm the algorithm to be used
     * @return the generated KEY
     */
    public static String genKey(String algorithm) {
        return genKey(null, algorithm, DEFAULT_IV, 0);
    }

    /**
     * Generate Base64 encoded KEY for the specified algorithm.
     *
     * @param algorithm the algorithm to be used
     * @param ivSize The IV size, the unit is bits, must be multiple of 8
     * @param keySize the KEY size, the unit is bits set to 0 to use the default size
     * @return the generated KEY
     */
    public static String genKey(String algorithm, int ivSize, int keySize) {
        return genKey(null, algorithm, ivSize, keySize);
    }

    /**
     * Generate Base64 encoded KEY for the specified algorithm.
     *
     * @param seed the seed to init the secure random generator
     * @param algorithm the algorithm to be used
     * @param ivSize The IV size, the unit is bits, must be multiple of 8
     * @param keySize the KEY size, the unit is bits set to 0 to use the default size
     * @return the generated KEY
     */
    public static String genKey(String seed, String algorithm, final int ivSize, final int keySize) {
        LOG.info("Start to generate key for {}; the current seed is set to: {}.", algorithm, seed);
        SecureRandom secureRandom = null;
        final int ivLength = ivSize / 8;

        if (seed != null) {
            secureRandom = new SecureRandom(Base64Util.base64ToByte(seed));
        } else {
            secureRandom = new SecureRandom();
            secureRandom.setSeed(System.nanoTime());
        }

        KeyGenerator kg = getKeyGenerator(algorithm);
        if (keySize <= 0) {
            kg.init(secureRandom);
        } else {
            kg.init(keySize, secureRandom);
        }

        SecretKey key = kg.generateKey();
        byte[] secretKey = key.getEncoded();
        final int keyLength = secretKey.length;
        LOG.info("Secret key for {} generated; key class: {}, key length: {}bits.", algorithm, key.getClass().getName(),
                keyLength * 8);

        byte[] keyBytes = new byte[ivLength + keyLength];
        secureRandom.nextBytes(keyBytes);
        System.arraycopy(secretKey, 0, keyBytes, ivLength, keyLength);

        String curKey = Base64Util.byteToBase64(keyBytes);
        LOG.debug("The generate key is: {}.", curKey);
        return curKey;
    }

}
