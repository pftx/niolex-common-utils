/**
 * ZKExceptionTest.java
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

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.niolex.commons.test.MockUtil;
import org.apache.niolex.commons.util.SystemUtil;
import org.apache.niolex.notify.AppTest;
import org.apache.niolex.notify.NotifyListener;
import org.apache.zookeeper.KeeperException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2013-3-13
 */
public class ZKExceptionTest {

    private static NotifyListener LI = new NotifyListener() {

        @Override
        public void onPropertyChange(byte[] key, byte[] value) {
            System.out.println("[on] [" + new String(key) +
                    "] ==> " + new String(value));
        }

        @Override
        public void onDataChange(byte[] data) {
            System.out.println("[on]DataChange ==> " + new String(data));
        }
    };

    private static Notify notify;

    @BeforeClass
    public static void setUp() throws IOException {
        notify = AppTest.APP.getNotify("/notify/test/tmp");
        notify.addListener(LI);
    }

    @AfterClass
    public static void shutdown() {
        SystemUtil.sleep(100);
        notify.removeListener(LI);
    }

    /**
     * Test method for {@link org.apache.niolex.notify.core.ZKException#getCode()}.
     */
    @Test
    public void testUpdateData() {
        notify.updateData(MockUtil.randUUID());
        SystemUtil.sleep(20);
        notify.updateData(MockUtil.randByteArray(8));
    }

    /**
     * Test method for {@link org.apache.niolex.notify.core.ZKException#getMessage()}.
     */
    @Test
    public void testUpdateProperty() {
        notify.replaceProperty("permkey", MockUtil.randUUID());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testMakeInstance() throws Exception {
        ZKException.makeInstance("not yet implemented", new IllegalArgumentException("Abc"));
    }

    @Test
    public void testGetCode() throws Exception {
        ZKException zk = ZKException.makeInstance("not yet implemented", new InterruptedException("Abc"));
        assertEquals(zk.getCode(), ZKException.Code.INTERRUPT);
    }

    @Test
    public void testGetMessage() throws Exception {
        ZKException zk = ZKException.makeInstance("not yet implemented", KeeperException.create(KeeperException.Code.NOAUTH));
        assertEquals(zk.getCode(), ZKException.Code.NOAUTH);
    }

}
