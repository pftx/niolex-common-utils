/**
 * WatcherHolderTest.java
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


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-12-6
 */
public class WatcherHolderTest {

    private WatcherHolder h = new WatcherHolder();
    private RecoverableWatcher recoWatcher = mock(RecoverableWatcher.class);

    @Test
    public void testReconnected() throws Exception {
        h.add("abc", recoWatcher);
        ZooKeeper zk = mock(ZooKeeper.class);
        h.reconnected(zk);
        verify(recoWatcher, times(1)).reconnected(zk, "abc");
    }

    @Test
    public void testAdd() throws Exception {
        WatcherItem a = new WatcherItem("abc", recoWatcher);
        WatcherItem b = new WatcherItem("abd", recoWatcher);
        WatcherItem c = new WatcherItem("abc", recoWatcher);
        assertNotEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, b);
        assertEquals(a.hashCode(), c.hashCode());
        assertEquals(a, c);
    }

    @Test
    public void testEquals() throws Exception {
        WatcherItem a = new WatcherItem("abc", recoWatcher);
        WatcherItem b = new WatcherItem("abc", mock(RecoverableWatcher.class));
        assertNotEquals(a, null);
        assertNotEquals(a, b);
        assertNotEquals(a, "not yet implemented");
    }

}
