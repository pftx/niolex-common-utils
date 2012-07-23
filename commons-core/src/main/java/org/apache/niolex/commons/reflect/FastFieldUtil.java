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

import com.esotericsoftware.reflectasm.FieldAccess;

/**
 * FastFieldUtil是一个通过Reflect ASM来操作Java对象的工具类
 * 注意！本类只能操作public/protected/package修饰的字段，私有字段请通过FieldUtil操作
 *
 * 目前提供的功能如下：
 * 1. public static final String[] getFields(Class<?> clazz)
 * 获取一个Java类所有非私有的属性
 *
 * 2. public static final FieldAccess getFieldAccess(Class<?> clazz)
 * 获取一个Java类所对应的属性操作类
 *
 * 3. public static final <T> T getFieldValue(String f, Object host)
 * 获取一个Java对象中指定属性的值
 *
 * 4. public static final void setFieldValue(String f, Object host, * value)
 * 设置一个Java对象中指定属性的值
 *
 * @used 暂无项目使用
 * @category niolex-common-utils -> 公共库 -> 反射处理
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-7-20
 */
public abstract class FastFieldUtil {

    /**
     * 获取一个Java类定义的所有属性
     *
     * @param clazz 需要获取属性的Java类
     * @return 所有属性的数组
     * @throws RuntimeException 如果对这个类使用反射失败
     */
    public static final String[] getFields(Class<?> clazz) {
    	FieldAccess access = FieldAccess.get(clazz);
        return access.getFieldNames();
    }

    /**
     * 获取一个Java类所对应的属性操作类
     *
     * @param clazz 需要获取属性的Java类
     * @return 所对应的属性操作类
     * @throws RuntimeException 如果对这个类使用反射失败
     */
    public static final FieldAccess getFieldAccess(Class<?> clazz) {
    	return FieldAccess.get(clazz);
    }

    /**
     * 获取一个Java对象中指定属性的值
     *
     * @param <T> 该属性的返回类型，方法中将按照该类型进行强制类型转换
     * @param f 需要获取的值的属性名称
     * @param host 用来获取指定属性的值的对象
     * @return 指定属性的值
     * @throws RuntimeException 如果对这个类使用反射失败或者指定的对象里面没有该属性
     */
    @SuppressWarnings("unchecked")
    public static final <T> T getFieldValue(String f, Object host)
            throws RuntimeException {
    	FieldAccess access = FieldAccess.get(host.getClass());

        return (T) access.get(host, f);
    }

    /**
     * 设置一个Java对象中指定属性的值
     *
     * @param f 需要设置的值的属性
     * @param host 用来设置指定属性的值的对象
     * @param value 指定属性的值
     * @throws RuntimeException 如果对这个类使用反射失败或者指定的对象里面没有该属性
     */
    public static final void setFieldValue(String f, Object host, Object value)
            throws RuntimeException {
    	FieldAccess access = FieldAccess.get(host.getClass());
    	access.set(host, f, value);
    }

    /**
     * 设置一个Java对象中指定属性的值
     *
     * @param f 需要设置的值的属性
     * @param host 用来设置指定属性的值的对象
     * @param value 指定属性的值
     * @throws RuntimeException 如果对这个类使用反射失败或者指定的对象里面没有该属性
     */
    public static final void setFieldValue(String f, Object host, boolean value) {
    	FieldAccess access = FieldAccess.get(host.getClass());
    	access.setBoolean(host, access.getIndex(f), value);
    }

    /**
     * 设置一个Java对象中指定属性的值
     *
     * @param f 需要设置的值的属性
     * @param host 用来设置指定属性的值的对象
     * @param value 指定属性的值
     * @throws RuntimeException 如果对这个类使用反射失败或者指定的对象里面没有该属性
     */
    public static final void setFieldValue(String f, Object host, byte value) {
    	FieldAccess access = FieldAccess.get(host.getClass());
    	access.setByte(host, access.getIndex(f), value);
    }

    /**
     * 设置一个Java对象中指定属性的值
     *
     * @param f 需要设置的值的属性
     * @param host 用来设置指定属性的值的对象
     * @param value 指定属性的值
     * @throws RuntimeException 如果对这个类使用反射失败或者指定的对象里面没有该属性
     */
    public static final void setFieldValue(String f, Object host, char value) {
    	FieldAccess access = FieldAccess.get(host.getClass());
    	access.setChar(host, access.getIndex(f), value);
    }

    /**
     * 设置一个Java对象中指定属性的值
     *
     * @param f 需要设置的值的属性
     * @param host 用来设置指定属性的值的对象
     * @param value 指定属性的值
     * @throws RuntimeException 如果对这个类使用反射失败或者指定的对象里面没有该属性
     */
    public static final void setFieldValue(String f, Object host, double value) {
    	FieldAccess access = FieldAccess.get(host.getClass());
    	access.setDouble(host, access.getIndex(f), value);
    }

    /**
     * 设置一个Java对象中指定属性的值
     *
     * @param f 需要设置的值的属性
     * @param host 用来设置指定属性的值的对象
     * @param value 指定属性的值
     * @throws RuntimeException 如果对这个类使用反射失败或者指定的对象里面没有该属性
     */
    public static final void setFieldValue(String f, Object host, float value) {
    	FieldAccess access = FieldAccess.get(host.getClass());
    	access.setFloat(host, access.getIndex(f), value);
    }

    /**
     * 设置一个Java对象中指定属性的值
     *
     * @param f 需要设置的值的属性
     * @param host 用来设置指定属性的值的对象
     * @param value 指定属性的值
     * @throws RuntimeException 如果对这个类使用反射失败或者指定的对象里面没有该属性
     */
    public static final void setFieldValue(String f, Object host, int value)
            throws RuntimeException {
    	FieldAccess access = FieldAccess.get(host.getClass());
    	access.setInt(host, access.getIndex(f), value);
    }

    /**
     * 设置一个Java对象中指定属性的值
     *
     * @param f 需要设置的值的属性
     * @param host 用来设置指定属性的值的对象
     * @param value 指定属性的值
     * @throws RuntimeException 如果对这个类使用反射失败或者指定的对象里面没有该属性
     */
    public static final void setFieldValue(String f, Object host, long value)
            throws RuntimeException {
    	FieldAccess access = FieldAccess.get(host.getClass());
    	access.setLong(host, access.getIndex(f), value);
    }

    /**
     * 设置一个Java对象中指定属性的值
     *
     * @param f 需要设置的值的属性
     * @param host 用来设置指定属性的值的对象
     * @param value 指定属性的值
     * @throws RuntimeException 如果对这个类使用反射失败或者指定的对象里面没有该属性
     */
    public static final void setFieldValue(String f, Object host, short value)
            throws RuntimeException {
    	FieldAccess access = FieldAccess.get(host.getClass());
    	access.setShort(host, access.getIndex(f), value);
    }
}
