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
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ClassUtils;

/**
 * FieldUtil is a utility class help programmer access object fields.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 */
public abstract class FieldUtil {

    /**
     * Get the field value from the host object.
     *
     * @param <T> the return type of the field
     * @param host the host object
     * @param fieldName the field name
     * @return the field value
     * @throws IllegalArgumentException if field not found
     * @throws SecurityException if access this field is refused
     */
    @SuppressWarnings("unchecked")
    public static final <T> T getValue(Object host, String fieldName) {
        try {
            return (T) safeGetFieldValue(getField(host.getClass(), fieldName), host);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Field not found in host.", e);
        }
    }

    /**
     * Set the field value into the host object.
     *
     * @param host the host object
     * @param fieldName the field name
     * @param value the new field value to set
     * @throws IllegalArgumentException if field not found
     * @throws SecurityException if access this field is refused
     */
    public static final void setValue(Object host, String fieldName, Object value) {
        try {
            setFieldValue(getField(host.getClass(), fieldName), host, value);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Field not found in host.", e);
        }
    }

    /**
     * Set the field value into the host object and automatically convert the value into
     * correct type.
     *
     * @param host the host object
     * @param fieldName the field name
     * @param value the new field value to set
     * @throws IllegalArgumentException if field not found
     * @throws SecurityException if access this field is refused
     * @throws UnsupportedOperationException if we can not support this field type
     */
    public static final void setValueAutoConvert(Object host, String fieldName, String value) {
        try {
            setFieldValueAutoConvert(getField(host.getClass(), fieldName), host, value);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Field not found in host.", e);
        }
    }

