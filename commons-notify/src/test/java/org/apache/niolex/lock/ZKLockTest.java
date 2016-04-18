package org.apache.niolex.lock;


import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.niolex.commons.bean.One;
import org.apache.niolex.commons.concurrent.ThreadUtil;
import org.apache.niolex.commons.reflect.FieldUtil;
import org.apache.niolex.commons.test.MockUtil;
import org.apache.niolex.commons.util.Runner;
import org.apache.niolex.notify.AppTest;
import org.apache.niolex.zookeeper.core.ZKConnector;
import org.apache.niolex.zookeeper.core.ZKException;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
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
        List<String> c = ZKC.getChildren(BS);
        assertEquals(0, c.size());
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
        // Unlock again.
        lock2.unlock();
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
        ZKLock lock1 = new ZKLock(ZKC, BS);
        ZKLock lock2 = new ZKLock(ZKC, BS + "/");
        ZKLock lock3 = new ZKLock(ZKC, BS);
        ZKLock lock4 = new ZKLock(ZKC, BS + "/");

        lock1.lock();

        One<Thread> th1 = One.create(null);
        Future<Object> fu1 = Runner.run(th1, lock2, "lockInterruptibly");
        ThreadUtil.sleep(10);

        One<Thread> th2 = One.create(null);
        Future<Object> fu2 = Runner.run(th2, lock3, "lockInterruptibly");
        ThreadUtil.sleep(50);

        lock1.unlock();

        fu1.get();
        assertFalse(lock1.locked());
        assertTrue(lock2.locked());
        assertFalse(lock3.locked());
        assertFalse(lock4.tryLock());

        FieldUtil.setValue(lock1, "selfPath", "/a/b/c");
        try {
            lock1.isLockReady();
            assertTrue(false);
        } catch (IllegalStateException e) {
            assertEquals("Invalid zookeeper data, current path not found.", e.getMessage());
        }

        lock2.unlock();

        fu2.get();

        assertFalse(lock1.locked());
        assertFalse(lock2.locked());
        assertTrue(lock3.locked());

        lock3.unlock();
    }

    @Test
    public void testWatchLock() throws Exception {
        ZKLock lock1 = new ZKLock(ZKC, BS);
        FieldUtil.setValue(lock1, "selfPath", "/a/b/c");
        FieldUtil.setValue(lock1, "watchPath", "/a/b/b");
        lock1.watchLock();
    }


    @Test
    public void testIsLockReadyLocked() throws Exception {
        ZKLock lock1 = new ZKLock(ZKC, BS);
        FieldUtil.setValue(lock1, "selfPath", "/a/b/c");
        FieldUtil.setValue(lock1, "locked", true);
        assertTrue(lock1.isLockReady());
    }

    @Test(expected=IllegalStateException.class)
    public void testIsLockReadyNoWatchPath() throws Exception {
        ZKLock lock1 = new ZKLock(ZKC, BS);
        FieldUtil.setValue(lock1, "selfPath", "/a/b/c");
        lock1.isLockReady();
    }

    @Test(expected=IllegalStateException.class)
    public void testIsLockReadyIll() throws Exception {
        ZKLock lock1 = new ZKLock(ZKC, BS);
        FieldUtil.setValue(lock1, "watchPath", "/a/b/b");
        lock1.isLockReady();
    }

    @Test
    public void testReleaseLock() throws Exception {
        ZKLock lock1 = new ZKLock(ZKC, BS);
        lock1.initLock();
        assertTrue(lock1.isLockReady());
        FieldUtil.setValue(lock1, "watchPath", "/a/b/b");
        try {
        lock1.watchLock();
        } finally {
        lock1.unlock();
        }
    }

    @Test
    public void testZKLockStringIntString() throws Exception {
        ZKLock lock1 = new ZKLock(ZKC, BS);
        lock1.lock();
        FieldUtil.setValue(lock1, "watchPath", "/a/b/b");
        lock1.watchLock();

        ZKLock lock2 = new ZKLock(ZKC, BS);
        lock2.initLock();
        assertFalse(lock2.isLockReady());
        try {
        FieldUtil.setValue(lock2, "watchPath", "/a/b/b");
        lock2.watchLock();
        } finally {
            lock1.unlock();
            lock2.unlock();
        }
    }

    @Test(expected=ZKException.class)
    public void testZKLockZKConnectorString() throws Exception {
        ZooKeeper zk = mock(ZooKeeper.class);
        ZKConnector zkc = mock(ZKConnector.class);
        when(zkc.zooKeeper()).thenReturn(zk);

        KeeperException ke = KeeperException.create(KeeperException.Code.APIERROR);
        when(zk.exists(anyString(), any(Watcher.class))).thenThrow(ke);

        ZKLock lock1 = new ZKLock(zkc, BS);

        FieldUtil.setValue(lock1, "watchPath", "/a/b/c");
        lock1.watchLock();
    }

    @Test(expected=IllegalStateException.class)
    public void testAddAuthInfo() throws Exception {
        ZKLock lock1 = new ZKLock(ZKC, BS);
        lock1.addAuthInfo("abc", "lex");
        lock1.watchLock();
    }

    @Test(expected=IllegalStateException.class)
        public void testIsLockReady2() throws Exception {
            ZKLock lock1 = new ZKLock(ZKC, BS);
            ZKLock lock2 = new ZKLock(ZKC, BS);
            lock2.lock();
            try {
                FieldUtil.setValue(lock1, "selfPath", "/a/b/c");
                lock1.isLockReady();
            } finally {
                lock2.unlock();
            }
        }

    @Test
    public void testProcess() throws Exception {
        ZKLock lock1 = new ZKLock(ZKC, BS);
        CountDownLatch latch = new CountDownLatch(1);
        Watcher w = lock1.new ExistsWather(latch);
        lock1.lock();

        w.process(new WatchedEvent(Watcher.Event.EventType.None, Watcher.Event.KeeperState.Disconnected, ""));
        w.process(new WatchedEvent(Watcher.Event.EventType.NodeCreated, Watcher.Event.KeeperState.Disconnected, ""));

        lock1.unlock();
    }

    @Test
    public void testProcess2() throws Exception {
        ZKConnector zkc = mock(ZKConnector.class);
        when(zkc.connected()).thenReturn(false, false, true);

        ZKLock lock1 = new ZKLock(zkc, BS);

        FieldUtil.setValue(lock1, "watchPath", "/a/b/c");
        CountDownLatch latch = new CountDownLatch(1);
        Watcher w = lock1.new ExistsWather(latch);
        w.process(new WatchedEvent(Watcher.Event.EventType.None, Watcher.Event.KeeperState.Disconnected, ""));
    }

    @Test(expected=NullPointerException.class)
    public void testProcess3() throws Exception {
        ZKConnector zkc = mock(ZKConnector.class);
        when(zkc.connected()).thenReturn(false, false, true);
        doThrow(new NullPointerException()).when(zkc).waitForConnectedTillDeath();

        ZKLock lock1 = new ZKLock(zkc, BS);
        CountDownLatch latch = new CountDownLatch(1);
        Watcher w = lock1.new ExistsWather(latch);

        w.process(new WatchedEvent(Watcher.Event.EventType.None, Watcher.Event.KeeperState.Disconnected, ""));
    }

    @Test(expected=IllegalStateException.class)
    public void testWatchLockLongTimeUnitEx() throws Exception {
        ZKLock lock1 = new ZKLock(ZKC, BS);
        lock1.watchLock(100, TimeUnit.MICROSECONDS);
    }

    @Test
    public void testWatchLockLongTimeUnit() throws Exception {
        ZKLock lock1 = new ZKLock(ZKC, BS);
        FieldUtil.setValue(lock1, "watchPath", "/a/b/c");
        lock1.watchLock(100, TimeUnit.MILLISECONDS);
    }

    @Test(expected=ZKException.class)
    public void testWatchLockLongTimeUnitZKEX() throws Exception {
        ZooKeeper zk = mock(ZooKeeper.class);
        ZKConnector zkc = mock(ZKConnector.class);
        when(zkc.zooKeeper()).thenReturn(zk);
        when(zkc.connected()).thenReturn(false, false, true);

        KeeperException ke = KeeperException.create(KeeperException.Code.APIERROR);
        when(zk.exists(anyString(), any(Watcher.class))).thenThrow(ke);

        ZKLock lock1 = new ZKLock(zkc, BS);

        FieldUtil.setValue(lock1, "watchPath", "/a/b/c");
        lock1.watchLock(100, TimeUnit.MILLISECONDS);
    }

}
