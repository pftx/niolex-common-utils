/**
 * Election.java
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
package org.apache.niolex.election;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.collection.CollectionUtil;
import org.apache.niolex.zookeeper.core.ZKConnector;
import org.apache.niolex.zookeeper.core.ZKListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Elector is for P2P nodes elect one leader.
 * We use zookeeper for sequential order. The first registered node will
 * be elected as leader. If this leader goes down, the second one will
 * take his place, and so on.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-12-5
 */
public class Elector extends ZKConnector implements ZKListener {
    protected static final Logger LOG = LoggerFactory.getLogger(Elector.class);

    /**
     * Listen to the changes of elect leader.
     *
     * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
     * @version 1.0.0
     * @since 2013-12-6
     */
    public static interface Listener {

        /**
         * Leader changed to this new address.
         *
         * @param address the new leader
         */
        public void leaderChange(String address);

        /**
         * The current node need to run as new leader.
         */
        public void runAsLeader();

    }

    /**
     * The election base path. This path is guaranteed not end with "/"
     */
    private final String basePath;

    /**
     * The current listener.
     */
    private final Listener listn;

    /**
     * The absolute path of the self node path.
     */
    private volatile String selfPath;

    /**
     * The current leader absolute path.
     */
    private volatile String leaderPath;

    /**
     * Create a new Elector under this bash path.<br>
     * User need to call {@link #register(String)} to register this node if he
     * want to elect the leader.
     *
     * @param clusterAddress the zookeeper cluster servers address list
     * @param sessionTimeout the session timeout in microseconds
     * @param basePath the ZK base path of this elector
     * @param listn the leader change listener
     * @throws IOException in cases of network failure
     * @throws IllegalArgumentException if sessionTimeout is too small
     */
    public Elector(String clusterAddress, int sessionTimeout,
            String basePath, Listener listn) throws IOException {
        super(clusterAddress, sessionTimeout);
        if (basePath.endsWith("/")) {
            basePath = basePath.substring(0, basePath.length() - 1);
        }
        this.basePath = basePath;
        this.listn = listn;
    }

    /**
     * Register this address to the election. Please note that we can only
     * manage one address here. So do not call this method multiple times.
     *
     * @param address the address of this node
     * @return true if this address is registered, false if this elector already has another address
     */
    public synchronized boolean register(String address) {
        if (selfPath != null) {
            return false;
        }
        selfPath = this.createNode(basePath + "/Elector-", StringUtil.strToUtf8Byte(address), true, true);
        LOG.info("A new Elector join with path: {}.", selfPath);
        this.onChildrenChange(watchChildren(basePath, this));
        return true;
    }

    /**
     * Give up the current node. That means we don't want to elect the leader any more.
     *
     * @return true if give up success, false if not registered
     */
    public synchronized boolean giveUp() {
        if (selfPath == null) {
            return false;
        }
        this.deleteNode(selfPath);
        LOG.info("The current node: {} now given up.", selfPath);
        selfPath = null;
        return true;
    }

    /**
     * @return the current node zoopeeker path
     */
    public synchronized String getCurrentPath() {
        return selfPath;
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.zookeeper.core.ZKListener#onDataChange(byte[])
     */
    @Override
    public void onDataChange(byte[] data) {
        // We only care about children
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.zookeeper.core.ZKListener#onChildrenChange(java.util.List)
     */
    @Override
    public synchronized void onChildrenChange(List<String> list) {
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        Collections.sort(list);
        // The absolute leader path.
        String leader = basePath + "/" + list.get(0);
        if (leader.equals(leaderPath)) {
            return;
        } else {
            if (leader.equals(selfPath)) {
                LOG.info("The current node is now run as the leader.");
                listn.runAsLeader();
            } else {
                String address = this.getDataAsStr(leader);
                LOG.info("The leader address is changed to: {}.", address);
                listn.leaderChange(address);
            }
            leaderPath = leader;
        }
    }

}