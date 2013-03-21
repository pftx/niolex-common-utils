/**
 * ZKConnWatcher.java
 *
 * Copyright 2012 Niolex, Inc.
 *
 * Niolex licenses this file to you under the Apache License, version 2.0
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
package org.apache.niolex.notify.core;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Watch connection status, try to reconnect if connection is broken.
 * 
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, Date: 2012-6-18
 */
public class ZKConnWatcher implements Watcher {

    protected static final Logger LOG = LoggerFactory.getLogger(ZKConnWatcher.class);
    
    private ZKConnector conn;
    private CountDownLatch latch;

	/**
     * @param conn
     * @param latch use it to count down when connected.
     */
    public ZKConnWatcher(ZKConnector conn, CountDownLatch latch) {
        super();
        this.conn = conn;
        this.latch = latch;
    }

    /**
	 * Override super method
	 * @see org.apache.zookeeper.Watcher#process(org.apache.zookeeper.WatchedEvent)
	 */
	@Override
	public void process(WatchedEvent event) {
		LOG.info("ZK Connection status: " + event.getState());
		switch (event.getState()) {
            case SyncConnected:
                latch.countDown();
                break;
            case Expired:
                conn.reconnect();
                break;
        }
	}

}
