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
package org.apache.niolex.notify;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.niolex.commons.util.DateTimeUtil;
import org.apache.niolex.commons.util.SystemUtil;
import org.apache.niolex.notify.App;
import org.apache.niolex.notify.ByteArray;
import org.apache.niolex.notify.Notify;
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

    private static Notify.Listener LI = new Notify.Listener() {

        @Override
        public void onPropertyChange(byte[] key, byte[] value) {
            System.out.println("=onPropertyChange Key [" + new String(key) +
                    "] Value: " + new String(value));
        }

        @Override
        public void onDataChange(byte[] data) {
            System.out.println("=====onDataChange data: " + new String(data));
        }
    };

    private static Notify NO;
    private static final App APP = AppTest.APP;

    @BeforeClass
    public static void setUp() throws IOException {
        NO = APP.getNotify("/notify/test/tmp");
        NO.addListener(LI);
        String v = "Client environment:os.name=" + SystemUtil.getSystemProperty("os.name");
        NO.replaceProperty("permkey", v);
        NO.replaceProperty("love", "lex");
        System.out.println("After --------- permkey -------------- change");
    }

    @AfterClass
    public static void shutdown() {
        boolean b = NO.removeListener(LI);
        assertTrue(b);
    }

    /**
     * Test method for {@link org.apache.niolex.notify.Notify#updateData(byte[])}.
     */
    @Test
    public void testUpdateData() {
        NO.updateData(("This is new data. Time: " + DateTimeUtil.formatDate2DateTimeStr()).getBytes());
    }

    /**
     * Test method for {@link org.apache.niolex.notify.Notify#deleteProperty(byte[])}.
     */
    @Test
    public void testDeleteProperty() {
        Notify notify = App.instance().getNotify("/notify/test/tmp");
        boolean b = notify.deleteProperty("tmpkey");
        System.out.println("DeleteProperty res " + b);
    }

    /**
     * Test method for {@link org.apache.niolex.notify.Notify#deleteProperty(byte[])}.
     */
    @Test
    public void testDeletePropertyNotFound() {
        Notify notify = App.instance().getNotify("/notify/test/tmp");
        boolean b = notify.deleteProperty("tmpkey-lex-22");
        assertFalse(b);
    }

    /**
     * Test method for {@link org.apache.niolex.notify.Notify#replaceProperty(byte[], byte[])}.
     */
    @Test
    public void testReplaceProperty() {
        Notify notify = App.instance().getNotify("/notify/test/tmp");
        notify.replaceProperty("tmpkey".getBytes(), "Love this.".getBytes());
        System.out.println("After --------- tmpkey -------------- change");
    }

    /**
     * Test method for {@link org.apache.niolex.notify.Notify#onDataChange(byte[])}.
     */
    @Test
    public void testOnDataChange() {
        Notify notify = App.instance().getNotify("/notify/test/tmp");
        notify.onDataChange("Not yet implemented".getBytes());
    }

    /**
     * Test method for {@link org.apache.niolex.notify.Notify#onChildrenChange(java.util.List)}.
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
     * Test method for {@link org.apache.niolex.notify.Notify#getProperty(byte[])}.
     */
    @Test
    public void testGetPropertyBytes() {
        Notify notify = App.instance().getNotify("/notify/test/tmp");
        byte[] v = notify.getProperty("love".getBytes());
        assertEquals(new String(v), "lex");
    }

    /**
     * Test method for {@link org.apache.niolex.notify.Notify#getProperty(org.apache.niolex.notify.ByteArray)}.
     */
    @Test
    public void testGetPropertyByteArray() {
        Notify notify = App.instance().getNotify("/notify/test/tmp");
        byte[] v = notify.getProperty(new ByteArray("love".getBytes()));
        assertEquals(new String(v), "lex");
    }

    /**
     * Test method for {@link org.apache.niolex.notify.Notify#getProperty(org.apache.niolex.notify.ByteArray)}.
     */
    @Test
    public void testGetPropertyByteArrayStr() {
        Notify notify = App.instance().getNotify("/notify/test/tmp");
        String v = notify.getProperty("love");
        assertEquals(v, "lex");
    }

    /**
     * Test method for {@link org.apache.niolex.notify.Notify#getProperty(org.apache.niolex.notify.ByteArray)}.
     */
    @Test
    public void testGetPropertyByteArrayStrNull() {
        Notify notify = App.instance().getNotify("/notify/test/tmp");
        String v = notify.getProperty("love my best");
        assertNull(v);
    }

    /**
     * Test method for {@link org.apache.niolex.notify.Notify#getData()}.
     */
    @Test
    public void testGetData() {
        Notify notify = App.instance().getNotify("/notify/test/tmp");
        byte[] data = notify.getData();
        System.out.println("testGetData data: " + new String(data));
    }

    /**
     * Test method for {@link org.apache.niolex.notify.Notify#getProperties()}.
     */
    @Test
    public void testGetProperties() {
        Notify notify = App.instance().getNotify("/notify/test/tmp");
        System.out.println("testGetProperties data: " + notify.getProperties());
    }

}
