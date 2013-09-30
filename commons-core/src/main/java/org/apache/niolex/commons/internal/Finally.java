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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
     * Write the data to the output stream and close it.
     *
     * @param zout
     * @param data
     * @throws IOException
     */
    public static void writeAndClose(OutputStream zout, byte[] data) throws IOException {
        try {
            zout.write(data);
        } finally {
            zout.close();
        }
    }

    /**
     * Transfer all the data from the input stream to the output stream and close them.
     *
     * @param in
     * @param out
     * @param BUF_SIZE
     * @throws IOException
     */
    public static void transferAndClose(InputStream in, OutputStream out, final int BUF_SIZE) throws IOException {
        try {
            byte[] data = new byte[BUF_SIZE];
            int len;
            while ((len = in.read(data)) != -1) {
                out.write(data, 0, len);
            }
        } finally {
            in.close();
            out.close();
        }
    }

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
