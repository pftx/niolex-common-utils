package org.apache.niolex.lock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import org.apache.niolex.zookeeper.core.TempNodeAutoCreator;
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
    private static TempNodeAutoCreator ZKC;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        ZKC = new TempNodeAutoCreator(AppTest.URL, 10000);
        String bb = "/lock/zkc";

        if (ZKC.exists(bb))
            ZKC.deleteTree(bb);
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
        ZKLock lock1 = new ZKLock(AppTest.URL, 6000, BS);
        ZKLock lock2 = new ZKLock(AppTest.URL, 6000, BS);

        assertTrue(lock1.tryLock());
        assertFalse(lock2.tryLock());

        lock1.unlock();
        assertTrue(lock2.tryLock());
        assertFalse(lock1.tryLock());

        lock2.unlock();
        System.out.println("testWholeLock2 done.");
        // Unlock again.
        lock2.unlock();
        lock1.close();
        lock2.close();
    }

    @Test
    public void testWholeLock3() throws Exception {
        ZKLock lock1 = new ZKLock(AppTest.URL, 6000, BS);
        ZKLock lock2 = new ZKLock(AppTest.URL, 6000, BS + "/");
        ZKLock lock3 = new ZKLock(AppTest.URL, 6000, BS);

        lock1.lock();
        One<Thread> threadVal = One.create(null);

        Future<Object> fu = Runner.run(threadVal, lock2, "lockInterruptibly");
        ThreadUtil.sleepAtLeast(50);
        
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
        lock1.close();
        lock2.close();
        lock3.close();
    }
    
    @Test
    public void testAddAuthInfo() throws Exception {
        ZKLock lock1 = new ZKLock(AppTest.URL, 6000, BS);
        lock1.addAuthInfo("abc", "lex");
        lock1.close();
    }

    @Test(expected=IllegalStateException.class)
    public void testInitLockEx() throws Exception {
        ZKLock lock1 = new ZKLock(AppTest.URL, 6000, BS);
        lock1.lock();

        try {
            lock1.initLock();
        } finally {
            lock1.unlock();
            lock1.close();
        }
    }
    
    @Test
    public void testIsLockReadyWhole() throws Exception {
        ZKLock lock1 = new ZKLock(AppTest.URL, 6000, BS);
        ZKLock lock2 = new ZKLock(AppTest.URL, 6000, BS + "/");
        ZKLock lock3 = new ZKLock(AppTest.URL, 6000, BS);
        ZKLock lock4 = new ZKLock(AppTest.URL, 6000, BS + "/");

        lock1.lock();

        One<Thread> th1 = One.create(null);
        Future<Object> fu1 = Runner.run(th1, lock2, "lockInterruptibly");
        ThreadUtil.sleep(50);

        One<Thread> th2 = One.create(null);
        Future<Object> fu2 = Runner.run(th2, lock3, "lockInterruptibly");
        ThreadUtil.sleep(50);

        lock1.unlock();

        fu1.get();
        assertFalse(lock1.locked());
        assertTrue(lock2.locked());
        assertFalse(lock3.locked());
        assertFalse(lock4.tryLock());

        TempNodeAutoCreator autoCreator = FieldUtil.getValue(lock1, "autoCreator");
        FieldUtil.setValue(autoCreator, "selfPath", "/a/b/c");
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
        
        lock1.close();
        lock2.close();
        lock3.close();
        lock4.close();
    }
    
    @Test(expected=IllegalStateException.class)
    public void testIsLockReadyDirectly() throws Exception {
        ZKLock lock1 = new ZKLock(AppTest.URL, 6000, BS);
        try {
            lock1.isLockReady();
        } finally {
            lock1.close();
        }
    }
    
    @Test(expected=IllegalStateException.class)
    public void testIsLockReadyNoWatchPath() throws Exception {
        ZKLock lock1 = new ZKLock(AppTest.URL, 6000, BS);
        TempNodeAutoCreator autoCreator = FieldUtil.getValue(lock1, "autoCreator");
        FieldUtil.setValue(autoCreator, "selfPath", "/a/b/c");
        try {
            lock1.isLockReady();
        } catch (IllegalStateException e) {
            assertEquals("Invalid zookeeper data.", e.getMessage());
            throw e;
        } finally {
            lock1.close();
        }
    }

    @Test(expected=IllegalStateException.class)
    public void testIsLockReadyIll() throws Exception {
        ZKLock lock1 = new ZKLock(AppTest.URL, 6000, BS);
        lock1.initLock();
        TempNodeAutoCreator autoCreator = FieldUtil.getValue(lock1, "autoCreator");
        FieldUtil.setValue(autoCreator, "selfPath", "/a/b/c");
        try {
            lock1.isLockReady();
        } catch (IllegalStateException e) {
            assertEquals("Invalid zookeeper data.", e.getMessage());
            throw e;
        } finally {
            lock1.close();
        }
    }
    
    @Test
    public void testIsLockReadyLocked() throws Exception {
        ZKLock lock1 = new ZKLock(AppTest.URL, 6000, BS);
        TempNodeAutoCreator autoCreator = FieldUtil.getValue(lock1, "autoCreator");
        FieldUtil.setValue(autoCreator, "selfPath", "/a/b/c");
        FieldUtil.setValue(lock1, "locked", true);
        assertTrue(lock1.isLockReady());
        lock1.close();
    }

    @Test(expected=IllegalStateException.class)
    public void testIsLockReady2Lock1Fake() throws Exception {
        ZKLock lock1 = new ZKLock(AppTest.URL, 6000, BS);
        ZKLock lock2 = new ZKLock(AppTest.URL, 6000, BS);
        ZKLock lock3 = new ZKLock(AppTest.URL, 6000, BS);
        lock2.lock();
        lock3.initLock();
        try {
            TempNodeAutoCreator autoCreator = FieldUtil.getValue(lock1, "autoCreator");
            FieldUtil.setValue(autoCreator, "selfPath", "/a/b/c");
            lock1.isLockReady();
        } catch (IllegalStateException e) {
            assertEquals("Invalid zookeeper data, current path not found.", e.getMessage());
            throw e;
        } finally {
            lock2.unlock();
            lock1.close();
            lock2.close();
            lock3.close();
        }
    }
    
    @Test(expected=IllegalStateException.class)
    public void testWatchLockDirectly() throws Exception {
        ZKLock lock1 = new ZKLock(AppTest.URL, 6000, BS);
        try {
            lock1.watchLock();
        } finally {
            lock1.close();
        }
    }
    
    @Test(expected=IllegalStateException.class)
    public void testWatchLockDirectlyWithTime() throws Exception {
        ZKLock lock1 = new ZKLock(AppTest.URL, 6000, BS);
        try {
            lock1.watchLock(10, TimeUnit.MILLISECONDS);
        } finally {
            lock1.close();
        }
    }
    
    @Test
    public void testWatchLockNotExist() throws Exception {
        ZKLock lock1 = new ZKLock(AppTest.URL, 6000, BS);
        TempNodeAutoCreator autoCreator = FieldUtil.getValue(lock1, "autoCreator");
        FieldUtil.setValue(autoCreator, "selfPath", "/a/b/c");
        FieldUtil.setValue(lock1, "watchPath", "/a/b/b");
        lock1.watchLock();
        lock1.close();
    }
    
    @Test
    public void testWatchLockReady() throws Exception {
        ZKLock lock1 = new ZKLock(AppTest.URL, 6000, BS);
        lock1.initLock();
        assertTrue(lock1.isLockReady());
        FieldUtil.setValue(lock1, "watchPath", "/a/b/b");
        try {
            lock1.watchLock(10, TimeUnit.MILLISECONDS);
        } finally {
            lock1.unlock();
            lock1.close();
        }
    }

    @Test
    public void testWatchLockWithTime() throws Exception {
        ZKLock lock1 = new ZKLock(AppTest.URL, 6000, BS);
        lock1.lock();
        FieldUtil.setValue(lock1, "watchPath", "/a/b/b");
        lock1.watchLock(10, TimeUnit.MILLISECONDS);

        ZKLock lock2 = new ZKLock(AppTest.URL, 6000, BS);
        lock2.initLock();
        assertFalse(lock2.isLockReady());
        
        try {
            FieldUtil.setValue(lock2, "watchPath", "/a/b/b");
            lock2.watchLock();
        } finally {
            lock1.unlock();
            lock2.unlock();
            lock1.close();
            lock2.close();
        }
    }

    @Test(expected=ZKException.class)
    public void testWatchLockEx() throws Exception {
        ZooKeeper zk = mock(ZooKeeper.class);
        TempNodeAutoCreator zkc = mock(TempNodeAutoCreator.class);
        when(zkc.zooKeeper()).thenReturn(zk);

        KeeperException ke = KeeperException.create(KeeperException.Code.APIERROR);
        when(zk.exists(anyString(), any(Watcher.class))).thenThrow(ke);

        ZKLock lock1 = new ZKLock(AppTest.URL, 6000, BS);

        FieldUtil.setValue(lock1, "autoCreator", zkc);
        FieldUtil.setValue(lock1, "watchPath", "/a/b/c");
        lock1.watchLock();
    }
    
    @Test(expected=ZKException.class)
    public void testWatchLockExWithTime() throws Exception {
        ZooKeeper zk = mock(ZooKeeper.class);
        TempNodeAutoCreator zkc = mock(TempNodeAutoCreator.class);
        when(zkc.zooKeeper()).thenReturn(zk);
        
        KeeperException ke = KeeperException.create(KeeperException.Code.APIERROR);
        when(zk.exists(anyString(), any(Watcher.class))).thenThrow(ke);
        
        ZKLock lock1 = new ZKLock(AppTest.URL, 6000, BS);
        
        FieldUtil.setValue(lock1, "autoCreator", zkc);
        FieldUtil.setValue(lock1, "watchPath", "/a/b/c");
        lock1.watchLock(10, TimeUnit.MILLISECONDS);
    }


    @Test
    public void testWatchLockTimeout() throws Exception {
        ZKLock lock1 = new ZKLock(AppTest.URL, 6000, BS);
        lock1.lock();
        ZKLock lock2 = new ZKLock(AppTest.URL, 6000, BS);
        lock2.initLock();
        assertFalse(lock2.isLockReady());
        
        try {
            assertFalse(lock2.watchLock(100, TimeUnit.MICROSECONDS));
        } finally {
            lock1.releaseLock();
            lock1.close();
            lock2.close();
        }
    }

    @Test
    public void testWatchLockLongTimeUnit() throws Exception {
        ZKLock lock1 = new ZKLock(AppTest.URL, 6000, BS);
        FieldUtil.setValue(lock1, "watchPath", "/a/b/c");
        assertTrue(lock1.watchLock(100, TimeUnit.MILLISECONDS));
        lock1.close();
    }

    @Test(expected = ZKException.class)
    public void testWatchLockLongTimeUnitZKEX() throws Exception {
        ZooKeeper zk = mock(ZooKeeper.class);
        TempNodeAutoCreator zkc = mock(TempNodeAutoCreator.class);
        when(zkc.zooKeeper()).thenReturn(zk);
        when(zkc.connected()).thenReturn(false, false, true);

        KeeperException ke = KeeperException.create(KeeperException.Code.APIERROR);
        when(zk.exists(anyString(), any(Watcher.class))).thenThrow(ke);

        ZKLock lock1 = new ZKLock(AppTest.URL, 6000, BS);
        FieldUtil.setValue(lock1, "autoCreator", zkc);
        
        try {
            FieldUtil.setValue(lock1, "watchPath", "/a/b/c");
            lock1.watchLock(100, TimeUnit.MILLISECONDS);
        } finally {
            lock1.close();
        }
    }
    
    @Test
    public void testReleaseLock() throws Exception {
        ZKLock lock1 = new ZKLock(AppTest.URL, 6000, BS);
        lock1.lock();
        ZKLock lock2 = new ZKLock(AppTest.URL, 6000, BS);
        lock2.initLock();
        assertFalse(lock2.isLockReady());
        
        lock1.releaseLock();
        lock2.releaseLock();
        lock1.releaseLock();
        lock2.releaseLock();
        
        lock1.close();
        lock2.close();
    }
    
    @Test
    public void testExistsWatherProcess() throws Exception {
        ZKLock lock1 = new ZKLock(AppTest.URL, 6000, BS);
        CountDownLatch latch = new CountDownLatch(1);
        Watcher w = lock1.new ExistsWather(latch);
        lock1.lock();

        w.process(new WatchedEvent(Watcher.Event.EventType.None, Watcher.Event.KeeperState.Disconnected, ""));
        w.process(new WatchedEvent(Watcher.Event.EventType.NodeCreated, Watcher.Event.KeeperState.Disconnected, ""));

        lock1.unlock();
        lock1.close();
    }

    @Test
    public void testExistsWatherProcess2() throws Exception {
        TempNodeAutoCreator zkc = mock(TempNodeAutoCreator.class);
        when(zkc.connected()).thenReturn(false, false, true);

        ZKLock lock1 = new ZKLock(AppTest.URL, 6000, BS);
        FieldUtil.setValue(lock1, "autoCreator", zkc);

        FieldUtil.setValue(lock1, "watchPath", "/a/b/c");
        CountDownLatch latch = new CountDownLatch(1);
        Watcher w = lock1.new ExistsWather(latch);
        w.process(new WatchedEvent(Watcher.Event.EventType.None, Watcher.Event.KeeperState.Disconnected, ""));
        lock1.close();
    }

    @Test(expected=NullPointerException.class)
    public void testExistsWatherProcess3() throws Exception {
        TempNodeAutoCreator zkc = mock(TempNodeAutoCreator.class);
        when(zkc.connected()).thenReturn(false, false, true);
        doThrow(new NullPointerException()).when(zkc).waitForConnectedTillDeath();

        ZKLock lock1 = new ZKLock(AppTest.URL, 6000, BS);
        FieldUtil.setValue(lock1, "autoCreator", zkc);
        
        CountDownLatch latch = new CountDownLatch(1);
        Watcher w = lock1.new ExistsWather(latch);

        w.process(new WatchedEvent(Watcher.Event.EventType.None, Watcher.Event.KeeperState.Disconnected, ""));
        lock1.close();
    }

}
