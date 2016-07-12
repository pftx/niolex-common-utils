/**
 * WatcherSet.java
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
package org.apache.niolex.zookeeper.watcher;

import java.util.HashSet;
import java.util.Set;

/**
 * The container to hold all the watchers in one zookeeper connector.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-12-5
 */
public class WatcherHolder {

    // The watcher set.
    Set<RecoverableWatcher> watcherSet = new HashSet<RecoverableWatcher>();

    /**
     * Re add all the watcher to this zookeeper.
     */
    public synchronized void reconnected() {
        for (RecoverableWatcher recoWatcher : watcherSet) {
            recoWatcher.reconnected();
        }
    }

    /**
     * Add this watcher into the holder.
     *
     * @param recoWatcher the watcher
     */
    public synchronized void add(RecoverableWatcher recoWatcher) {
        watcherSet.add(recoWatcher);
    }

}
