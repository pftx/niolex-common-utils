/**
 * Syncer.java
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
package org.apache.niolex.commons.concurrent;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Pattern;

import org.apache.niolex.commons.internal.Finally;

import com.google.common.collect.Maps;

/**
 * The <b>Syncer</b> is a utility to decorate an object into synchronized object for
 * concurrent use in multiple threads environment.<br>
 * We use read/write lock for better performance.<br>
 *
 * We support two techniques: annotation and regex match for method name.<br>
 *
 * Annotation:
 *  Mark the method with @Read for acquiring read lock.
 *  Mark the method with @Write for acquiring write lock.
 *  All other methods will not be synchronized.
 * <br>
 * Regex match for method name:
 *  User input two regex expressions, one for read method name match, the other
 *  for write method name match. If a method match both, we prefer write lock.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-7-27
 */
public class Syncer implements InvocationHandler {

    /**
     * The Read lock mark annotation. Use this for methods which need read lock.
     *
     * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
     * @version 1.0.0
     * @since 2013-7-27
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    public static @interface Read{}

    /**
     * The Write lock mark annotation. Use this for methods which need write lock.
     *
     * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
     * @version 1.0.0
     * @since 2013-7-27
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    public static @interface Write{}

    // The way to be used for method match.
    public static final int BY_REGEX = 1;
    public static final int BY_ANNO = 2;

    // The method lock flag.
    public static final Integer LOCK_READ = 1;
    public static final Integer LOCK_WRITE = 2;
    public static final Integer NO_LOCK = 3;

    /**
     * Decorate the host object by this Syncer using the regex patterns to match methods.
     * <b>We are using Java proxy technique, only delegate the interfaces.</b>
     *
     * @param host the host object for this decoration
     * @param readRegex the read regex
     * @param writeRegex the write regex
     * @return the decorated object
     */
    @SuppressWarnings("unchecked")
    public static final <T> T syncByRegex(Object host, String readRegex, String writeRegex) {
        Syncer s = new Syncer(BY_REGEX, host, Pattern.compile(readRegex), Pattern.compile(writeRegex));
        Object ret = Proxy.newProxyInstance(host.getClass().getClassLoader(),
                host.getClass().getInterfaces(), s);
        return (T)ret;
    }

    /**
     * Decorate the host object by this Syncer using the annotations to match methods.
     * <b>We are using Java proxy technique, only delegate the interfaces.</b>
     *
     * @param host the host object for this decoration
     * @return the decorated object
     */
    @SuppressWarnings("unchecked")
    public static final <T> T syncByAnnotation(Object host) {
        Syncer s = new Syncer(BY_ANNO, host, null, null);
        Object ret = Proxy.newProxyInstance(host.getClass().getClassLoader(),
                host.getClass().getInterfaces(), s);
        return (T)ret;
    }

    // The internal cache for higher speed.
    private final Map<Method, Integer> cache = Maps.newConcurrentMap();

    // The internal lock for this class.
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    // The current match method.
    private final int matchBy;

    // The host object.
    private final Object host;

    // The read and write regex pattern if it's using the regex way.
    private final Pattern readPattern;
    private final Pattern writePattern;

    /**
     * The private Constructor, user need to invoke the static factory method.
     *
     * @param matchBy the match method
     * @param readPattern the read regex pattern
     * @param writePattern the write regex pattern
     */
    private Syncer(int matchBy, Object host, Pattern readPattern, Pattern writePattern) {
        super();
        this.matchBy = matchBy;
        this.host = host;
        this.readPattern = readPattern;
        this.writePattern = writePattern;
    }

    /**
     * This is the override of super method.
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Integer i = cache.get(method);
        if (i != null) {
            // Use cache.
            if (i == LOCK_WRITE) {
                return Finally.useWriteLock(lock, host, method, args);
            } else if (i == LOCK_READ) {
                return Finally.useReadLock(lock, host, method, args);
            } else {
                // Do not use lock.
                return method.invoke(host, args);
            }
        }
        if (matchBy == BY_REGEX) {
            // Regex way.
            String name = method.getName();
            if (writePattern.matcher(name).matches()) {
                // Write lock.
                cache.put(method, LOCK_WRITE);
                return Finally.useWriteLock(lock, host, method, args);
            } else if (readPattern.matcher(name).matches()) {
                // Read lock.
                cache.put(method, LOCK_READ);
                return Finally.useReadLock(lock, host, method, args);
            } else {
                // No lock.
                cache.put(method, NO_LOCK);
                return method.invoke(host, args);
            }
        } else {
            // Annotation way.
            if (method.isAnnotationPresent(Write.class)) {
                // Write lock.
                cache.put(method, LOCK_WRITE);
                return Finally.useWriteLock(lock, host, method, args);
            } else if (method.isAnnotationPresent(Read.class)) {
                // Read lock.
                cache.put(method, LOCK_READ);
                return Finally.useReadLock(lock, host, method, args);
            } else {
                // No lock.
                cache.put(method, NO_LOCK);
                return method.invoke(host, args);
            }
        }
        // This path will not happen.
    }

}
