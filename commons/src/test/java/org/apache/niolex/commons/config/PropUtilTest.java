/**
 * PropUtilTest.java
 *
 * Copyright 2011 Baidu, Inc.
 *
 * Baidu licenses this file to you under the Apache License, version 2.0
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
package org.apache.niolex.commons.config;

import java.io.IOException;

import junit.framework.Assert;

import org.apache.niolex.commons.config.PropUtil;
import org.junit.BeforeClass;
import org.junit.Test;


public class PropUtilTest {
    
    @BeforeClass
    public static final void init() {
        try {
            PropUtil.loadConfigFromClassPath("/com/baidu/api/core/config/config.properties");
            PropUtil.loadConfigFromClassPath("/com/baidu/api/core/config/new.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetProperty() {
        String hello = null;
        hello = PropUtil.getProperty("a");
        System.out.println("a => " + hello);
        Assert.assertEquals("Hello World!", hello);
        hello = PropUtil.getProperty("c");
        System.out.println("c => " + hello);
        Assert.assertEquals("3", hello);
        hello = PropUtil.getProperty("e");
        System.out.println("e => " + hello);
        Assert.assertEquals(null, hello);
        hello = PropUtil.getProperty("g", "default");
        System.out.println("g => " + hello);
        Assert.assertEquals("default", hello);
    }
    
    @Test
    public void testOverried() {
        String hello = null;
        hello = PropUtil.getProperty("greeting");
        System.out.println("a => " + hello);
        Assert.assertEquals("您好！", hello);
    }
    
    @Test
    public void testGetString() {
        String hello = null;
        hello = PropUtil.getString("a");
        System.out.println("a => " + hello);
        Assert.assertEquals("Hello World!", hello);
        hello = PropUtil.getString("c");
        System.out.println("c => " + hello);
        Assert.assertEquals("3", hello);
        hello = PropUtil.getString("e");
        System.out.println("e => " + hello);
        Assert.assertEquals(null, hello);
        hello = PropUtil.getString("g", "default");
        System.out.println("g => " + hello);
        Assert.assertEquals("default", hello);
    }
    
    @Test
    public void testGetInteger() {
        int hello = 0;
        try {
            hello = PropUtil.getInteger("a");
            Assert.assertEquals(2, 3);
        } catch (NumberFormatException nfe) {
        }
        hello = PropUtil.getInteger("b");
        System.out.println("b => " + hello);
        Assert.assertEquals(2, hello);
        hello = PropUtil.getInteger("c");
        System.out.println("c => " + hello);
        Assert.assertEquals(3, hello);
        try {
            hello = PropUtil.getInteger("d");
            Assert.assertEquals(2, 3);
        } catch (NumberFormatException nfe) {
        }
        hello = PropUtil.getInteger("g", "5");
        System.out.println("g => " + hello);
        Assert.assertEquals(5, hello);
        hello = PropUtil.getInteger("h", 7);
        System.out.println("h => " + hello);
        Assert.assertEquals(7, hello);
    }
    
    @Test
    public void testGetBoolean() {
        boolean hello = false;
        hello = PropUtil.getBoolean("b");
        System.out.println("b => " + hello);
        Assert.assertEquals(false, hello);
        hello = PropUtil.getBoolean("k");
        System.out.println("k => " + hello);
        Assert.assertEquals(false, hello);
        hello = PropUtil.getBoolean("xxx");
        System.out.println("xxx => " + hello);
        Assert.assertEquals(true, hello);
        hello = PropUtil.getBoolean("www", "5");
        System.out.println("www => " + hello);
        Assert.assertEquals(false, hello);
        hello = PropUtil.getBoolean("h", true);
        System.out.println("h => " + hello);
        Assert.assertEquals(true, hello);
        hello = PropUtil.getBoolean("zzz", true);
        System.out.println("zzz => " + hello);
        Assert.assertEquals(false, hello);
        hello = PropUtil.getBoolean("yyy", true);
        System.out.println("yyy => " + hello);
        Assert.assertEquals(false, hello);
    }
}
