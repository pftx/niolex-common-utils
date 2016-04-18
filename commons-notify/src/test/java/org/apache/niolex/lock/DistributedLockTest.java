package org.apache.niolex.lock;


import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;
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
        assertEquals(2, isLockReady);
        assertEquals(1, watchLock);
        assertEquals(0, release);

        lock();
        assertEquals(2, initLock);
        assertEquals(4, isLockReady);
        assertEquals(2, watchLock);
        assertEquals(0, release);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testLockException() throws Throwable {
        One<Thread> threadVal = One.create(null);
        latch = new CountDownLatch(1);
        exception = new IllegalArgumentException("test");
        Future<Object> fu = Runner.run(threadVal, this, "lock");

        int i = 5;
        while (i-- > 0) {
            ThreadUtil.sleep(20);
            threadVal.a.interrupt();
        }

        latch.countDown();
        assertEquals(1, initLock);
        assertTrue(1 < watchLock);

        try {
            fu.get();
        } catch (ExecutionException e) {
            assertEquals(1, release);
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
        assertEquals(2, isLockReady);
        assertEquals(1, watchLock);
        assertEquals(0, release);

        lockInterruptibly();
        assertEquals(2, initLock);
        assertEquals(4, isLockReady);
        assertEquals(2, watchLock);
        assertEquals(0, release);
    }

    @Test(expected=InterruptedException.class)
    public void testLockInterruptiblyInterrupt() throws Throwable {
        One<Thread> threadVal = One.create(null);
        latch = new CountDownLatch(1);
        exception = new IllegalArgumentException("test");
        Future<Object> fu = Runner.run(threadVal, this, "lockInterruptibly");

        int i = 5;
        while (i-- > 0) {
            ThreadUtil.sleep(20);
            threadVal.a.interrupt();
        }

        assertEquals(1, initLock);
        assertEquals(1, isLockReady);
        assertEquals(1, watchLock);
        assertEquals(1, release);

        try {
            fu.get();
        } catch (ExecutionException e) {
            assertEquals(1, release);
            throw e.getCause();
        }

        assertEquals(2, initLock);
        assertEquals(1, watchLock);
        assertEquals(0, release);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testLockInterruptiblyException() throws Throwable {
        One<Thread> threadVal = One.create(null);
        latch = new CountDownLatch(1);
        exception = new IllegalArgumentException("test");
        Future<Object> fu = Runner.run(threadVal, this, "lockInterruptibly");

        ThreadUtil.sleep(100);

        latch.countDown();

        assertEquals(1, initLock);
        assertEquals(1, watchLock);
        assertEquals(1, isLockReady);

        try {
            fu.get();
        } catch (ExecutionException e) {
            assertEquals(1, release);
            throw e.getCause();
        }

        assertEquals(2, initLock);
        assertEquals(1, watchLock);
        assertEquals(0, release);
    }

    @Test
    public void testLockInterruptiblyOK() throws Throwable {
        One<Thread> threadVal = One.create(null);
        latch = new CountDownLatch(1);
        Future<Object> fu = Runner.run(threadVal, this, "lockInterruptibly");

        ThreadUtil.sleep(100);

        latch.countDown();
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
        assertEquals(1, isLockReady);
        assertEquals(0, watchLock);
        assertEquals(1, release);
        assertTrue(tryLock());
        assertEquals(2, initLock);
        assertEquals(2, isLockReady);
        assertEquals(0, watchLock);
        assertEquals(1, release);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testTryLockException() throws Throwable {
        One<Thread> threadVal = One.create(null);
        exception2 = new IllegalArgumentException("test");
        Future<Object> fu = Runner.run(threadVal, this, "tryLock");

        ThreadUtil.sleep(100);

        assertEquals(1, initLock);
        assertEquals(0, watchLock);

        try {
            fu.get();
        } catch (ExecutionException e) {
            assertEquals(1, release);
            assertEquals(0, isLockReady);
            assertEquals(1, initLock);
            assertEquals(0, watchLock);

            throw e.getCause();
        }

        assertEquals(1000, release);
    }

    @Test
    public void testTryLockLongTimeUnit() throws Exception {
        assertTrue(tryLock(100, TimeUnit.MICROSECONDS));
        assertEquals(1, initLock);
        assertEquals(2, isLockReady);
        assertEquals(1, watchLock);
        assertEquals(0, release);

        assertTrue(tryLock(100, TimeUnit.MICROSECONDS));
        assertEquals(2, initLock);
        assertEquals(4, isLockReady);
        assertEquals(2, watchLock);
        assertEquals(0, release);
    }

    @Test
    public void testTryLockLongTimeUnitOKD() throws Exception {
        isLockReady = 1;
        assertTrue(tryLock(100, TimeUnit.MICROSECONDS));
        assertEquals(1, initLock);
        assertEquals(2, isLockReady);
        assertEquals(0, watchLock);
        assertEquals(0, release);

        assertTrue(tryLock(100, TimeUnit.MICROSECONDS));
        assertEquals(2, initLock);
        assertEquals(4, isLockReady);
        assertEquals(1, watchLock);
        assertEquals(0, release);
    }

    @Test(expected=InterruptedException.class)
    public void testTryLockLongTimeUnitInterrupt() throws Throwable {
        One<Thread> threadVal = One.create(null);
        latch = new CountDownLatch(1);
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

        latch = new CountDownLatch(1);
        exception = new IllegalArgumentException("test");
        Future<Object> fu = Runner.run(threadVal, this, "tryLock", 200, TimeUnit.MILLISECONDS);

        ThreadUtil.sleep(100);
        latch.countDown();

        assertEquals(1, initLock);
        assertEquals(1, watchLock);

        try {
            fu.get();
        } catch (ExecutionException e) {
            assertEquals(1, release);
            throw e.getCause();
        }

        assertEquals(2, initLock);
        assertEquals(1, watchLock);
        assertEquals(0, release);
    }

    @Test
    public void testTryLockLongTimeUnitOK() throws Throwable {
        One<Thread> threadVal = One.create(null);
        latch = new CountDownLatch(1);
        Future<Object> fu = Runner.run(threadVal, this, "tryLock", 200, TimeUnit.MILLISECONDS);

        ThreadUtil.sleep(100);

        latch.countDown();

        try {
            fu.get();
            assertEquals(1, initLock);
            assertEquals(1, watchLock);
            assertEquals(0, release);
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
        latch = new CountDownLatch(1);
        Future<Boolean> fu = Runner.run(threadVal, this, "tryLock", 50, TimeUnit.MILLISECONDS);

        ThreadUtil.sleep(100);

        latch.countDown();

        try {
            Boolean r = fu.get();
            assertFalse(r);
            assertEquals(1, initLock);
            assertEquals(2, isLockReady);
            assertEquals(1, watchLock);
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

    private int initLock = 0;
    private int isLockReady = 0;
    private int watchLock = 0;
    private int release = 0;
    private RuntimeException exception = null;
    private RuntimeException exception2 = null;
    private CountDownLatch latch = null;
    private boolean timeout2 = false;

    /**
     * This is the override of super method.
     * @see org.apache.niolex.lock.DistributedLock#initLock()
     */
    @Override
    protected void initLock() {
        ++initLock;
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.lock.DistributedLock#releaseLock()
     */
    @Override
    protected void releaseLock() {
        ++release;
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.lock.DistributedLock#isLockReady()
     */
    @Override
    protected boolean isLockReady() {
        if (exception2 != null)
            throw exception2;

        if (++isLockReady % 2 == 0 && exception == null && !timeout2) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This is the override of super method.
     * @throws InterruptedException
     * @see org.apache.niolex.lock.DistributedLock#watchLock()
     */
    @Override
    protected void watchLock() throws InterruptedException {
        ++watchLock;
        if (latch != null)
            latch.await();

        if (exception != null)
            throw exception;
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.lock.DistributedLock#watchLock(long, java.util.concurrent.TimeUnit)
     */
    @Override
    protected boolean watchLock(long timeout, TimeUnit unit) throws InterruptedException {
        ++watchLock;

        if (latch != null)
            timeout2 = !latch.await(timeout, unit);

        if (exception != null)
            throw exception;

        if (watchLock % 2 == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.lock.DistributedLock#locked()
     */
    @Override
    public boolean locked() {
        return false;
    }

}
