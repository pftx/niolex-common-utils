/**
 * SimpleThreadFactoryTest.java
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


import static org.junit.Assert.assertEquals;

import org.apache.niolex.commons.test.Counter;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-7-23
 */
public class SimpleThreadFactoryTest {

    private SimpleThreadFactory factory;

    @Before
    public void testSimpleThreadFactory() throws Exception {
        factory = new SimpleThreadFactory("thread-fac-test");
    }

    @Test
    public void testNewThread() throws Exception {
        final Counter cnt = new Counter();
        final Blocker<Integer> blocker = new Blocker<Integer>();
        Thread t = factory.newThread(new Runnable() {

            @Override
            public void run() {
                blocker.release("s", 1);
                while (cnt.cnt() < 50) {
                    cnt.inc();
                    if (Thread.interrupted()) break;
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }});
        WaitOn<Integer> waitOn = blocker.initWait("s");
        t.start();
        waitOn.waitForResult(100);
        assertEquals(t.getThreadGroup(), factory.getThreadGroup());
        assertEquals(t.getName(), "thread-fac-test@0");
        assertEquals(1, factory.getThreadGroup().activeCount());
        factory.getThreadGroup().interrupt();
        Thread.sleep(1);
        assertEquals(0, factory.getThreadGroup().activeCount());
    }

    @Test
    public void testGetThreadGroup() throws Exception {
        assertEquals(factory.getThreadGroup().getName(), "thread-fac-test");
        assertEquals(0, factory.getThreadGroup().activeCount());
    }

}
