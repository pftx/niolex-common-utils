/**
 * Blowfish2Coder.java
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

import org.apache.niolex.commons.codec.Base64Util;

/**
 * The coder backed by Blowfish2 algorithm.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2011-7-12$
 *
 */
public class Blowfish2Coder extends BaseCoder {

    private Blowfish2 blow;

    /**
     * Init the secret key of blow fish by the specified Base64 encoded key.
     *
     * @param key the base 64 encoded key
     */
    @Override
    public void initKey(String key) {
        byte[] secretKey = Base64Util.base64ToByte(key);
        blow = new Blowfish2(secretKey);
    }

    /**
     * Encryption.
     *
     * @param data the data to be encrypted
     * @return the object
     * @throws Exception if necessary
     */
    @Override
    public byte[] internalEncrypt(byte[] data) throws Exception {
        return blow.encrypt(data);
    }

    /**
     * Decryption.
     *
     * @param data the data to be decrypted
     * @return the object
     * @throws Exception if necessary
     */
    @Override
    public byte[] internalDecrypt(byte[] data) throws Exception {
        return blow.decrypt(data);
    }

}
