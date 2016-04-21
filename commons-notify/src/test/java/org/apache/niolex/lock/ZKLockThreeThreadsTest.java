/**
 * ZKLockThreeThreadsTest.java
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

import static org.junit.Assert.assertEquals;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.niolex.commons.bean.One;
import org.apache.niolex.commons.test.MockUtil;
import org.apache.niolex.commons.util.Runner;
import org.apache.niolex.notify.AppTest;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2016-4-21
 */
public class ZKLockThreeThreadsTest {
    private static final String BS = "/lock/zkc/tmp-" + MockUtil.randInt(100, 999);

    Exception exception1;
    Exception exception2;
    Exception exception3;
    CountDownLatch latch = new CountDownLatch(3);

    public void lock1(ZKLock lock) {
        latch.countDown();
        try {
            lock.lock();
        } catch (Exception e) {
            exception1 = e;
        }
    }

    public void lock2(ZKLock lock) {
        latch.countDown();
        try {
            lock.lockInterruptibly();
        } catch (Exception e) {
            exception2 = e;
        }
    }

    public void lock3(ZKLock lock) {
        latch.countDown();
        try {
            lock.tryLock(5000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            exception3 = e;
        }
    }

    @Test
    public void testThreeLock() throws Exception {
        ZKLock lock = new ZKLock(AppTest.URL, 6000, BS);

        lock.lock();

        One<Thread> threadVal_1 = One.create(null);
        Runner.run(threadVal_1, this, "lock1", lock);

        One<Thread> threadVal_2 = One.create(null);
        Runner.run(threadVal_2, this, "lock2", lock);

        One<Thread> threadVal_3 = One.create(null);
        Runner.run(threadVal_3, this, "lock3", lock);

        latch.await();
        lock.unlock();

        threadVal_1.a.join();
        threadVal_2.a.join();
        threadVal_3.a.join();

        int ex = 0;
        int ok = 0;
        if (exception1 == null) {
            ++ok;
        } else {
            exception1.printStackTrace();
            ++ex;
        }
        if (exception2 == null) {
            ++ok;
        } else {
            exception2.printStackTrace();
            ++ex;
        }
        if (exception3 == null) {
            ++ok;
        } else {
            exception3.printStackTrace();
            ++ex;
        }

        lock.unlock();

        assertEquals(1, ok);
        assertEquals(2, ex);
    }
}
