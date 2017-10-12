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

import java.nio.charset.Charset;

import org.apache.niolex.commons.codec.Base64Util;
import org.apache.niolex.commons.codec.StringUtil;

/**
 * The base coder, implementing encoding and decoding string by the corresponding binary methods.
 * The encrypted data is then encoded using Base64. So the input and output are both string.
 * <br>
 * We will catch all the checked exceptions and wrap it into {@link CoderException}.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2011-7-14$
 */
public abstract class BaseCoder implements Coder {

    public static final Charset ENC = StringUtil.UTF_8;

    protected abstract byte[] internalEncrypt(byte[] data) throws Exception;

    protected abstract byte[] internalDecrypt(byte[] data) throws Exception;

    @Override
    public byte[] encrypt(byte[] data) throws CoderException {
        try {
            return internalEncrypt(data);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new CoderException("Failed to encrypt data.", e);
        }
    }

    @Override
    public byte[] decrypt(byte[] data) throws CoderException {
        try {
            return internalDecrypt(data);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new CoderException("Failed to decrypt data.", e);
        }
    }

    @Override
    public String decode(String str) throws CoderException {
        return new String(decrypt(Base64Util.base64ToByte(str)), ENC);
    }

    @Override
    public String encode(String str) throws CoderException {
        return Base64Util.byteToBase64(encrypt(str.getBytes(ENC)));
    }

}
