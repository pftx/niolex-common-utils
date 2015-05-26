/**
 * InvokableExecutorServiceTest.java
 *
 * Copyright 2015 the original author or authors.
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
import static org.mockito.Mockito.mock;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.niolex.commons.reflect.MethodUtil;
import org.apache.niolex.commons.util.ThrowableUtil;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2015-5-26
 */
public class InvokableExecutorServiceTest {

    private final AtomicInteger ai = new AtomicInteger();

    public void increment(int x) {
        ai.addAndGet(x);
        ThreadUtil.sleepAtLeast(x);
    }

    public int dead(long x) {
        if (x == 1)
            throw new RuntimeException("test only");
        return 2;
    }

    /**
     * Test method for {@link org.apache.niolex.commons.concurrent.InvokableExecutorService#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])}.
     */
    @Test
    public void testInvoker1() throws Exception {
        InvokableExecutorService tar = new InvokableExecutorService(Executors.newFixedThreadPool(1, new SimpleThreadFactory("InvokableExecutorServiceTest#2")));
        Method method = MethodUtil.getMethod(InvokableExecutorServiceTest.class, "dead", long.class);

        Future<String> future = tar.invoke(this, method, 1);
        try {
            String s = future.get();
            assertNotNull(s);
        } catch (Exception e) {
            Throwable cause = ThrowableUtil.getRootCause(e);
            assertEquals("test only", cause.getMessage());
        }
    }

    /**
     * Test method for {@link org.apache.niolex.commons.concurrent.InvokableExecutorService#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])}.
     */
    @Test
    public void testInvoker2() throws Exception {
        InvokableExecutorService tar = new InvokableExecutorService(Executors.newFixedThreadPool(1, new SimpleThreadFactory("InvokableExecutorServiceTest#2")));
        Method method = MethodUtil.getMethod(InvokableExecutorServiceTest.class, "dead", long.class);

        Future<String> future = tar.invoke(this, method, 2);
        try {
            String s = future.get();
            assertNotNull(s);
        } catch (Exception e) {
            Throwable cause = ThrowableUtil.getRootCause(e);
            assertEquals("java.lang.Integer cannot be cast to java.lang.String", cause.getMessage());
        }
    }

    /**
     * Test method for {@link org.apache.niolex.commons.concurrent.InvokableExecutorService#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])}.
     */
    @Test
    public void testInvoke() throws Exception {
        InvokableExecutorService tar = new InvokableExecutorService(Executors.newFixedThreadPool(5, new SimpleThreadFactory("InvokableExecutorServiceTest#1")));

        Method method = MethodUtil.getMethod(InvokableExecutorServiceTest.class, "increment", int.class);
        long in = System.currentTimeMillis();
        for (int i = 0; i < 20; ++ i) {
            tar.invoke(this, method, 1);
        }

        for (int i = 0; i < 5; ++ i) {
            tar.invoke(this, method, 5);
        }

        long out = System.currentTimeMillis();

        System.out.println("Submit job time - " + (out - in));
        assertTrue((out - in) < 15);

        ConcurrentUtil.shutdownAndAwaitTermination(tar);
        assertEquals(45, ai.get());
    }

    /**
     * Test method for {@link org.apache.niolex.commons.concurrent.InvokableExecutorService#execute(java.lang.Runnable)}.
     */
    @Test
    public void testExecute() throws Exception {
        // Cover all the delegated methods here.
        ExecutorService mk = mock(ExecutorService.class);
        InvokableExecutorService tar = new InvokableExecutorService(mk);

        tar.awaitTermination(0, null);
        tar.execute(null);
        tar.invokeAll(null);
        tar.invokeAll(null, 0, null);
        tar.invokeAny(null);
        tar.invokeAny(null, 0, null);
        tar.isShutdown();
        tar.isTerminated();
        tar.shutdown();
        tar.shutdownNow();

        tar.submit(new Runnable(){

            @Override
            public void run() {

            }});
        tar.submit(new Callable<String>(){

            @Override
            public String call() throws Exception {
                return "Auto-generated method stub";
            }});
        tar.submit(null, mk);
    }

}
