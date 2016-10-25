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
 * <p>中: FastFieldUtil是一个通过Reflect ASM来操作Java对象的工具类
 * <b>注意！</b>本类只能操作public/protected/package修饰的字段，私有和静态字段请通过FieldUtil操作
 * </p><p>
 * En: FastFieldUtil using Reflect ASM to operate on Java bean to achieve high speed.
 * <b>Notion!</b> This utility can only operate on public/protected/package fields. For other
 * private or static fields, please use {@link org.apache.niolex.commons.reflect.FieldUtil}
 * </p>
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-7-20
 * @see org.apache.niolex.commons.reflect.FieldUtil
 */
public abstract class FastFieldUtil {
    
    private static final ConcurrentMap<Class<?>, FieldAccess> FIELD_ACCESS_MAP = new ConcurrentHashMap<Class<?>, FieldAccess>();

    /**
     * En: Retrieve all the non-private fields defined in this class.<br>
     * 中: 获取一个Java类定义的所有非私有属性
     *
     * @param clazz 需要获取属性的Java类
     * @return 所有属性的数组
     * @throws RuntimeException 如果对这个类使用反射失败
     */
    public static final String[] getFields(Class<?> clazz) {
        return getFieldAccess(clazz).getFieldNames();
    }

    /**
     * En: Get the field access object for this class.<br>
     * 中: 获取一个Java类所对应的属性操作类
     *
     * @param clazz 需要获取属性的Java类
     * @return 所对应的属性操作类
     * @throws RuntimeException 如果对这个类使用反射失败
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
     * En: Get the field value of the specified field name.<br>
     * 中: 获取一个Java对象中指定属性的值
     *
     * @param <T> 该属性的返回类型，方法中将按照该类型进行强制类型转换
     * @param host 用来获取指定属性的值的对象
     * @param fieldName 需要获取的值的属性名称
     * @return 指定属性的值
     * @throws RuntimeException 如果对这个类使用反射失败或者指定的对象里面没有该属性
     */
    @SuppressWarnings("unchecked")
    public static final <T> T getFieldValue(Object host, String fieldName) {
        return (T) getFieldAccess(host.getClass()).get(host, fieldName);
    }

    /**
     * En: Set the field value of the specified field name.<br>
     * 中: 设置一个Java对象中指定属性的值
     *
     * @param host 用来设置指定属性的值的对象
     * @param fieldName 需要设置的值的属性
     * @param value 指定属性的值
     * @throws RuntimeException 如果对这个类使用反射失败或者指定的对象里面没有该属性
     */
    public static final void setFieldValue(Object host, String fieldName, Object value) {
    	getFieldAccess(host.getClass()).set(host, fieldName, value);
    }

    /**
     * En: Set the field value of the specified field name.<br>
     * 中: 设置一个Java对象中指定属性的值
     *
     * @param host 用来设置指定属性的值的对象
     * @param fieldName 需要设置的值的属性
     * @param value 指定属性的值
     * @throws RuntimeException 如果对这个类使用反射失败或者指定的对象里面没有该属性
     */
    public static final void setFieldValue(Object host, String fieldName, boolean value) {
    	FieldAccess access = getFieldAccess(host.getClass());
    	access.setBoolean(host, access.getIndex(fieldName), value);
    }

    /**
     * En: Set the field value of the specified field name.<br>
     * 中: 设置一个Java对象中指定属性的值
     *
     * @param host 用来设置指定属性的值的对象
     * @param fieldName 需要设置的值的属性
     * @param value 指定属性的值
     * @throws RuntimeException 如果对这个类使用反射失败或者指定的对象里面没有该属性
     */
    public static final void setFieldValue(Object host, String fieldName, byte value) {
    	FieldAccess access = getFieldAccess(host.getClass());
    	access.setByte(host, access.getIndex(fieldName), value);
    }

    /**
     * En: Set the field value of the specified field name.<br>
     * 中: 设置一个Java对象中指定属性的值
     *
     * @param host 用来设置指定属性的值的对象
     * @param fieldName 需要设置的值的属性
     * @param value 指定属性的值
     * @throws RuntimeException 如果对这个类使用反射失败或者指定的对象里面没有该属性
     */
    public static final void setFieldValue(Object host, String fieldName, char value) {
    	FieldAccess access = getFieldAccess(host.getClass());
    	access.setChar(host, access.getIndex(fieldName), value);
    }

    /**
     * En: Set the field value of the specified field name.<br>
     * 中: 设置一个Java对象中指定属性的值
     *
     * @param host 用来设置指定属性的值的对象
     * @param fieldName 需要设置的值的属性
     * @param value 指定属性的值
     * @throws RuntimeException 如果对这个类使用反射失败或者指定的对象里面没有该属性
     */
    public static final void setFieldValue(Object host, String fieldName, double value) {
    	FieldAccess access = getFieldAccess(host.getClass());
    	access.setDouble(host, access.getIndex(fieldName), value);
    }

    /**
     * En: Set the field value of the specified field name.<br>
     * 中: 设置一个Java对象中指定属性的值
     *
     * @param host 用来设置指定属性的值的对象
     * @param fieldName 需要设置的值的属性
     * @param value 指定属性的值
     * @throws RuntimeException 如果对这个类使用反射失败或者指定的对象里面没有该属性
     */
    public static final void setFieldValue(Object host, String fieldName, float value) {
    	FieldAccess access = getFieldAccess(host.getClass());
    	access.setFloat(host, access.getIndex(fieldName), value);
    }

    /**
     * En: Set the field value of the specified field name.<br>
     * 中: 设置一个Java对象中指定属性的值
     *
     * @param host 用来设置指定属性的值的对象
     * @param fieldName 需要设置的值的属性
     * @param value 指定属性的值
     * @throws RuntimeException 如果对这个类使用反射失败或者指定的对象里面没有该属性
     */
    public static final void setFieldValue(Object host, String fieldName, int value) {
    	FieldAccess access = getFieldAccess(host.getClass());
    	access.setInt(host, access.getIndex(fieldName), value);
    }

    /**
     * En: Set the field value of the specified field name.<br>
     * 中: 设置一个Java对象中指定属性的值
     *
     * @param host 用来设置指定属性的值的对象
     * @param fieldName 需要设置的值的属性
     * @param value 指定属性的值
     * @throws RuntimeException 如果对这个类使用反射失败或者指定的对象里面没有该属性
     */
    public static final void setFieldValue(Object host, String fieldName, long value) {
    	FieldAccess access = getFieldAccess(host.getClass());
    	access.setLong(host, access.getIndex(fieldName), value);
    }

    /**
     * En: Set the field value of the specified field name.<br>
     * 中: 设置一个Java对象中指定属性的值
     *
     * @param host 用来设置指定属性的值的对象
     * @param fieldName 需要设置的值的属性
     * @param value 指定属性的值
     * @throws RuntimeException 如果对这个类使用反射失败或者指定的对象里面没有该属性
     */
    public static final void setFieldValue(Object host, String fieldName, short value) {
    	FieldAccess access = getFieldAccess(host.getClass());
    	access.setShort(host, access.getIndex(fieldName), value);
    }

}
