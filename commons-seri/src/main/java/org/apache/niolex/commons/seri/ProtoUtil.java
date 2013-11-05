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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.niolex.commons.reflect.MethodUtil;

import com.google.protobuf.GeneratedMessage;

/**
 * Common Utility to do protocol buffer serialization.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-8-7
 */
public class ProtoUtil {

	/**
	 * Store all the class been parsed with fast method utility, for faster speed.
	 */
	private static final ConcurrentHashMap<Type, Method> ONE_MAP = new ConcurrentHashMap<Type, Method>();
	private static final ConcurrentHashMap<Type, Method> MUL_MAP = new ConcurrentHashMap<Type, Method>();

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
				Method method = ONE_MAP.get(type);
				if (method == null) {
					method = MethodUtil.getMethod((Class<?>) type, "parseFrom", byte[].class);
					ONE_MAP.putIfAbsent(type, method);
				}
				return method.invoke(null, ret);
			} catch (Exception e) {
				throw new SeriException("Return type is not protobuf type.", e);
			}
		}
		throw new SeriException("Return type is not protobuf type.");
	}

	/**
	 * Parse delimited one object of type <code>type</code> from the input stream.
	 *
	 * @param input
	 * @param type
	 * @return the object
	 */
	public static final Object parseDelimitedOne(InputStream input, Type type) {
		if (type instanceof Class<?>) {
			try {
				Method method = MUL_MAP.get(type);
				if (method == null) {
					method = MethodUtil.getMethod((Class<?>) type, "parseDelimitedFrom", InputStream.class);
					MUL_MAP.putIfAbsent(type, method);
				}
				return method.invoke(null, input);
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
		Object[] r = new Object[generic.length];
		ByteArrayInputStream binput = new ByteArrayInputStream(data);
		for (int i = 0; i < generic.length; ++i) {
			r[i] = parseDelimitedOne(binput, generic[i]);
		}
		return r;
	}

	/**
	 * Serialize one object using protocol buffer.
	 *
	 * @param o the object to serialize
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
	 * Serialize one object delimited using protocol buffer.
	 *
	 * @param o the object to serialize
	 * @param output where to write output
	 */
	public static final void seriDelimitedOne(Object o, OutputStream output) {
		if (o instanceof GeneratedMessage) {
			GeneratedMessage gen = (GeneratedMessage)o;
			try {
				gen.writeDelimitedTo(output);
			} catch (IOException e) {
				throw new SeriException("Message is not protobuf type: " + o.getClass(), e);
			}
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
		ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
		for (int i = 0; i < params.length; ++i) {
			seriDelimitedOne(params[i], out);
		}
		return out.toByteArray();
	}

}
