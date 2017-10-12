/**
 * ProtobufUtil.java
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
import org.apache.niolex.commons.util.Const;

import com.google.protobuf.MessageLite;

/**
 * Common Utility to do protocol buffer serialization.
 * <p>
 * We have a faster access mode, which will need the security rights to set accessible.
 * If your system can not grant this, invoke the method {@link #setUseFasterAccess(boolean)}
 * with parameter "false" to disable it.
 * </p>
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-8-7
 */
public class ProtobufUtil {

    /**
     * Store all the classes been parsed with fast method utility, for faster speed.
     */
    private static final ConcurrentHashMap<Type, Method> ONE_MAP = new ConcurrentHashMap<Type, Method>();
    private static final ConcurrentHashMap<Type, Method> MUL_MAP = new ConcurrentHashMap<Type, Method>();
    private static final int BUF_SIZE = 8 * Const.K;

    private static boolean fasterAccess = true;

    /**
     * Set the faster access flag with this new value.
     *
     * @param faster the new faster access flag
     */
    public static final void setUseFasterAccess(boolean faster) {
        fasterAccess = faster;
    }

    /**
     * Clear the internal methods maps.
     */
    public static final void clearMethodsCache() {
        ONE_MAP.clear();
        MUL_MAP.clear();
    }

    /**
     * Parse one object of type <code>type</code> from the byte array.
     *
     * @param <T> the object type
     * @param data the binary data
     * @param type the object type
     * @return the object
     */
    public static final <T> T parseOne(byte[] data, Class<T> type) {
        try {
            Method method = ONE_MAP.get(type);
            if (method == null) {
                method = MethodUtil.getMethod(type, "parseFrom", byte[].class);
                if (fasterAccess)
                    method.setAccessible(true);
                ONE_MAP.putIfAbsent(type, method);
            }
            @SuppressWarnings("unchecked")
            T r = (T) method.invoke(null, data);
            return r;
        } catch (Exception e) {
            throw new SeriException("Return type is not protobuf type.", e);
        }
    }

    /**
     * Parse delimited one object of type <code>type</code> from the input stream.
     *
     * @param input the input stream used to read data
     * @param type the object type
     * @return the object
     */
    public static final Object parseDelimitedOne(InputStream input, Class<?> type) {
        try {
            Method method = MUL_MAP.get(type);
            if (method == null) {
                method = MethodUtil.getMethod(type, "parseDelimitedFrom", InputStream.class);
                if (fasterAccess)
                    method.setAccessible(true);
                MUL_MAP.putIfAbsent(type, method);
            }
            return method.invoke(null, input);
        } catch (Exception e) {
            throw new SeriException("Return type is not protobuf type.", e);
        }
    }

    /**
     * Parse multiple objects of the specified types from the byte array.
     *
     * @param data the binary data
     * @param types the types array
     * @return the objects array
     */
    public static final Object[] parseMulti(byte[] data, Class<?>[] types) {
        Object[] r = new Object[types.length];
        ByteArrayInputStream binput = new ByteArrayInputStream(data);
        for (int i = 0; i < types.length; ++i) {
            r[i] = parseDelimitedOne(binput, types[i]);
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
        if (o instanceof MessageLite) {
            MessageLite gen = (MessageLite) o;
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
        if (o instanceof MessageLite) {
            MessageLite gen = (MessageLite) o;
            try {
                gen.writeDelimitedTo(output);
            } catch (IOException e) {
                throw new SeriException("Failed to write data to the output stream.", e);
            }
        } else {
            throw new SeriException("Message is not protobuf type: " + o.getClass());
        }
    }

    /**
     * Serialize multiple protocol buffer objects into byte array.
     *
     * @param params the objects to be serialized
     * @return the byte array
     */
    public static final byte[] seriMulti(Object[] params) {
        ByteArrayOutputStream out = new ByteArrayOutputStream(BUF_SIZE);
        for (int i = 0; i < params.length; ++i) {
            seriDelimitedOne(params[i], out);
        }
        return out.toByteArray();
    }

}
