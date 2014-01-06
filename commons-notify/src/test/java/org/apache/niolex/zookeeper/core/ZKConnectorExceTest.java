/**
 * ZKConnWatcherTest.java
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
package org.apache.niolex.zookeeper.core;


import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;

import org.apache.niolex.commons.concurrent.ThreadUtil;
import org.apache.niolex.commons.reflect.FieldUtil;
import org.apache.niolex.commons.test.AnnotationOrderedRunner;
import org.apache.niolex.commons.test.AnnotationOrderedRunner.Order;
import org.apache.niolex.commons.util.Runner;
import org.apache.niolex.notify.AppTest;
import org.apache.niolex.zookeeper.watcher.WatcherHolder;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-12-5
 */
@RunWith(AnnotationOrderedRunner.class)
public class ZKConnectorExceTest {

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

    ZooKeeper zkOld;

    @Before
    public void save() {
        zkOld = ZKC.zk;
    }

    @After
    public void restore() {
        ZKC.zk = zkOld;
    }

    @Test
    @Order(-2)
    public void testConnectToZookeeper() throws Exception {
        // Connect again, do nothing.
        ZKC.connectToZookeeper();
    }

    @Test
    @Order(-1)
    public void testWaitForConnectedTillDeath() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        FieldUtil.setValue(ZKC, "latch", latch);
        Thread t = Runner.run(ZKC, "waitForConnectedTillDeath");
        for (int i = 0; i < 20; ++i) {
            t.interrupt();
            ThreadUtil.sleep(1);
        }
        latch.countDown();
        t.join();
    }

    @Test
    @Order(0)
    public void testProcessEvent() throws Exception {
        WatchedEvent event = new WatchedEvent(EventType.NodeCreated, KeeperState.Expired, "/a/ab/cc");
        ZKC.addAuthInfo("abc", "lex");
        ZKC.process(event);
        event = new WatchedEvent(EventType.NodeCreated, KeeperState.Disconnected, "/a/ab/cc");
        ZKC.process(event);
        event = new WatchedEvent(EventType.NodeCreated, KeeperState.AuthFailed, "/a/ab/cc");
        ZKC.process(event);
    }

    @Test
    @Order(1)
    public void testConnected() throws Exception {
        assertTrue(ZKC.connected());
        ZKC.zk = mock(ZooKeeper.class);
        assertFalse(ZKC.connected());
        ZKC.zk = null;
        assertFalse(ZKC.connected());
        ZKC.zk = zkOld;
        assertTrue(ZKC.connected());
    }

    @Order(2)
    @Test
    public void testReconnect() throws Exception {
        Field watcherHolderF = FieldUtil.getField(ZKConnector.class, "watcherHolder");
        WatcherHolder watcherOld = FieldUtil.getFieldValue(ZKC, watcherHolderF);
        WatcherHolder watcher = mock(WatcherHolder.class);
        doThrow(new RuntimeException("Lex-xeL")).when(watcher).reconnected();
        FieldUtil.setFieldValue(ZKC, watcherHolderF, watcher);
        FieldUtil.setFieldValue(ZKC, FieldUtil.getField(ZKConnector.class, "sessionTimeout"), 3);
        Thread t = Runner.run(ZKC, "reconnect");
        ThreadUtil.sleep(20);
        FieldUtil.setFieldValue(ZKC, watcherHolderF, watcherOld);
        ThreadUtil.join(t);
        FieldUtil.setFieldValue(ZKC, FieldUtil.getField(ZKConnector.class, "sessionTimeout"), 3000);
    }

    @Order(3)
    @Test
    public void testClose() throws Exception {
        ZKC.zk = mock(ZooKeeper.class);
        doThrow(new InterruptedException("Haha")).when(ZKC.zk).close();
        ZKC.close();
        assertNull(ZKC.zk);
    }

    @Test(expected=ZKException.class)
    @Order(4)
    public void testGetDataEx() throws Exception {
        ZKC.zk = mock(ZooKeeper.class);
        doThrow(new InterruptedException("exxx")).when(ZKC.zk).getData(any(String.class), any(Boolean.class), any(Stat.class));
        ZKC.getData("/a/b/c");
    }

    @Test(expected=ZKException.class)
    @Order(5)
    public void testGetChildrenEx() throws Exception {
        ZKC.zk = mock(ZooKeeper.class);
        String path = "/a/b/c";
        doThrow(new InterruptedException("exxx")).when(ZKC.zk).getChildren(path, false);
        ZKC.getChildren(path);
    }

    @Test
    @Order(6)
    public void testExistsEx() throws Exception {
        String path = "/path/to/echeo";
        ZKC.zk = mock(ZooKeeper.class);
        doThrow(new InterruptedException("Haha")).when(ZKC.zk).exists(path, false);
        try {
            assertTrue(ZKC.exists(path));
        } catch (ZKException e) {
            assertEquals(e.getCode(), ZKException.Code.INTERRUPT);
        }
    }

    @Test
    @Order(7)
    public void testCreateNodeIfAbsentEx() throws Exception {
        String path = "/path/to/echeo";
        ZKC.zk = mock(ZooKeeper.class);
        KeeperException ke = KeeperException.create(KeeperException.Code.NODEEXISTS);
        doThrow(ke).when(ZKC.zk).create(path, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        assertFalse(ZKC.createNodeIfAbsent(path, null));
    }

    @Test
    @Order(8)
    public void testMakeSurePathExistsEx() throws Exception {
        ZKC.makeSurePathExists("/lex/zkc/a");
        ZKC.makeSurePathExists("/lex/zkc/b");
        ZKC.zk = mock(ZooKeeper.class);
        KeeperException ke = KeeperException.create(KeeperException.Code.NODEEXISTS);
        doThrow(ke).when(ZKC.zk).create("/lex/zkc", null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        KeeperException ke2 = KeeperException.create(KeeperException.Code.AUTHFAILED);
        doThrow(ke2).when(ZKC.zk).create("/lex/zkc/c", null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        try {
            ZKC.makeSurePathExists("/lex/zkc/c");
            assertFalse(true);
        } catch (ZKException e) {
            assertEquals(e.getCode(), ZKException.Code.NO_AUTH);
        }
    }

    @Test
    @Order(9)
    public void testUpdateNodeData() throws Exception {
        byte[] data = "not".getBytes();
        String path = "/lex/zkc/bbc";
        try {
            ZKC.updateNodeData(path, data);
            assertFalse(true);
        } catch (ZKException e) {
            assertEquals(e.getCode(), ZKException.Code.NO_NODE);
        }
    }

    @Test
    @Order(9)
    public void testUpdateNodeDataE() throws Exception {
        ZKC.zk = mock(ZooKeeper.class);
        KeeperException ke = KeeperException.create(KeeperException.Code.NONODE);
        byte[] data = "not".getBytes();
        String path = "/lex/zkc/bbc";
        doThrow(ke).when(ZKC.zk).setData(path, data, -1);
        try {
            ZKC.updateNodeData(path, data);
            assertFalse(true);
        } catch (ZKException e) {
            assertEquals(e.getCode(), ZKException.Code.NO_NODE);
        }
    }

    @Test
    @Order(11)
    public void testDeleteNodeEx() throws Exception {
        String path = "/lex/zkc/bbc";
        try {
            ZKC.deleteNode(path);
            assertFalse(true);
        } catch (ZKException e) {
            assertEquals(e.getCode(), ZKException.Code.NO_NODE);
        }
    }

    @Test
    @Order(29)
    public void testDeleteTreeEx() throws Exception {
        ZKC.deleteTree("/lex");
        assertFalse(ZKC.exists("/lex"));
    }

}
