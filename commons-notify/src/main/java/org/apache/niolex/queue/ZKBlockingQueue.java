/**
 * ZKBlockingQueue.java
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
package org.apache.niolex.queue;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.niolex.commons.bean.BeanUtil;
import org.apache.niolex.commons.collection.CollectionUtil;
import org.apache.niolex.zookeeper.core.ZKConnector;
import org.apache.niolex.zookeeper.core.ZKException;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The distributed blocking queue implementation powered by Zookeeper.<br>If you call the constructor
 * {@link #ZKBlockingQueue(String, int, String)} to create a new instance, you need to call the {@link #close()}
 * method on this instance to close the inner ZKConnector when you do not want to use this lock any more.
 * <br>If you call constructor {@link #ZKBlockingQueue(ZKConnector, String)}, the ZKConnector instance will be managed
 * by you. Even if you call {@link #close()}, we will not close the ZKConnector instance.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2016-4-19
 */
public class ZKBlockingQueue<E extends Serializable> extends DistributedBlockingQueue<E> implements Closeable {
    protected static final Logger LOG = LoggerFactory.getLogger(ZKBlockingQueue.class);
    protected static final String PREFIX = "/zkbq-";

    private static final String[] STUB = new String[0];

    // Used to store current parsed items.
    private final LinkedBlockingQueue<String> itemQueue = new LinkedBlockingQueue<String>();
    private final ZKConnector zkc;
    private final String basePath;
    
    private boolean closeZKC = false;

    /**
     * The Constructor to create a {@link ZKConnector} inside it.
     *
     * @param clusterAddress the zookeeper cluster servers address list
     * @param sessionTimeout the zookeeper session timeout in microseconds
     * @param basePath the queue base path
     * @throws IOException in cases of network failure
     * @throws IllegalArgumentException if sessionTimeout is too small
     */
    public ZKBlockingQueue(String clusterAddress, int sessionTimeout, String basePath) throws IOException {
        this(new ZKConnector(clusterAddress, sessionTimeout), basePath);
        closeZKC = true;
    }

    /**
     * The ZKBlockingQueue Constructor.
     *
     * @param zkc the zookeeper connector
     * @param basePath the queue base path
     */
    public ZKBlockingQueue(ZKConnector zkc, String basePath) {
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
     * @see java.util.concurrent.BlockingQueue#offer(java.lang.Object)
     */
    @Override
    public boolean offer(E e) {
        byte[] data;
        try {
            data = BeanUtil.toBytes(e);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
        zkc.createNode(basePath + PREFIX, data, false, true);
        return true;
    }

    /**
     * Get children of the base path, No sort.
     *
     * @return the children
     */
    protected List<String> getChildren() {
        return zkc.getChildren(basePath);
    }

    /**
     * Get the real data from ZK and store them in the {@link #itemQueue}. We do sort here.
     *
     * @param children the children of the base path
     */
    protected void parseDataFromZK(List<String> children) {
        // Sort itemArray.
        String[] data = children.toArray(STUB);
        Arrays.sort(data);

        for (String s : data) {
            itemQueue.offer(s);
        }
    }

    /**
     * This is the override of super method.
     * @see java.util.Queue#peek()
     */
    @Override
    public E peek() {
        return getHead(false);
    }

    /**
     * This is the override of super method.
     * @see java.util.Queue#poll()
     */
    @Override
    public E poll() {
        return getHead(true);
    }

    /**
     * Get the current queue head. Remove it from queue if the parameter <tt>removeItem</tt>  is {@code true}.
     *
     * @param removeItem whether we need to remove the item from the queue or not
     * @return the head item or null if queue is empty
     */
    public E getHead(boolean removeItem) {
        boolean queryZK = false;
        String child = null;
        byte[] arr = null;

        while (true) {
            // Get item from queue.
            if (removeItem)
                child = itemQueue.poll();
            else
                child = itemQueue.peek();

            // Get item from ZK only once.
            if (child == null) {
                if (!queryZK) {
                    queryZK = true;
                    parseDataFromZK(getChildren());
                    // After query, poll queue again.
                    continue;
                } else {
                    return null;
                }
            }

            // Make whole path.
            String path = basePath + "/" + child;

            // Get data from ZK.
            try {
                arr = zkc.getData(path);
                if (removeItem)
                    zkc.deleteNode(path);

                // We got the data.
                break;
            } catch (ZKException zk) {
                if (zk.getCode() != ZKException.Code.NO_NODE) {
                    throw zk;
                } else if (!removeItem) {
                    // Item already removed from ZK, we need to remove it from itemQueue.
                    itemQueue.remove(child);
                }
            }
        }

        // De-serialize data to object.
        if (arr == null) {
            return null;
        } else {
            try {
                @SuppressWarnings("unchecked")
                E o = (E) BeanUtil.toObject(arr);
                return o;
            } catch (Exception e) {
                throw new IllegalStateException("Failed to deserialize object for " + child, e);
            }
        }
    }


    /**
     * This is the override of super method.
     * @see org.apache.niolex.queue.DistributedBlockingQueue#watchQueue()
     */
    @Override
    protected void watchQueue() throws InterruptedException {
        try {
            CountDownLatch latch = new CountDownLatch(1);
            List<String> children = zkc.zooKeeper().getChildren(basePath, new ChildrenChangeWather(latch));

            if (!CollectionUtil.isEmpty(children)) {
                parseDataFromZK(children);
                return;
            } else {
                latch.await();
            }
        } catch (KeeperException e) {
            throw ZKException.makeInstance("ZKBlockingQueue internal error.", e);
        }
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.queue.DistributedBlockingQueue#watchQueue(long, java.util.concurrent.TimeUnit)
     */
    @Override
    protected boolean watchQueue(long timeout, TimeUnit unit) throws InterruptedException {
        try {
            CountDownLatch latch = new CountDownLatch(1);
            List<String> children = zkc.zooKeeper().getChildren(basePath, new ChildrenChangeWather(latch));

            if (!CollectionUtil.isEmpty(children)) {
                parseDataFromZK(children);
                return true;
            } else {
                return latch.await(timeout, unit);
            }
        } catch (KeeperException e) {
            throw ZKException.makeInstance("ZKBlockingQueue internal error.", e);
        }
    }

    /**
     * Check the children changes of the base path.
     *
     * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
     * @version 1.0.0
     * @since 2016-4-21
     */
    public class ChildrenChangeWather implements Watcher {

        // The latch used to wait for the children changes.
        private final CountDownLatch latch;

        /**
         * Constructor.
         *
         * @param latch the latch used to notify event
         */
        public ChildrenChangeWather(CountDownLatch latch) {
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
     * This is the override of super method.
     * @see java.util.AbstractCollection#size()
     */
    @Override
    public int size() {
        return getChildren().size();
    }

    /**
     * Close the internal zookeeper connector if and only if it's created by this class.
     * 
     * This is the override of super method.
     * @see java.io.Closeable#close()
     */
    @Override
    public void close() {
        if (closeZKC) {
            zkc.close();
        }
    }

}
