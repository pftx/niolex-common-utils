/**
 * ConcSyncerTest.java
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

import java.util.concurrent.CountDownLatch;

import org.apache.niolex.commons.util.Runner;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-7-27
 */
public class ConcSyncerTest {
    private static Locker regex = Syncer.syncByRegex(new QuickLocker(true), "r.*", "w.*");
    private static Locker anno = Syncer.syncByAnnotation(new QuickLocker(true));

    CountDownLatch rl = new CountDownLatch(1);

    public void read(int k) {
        rl.countDown();
        regex.read(k);
    }

    @Test(timeout=500)
    public final void test1SyncByRegex() throws InterruptedException {
        Runner.run(this, "read", 130);
        rl.await();
        ThreadUtil.sleepAtLeast(10);
        for (int i = 0; i < 10; ++i) {
            regex.read(i);
        }
        assertEquals(175, regex.getReadCnt());
    }

    public void anoRead(int k) {
        rl.countDown();
        anno.anoRead(k);
    }

    @Test(timeout=500)
    public final void test3SyncByAnnotation() throws InterruptedException {
        Runner.run(this, "anoRead", 30);
        rl.await();
        ThreadUtil.sleepAtLeast(10);
        for (int i = 0; i < 10; ++i) {
            anno.anoRead(i);
        }
        assertEquals(75, anno.getReadCnt());
    }
}
