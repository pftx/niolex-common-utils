/**
 * ProtoStuffUtil.java
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
package org.apache.niolex.commons.seri;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

/**
 * Common Utility to do protocol stuff protocol serialization.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-9-4
 */
public class ProtoStuffUtil {

	/**
	 * Serialize one object using protocol stuff.
	 *
	 * @param o
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public static final <T> byte[] seriOne(T o) {
	    Schema<T> schema = (Schema<T>) RuntimeSchema.getSchema(o.getClass());
	    LinkedBuffer buffer = LinkedBuffer.allocate(4096);
	    return ProtostuffIOUtil.toByteArray(o, schema, buffer);
	}

	/**
	 * Parse one object of type <code>type</code> from the byte array.
	 *
	 * @param data
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public static final <T> T parseOne(byte[] data, Type type) {
		if (type instanceof Class<?>) {
		    Schema<T> schema = RuntimeSchema.getSchema((Class<T>) type);
		    T ret = schema.newMessage();
		    ProtostuffIOUtil.mergeFrom(data, ret, schema);
			return ret;
		}
		throw new SeriException("Return type is not protostuff type.");
	}

	/**
	 * Serialize multiple objects using protocol stuff into byte array.
	 *
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public static final byte[] seriMulti(Object[] params) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		LinkedBuffer buffer = LinkedBuffer.allocate(4096);
		try {
    		for (int i = 0; i < params.length; ++i) {
    		    Schema<Object> schema = (Schema<Object>) RuntimeSchema.getSchema(params[i].getClass());
    		    ProtostuffIOUtil.writeDelimitedTo(out, params[i], schema, buffer);
    		    buffer.clear();
    		}
		} catch (Exception e) {
            throw new SeriException("Error occured when serialize using protostuff.", e);
        }
		return out.toByteArray();
	}


	/**
	 * Parse multiple objects of the specified type array from the byte array.
	 *
	 * @param data
	 * @param generic
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public static final Object[] parseMulti(byte[] data, Type[] generic) {
		Object[] r = new Object[generic.length];
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		try {
			for (int i = 0; i < generic.length; ++i) {
			    Schema<Object> schema = RuntimeSchema.getSchema((Class<Object>) generic[i]);
			    Object ret = schema.newMessage();
			    ProtostuffIOUtil.mergeDelimitedFrom(in, ret, schema);
			    r[i] = ret;
			}
		} catch (Exception e) {
            throw new SeriException("Error occured when parse multi using protostuff.", e);
        }
		return r;
	}
}
