/**
 * SmileProxy.java
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
package org.apache.niolex.commons.stream;

import java.io.IOException;
import java.io.InputStream;

import org.apache.niolex.commons.seri.SmileUtil;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * This is the proxy for read multiple objects from one input stream.
 * Please use this class instead of {@link SmileUtil} for this purpose.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-7-23
 */
public class SmileProxy {

    private static final JsonFactory FACTORY = SmileUtil.getJsonFactory();

    private final JsonParser jp;

    /**
     * Create a proxy using this input stream.
     *
     * @param in the input stream
     * @throws IOException if any I / O related exception occurred
     */
    public SmileProxy(InputStream in) throws IOException {
        super();
        jp = FACTORY.createParser(in);
    }

    /**
     * Read an object from the input stream.
     *
     * @param <T> the object type
     * @param clazz the object class
     * @return the object
     * @throws IOException if any I / O related exception occurred
     */
    public <T> T readObject(Class<T> clazz) throws IOException {
        return jp.readValueAs(clazz);
    }

    /**
     * Read an object from the input stream.
     *
     * @param <T> the object type
     * @param valueTypeRef the object type reference
     * @return the object
     * @throws IOException if any I / O related exception occurred
     */
    @SuppressWarnings("unchecked")
    public <T> T readObject(TypeReference<?> valueTypeRef) throws IOException {
        return (T) jp.readValueAs(valueTypeRef);
    }

}
