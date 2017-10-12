/**
 * ZLibUtil.java
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
 * The compress tool using the Deflater to compress data and Inflater to decompress data.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 3.0.1
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
     * Compress data by the Deflater algorithm.
     *
     * @param data the data to be compressed
     * @return byte[] the compressed data
     */
    public static byte[] compress(byte[] data) {
        return ZLIB.compress(data);
    }

    /**
     * Decompress data by the Inflater algorithm.
     *
     * @param data the compressed data
     * @return byte[] the original data
     */
    public static byte[] decompress(byte[] data) {
        return ZLIB.decompress(data);
    }

    /**
     * Compress the specified string using UTF-8 encoding and Deflater algorithm.
     *
     * @param str the string to be compressed
     * @return the compressed result
     */
    public static final byte[] compressString(String str) {
        return ZLIB.compressString(str);
    }

    /**
     * Decompress the specified data to string using UTF-8 encoding and Inflater algorithm.
     *
     * @param data the compressed data
     * @return the original string
     */
    public static final String decompressString(byte[] data) {
        return ZLIB.decompressString(data);
    }

    /**
     * Compress the specified object using JSON as the internal format and then use the Deflater algorithm.
     *
     * @param value the object to be compressed
     * @return the compressed data
     * @throws IOException if something goes wrong in Jackson JSON
     */
    public static byte[] compressObj(Object value) throws IOException {
        return ZLIB.compressObj(value);
    }

    /**
     * Decompress the data into object.
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
     * Decompress the data into object.
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