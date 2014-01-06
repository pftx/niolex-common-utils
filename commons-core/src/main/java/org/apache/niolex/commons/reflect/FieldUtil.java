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

import org.apache.niolex.commons.collection.CollectionUtil;

/**
 * FieldUtil is a utility class help programmers access object fields.
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
     * @throws ItemNotFoundException if field not found
     * @throws SecurityException if access this field is refused
     */
    @SuppressWarnings("unchecked")
    public static final <T> T getValue(Object host, String fieldName) {
        try {
            return (T) safeGetFieldValue(host, getField(host.getClass(), fieldName));
        } catch (NoSuchFieldException e) {
            throw new ItemNotFoundException("Field not found in host.", e);
        }
    }

    /**
     * Set the field value into the host object.
     *
     * @param host the host object
     * @param fieldName the field name
     * @param value the new field value to set
     * @throws ItemNotFoundException if field not found
     * @throws SecurityException if access this field is refused
     */
    public static final void setValue(Object host, String fieldName, Object value) {
        try {
            setFieldValue(host, getField(host.getClass(), fieldName), value);
        } catch (NoSuchFieldException e) {
            throw new ItemNotFoundException("Field not found in host.", e);
        }
    }

    /**
     * Set the field value into the host object and automatically convert the value into
     * correct type.
     *
     * @param host the host object
     * @param fieldName the field name
     * @param value the new field value to set
     * @throws ItemNotFoundException if field not found
     * @throws SecurityException if access this field is refused
     * @throws UnsupportedOperationException if we can not support this field type
     */
    public static final void setValueAutoConvert(Object host, String fieldName, String value) {
        try {
            setFieldValueAutoConvert(host, getField(host.getClass(), fieldName), value);
        } catch (NoSuchFieldException e) {
            throw new ItemNotFoundException("Field not found in host.", e);
        }
    }

    /**
     * 获取一个Java类和父类所定义的所有属性
     * Get all the fields of this class and it's super classes.
     *
     * @param clazz 需要获取属性的Java类
     * @return 所有属性的列表
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final List<Field> getAllFields(Class<?> clazz) {
        List<Field> outList = new ArrayList<Field>();
        do {
            CollectionUtil.addAll(outList, clazz.getDeclaredFields());
            clazz = clazz.getSuperclass();
        } while (clazz != null);
        return outList;
    }

    /**
     * The filter used to filter fields.
     *
     * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
     * @version 1.0.0
     * @since 2014-1-6
     */
    public static interface Filter {

        /**
         * Test whether this field is valid for return.
         *
         * @param f the field to be tested
         * @return true if valid, false otherwise
         */
        boolean isValid(Field f);
    }

    /**
     * 获取一个Java类和父类所定义的所有指定类型的属性
     * Access all the fields of the specified type from this class and it's super classes.
     *
     * @param clazz 需要获取属性的Java类
     * @param filter 需要获取的属性的过滤器
     * @return 指定类型的属性的列表
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final List<Field> getFields(Class<?> clazz, Filter filter) {
        List<Field> outList = new ArrayList<Field>();
        do {
            for (Field f : clazz.getDeclaredFields()) {
                if (filter.isValid(f)) {
                    outList.add(f);
                }
            }
            clazz = clazz.getSuperclass();
        } while (clazz != null);
        return outList;
    }

    /**
     * 获取一个Java类中指定名字的属性，如果找不到，尝试到父类中去找
     * Access the field with this name. If not found in this class, we will try
     * to locate it from the super class, too.
     *
     * @param clazz 需要获取属性的Java类
     * @param name 需要获取的属性的名字
     * @return 指定名字的属性
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     * @throws NoSuchFieldException 如果指定名字的属性不存在
     */
    public static final Field getField(Class<?> clazz, String name)
            throws SecurityException, NoSuchFieldException {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            clazz = clazz.getSuperclass();
            if (clazz == null) {
                throw e;
            } else {
                return getField(clazz, name);
            }
        }
    }

    /**
     * 获取一个Java对象中指定属性的值，不抛出任何检查的异常
     * Get the value of this field from the host object and cast the result to T.
     *
     * @param <T> 该属性的返回类型，方法中将按照该类型进行强制类型转换
     * @param host 用来获取指定属性的值的对象
     * @param f 需要获取的值的属性定义
     * @return 指定属性的值
     * @throws IllegalArgumentException 如果指定的对象里面没有该属性
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    @SuppressWarnings("unchecked")
    public static final <T> T getFieldValue(Object host, Field f) {
        return (T) safeGetFieldValue(host, f);
    }


    /**
     * 获取一个Java对象中指定属性的值，不抛出任何检查的异常
     * Get the value of this field from the host object. We set accessible to true.
     *
     * @param host 用来获取指定属性的值的对象
     * @param f 需要获取的值的属性定义
     * @return 指定属性的值
     * @throws IllegalArgumentException 如果指定的对象里面没有该属性
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final Object safeGetFieldValue(Object host, Field f) {
        f.setAccessible(true);
        return unsafeGetFieldValue(host, f);
    }

    /**
     * 获取一个Java对象中指定属性的值，不抛出任何检查的异常
     * Get the value of this field from the host object.
     *
     * @param host 用来获取指定属性的值的对象
     * @param f 需要获取的值的属性定义
     * @return 指定属性的值
     * @throws IllegalArgumentException 如果指定的对象里面没有该属性
     * @throws IllegalStateException 如果指定的属性不可访问
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final Object unsafeGetFieldValue(Object host, Field f) {
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
     * @param host 用来设置指定属性的值的对象
     * @param f 需要设置的值的属性定义
     * @param value 指定属性的值
     * @throws IllegalArgumentException 如果指定的对象里面没有该属性
     * @throws IllegalStateException 如果指定的属性无法进行反射操作
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final void setFieldValue(Object host, Field f, Object value) {
        f.setAccessible(true);
        unsafeSetFieldValue(host, f, value);
    }

    /**
     * 设置一个Java对象中指定属性的值
     * Set the field value into the host object.
     *
     * @param host 用来设置指定属性的值的对象
     * @param f 需要设置的值的属性定义
     * @param value 指定属性的值
     * @throws IllegalArgumentException 如果指定的对象里面没有该属性
     * @throws IllegalStateException 如果指定的属性无法进行反射操作
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final void unsafeSetFieldValue(Object host, Field f, Object value) {
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
     * @param host 用来设置指定属性的值的对象
     * @param f 需要设置的值的属性定义
     * @param value 指定属性的值
     * @throws IllegalArgumentException 如果指定的对象里面没有该属性
     * @throws IllegalAccessException 如果指定的属性无法进行反射操作
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final void setFieldValue(Object host, Field f, boolean value)
            throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setBoolean(host, value);
    }

    /**
     * 设置一个Java对象中指定属性的值
     * Set the field value into the host object.
     *
     * @param host 用来设置指定属性的值的对象
     * @param f 需要设置的值的属性定义
     * @param value 指定属性的值
     * @throws IllegalArgumentException 如果指定的对象里面没有该属性
     * @throws IllegalAccessException 如果指定的属性无法进行反射操作
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final void setFieldValue(Object host, Field f, byte value)
            throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setByte(host, value);
    }

    /**
     * 设置一个Java对象中指定属性的值
     * Set the field value into the host object.
     *
     * @param host 用来设置指定属性的值的对象
     * @param f 需要设置的值的属性定义
     * @param value 指定属性的值
     * @throws IllegalArgumentException 如果指定的对象里面没有该属性
     * @throws IllegalAccessException 如果指定的属性无法进行反射操作
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final void setFieldValue(Object host, Field f, char value)
            throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setChar(host, value);
    }

    /**
     * 设置一个Java对象中指定属性的值
     * Set the field value into the host object.
     *
     * @param host 用来设置指定属性的值的对象
     * @param f 需要设置的值的属性定义
     * @param value 指定属性的值
     * @throws IllegalArgumentException 如果指定的对象里面没有该属性
     * @throws IllegalAccessException 如果指定的属性无法进行反射操作
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final void setFieldValue(Object host, Field f, double value)
            throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setDouble(host, value);
    }

    /**
     * 设置一个Java对象中指定属性的值
     * Set the field value into the host object.
     *
     * @param host 用来设置指定属性的值的对象
     * @param f 需要设置的值的属性定义
     * @param value 指定属性的值
     * @throws IllegalArgumentException 如果指定的对象里面没有该属性
     * @throws IllegalAccessException 如果指定的属性无法进行反射操作
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final void setFieldValue(Object host, Field f, float value)
            throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setFloat(host, value);
    }

    /**
     * 设置一个Java对象中指定属性的值
     * Set the field value into the host object.
     *
     * @param host 用来设置指定属性的值的对象
     * @param f 需要设置的值的属性定义
     * @param value 指定属性的值
     * @throws IllegalArgumentException 如果指定的对象里面没有该属性
     * @throws IllegalAccessException 如果指定的属性无法进行反射操作
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final void setFieldValue(Object host, Field f, int value)
            throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setInt(host, value);
    }

    /**
     * 设置一个Java对象中指定属性的值
     * Set the field value into the host object.
     *
     * @param host 用来设置指定属性的值的对象
     * @param f 需要设置的值的属性定义
     * @param value 指定属性的值
     * @throws IllegalArgumentException 如果指定的对象里面没有该属性
     * @throws IllegalAccessException 如果指定的属性无法进行反射操作
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final void setFieldValue(Object host, Field f, long value)
            throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setLong(host, value);
    }

    /**
     * 设置一个Java对象中指定属性的值
     * Set the field value into the host object.
     *
     * @param host 用来设置指定属性的值的对象
     * @param f 需要设置的值的属性定义
     * @param value 指定属性的值
     * @throws IllegalArgumentException 如果指定的对象里面没有该属性
     * @throws IllegalAccessException 如果指定的属性无法进行反射操作
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final void setFieldValue(Object host, Field f, short value)
            throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setShort(host, value);
    }

    /**
     * 自动从string类型解析需要的类型并设置一个Java对象中指定属性的值
     * Set the field with correct value converting from the string value.
     * We set accessible to true.
     *
     * @param host 用来设置指定属性的值的对象
     * @param field 需要设置的值的属性定义
     * @param value 指定属性的值
     * @throws IllegalArgumentException 如果指定的对象里面没有该属性
     * @throws IllegalStateException 如果指定的属性无法进行反射操作
     * @throws UnsupportedOperationException 如果我们无法支持这个属性的类型
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final void setFieldValueAutoConvert(Object host, Field field, String value) {
        field.setAccessible(true);
        unsafeSetFieldValueAutoConvert(host, field, value);
    }

    /**
     * 自动从string类型解析需要的类型并设置一个Java对象中指定属性的值
     * Set the field with correct value converting from the string value.
     *
     * @param host 用来设置指定属性的值的对象
     * @param field 需要设置的值的属性定义
     * @param value 指定属性的值
     * @throws IllegalArgumentException 如果指定的对象里面没有该属性
     * @throws IllegalStateException 如果指定的属性无法进行反射操作
     * @throws UnsupportedOperationException 如果我们无法支持这个属性的类型
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final void unsafeSetFieldValueAutoConvert(Object host, Field field, String value) {
        Class<?> type = field.getType();
        try {
            if (type == String.class) {
                field.set(host, value);
            } else if (type == Date.class) {
                Date d = new Date(Long.parseLong(value));
                field.set(host, d);
            } else if (type == Integer.class) {
                field.set(host, Integer.parseInt(value));
            } else if (type == int.class) {
                field.setInt(host, Integer.parseInt(value));
            } else if (type == Long.class) {
                field.set(host, Long.parseLong(value));
            } else if (type == long.class) {
                field.setLong(host, Long.parseLong(value));
            } else if (type == Short.class) {
                field.set(host, Short.parseShort(value));
            } else if (type == short.class) {
                field.setShort(host, Short.parseShort(value));
            } else if (type == Byte.class) {
                field.set(host, Byte.parseByte(value));
            } else if (type == byte.class) {
                field.setByte(host, Byte.parseByte(value));
            } else if (type == Boolean.class) {
                field.set(host, Boolean.parseBoolean(value));
            } else if (type == boolean.class) {
                field.setBoolean(host, Boolean.parseBoolean(value));
            } else if (type == Character.class) {
                field.set(host, value.charAt(0));
            } else if (type == char.class) {
                field.setChar(host, value.charAt(0));
            } else if (type == Double.class) {
                field.set(host, Double.parseDouble(value));
            } else if (type == double.class) {
                field.setDouble(host, Double.parseDouble(value));
            } else if (type == Float.class) {
                field.set(host, Float.parseFloat(value));
            } else if (type == float.class) {
                field.setFloat(host, Float.parseFloat(value));
            } else {
                throw new UnsupportedOperationException("The Field Type [" + type.getSimpleName()
                        + "] Is Not Supported.");
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Failed to access the field.", e);
        }
    }

}
