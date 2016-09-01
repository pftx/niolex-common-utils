/**
 * ZLibUtils.java
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
package org.apache.niolex.commons.compress;

import java.io.IOException;

import com.fasterxml.jackson.databind.JavaType;

/**
 * ZLib压缩工具
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0
 * @since 1.0
 */
public abstract class ZLibUtil {

    // The internal instance to make the new API compatible with the old API.
    private static final ZLiber ZLIB = new ZLiber();

    /**
     * Get the internal compressor.
     *
     * @return the internal compressor
     */
    public static final Compressor getInstance() {
        return ZLIB;
    }

    /**
     * 压缩
     *
     * @param data
     *            待压缩数据
     * @return byte[] 压缩后的数据
     */
    public static byte[] compress(byte[] data) {
        return ZLIB.compress(data);
    }

    /**
     * 解压缩
     *
     * @param data
     *            待解压缩的数据
     * @return byte[] 解压缩后的数据
     */
    public static byte[] decompress(byte[] data) {
        return ZLIB.decompress(data);
    }

    /**
     * 压缩字符串
     *
     * @param str 准备压缩的字符串
     * @return 压缩后的数据
     */
    public static final byte[] compressString(String str) {
        return ZLIB.compressString(str);
    }

    /**
     * 解压缩字符串
     *
     * @param data 准备解压的数据
     * @return 解压后的字符串
     */
    public static final String decompressString(byte[] data) {
        return ZLIB.decompressString(data);
    }

    /**
     * 压缩对象，使用Json作为内部表现形式
     *
     * @param value the object to be compressed
     * @return the compressed data
     * @throws IOException if something goes wrong in Jackson JSON
     */
    public static byte[] compressObj(Object value) throws IOException {
        return ZLIB.compressObj(value);
    }

    /**
     * 解压缩对象
     *
     * @param <T> the value class type
     * @param data the binary data
     * @param valueType the value class type
     * @return the object
     * @throws IOException if something goes wrong in Jackson JSON
     */
    public static final <T> T decompressObj(byte[] data, Class<T> valueType) throws IOException {
    	return ZLIB.decompressObj(data, valueType);
    }

    /**
     * 解压缩对象
     *
     * @param <T> the value class type
     * @param data the binary data
     * @param valueType the value class type
     * @return the object
     * @throws IOException if something goes wrong in Jackson JSON
     */
    public static final <T> T decompressObj(byte[] data, JavaType valueType) throws IOException {
        return ZLIB.decompressObj(data, valueType);
    }

}