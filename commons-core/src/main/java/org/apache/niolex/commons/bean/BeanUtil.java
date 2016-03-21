/**
 * BeanUtil.java
 *
 * Copyright 2013 the original author or authors.
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
package org.apache.niolex.commons.bean;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ClassUtils;
import org.apache.niolex.commons.test.ObjToStringUtil;

import com.google.common.collect.Maps;

/**
 * This class utilize methods operating bean.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-6-20
 */
public class BeanUtil {

    /**
     * translate general objects into string.
     *
     * @param obj the target object
     * @return The string representation of this Object.
     */
    public static final String toString(Object obj) {
        return ObjToStringUtil.objToString(obj);
    }

    /**
     * Merge the non null properties from the source bean to the target bean.
     * We will not merge default numeric primitives(0 for int long etc, 0.0 for float double).
     *
     * @param <To> the type of the target bean
     * @param <From> the type of the source bean
     *
     * @param to the target bean
     * @param from the source bean
     * @return the target bean
     */
    public static final <To, From> To merge(To to, From from) {
        return merge(to, from, false);
    }

    /**
     * Merge the non null properties from the source bean to the target bean.
     *
     * @param <To> the type of the target bean
     * @param <From> the type of the source bean
     *
     * @param to the target bean
     * @param from the source bean
     * @param mergeDefault whether do we merge default numeric primitives
     * @return the target bean
     */
    public static final <To, From> To merge(To to, From from, boolean mergeDefault) {
        try {
            Map<String, Method> writeMap = prepareWriteMethodMap(to.getClass());
            BeanInfo fromInfo = Introspector.getBeanInfo(from.getClass());
            // Iterate over all the attributes of from, do copy here.
            for (PropertyDescriptor descriptor : fromInfo.getPropertyDescriptors()) {
                Method readMethod = descriptor.getReadMethod();
                if (readMethod == null) {
                    continue;
                }
                Method writeMethod = writeMap.get(descriptor.getName());
                if (writeMethod == null) {
                    continue;
                }
                Object value = readMethod.invoke(from);
                if (value == null) {
                    continue;
                }
                if (!mergeDefault && isNumericPrimitiveDefaultValue(readMethod.getReturnType(), value)) {
                    continue;
                }
                // Only copy value if it's assignable, auto boxing is OK.
                if (ClassUtils.isAssignable(value.getClass(), writeMethod.getParameterTypes()[0], true)) {
                    writeMethod.invoke(to, value);
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to merge propeties.", e);
        }
        return to;
    }

    /**
     * Parse all the write methods and prepare them to the hash map.
     *
     * @param toClass the class to be introspected
     * @return the hash map
     * @throws IntrospectionException if an exception occurs during introspection.
     */
    public static Map<String, Method> prepareWriteMethodMap(Class<?> toClass) throws IntrospectionException {
        BeanInfo toInfo = Introspector.getBeanInfo(toClass);
        HashMap<String, Method> writeMap = Maps.newHashMap();
        // Iterate over all the attributes of to, prepare write methods.
        for (PropertyDescriptor descriptor : toInfo.getPropertyDescriptors()) {
            Method writeMethod = descriptor.getWriteMethod();
            if (writeMethod == null) {
                continue;
            }
            writeMap.put(descriptor.getName(), writeMethod);
        }
        return writeMap;
    }

    /**
     * Test whether the type is numeric primitive and the value is 0.
     *
     * @param type the value's real type
     * @param value the value to be tested
     * @return true if it's numeric primitive and the value is 0
     */
    public static boolean isNumericPrimitiveDefaultValue(Class<?> type, Object value) {
        if (type.isPrimitive() && Number.class.isInstance(value)) {
            Number num = Number.class.cast(value);
            if (num.doubleValue() == 0.0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Merge the non null properties from the map to the target bean.
     *
     * @param <To> the type of the target bean
     *
     * @param to the target bean
     * @param from the source map
     * @return the target bean
     */
    public static final <To> To merge(To to, Map<String, Object> from) {
        try {
            Map<String, Method> writeMap = prepareWriteMethodMap(to.getClass());
            for (Map.Entry<String, Object> entry : from.entrySet()) {
                Method writeMethod = writeMap.get(entry.getKey());
                if (writeMethod == null) {
                    continue;
                }
                Object value = entry.getValue();
                // Only copy value if it's assignable, auto boxing is OK.
                if (ClassUtils.isAssignable(value.getClass(), writeMethod.getParameterTypes()[0], true)) {
                    writeMethod.invoke(to, value);
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to merge propeties.", e);
        }
        return to;
    }

}
