package org.apache.niolex.lock;


import static org.junit.Assert.*;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.niolex.commons.bean.One;
import org.apache.niolex.commons.test.MockUtil;
import org.apache.niolex.commons.util.Runner;
import org.apache.niolex.notify.AppTest;
import org.apache.niolex.zookeeper.core.ZKConnector;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ZKLockTest {

    private static String BS = "/lock/zkc/tmp-" + MockUtil.randInt(100, 999);
    private static ZKConnector ZKC;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        ZKC = new ZKConnector(AppTest.URL, 10000);
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        ZKC.close();
    }

    @Test
    public void testWholeLock1() throws Exception {
        ZKLock lock = new ZKLock(AppTest.URL, 6000, BS);
        lock.lock();
        lock.unlock();
        lock.close();
        System.out.println("testWholeLock1 done.");
    }

    @Test
    public void testWholeLock2() throws Exception {
        ZKLock lock1 = new ZKLock(ZKC, BS);
        ZKLock lock2 = new ZKLock(ZKC, BS);

        assertTrue(lock1.tryLock());
        assertFalse(lock2.tryLock());

        lock1.unlock();
        assertTrue(lock2.tryLock());
        assertFalse(lock1.tryLock());

        lock2.unlock();
        System.out.println("testWholeLock2 done.");
    }

    @Test
    public void testWholeLock3() throws Exception {
        ZKLock lock1 = new ZKLock(ZKC, BS);
        ZKLock lock2 = new ZKLock(ZKC, BS + "/");
        ZKLock lock3 = new ZKLock(ZKC, BS);

        lock1.lock();
        One<Thread> threadVal = One.create(null);

        Future<Object> fu = Runner.run(threadVal, lock2, "lockInterruptibly");
        assertFalse(lock3.tryLock());
        assertFalse(fu.isDone());

        lock1.unlock();

        assertFalse(lock3.tryLock(100, TimeUnit.MICROSECONDS));
        fu.get();
        assertFalse(lock3.tryLock());
        assertTrue(lock2.locked());
        lock2.unlock();
        assertFalse(lock2.locked());
        assertFalse(lock3.locked());

        assertTrue(lock3.tryLock(100, TimeUnit.MICROSECONDS));
        assertTrue(lock3.locked());

        lock3.unlock();
        assertFalse(lock3.locked());
        System.out.println("testWholeLock3 done.");
    }

    @Test
    public void testInitLock() throws Exception {
        System.out.println("not yet implemented");
    }

    @Test
    public void testWatchLock() throws Exception {
        System.out.println("not yet implemented");
    }

    @Test
    public void testRelease() throws Exception {
        System.out.println("not yet implemented");
    }

    @Test
    public void testZKLockStringIntString() throws Exception {
        System.out.println("not yet implemented");
    }

    @Test
    public void testZKLockZKConnectorString() throws Exception {
        System.out.println("not yet implemented");
    }

    @Test
    public void testAddAuthInfo() throws Exception {
        ZKLock lock1 = new ZKLock(ZKC, BS);
        lock1.addAuthInfo("abc", "lex");
    }

    @Test
    public void testCheckLockStatus() throws Exception {
        System.out.println("not yet implemented");
    }

    @Test
    public void testProcess() throws Exception {
        System.out.println("not yet implemented");
    }

}
