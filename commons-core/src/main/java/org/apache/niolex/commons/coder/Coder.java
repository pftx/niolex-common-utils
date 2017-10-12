/**
 * Coder.java
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

/**
 * The basic interface for the Coder framework.
 * <br>
 * <br>
 * ALGORITHM <br>
 * The common algorithms supported by JDK:
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
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2011-7-13$
 */
public interface Coder {

    /**
     * Init the secret KEY and possibly IV parameter for this coder.
     *
     * @param key the Base64 encoded key
     * @throws CoderException if necessary
     */
    public void initKey(String key) throws CoderException;

    /**
     * Encrypt the data.
     *
     * @param data the data to be encrypted
     * @return the encrypted data
     * @throws CoderException if necessary
     */
    public byte[] encrypt(byte[] data) throws CoderException;

    /**
     * Decrypt the data.
     *
     * @param data the data to be decrypted
     * @return the decrypted data
     * @throws CoderException if necessary
     */
    public byte[] decrypt(byte[] data) throws CoderException;

    /**
     * Encode the string.
     *
     * @param str the input string
     * @return the encoded string
     * @throws CoderException if necessary
     */
    public String encode(String str) throws CoderException;

    /**
     * Decode the string.
     *
     * @param str the input string
     * @return the decoded string
     * @throws CoderException if necessary
     */
    public String decode(String str) throws CoderException;

}
