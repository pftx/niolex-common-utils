/**
 * TempNodeRecoverableWatcher.java
 *
 * Copyright 2016 the original author or authors.
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

import org.apache.niolex.zookeeper.core.ZKConnector;
import org.apache.zookeeper.WatchedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Recover temporary nodes when we re-connected to ZK server.
 * 
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 2.1.2
 * @since Jul 11, 2016
 */
public class TempNodeRecoverableWatcher implements RecoverableWatcher {
    
    protected static final Logger LOG = LoggerFactory.getLogger(TempNodeRecoverableWatcher.class);
    
    /**
     * The zookeeper connector.
     */
    private final ZKConnector zkc;
    
    /**
     * The temporary ZK path.
     */
    private final String path;
    
    /**
     * The data to be stored to the temporary path.
     */
    private final byte[] data;
    
    /**
     * Whether the temporary node is a sequential node or not
     */
    private final boolean isSequential;

    /**
     * Construct a temporary node recoverable watcher with the specified parameters.
     *
     * @param zkc the zookeeper connector to be used
     * @param path the node path
     * @param data the node data
     * @param isSequential whether the node is a sequential node or not
     */
    public TempNodeRecoverableWatcher(ZKConnector zkc, String path, byte[] data, boolean isSequential) {
        super();
        this.zkc = zkc;
        this.path = path;
        this.data = data;
        this.isSequential = isSequential;
    }

    /**
     * This is the override of super method.
     * @see org.apache.zookeeper.Watcher#process(org.apache.zookeeper.WatchedEvent)
     */
    @Override
    public void process(WatchedEvent event) {
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.zookeeper.watcher.RecoverableWatcher#reconnected()
     */
    @Override
    public void reconnected() {
        try {
            zkc.createTempNodeAutoRecover(path, data, isSequential);
        } catch (Exception e) {
            LOG.error("Failed to recover temporary node.", e);
        }
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.zookeeper.watcher.RecoverableWatcher#getType()
     */
    @Override
    public Type getType() {
        return null;
    }

}
