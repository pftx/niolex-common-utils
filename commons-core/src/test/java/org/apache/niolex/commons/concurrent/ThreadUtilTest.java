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
        Thread t = new Thread("Makr-Lex") {

            /**
             * This is the override of super method.
             * @see java.lang.Thread#run()
             */
            @Override
            public void run() {
                ThreadUtil.sleep(50);
            }

        };
        t.start();
        sleep(5);
        String r = "MainThreads " + getAllThreadsInGroup(Thread.currentThread().getThreadGroup(), -99);
        System.out.println(r);
        assertTrue(r.contains("Makr-Lex"));
    }

    @Test
    public void testSleep() throws Exception {
        sleep(-1);
    }

    @Test
    public void testSleepAtLeast() throws Exception {
        for (int i = 0; i < 10; i++) {
            long in = System.currentTimeMillis();
            sleepAtLeast(1);
            assertTrue(0 < (System.currentTimeMillis() - in));
        }
    }

}
