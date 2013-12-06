/**
 * WatcherSet.java
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

import java.util.HashSet;
import java.util.Set;

import org.apache.niolex.commons.test.Check;
import org.apache.zookeeper.ZooKeeper;

/**
 * The container to holder all the watchers in zookeeper connector.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-12-5
 */
public class WatcherHolder {

    // The watcher set.
    Set<WatcherItem> watcherSet = new HashSet<WatcherItem>();

    /**
     * Re add all the watcher to this zookeeper.
     *
     * @param zk the new zookeeper
     */
    public synchronized void reconnected(ZooKeeper zk) {
        for (WatcherItem item : watcherSet) {
            item.getWat().reconnected(zk, item.getPath());
        }
    }

    /**
     * Add this watcher into the holder.
     *
     * @param path the watch path
     * @param recoWatcher the watcher
     */
    public synchronized void add(String path, RecoverableWatcher recoWatcher) {
        watcherSet.add(new WatcherItem(path, recoWatcher));
    }

}

/**
 * The item to save watcher information, used when reconnect.
 *
 * @author Xie, Jiyun
 */
class WatcherItem {

    /**
     * The path to watch.
     */
    private final String path;

    private final RecoverableWatcher wat;

    /**
     * Create a new WatcherItem.
     *
     * @param path
     * @param wat
     */
    public WatcherItem(String path, RecoverableWatcher wat) {
        super();
        Check.notNull(path, "path should not be null.");
        Check.notNull(wat, "watcher should not be null.");
        this.path = path;
        this.wat = wat;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @return the watcher
     */
    public RecoverableWatcher getWat() {
        return wat;
    }

    /**
     * This is the override of super method.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return 31 * path.hashCode() + wat.hashCode();
    }

    /**
     * This is the override of super method.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof WatcherItem))
            return false;
        WatcherItem other = (WatcherItem) obj;
        return path.equals(other.path) && wat.equals(other.wat);
    }

}
