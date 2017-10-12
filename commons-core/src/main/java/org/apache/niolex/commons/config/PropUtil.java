/**
 * PropUtil.java
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
package org.apache.niolex.commons.config;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * The PropUtil can manage a list of properties files, the one loaded later will override the one loaded earlier. Users
 * can retrieve property as int, long, boolean, double and trimmed string from it.
 * 
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 1.0.0
 */

public abstract class PropUtil {
    private static final PropertiesWrapper PROPS = new PropertiesWrapper();

    /**
     * Load properties from the specified classpath resource file using the same
     * class loader which loaded the specified class.
     *
     * @param resource the classpath resource file
     * @param cls the class object to be used to retrieve class loader
     * @throws IOException if failed to read the resource file
     * @throws IllegalArgumentException if the file contains illegal character
     * @throws NullPointerException if any of the parameters is null
     */
    public static final void loadConfig(String resource, Class<?> cls) throws IOException {
        PROPS.load(cls.getResourceAsStream(resource));
    }

    /**
     * Load properties from the specified file. The <code>filePath</code> must be an
     * absolute file path and name in the file system.
     *
     * @param filePath the absolute file path and name
     * @throws IOException if failed to read the property file
     * @throws IllegalArgumentException if the file contains illegal character
     * @throws NullPointerException if the parameter is null
     * @throws SecurityException if the configured security manager rejected the requirement to read the specified file
     */
    public static final void loadConfig(String filePath) throws IOException {
        PROPS.load(new FileInputStream(filePath));
    }

    /**
     * Searches for the property with the specified key in this property list.
     * If the key is not found in this property list, the default property list,
     * and its defaults, recursively, are then checked. The method returns
     * <code>null</code> if the property is not found.
     *
     * @param key the property key.
     * @return the value in this property list with the specified key value.
     */
    public static final String getProperty(String key) {
        return PROPS.getProperty(key);
    }

    /**
     * Searches for the property with the specified key in this property list.
     * If the key is not found in this property list, the default property list,
     * and its defaults, recursively, are then checked. The method returns the
     * default value argument if the property is not found.
     *
     * @param key the hashtable key.
     * @param defaultValue a default value.
     * @return the value in this property list with the specified key value.
     */
    public static final String getProperty(String key, String defaultValue) {
        return PROPS.getProperty(key, defaultValue);
    }

    /**
     * Searches for a string value from properties by the specified key. We will trim
     * the empty characters before return. We will return null if the property not found.
     *
     * @see #getProperty(String)
     * @param key the property key to be used to retrieve property
     * @return the property value, or null if the property not found
     */
    public static final String getString(String key) {
        return PROPS.getString(key);
    }

    /**
     * Searches for a string value from properties by the specified key. We will trim the empty characters before
     * return. We will return the specified default value if the property not found.
     *
     * @see #getProperty(String, String)
     * @param key the property key to be used to retrieve property
     * @param defaultValue the default value
     * @return the property value, or the <code>defaultValue</code> if the property not found
     */
    public static final String getString(String key, String defaultValue) {
        return PROPS.getString(key, defaultValue);
    }

    /**
     * Searches for an integer value from properties by the specified key.
     *
     * @param key the property key to be used to retrieve property
     * @return the property value as integer
     * @throws NumberFormatException if the property not found or is not a valid integer
     */
    public static final int getInteger(String key) {
        return PROPS.getInteger(key);
    }

    /**
     * Read an integer value from properties by the specified key. If the property not found, we will use the specified
     * default value.
     *
     * @param key the property key to be used to retrieve property
     * @param defaultValue the default value, please make sure it's a valid integer
     * @return the property value as integer, or the <code>defaultValue</code> if the property not found
     * @throws NumberFormatException if the property not found or is not a valid integer or the specified
     *             <code>defaultValue</code> is not a valid integer
     */
    public static final int getInteger(String key, String defaultValue) {
        return PROPS.getInteger(key, defaultValue);
    }

