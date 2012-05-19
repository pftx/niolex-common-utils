/**
 * Blowfish2Coder.java
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


/**
 * @author @author@ (@author-email@)
 * 
 * @version @version@, $Date: 2011-7-12$
 * 
 */
public class Blowfish2Coder extends BaseCoder {

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

    private byte[] secretKey;
    
    /**
     * 初始化密钥和IV参数
     * @param key
     * @throws Exception
     */
    public void initKey(String key) throws Exception {
        secretKey = key.getBytes(ENC);
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
        Blowfish2 blow = new Blowfish2(secretKey);

        return blow.encrypt(data);
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
        Blowfish2 blow = new Blowfish2(secretKey);

        return blow.decrypt(data);
    }
}
