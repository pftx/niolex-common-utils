/**
 * JacksonUtil.java
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
import java.io.InputStream;
import java.io.OutputStream;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * The utility class to help us using Jackson to serialize object to JSON and vice-versa.
 * Since commons-core 2.2.0, we upgrade the Jackson library to 2.0 version.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2011-3-18$
 */
public abstract class JacksonUtil {

    // can reuse, share globally
    private static final ObjectMapper mapper;

    static {
    	/**
    	 * Init the Object Mapper as follows.
    	 */
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * @return the internal Jackson object mapper
     */
    public static final ObjectMapper getmapper() {
        return mapper;
    }

    /**
     * Get the Json Factory the ObjectMapper inside this class is using.
     *
     * @return the internal json factory
     */
    public static final JsonFactory getJsonFactory() {
        return mapper.getFactory();
    }

    /**
     * Get the Type Factory the ObjectMapper inside this class is using.
     *
     * @return the internal type factory
     */
    public static final TypeFactory getTypeFactory() {
        return mapper.getTypeFactory();
    }

    /**
     * Serialize the specified Object into binary form.
     *
     * @param o the object needs to be serialized
     * @return the binary array
     * @throws IOException if necessary
     */
    public static final byte[] obj2bin(Object o) throws IOException {
        return mapper.writeValueAsBytes(o);
    }

    /**
     * Convert the binary data into the original Object.
     *
     * @param <T> the java bean type
     *
     * @param bin the binary array data
     * @param valueType the Java class type
     * @return the java bean
     * @throws IOException if necessary
     */
    public static final <T> T bin2Obj(byte[] bin, Class<T> valueType) throws IOException {
        return mapper.readValue(bin, valueType);
    }

    /**
     * Convert the binary data into the original Object.
     *
     * @param <T> the java bean type
     *
     * @param bin the binary array data
     * @param valueType the Java class type
     * @return the java bean
     * @throws IOException if necessary
     */
    @SuppressWarnings("unchecked")
    public static final <T> T bin2Obj(byte[] bin, JavaType valueType) throws IOException {
        return (T)mapper.readValue(bin, valueType);
    }

    /**
     * Convert the binary data into the original Object.
     *
     * @param <T> the java bean type
     *
     * @param bin the binary array data
     * @param valueType the Java class type
     * @return the java bean
     * @throws IOException if necessary
     */
    @SuppressWarnings("unchecked")
    public static final <T> T bin2Obj(byte[] bin, TypeReference<T> valueType) throws IOException {
        return (T)mapper.readValue(bin, valueType);
    }

    /**
     * Serialize the specified Object into string form.
     *
     * @param o the object needs to be serialized
     * @return the string represents the object
     * @throws IOException if necessary
     */
    public static final String obj2Str(Object o) throws IOException {
        return mapper.writeValueAsString(o);
    }

    /**
     * Convert the string data into the original Object.
     *
     * @param <T> the java bean type
     * @param s the string data
     * @param valueType the Java class type
     * @return the object
     * @throws IOException if necessary
     */
    public static final <T> T str2Obj(String s, Class<T> valueType) throws IOException {
        return mapper.readValue(s, valueType);
    }

    /**
     * Convert the string data into the original Object.
     *
     * @param <T> the java bean type
     * @param s the string data
     * @param valueType the Java class type
     * @return the object
     * @throws IOException if necessary
     */
    @SuppressWarnings("unchecked")
    public static final <T> T str2Obj(String s, JavaType valueType) throws IOException {
    	return (T)mapper.readValue(s, valueType);
    }

    /**
     * Convert the string data into the original Object.
     *
     * @param <T> the java bean type
     * @param s the string data
     * @param valueType the Java class type
     * @return the object
     * @throws IOException if necessary
     */
    @SuppressWarnings("unchecked")
    public static final <T> T str2Obj(String s, TypeReference<T> valueType) throws IOException {
    	return (T)mapper.readValue(s, valueType);
    }

    /**
     * Write object json representation to the specified output stream.
     *
     * @param out the output stream used to write the output
     * @param value the object to be written
     * @throws IOException if necessary
     */
    public static final void writeObj(OutputStream out, Object value) throws IOException {
    	mapper.writeValue(out, value);
    }

    /**
     * Read object from the specified input stream.
     *
     * @param <T> the java bean type
     *
     * @param in the input stream to read data from
     * @param valueType the Java class type
     * @return the object
     * @throws IOException if necessary
     */
    public static final <T> T readObj(InputStream in, Class<T> valueType) throws IOException {
        return mapper.readValue(in, valueType);
    }

    /**
     * Read object from the specified input stream.
     *
     * @param <T> the java bean type
     *
     * @param in the input stream to read data from
     * @param valueType the Java class type
     * @return the object
     * @throws IOException if necessary
     */
    @SuppressWarnings("unchecked")
    public static final <T> T readObj(InputStream in, JavaType valueType) throws IOException {
        return (T)mapper.readValue(in, valueType);
    }

    /**
     * Read object from the specified input stream.
     *
     * @param <T> the java bean type
     *
     * @param in the input stream to read data from
     * @param valueType the Java class type
     * @return the object
     * @throws IOException if necessary
     */
    @SuppressWarnings("unchecked")
    public static final <T> T readObj(InputStream in, TypeReference<T> valueType) throws IOException {
    	return (T)mapper.readValue(in, valueType);
    }

}
