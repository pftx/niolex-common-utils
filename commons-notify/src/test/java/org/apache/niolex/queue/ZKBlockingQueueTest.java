package org.apache.niolex.queue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.niolex.commons.concurrent.ThreadUtil;
import org.apache.niolex.commons.reflect.FieldUtil;
import org.apache.niolex.commons.test.MockUtil;
import org.apache.niolex.commons.util.Runner;
import org.apache.niolex.notify.AppTest;
import org.apache.niolex.queue.ZKBlockingQueue.ChildrenChangeWather;
import org.apache.niolex.zookeeper.core.ZKConnector;
import org.apache.niolex.zookeeper.core.ZKException;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ZKBlockingQueueTest {
    
    private static String BS = "/queue/zkc/tmp-" + MockUtil.randInt(100, 999);
    private static ZKConnector ZKC;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        ZKC = new ZKConnector(AppTest.URL, 10000);

        if (ZKC.exists(BS))
            ZKC.deleteTree(BS);
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
    
    ZKBlockingQueue<String> queue1;
    ZKBlockingQueue<String> queue2;
    
    @Before
    public void setup() throws IOException {
        queue1 = new ZKBlockingQueue<String>(ZKC, BS);
        queue2 = new ZKBlockingQueue<String>(ZKC, BS + "/");
    }
    
    @After
    public void clean() throws IOException {
        queue1.poll();
        queue2.poll();
        queue1.close();
        queue2.close();
    }
    
    @Test
    public void testAddAuthInfo() throws Exception {
        queue1.addAuthInfo("lex", "paS$w0rd");
        queue2.addAuthInfo("lex", "paS$w0rd");
    }

    @Test
    public void testOffer() throws Exception {
        assertEquals(0, queue1.size());
        assertEquals(0, queue2.size());
        queue1.offer("nice-to-mm");
        assertEquals(1, queue1.size());
        assertEquals(1, queue2.size());
        String s = queue2.poll(100, TimeUnit.MILLISECONDS);
        assertEquals("nice-to-mm", s);
        assertNull(queue1.peek());
        assertNull(queue2.peek());
        assertNull(queue1.poll());
        assertNull(queue2.poll());
        assertEquals(0, queue1.size());
        assertEquals(0, queue2.size());
    }
    
    @Test
    public void testOfferNull() throws Exception {
        queue1.offer(null);
        assertEquals(1, queue2.size());
        assertNull(queue2.poll());
        assertEquals(0, queue1.size());
    }
    
    public static class Bad implements Serializable {
        private static final long serialVersionUID = -3027510281402896064L;
        
        private void writeObject(java.io.ObjectOutputStream out)
             throws IOException {
            throw new IOException("Don't write this!");
        }
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testOfferEx() throws Exception {
        ZKBlockingQueue<Bad> queue3 = new ZKBlockingQueue<Bad>(AppTest.URL, 10000, BS);
        try {
            queue3.offer(new Bad());
        } finally {
            queue3.close();
        }
    }
    
    @Test
    public void testGetChildren() throws Exception {
        ZKC.createNode(BS + "/abc");
        List<String> list = queue1.getChildren();
        assertEquals(1, list.size());
        assertEquals(1, queue2.size());
        assertEquals("abc", list.get(0));
        assertNull(queue1.peek());
        assertNull(queue2.peek());
        assertNull(queue1.poll());
        assertNull(queue2.poll());
        assertEquals(0, queue1.size());
        assertEquals(0, queue2.size());
    }

    @Test
    public void testPeek() throws Exception {
        ZKBlockingQueue<Integer> queue3 = new ZKBlockingQueue<Integer>(AppTest.URL, 10000, BS);
        queue3.offer(345);
        try {
            queue2.poll();
        } finally {
            queue3.close();
        }
    }
    
    @Test(expected=IllegalStateException.class)
    public void testPeekEx() throws Exception {
        ZKC.createNode(BS + "/abc", new byte[] {33,44,55,66});
        try {
            queue2.peek();
        } finally {
            ZKC.deleteNode(BS + "/abc");
        }
    }

    @Test(expected=ZKException.class)
    public void testPoll() throws Exception {
        ZKBlockingQueue<Integer> queue3 = new ZKBlockingQueue<Integer>(AppTest.URL, 10000, BS);
        queue3.offer(345);
        Integer c = queue3.poll(100, TimeUnit.MILLISECONDS);
        assertEquals(345, c.intValue());
        queue3.offer(7788);
        c = queue3.peek();
        assertEquals(7788, c.intValue());
        try {
            queue3.close();
            ZooKeeper ZKC_zk = mock(ZooKeeper.class);
            KeeperException ke = KeeperException.create(KeeperException.Code.NODEEXISTS);
            doThrow(ke).when(ZKC_zk).getData(anyString(), anyBoolean(), any(Stat.class));
            KeeperException ke2 = KeeperException.create(KeeperException.Code.AUTHFAILED);
            doThrow(ke2).when(ZKC_zk).getData(anyString(), anyBoolean(), any(Stat.class));
            ZKConnector zkc = FieldUtil.getValue(queue3, "zkc");
            FieldUtil.setValue(zkc, "zk", ZKC_zk);
            queue3.peek();
        } finally {
            queue2.poll(10, TimeUnit.MILLISECONDS);
            queue3.close();
        }
    }

    
    @Test
    public void testSize() throws Exception {
        queue1.put("Great!");
        queue1.put("Go Away!");
        
        assertEquals("Great!", queue1.peek());
        assertEquals("Great!", queue2.peek());
        assertEquals("Great!", queue2.poll());
        assertEquals("Go Away!", queue2.poll());
        assertNull(queue1.peek());
        assertNull(queue2.peek());
    }
    
    public void offer() throws InterruptedException {
        ThreadUtil.sleepAtLeast(50);
        queue2.offer("after.");
    }

    @Test
    public void testWatchQueue() throws Exception {
        Thread t = Runner.run(this, "offer");
        String s = queue1.take();
        assertEquals("after.", s);
        t.join();
    }

    @Test
    public void testWatchQueueNormal() throws Exception {
        assertNull(queue1.poll(10, TimeUnit.MICROSECONDS));
        queue2.put("usage");
        assertEquals("usage", queue2.peek());
        queue2.watchQueue();
        assertEquals("usage", queue2.poll());
    }
    
    @Test
    public void testWatchQueueLongTimeUnit() throws Exception {
        assertNull(queue1.poll(10, TimeUnit.MICROSECONDS));
        queue2.put("usage");
        assertEquals("usage", queue2.peek());
        assertTrue(queue2.watchQueue(10, TimeUnit.MICROSECONDS));
        assertEquals("usage", queue2.poll());
        assertNull(queue1.poll(10, TimeUnit.MICROSECONDS));
    }
    
    @Test(expected=ZKException.class)
    public void testWatchQueueEx() throws Exception {
        ZKBlockingQueue<Integer> queue3 = new ZKBlockingQueue<Integer>(AppTest.URL, 10000, BS);
        try {
            queue3.close();
            ZooKeeper ZKC_zk = mock(ZooKeeper.class);
            KeeperException ke = KeeperException.create(KeeperException.Code.NODEEXISTS);
            doThrow(ke).when(ZKC_zk).getChildren(anyString(), any(ChildrenChangeWather.class));
            KeeperException ke2 = KeeperException.create(KeeperException.Code.AUTHFAILED);
            doThrow(ke2).when(ZKC_zk).getChildren(anyString(), any(ChildrenChangeWather.class));
            ZKConnector zkc = FieldUtil.getValue(queue3, "zkc");
            FieldUtil.setValue(zkc, "zk", ZKC_zk);
            queue3.watchQueue();
        } finally {
            queue2.poll(1, TimeUnit.MILLISECONDS);
            queue3.close();
        }
    }
    
    @Test(expected=ZKException.class)
    public void testWatchQueueLongTimeUnitHasEx() throws Exception {
        ZKBlockingQueue<Integer> queue3 = new ZKBlockingQueue<Integer>(AppTest.URL, 10000, BS);
        try {
            queue3.close();
            ZooKeeper ZKC_zk = mock(ZooKeeper.class);
            KeeperException ke = KeeperException.create(KeeperException.Code.NODEEXISTS);
            doThrow(ke).when(ZKC_zk).getChildren(anyString(), any(ChildrenChangeWather.class));
            KeeperException ke2 = KeeperException.create(KeeperException.Code.AUTHFAILED);
            doThrow(ke2).when(ZKC_zk).getChildren(anyString(), any(ChildrenChangeWather.class));
            ZKConnector zkc = FieldUtil.getValue(queue3, "zkc");
            FieldUtil.setValue(zkc, "zk", ZKC_zk);
            queue3.watchQueue(10, TimeUnit.MICROSECONDS);
        } finally {
            queue2.poll();
            queue3.close();
        }
    }

    @Test
    public void testChildrenChangeWather() {
        CountDownLatch latch = new CountDownLatch(1);
        ZKBlockingQueue<String>.ChildrenChangeWather w = queue1.new ChildrenChangeWather(latch);
        WatchedEvent event = new WatchedEvent(EventType.None, KeeperState.Expired, "/a/ab/cc");
        w.process(event);
    }

    @Test
    public void testChildrenChangeWatherProcess2() throws Exception {
        ZKConnector zkc = mock(ZKConnector.class);
        when(zkc.connected()).thenReturn(false, false, true);

        ZKBlockingQueue<String> queue = new ZKBlockingQueue<String>(zkc, BS);
        CountDownLatch latch = new CountDownLatch(1);
        ZKBlockingQueue<String>.ChildrenChangeWather w = queue.new ChildrenChangeWather(latch);
        w.process(new WatchedEvent(Watcher.Event.EventType.None, Watcher.Event.KeeperState.Disconnected, ""));
        queue.close();
    }

    @Test(expected=NullPointerException.class)
    public void testChildrenChangeWatherProcess3() throws Exception {
        ZKConnector zkc = mock(ZKConnector.class);
        when(zkc.connected()).thenReturn(false, false, true);
        doThrow(new NullPointerException()).when(zkc).waitForConnectedTillDeath();

        ZKBlockingQueue<String> queue = new ZKBlockingQueue<String>(zkc, BS);
        CountDownLatch latch = new CountDownLatch(1);
        ZKBlockingQueue<String>.ChildrenChangeWather w = queue.new ChildrenChangeWather(latch);
        try {
            w.process(new WatchedEvent(Watcher.Event.EventType.None, Watcher.Event.KeeperState.Disconnected, ""));
        } finally {
            queue.close();
        }
    }

}
