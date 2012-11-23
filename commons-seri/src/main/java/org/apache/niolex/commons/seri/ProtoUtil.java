/**
 * ProtoUtil.java
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

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.niolex.commons.codec.IntegerUtil;
import org.apache.niolex.commons.reflect.MethodUtil;

import com.google.protobuf.GeneratedMessage;

/**
 * Common Utility to do protocol buffer serialization.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-8-7
 */
public class ProtoUtil {

	/**
	 * Store all the class been parsed with fast method utility, for faster speed.
	 */
	private static final ConcurrentHashMap<Type, Method> MAP = new ConcurrentHashMap<Type, Method>();

	/**
	 * Parse one object of type <code>type</code> from the byte array.
	 *
	 * @param ret
	 * @param type
	 * @return the object
	 */
	public static final Object parseOne(byte[] ret, Type type) {
		if (type instanceof Class<?>) {
			try {
				Method method = MAP.get(type);
				if (method == null) {
					method = MethodUtil.getMethod((Class<?>) type, "parseFrom", byte[].class);
					MAP.putIfAbsent(type, method);
				}
				return method.invoke(null, ret);
			} catch (Exception e) {
				throw new SeriException("Return type is not protobuf type.", e);
			}
		}
		throw new SeriException("Return type is not protobuf type.");
	}

	/**
	 * Parse multiple objects of the specified type array from the byte array.
	 *
	 * @param data
	 * @param generic
	 * @return the object array
	 */
	public static final Object[] parseMulti(byte[] data, Type[] generic) {
		int idx = 0;
		Object[] r = new Object[generic.length];
		for (int i = 0; i < generic.length; ++i) {
			int size = IntegerUtil.threeBytes(data[idx++], data[idx++], data[idx++]);
			byte[] ret = Arrays.copyOfRange(data, idx, size + idx);
			idx += size;
			r[i] = parseOne(ret, generic[i]);
		}
		return r;
	}

	/**
	 * Serialize one object using protocol buffer.
	 *
	 * @param o
	 * @return the byte array
	 */
	public static final byte[] seriOne(Object o) {
		if (o instanceof GeneratedMessage) {
			GeneratedMessage gen = (GeneratedMessage)o;
			return gen.toByteArray();
		} else {
			throw new SeriException("Message is not protobuf type: " + o.getClass());
		}
	}

	/**
	 * Serialize multiple protocol buffer objects into byte array.
	 *
	 * @param params
	 * @return the byte array
	 */
	public static final byte[] seriMulti(Object[] params) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		for (int i = 0; i < params.length; ++i) {
			byte[] ret = seriOne(params[i]);
			int size = ret.length;
			if (size > 0xffffff) {
				throw new SeriException("We can not support object larger than 0xffffff.");
			}
			out.write(size >> 16);
			out.write(size >> 8);
			out.write(size);
			out.write(ret, 0, size);
		}
		return out.toByteArray();
	}

}
