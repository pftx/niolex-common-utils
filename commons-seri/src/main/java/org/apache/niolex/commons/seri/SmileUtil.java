/**
 * SmileUtil.java
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
package org.apache.niolex.commons.seri;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.smile.SmileFactory;
import org.codehaus.jackson.smile.SmileGenerator;
import org.codehaus.jackson.smile.SmileParser;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

/**
 * This Utility use Jackson Smile as the binary format to serialize objects.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2011-3-18$
 */
public abstract class SmileUtil {
    // can reuse, share globally
    private static final ObjectMapper mapper;

    static {
    	/**
    	 * Init the Object Mapper as follows.
    	 */
    	SmileFactory factory = new SmileFactory();
    	factory.configure(SmileGenerator.Feature.WRITE_HEADER, false);
    	factory.configure(SmileParser.Feature.REQUIRE_HEADER, false);
        mapper = new ObjectMapper(factory);
        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    /**
     * Get the internal Json Factory this Object Mapper is using.
     *
     * @return the internal Json Factory
     */
    public static final JsonFactory getJsonFactory() {
    	return mapper.getJsonFactory();
    }

    /**
     * Convert Object to binary
     *
     * @param o the object need to serialization
     * @return the binary array
     * @throws IOException
     */
    public static final byte[] obj2bin(Object o) throws IOException {
        return mapper.writeValueAsBytes(o);
    }

    /**
     * Convert binary to Object
     *
     * @param bin the binary array data
     * @param valueType the Java class type
     * @return the java bean
     * @throws IOException
     */
    public static final <T> T bin2Obj(byte[] bin, Class<T> valueType) throws IOException {
        return mapper.readValue(bin, valueType);
    }

    /**
     * Convert binary to Object
     *
     * @param bin the binary array data
     * @param valueType the Java class type
     * @return the java bean
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static final <T> T bin2Obj(byte[] bin, JavaType valueType) throws IOException {
    	return (T)mapper.readValue(bin, valueType);
    }

    /**
     * Convert binary to Object
     *
     * @param bin the binary array data
     * @param valueType the Java class type
     * @return the java bean
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static final <T> T bin2Obj(byte[] bin, TypeReference<T> valueType) throws IOException {
    	return (T)mapper.readValue(bin, valueType);
    }

    /**
     * Write object smile representation to the OutputStream
     *
     * @param out the output stream to write the output
     * @param value the java bean need to serialize
     * @throws IOException
     */
    public static final void writeObj(OutputStream out, Object value) throws IOException {
    	mapper.writeValue(out, value);
    }

    /**
     * Read object from the InputStream
     *
     * @param in the input stream to read data from
     * @param valueType the Java class type
     * @return the java bean
     * @throws IOException
     */
    public static final <T> T readObj(InputStream in, Class<T> valueType) throws IOException {
        return mapper.readValue(in, valueType);
    }

    /**
     * Read object from the InputStream
     *
     * @param in the input stream to read data from
     * @param valueType the Java class type
     * @return the java bean
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static final <T> T readObj(InputStream in, JavaType valueType) throws IOException {
        return (T)mapper.readValue(in, valueType);
    }

    /**
     * Read object from the InputStream
     *
     * @param in the input stream to read data from
     * @param valueType the Java class type
     * @return the java bean
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static final <T> T readObj(InputStream in, TypeReference<T> valueType) throws IOException {
    	return (T)mapper.readValue(in, valueType);
    }

}
