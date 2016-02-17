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
 * PropUtil是一个用来管理properties配置文件的工具类
 * <pre>
 * 目前提供的功能如下：
 * 1-1. public static final void loadConfig(String resource, Class&lt;T&gt; cls)
 * 从class path中加载指定名字的配置文件
 *
 * 1-2. public static final void loadConfig(String fileName)
 * 从文件系统中加载指定名字的配置文件
 *
 * 2-1. public static final String getProperty(String key)
 * 从配置文件中读取字符串类型的配置
 *
 * 2-2. public static final String getProperty(String key, String defaultValue)
 * 从配置文件中读取字符串类型的配置
 *
 * 3-1. public static final String getString(String key)
 * 从配置文件中读取字符串类型的配置
 *
 * 3-2. public static final String getString(String key, String defaultValue)
 * 从配置文件中读取字符串类型的配置
 *
 * 4-1. public static final int getInteger(String key)
 * 从配置文件中读取整数类型的配置
 *
 * 4-2. public static final int getInteger(String key, String defaultValue)
 * 从配置文件中读取整数类型的配置
 *
 * 4-3. public static final int getInteger(String key, int defaultValue)
 * 从配置文件中读取整数类型的配置
 *
 * 5-1. public static final long getLong(String key)
 * 从配置文件中读取长整数类型的配置
 *
 * 5-2. public static final long getLong(String key, String defaultValue)
 * 从配置文件中读取长整数类型的配置
 *
 * 5-3. public static final long getLong(String key, long defaultValue)
 * 从配置文件中读取长整数类型的配置
 *
 * 6-1. public static boolean getBoolean(String key)
 * 从配置文件中读取布尔类型的配置
 *
 * 6-2. public static boolean getBoolean(String key, String defaultValue)
 * 从配置文件中读取布尔类型的配置
 *
 * 6-3. public static boolean getBoolean(String key, boolean defaultValue)
 * 从配置文件中读取布尔类型的配置
 * </pre>
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 */

public abstract class PropUtil {
    private static final PropertiesWrapper PROPS = new PropertiesWrapper();

    /**
     * 从class path中加载指定名字的配置文件
     *
     * @param resource 待加载的文件的资源名
     * @param cls 携带待使用的类加载器
     * @throws IOException 假如读文件出错
     * @throws IllegalArgumentException 假如文件中含有非法字符
     * @throws NullPointerException 假如文件名是null
     */
    public static final <T> void loadConfig(String resource, Class<T> cls) throws IOException {
        PROPS.load(cls.getResourceAsStream(resource));
    }

    /**
     * 从文件系统中加载指定名字的配置文件
     *
     * @param fileName 待加载的文件名
     * @throws IOException 假如读文件出错
     * @throws IllegalArgumentException 假如文件中含有非法字符
     * @throws NullPointerException 假如文件名是null
     * @throws SecurityException 假如系统中配置了security manager并且该security manager拒绝了读文件的请求
     */
    public static final void loadConfig(String fileName) throws IOException {
        PROPS.load(new FileInputStream(fileName));
    }

    /**
     * 从配置文件中读取字符串类型的配置，不对字符串进行trim
     *
     * @param key 待读取的配置的键
     * @return 待读取的配置的值，如果配置不存在则返回null
     */
    public static final String getProperty(String key) {
        return PROPS.getProperty(key);
    }

    /**
     * 从配置文件中读取字符串类型的配置，不对字符串进行trim
     *
     * @param key 待读取的配置的键
     * @param defaultValue 待读取的配置的默认值
     * @return 待读取的配置的值，如果配置不存在则返回<code>defaultValue</code>
     */
    public static final String getProperty(String key, String defaultValue) {
        return PROPS.getProperty(key, defaultValue);
    }

    /**
     * 从配置文件中读取字符串类型的配置并对字符串进行trim
     *
     * @see #getProperty(String)
     * @param key 待读取的配置的键
     * @return 待读取的配置的值，如果配置不存在则返回null
     */
    public static final String getString(String key) {
        return PROPS.getString(key);
    }

    /**
     * 从配置文件中读取字符串类型的配置并对字符串进行trim
     *
     * @see #getProperty(String, String)
     * @param key 待读取的配置的键
     * @param defaultValue 待读取的配置的默认值
     * @return 待读取的配置的值，如果配置不存在则返回<code>defaultValue</code>
     */
    public static final String getString(String key, String defaultValue) {
        return PROPS.getString(key, defaultValue);
    }

