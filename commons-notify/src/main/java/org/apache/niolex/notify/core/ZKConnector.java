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
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The main Zookeeper connector, manage zookeeper and retry connection.
 *
 * @author Xie, Jiyun
 * @version 1.0.0, Date: 2012-6-10
 */
public class ZKConnector {

    protected static final Logger LOG = LoggerFactory.getLogger(ZKConnector.class);

    private final String clusterAddress;

    private final Set<WatcherItem> watcherSet = Collections.synchronizedSet(new HashSet<WatcherItem>());

    private final int sessionTimeout;

    private byte[] auth;

    protected ZooKeeper zk;

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
        try {
            auth = (username + ":" + password).getBytes("utf8");
            this.zk.addAuthInfo("digest", auth);
        } catch (UnsupportedEncodingException e) {
            LOG.error("Failed to add auth info because your jdk doesn't support utf8.", e);
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

    /**
     * Try to reconnect to zookeeper cluster.
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
                try {
                    Thread.sleep(sessionTimeout / 3);
                } catch (Exception e1) { }
            }
        }
    }

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
    protected <T> T submitWatcher(String path, RecoverableWatcher wat, boolean isChildren) {
        WatcherItem item = new WatcherItem(path, wat, isChildren);
        Object r = doWatch(item);
        // Add this item to the list, so the system will
        // add them after reconnected.
        watcherSet.add(item);
        return (T)r;
    }

    /**
     * Do real watch. Please use submitWatcher instead. This method is for internal use.
     * 
     * @param item the item to do watch
     * @return the current data
     */
    private Object doWatch(WatcherItem item) {
        try {
            if (item.isChildren()) {
                return this.zk.getChildren(item.getPath(), item.getWat());
            } else {
                Stat st = new Stat();
                return this.zk.getData(item.getPath(), item.getWat(), st);
            }
        } catch (Exception e) {
            throw NotifyException.makeInstance("Failed to do Watch.", e);
        }
    }
    
    /**
     * Create node.
     *
     * @param path
     * @throws NotifyException
     */
    protected void createNode(String path) {
        createNode(path, null, false, false);
    }
    
    /**
     * Create node.
     *
     * @param path
     * @param data
     * @throws NotifyException
     */
    protected void createNode(String path, byte[] data) {
        createNode(path, data, false, false);
    }

    /**
     * Create node.
     *
     * @param path
     * @param data
     * @param isTmp
     * @param isSequential
     * @return the actual path of the created node
     * @throws NotifyException
     */
    protected String createNode(String path, byte[] data, boolean isTmp, boolean isSequential) {
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
            throw NotifyException.makeInstance("Failed to create Node.", e);
        }
    }

    /**
     * Do create a new ZK node.
     * 
     * @param path
     * @param data
     * @param createMode
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
     * @param path
     * @param data
     * @throws NotifyException
     */
    protected void updateNodeData(String path, byte[] data) {
        try {
            zk.setData(path, data, -1);
        } catch (Exception e) {
            throw NotifyException.makeInstance("Failed to update Node data.", e);
        }
    }
    
    /**
     * Delete a node from zookeeper.
     * This is very important, so we only open this method for subclasses.
     * 
     * @param path
     * @throws NotifyException
     */
    protected void deleteNode(String path) {
        try {
            zk.delete(path, -1);
        } catch (Exception e) {
            throw NotifyException.makeInstance("Failed to delete Node.", e);
        }
    }

}
