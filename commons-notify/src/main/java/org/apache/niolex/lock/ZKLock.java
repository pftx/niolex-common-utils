/**
 * ZKLock.java
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
package org.apache.niolex.lock;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.niolex.zookeeper.core.ZKConnector;
import org.apache.niolex.zookeeper.core.ZKException;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The DistributedLock powered by Zookeeper.<br><b>
 * This implementation is non-reentrant, and you should not share it between different threads.</b>
 * Lock instance can be reused, call {@link #releaseLock()} and use it again. <br>If you call the constructor
 * {@link #ZKLock(String, int, String)} to create a new instance, you need to call the {@link #close()}
 * method on this instance to close the inner ZKConnector if you do not want to use this lock any more.
 * <br>If you call constructor {@link #ZKLock(ZKConnector, String)}, the ZKConnector instance will be managed
 * by you. Even if you call {@link #close()}, we will not close the ZKConnector instance.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2016-4-14
 */
public class ZKLock extends DistributedLock implements Closeable {
    protected static final Logger LOG = LoggerFactory.getLogger(ZKLock.class);

    private static final String[] STUB = new String[0];

    private final ZKConnector zkConn;
    private final String basePath;

    private String selfPath = null;
    private String watchPath = null;
    private boolean closeZKC = false;
    
    /**
     * The lock status:
     *          0 - not locked / lock released
     *          1 - lock initialized
     *          2 - lock acquired
     */
    private static final int NO_LOCK = 0;
    private static final int LOCK_INITIALIZED = 1;
    private static final int LOCKED = 2;
    private int lockStatus = NO_LOCK;

    /**
     * The Constructor to create a new ZKLock instance.
     * We will create a {@link ZKConnector} inside this method.
     *
     * @param clusterAddress the zookeeper cluster servers address list
     * @param sessionTimeout the zookeeper session timeout in microseconds
     * @param basePath the lock base path
     * @throws IOException in cases of network failure
     * @throws IllegalArgumentException if sessionTimeout is too small
     */
    public ZKLock(String clusterAddress, int sessionTimeout, String basePath)
            throws IOException, IllegalArgumentException {
        this(new ZKConnector(clusterAddress, sessionTimeout), basePath);
        closeZKC = true;
    }
    
    /**
     * The ZKLock Constructor.
     *
     * @param zkc the zookeeper connector
     * @param basePath the lock base path
     */
    public ZKLock(ZKConnector zkc, String basePath) {
        super();
        this.zkConn = zkc;
        if (basePath.endsWith("/")) {
            basePath = basePath.substring(0, basePath.length() - 1);
        }

        this.basePath = basePath;
        zkc.makeSurePathExists(basePath);
    }
    
