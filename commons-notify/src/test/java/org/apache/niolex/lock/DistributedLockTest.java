package org.apache.niolex.lock;


import static org.junit.Assert.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.niolex.commons.bean.One;
import org.apache.niolex.commons.concurrent.ThreadUtil;
import org.apache.niolex.commons.util.Runner;
import org.junit.Test;

public class DistributedLockTest extends DistributedLock {

    @Test
    public void testLock() throws Exception {
        lock();
        assertEquals(1, initLock);
        assertEquals(1, watchLock);
        assertEquals(0, release);

        lock();
        assertEquals(2, initLock);
        assertEquals(1, watchLock);
        assertEquals(0, release);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testLockException() throws Throwable {
        One<Thread> threadVal = One.create(null);
        exception = new IllegalArgumentException("test");
        Future<Object> fu = Runner.run(threadVal, this, "lock");

        int i = 10;
        while (i-- > 0) {
            ThreadUtil.sleep(20);
            threadVal.a.interrupt();
        }

        lockReady(exception);
        assertEquals(1, initLock);
        assertEquals(1, watchLock);
        assertEquals(0, release);

        try {
            fu.get();
        } catch (ExecutionException e) {
            throw e.getCause();
        }

        assertEquals(2, initLock);
        assertEquals(1, watchLock);
        assertEquals(0, release);
    }

    @Test
    public void testLockInterruptibly() throws Exception {
        lockInterruptibly();
        assertEquals(1, initLock);
        assertEquals(1, watchLock);
        assertEquals(0, release);

        lockInterruptibly();
        assertEquals(2, initLock);
        assertEquals(1, watchLock);
        assertEquals(0, release);
    }

    @Test(expected=InterruptedException.class)
    public void testLockInterruptiblyInterrupt() throws Throwable {
        One<Thread> threadVal = One.create(null);
        exception = new IllegalArgumentException("test");
        Future<Object> fu = Runner.run(threadVal, this, "lockInterruptibly");

        int i = 5;
        while (i-- > 0) {
            ThreadUtil.sleep(20);
            threadVal.a.interrupt();
        }

        assertEquals(1, initLock);
        assertEquals(1, watchLock);
        assertEquals(1, release);

        try {
            fu.get();
        } catch (ExecutionException e) {
            throw e.getCause();
        }

        assertEquals(2, initLock);
        assertEquals(1, watchLock);
        assertEquals(0, release);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testLockInterruptiblyException() throws Throwable {
        One<Thread> threadVal = One.create(null);
        exception = new IllegalArgumentException("test");
        Future<Object> fu = Runner.run(threadVal, this, "lockInterruptibly");

        ThreadUtil.sleep(100);

        lockReady(exception);
        assertEquals(1, initLock);
        assertEquals(1, watchLock);
        assertEquals(0, release);

        try {
            fu.get();
        } catch (ExecutionException e) {
            throw e.getCause();
        }

        assertEquals(2, initLock);
        assertEquals(1, watchLock);
        assertEquals(0, release);
    }

    @Test
    public void testLockInterruptiblyOK() throws Throwable {
        One<Thread> threadVal = One.create(null);
        exception = new IllegalArgumentException("test");
        Future<Object> fu = Runner.run(threadVal, this, "lockInterruptibly");

        ThreadUtil.sleep(100);

        lockReady(null);
        assertEquals(1, initLock);
        assertEquals(1, watchLock);
        assertEquals(0, release);

        try {
            fu.get();
        } catch (ExecutionException e) {
            throw e.getCause();
        }

        assertEquals(1, initLock);
        assertEquals(1, watchLock);
        assertEquals(0, release);
    }

    @Test
    public void testTryLock() throws Exception {
        assertFalse(tryLock());
        assertEquals(1, initLock);
        assertEquals(0, watchLock);
        assertEquals(1, release);
        assertTrue(tryLock());
        assertEquals(2, initLock);
        assertEquals(0, watchLock);
        assertEquals(1, release);
    }

    @Test
    public void testTryLockLongTimeUnit() throws Exception {
        assertTrue(tryLock(100, TimeUnit.MICROSECONDS));
        assertEquals(1, initLock);
        assertEquals(1, watchLock);
        assertEquals(0, release);

        assertTrue(tryLock(100, TimeUnit.MICROSECONDS));
        assertEquals(2, initLock);
        assertEquals(1, watchLock);
        assertEquals(0, release);
    }

    @Test(expected=InterruptedException.class)
    public void testTryLockLongTimeUnitInterrupt() throws Throwable {
        One<Thread> threadVal = One.create(null);
        exception = new IllegalArgumentException("test");
        Future<Object> fu = Runner.run(threadVal, this, "tryLock", 200, TimeUnit.MILLISECONDS);

        int i = 5;
        while (i-- > 0) {
            ThreadUtil.sleep(20);
            threadVal.a.interrupt();
        }

        assertEquals(1, initLock);
        assertEquals(1, watchLock);
        assertEquals(1, release);

        try {
            fu.get();
        } catch (ExecutionException e) {
            throw e.getCause();
        }

        assertEquals(2, initLock);
        assertEquals(1, watchLock);
        assertEquals(0, release);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testTryLockLongTimeUnitException() throws Throwable {
        One<Thread> threadVal = One.create(null);
        exception = new IllegalArgumentException("test");
        Future<Object> fu = Runner.run(threadVal, this, "tryLock", 200, TimeUnit.MILLISECONDS);

        ThreadUtil.sleep(100);

        lockReady(exception);
        assertEquals(1, initLock);
        assertEquals(1, watchLock);
        assertEquals(0, release);

        try {
            fu.get();
        } catch (ExecutionException e) {
            throw e.getCause();
        }

        assertEquals(2, initLock);
        assertEquals(1, watchLock);
        assertEquals(0, release);
    }

    @Test
    public void testTryLockLongTimeUnitOK() throws Throwable {
        One<Thread> threadVal = One.create(null);
        exception = new IllegalArgumentException("test");
        Future<Object> fu = Runner.run(threadVal, this, "tryLock", 200, TimeUnit.MILLISECONDS);

        ThreadUtil.sleep(100);

        lockReady(null);
        assertEquals(1, initLock);
        assertEquals(1, watchLock);
        assertEquals(0, release);

        try {
            fu.get();
        } catch (ExecutionException e) {
            throw e.getCause();
        }

        assertEquals(1, initLock);
        assertEquals(1, watchLock);
        assertEquals(0, release);
    }

    @Test
    public void testTryLockLongTimeUnitTimeout() throws Throwable {
        One<Thread> threadVal = One.create(null);
        exception = new IllegalArgumentException("test");
        Future<Boolean> fu = Runner.run(threadVal, this, "tryLock", 50, TimeUnit.MILLISECONDS);

        ThreadUtil.sleep(100);

        lockReady(null);
        assertEquals(1, initLock);
        assertEquals(1, watchLock);
        assertEquals(1, release);

        try {
            Boolean r = fu.get();
            assertFalse(r);
        } catch (ExecutionException e) {
            throw e.getCause();
        }

        assertEquals(1, initLock);
        assertEquals(1, watchLock);
        assertEquals(1, release);
    }


    @Test(expected=UnsupportedOperationException.class)
    public void testNewCondition() throws Exception {
        newCondition();
    }

    @Test
    public void testUnlock() throws Exception {
        unlock();
        assertEquals(1, release);
        unlock();
        assertEquals(2, release);
    }

    @Test
    public void testInitWait() throws Exception {
        initWait();
        lockReady(null);
    }

    @Test
    public void testLockReady() throws Exception {
        lockReady(null);
    }

    private int initLock = 0;
    private int watchLock = 0;
    private int release = 0;
    private RuntimeException exception = null;

    /**
     * This is the override of super method.
     * @see org.apache.niolex.lock.DistributedLock#initLock()
     */
    @Override
    protected boolean initLock() {
        if (++initLock % 2 == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.lock.DistributedLock#watchLock()
     */
    @Override
    protected void watchLock() {
        ++watchLock;
        if (exception == null)
            lockReady(exception);
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.lock.DistributedLock#release()
     */
    @Override
    protected void release() {
        ++release;
    }

}
