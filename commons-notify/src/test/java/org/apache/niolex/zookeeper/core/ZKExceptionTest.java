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
package org.apache.niolex.zookeeper.core;

import static org.junit.Assert.assertEquals;

import org.apache.zookeeper.KeeperException;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2013-3-13
 */
public class ZKExceptionTest {

    @Test(expected=IllegalArgumentException.class)
    public void testMakeInstance() throws Exception {
        ZKException.makeInstance("not yet implemented", new IllegalArgumentException("Abc"));
    }

    @Test
    public void testGetCodeOther() throws Exception {
        ZKException zk = ZKException.makeInstance("not yet implemented", new InterruptedException("Abc"));
        assertEquals(zk.getCode(), ZKException.Code.INTERRUPT);
        System.out.println(zk.getMessage());
    }

    @Test
    public void testGetCode() throws Exception {
        ZKException zk = ZKException.makeInstance("not yet implemented", new Exception("Abc"));
        assertEquals(zk.getCode(), ZKException.Code.OTHER);
        System.out.println(zk.getMessage());
    }

    @Test
    public void testGetCode2() throws Exception {
        ZKException zk = ZKException.makeInstance("NODE_EXISTS", KeeperException.create(KeeperException.Code.NODEEXISTS));
        assertEquals(zk.getCode(), ZKException.Code.NODE_EXISTS);
        System.out.println(zk.getMessage());
    }

    @Test
    public void testGetCode3() throws Exception {
        ZKException zk = ZKException.makeInstance("SYSTEM_ERROR", KeeperException.create(KeeperException.Code.SYSTEMERROR));
        assertEquals(zk.getCode(), ZKException.Code.SYSTEM_ERROR);
        System.out.println(zk.getMessage());
    }

    @Test
    public void testGetCode4() throws Exception {
        ZKException zk = ZKException.makeInstance("DISCONNECTED", KeeperException.create(KeeperException.Code.CONNECTIONLOSS));
        assertEquals(zk.getCode(), ZKException.Code.DISCONNECTED);
        System.out.println(zk.getMessage());
    }

    @Test
    public void testGetCode5() throws Exception {
        ZKException zk = ZKException.makeInstance("DISCONNECTED", KeeperException.create(KeeperException.Code.NONODE));
        assertEquals(zk.getCode(), ZKException.Code.NO_NODE);
        System.out.println(zk.getMessage());
    }

    @Test
    public void testZKExceptionMessage() throws Exception {
        ZKException zk = new ZKException("not yet implemented", ZKException.Code.SYSTEM_ERROR);
        assertEquals(zk.getCode(), ZKException.Code.SYSTEM_ERROR);
    }

    @Test
    public void testGetMessage() throws Exception {
        ZKException zk = ZKException.makeInstance("not yet implemented", KeeperException.create(KeeperException.Code.NOAUTH));
        assertEquals(zk.getCode(), ZKException.Code.NO_AUTH);
    }

    @Test
    public void testGetMessage2() throws Exception {
        ZKException zk = ZKException.makeInstance("not yet implemented", KeeperException.create(KeeperException.Code.AUTHFAILED));
        assertEquals(zk.getCode(), ZKException.Code.NO_AUTH);
    }

    @Test
    public void testGetMessage3() throws Exception {
        ZKException zk = ZKException.makeInstance("not yet implemented", KeeperException.create(KeeperException.Code.APIERROR));
        assertEquals(zk.getCode(), ZKException.Code.OTHER);
    }

}
