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


import org.apache.niolex.notify.AppTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-7-5
 */
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
    public void testZKConnector() throws Exception {
        ;
        System.out.println("not yet implemented");
    }

    @Test
    public void testAddAuthInfo() throws Exception {
        System.out.println("not yet implemented");
    }

    @Test
    public void testReconnect() throws Exception {
        System.out.println("not yet implemented");
    }

    @Test
    public void testSubmitWatcher() throws Exception {
        System.out.println("not yet implemented");
    }

    @Test
    public void testDoWatch() throws Exception {
        System.out.println("not yet implemented");
    }

    @Test
    public void testGetData() throws Exception {
        System.out.println("not yet implemented");
    }

    @Test
    public void testGetChildren() throws Exception {
        System.out.println("not yet implemented");
    }

    @Test
    public void testExists() throws Exception {
        System.out.println("not yet implemented");
    }

    @Test
    public void testCreateNodeString() throws Exception {
        System.out.println("not yet implemented");
    }

    @Test
    public void testCreateNodeStringByteArray() throws Exception {
        System.out.println("not yet implemented");
    }

    @Test
    public void testCreateNodeIfAbsentString() throws Exception {
        System.out.println("not yet implemented");
    }

    @Test
    public void testCreateNodeIfAbsentStringByteArray() throws Exception {
        System.out.println("not yet implemented");
    }

    @Test
    public void testMakeSurePathExists() throws Exception {
        System.out.println("not yet implemented");
    }

    @Test
    public void testCreateNodeStringByteArrayBooleanBoolean() throws Exception {
        System.out.println("not yet implemented");
    }

    @Test
    public void testDoCreateNode() throws Exception {
        System.out.println("not yet implemented");
    }

    @Test
    public void testUpdateNodeData() throws Exception {
        System.out.println("not yet implemented");
    }

    @Test
    public void testDeleteNode() throws Exception {
        System.out.println("not yet implemented");
    }

}
