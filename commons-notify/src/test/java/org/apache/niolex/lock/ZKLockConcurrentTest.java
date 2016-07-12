/**
 * ZKLockConcurrentTest.java
 *
 * Copyright 2016 the original author or authors.
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
package org.apache.niolex.lock;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.niolex.commons.test.MockUtil;
import org.apache.niolex.notify.AppTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2016-4-15
 */
public class ZKLockConcurrentTest {
    private static final String BS = "/lock/zkc/tmp-" + MockUtil.randInt(100, 999);
    private static int THREAD_CNT = 10;
    private static int TOTAL_CNT = 100;

    @Test
    public void testConcLock() throws Exception {
        THREAD_CNT = 4;
        TOTAL_CNT = 50;
        main(null);
    }

    public static void main(String[] args) throws Exception {
        AppTest.cleanZK(BS);
        
        Thread[] tarr = new Thread[THREAD_CNT];
        final AtomicInteger count1 = new AtomicInteger();
        final AtomicInteger count2 = new AtomicInteger();
        final AtomicInteger flag = new AtomicInteger();

        for (int i = 0; i < THREAD_CNT; ++i) {
            ZKLock lock = new ZKLock(AppTest.URL, 6000, BS);
            if (i % 2 == 0) {
                tarr[i] = new Thread(new ZKLockConcurrent1(lock, count1, flag, TOTAL_CNT));
            } else {
                tarr[i] = new Thread(new ZKLockConcurrent2(lock, count2, flag, TOTAL_CNT));
            }

            tarr[i].start();
        }

        for (int i = 0; i < THREAD_CNT; ++i) {
            tarr[i].join();
        }

        assertEquals(TOTAL_CNT * THREAD_CNT / 2, count1.get());
        assertTrue(count2.get() <= count1.get());
        assertEquals(0, flag.get());
        System.out.println("testConcLock done.");
    }

}

class ZKLockConcurrent1 implements Runnable {
    protected static final Logger LOG = LoggerFactory.getLogger(ZKLockConcurrentTest.class);

    private final ZKLock lock;
    private final AtomicInteger count;
    private final AtomicInteger flag;
    private final int totalCount;

    /**
     * Constructor
     * @param lock
     * @param count
     * @param flag
     * @param totalCount
     */
    public ZKLockConcurrent1(ZKLock lock, AtomicInteger count, AtomicInteger flag, int totalCount) {
        super();
        this.lock = lock;
        this.count = count;
        this.flag = flag;
        this.totalCount = totalCount;
    }

    /**
     * This is the override of super method.
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        for (int i = 0; i < totalCount; ++i) {
            lock.lock();
            try {
                int c = flag.incrementAndGet();
                count.incrementAndGet();
                if (c != 1) {
                    LOG.error("Failed to keep lock semantics: c = " + c);
                }
                flag.decrementAndGet();
            } finally {
                lock.unlock();
            }
        }
        LOG.error("ZKLockConcurrent1 finished.");
    }

}

class ZKLockConcurrent2 implements Runnable {
    protected static final Logger LOG = LoggerFactory.getLogger(ZKLockConcurrentTest.class);

    private final ZKLock lock;
    private final AtomicInteger count;
    private final AtomicInteger flag;
    private final int totalCount;

    /**
     * Constructor
     * @param lock
     * @param count
     * @param flag
     * @param totalCount
     */
    public ZKLockConcurrent2(ZKLock lock, AtomicInteger count, AtomicInteger flag, int totalCount) {
        super();
        this.lock = lock;
        this.count = count;
        this.flag = flag;
        this.totalCount = totalCount;
    }

    /**
     * This is the override of super method.
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        for (int i = 0; i < totalCount; ++i) {
            try {
                if (lock.tryLock(100, TimeUnit.MILLISECONDS))
                    try {
                        int c = flag.incrementAndGet();
                        count.incrementAndGet();
                        if (c != 1) {
                            LOG.error("Failed to keep lock semantics: c = " + c);
                        }
                        flag.decrementAndGet();
                    } finally {
                        lock.unlock();
                    }
            } catch (InterruptedException e) {
            }

        }
        LOG.error("ZKLockConcurrent2 finished.");
    }

}