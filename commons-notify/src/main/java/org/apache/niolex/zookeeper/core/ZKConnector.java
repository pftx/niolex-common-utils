/**
 * ZKConnector.java
 *
 * Copyright 2012 Niolex, Inc.
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
package org.apache.niolex.zookeeper.core;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.collection.CollectionUtil;
import org.apache.niolex.commons.concurrent.ThreadUtil;
import org.apache.niolex.commons.util.SystemUtil;
import org.apache.niolex.zookeeper.watcher.CommonRecoverableWatcher;
import org.apache.niolex.zookeeper.watcher.RecoverableWatcher;
import org.apache.niolex.zookeeper.watcher.WatcherHolder;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The main Zookeeper connector, manage zookeeper and retry connection.
 * We encapsulate the common add, update, delete and watch operations for Zookeeper.
 *
 * @author Xie, Jiyun
 * @version 1.0.0, Date: 2012-6-10
 */
public class ZKConnector implements Watcher {

    protected static final Logger LOG = LoggerFactory.getLogger(ZKConnector.class);

    /**
     * Store all the watchers here.
     */
    private final WatcherHolder watcherHolder = new WatcherHolder();

    /**
     * The zookeeper cluster address.
     */
    private final String clusterAddress;

    /**
     * The zookeeper connection session timeout.
     */
    private final int sessionTimeout;

    /**
     * The zookeeper authentication information.
     */
    private byte[] auth;

    /**
     * The latch to wait for connected.
     */
    private CountDownLatch latch;

    /**
     * The internal zookeeper instance.
     */
    protected ZooKeeper zk;

    /**
     * Construct a new ZKConnector and connect to ZK server.
     * We will wait until get connected in this method.
     *
     * @param clusterAddress the zookeeper cluster servers address list
     * @param sessionTimeout the session timeout in microseconds
     * @throws IOException in cases of network failure
     * @throws IllegalArgumentException if sessionTimeout is too small
     */
    public ZKConnector(String clusterAddress, int sessionTimeout) throws IOException {
        super();
        this.clusterAddress = clusterAddress;
        this.sessionTimeout = sessionTimeout;
        if (sessionTimeout < 5000) {
            throw new IllegalArgumentException("sessionTimeout too small.");
        }
        connectToZookeeper();
    }

    /**
     * Make a connection to zookeeper, and wait until connected.
     *
     * @throws IOException
     */
    protected synchronized void connectToZookeeper() throws IOException {
        if (connected()) {
            return;
        }
        // Close it first to ensure there will be no more than one connection.
        close();
        // Use this to sync for connected event.
        latch = new CountDownLatch(1);
        this.zk = new ZooKeeper(clusterAddress, sessionTimeout, this);
        waitForConnectedTillDeath();
    }

    /**
     * Wait for zookeeper to be connected, if can not connect, wait forever.
     *
     * @param latch the latch to wait for
     */
    public void waitForConnectedTillDeath() {
        while (!ThreadUtil.waitFor(latch)) {}
    }

    /**
     * Override super method
     * @see org.apache.zookeeper.Watcher#process(org.apache.zookeeper.WatchedEvent)
     */
    @Override
    public void process(WatchedEvent event) {
        LOG.info("ZK Connection status changed to: {}.", event.getState());
        switch (event.getState()) {
            case SyncConnected:
                latch.countDown();
                break;
            case Expired:
                reconnect();
                break;
        }
    }

    /**
     * Add authenticate info for this client.
     * 添加client的权限认证信息
     *
     * @param username the user name of this client
     * @param password the password of this client
     */
    public void addAuthInfo(String username, String password) {
        auth = StringUtil.strToUtf8Byte(username + ":" + password);
        this.zk.addAuthInfo("digest", auth);
    }

    // ========================================================================
    // Connection related operations
    // ========================================================================

    /**
     * @return the connection status.
     */
    public boolean connected() {
        return zk != null && (zk.getState() == ZooKeeper.States.CONNECTED);
    }

    /**
     * @return the current zookeeper.
     */
    public ZooKeeper zooKeeper() {
        return zk;
    }

    /**
     * Try to reconnect to zookeeper cluster, do not call this method, we will use it when necessary.
     */
    protected void reconnect() {
        while (true) {
            try {
                connectToZookeeper();
                if (auth != null) {
                    this.zk.addAuthInfo("digest", auth);
                }
                // Notify watchers.
                watcherHolder.reconnected();
                break;
            } catch (Exception e) {
                // We don't care, we will retry again and again.
                LOG.warn("Error occured when reconnect - {}, system will retry.", e.toString());
                SystemUtil.sleep(sessionTimeout / 3);
            }
        }
    }

