/**
 * CipherUtil.java
 *
 * Copyright 2012 Niolex, Inc.
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

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;

import org.apache.commons.lang.ArrayUtils;

/**
 * 有些Cipher无法处理大数据，只能处理固定的块大小。使用本工具可以解决这个问题。
 *
 * @used RSAUtil
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2012-4-9$
 */
public abstract class CipherUtil {

    /**
     * For some kind of cipher, e.g. RSA, can not handle bytes larger than a fixed block size.
     * So, this method is just for this kind of cipher to handle large bytes.
     *
     * @param cipher
     * @param blockSize
     * @param input
     * @return the processed bytes
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws ShortBufferException
     */
    public static byte[] process(Cipher cipher, int blockSize, byte[] input) throws IllegalBlockSizeException, BadPaddingException, ShortBufferException {
        if (input.length <= blockSize) {
            return cipher.doFinal(input);
        }
        final int OUTPUT_SIZE = (input.length + blockSize - 1) / blockSize * cipher.getOutputSize(blockSize);

        byte[] output = new byte[OUTPUT_SIZE];
        int outputIndex = 0;
        for (int i = 0; ;i += blockSize) {
            if (i + blockSize < input.length)
                outputIndex += cipher.doFinal(input, i, blockSize, output, outputIndex);
            else {
                outputIndex += cipher.doFinal(input, i, input.length - i, output, outputIndex);
                break;
            }
        }
        return ArrayUtils.subarray(output, 0, outputIndex);
    }

}
