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

import org.apache.niolex.commons.util.SystemUtil;

/**
 * PropertiesWrapper是在PropUtil的基础上发展而来。对于普通项目只管理一个配置文件来说，使用PropUtil的静态方法来取得配置无疑很
 * 方便。但是如果要同时管理多个配置文件，那么静态方法就显得不足。所以需要本类来满足这类型的需求。
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-5-25
 */
public class PropertiesWrapper extends Properties {

    /**
     * Generated ID
     */
    private static final long serialVersionUID = -352075645997166876L;

    /**
     * 默认构造函数
     * Constructor
     */
    public PropertiesWrapper() {
        super();
    }

    /**
     * 从父类继承的构造函数
     * Creates an empty property list with the specified defaults.
     *
     * @param defaults
     */
    public PropertiesWrapper(Properties defaults) {
        super(defaults);
    }

    /**
     * 从class path中加载指定名字的配置文件
     *
     * @param resource 待加载的文件的资源名
     * @param cls 携带待使用的类加载器
     * @throws IOException 假如读文件出错
     * @throws IllegalArgumentException 假如文件中含有非法字符
     * @throws NullPointerException 假如文件名是null
     */
    public PropertiesWrapper(String resource, Class<?> cls) throws IOException {
        load(resource, cls);
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
    public PropertiesWrapper(String fileName) throws IOException {
        load(fileName);
    }

    /**
     * 从class path中加载指定名字的配置文件
     *
     * @param resource 待加载的文件的资源名
     * @param cls 携带待使用的类加载器
     * @throws IOException 假如读文件出错
     * @throws IllegalArgumentException 假如文件中含有非法字符
     * @throws NullPointerException 假如文件名是null
     */
    public void load(String resource, Class<?> cls) throws IOException {
        InputStream in = cls.getResourceAsStream(resource);
        try {
            load(in);
        } finally {
            SystemUtil.close(in);
        }
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
    public void load(String fileName) throws IOException {
        InputStream in = new FileInputStream(fileName);
        try {
            load(in);
        } finally {
            SystemUtil.close(in);
        }
    }


    /**
     * 从配置文件中读取字符串类型的配置
     * 与getProperty不同的是，我们会trim掉前后的空格，这样有利于进行格式转换
     *
     * @see #getProperty(String)
     * @param key 待读取的配置的键
     * @return 待读取的配置的值，如果配置不存在则返回null
     */
    public final String getString(String key) {
        String s = this.getProperty(key);
        return s == null ? s : s.trim();
    }

    /**
     * 从配置文件中读取字符串类型的配置
     * 与getProperty不同的是，我们会trim掉前后的空格，这样有利于进行格式转换
     *
     * @see #getProperty(String, String)
     * @param key 待读取的配置的键
     * @param defaultValue 待读取的配置的默认值
     * @return 待读取的配置的值，如果配置不存在则返回<code>defaultValue</code>
     */
    public final String getString(String key, String defaultValue) {
        String s = this.getProperty(key, defaultValue);
        return s == null ? s : s.trim();
    }

    /**
     * 从配置文件中读取整数类型的配置
     *
     * @param key 待读取的配置的键
     * @return 待读取的配置的值，如果配置不存在则抛出NumberFormatException
     * @throws NumberFormatException 如果配置不存在,或者配置不是可以解析的整数
     */
    public final int getInteger(String key) {
        return Integer.parseInt(this.getString(key));
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
    public final int getInteger(String key, String defaultValue) {
        return Integer.parseInt(this.getString(key, defaultValue));
    }

    /**
     * 从配置文件中读取整数类型的配置
     *
     * @param key 待读取的配置的键
     * @param defaultValue 待读取的配置的默认值
     * @return 待读取的配置的值，如果配置不存在则使用<code>defaultValue</code>
     * @throws NumberFormatException 如果配置不是可以解析的整数
     */
    public final int getInteger(String key, int defaultValue) {
        return Integer.parseInt(this.getString(key, Integer.toString(defaultValue)));
    }

    /**
     * 从配置文件中读取长整数类型的配置
     *
     * @param key 待读取的配置的键
     * @return 待读取的配置的值，如果配置不存在则抛出NumberFormatException
     * @throws NumberFormatException 如果配置不存在,或者配置不是可以解析的整数
     */
    public final long getLong(String key) {
        return Long.parseLong(this.getString(key));
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
    public final long getLong(String key, String defaultValue) {
        return Long.parseLong(this.getString(key, defaultValue));
    }

    /**
     * 从配置文件中读取长整数类型的配置
     *
     * @param key 待读取的配置的键
     * @param defaultValue 待读取的配置的默认值
     * @return 待读取的配置的值，如果配置不存在则使用<code>defaultValue</code>
     * @throws NumberFormatException 如果配置不是可以解析的整数
     */
    public final long getLong(String key, long defaultValue) {
        return Long.parseLong(this.getString(key, Long.toString(defaultValue)));
    }

    /**
     * 从配置文件中读取布尔类型的配置
     * 当且仅当配置为字符串"true"(忽略大小写)时，返回true,其他情况一概返回false
     *
     * @param key 待读取的配置的键
     * @return 待读取的配置的值，（请注意）如果配置不存在则返回false
     */
    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(this.getString(key));
    }

    /**
     * 从配置文件中读取布尔类型的配置
     * 当且仅当配置为字符串"true"(忽略大小写)时，返回true,其他情况一概返回false
     *
     * @param key 待读取的配置的键
     * @param defaultValue 待读取的配置的默认值
     * @return 待读取的配置的值，如果配置不存在则使用<code>defaultValue</code>
     */
    public boolean getBoolean(String key, String defaultValue) {
        return Boolean.parseBoolean(this.getString(key, defaultValue));
    }

    /**
     * 从配置文件中读取布尔类型的配置
     * 当且仅当配置为字符串"true"(忽略大小写)时，返回true,其他情况一概返回false
     *
     * @param key 待读取的配置的键
     * @param defaultValue 待读取的配置的默认值
     * @return 待读取的配置的值，如果配置不存在则使用<code>defaultValue</code>
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        return Boolean.parseBoolean(this.getString(key, Boolean.toString(defaultValue)));
    }

}