    /**
     * 获取一个Java类定义的所有属性
     * Access all the fields of this class.
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
     * Access all the fields of the specified type.
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
            if (ClassUtils.isAssignable(f.getType(), filter)) {
                outLst.add(f);
            }
        }
        return outLst.toArray(new Field[outLst.size()]);
    }

    /**
     * 获取一个Java类中指定名字的属性
     * Access the field with this name.
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
     * 获取一个Java对象中指定属性的值，不抛出任何检查的异常
     * Get the value of this field from the host object and cast the result to T.
     *
     * @param <T> 该属性的返回类型，方法中将按照该类型进行强制类型转换
     * @param f 需要获取的值的属性定义
     * @param host 用来获取指定属性的值的对象
     * @return 指定属性的值
     * @throws IllegalArgumentException 如果指定的对象里面没有该属性
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    @SuppressWarnings("unchecked")
    public static final <T> T getFieldValue(Field f, Object host) {
        return (T) safeGetFieldValue(f, host);
    }


    /**
     * 获取一个Java对象中指定属性的值，不抛出任何检查的异常
     * Get the value of this field from the host object. We set accessible to true.
     *
     * @param f 需要获取的值的属性定义
     * @param host 用来获取指定属性的值的对象
     * @return 指定属性的值
     * @throws IllegalArgumentException 如果指定的对象里面没有该属性
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final Object safeGetFieldValue(Field f, Object host) {
        f.setAccessible(true);
        return unsafeGetFieldValue(f, host);
    }

    /**
     * 获取一个Java对象中指定属性的值，不抛出任何检查的异常
     * Get the value of this field from the host object.
     *
     * @param f 需要获取的值的属性定义
     * @param host 用来获取指定属性的值的对象
     * @return 指定属性的值
     * @throws IllegalArgumentException 如果指定的对象里面没有该属性
     * @throws IllegalStateException 如果指定的属性不可访问
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final Object unsafeGetFieldValue(Field f, Object host) {
        try {
            return f.get(host);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Failed to access the field.", e);
        }
    }

    /**
     * 设置一个Java对象中指定属性的值
     * Set the field value into the host object. We set accessible to true.
     *
     * @param f 需要设置的值的属性定义
     * @param host 用来设置指定属性的值的对象
     * @param value 指定属性的值
     * @throws IllegalArgumentException 如果指定的对象里面没有该属性
     * @throws IllegalAccessException 如果指定的属性无法进行反射操作
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final void setFieldValue(Field f, Object host, Object value) {
        f.setAccessible(true);
        unsafeSetFieldValue(f, host, value);
    }

    /**
     * 设置一个Java对象中指定属性的值
     * Set the field value into the host object.
     *
     * @param f 需要设置的值的属性定义
     * @param host 用来设置指定属性的值的对象
     * @param value 指定属性的值
     * @throws IllegalArgumentException 如果指定的对象里面没有该属性
     * @throws IllegalStateException 如果指定的属性无法进行反射操作
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final void unsafeSetFieldValue(Field f, Object host, Object value) {
        try {
            f.set(host, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Failed to access the field.", e);
        }
    }

    /**
     * 设置一个Java对象中指定属性的值
     * Set the field value into the host object.
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
     * Set the field value into the host object.
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
     * Set the field value into the host object.
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
     * Set the field value into the host object.
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
     * Set the field value into the host object.
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
     * Set the field value into the host object.
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
     * Set the field value into the host object.
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
     * Set the field value into the host object.
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

    /**
     * 自动从string类型解析需要的类型并设置一个Java对象中指定属性的值
     * Set the field with correct value converting from the string value.
     * We set accessible to true.
     *
     * @param f 需要设置的值的属性定义
     * @param o 用来设置指定属性的值的对象
     * @param value 指定属性的值
     * @throws IllegalArgumentException 如果指定的对象里面没有该属性
     * @throws IllegalStateException 如果指定的属性无法进行反射操作
     * @throws UnsupportedOperationException 如果我们无法支持这个属性的类型
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final void setFieldValueAutoConvert(Field f, Object o, String value) {
        f.setAccessible(true);
        unsafeSetFieldValueAutoConvert(f, o, value);
    }

    /**
     * 自动从string类型解析需要的类型并设置一个Java对象中指定属性的值
     * Set the field with correct value converting from the string value.
     *
     * @param f 需要设置的值的属性定义
     * @param o 用来设置指定属性的值的对象
     * @param value 指定属性的值
     * @throws IllegalArgumentException 如果指定的对象里面没有该属性
     * @throws IllegalStateException 如果指定的属性无法进行反射操作
     * @throws UnsupportedOperationException 如果我们无法支持这个属性的类型
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final void unsafeSetFieldValueAutoConvert(Field f, Object o, String value) {
        Class<?> type = f.getType();
        try {
            if (type == String.class) {
                f.set(o, value);
            } else if (type == Date.class) {
                Date d = new Date(Long.parseLong(value));
                f.set(o, d);
            } else if (type == Integer.class) {
                f.set(o, Integer.parseInt(value));
            } else if (type == int.class) {
                f.setInt(o, Integer.parseInt(value));
            } else if (type == Long.class) {
                f.set(o, Long.parseLong(value));
            } else if (type == long.class) {
                f.setLong(o, Long.parseLong(value));
            } else if (type == Short.class) {
                f.set(o, Short.parseShort(value));
            } else if (type == short.class) {
                f.setShort(o, Short.parseShort(value));
            } else if (type == Byte.class) {
                f.set(o, Byte.parseByte(value));
            } else if (type == byte.class) {
                f.setByte(o, Byte.parseByte(value));
            } else if (type == Boolean.class) {
                f.set(o, Boolean.parseBoolean(value));
            } else if (type == boolean.class) {
                f.setBoolean(o, Boolean.parseBoolean(value));
            } else if (type == Character.class) {
                f.set(o, value.charAt(0));
            } else if (type == char.class) {
                f.setChar(o, value.charAt(0));
            } else if (type == Double.class) {
                f.set(o, Double.parseDouble(value));
            } else if (type == double.class) {
                f.setDouble(o, Double.parseDouble(value));
            } else if (type == Float.class) {
                f.set(o, Float.parseFloat(value));
            } else if (type == float.class) {
                f.setFloat(o, Float.parseFloat(value));
            } else {
                throw new UnsupportedOperationException("The Field Type [" + type.getSimpleName()
                        + "] Is Not Supported.");
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Failed to access the field.", e);
        }
    }

}
