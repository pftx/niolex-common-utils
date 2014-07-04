package org.apache.niolex.commons.concurrent;


import static org.junit.Assert.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.niolex.commons.bean.One;
import org.apache.niolex.commons.util.Runner;
import org.junit.Test;

public class SimpleFutureTest {

    SimpleFuture<String> future = new SimpleFuture<String>();

    @Test
    public void testCancel() throws Exception {
        assertFalse(future.cancel(true));
        assertFalse(future.cancel(false));
    }

    @Test
    public void testIsCancelled() throws Exception {
        assertFalse(future.isCancelled());
        assertFalse(future.cancel(true));
        assertFalse(future.isCancelled());
    }

    @Test
    public void testIsDone() throws Exception {
        assertFalse(future.isDone());
    }

    @Test
    public void testSetDone() throws Exception {
        assertFalse(future.isDone());
        future.setDone("Good.");
        assertTrue(future.isDone());
    }

    @Test
    public void testSetAbort() throws Exception {
        assertFalse(future.isDone());
        future.setAbort(new TimeoutException());
        assertTrue(future.isDone());
    }

    @Test
    public void testGet() throws Exception {
        Thread t = Runner.run(future, "setDone", "Nice Shirt!");
        String v = future.get();
        assertEquals("Nice Shirt!", v);
        assertFalse(t.isAlive());
    }

    @Test
    public void testGetLongTimeUnit() throws Exception {
        Runner.run(future, "setDone", "Nice Shirt!");
        String v = future.get(500, TimeUnit.MILLISECONDS);
        assertEquals("Nice Shirt!", v);
    }

    @Test(expected=InterruptedException.class)
    public void testGetInterruptedException() throws Throwable {
        One<Thread> one = new One<Thread>();
        Future<Object> fu = Runner.run(one, future, "get");
        one.a.interrupt();
        try {
            fu.get();
        } catch (ExecutionException e) {
            throw e.getCause();
        }
    }

    @Test(expected=InterruptedException.class)
    public void testGetLongTimeUnitInterruptedException() throws Throwable {
        One<Thread> one = new One<Thread>();
        Future<Object> fu = Runner.run(one, future, "get", 10, TimeUnit.MILLISECONDS);
        one.a.interrupt();
        try {
            fu.get(10, TimeUnit.MILLISECONDS);
        } catch (ExecutionException e) {
            throw e.getCause();
        }
    }

    @Test(expected=TimeoutException.class)
    public void testGetLongTimeUnitTimeoutException() throws Exception {
        String v = future.get(5, TimeUnit.MILLISECONDS);
        assertEquals("Nice Shirt!", v);
    }

}
