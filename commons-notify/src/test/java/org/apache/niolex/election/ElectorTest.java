/**
 * ElectorTest.java
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


import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.niolex.commons.concurrent.ThreadUtil;
import org.apache.niolex.commons.reflect.FieldUtil;
import org.apache.niolex.commons.test.MockUtil;
import org.apache.niolex.notify.AppTest;
import org.apache.niolex.zookeeper.core.ZKConnectorExceTest;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-12-6
 */
public class ElectorTest {

    private static Elector EL;
    private static String BS = "/election/zkc/tmp";
    private static Elector.Listener LI = new Elector.Listener() {

        @Override
        public void leaderChange(String address) {
            System.out.println("New leader address: " + address);
        }

        @Override
        public void runAsLeader() {
            System.out.println("Current Node Now run as Leader.");

        }};

    private Elector.Listener mocLi;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        EL = new Elector(AppTest.URL, 10000, BS, LI);
        if (EL.exists(BS))
            EL.deleteTree(BS);
        EL.makeSurePathExists(BS);
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        mocLi = mock(Elector.Listener.class);
        EL.register("localhost:" + MockUtil.randInt(1000, 10000));
    }

    @After
    public void tearDown() throws Exception {
        EL.giveUp();
    }

    @Test
    public void testElector() throws Exception {
        Elector el = new Elector(AppTest.URL, 10000, BS + "/", mocLi);
        el.register("中国人");
        verify(mocLi, times(1)).leaderChange(anyString());
        assertTrue(EL.giveUp());
        ThreadUtil.sleepAtLeast(100);

        verify(mocLi, times(1)).runAsLeader();
        verify(mocLi, times(1)).leaderChange(anyString());
    }

    @Test
    public void testRegister() throws Exception {
        assertFalse(EL.register("any-str"));
    }

    @Test
    public void testReconnected() throws Exception {
        String s = EL.getCurrentPath();
        EL.close();
        ZKConnectorExceTest.reconn(EL);
        String n = EL.getCurrentPath();
        System.out.println("Before: " + s + ", recovered: " + n);
        assertNotNull(s);
        assertNotNull(n);
        assertNotEquals(s, n);
    }
    
    @Test
    public void testGiveUp() throws Exception {
        assertTrue(EL.giveUp());
        assertFalse(EL.giveUp());
        assertNull(EL.getCurrentPath());
    }

    @Test
    public void testGetCurrentPath() throws Exception {
        assertTrue(EL.getCurrentPath().startsWith(BS));
    }

    @Test
    public void testOnDataChange() throws Exception {
        EL.onDataChange(null);
    }

    @Test
    public void testOnChildrenChange() throws Exception {
        EL.onChildrenChange(null);
        Elector el = new Elector(AppTest.URL, 10000, BS + "/", mocLi);
        el.createNode(BS + "/good-girl", "1st".getBytes(), true, false);
        el.createNode(BS + "/good-boy", "2nd".getBytes(), true, false);
        el.createNode(BS + "/bad-girl", "3rd".getBytes(), true, false);
        List<String> list = new ArrayList<String>();
        list.add("good-girl");
        list.add("good-boy");
        list.add("bad-girl");
        el.onChildrenChange(list);
        verify(mocLi, times(0)).runAsLeader();
        verify(mocLi, times(1)).leaderChange("3rd");

        list.remove(2);
        el.onChildrenChange(list);
        verify(mocLi, times(0)).runAsLeader();
        verify(mocLi, times(1)).leaderChange("3rd");
        verify(mocLi, times(1)).leaderChange("2nd");
        verify(mocLi, times(0)).leaderChange("1st");

        FieldUtil.setValue(el, "selfPath", BS + "/fake-p");
        list.add("fake-p");
        el.onChildrenChange(list);
        verify(mocLi, times(1)).runAsLeader();
        verify(mocLi, times(1)).leaderChange("3rd");
        verify(mocLi, times(1)).leaderChange("2nd");
        verify(mocLi, times(0)).leaderChange("1st");
    }

}
