/**
 * FastFieldUtil.java
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
package org.apache.niolex.commons.reflect;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.esotericsoftware.reflectasm.FieldAccess;

/**
 * FastFieldUtil using Reflect ASM to operate on Java bean to achieve high speed.<br>
 * <b>Notion!</b> This utility can only operate on public/protected/package fields. For other
 * private or static fields, please use {@link org.apache.niolex.commons.reflect.FieldUtil}
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-7-20
 * @see org.apache.niolex.commons.reflect.FieldUtil
 */
public abstract class FastFieldUtil {
    
    private static final ConcurrentMap<Class<?>, FieldAccess> FIELD_ACCESS_MAP = new ConcurrentHashMap<Class<?>, FieldAccess>();

    /**
     * Retrieve all the non-private field names defined in the specified class.
     *
     * @param clazz the class to be used to retrieve fields
     * @return all the non-private field names as an array
     * @throws RuntimeException if error occurred when constructing the field access class
     */
    public static final String[] getFields(Class<?> clazz) {
        return getFieldAccess(clazz).getFieldNames();
    }

    /**
     * Get the field access object for this class.
     *
     * @param clazz the class to be used to retrieve fields
     * @return the field access object
     * @throws RuntimeException if error occurred when constructing the field access class
     */
    public static final FieldAccess getFieldAccess(Class<?> clazz) {
        FieldAccess fa = FIELD_ACCESS_MAP.get(clazz);
        if (fa == null) {
            fa = FieldAccess.get(clazz);
            FIELD_ACCESS_MAP.putIfAbsent(clazz, fa);
        }
    	return fa;
    }

    /**
     * Get the field value of the specified field from the host object.
     *
     * @param <T> the return type
     * @param host the host object used to retrieve field value
     * @param fieldName the name of the field
     * @return the field value
     * @throws RuntimeException if error occurred when constructing the field access class or field not found
     */
    @SuppressWarnings("unchecked")
    public static final <T> T getFieldValue(Object host, String fieldName) {
        return (T) getFieldAccess(host.getClass()).get(host, fieldName);
    }

    /**
     * Set the specified value into the specified field of the specified host object.
     *
     * @param host the host object used to set field value
     * @param fieldName the name of the field
     * @param value the new field value
     * @throws RuntimeException if error occurred when constructing the field access class or field not found
     */
    public static final void setFieldValue(Object host, String fieldName, Object value) {
    	getFieldAccess(host.getClass()).set(host, fieldName, value);
    }

    /**
     * Set the specified value into the specified field of the specified host object.
     *
     * @param host the host object used to set field value
     * @param fieldName the name of the field
     * @param value the new field value
     * @throws RuntimeException if error occurred when constructing the field access class or field not found
     */
    public static final void setFieldValue(Object host, String fieldName, boolean value) {
    	FieldAccess access = getFieldAccess(host.getClass());
    	access.setBoolean(host, access.getIndex(fieldName), value);
    }

    /**
     * Set the specified value into the specified field of the specified host object.
     *
     * @param host the host object used to set field value
     * @param fieldName the name of the field
     * @param value the new field value
     * @throws RuntimeException if error occurred when constructing the field access class or field not found
     */
    public static final void setFieldValue(Object host, String fieldName, byte value) {
    	FieldAccess access = getFieldAccess(host.getClass());
    	access.setByte(host, access.getIndex(fieldName), value);
    }

    /**
     * Set the specified value into the specified field of the specified host object.
     *
     * @param host the host object used to set field value
     * @param fieldName the name of the field
     * @param value the new field value
     * @throws RuntimeException if error occurred when constructing the field access class or field not found
     */
    public static final void setFieldValue(Object host, String fieldName, char value) {
    	FieldAccess access = getFieldAccess(host.getClass());
    	access.setChar(host, access.getIndex(fieldName), value);
    }

    /**
     * Set the specified value into the specified field of the specified host object.
     *
     * @param host the host object used to set field value
     * @param fieldName the name of the field
     * @param value the new field value
     * @throws RuntimeException if error occurred when constructing the field access class or field not found
     */
    public static final void setFieldValue(Object host, String fieldName, short value) {
    	FieldAccess access = getFieldAccess(host.getClass());
        access.setShort(host, access.getIndex(fieldName), value);
    }

    /**
     * Set the specified value into the specified field of the specified host object.
     *
     * @param host the host object used to set field value
     * @param fieldName the name of the field
     * @param value the new field value
     * @throws RuntimeException if error occurred when constructing the field access class or field not found
     */
    public static final void setFieldValue(Object host, String fieldName, int value) {
        FieldAccess access = getFieldAccess(host.getClass());
        access.setInt(host, access.getIndex(fieldName), value);
    }

    /**
     * Set the specified value into the specified field of the specified host object.
     *
     * @param host the host object used to set field value
     * @param fieldName the name of the field
     * @param value the new field value
     * @throws RuntimeException if error occurred when constructing the field access class or field not found
     */
    public static final void setFieldValue(Object host, String fieldName, long value) {
        FieldAccess access = getFieldAccess(host.getClass());
        access.setLong(host, access.getIndex(fieldName), value);
    }

    /**
     * Set the specified value into the specified field of the specified host object.
     *
     * @param host the host object used to set field value
     * @param fieldName the name of the field
     * @param value the new field value
     * @throws RuntimeException if error occurred when constructing the field access class or field not found
     */
    public static final void setFieldValue(Object host, String fieldName, float value) {
        FieldAccess access = getFieldAccess(host.getClass());
        access.setFloat(host, access.getIndex(fieldName), value);
    }

    /**
     * Set the specified value into the specified field of the specified host object.
     *
     * @param host the host object used to set field value
     * @param fieldName the name of the field
     * @param value the new field value
     * @throws RuntimeException if error occurred when constructing the field access class or field not found
     */
    public static final void setFieldValue(Object host, String fieldName, double value) {
        FieldAccess access = getFieldAccess(host.getClass());
        access.setDouble(host, access.getIndex(fieldName), value);
    }

}
