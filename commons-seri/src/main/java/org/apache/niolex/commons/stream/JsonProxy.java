/**
 * JsonProxy.java
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

import org.apache.niolex.commons.compress.JacksonUtil;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.type.TypeReference;

/**
 * This is the proxy to read multiple objects from one input stream.
 * Please use this class instead of @see JacksonUtil
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-7-23
 */
public class JsonProxy {

	private static final JsonFactory FACTORY = JacksonUtil.getJsonFactory();

	private final JsonParser jp;

	/**
	 * Create a proxy using this input stream.
	 *
	 * @param in
	 * @throws IOException
	 */
	public JsonProxy(InputStream in) throws IOException {
		super();
		jp = FACTORY.createJsonParser(in);
	}

	/**
	 * Read an object from the input stream
	 *
	 * @param clazz
	 * @return the object
	 * @throws IOException
	 */
	public <T> T readObject(Class<T> clazz) throws IOException {
		return (T)jp.readValueAs(clazz);
	}

	/**
	 * Read an object from the input stream
	 *
	 * @param valueTypeRef
	 * @return the object
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public <T> T readObject(TypeReference<?> valueTypeRef) throws IOException {
		return (T)jp.readValueAs(valueTypeRef);
	}

}
