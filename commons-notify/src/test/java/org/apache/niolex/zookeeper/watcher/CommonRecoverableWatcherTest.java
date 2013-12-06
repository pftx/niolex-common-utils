/**
 * CommonRecoverableWatcherTest.java
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
package org.apache.niolex.zookeeper.watcher;


import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.util.List;

import org.apache.niolex.zookeeper.watcher.CommonRecoverableWatcher.Listener;
import org.apache.niolex.zookeeper.watcher.RecoverableWatcher.Type;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-12-6
 */
@SuppressWarnings("unchecked")
public class CommonRecoverableWatcherTest {

    CommonRecoverableWatcher wcl;
    CommonRecoverableWatcher wda;
    Listener listn;
    ZooKeeper zk;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        zk = mock(ZooKeeper.class);
        listn = mock(Listener.class);
        wcl = new CommonRecoverableWatcher(zk, Type.CHILDREN, listn);
        wda = new CommonRecoverableWatcher(zk, Type.DATA, listn);
    }

    @Test
    public void testCommonRecoverableWatcher() throws Exception {
        WatchedEvent event = new WatchedEvent(EventType.NodeCreated, KeeperState.AuthFailed, "/a/ab/cc");
        wcl.process(event);
        verify(listn, never()).onDataChange(any(byte[].class));
        verify(listn, never()).onChildrenChange(any(List.class));
        verify(zk, never()).getData(any(String.class), any(Watcher.class), any(Stat.class));
        verify(zk, never()).getChildren(any(String.class), any(Watcher.class));
    }

    @Test
    public void testCommonRecoverableWatchera() throws Exception {
        WatchedEvent event = new WatchedEvent(EventType.NodeCreated, KeeperState.AuthFailed, "/a/ab/cc");
        wda.process(event);
        verify(listn, never()).onDataChange(any(byte[].class));
        verify(listn, never()).onChildrenChange(any(List.class));
        verify(zk, never()).getData(any(String.class), any(Watcher.class), any(Stat.class));
        verify(zk, never()).getChildren(any(String.class), any(Watcher.class));
    }

    @Test
    public void testProcess() throws Exception {
        WatchedEvent event = new WatchedEvent(EventType.NodeChildrenChanged, KeeperState.AuthFailed, "/a/ab/cc");
        wcl.process(event);
        verify(listn, never()).onDataChange(any(byte[].class));
        verify(listn, times(1)).onChildrenChange(any(List.class));
        verify(zk, never()).getData(any(String.class), any(Watcher.class), any(Stat.class));
        verify(zk, times(1)).getChildren(any(String.class), any(Watcher.class));
    }

    @Test
    public void testProcessa() throws Exception {
        WatchedEvent event = new WatchedEvent(EventType.NodeChildrenChanged, KeeperState.AuthFailed, "/a/ab/cc");
        wda.process(event);
        verify(listn, never()).onDataChange(any(byte[].class));
        verify(listn, never()).onChildrenChange(any(List.class));
        verify(zk, never()).getData(any(String.class), any(Watcher.class), any(Stat.class));
        verify(zk, never()).getChildren(any(String.class), any(Watcher.class));
    }

    @Test
    public void testReconnected() throws Exception {
        WatchedEvent event = new WatchedEvent(EventType.NodeDataChanged, KeeperState.AuthFailed, "/a/ab/cc");
        wcl.process(event);
        verify(listn, never()).onDataChange(any(byte[].class));
        verify(listn, never()).onChildrenChange(any(List.class));
        verify(zk, never()).getData(any(String.class), any(Watcher.class), any(Stat.class));
        verify(zk, never()).getChildren(any(String.class), any(Watcher.class));
    }

    @Test
    public void testReconnecteda() throws Exception {
        WatchedEvent event = new WatchedEvent(EventType.NodeDataChanged, KeeperState.AuthFailed, "/a/ab/cc");
        wda.process(event);
        verify(listn, times(1)).onDataChange(any(byte[].class));
        verify(listn, never()).onChildrenChange(any(List.class));
        verify(zk, times(1)).getData(any(String.class), any(Watcher.class), any(Stat.class));
        verify(zk, never()).getChildren(any(String.class), any(Watcher.class));
    }

    @Test
    public void testReconnectedEx() throws Exception {
        doThrow(new InterruptedException("exxx")).when(zk).getChildren(any(String.class), any(Watcher.class));
        wcl.reconnected(zk, "/a/b/c");
        verify(listn, never()).onDataChange(any(byte[].class));
        verify(listn, never()).onChildrenChange(any(List.class));
        verify(zk, never()).getData(any(String.class), any(Watcher.class), any(Stat.class));
        verify(zk, times(1)).getChildren(any(String.class), any(Watcher.class));
    }

    @Test
    public void testReconnectedaEx() throws Exception {
        doThrow(new InterruptedException("exxx")).when(zk).getData(any(String.class), any(Watcher.class), any(Stat.class));
        wda.reconnected(zk, "/a/b/c");
        verify(listn, never()).onDataChange(any(byte[].class));
        verify(listn, never()).onChildrenChange(any(List.class));
        verify(zk, times(1)).getData(any(String.class), any(Watcher.class), any(Stat.class));
        verify(zk, never()).getChildren(any(String.class), any(Watcher.class));
    }

    @Test
    public void testGetType() throws Exception {
        wcl.reconnected(zk, "/a/b/c");
        verify(listn, never()).onDataChange(any(byte[].class));
        verify(listn, times(1)).onChildrenChange(any(List.class));
        verify(zk, never()).getData(any(String.class), any(Watcher.class), any(Stat.class));
        verify(zk, times(1)).getChildren(any(String.class), any(Watcher.class));
        assertEquals(wcl.getType(), Type.CHILDREN);
    }

    @Test
    public void testGetTypea() throws Exception {
        wda.reconnected(zk, "/a/b/c");
        verify(listn, times(1)).onDataChange(any(byte[].class));
        verify(listn, never()).onChildrenChange(any(List.class));
        verify(zk, times(1)).getData(any(String.class), any(Watcher.class), any(Stat.class));
        verify(zk, never()).getChildren(any(String.class), any(Watcher.class));
        assertEquals(wda.getType(), Type.DATA);
    }

}
