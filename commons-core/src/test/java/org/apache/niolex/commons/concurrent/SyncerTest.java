/**
 * SyncerTest.java
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
public class SyncerTest {

    private CountDownLatch rl = new CountDownLatch(2);
    private CountDownLatch wl = new CountDownLatch(1);

    private Locker regex = Syncer.syncByRegex(new LockerImpl(rl, wl, rl, wl), "r.*", "w.*");
    private Locker anno = Syncer.syncByAnnotation(new LockerImpl(rl, wl, rl, wl));

    /**
     * Test method for {@link org.apache.niolex.commons.concurrent.Syncer#syncByRegex(java.lang.Object, java.lang.String, java.lang.String)}.
     * @throws InterruptedException
     */
    @Test
    public final void testSyncByRegexRead() throws InterruptedException {
        Runner.run(regex, "read", 130);
        Runner.run(regex, "read", 150);
        rl.await();
        Runner.run(regex, "write", 20);
        ThreadUtil.sleepAtLeast(20);
        assertEquals(280, regex.getReadCnt());
        assertEquals(0, regex.getWriteCnt());
        Runner.run(regex, "anoWrite", 20);
        wl.await();
        ThreadUtil.sleepAtLeast(20);
        assertEquals(280, regex.getReadCnt());
        assertEquals(40, regex.getWriteCnt());
    }

    @Test
    public final void testSyncByRegexWrite() throws InterruptedException {
        Runner.run(regex, "write", 20);
        wl.await();
        Runner.run(regex, "read", 130);
        Runner.run(regex, "read", 150);
        ThreadUtil.sleepAtLeast(20);
        assertEquals(0, regex.getReadCnt());
        assertEquals(20, regex.getWriteCnt());
        Runner.run(regex, "anoRead", 15);
        Runner.run(regex, "anoRead", 13);
        rl.await();
        ThreadUtil.sleepAtLeast(20);
        assertEquals(308, regex.getReadCnt());
        assertEquals(20, regex.getWriteCnt());
    }

    /**
     * Test method for {@link org.apache.niolex.commons.concurrent.Syncer#syncByAnnotation(java.lang.Object)}.
     * @throws InterruptedException
     */
    @Test
    public final void testSyncByAnnotationR() throws InterruptedException {
        Runner.run(anno, "anoRead", 15);
        Runner.run(anno, "anoRead", 13);
        rl.await();
        Runner.run(anno, "anoWrite", 20);
        ThreadUtil.sleepAtLeast(20);
        assertEquals(28, anno.getReadCnt());
        assertEquals(0, anno.getWriteCnt());
        Runner.run(anno, "write", 2);
        wl.await();
        ThreadUtil.sleepAtLeast(20);
        assertEquals(28, anno.getReadCnt());
        assertEquals(22, anno.getWriteCnt());
    }

    @Test
    public final void testSyncByAnnotationW() throws InterruptedException {
        Runner.run(anno, "anoWrite", 20);
        wl.await();
        Runner.run(anno, "anoRead", 15);
        Runner.run(anno, "anoRead", 13);
        ThreadUtil.sleepAtLeast(20);
        assertEquals(0, anno.getReadCnt());
        assertEquals(20, anno.getWriteCnt());
        Runner.run(anno, "read", 200);
        Runner.run(anno, "read", 2);
        Runner.run(anno, "anoWrite", 2);
        ThreadUtil.sleepAtLeast(20);
        assertEquals(230, anno.getReadCnt());
        assertEquals(22, anno.getWriteCnt());
    }

}
