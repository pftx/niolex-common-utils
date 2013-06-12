/**
 * FieldUtil.java
 *
 * Copyright 2011 Niolex, Inc.
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * FieldUtil是一个用来通过反射机制来操作Java对象的工具类
 *
 * 目前提供的功能如下：
 * 1. public static final Field[] getFields(Class<?> clazz)
 * 获取一个Java类所有定义的属性
 *
 * 2. public static final Field[] getFields(Class<?> clazz, Class<?> filter)
 * 获取一个Java类所有指定类型的属性
 *
 * 3. public static final Field getField(Class<?> clazz, String name)
 * 获取一个Java类中指定名字的属性
 *
 * 4. public static final <T> T getFieldValue(Field f, Object host)
 * 获取一个Java对象中指定属性的值
 *
 * 5. public static final void setFieldValue(Field f, Object host, * value)
 * 设置一个Java对象中指定属性的值
 *
 * @used 暂无项目使用
 * @category niolex-common-utils -> 公共库 -> 反射处理
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 */
public abstract class FieldUtil {

    /**
     * 获取一个Java类所有定义的属性
     *
     * @param clazz 需要获取属性的Java类
     * @return 所有属性的数组
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final Field[] getFields(Class<?> clazz) {
        return clazz.getDeclaredFields();
    }

    /**
     * 获取一个Java类所有指定类型的属性
     *
     * @param clazz 需要获取属性的Java类
     * @param filter 需要获取的属性的类型
     * @return 指定类型的属性的数组
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final Field[] getFields(Class<?> clazz, Class<?> filter) {
        Field[] oriArr = clazz.getDeclaredFields();
        List<Field> outLst = new ArrayList<Field>();

        for (Field f : oriArr) {
            if (f != null && f.getType().equals(filter)) {
                outLst.add(f);
            }
        }
        return outLst.toArray(new Field[outLst.size()]);
    }

    /**
     * 获取一个Java类中指定名字的属性
     *
     * @param clazz 需要获取属性的Java类
     * @param name 需要获取的属性的名字
     * @return 指定名字的属性
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     * @throws NoSuchFieldException 如果指定名字的属性不存在
     */
    public static final Field getField(Class<?> clazz, String name)
            throws SecurityException, NoSuchFieldException {
        return clazz.getDeclaredField(name);
    }

    /**
     * 获取一个Java对象中指定属性的值
     *
     * @param <T> 该属性的返回类型，方法中将按照该类型进行强制类型转换
     * @param f 需要获取的值的属性定义
     * @param host 用来获取指定属性的值的对象
     * @return 指定属性的值
     * @throws IllegalArgumentException 如果指定的对象里面没有该属性
     * @throws IllegalAccessException 如果指定的属性无法进行反射操作
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    @SuppressWarnings("unchecked")
    public static final <T> T getFieldValue(Field f, Object host)
            throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        return (T) f.get(host);
    }

    /**
     * 获取一个Java对象中指定属性的值，不抛出任何检查的异常
     *
     * @param <T> 该属性的返回类型，方法中将按照该类型进行强制类型转换
     * @param f 需要获取的值的属性定义
     * @param host 用来获取指定属性的值的对象
     * @return 指定属性的值
     * @throws IllegalArgumentException 如果指定的对象里面没有该属性
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    @SuppressWarnings("unchecked")
    public static final <T> T safeFieldValue(Field f, Object host) {
        f.setAccessible(true);
        try {
            return (T) f.get(host);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Failed to access the field.", e);
        }
    }

    /**
     * 设置一个Java对象中指定属性的值
     *
     * @param f 需要设置的值的属性定义
     * @param host 用来设置指定属性的值的对象
     * @param value 指定属性的值
     * @throws IllegalArgumentException 如果指定的对象里面没有该属性
     * @throws IllegalAccessException 如果指定的属性无法进行反射操作
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final void setFieldValue(Field f, Object host, Object value)
            throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.set(host, value);
    }

    /**
     * 设置一个Java对象中指定属性的值
     *
     * @param f 需要设置的值的属性定义
     * @param host 用来设置指定属性的值的对象
     * @param value 指定属性的值
     * @throws IllegalArgumentException 如果指定的对象里面没有该属性
     * @throws IllegalAccessException 如果指定的属性无法进行反射操作
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final void setFieldValue(Field f, Object host, boolean value)
            throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setBoolean(host, value);
    }

    /**
     * 设置一个Java对象中指定属性的值
     *
     * @param f 需要设置的值的属性定义
     * @param host 用来设置指定属性的值的对象
     * @param value 指定属性的值
     * @throws IllegalArgumentException 如果指定的对象里面没有该属性
     * @throws IllegalAccessException 如果指定的属性无法进行反射操作
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final void setFieldValue(Field f, Object host, byte value)
            throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setByte(host, value);
    }

    /**
     * 设置一个Java对象中指定属性的值
     *
     * @param f 需要设置的值的属性定义
     * @param host 用来设置指定属性的值的对象
     * @param value 指定属性的值
     * @throws IllegalArgumentException 如果指定的对象里面没有该属性
     * @throws IllegalAccessException 如果指定的属性无法进行反射操作
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final void setFieldValue(Field f, Object host, char value)
            throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setChar(host, value);
    }

    /**
     * 设置一个Java对象中指定属性的值
     *
     * @param f 需要设置的值的属性定义
     * @param host 用来设置指定属性的值的对象
     * @param value 指定属性的值
     * @throws IllegalArgumentException 如果指定的对象里面没有该属性
     * @throws IllegalAccessException 如果指定的属性无法进行反射操作
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final void setFieldValue(Field f, Object host, double value)
            throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setDouble(host, value);
    }

    /**
     * 设置一个Java对象中指定属性的值
     *
     * @param f 需要设置的值的属性定义
     * @param host 用来设置指定属性的值的对象
     * @param value 指定属性的值
     * @throws IllegalArgumentException 如果指定的对象里面没有该属性
     * @throws IllegalAccessException 如果指定的属性无法进行反射操作
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final void setFieldValue(Field f, Object host, float value)
            throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setFloat(host, value);
    }

    /**
     * 设置一个Java对象中指定属性的值
     *
     * @param f 需要设置的值的属性定义
     * @param host 用来设置指定属性的值的对象
     * @param value 指定属性的值
     * @throws IllegalArgumentException 如果指定的对象里面没有该属性
     * @throws IllegalAccessException 如果指定的属性无法进行反射操作
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final void setFieldValue(Field f, Object host, int value)
            throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setInt(host, value);
    }

    /**
     * 设置一个Java对象中指定属性的值
     *
     * @param f 需要设置的值的属性定义
     * @param host 用来设置指定属性的值的对象
     * @param value 指定属性的值
     * @throws IllegalArgumentException 如果指定的对象里面没有该属性
     * @throws IllegalAccessException 如果指定的属性无法进行反射操作
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final void setFieldValue(Field f, Object host, long value)
            throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setLong(host, value);
    }

    /**
     * 设置一个Java对象中指定属性的值
     *
     * @param f 需要设置的值的属性定义
     * @param host 用来设置指定属性的值的对象
     * @param value 指定属性的值
     * @throws IllegalArgumentException 如果指定的对象里面没有该属性
     * @throws IllegalAccessException 如果指定的属性无法进行反射操作
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final void setFieldValue(Field f, Object host, short value)
            throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setShort(host, value);
    }
}
