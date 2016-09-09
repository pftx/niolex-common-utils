/**
 * ZookeeperMain.java
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
package org.apache.niolex.zookeeper.core;

import static org.junit.Assert.assertArrayEquals;

import org.apache.niolex.commons.concurrent.ThreadUtil;
import org.apache.niolex.notify.AppTest;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 2.1.3
 * @since Sep 7, 2016
 */
public class ZookeeperMain {

    public static void main(String[] args) throws Exception {
        String BS = "/notify/zkc/main";
        ZKConnector ZKC = new ZKConnector(AppTest.URL, 6000);
        ZooKeeper zk = ZKC.zk;
        if (ZKC.exists(BS)) {
            ZKC.deleteTree(BS);
        }
        ZKC.makeSurePathExists(BS);
        String path = BS + "/test.txt";
        ZKC.createNode(path, "This is the data of test.txt");
        Watcher watcher = new Watcher() {

            @Override
            public void process(WatchedEvent event) {
                System.out.println(" X -> " + event);
            }
        };

        byte[] data1 = zk.getData(BS + "/test.txt", watcher, new Stat());
        byte[] data2 = zk.getData(BS + "/test.txt", watcher, new Stat());

        assertArrayEquals(data1, data2);

        ZKC.updateNodeData(path, "nothing.");

        ThreadUtil.sleep(200);
        ZKC.deleteNode(path);

        ThreadUtil.sleep(200);
        ZKC.deleteTree(BS);

        ThreadUtil.sleep(200);
        ZKC.close();
        ThreadUtil.sleep(200);
    }
}
