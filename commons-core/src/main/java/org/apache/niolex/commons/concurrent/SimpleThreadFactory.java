/**
 * SimpleThreadFactory.java
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

import java.util.concurrent.ThreadFactory;

/**
 * This is a very simple thread factory, all the threads created by this class will be filled into
 * the internal thread group.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-7-22
 */
public class SimpleThreadFactory implements ThreadFactory {

    // The internal thread group.
    private final ThreadGroup threadGroup;
    private final String threadGroupName;
    private int cnt = 0;

    /**
     * The only Constructor, we will create a ThreadGroup internally.
     *
     * @param threadGroupName the internal thread group name
     */
    public SimpleThreadFactory(String threadGroupName) {
        super();
        this.threadGroup = new ThreadGroup(threadGroupName);
        this.threadGroupName = threadGroupName;
    }



    /**
     * This is the override of super method.
     * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
     */
    @Override
    public Thread newThread(Runnable r) {
        return new Thread(threadGroup, r, threadGroupName + "@" + cnt++);
    }

    /**
     * @return the threadGroup
     */
    public ThreadGroup getThreadGroup() {
        return threadGroup;
    }

}
