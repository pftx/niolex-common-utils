/**
 * Compressor.java
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

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.type.JavaType;

/**
 * The Compressor common interface.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2016-4-8
 */
public interface Compressor {

    /**
     * Compress the byte array.
     *
     * @param data the data to be compressed
     * @return the compressed result
     */
    public byte[] compress(byte[] data);

    /**
     * To decompress the byte array.
     *
     * @param data the compressed data
     * @return the original data
     */
    public byte[] decompress(byte[] data);

    /**
     * Compress the specified string using UTF-8 encoding.
     *
     * @param str the string to be compressed
     * @return the compressed result
     */
    public byte[] compressString(String str);

    /**
     * To decompress the byte array and using UTF-8 encoding to recover the original string.
     *
     * @param data the compressed data
     * @return the original string
     */
    public String decompressString(byte[] data);

    /**
     * Compress the specified object using Jackson JSON.
     *
     * @param value the object to be compressed
     * @return the compressed result
     * @throws IOException if necessary
     */
    public byte[] compressObj(Object value) throws IOException;

    /**
     * Decompress the object.
     *
     * @param <T> the value class type
     * @param data the compressed data
     * @param valueType the value class type
     * @return the original object
     * @throws JsonParseException if necessary
     * @throws JsonMappingException if necessary
     * @throws IOException if necessary
     */
    public <T> T decompressObj(byte[] data, Class<T> valueType) throws JsonParseException, JsonMappingException,
            IOException;

    /**
     * Decompress the object.
     *
     * @param <T> the value class type
     * @param data the compressed data
     * @param valueType the value class type
     * @return the original object
     * @throws JsonParseException if necessary
     * @throws JsonMappingException if necessary
     * @throws IOException if necessary
     */
    public <T> T decompressObj(byte[] data, JavaType valueType) throws JsonParseException, JsonMappingException,
            IOException;
}