    /**
     * 从配置文件中读取整数类型的配置
     *
     * @param key 待读取的配置的键
     * @return 待读取的配置的值，如果配置不存在则抛出NumberFormatException
     * @throws NumberFormatException 如果配置不存在,或者配置不是可以解析的整数
     */
    public static final int getInteger(String key) {
        return PROPS.getInteger(key);
    }

    /**
     * 从配置文件中读取整数类型的配置
     *
     * @param key 待读取的配置的键
     * @param defaultValue 待读取的配置的默认值，请确保该默认值是可以解析的整数
     * @return 待读取的配置的值，如果配置不存在则使用<code>defaultValue</code>
     * @throws NumberFormatException 如果配置不存在时<code>defaultValue</code>不是可以解析的整数，
     *         或者配置不是可以解析的整数
     */
    public static final int getInteger(String key, String defaultValue) {
        return PROPS.getInteger(key, defaultValue);
    }

    /**
     * 从配置文件中读取整数类型的配置
     *
     * @param key 待读取的配置的键
     * @param defaultValue 待读取的配置的默认值
     * @return 待读取的配置的值，如果配置不存在则使用<code>defaultValue</code>
     * @throws NumberFormatException 如果配置不是可以解析的整数
     */
    public static final int getInteger(String key, int defaultValue) {
        return PROPS.getInteger(key, defaultValue);
    }

    /**
     * 从配置文件中读取长整数类型的配置
     *
     * @param key 待读取的配置的键
     * @return 待读取的配置的值，如果配置不存在则抛出NumberFormatException
     * @throws NumberFormatException 如果配置不存在,或者配置不是可以解析的整数
     */
    public static final long getLong(String key) {
    	return PROPS.getLong(key);
    }

    /**
     * 从配置文件中读取长整数类型的配置
     *
     * @param key 待读取的配置的键
     * @param defaultValue 待读取的配置的默认值，请确保该默认值是可以解析的整数
     * @return 待读取的配置的值，如果配置不存在则使用<code>defaultValue</code>
     * @throws NumberFormatException 如果配置不存在时<code>defaultValue</code>不是可以解析的整数，
     *         或者配置不是可以解析的整数
     */
    public static final long getLong(String key, String defaultValue) {
    	return PROPS.getLong(key, defaultValue);
    }

    /**
     * 从配置文件中读取长整数类型的配置
     *
     * @param key 待读取的配置的键
     * @param defaultValue 待读取的配置的默认值
     * @return 待读取的配置的值，如果配置不存在则使用<code>defaultValue</code>
     * @throws NumberFormatException 如果配置不是可以解析的整数
     */
    public static final long getLong(String key, long defaultValue) {
    	return PROPS.getLong(key, defaultValue);
    }

    /**
     * 从配置文件中读取布尔类型的配置
     * 当且仅当配置为字符串"true", "1", "on", "yes"(忽略大小写)时，返回true,其他情况一概返回false
     *
     * @param key 待读取的配置的键
     * @return 待读取的配置的值，（请注意）如果配置不存在则返回false
     */
    public static boolean getBoolean(String key) {
        return PROPS.getBoolean(key);
    }

    /**
     * 从配置文件中读取布尔类型的配置
     * 当且仅当配置为字符串"true", "1", "on", "yes"(忽略大小写)时，返回true,其他情况一概返回false
     *
     * @param key 待读取的配置的键
     * @param defaultValue 待读取的配置的默认值
     * @return 待读取的配置的值，如果配置不存在则使用<code>defaultValue</code>
     */
    public static boolean getBoolean(String key, String defaultValue) {
        return PROPS.getBoolean(key, defaultValue);
    }

    /**
     * 从配置文件中读取布尔类型的配置
     * 当且仅当配置为字符串"true", "1", "on", "yes"(忽略大小写)时，返回true,其他情况一概返回false
     *
     * @param key 待读取的配置的键
     * @param defaultValue 待读取的配置的默认值
     * @return 待读取的配置的值，如果配置不存在则使用<code>defaultValue</code>
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        return PROPS.getBoolean(key, defaultValue);
    }
}
