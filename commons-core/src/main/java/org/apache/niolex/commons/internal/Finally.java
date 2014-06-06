/**
 * Finally.java
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
package org.apache.niolex.commons.internal;

import java.lang.reflect.Method;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Do things in the finally block.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-6-14
 */
public class Finally {

    /**
     * Use read lock to invoke this method.
     *
     * @param lock
     * @param proxy
     * @param method
     * @param args
     * @return the result
     * @throws Throwable
     */
    public static Object useReadLock(ReentrantReadWriteLock lock, Object proxy, Method method, Object[] args)
            throws Throwable {
        lock.readLock().lock();
        try {
            return method.invoke(proxy, args);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Use write lock to invoke this method.
     *
     * @param lock
     * @param proxy
     * @param method
     * @param args
     * @return the result
     * @throws Throwable
     */
    public static Object useWriteLock(ReentrantReadWriteLock lock, Object proxy, Method method, Object[] args)
            throws Throwable {
        lock.writeLock().lock();
        try {
            return method.invoke(proxy, args);
        } finally {
            lock.writeLock().unlock();
        }
    }

}
