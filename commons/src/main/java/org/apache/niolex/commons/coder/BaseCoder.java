/**
 * BaseCoder.java
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
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * 
 * @version 1.0.0, $Date: 2011-7-14$
 * 
 */
public abstract class BaseCoder implements Coder {

    @Override
    public String decode(String str) throws Exception {
        return new String(decrypt(Base64Util.base64toByte(str)), ENC);
    }

    @Override
    public String encode(String str) throws Exception {
        return Base64Util.byteToBase64(encrypt(str.getBytes(ENC)));
    }

}
