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
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2016-4-14
 */
public class ZKLock extends DistributedLock implements Closeable {
    protected static final Logger LOG = LoggerFactory.getLogger(ZKLock.class);

    private static final String[] STUB = new String[0];

    private final ZKConnector zkc;
    private final String basePath;

    private volatile String selfPath = null;
    private volatile String watchPath = null;
    private volatile boolean locked = false;

    /**
     * The Constructor to create a {@link ZKConnector} inside it.
     *
     * @param clusterAddress the zookeeper cluster servers address list
     * @param sessionTimeout the zookeeper session timeout in microseconds
     * @param basePath the lock base path
     * @throws IOException in cases of network failure
     * @throws IllegalArgumentException if sessionTimeout is too small
     */
    public ZKLock(String clusterAddress, int sessionTimeout, String basePath) throws IOException {
        this(new ZKConnector(clusterAddress, sessionTimeout), basePath);
    }

    /**
     * The ZKLock Constructor.
     *
     * @param zkc the zookeeper connector
     * @param basePath the lock base path
     */
    public ZKLock(ZKConnector zkc, String basePath) {
        super();
        this.zkc = zkc;
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
        zkc.addAuthInfo(username, password);
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.lock.DistributedLock#initLock()
     */
    @Override
    protected void initLock() {
        if (locked) {
            throw new IllegalStateException("Lock not released, Please unlock it first.");
        }
        selfPath = zkc.createNode(basePath + "/lock-", null, true, true);
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
        if (locked) {
            return true;
        }

        List<String> children = zkc.getChildren(basePath);
        String self = makeChildPath(selfPath);

        // If only one, it's meat to be me.
        if (children.size() <= 1) {
            if (children.size() == 0 || !self.equals(children.get(0))) {
                throw new IllegalStateException("Invalid zookeeper data.");
            } else {
                LOG.debug("Lock acquired directly with path [{}].", selfPath);
                return locked = true;
            }
        }

        // Sort children.
        String[] arr = children.toArray(STUB);
        Arrays.sort(arr);

        if (self.equals(arr[0])) {
            // I am holding the lock.
            LOG.debug("Lock acquired as header with path [{}].", selfPath);
            return locked = true;
        } else {
            for (int i = 1; i < arr.length; ++i) {
                if (self.equals(arr[i])) {
                    watchPath = makeWholePath(arr[i - 1]);
                    LOG.debug("Lock is not ready, waiting for path [{}], self path [{}].", watchPath, selfPath);
                    return locked = false;
                }
            }
            throw new IllegalStateException("Invalid zookeeper data, current path not found.");
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
            Stat s = zkc.zooKeeper().exists(watchPath, new ExistsWather(latch));
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
            Stat s = zkc.zooKeeper().exists(watchPath, new ExistsWather(latch));
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
            zkc.deleteNode(selfPath);
            if (locked)
                LOG.debug("Lock released with path [{}].", selfPath);
            selfPath = null;
        }
        locked = false;
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
                    while (!zkc.connected())
                        zkc.waitForConnectedTillDeath();
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
        return locked;
    }

    /**
     * Close the internal zookeeper connector.
     */
    @Override
    public void close() {
        zkc.close();
    }

}
