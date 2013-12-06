/**
 * ThreadUtilTest.java
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


import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;

import org.apache.niolex.commons.bean.One;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-7-24
 */
public class ThreadUtilTest extends ThreadUtil {

    @Test
    public void testTopGroup() throws Exception {
        topGroup().list();
    }

    @Test
    public void testGetAllThreads() throws Exception {
        System.out.println("AllThreads " + getAllThreads());
    }

    @Test
    public void testGetAllThreadsInGroup() throws Exception {
        Thread t = new BlockThread("Makr-Lex", 500);
        t.start();
        String r = "MainThreads " + getAllThreadsInGroup(Thread.currentThread().getThreadGroup(), -99);
        System.out.println(r);
        t.interrupt();
        assertTrue(r.contains("Makr-Lex"));
    }

    @Test
    public void testSleep() throws Exception {
        sleep(-1);
    }

    @Test
    public void testSleepAtLeast() throws Exception {
        final Thread t1 = new BlockThread("Makr-Lex") {

            @Override
            public void run0() {
                ThreadUtil.sleepAtLeast(100);
            }

        };
        t1.start();
        for (int i = 0; i < 20; i++) {
            t1.interrupt();
            ThreadUtil.sleepAtLeast(1);
        }
    }

    @Test
    public void testJoin() throws Exception {
        final Thread t1 = new BlockThread("Makr-Lex", 50000);
        t1.start();
        final CountDownLatch c2 = new CountDownLatch(1);
        final Thread t2 = new Thread("Makr-Lex") {
            @Override
            public void run() {
                c2.countDown();
                ThreadUtil.join(t1);
            }
        };
        t2.start();
        c2.await();
        t1.interrupt();
        ThreadUtil.join(t2);
    }

    @Test
    public void testJoinInterupt() throws Exception {
        final CountDownLatch c1 = new CountDownLatch(1);
        final Thread t1 = new Thread("Makr-Lex") {

            /**
             * This is the override of super method.
             * @see java.lang.Thread#run()
             */
            @Override
            public void run() {
                c1.countDown();
                ThreadUtil.sleep(50000);
            }

        };
        t1.start();
        c1.await();
        final CountDownLatch c2 = new CountDownLatch(1);
        final One<Boolean> one = One.create(true);
        final Thread t2 = new Thread("Makr-Lex") {
            @Override
            public void run() {
                c2.countDown();
                one.a = ThreadUtil.join(t1);
            }
        };
        t2.start();
        c2.await();
        t2.interrupt();
        ThreadUtil.join(t2);
        assertFalse(one.a);
    }

    @Test
    public void testWaitFor() throws Exception {
        final CountDownLatch c1 = new CountDownLatch(1);
        final One<Boolean> one = One.create(true);
        final Thread t1 = new Thread("WaitFor-Lex") {

            /**
             * This is the override of super method.
             * @see java.lang.Thread#run()
             */
            @Override
            public void run() {
                one.a = waitFor(c1);
                c1.countDown();
            }

        };
        t1.start();
        t1.interrupt();
        join(t1);
        assertFalse(one.a);
        // now already count down.
        assertTrue(waitFor(c1));
    }

}
