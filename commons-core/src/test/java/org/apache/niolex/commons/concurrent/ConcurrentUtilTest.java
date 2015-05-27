/**
 * ConcurrentUtilTest.java
 *
 * Copyright 2012 The original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.niolex.commons.concurrent;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2012-12-20
 */
public class ConcurrentUtilTest extends ConcurrentUtil {

    /**
     * Test method for {@link org.apache.niolex.commons.concurrent.ConcurrentUtil#initMap(java.util.concurrent.ConcurrentHashMap, java.lang.Object, java.lang.Object)}.
     */
    @Test
    public void testInitMap1() {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
        map.put("a", "bc");
        String v = ConcurrentUtil.initMap(map, "a", "de");
        assertEquals("bc", v);
    }

    /**
     * Test method for {@link org.apache.niolex.commons.concurrent.ConcurrentUtil#initMap(java.util.concurrent.ConcurrentHashMap, java.lang.Object, java.lang.Object)}.
     */
    @Test
    public void testInitMap2() {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
        map.put("a", "bc");
        String v = ConcurrentUtil.initMap(map, "ab", "de");
        assertEquals("de", v);
    }

    private static class Wait implements Runnable {
        ExecutorService pool;

        /**
         * Constructor
         * @param pool
         */
        public Wait(ExecutorService pool) {
            super();
            this.pool = pool;
        }

        @Override
        public void run() {
            shutdownAndAwaitTermination(pool);
        }
    }

    private static class Wait2 implements Runnable {
        ExecutorService pool;

        /**
         * Constructor
         * @param pool
         */
        public Wait2(ExecutorService pool) {
            super();
            this.pool = pool;
        }

        @Override
        public void run() {
            shutdownAndAwaitTermination(pool, 10, TimeUnit.SECONDS);
        }
    }

    @Test
    public void testShutdownAndAwaitTermination1() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(1, new SimpleThreadFactory("ConcurrentUtilTest#1"));
        final AtomicInteger ai = new AtomicInteger();

        pool.submit(new Runnable(){
            @Override
            public void run() {
                ThreadUtil.sleep(100);
                ai.incrementAndGet();
            }});

        Thread thread = new Thread(new Wait(pool));
        thread.start();

        ThreadUtil.sleep(2);

        for (int i = 0; i < 5; ++i) {
            thread.interrupt();
            ThreadUtil.sleep(1);
        }

        thread.join();
        assertEquals(1, ai.get());
    }

    @Test
    public void testShutdownAndAwaitTermination2() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(1, new SimpleThreadFactory("ConcurrentUtilTest#2"));
        final AtomicInteger ai = new AtomicInteger();

        pool.submit(new Runnable(){
            @Override
            public void run() {
                ThreadUtil.sleep(100);
                ai.incrementAndGet();
            }});

        Thread thread = new Thread(new Wait2(pool));
        thread.start();

        ThreadUtil.sleep(2);

        for (int i = 0; i < 5; ++i) {
            thread.interrupt();
            ThreadUtil.sleep(1);
        }

        thread.join();
        assertEquals(1, ai.get());
    }

    @Test
    public void testShutdownAndAwaitTermination3() throws Exception {
        ExecutorService pool = mock(ExecutorService.class);
        when(pool.awaitTermination(anyLong(), any(TimeUnit.class))).thenReturn(false, false, true);

        shutdownAndAwaitTermination(pool);
    }

    @Test
    public void testShutdownAndAwaitTermination4() throws Exception {
        ExecutorService pool = mock(ExecutorService.class);
        when(pool.awaitTermination(anyLong(), any(TimeUnit.class))).thenAnswer(new Answer<Boolean>(){

            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                ThreadUtil.sleepAtLeast(1);
                return Boolean.FALSE;
            }});

        shutdownAndAwaitTermination(pool, 50, TimeUnit.MILLISECONDS);
    }

}