    /**
     * Add authenticate info for this client.
     * 添加client的权限认证信息
     *
     * @param username the user name of this client
     * @param password the password of this client
     * @see org.apache.niolex.zookeeper.core.ZKConnector#addAuthInfo(java.lang.String, java.lang.String)
     */
    public void addAuthInfo(String username, String password) {
        zkConn.addAuthInfo(username, password);
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.lock.DistributedLock#initLock()
     */
    @Override
    protected void initLock() {
        if (lockStatus > NO_LOCK) {
            throw new IllegalStateException("Lock not released, Please unlock it first.");
        }
        selfPath = zkConn.createNode(basePath + "/lock-", null, true, true);
        lockStatus = LOCK_INITIALIZED;
    }

    /**
     * Make whole path from the specified child path.
     *
     * @param child the child path
     * @return the whole path
     */
    protected String makeWholePath(String child) {
        return basePath + "/" + child;
    }

    /**
     * Make child path from the specified whole path.
     *
     * @param wholePath the whole path
     * @return the child path
     */
    protected String makeChildPath(String wholePath) {
        return wholePath.substring(wholePath.lastIndexOf('/') + 1);
    }

    /**
     * Check the current lock status, and set the {@link #watchPath} properly.
     *
     * @return true if we got the lock
     */
    @Override
    protected boolean isLockReady() {
        // Invalid usage.
        if (selfPath == null) {
            throw new IllegalStateException("Lock not initialized or already released.");
        }

        // Already locked.
        if (lockStatus == LOCKED) {
            return true;
        }

        List<String> children = zkConn.getChildren(basePath);
        String self = makeChildPath(selfPath);

        // Sort children.
        String[] arr = children.toArray(STUB);
        Arrays.sort(arr);

        // Find myself.
        int selfIndex = Arrays.binarySearch(arr, self);
        
        if (selfIndex < 0) {
            // Self not found. We need to re-init this lock. This maybe due to session expired...
            lockStatus = NO_LOCK;
            initLock();
            return isLockReady();
        } else if (selfIndex == 0) {
            // Lock acquired. I am holding the lock.
            LOG.debug("Lock acquired as header with path [{}].", selfPath);
            lockStatus = LOCKED;
            return true;
        } else {
            watchPath = makeWholePath(arr[selfIndex - 1]);
            LOG.debug("Lock is not ready, waiting for path [{}], self path [{}].", watchPath, selfPath);
            return false;
        }
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.lock.DistributedLock#watchLock()
     */
    @Override
    protected void watchLock() throws InterruptedException {
        if (watchPath == null) {
            throw new IllegalStateException("Should not call this method at this moment.");
        }

        try {
            CountDownLatch latch = new CountDownLatch(1);
            Stat s = zkConn.zooKeeper().exists(watchPath, new ExistsWather(latch));
            if (s == null) {
                return;
            } else {
                latch.await();
            }
        } catch (KeeperException e) {
            throw ZKException.makeInstance("ZKLock internal error.", e);
        }
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.lock.DistributedLock#watchLock(long, java.util.concurrent.TimeUnit)
     */
    @Override
    protected boolean watchLock(long timeout, TimeUnit unit) throws InterruptedException {
        if (watchPath == null) {
            throw new IllegalStateException("Should not call this method at this moment.");
        }

        try {
            CountDownLatch latch = new CountDownLatch(1);
            Stat s = zkConn.zooKeeper().exists(watchPath, new ExistsWather(latch));
            if (s == null) {
                return true;
            } else {
                return latch.await(timeout, unit);
            }
        } catch (KeeperException e) {
            throw ZKException.makeInstance("ZKLock internal error.", e);
        }
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.lock.DistributedLock#releaseLock()
     */
    @Override
    protected void releaseLock() {
        if (selfPath != null) {
            zkConn.deleteNode(selfPath);
            if (lockStatus == LOCKED)
                LOG.debug("Lock released with path [{}].", selfPath);
        }
        lockStatus = NO_LOCK;
        selfPath = null;
        watchPath = null;
    }

    /**
     * Check the exists of the watched node.
     *
     * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
     * @version 1.0.0
     * @since 2016-4-14
     */
    public class ExistsWather implements Watcher {

        // The latch used to wait for the lock to be ready.
        private final CountDownLatch latch;

        /**
         * Constructor.
         *
         * @param latch the latch used to notify event
         */
        public ExistsWather(CountDownLatch latch) {
            super();
            this.latch = latch;
        }

        /**
         * This is the override of super method.
         * @see org.apache.zookeeper.Watcher#process(org.apache.zookeeper.WatchedEvent)
         */
        @Override
        public void process(WatchedEvent event) {
            // Each watch can be triggered only once.
            // We need to make sure do this thing right.
            try {
                if (event.getType() == Watcher.Event.EventType.None) {
                    // If event type is none, then something wrong with the zookeeper.
                    while (!zkConn.connected())
                        zkConn.waitForConnectedTillDeath();
                }
            } finally {
                latch.countDown();
            }
        }

    }

    /**
     * @return the current lock status
     */
    public boolean locked() {
        return lockStatus == LOCKED;
    }

    /**
     * Close the internal zookeeper connector if it's created by this class, otherwise just release this lock.
     */
    @Override
    public void close() {
        if (closeZKC) {
            zkConn.close();
        } else {
            releaseLock();
        }
    }
}
