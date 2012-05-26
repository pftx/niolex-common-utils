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

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.JavaType;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 *
 * @version 1.0.0, $Date: 2011-3-18$
 *
 */
public abstract class JacksonUtil {
    // can reuse, share globally
    private static final ObjectMapper mapper;

    static {
    	/**
    	 * Init the Object Mapper as follows.
    	 */
        mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Convert Object to String
     * @param o
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static final String obj2Str(Object o) throws JsonGenerationException, JsonMappingException, IOException {
        return mapper.writeValueAsString(o);
    }

    /**
     * Convert String to Object
     * @param s
     * @param valueType
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static final <T> T str2Obj(String s, Class<T> valueType) throws JsonParseException, JsonMappingException, IOException {
        return mapper.readValue(s, valueType);
    }

    /**
     * Convert String to Object
     * @param s
     * @param valueType
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static final <T> T str2Obj(String s, JavaType valueType) throws JsonParseException, JsonMappingException, IOException {
    	return mapper.readValue(s, valueType);
    }

    /**
     * Write object json representation to the OutputStream
     * @param out
     * @param value
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static final void writeObj(OutputStream out, Object value) throws JsonGenerationException, JsonMappingException, IOException {
    	mapper.writeValue(out, value);
    }

    /**
     * Read object from the InputStream
     * @param in
     * @param valueType
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static final <T> T readObj(InputStream in, Class<T> valueType) throws JsonParseException, JsonMappingException, IOException {
        return mapper.readValue(in, valueType);
    }

    /**
     * Read object from the InputStream
     * @param in
     * @param valueType
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static final <T> T readObj(InputStream in, JavaType valueType) throws JsonParseException, JsonMappingException, IOException {
        return (T)mapper.readValue(in, valueType);
    }

}
