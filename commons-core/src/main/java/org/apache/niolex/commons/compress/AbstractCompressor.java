/**
 * AbstractCompressor.java
 *
 * Copyright 2016 the original author or authors.
 *
 * We licenses this file to you under the Apache License, version 2.0
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
package org.apache.niolex.commons.compress;

import java.io.IOException;

import org.apache.niolex.commons.codec.StringUtil;

import com.fasterxml.jackson.databind.JavaType;

/**
 * With the two methods {@link #compress(byte[])} and {@link #decompress(byte[])}, we can implement all
 * the other methods here. So with this ABC, the compressor implementor will not need to implement them
 * again.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2016-4-8
 */
public abstract class AbstractCompressor implements Compressor {

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.compress.Compressor#compressString(java.lang.String)
     */
    @Override
    public byte[] compressString(String str) {
        return compress(str.getBytes(StringUtil.UTF_8));
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.compress.Compressor#decompressString(byte[])
     */
    @Override
    public String decompressString(byte[] data) {
        return new String(decompress(data), StringUtil.UTF_8);
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.compress.Compressor#compressObj(java.lang.Object)
     */
    @Override
    public byte[] compressObj(Object value) throws IOException {
        return compress(JacksonUtil.obj2bin(value));
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.compress.Compressor#decompressObj(byte[], java.lang.Class)
     */
    @Override
    public <T> T decompressObj(byte[] data, Class<T> valueType) throws IOException {
        return JacksonUtil.bin2Obj(decompress(data), valueType);
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.compress.Compressor#decompressObj(byte[], com.fasterxml.jackson.databind.JavaType)
     */
    @Override
    public <T> T decompressObj(byte[] data, JavaType valueType) throws IOException {
        return JacksonUtil.bin2Obj(decompress(data), valueType);
    }

}
