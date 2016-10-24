/**
 * DESCoder.java
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
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.niolex.commons.codec.Base64Util;

/**
 * DES encoding utility class.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2010-8-31$
 */
public class DESCoder extends BaseCoder {
    private static final String ALGORITHM = "DES";
    public static final String TRANSFORMATION = "DES/CBC/PKCS5Padding";
    private IvParameterSpec ivParam;
    private Key key;

    /**
     * Init the secret key and IV parameter by the specified Base64 encoded key.
     *
     * @param key the base 64 encoded key
     */
    @Override
    public void initKey(String key) {
        byte[] keyData = Base64Util.base64ToByte(key);
        ivParam = new IvParameterSpec(keyData, 0, 8);
        initKey(keyData);
    }

    protected void initKey(byte[] keyData) {
        try {
            DESKeySpec dks = new DESKeySpec(keyData, keyData.length - 8);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            this.key = keyFactory.generateSecret(dks);
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
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParam);

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
        cipher.init(Cipher.DECRYPT_MODE, key, ivParam);

        return cipher.doFinal(data);
    }

    /**
     * Encode multiple string together into a Base64 string.
     *
     * @param args the arguments to be encoded
     * @return the encoded string
     */
    public String encodes(String... args) {
        if (args == null || args.length == 0)
            return "";

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < args.length; ++i) {
            sb.append(args[i]);
            if (i < args.length - 1)
                sb.append(']').append(i).append('[');
        }
        sb.append("]");

        String encoded = encode(sb.toString());

        int i = encoded.indexOf('=');
        int l = encoded.length();
        i = i < 0 ? l : i;
        return encoded.substring(0, i) + '-' + (l - i);
    }

    /**
     * Decode the argument into plain text
     *
     * @param arg the argument to be decoded
     * @return the plain text
     */
    public String decodes(String arg) {
        if (arg == null || arg.length() < 3)
            return "";
        
        StringBuilder sb = new StringBuilder();
        sb.append(arg.substring(0, arg.length() - 2));
        int i = arg.charAt(arg.length() - 1) - '0';
        
        while (i-- > 0)
            sb.append('=');
        return decode(sb.toString());
    }

}
