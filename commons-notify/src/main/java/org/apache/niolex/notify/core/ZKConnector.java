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
package org.apache.niolex.notify.core;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.util.SystemUtil;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
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
public class ZKConnector {

    protected static final Logger LOG = LoggerFactory.getLogger(ZKConnector.class);

    protected ZooKeeper zk;

    private final String clusterAddress;

    private final int sessionTimeout;

    private final Set<WatcherItem> watcherSet = Collections.synchronizedSet(new HashSet<WatcherItem>());

    private byte[] auth;

    /**
     * Construct a new ZKConnector and connect to ZK server.
     *
     * @param clusterAddress
     * @param sessionTimeout
     * @throws IOException
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
    private void connectToZookeeper() throws IOException {
        // Use this to sync for connected event.
        CountDownLatch latch = new CountDownLatch(1);
        this.zk = new ZooKeeper(clusterAddress, sessionTimeout, new ZKConnWatcher(this, latch));
        waitForConnectedTillDeath(latch);
    }

    /**
     * Wait for zookeeper to be connected, if can not connect, wait forever.
     *
     * @param latch
     */
    private void waitForConnectedTillDeath(CountDownLatch latch) {
        while (true) {
            try {
                latch.await();
                return;
            } catch (InterruptedException e) {}
        }
    }

    /**
     * Add authenticate info for this client.
     * 添加client的权限认证信息
     *
     * @param username
     * @param password
     */
    public void addAuthInfo(String username, String password) {
        auth = StringUtil.strToUtf8Byte(username + ":" + password);
        this.zk.addAuthInfo("digest", auth);
    }

    // ========================================================================
    // Connection related operations
    // ========================================================================

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
                // Re add all the watcher.
                synchronized (watcherSet) {
                    for (WatcherItem item : watcherSet) {
                        item.getWat().reconnected(item.path);
                    }
                }
                break;
            } catch (Exception e) {
                // We don't care, we will retry again and again.
                LOG.error("Error occured when reconnect, system will retry.", e);
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
            this.zk.close();
        } catch (Exception e) {
            LOG.info("Failed to close ZK connection.", e);
        }
    }

    // ========================================================================
    // Watch/Read related operations
    // ========================================================================

    /**
     * Attach a watcher to the path you want to watch.
     * We are using prototype for convenience, but the return type is
     * fixed:
     *  when watch data, return byte[]
     *  when watch children, return List<String>
     * If user use other types as return type, a ClassCastException will throw.
     *
     * @param path
     * @param wat
     * @param isChildren
     * @return the current data
     */
    @SuppressWarnings("unchecked")
    public <T> T submitWatcher(String path, RecoverableWatcher wat, boolean isChildren) {
        WatcherItem item = new WatcherItem(path, wat, isChildren);
        Object r = doWatch(item);
        // Add this item to the list, so the system will
        // recover them after reconnected.
        watcherSet.add(item);
        return (T) r;
    }

    /**
     * Do real watch. Please use submitWatcher instead. This method is for internal use.
     *
     * @param item the item to do watch
     * @return the current data in Zookeeper
     */
    protected Object doWatch(WatcherItem item) {
        try {
            if (item.isChildren()) {
                return this.zk.getChildren(item.getPath(), item.getWat());
            } else {
                Stat st = new Stat();
                return this.zk.getData(item.getPath(), item.getWat(), st);
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
     * @throws ZKException
     */
    public void createNode(String path) {
        createNode(path, null, false, false);
    }

    /**
     * Create node.
     *
     * @param path the node path
     * @param data the data data
     * @throws ZKException
     */
    public void createNode(String path, byte[] data) {
        createNode(path, data, false, false);
    }

    /**
     * Create node if absent without data.
     *
     * @param path the node path
     * @param data the data data
     * @throws ZKException
     */
    public void createNodeIfAbsent(String path) {
        createNodeIfAbsent(path, null);
    }

    /**
     * Create node if absent.
     *
     * @param path the node path
     * @param data the data data
     * @throws ZKException
     */
    public void createNodeIfAbsent(String path, byte[] data) {
        try {
            if (!exists(path)) {
                createNode(path, data, false, false);
            }
        } catch (ZKException e) {
            // The node may already exist.
            if (e.getCode() != ZKException.Code.NODEEXISTS) {
                throw e;
            }
        } catch (Exception e) {
            throw ZKException.makeInstance("Failed to create Node.", e);
        }
    }

    /**
     * Make sure that the specified path exists. We will create any parent path if necessary.
     *
     * @param path the specified path
     */
    public void makeSurePathExists(String path) {
        String[] seg = path.split("/");
        StringBuilder sb = new StringBuilder();
        for (String part : seg) {
            if (StringUtil.isBlank(part)) {
                continue;
            }
            sb.append("/").append(part);
            createNodeIfAbsent(sb.toString());
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
     * @throws ZKException
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
     * @throws ZKException
     */
    public void updateNodeData(String path, byte[] data) {
        try {
            zk.setData(path, data, -1);
        } catch (Exception e) {
            throw ZKException.makeInstance("Failed to update Node data.", e);
        }
    }

    /**
     * Delete a node from zookeeper.
     * This is very critical, so we only open this method for subclasses.
     *
     * @param path the node path
     * @throws ZKException
     */
    protected void deleteNode(String path) {
        try {
            zk.delete(path, -1);
        } catch (Exception e) {
            throw ZKException.makeInstance("Failed to delete Node.", e);
        }
    }

}
