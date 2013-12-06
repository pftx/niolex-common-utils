/**
 * ZKConnectorTest.java
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
package org.apache.niolex.zookeeper.core;


import static org.junit.Assert.*;

import org.apache.niolex.commons.test.AnnotationOrderedRunner;
import org.apache.niolex.commons.test.AnnotationOrderedRunner.Order;
import org.apache.niolex.notify.AppTest;
import org.apache.niolex.zookeeper.watcher.RecoverableWatcher;
import org.apache.zookeeper.WatchedEvent;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-7-5
 */
@RunWith(AnnotationOrderedRunner.class)
public class ZKConnectorTest {

    private static ZKConnector ZKC;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        ZKC = new ZKConnector(AppTest.URL, 10000);
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        ZKC.close();
    }

    @Test
    @Order(-110)
    public void testMakeSurePathExists() throws Exception {
        ZKC.makeSurePathExists("/notify/zkc/tmp");
    }

    String path = null;
    int cnt = 0;
    RecoverableWatcher watData = new RecoverableWatcher() {

        @Override
        public void process(WatchedEvent event) {
            System.out.println("WatchedEvent " + event);
        }

        @Override
        public void reconnected() {
            System.out.println("reconnected.");
        }

        @Override
        public Type getType() {
            return Type.DATA;
        }};

    RecoverableWatcher watChildren = new RecoverableWatcher() {

        @Override
        public void process(WatchedEvent event) {
            System.out.println("WatchedEvent " + event);
        }

        @Override
        public void reconnected() {
            System.out.println("reconnected.");
        }

        @Override
        public Type getType() {
            return Type.CHILDREN;
        }};

    @Order(-2)
    @Test(expected=ZKException.class)
    public void testSubmitWatcherData() throws Exception {
        ZKC.submitWatcher("/a/b/cv", watData);
    }

    @Test(expected=ZKException.class)
    @Order(-1)
    public void testSubmitWatcherChildren() throws Exception {
            ZKC.submitWatcher("/a/b/cv", watChildren);
    }

    @Test
    @Order(1)
    public void testSubmitC() throws Exception {
        ZKC.submitWatcher("/notify/zkc/tmp", watChildren);
    }

    @Test
    @Order(1)
    public void testSubmitD() throws Exception {
        ZKC.submitWatcher("/notify/zkc/tmp", watData);
    }

    @Test
    @Order(2)
    public void testCreateNodeStringByteArrayBooleanBoolean() throws Exception {
        ZKC.createNode("/notify/zkc/tmp/T1322", null, true, false);
        ZKC.createNode("/notify/zkc/tmp/T1323", null, true, true);
    }

    @Test
    @Order(3)
    public void testDoCreateNode() throws Exception {
        ZKC.createNode("/notify/zkc/tmp/P12", null, false, false);
        path = ZKC.createNode("/notify/zkc/tmp/P13", null, false, true);
    }

    @Test
    @Order(4)
    public void testGetData() throws Exception {
        ZKC.getData("/notify/zkc/tmp");
    }

    @Order(5)
    @Test
    public void testGetChildren() throws Exception {
        ZKC.getChildren("/notify/zkc");
    }

    @Test
    @Order(6)
    public void testExists() throws Exception {
        assertFalse(ZKC.exists("/a/b/c"));
    }

    @Test
    @Order(6)
    public void testExistsT() throws Exception {
        assertTrue(ZKC.exists("/notify/zkc"));
    }

    @Test
    @Order(7)
    public void testCreateNodeString() throws Exception {
        ZKC.createNode("/notify/zkc/tmp/CRN001");
    }

    @Test
    @Order(7)
    public void testCreateNodeStringByteArray() throws Exception {
        ZKC.createNode("/notify/zkc/tmp/CRN002", new byte[] {1, 2, 3});
    }

    @Test(expected=ZKException.class)
    @Order(7)
    public void testCreateNodeStringByteArrayEx() throws Exception {
        ZKC.createNode("/a/b/c", new byte[] {1, 2, 3});
    }

    @Test
    @Order(8)
    public void testCreateNodeIfAbsentString() throws Exception {
        ZKC.createNodeIfAbsent("/notify");
    }

    @Test
    @Order(8)
    public void testCreateNodeIfAbsent() throws Exception {
        assertTrue(ZKC.createNodeIfAbsent("/notify/zkc/tmp/P13", null));
    }

    @Test
    @Order(9)
    public void testCreateNodeIfAbsentT() throws Exception {
        assertFalse(ZKC.createNodeIfAbsent("/notify/zkc/tmp", "Yes, Good.".getBytes()));
    }

    @Test(expected=ZKException.class)
    @Order(9)
    public void testCreateNodeIfAbsentStringByteArray() throws Exception {
        assertFalse(ZKC.createNodeIfAbsent("/a/b/c", new byte[] {1, 2, 3}));
    }

    @Test
    @Order(17)
    public void testUpdateNodeData() throws Exception {
        ZKC.updateNodeData("/notify/zkc/tmp", "not".getBytes());
    }

    @Test(expected=IllegalArgumentException.class)
    @Order(20)
    public void testDeleteTree() throws Exception {
        ZKC.deleteTree("iejd/no/tify/zkc");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testZKConnector() throws Exception {
        new ZKConnector(AppTest.URL, 1);
    }

    @Test
    public void testDoWatch() throws Exception {
        Object r = ZKC.getChildren("/");
        System.out.println("/[] = " + r);
    }

    @Test
    @Order(40)
    public void testReconnect() throws Exception {
        ZKC.close();
        ZKC.close();
        ZKC.reconnect();
    }

    @Test
    @Order(50)
    public void testAddAuthInfo() throws Exception {
        ZKC.addAuthInfo("LEX", "password");
    }

    @Test
    @Order(7009)
    public void testDeleteNodeFinal() throws Exception {
        try {ZKC.deleteNode(path);} catch(Exception e){}
        try {ZKC.deleteNode("/notify/zkc/tmp/P13");} catch(Exception e){}
        try {ZKC.deleteNode("/notify/zkc/tmp/P12");} catch(Exception e){}
        try {ZKC.deleteTree("/notify/zkc/tmp");} catch(Exception e){}
        try {ZKC.deleteTree("/notify/zkc");} catch(Exception e){}
    }

}
