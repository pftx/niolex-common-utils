/**
 * PropertiesWrapper.java
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
package org.apache.niolex.commons.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.util.SystemUtil;

/**
 * This class wraps the standard java.util.Properties, provides convenient methods for
 * accessing trimmed string, int, boolean, long and double values.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-5-25
 */
public class PropertiesWrapper extends Properties {

    /**
     * The Generated ID.
     */
    private static final long serialVersionUID = -352075645997166876L;

    /**
     * Parse the boolean value from this parameter.
     * We interpret "true", "1", "on", "yes"(case insensitive) as true, all others as false.
     *
     * @param s the input string
     * @return the boolean value
     * @throws NullPointerException if the parameter is null
     */
    public static boolean parseBoolean(String s) {
        return StringUtil.isIn(s, false, "true", "1", "on", "yes");
    }

    /**
     * The default Constructor, creates an empty property list with no default values..
     */
    public PropertiesWrapper() {
        super();
    }

    /**
     * Creates an empty property list with the specified defaults.
     *
     * @param defaults the defaults
     */
    public PropertiesWrapper(Properties defaults) {
        super(defaults);
    }

    /**
     * Create a new PropertiesWrapper and read properties from the specified classpath resource file using the same
     * class loader which loaded the specified class.
     *
     * @param resource the classpath resource file
     * @param cls the class object to be used to retrieve class loader
     * @throws IOException if failed to read the resource file
     * @throws IllegalArgumentException if the file contains illegal character
     * @throws NullPointerException if any of the parameters is null
     */
    public PropertiesWrapper(String resource, Class<?> cls) throws IOException {
        load(resource, cls);
    }

    /**
     * Create a new PropertiesWrapper and read properties from the specified file. The <code>filePath</code> must be
     * the absolute path of the file in the file system.
     *
     * @param filePath the absolute file path and name
     * @throws IOException if failed to read the resource file
     * @throws IllegalArgumentException if the file contains illegal character
     * @throws NullPointerException if the parameter is null
     * @throws SecurityException if the configured security manager rejected the requirement to read the specified file
     */
    public PropertiesWrapper(String filePath) throws IOException {
        load(filePath);
    }

    /**
     * Read properties from the specified classpath resource file using the same
     * class loader which loaded the specified class.
     * <p>
     * If user call this method multiple times, the property with the same key will be overridden by the later
     * loaded one.
     * </p>
     *
     * @param resource the classpath resource file
     * @param cls the class object to be used to retrieve class loader
     * @throws IOException if failed to read the resource file
     * @throws IllegalArgumentException if the file contains illegal character
     * @throws NullPointerException if any of the parameters is null
     */
    public void load(String resource, Class<?> cls) throws IOException {
        load(cls.getResourceAsStream(resource));
    }

    /**
     * Read properties from the specified file. The <code>filePath</code> must be the
     * absolute path of the file in the file system.
     * <p>
     * If user call this method multiple times, the property with the same key will be overridden by the later
     * loaded one.
     * </p>
     *
     * @param filePath the absolute file path and name
     * @throws IOException if failed to read the resource file
     * @throws IllegalArgumentException if the file contains illegal character
     * @throws NullPointerException if the parameter is null
     * @throws SecurityException if the configured security manager rejected the requirement to read the specified file
     */
    public void load(String filePath) throws IOException {
        load(new FileInputStream(filePath));
    }

    /**
     * Reads a property list (key and element pairs) from the input
     * byte stream. We will close this stream here, instead of leave it open as the super method do.
     * This is the override of super method.
     *
     * @see java.util.Properties#load(java.io.InputStream)
     */
    @Override
    public synchronized void load(InputStream inStream) throws IOException {
        try {
            super.load(inStream);
        } finally {
            SystemUtil.close(inStream);
        }
    }

    /**
     * Read a string value from properties by the specified key. We will trim
     * the empty characters before return. We will return null if the property not found.
     *
     * @see #getProperty(String)
     * @param key the property key to be used to retrieve property
     * @return the property value, or null if the property not found
     */
    public final String getString(String key) {
        String s = this.getProperty(key);
        return s == null ? s : s.trim();
    }

    /**
     * Read a string value from properties by the specified key. We will trim the empty characters before return. We
     * will return the specified default value if the property not found.
     *
     * @see #getProperty(String, String)
     * @param key the property key to be used to retrieve property
     * @param defaultValue the default value
     * @return the property value, or the <code>defaultValue</code> if the property not found
     */
    public final String getString(String key, String defaultValue) {
        String s = this.getProperty(key, defaultValue);
        return s == null ? s : s.trim();
    }

    /**
     * Read an integer value from properties by the specified key.
     *
     * @param key the property key to be used to retrieve property
     * @return the property value as integer
     * @throws NumberFormatException if the property not found or is not a valid integer
     */
    public final int getInteger(String key) {
        return Integer.parseInt(this.getString(key));
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
    public final int getInteger(String key, String defaultValue) {
        return Integer.parseInt(this.getString(key, defaultValue));
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
    public final int getInteger(String key, int defaultValue) {
        String val = this.getString(key);
        return val == null ? defaultValue : Integer.parseInt(val);
    }

    /**
     * Read a long integer value from properties by the specified key.
     *
     * @param key the property key to be used to retrieve property
     * @return the property value as long integer
     * @throws NumberFormatException if the property not found or is not a valid long integer
     */
    public final long getLong(String key) {
        return Long.parseLong(this.getString(key));
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
    public final long getLong(String key, String defaultValue) {
        return Long.parseLong(this.getString(key, defaultValue));
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
    public final long getLong(String key, long defaultValue) {
        String val = this.getString(key);
        return val == null ? defaultValue : Long.parseLong(val);
    }

    /**
     * Read boolean value from properties by the specified key. We will
     * interpret "true", "1", "on", "yes"(case insensitive) as true, all other values as false.
     * Specially, we will return false if the property not found.
     *
     * @param key the property key to be used to retrieve property
     * @return the property value, or false if the property not found
     */
    public boolean getBoolean(String key) {
        String s = this.getString(key);
        return s == null ? false : parseBoolean(s);
    }

    /**
     * Read boolean value from properties by the specified key. We will
     * interpret "true", "1", "on", "yes"(case insensitive) as true, all other values as false.
     *
     * @param key the property key to be used to retrieve property
     * @param defaultValue the default value
     * @return the property value as boolean, or the <code>defaultValue</code> if the property not found
     */
    public boolean getBoolean(String key, String defaultValue) {
        String s = this.getString(key, defaultValue);
        return s == null ? false : parseBoolean(s);
    }

    /**
     * Read boolean value from properties by the specified key. We will
     * interpret "true", "1", "on", "yes"(case insensitive) as true, all other values as false.
     *
     * @param key the property key to be used to retrieve property
     * @param defaultValue the default value
     * @return the property value as boolean, or the <code>defaultValue</code> if the property not found
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        String s = this.getString(key);
        return s == null ? defaultValue : parseBoolean(s);
    }

    /**
     * Read a double value from properties by the specified key.
     * 
     * @param key the property key to be used to retrieve property
     * @return the property value as double
     * @throws NullPointerException if the property not found
     * @throws NumberFormatException if the property is not a valid double number
     */
    public double getDouble(String key) {
        return Double.parseDouble(this.getString(key));
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
    public double getDouble(String key, String defaultValue) {
        return Double.parseDouble(this.getString(key, defaultValue));
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
    public double getDouble(String key, double defaultValue) {
        String val = this.getString(key);
        return val == null ? defaultValue : Double.parseDouble(val);
    }

}
