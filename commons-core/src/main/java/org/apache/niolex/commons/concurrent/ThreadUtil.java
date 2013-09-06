/**
 * ThreadUtil.java
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

import java.util.ArrayList;
import java.util.List;

import org.apache.niolex.commons.collection.CollectionUtil;

/**
 * Thread related utilities.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-7-24
 */
public class ThreadUtil {

    /**
     * Get the top thread group of this JVM.
     *
     * @return the top thread group
     */
    public static final ThreadGroup topGroup() {
        ThreadGroup topGroup = Thread.currentThread().getThreadGroup();
        ThreadGroup pareGroup = topGroup.getParent();
        // Iterate through the group link.
        while (pareGroup != null) {
            topGroup = pareGroup;
            pareGroup = pareGroup.getParent();
        }
        return topGroup;
    }

    /**
     * Get all the threads in this JVM.
     *
     * @return all the threads
     */
    public static final List<Thread> getAllThreads() {
        ThreadGroup topGroup = topGroup();
        return getAllThreadsInGroup(topGroup, topGroup.activeCount());
    }

    /**
     * Get all the threads in this thread group.
     *
     * @param group the specified thread group
     * @param size the group size
     * @return all the threads in the specified thread group
     */
    public static final List<Thread> getAllThreadsInGroup(ThreadGroup group, int size) {
        Thread[] array = null;
        do {
            array = new Thread[size + 100];
            size = group.enumerate(array, true);
        } while (size >= array.length);
        List<Thread> ret = new ArrayList<Thread>(size);
        CollectionUtil.addAll(ret, array);
        return ret;
    }

    /**
     * Make the current thread sleep, do not care about the exception.
     *
     * @param milliseconds the time to sleep in milliseconds
     */
    public static final void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (Exception e) {/*We Don't Care*/}
    }

    /**
     * Make the current thread sleep at least some time, do not care about the exception.
     *
     * @param milliseconds the time to sleep in milliseconds
     */
    public static final void sleepAtLeast(long milliseconds) {
        long in = System.currentTimeMillis(), t = 0;
        do {
            sleep(milliseconds - t);
            t = System.currentTimeMillis() - in;
        } while (t < milliseconds);
    }

}
