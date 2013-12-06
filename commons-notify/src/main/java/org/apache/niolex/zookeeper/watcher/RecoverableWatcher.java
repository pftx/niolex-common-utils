/**
 * RecoverableWatcher.java
 *
 * Copyright 2013 Niolex, Inc.
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
package org.apache.niolex.zookeeper.watcher;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 * The interface extends the standard ZK watcher, and add a new
 * method for recover watcher when connection with ZK expired.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2013-3-13
 */
public interface RecoverableWatcher extends Watcher {

    /**
     * The watcher type. We have two types:<pre>
     *  DATA        - watch node data
     *  CHILDREN    - watch node children
     * <pre>
     * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
     * @version 1.0.0
     * @since 2013-12-5
     */
    public static enum Type {
        DATA, CHILDREN;
    }

    /**
     * The connection with ZK expired and reconnected. So watcher
     * need to attache it to the new connection.
     *
     * @param path the path to watch
     */
    public void reconnected(ZooKeeper zk, String path);

    /**
     * @return the type of this watcher
     */
    public Type getType();

}
