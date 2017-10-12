/**
 * GZipUtil.java
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
 * Using the GZIP algorithm ({@link GZiper}) to compress data and decompress data.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2011-3-28$
 */
public abstract class GZipUtil {

    // The internal instance to make the new API compatible with the old API.
    private static final GZiper GZIP = new GZiper();

    /**
     * Get the internal compressor.
     *
     * @return the internal compressor
     */
    public static final Compressor getInstance() {
        return GZIP;
    }

    /**
     * Compress data by the GZIP algorithm.
     *
     * @param data the data to be compressed
     * @return byte[] the compressed data
     */
    public static final byte[] compress(byte[] data) {
        return GZIP.compress(data);
    }

    /**
     * Decompress data by the GZIP algorithm.
     *
     * @param data the compressed data
     * @return byte[] the original data
     */
    public static final byte[] decompress(byte[] data) {
        return GZIP.decompress(data);
    }

    /**
     * Compress the specified string using UTF-8 encoding and GZIP algorithm.
     *
     * @param str the string to be compressed
     * @return the compressed result
     */
    public static final byte[] compressString(String str) {
        return GZIP.compressString(str);
    }

    /**
     * Decompress the specified data to string using UTF-8 encoding and GZIP algorithm.
     *
     * @param data the compressed data
     * @return the original string
     */
    public static final String decompressString(byte[] data) {
        return GZIP.decompressString(data);
    }

    /**
     * Compress the specified object using JSON as the internal format and then use the GZIP algorithm.
     *
     * @param value the object to be compressed
     * @return the compressed result
     * @throws IOException if something goes wrong in Jackson JSON
     */
    public static byte[] compressObj(Object value) throws IOException {
        return GZIP.compressObj(value);
    }

    /**
     * Decompress the data into object.
     *
     * @param <T> the value class type
     * @param data the binary data
     * @param valueType the value class type
     * @return the original object
     * @throws IOException if something goes wrong in Jackson JSON
     */
    public static final <T> T decompressObj(byte[] data, Class<T> valueType) throws IOException {
        return GZIP.decompressObj(data, valueType);
    }

    /**
     * Decompress the data into object.
     *
     * @param <T> the value class type
     * @param data the binary data
     * @param valueType the value class type
     * @return the original object
     * @throws IOException if something goes wrong in Jackson JSON
     */
    public static final <T> T decompressObj(byte[] data, JavaType valueType) throws IOException {
        return GZIP.decompressObj(data, valueType);
    }

}
