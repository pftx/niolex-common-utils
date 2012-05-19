/**
 * EditorCoder.java
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
 * @version @version@, $Date: 2011-7-13$
 * 
 */
public class EditorCoder implements Coder {
    private RC2Coder rc2;
    private Blowfish2Coder bf2;

    /**
     * 初始化密钥和IV参数
     * @param key
     * @throws Exception
     */
    public void initKey(String key) throws Exception {
        String[] keys = key.split("\\^");
        rc2 = new RC2Coder();
        rc2.secureInitKey(keys[1]);
        
        bf2 = new Blowfish2Coder();
        bf2.initKey(keys[0]);
    }

    /**
     * 加密
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public String encode(String data) throws Exception {
        int random = (int)(System.nanoTime() % Integer.MAX_VALUE | 0x1000);
        String rs = Integer.toString(random);
        int rBits = rs.charAt(rs.length() - 2) - '0';
        int ver = (random >> rBits) & 3;
        ver = (ver & 1) ^ (ver >> 1);
        switch(ver) {
            case 0:
                return rc2.encode(data) + '#' + random;
            case 1:
                return bf2.encode(data) + '#' + random;
        }
        throw new IllegalArgumentException("Version not supported: " + ver);
    }

    /**
     * 解密
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public String decode(String data) throws Exception {
        String[] strs = data.split("#");
        int rBits = strs[1].charAt(strs[1].length() - 2) - '0';
        int ver = (Integer.parseInt(strs[1]) >> rBits) & 3;
        ver = (ver & 1) ^ (ver >> 1);
        switch(ver) {
            case 0:
                return rc2.decode(strs[0]);
            case 1:
                return bf2.decode(strs[0]);
        }
        throw new IllegalArgumentException("Version not supported: " + ver);
    }

    /* (non-Javadoc)
     * @see com.baidu.api.core.coder.Coder#decrypt(byte[])
     */
    @Override
    public byte[] decrypt(byte[] data) throws Exception {
        throw new UnsupportedOperationException("This method has not been implemented.");
    }

    /* (non-Javadoc)
     * @see com.baidu.api.core.coder.Coder#encrypt(byte[])
     */
    @Override
    public byte[] encrypt(byte[] data) throws Exception {
        throw new UnsupportedOperationException("This method has not been implemented.");
    }
}
