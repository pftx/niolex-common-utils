/**
 * NotifyDataWatcher.java
 * 
 * Copyright 2013 Niolex, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.niolex.notify.core;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

/**
 * Watch the data changes of notify.
 * 
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2013-1-4
 */
public class NotifyDataWatcher implements RecoverableWatcher {
    
    private final ZKConnector zkConn;
    private final Notify notify;
    

    /**
     * Create a new NotifyDataWatcher
     * 
     * @param zkConn
     * @param notify
     */
    public NotifyDataWatcher(ZKConnector zkConn, Notify notify) {
        super();
        this.zkConn = zkConn;
        this.notify = notify;
    }

    /**
     * Override super method
     * @see org.apache.zookeeper.Watcher#process(org.apache.zookeeper.WatchedEvent)
     */
    @Override
    public void process(WatchedEvent event) {
        if (event.getType() == Watcher.Event.EventType.NodeDataChanged) {
            try {
                Stat st = new Stat();
                byte[] data = zkConn.zk.getData(event.getPath(), this, st);
                notify.onDataChange(data);
            } catch (Exception e) {
                ZKConnector.LOG.error("Failed to watch Data.", e);
            }
        }
    }

    /**
     * Override super method
     * @see org.apache.niolex.notify.core.RecoverableWatcher#reconnected(java.lang.String)
     */
    @Override
    public void reconnected(String path) {
        try {
            Stat st = new Stat();
            byte[] data = zkConn.zk.getData(path, this, st);
            notify.onDataChange(data);
        } catch (Exception e) {
            ZKConnector.LOG.error("Failed to watch Data.", e);
        }
    }

}