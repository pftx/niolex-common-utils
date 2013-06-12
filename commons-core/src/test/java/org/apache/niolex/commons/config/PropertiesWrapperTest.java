/**
 * PropertiesWrapperTest.java
 *
 * Copyright 2011 Niolex, Inc.
 *
 * Niolex licenses this file to you under the Apache License, version 2.0
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

import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


public class PropertiesWrapperTest {

    static PropertiesWrapper PropUtil = new PropertiesWrapper();

    @BeforeClass
    public static final void init() {
        try {
            PropUtil.load("config.properties", PropertiesWrapperTest.class);
            String f = PropertiesWrapperTest.class.getResource("new.properties").toExternalForm().substring(6);
            System.out.println("f => " + f);
            PropUtil.load(f);
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
    public void testCloseStream() throws Exception {
        String f = PropertiesWrapperTest.class.getResource("new.properties").toExternalForm().substring(6);
        FileInputStream in = new FileInputStream(f);
        in = spy(in);
        PropertiesWrapper p = new PropertiesWrapper();
        p.load(in);
        Assert.assertEquals(798197318998413l, p.getLong("lb"));
        verify(in, times(1)).close();
    }

    @Test
    public void testCloseStreamError() throws Exception {
        FileInputStream in = mock(FileInputStream.class);
        PropertiesWrapper p = new PropertiesWrapper();
        when(in.read(any(byte[].class))).thenThrow(new IOException("Just 4 test"));
        boolean f = true;
        try {
            p.load(in);
        } catch (Exception e) {
            f = false;
        }
        Assert.assertEquals(566l, p.getLong("lb", 566));
        verify(in, times(1)).close();
        assertFalse(f);
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
        	System.out.println("msg => " + nfe);
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
        	System.out.println("msg => " + nfe);
        }
        hello = PropUtil.getInteger("g", "5");
        System.out.println("g => " + hello);
        Assert.assertEquals(5, hello);
        hello = PropUtil.getInteger("h", 7);
        System.out.println("h => " + hello);
        Assert.assertEquals(7, hello);
    }

    @Test
    public void testGetLong() {
    	long hello = 0;
    	try {
    		hello = PropUtil.getLong("a");
    		Assert.assertEquals(2, 3);
    	} catch (NumberFormatException nfe) {
    		System.out.println("msg => " + nfe);
    	}
    	hello = PropUtil.getLong("lb");
    	System.out.println("lb => " + hello);
    	Assert.assertEquals(798197318998413l, hello);
    	hello = PropUtil.getLong("lc");
    	System.out.println("lc => " + hello);
    	Assert.assertEquals(798197318129122l, hello);
    	try {
    		hello = PropUtil.getLong("d");
    		Assert.assertEquals(2, 3);
    	} catch (NumberFormatException nfe) {
    		System.out.println("msg => " + nfe);
    	}
    	hello = PropUtil.getLong("lg", "879871387410328");
    	System.out.println("lg => " + hello);
    	Assert.assertEquals(879871387410328l, hello);
    	hello = PropUtil.getLong("h", 79812981209832L);
    	System.out.println("h => " + hello);
    	Assert.assertEquals(79812981209832L, hello);
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
        hello = PropUtil.getBoolean("vvv", false);
        System.out.println("vvv => " + hello);
        Assert.assertEquals(true, hello);
        hello = PropUtil.getBoolean("rrr", false);
        System.out.println("rrr => " + hello);
        Assert.assertEquals(true, hello);
    }

    @Test
    public void testPropertiesWithSpace() throws Exception {
        String s = PropUtil.getProperty("sb");
        System.out.println("rrr => [" + s + "]");
        Assert.assertEquals("3455  ", s);
        int k = PropUtil.getInteger("sb");
        Assert.assertEquals(3455, k);
        //----
        s = PropUtil.getProperty("sc", "  5858  ");
        System.out.println("rrr => [" + s + "]");
        Assert.assertEquals("  5858  ", s);
        k = PropUtil.getInteger("sc", "  5858  ");
        Assert.assertEquals(5858, k);
        s = PropUtil.getString("sd", null);
        System.out.println("rrr => [" + s + "]");
    }

    @Test
    public void testPropertiesWrapper() throws Exception {
        Properties p = new Properties();
        p.put("c", "5");
        p.put("a", "nice");
        PropertiesWrapper prop = new PropertiesWrapper(p);
        prop.put("a", "7");

        Assert.assertEquals(5, prop.getInteger("c"));
        Assert.assertEquals(7, prop.getInteger("a"));
    }

    @Test
    public void testPropertiesWrapperSC() throws Exception {
        PropertiesWrapper prop = new PropertiesWrapper("config.properties", PropertiesWrapperTest.class);
        Assert.assertEquals("Welcome", prop.getProperty("greeting"));
    }

    @Test
    public void testPropertiesWrapperF() throws Exception {
        String f = PropertiesWrapperTest.class.getResource("new.properties").toExternalForm().substring(6);
        PropertiesWrapper prop = new PropertiesWrapper(f);
        Assert.assertEquals("您好！", prop.getProperty("greeting"));
    }
}
