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

import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;

import org.apache.niolex.commons.test.OrderedRunner;
import org.apache.niolex.commons.util.Runner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-7-27
 */
@RunWith(OrderedRunner.class)
public class SyncerTest {

    private static Locker regex = Syncer.syncByRegex(new DemoLocker(), "r.*", "w.*");
    private static Locker anno = Syncer.syncByAnnotation(new DemoLocker());

    CountDownLatch rl = new CountDownLatch(2);
    CountDownLatch wl = new CountDownLatch(1);

    public void read(int k) {
        rl.countDown();
        regex.read(k);
    }

    public void write(int k) {
        wl.countDown();
        regex.write(k);
    }

    /**
     * Test method for {@link org.apache.niolex.commons.concurrent.Syncer#syncByRegex(java.lang.Object, java.lang.String, java.lang.String)}.
     * @throws InterruptedException
     */
    @Test
    public final void test1SyncByRegex() throws InterruptedException {
        Runner.run(this, "read", 130);
        Runner.run(this, "read", 150);
        rl.await();
        ThreadUtil.sleep(1);
        Runner.run(regex, "ano2", 20);
        ThreadUtil.sleep(10);
        assertEquals(280, regex.getReadCnt());
        assertEquals(40, regex.getWriteCnt());
    }

    @Test
    public final void test2SyncByRegexCache() throws InterruptedException {
        Runner.run(this, "read", 130);
        Runner.run(this, "read", 150);
        rl.await();
        ThreadUtil.sleep(1);
        Runner.run(this, "write", 20);
        wl.await();
        ThreadUtil.sleep(10);
        assertEquals(560, regex.getReadCnt());
        assertEquals(40, regex.getWriteCnt());
        regex.ano1(3);
        regex.ano2(4);
        assertEquals(566, regex.getReadCnt());
        assertEquals(48, regex.getWriteCnt());
    }

    public void ano1(int k) {
        rl.countDown();
        anno.ano1(k);
    }

    public void ano2(int k) {
        wl.countDown();
        anno.ano2(k);
    }

    /**
     * Test method for {@link org.apache.niolex.commons.concurrent.Syncer#syncByAnnotation(java.lang.Object)}.
     * @throws InterruptedException
     */
    @Test
    public final void test3SyncByAnnotation() throws InterruptedException {
        Runner.run(this, "ano1", 30);
        Runner.run(this, "ano1", 50);
        rl.await();
        ThreadUtil.sleep(1);
        Runner.run(anno, "write", 20);
        ThreadUtil.sleep(10);
        assertEquals(160, anno.getReadCnt());
        assertEquals(20, anno.getWriteCnt());
    }

    @Test
    public final void test4SyncByAnnotationCache() throws InterruptedException {
        Runner.run(this, "ano1", 30);
        Runner.run(this, "ano1", 50);
        rl.await();
        ThreadUtil.sleep(1);
        Runner.run(this, "ano2", 20);
        wl.await();
        ThreadUtil.sleep(10);
        assertEquals(320, anno.getReadCnt());
        assertEquals(20, anno.getWriteCnt());
        anno.read(6);
        anno.write(3);
        assertEquals(326, anno.getReadCnt());
        assertEquals(23, anno.getWriteCnt());
    }

    /**
     * Test method for {@link org.apache.niolex.commons.concurrent.Syncer#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])}.
     */
    @Test
    public final void testInvoke() {
        Locker l = Syncer.syncByAnnotation(new QuickLocker());
        l.ano1(5);
        l.ano2(6);
        l.read(3);
        l.write(7);
        assertEquals(8, l.getReadCnt());
        assertEquals(13, l.getWriteCnt());
    }

    @Test
    public final void testInvokeRegex() {
        Locker l = Syncer.syncByRegex(new QuickLocker(), "r.*", "w.*");
        l.ano1(5);
        l.ano2(6);
        l.read(3);
        l.write(7);
        l.read(1);
        l.write(4);
        assertEquals(9, l.getReadCnt());
        assertEquals(17, l.getWriteCnt());
    }

}
