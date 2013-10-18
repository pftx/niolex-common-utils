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
package org.apache.niolex.notify.core;


import static org.junit.Assert.assertFalse;

import org.apache.niolex.commons.test.AnnotationOrderedRunner;
import org.apache.niolex.commons.test.AnnotationOrderedRunner.Order;
import org.apache.niolex.notify.AppTest;
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

    @Test(expected=IllegalArgumentException.class)
    public void testZKConnector() throws Exception {
        new ZKConnector(AppTest.URL, 1);
    }

    @Test
    public void testAddAuthInfo() throws Exception {
        ZKC.addAuthInfo("LEX", "password");
    }

    @Test(expected=ZKException.class)
    public void testSubmitWatcher() throws Exception {
        RecoverableWatcher wat = new RecoverableWatcher() {

            @Override
            public void process(WatchedEvent event) {
                System.out.println("WatchedEvent " + event);
            }

            @Override
            public void reconnected(String path) {
                System.out.println("reconnected " + path);
            }};
        ZKC.submitWatcher("/a/b/cv", wat, false);
    }

    @Test
    public void testDoWatch() throws Exception {
        Object r = ZKC.getChildren("/");
        System.out.println("/[] = " + r);
    }

    @Test(expected=ZKException.class)
    public void testGetData() throws Exception {
        ZKC.getData("/a/b/c");
    }

    @Test(expected=ZKException.class)
    public void testGetChildren() throws Exception {
        ZKC.getChildren("/a/b/c");
    }

    @Test
    public void testExists() throws Exception {
        assertFalse(ZKC.exists("/a/b/c"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateNodeString() throws Exception {
        assertFalse(ZKC.exists("a/b/c"));
    }

    @Test(expected=ZKException.class)
    public void testCreateNodeStringByteArray() throws Exception {
        ZKC.createNode("/a/b/c", new byte[] {1, 2, 3});
    }

    @Test
    public void testCreateNodeIfAbsentString() throws Exception {
        ZKC.createNodeIfAbsent("/notify");
    }

    @Test(expected=ZKException.class)
    public void testCreateNodeIfAbsentStringByteArray() throws Exception {
        assertFalse(ZKC.createNodeIfAbsent("/a/b/c", new byte[] {1, 2, 3}));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testUpdateNodeDataE() throws Exception {
        ZKC.updateNodeData("tify/zkc/tmp", "not".getBytes());
    }

    @Test
    @Order(0)
    public void testMakeSurePathExists() throws Exception {
        ZKC.makeSurePathExists("/notify/zkc/tmp");
    }

    String path = null;
    int cnt = 0;

    RecoverableWatcher watC = new NotifyChildrenWatcher(ZKC,
            new Notify(ZKC, "/notify/zkc/tmp"));
    RecoverableWatcher watD = new NotifyDataWatcher(ZKC,
            new Notify(ZKC, "/notify/zkc/tmp"));

    @Order(1)
    public void testSubmit() throws Exception {
        RecoverableWatcher wat = new RecoverableWatcher() {

            @Override
            public void process(WatchedEvent event) {
                System.out.println("WatchedEvent " + event);
                ++cnt;
            }

            @Override
            public void reconnected(String path) {
                System.out.println("reconnected " + path);
            }};
        ZKC.submitWatcher("/notify/zkc/tmp", wat, true);
        ZKC.submitWatcher("/notify/zkc/tmp", watC, true);
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
    public void testReconnect() throws Exception {
        ZKC.close();
        ZKC.close();
        ZKC.reconnect();
        watC.reconnected("/notify/zkc/tmp");
        watD.reconnected("/notify/zkc/tmp");
    }

    @Test
    @Order(5)
    public void testUpdateNodeData() throws Exception {
        ZKC.updateNodeData("/notify/zkc/tmp", "not".getBytes());
    }

    @Test
    @Order(6)
    public void testCreateNodeIfAbsent() throws Exception {
        ZKC.createNodeIfAbsent("/notify/zkc/tmp/P13", null);
    }

    @Test
    @Order(7)
    public void testDeleteNode() throws Exception {
        try {ZKC.deleteNode(path);} catch(Exception e){}
        try {ZKC.deleteNode("/notify/zkc/tmp/P13");} catch(Exception e){}
        try {ZKC.deleteNode("/notify/zkc/tmp/P12");} catch(Exception e){}
        try {ZKC.deleteNode("/notify/zkc/tmp");} catch(Exception e){}
        try {ZKC.deleteNode("/notify/zkc");} catch(Exception e){}
    }

}
