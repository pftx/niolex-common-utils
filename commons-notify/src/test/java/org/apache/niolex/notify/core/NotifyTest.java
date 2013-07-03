/**
 * NotifyTest.java
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
package org.apache.niolex.notify.core;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.niolex.commons.util.DateTimeUtil;
import org.apache.niolex.notify.App;
import org.apache.niolex.notify.AppTest;
import org.apache.niolex.notify.ByteArray;
import org.apache.niolex.notify.NotifyListener;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2013-1-5
 */
public class NotifyTest {

    private static NotifyListener LI = new NotifyListener() {

        @Override
        public void onPropertyChange(byte[] key, byte[] value) {
            System.out.println("=onPropertyChange Key [" + new String(key) +
                    "] Value: " + new String(value));
        }

        @Override
        public void onDataChange(byte[] data) {
            System.out.println("====onDataChange data: " + new String(data));
        }
    };

    private static Notify NO;

    @BeforeClass
    public static void setUp() throws IOException {
        App.init(AppTest.URL, 10000);
        App.instance().makeSurePathExists("/notify/test/tmp");
        NO = App.instance().getNotify("/notify/test/tmp");
        NO.addListener(LI);
        NO.replaceProperty("permkey".getBytes(), "Client environment:os.name=Windows XP".getBytes());
        System.out.println("After --------- permkey -------------- change");
    }

    @AfterClass
    public static void shutdown() {
        boolean b = NO.removeListener(LI);
        assertTrue(b);
    }

    /**
     * Test method for {@link org.apache.niolex.notify.core.Notify#updateData(byte[])}.
     */
    @Test
    public void testUpdateData() {
        Notify notify = App.instance().getNotify("/notify/test/tmp");
        notify.updateData(("This is new data. Time: " + DateTimeUtil.formatDate2DateTimeStr()).getBytes());
    }

    /**
     * Test method for {@link org.apache.niolex.notify.core.Notify#deleteProperty(byte[])}.
     */
    @Test
    public void testDeleteProperty() {
        Notify notify = App.instance().getNotify("/notify/test/tmp");
        boolean b = notify.deleteProperty("tmpkey".getBytes());
        System.out.println("DeleteProperty res " + b);
    }

    /**
     * Test method for {@link org.apache.niolex.notify.core.Notify#replaceProperty(byte[], byte[])}.
     */
    @Test
    public void testReplaceProperty() {
        Notify notify = App.instance().getNotify("/notify/test/tmp");
        notify.replaceProperty("tmpkey".getBytes(), "Love this.".getBytes());
        System.out.println("After --------- tmpkey -------------- change");
    }

    /**
     * Test method for {@link org.apache.niolex.notify.core.Notify#onDataChange(byte[])}.
     */
    @Test
    public void testOnDataChange() {
        Notify notify = App.instance().getNotify("/notify/test/tmp");
        notify.onDataChange("Not yet implemented".getBytes());
    }

    /**
     * Test method for {@link org.apache.niolex.notify.core.Notify#onChildrenChange(java.util.List)}.
     */
    @Test
    public void testOnChildrenChange() {
        Notify notify = App.instance().getNotify("/notify/test/tmp");
        List<String> list = new ArrayList<String>();
        list.add("Good");
        list.add("Good Morning#");
        list.add("HXBlcm1rZXlDbGllbnQgZW52aXJvbm1lbnQ6b3MubmFtZT1XaW5kb3dzIFhQ");
        notify.onChildrenChange(list);
    }

    /**
     * Test method for {@link org.apache.niolex.notify.core.Notify#getProperty(byte[])}.
     */
    @Test
    public void testGetPropertyByteArray() {
        Notify notify = App.instance().getNotify("/notify/test/tmp");
        byte[] v = notify.getProperty("permkey".getBytes());
        assertEquals(new String(v), "Client environment:os.name=Windows XP");
    }

    /**
     * Test method for {@link org.apache.niolex.notify.core.Notify#getProperty(org.apache.niolex.notify.ByteArray)}.
     */
    @Test
    public void testGetPropertyByteArray1() {
        Notify notify = App.instance().getNotify("/notify/test/tmp");
        byte[] v = notify.getProperty(new ByteArray("permkey".getBytes()));
        assertEquals(new String(v), "Client environment:os.name=Windows XP");
    }

    /**
     * Test method for {@link org.apache.niolex.notify.core.Notify#getData()}.
     */
    @Test
    public void testGetData() {
        Notify notify = App.instance().getNotify("/notify/test/tmp");
        byte[] data = notify.getData();
        System.out.println("testGetData data: " + new String(data));
    }

    /**
     * Test method for {@link org.apache.niolex.notify.core.Notify#getProperties()}.
     */
    @Test
    public void testGetProperties() {
        Notify notify = App.instance().getNotify("/notify/test/tmp");
        notify.getProperties();
    }

}
