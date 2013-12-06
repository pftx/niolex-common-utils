/**
 * CommonRecoverableWatcher.java
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

import java.util.List;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The common recoverable watcher, watch data changes or children changes.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-12-6
 */
public class CommonRecoverableWatcher implements RecoverableWatcher {

    protected static final Logger LOG = LoggerFactory.getLogger(CommonRecoverableWatcher.class);

    /**
     * The data or children change listener.
     *
     * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
     * @version 1.0.0
     * @since 2013-12-6
     */
    public static interface Listener {

        /**
         * Data changed in zookeeper.
         *
         * @param data the new data
         */
        public void onDataChange(byte[] data);

        /**
         * Children changed in zookeeper.
         *
         * @param list the new children list
         */
        public void onChildrenChange(List<String> list);

    }

    /**
     * The watch type.
     */
    private final Type type;

    /**
     * The data or children change listener.
     */
    private final Listener listn;

    /**
     * The zookeeper.
     */
    private ZooKeeper zk;

    /**
     * Construct a common recoverable watcher with the specified parameters.
     *
     * @param zk the zookeeper to be used
     * @param type the watch type
     * @param listn the data or children change listener
     */
    public CommonRecoverableWatcher(ZooKeeper zk, Type type, Listener listn) {
        super();
        this.zk = zk;
        this.type = type;
        this.listn = listn;
    }

    /**
     * This is the override of super method.
     * @see org.apache.zookeeper.Watcher#process(org.apache.zookeeper.WatchedEvent)
     */
    @Override
    public void process(WatchedEvent event) {
        if (type == Type.CHILDREN && event.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
            childrenCHanged(event.getPath());
        } else if (type == Type.DATA && event.getType() == Watcher.Event.EventType.NodeDataChanged) {
            dataChanged(event.getPath());
        }
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.zookeeper.watcher.RecoverableWatcher#reconnected(org.apache.zookeeper.ZooKeeper, java.lang.String)
     */
    @Override
    public void reconnected(ZooKeeper zk, String path) {
        // Replace the zookeeper.
        this.zk = zk;
        if (type == Type.CHILDREN) {
            childrenCHanged(path);
        } else {
            dataChanged(path);
        }
    }

    /**
     * Data changed.
     */
    private void dataChanged(String path) {
        try {
            byte[] data = zk.getData(path, this, new Stat());
            listn.onDataChange(data);
        } catch (Exception e) {
            LOG.error("Failed to watch Data.", e);
        }
    }

    /**
     * Children changed.
     */
    private void childrenCHanged(String path) {
        try {
            List<String> list = zk.getChildren(path, this);
            listn.onChildrenChange(list);
        } catch (Exception e) {
            LOG.error("Failed to watch Children.", e);
        }
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.zookeeper.watcher.RecoverableWatcher#getType()
     */
    @Override
    public Type getType() {
        return type;
    }

}