    /**
     * Close the connection to ZK server.
     * 注意！一但关闭连接，请立即丢弃该对象，该对象的所有的方法的结果将不确定
     */
    public void close() {
        try {
            if (this.zk != null) {
                this.zk.close();
                this.zk = null;
            }
        } catch (Exception e) {
            this.zk = null;
            LOG.info("Failed to close ZK connection.", e);
        }
    }

    // ========================================================================
    // Watch/Read related operations
    // ========================================================================

    /**
     * Attach a watcher to the data of the specified path.
     *
     * @param path the zookeeper path you want to watch
     * @param listn the zookeeper listener
     * @return the current result
     * @throws ZKException if failed to do watch
     */
    public byte[] watchData(String path, ZKListener listn) {
        RecoverableWatcher recoWatcher = new CommonRecoverableWatcher(this,
                RecoverableWatcher.Type.DATA, listn, path);
        return submitWatcher(path, recoWatcher);
    }

    /**
     * Attach a watcher to the children of the specified path.
     *
     * @param path the zookeeper path you want to watch
     * @param listn the zookeeper listener
     * @return the current result
     * @throws ZKException if failed to do watch
     */
    public List<String> watchChildren(String path, ZKListener listn) {
        RecoverableWatcher recoWatcher = new CommonRecoverableWatcher(this,
                RecoverableWatcher.Type.CHILDREN, listn, path);
        return submitWatcher(path, recoWatcher);
    }

    /**
     * Attach a watcher to the path you want to watch.
     * We are using prototype for convenience, but the return type is
     * fixed:
     *  when watch data, return byte[]
     *  when watch children, return List<String>
     * If user use other types as return type, a ClassCastException will throw.
     *
     * @param path the zookeeper path you want to watch
     * @param recoWatcher the recoverable watcher
     * @return the current result
     * @throws ZKException if failed to do watch
     * @throws ClassCastException if can not cast the return type to user specified type
     */
    @SuppressWarnings("unchecked")
    public <T> T submitWatcher(String path, RecoverableWatcher recoWatcher) {
        Object r = doWatch(path, recoWatcher);
        // Add this item to the watcher set, so the system will
        // recover them after reconnected.
        watcherHolder.add(recoWatcher);
        return (T) r;
    }

    /**
     * Do real watch. Please use {@link #submitWatcher(String, RecoverableWatcher)} instead.
     * This method is for internal use.
     *
     * @param item the item to do watch
     * @return the current data in Zookeeper
     * @throws ZKException if failed to do watch
     */
    protected Object doWatch(String path, RecoverableWatcher recoWatcher) {
        try {
            switch (recoWatcher.getType()) {
                case CHILDREN:
                    return this.zk.getChildren(path, recoWatcher);
                case DATA:
                default:
                    return this.zk.getData(path, recoWatcher, new Stat());
            }
        } catch (Exception e) {
            throw ZKException.makeInstance("Failed to do Watch.", e);
        }
    }

    /**
     * Get the node data of the specified path.
     *
     * @param path the specified path
     * @return the node data
     * @throws ZKException if failed to get data
     */
    public byte[] getData(String path) {
        try {
            return this.zk.getData(path, false, new Stat());
        } catch (Exception e) {
            throw ZKException.makeInstance("Failed to get Data.", e);
        }
    }

    /**
     * Get the node children of the specified path.
     *
     * @param path the specified path
     * @return the node children
     * @throws ZKException if failed to get children
     */
    public List<String> getChildren(String path) {
        try {
            return this.zk.getChildren(path, false);
        } catch (Exception e) {
            throw ZKException.makeInstance("Failed to get Children.", e);
        }
    }

    /**
     * Check whether the specified path exists.
     *
     * @param path the specified path
     * @return true if the node exists, false otherwise
     * @throws ZKException if failed to check exists
     */
    public boolean exists(String path) {
        try {
            return this.zk.exists(path, false) != null;
        } catch (Exception e) {
            throw ZKException.makeInstance("Failed to check Exists.", e);
        }
    }

    // ========================================================================
    // CUD related operations
    // ========================================================================

