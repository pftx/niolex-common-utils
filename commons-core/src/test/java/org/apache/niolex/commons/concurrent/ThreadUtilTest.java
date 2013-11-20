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


import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;

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
        final CountDownLatch latch = new CountDownLatch(1);
        Thread t = new Thread("Makr-Lex") {

            /**
             * This is the override of super method.
             * @see java.lang.Thread#run()
             */
            @Override
            public void run() {
                latch.countDown();
                ThreadUtil.sleep(500);
            }

        };
        t.start();
        latch.await();
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
        final CountDownLatch c1 = new CountDownLatch(1);
        final Thread t1 = new Thread("Makr-Lex") {

            /**
             * This is the override of super method.
             * @see java.lang.Thread#run()
             */
            @Override
            public void run() {
                c1.countDown();
                ThreadUtil.sleepAtLeast(100);
            }

        };
        t1.start();
        c1.await();
        for (int i = 0; i < 20; i++) {
            t1.interrupt();
            ThreadUtil.sleepAtLeast(1);
        }
    }

    @Test
    public void testJoin() throws Exception {
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
        final Thread t2 = new Thread("Makr-Lex") {
            @Override
            public void run() {
                c2.countDown();
                ThreadUtil.join(t1);
            }
        };
        t2.start();
        c2.await();
        t2.interrupt();
    }

}
