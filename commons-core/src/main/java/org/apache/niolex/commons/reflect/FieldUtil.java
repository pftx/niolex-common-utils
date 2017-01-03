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
import org.apache.niolex.commons.config.PropertiesWrapper;

/**
 * FieldUtil is a utility class help programmers access object fields.
 * <b>Reflection is time consuming operation, please use them cautiously.</b>
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
     * @throws SecurityException if using reflection method to access this field is refused by the security manager
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
     * @throws SecurityException if using reflection method to access this field is refused by the security manager
     */
    public static final void setValue(Object host, String fieldName, Object value) {
        try {
            setFieldValue(host, getField(host.getClass(), fieldName), value);
        } catch (NoSuchFieldException e) {
            throw new ItemNotFoundException("Field not found in host.", e);
        }
    }

    /**
     * Automatically convert the specified value of string type into correct type and set the converted value into the
     * host object.
     *
     * @param host the host object
     * @param fieldName the field name
     * @param value the new field value to set
     * @throws ItemNotFoundException if field not found
     * @throws SecurityException if the accessing of this field is refused by the security manager
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
     * Get all the fields of this class and it's super classes.
     *
     * @param clazz the class to be used to retrieve fields
     * @return a list of all the fields
     * @throws SecurityException if failed to access fields on the specified class or it's super classes
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
     * Retrieve proper fields by the specified filter from all the fields of this class and it's super classes.
     *
     * @param clazz the class to be used to retrieve fields
     * @param filter the filter used to filter fields
     * @return the filtered fields list
     * @throws SecurityException if failed to access fields on the specified class or it's super classes.
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
     * Get the field with the specified name. If not found in this class, we will try
     * to locate it from the super classes, too.
     *
     * @param clazz the class to be used to retrieve fields
     * @param name the name of the field to be retrieved
     * @return the field with the specified name
     * @throws SecurityException if the security manager refused the reflection access to the specified class.
     * @throws NoSuchFieldException if the field with the specified name not found.
     */
    public static final Field getField(Class<?> clazz, String name) throws SecurityException, NoSuchFieldException {
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
     * Get the value of this field from the host object and cast the result to type T.
     *
     * @param <T> the return type
     * @param host the host object used to retrieve field value
     * @param f the field to be used
     * @return the field value
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring
     *             the underlying field (or a subclass or implementor thereof).
     * @throws SecurityException if the security manager refused we set the accessible of this field to true
     */
    @SuppressWarnings("unchecked")
    public static final <T> T getFieldValue(Object host, Field f) {
        return (T) safeGetFieldValue(host, f);
    }

    /**
     * Get the value of this field from the host object. We set accessible to true to make sure we can access the field
     * value.
     *
     * @param host the host object used to retrieve field value
     * @param f the field to be used
     * @return the field value
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring
     *             the underlying field (or a subclass or implementor thereof).
     * @throws SecurityException if the security manager refused we set the accessible of this field to true
     */
    public static final Object safeGetFieldValue(Object host, Field f) {
        f.setAccessible(true);
        return unsafeGetFieldValue(host, f);
    }

    /**
     * Get the value of this field from the host object. If we can not access the field, we will throw
     * IllegalStateException instead of IllegalAccessException.
     *
     * @param host the host object used to retrieve field value
     * @param f the field to be used
     * @return the field value
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring
     *             the underlying field (or a subclass or implementor thereof).
     * @throws IllegalStateException if this Field object is enforcing Java language access control and the underlying
     *             field is inaccessible.
     */
    public static final Object unsafeGetFieldValue(Object host, Field f) {
        try {
            return f.get(host);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Failed to access the field.", e);
        }
    }

    /**
     * Set the specified field value into the host object. We set accessible of the specified field to true to make sure
     * we can modified the field value.
     *
     * @param host the object whose field should be modified
     * @param f the field to be used
     * @param value the new value to be set to the specified field
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring
     *             the underlying field (or a subclass or implementor thereof), or if an unwrapping conversion fails.
     * @throws SecurityException if the security manager refused we set the accessible of this field to true
     */
    public static final void setFieldValue(Object host, Field f, Object value) {
        f.setAccessible(true);
        unsafeSetFieldValue(host, f, value);
    }

    /**
     * Set the specified field value into the host object.
     *
     * @param host the object whose field should be modified
     * @param f the field to be used
     * @param value the new value to be set to the specified field
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring
     *             the underlying field (or a subclass or implementor thereof), or if an unwrapping conversion fails.
     * @throws IllegalStateException if this Field object is enforcing Java language access control and the underlying
     *             field is either inaccessible or final.
     */
    public static final void unsafeSetFieldValue(Object host, Field f, Object value) {
        try {
            f.set(host, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Failed to access the field.", e);
        }
    }

    /**
     * Sets the value of a field on the specified object. We set accessible of the specified field to true to make sure
     * we can modified the field value.
     *
     * @param host the object whose field should be modified
     * @param f the field to be used to modify host field value
     * @param value the new value for the field of host being modified
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring
     *             the underlying field (or a subclass or implementor thereof), or if an unwrapping conversion fails.
     * @throws IllegalAccessException this exception will not be thrown
     * @throws SecurityException if the security manager refused we set the accessible of this field to true.
     */
    public static final void setFieldValue(Object host, Field f, boolean value)
            throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setBoolean(host, value);
    }

    /**
     * Sets the value of a field on the specified object. We set accessible of the specified field to true to make sure
     * we can modified the field value.
     *
     * @param host the object whose field should be modified
     * @param f the field to be used to modify host field value
     * @param value the new value for the field of host being modified
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring
     *             the underlying field (or a subclass or implementor thereof), or if an unwrapping conversion fails.
     * @throws IllegalAccessException this exception will not be thrown
     * @throws SecurityException if the security manager refused we set the accessible of this field to true.
     */
    public static final void setFieldValue(Object host, Field f, byte value)
            throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setByte(host, value);
    }

    /**
     * Sets the value of a field on the specified object. We set accessible of the specified field to true to make sure
     * we can modified the field value.
     *
     * @param host the object whose field should be modified
     * @param f the field to be used to modify host field value
     * @param value the new value for the field of host being modified
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring
     *             the underlying field (or a subclass or implementor thereof), or if an unwrapping conversion fails.
     * @throws IllegalAccessException this exception will not be thrown
     * @throws SecurityException if the security manager refused we set the accessible of this field to true.
     */
    public static final void setFieldValue(Object host, Field f, char value)
            throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setChar(host, value);
    }

    /**
     * Sets the value of a field on the specified object. We set accessible of the specified field to true to make sure
     * we can modified the field value.
     *
     * @param host the object whose field should be modified
     * @param f the field to be used to modify host field value
     * @param value the new value for the field of host being modified
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring
     *             the underlying field (or a subclass or implementor thereof), or if an unwrapping conversion fails.
     * @throws IllegalAccessException this exception will not be thrown
     * @throws SecurityException if the security manager refused we set the accessible of this field to true.
     */
    public static final void setFieldValue(Object host, Field f, short value)
            throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setShort(host, value);
    }

    /**
     * Sets the value of a field on the specified object. We set accessible of the specified field to true to make sure
     * we can modified the field value.
     *
     * @param host the object whose field should be modified
     * @param f the field to be used to modify host field value
     * @param value the new value for the field of host being modified
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring
     *             the underlying field (or a subclass or implementor thereof), or if an unwrapping conversion fails.
     * @throws IllegalAccessException this exception will not be thrown
     * @throws SecurityException if the security manager refused we set the accessible of this field to true.
     */
    public static final void setFieldValue(Object host, Field f, int value)
            throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setInt(host, value);
    }

    /**
     * Sets the value of a field on the specified object. We set accessible of the specified field to true to make sure
     * we can modified the field value.
     *
     * @param host the object whose field should be modified
     * @param f the field to be used to modify host field value
     * @param value the new value for the field of host being modified
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring
     *             the underlying field (or a subclass or implementor thereof), or if an unwrapping conversion fails.
     * @throws IllegalAccessException this exception will not be thrown
     * @throws SecurityException if the security manager refused we set the accessible of this field to true.
     */
    public static final void setFieldValue(Object host, Field f, long value)
            throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setLong(host, value);
    }

    /**
     * Sets the value of a field on the specified object. We set accessible of the specified field to true to make sure
     * we can modified the field value.
     *
     * @param host the object whose field should be modified
     * @param f the field to be used to modify host field value
     * @param value the new value for the field of host being modified
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring
     *             the underlying field (or a subclass or implementor thereof), or if an unwrapping conversion fails.
     * @throws IllegalAccessException this exception will not be thrown
     * @throws SecurityException if the security manager refused we set the accessible of this field to true.
     */
    public static final void setFieldValue(Object host, Field f, float value)
            throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setFloat(host, value);
    }

    /**
     * Sets the value of a field on the specified object. We set accessible of the specified field to true to make sure
     * we can modified the field value.
     *
     * @param host the object whose field should be modified
     * @param f the field to be used to modify host field value
     * @param value the new value for the field of host being modified
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring
     *             the underlying field (or a subclass or implementor thereof), or if an unwrapping conversion fails.
     * @throws IllegalAccessException this exception will not be thrown
     * @throws SecurityException if the security manager refused we set the accessible of this field to true.
     */
    public static final void setFieldValue(Object host, Field f, double value)
            throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setDouble(host, value);
    }

    /**
     * Set the field with correct value type automatically converted from the specified string value. We set accessible
     * of the specified field to true to make sure we can modified the field value.
     *
     * @param host the object whose field should be modified
     * @param field the field to be used to modify host field value
     * @param value the new value for the field of host being modified
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring
     *             the underlying field (or a subclass or implementor thereof), or if an unwrapping conversion fails.
     * @throws UnsupportedOperationException if we can not support this field type.
     * @throws SecurityException if the security manager refused we set the accessible of this field to true.
     */
    public static final void setFieldValueAutoConvert(Object host, Field field, String value) {
        field.setAccessible(true);
        unsafeSetFieldValueAutoConvert(host, field, value);
    }

    /**
     * Set the field with correct value type automatically converted from the specified string value.
     *
     * @param host the object whose field should be modified
     * @param field the field to be used to modify host field value
     * @param value the new value for the field of host being modified
     * @throws IllegalStateException if this Field object is enforcing Java language access control and the underlying
     *             field is either inaccessible or final.
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring
     *             the underlying field (or a subclass or implementor thereof), or if an unwrapping conversion fails.
     * @throws UnsupportedOperationException if we can not support this field type.
     * @throws SecurityException if the security manager refused we set the accessible of this field to true.
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
                field.set(host, PropertiesWrapper.parseBoolean(value));
            } else if (type == boolean.class) {
                field.setBoolean(host, PropertiesWrapper.parseBoolean(value));
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