    /**
     * Create node without data.
     *
     * @param path the node path
     * @throws ZKException if failed to create node
     */
    public void createNode(String path) {
        createNode(path, null, false, false);
    }

    /**
     * Create node.
     *
     * @param path the node path
     * @param data the node data
     * @throws ZKException if failed to create node
     */
    public void createNode(String path, byte[] data) {
        createNode(path, data, false, false);
    }

    /**
     * Create node if absent without data.
     *
     * @param path the node path
     * @return true if a node created here, false if already exists
     * @throws ZKException if failed to create node
     */
    public boolean createNodeIfAbsent(String path) {
        return createNodeIfAbsent(path, null);
    }

    /**
     * Create node if absent.
     *
     * @param path the node path
     * @param data the node data
     * @return true if a node created here, false if already exists
     * @throws ZKException if failed to create node
     */
    public boolean createNodeIfAbsent(String path, byte[] data) {
        try {
            if (!exists(path)) {
                createNode(path, data, false, false);
                return true;
            }
        } catch (ZKException e) {
            // The node may already exist.
            if (e.getCode() != ZKException.Code.NODE_EXISTS) {
                throw e;
            }
        }
        return false;
    }

    /**
     * Make sure that the specified path exists. We will create any parent path if necessary.
     *
     * @param path the specified path
     * @throws ZKException if failed to check exists or make path
     */
    public void makeSurePathExists(String path) {
        if (!exists(path)) {
            int idx = path.lastIndexOf('/');
            if (idx > 0) {
                // Make sure the parent path exists.
                makeSurePathExists(path.substring(0, idx));
            }
            try {
                createNode(path, null, false, false);
            } catch (ZKException e) {
                // The node may already exist.
                if (e.getCode() != ZKException.Code.NODE_EXISTS) {
                    throw e;
                }
            }
        }
    }

    /**
     * Create node.
     *
     * @param path the node path
     * @param data the node data
     * @param isTmp whether the node is a temporary node or not
     * @param isSequential whether the node is a sequential node or not
     * @return the actual path of the created node
     * @throws ZKException if failed to create node
     */
    public String createNode(String path, byte[] data, boolean isTmp, boolean isSequential) {
        try {
            CreateMode createMode = null;
            if (isTmp) {
                if (isSequential) {
                    //临时自增
                    createMode = CreateMode.EPHEMERAL_SEQUENTIAL;
                } else {
                    //临时固定
                    createMode = CreateMode.EPHEMERAL;
                }
            } else {
                if (isSequential) {
                    //永久自增
                    createMode = CreateMode.PERSISTENT_SEQUENTIAL;
                } else {
                    //永久固定
                    createMode = CreateMode.PERSISTENT;
                }
            }
            return doCreateNode(path, data, createMode);
        } catch (Exception e) {
            throw ZKException.makeInstance("Failed to create Node.", e);
        }
    }

    /**
     * Do create a new ZK node.
     *
     * @param path the node path
     * @param data the node data
     * @param createMode the create mode of this node
     * @return the actual path of the created node
     * @throws KeeperException
     * @throws InterruptedException
     */
    protected String doCreateNode(String path, byte[] data, CreateMode createMode)
            throws KeeperException, InterruptedException {
        return zk.create(path, data, Ids.OPEN_ACL_UNSAFE, createMode);
    }

    /**
     * Update data of a node.
     *
     * @param path the node path
     * @param data the new node data
     * @throws ZKException if failed to update node data
     */
    public void updateNodeData(String path, byte[] data) {
        try {
            zk.setData(path, data, -1);
        } catch (Exception e) {
            throw ZKException.makeInstance("Failed to update Node Data.", e);
        }
    }

    /**
     * Delete a node from zookeeper.
     *
     * @param path the node path
     * @throws ZKException if failed to delete the node
     */
    public void deleteNode(String path) {
        try {
            zk.delete(path, -1);
        } catch (Exception e) {
            throw ZKException.makeInstance("Failed to delete Node.", e);
        }
    }

    /**
     * Delete the specified node and all of it's children from zookeeper.
     *
     * @param path the tree root path
     * @throws ZKException if failed to delete the tree
     */
    public void deleteTree(String path) {
        List<String> list = getChildren(path);
        if (!CollectionUtil.isEmpty(list)) {
            // Recursively delete all the children.
            for (String name : list) {
                deleteTree(path + "/" + name);
            }
        }
        deleteNode(path);
    }

}