    /**
     * Read an integer value from properties by the specified key. If the property not found, we will use the specified
     * default value.
     *
     * @param key the property key to be used to retrieve property
     * @param defaultValue the default value
     * @return the property value as integer, or the <code>defaultValue</code> if the property not found
     * @throws NumberFormatException if the property not found or is not a valid integer
     */
    public static final int getInteger(String key, int defaultValue) {
        return PROPS.getInteger(key, defaultValue);
    }

    /**
     * Read a long integer value from properties by the specified key.
     *
     * @param key the property key to be used to retrieve property
     * @return the property value as long integer
     * @throws NumberFormatException if the property not found or is not a valid long integer
     */
    public static final long getLong(String key) {
    	return PROPS.getLong(key);
    }

    /**
     * Read a long integer value from properties by the specified key. If the property not found, we will use the
     * specified default value.
     *
     * @param key the property key to be used to retrieve property
     * @param defaultValue the default value, please make sure it's a valid long integer
     * @return the property value as long integer, or the <code>defaultValue</code> if the property not found
     * @throws NumberFormatException if the property not found or is not a valid long integer or the specified
     *             <code>defaultValue</code> is not a valid long integer
     */
    public static final long getLong(String key, String defaultValue) {
    	return PROPS.getLong(key, defaultValue);
    }

    /**
     * Read a long integer value from properties by the specified key. If the property not found, we will use the
     * specified default value.
     *
     * @param key the property key to be used to retrieve property
     * @param defaultValue the default value
     * @return the property value as long integer, or the <code>defaultValue</code> if the property not found
     * @throws NumberFormatException if the property not found or is not a valid long integer
     */
    public static final long getLong(String key, long defaultValue) {
    	return PROPS.getLong(key, defaultValue);
    }

    /**
     * Read boolean value from properties by the specified key. We will
     * interpret "true", "1", "on", "yes"(case insensitive) as true, all other values as false.
     *
     * @param key the property key to be used to retrieve property
     * @return the property value, or false if the property not found
     */
    public static boolean getBoolean(String key) {
        return PROPS.getBoolean(key);
    }

    /**
     * Read boolean value from properties by the specified key. We will
     * interpret "true", "1", "on", "yes"(case insensitive) as true, all other values as false.
     *
     * @param key the property key to be used to retrieve property
     * @param defaultValue the default value
     * @return the property value as boolean, or the <code>defaultValue</code> if the property not found
     */
    public static boolean getBoolean(String key, String defaultValue) {
        return PROPS.getBoolean(key, defaultValue);
    }

    /**
     * Read boolean value from properties by the specified key. We will
     * interpret "true", "1", "on", "yes"(case insensitive) as true, all other values as false.
     *
     * @param key the property key to be used to retrieve property
     * @param defaultValue the default value
     * @return the property value as boolean, or the <code>defaultValue</code> if the property not found
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        return PROPS.getBoolean(key, defaultValue);
    }

    /**
     * Read a double value from properties by the specified key.
     * 
     * @param key the property key to be used to retrieve property
     * @return the property value as double
     * @throws NullPointerException if the property not found
     * @throws NumberFormatException if the property is not a valid double number
     */
    public static double getDouble(String key) {
        return PROPS.getDouble(key);
    }

    /**
     * Read a double value from properties by the specified key. If the property not found, we will use the specified
     * default value.
     * 
     * @param key the property key to be used to retrieve property
     * @param defaultValue the default value
     * @return the property value as double, or the <code>defaultValue</code> if the property not found
     * @throws NullPointerException if the property not found and the <code>defaultValue</code> is null
     * @throws NumberFormatException if the property is not a valid double number or the default value is not a valid
     *             double number
     */
    public static double getDouble(String key, String defaultValue) {
        return PROPS.getDouble(key, defaultValue);
    }

    /**
     * Read a double value from properties by the specified key. If the property not found, we will use the specified
     * default value.
     * 
     * @param key the property key to be used to retrieve property
     * @param defaultValue the default value
     * @return the property value as double, or the <code>defaultValue</code> if the property not found
     * @throws NumberFormatException if the property is not a valid double number
     */
    public static double getDouble(String key, double defaultValue) {
        return PROPS.getDouble(key, defaultValue);
    }

}
