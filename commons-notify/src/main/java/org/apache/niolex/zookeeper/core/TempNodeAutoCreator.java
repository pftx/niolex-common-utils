/**
 * TempNodeAutoCreator.java
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
package org.apache.niolex.zookeeper.core;

import java.io.IOException;

/**
 * The class used to automatically create temporary node.
 * <p>
 * We can only manage one temporary node here.
 * 
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 2.1.2
 * @since Jul 11, 2016
 */
public class TempNodeAutoCreator extends ZKConnector {
    
    /**
     * The temporary ZK path.
     */
    private volatile String path;
    
    /**
     * The data to be stored to the temporary path.
     */
    private volatile byte[] data;
    
    /**
     * Whether the temporary node is a sequential node or not
     */
    private volatile boolean isSequential;
    
    /**
     * The created temporary node path.
     */
    protected volatile String selfPath = null;
    
    /**
     * Construct a new TempNodeAutoCreator and connect to ZK server.
     * We will wait until get connected in this method.
     *
     * @param clusterAddress the zookeeper cluster servers address list
     * @param sessionTimeout the session timeout in microseconds
     * @throws IOException in cases of network failure
     * @throws IllegalArgumentException if sessionTimeout is too small
     */
    public TempNodeAutoCreator(String clusterAddress, int sessionTimeout) throws IOException {
        super(clusterAddress, sessionTimeout);
    }

    /**
     * Create temporary ZK node. When network error occurred, it will be deleted from ZK server
     * automatically. But if we re-connected to ZK again, we will create it again automatically.
     * <p>
     * The actual path of the created node path will be stored in a protected field: {@code selfPath}
     * 
     * @param path the node path
     * @param data the node data
     * @param isSequential whether the node is a sequential node or not
     * @return true if created, false if there is another temporary node already created
     * @throws ZKException if failed to create node
     */
    public synchronized boolean autoCreateTempNode(String path, byte[] data, boolean isSequential) {
        if (selfPath != null) {
            return false;
        }
        
        this.path = path;
        this.data = data;
        this.isSequential = isSequential;
        selfPath = createNode(path, data, true, isSequential);
        return true;
    }

    @Override
    protected synchronized void reconnected() {
        // Reconnected to zookeeper server after session expired.
        if (selfPath != null) {
            String oldPath = selfPath;
            try {
                selfPath = this.createNode(path, data, true, isSequential);
                LOG.info("We disconnected with old path: {}, re joined with new path: {}.", oldPath, selfPath);
            } catch (Exception e) {
                LOG.error("Failed to recover temporary node.", e);
            }
        }
        super.reconnected();
    }
    
    /**
     * Release the internal temporary ZK node.
     * 
     * @return true if released, false if there is no temporary node been managed
     */
    public synchronized boolean releaseTempNode() {
        if (selfPath == null) {
            return false;
        }
        deleteNode(selfPath);
        selfPath = null;
        return true;
    }

    /**
     * @return the current managed temporary node path.
     */
    public String getSelfPath() {
        return selfPath;
    }

}
