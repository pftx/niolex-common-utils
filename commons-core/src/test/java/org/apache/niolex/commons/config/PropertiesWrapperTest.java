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

    static PropertiesWrapper props = new PropertiesWrapper();
    
    static String trimProtocol(String s) {
    	int i = s.indexOf(':');
    	while (s.charAt(i + 1) == '/') {
    		++i;
    	}
    	return s.substring(i);
    }

    @BeforeClass
    public static final void init() {
        try {
            props.load("config.properties", PropertiesWrapperTest.class);
            String q = PropertiesWrapperTest.class.getResource("new.properties").toExternalForm();
            System.out.println("q => " + q);
            String f = trimProtocol(q);
            System.out.println("f => " + f);
            props.load(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        String f = trimProtocol(PropertiesWrapperTest.class.getResource("new.properties").toExternalForm());
        PropertiesWrapper prop = new PropertiesWrapper(f);
        Assert.assertEquals("您好！", prop.getProperty("greeting"));
    }

    @Test
    public void testLoad() throws Exception {
        String f = trimProtocol(PropertiesWrapperTest.class.getResource("new.properties").toExternalForm());
        FileInputStream iner = new FileInputStream(f);
        FileInputStream in = spy(iner);
        PropertiesWrapper p = new PropertiesWrapper();
        p.load(in);
        Assert.assertEquals(798197318998413l, p.getLong("lb"));
        verify(in, times(1)).close();
        iner.close();
    }

    @Test
    public void testLoadCloseStreamWithException() throws Exception {
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
    public void testPropertiesWithSpace() throws Exception {
        String s = props.getProperty("sb");
        System.out.println("rrr => [" + s + "]");
        Assert.assertEquals("3455  ", s);
        int k = props.getInteger("sb");
        Assert.assertEquals(3455, k);
        //----
        s = props.getProperty("sc", "  5858  ");
        System.out.println("rrr => [" + s + "]");
        Assert.assertEquals("  5858  ", s);
        k = props.getInteger("sc", "  5858  ");
        Assert.assertEquals(5858, k);
        s = props.getString("sd", null);
        System.out.println("rrr => [" + s + "]");
    }

    @Test
    public void testGetString() {
        String hello = null;
        hello = props.getString("a");
        System.out.println("a => " + hello);
        Assert.assertEquals("Hello World!", hello);
        hello = props.getString("c");
        System.out.println("c => " + hello);
        Assert.assertEquals("3", hello);
    }

    @Test
    public void testGetPropertyNull() {
        String hello = props.getString("e");
        System.out.println("e => " + hello);
        Assert.assertEquals(null, hello);
    }

    @Test
    public void testGetPropertyWithDefault() {
        String hello = props.getString("a", "default");
        System.out.println("a => " + hello);
        Assert.assertEquals("Hello World!", hello);
    }

    @Test
    public void testGetPropertyWithDefaultNull() {
        String hello = props.getString("g", "  default  ");
        System.out.println("g => " + hello);
        Assert.assertEquals("default", hello);
    }

    @Test
    public void testGetPropertyWithDefaultNullAndNull() {
        String hello = props.getString("g", null);
        System.out.println("g<null> => " + hello);
        Assert.assertEquals(null, hello);
    }

    @Test
    public void testGetStringOverried() {
        String hello = null;
        hello = props.getString("greeting");
        System.out.println("a => " + hello);
        Assert.assertEquals("您好！", hello);
    }

    @Test
    public void testGetStringTrim() {
        String hello = null;
        hello = props.getString("trim");
        System.out.println("trim => " + hello);
        Assert.assertEquals("Core\tLex", hello);
    }

    @Test
    public void testGetInteger() {
        int hello = 0;
        hello = props.getInteger("b");
        System.out.println("b => " + hello);
        Assert.assertEquals(2, hello);
    }

    @Test
    public void testGetIntegerWithDefaultInt() {
        int hello = 0;
        hello = props.getInteger("b", 6);
        System.out.println("b => " + hello);
        Assert.assertEquals(2, hello);
    }

    @Test
    public void testGetIntegerWithDefaultString() {
        int hello = 0;
        hello = props.getInteger("b", "&*^#@");
        System.out.println("b => " + hello);
        Assert.assertEquals(2, hello);
    }

    @Test
    public void testGetIntegerTrim() {
        int hello = 0;
        hello = props.getInteger("c");
        System.out.println("c => " + hello);
        Assert.assertEquals(3, hello);
    }

    @Test
    public void testGetIntegerNotInt() {
        boolean flag = true;
        try {
            props.getInteger("a");
            Assert.assertEquals(2, 3);
        } catch (NumberFormatException nfe) {
        	flag = false;
        }
        Assert.assertFalse(flag);
    }

    @Test
    public void testGetIntegerNull() {
        boolean flag = true;
        try {
            props.getInteger("d");
            Assert.assertEquals(2, 3);
        } catch (NumberFormatException nfe) {
            flag = false;
        }
        Assert.assertFalse(flag);
    }

    @Test
    public void testGetIntegerOKWithDefaultNull() {
        boolean flag = true;
        try {
            Assert.assertEquals(2, props.getInteger("b", null));
        } catch (NumberFormatException nfe) {
            flag = false;
        }
        Assert.assertTrue(flag);
    }

    @Test
    public void testGetIntegerNullWithDefaultNull() {
        boolean flag = true;
        try {
            props.getInteger("d", null);
            Assert.assertEquals(2, 3);
        } catch (NumberFormatException nfe) {
            flag = false;
        }
        Assert.assertFalse(flag);
    }

    @Test
    public void testGetIntegerNullWithDefaultString() {
        int hello = props.getInteger("g", "5");
        System.out.println("g => " + hello);
        Assert.assertEquals(5, hello);
    }

    @Test
    public void testGetIntegerNullWithDefaultInt() {
        int hello = props.getInteger("h", 7);
        System.out.println("h => " + hello);
        Assert.assertEquals(7, hello);
    }

    @Test
    public void testGetLong() {
    	long hello = 0;
    	hello = props.getLong("lb");
    	System.out.println("lb => " + hello);
    	Assert.assertEquals(798197318998413l, hello);
    }

    @Test
    public void testGetLongWithDefaultString() {
        long hello = 0;
        hello = props.getLong("lb", "*(&#*$");
        System.out.println("lb => " + hello);
        Assert.assertEquals(798197318998413l, hello);
    }

    @Test
    public void testGetLongWithDefaultInt() {
        long hello = 0;
        hello = props.getLong("lb", 1232);
        System.out.println("lb => " + hello);
        Assert.assertEquals(798197318998413l, hello);
    }

    @Test
    public void testGetLongNotLong() {
        boolean flag = true;
    	try {
    		props.getLong("a");
    		Assert.assertEquals(2, 3);
    	} catch (NumberFormatException nfe) {
    		flag = false;
    	}
    	Assert.assertFalse(flag);
    }

    @Test
    public void testGetLongNull() {
        boolean flag = true;
        try {
            props.getLong("lex");
            Assert.assertEquals(2, 3);
        } catch (NumberFormatException nfe) {
            flag = false;
        }
        Assert.assertFalse(flag);
    }

    @Test
    public void testGetLongNullWithDefaultNotLong() {
        boolean flag = true;
        try {
            props.getLong("lex", "lexAgain");
            Assert.assertEquals(2, 3);
        } catch (NumberFormatException nfe) {
            flag = false;
        }
        Assert.assertFalse(flag);
    }

    @Test
    public void testGetLongNullWithDefaultString() {
        long hello = props.getLong("lex", "93842");
        Assert.assertEquals(93842l, hello);
    }

    @Test
    public void testGetLongNullWithDefaultLong() {
        long hello = props.getLong("lex", 798197318129122l);
        Assert.assertEquals(798197318129122l, hello);
    }

    @Test
    public void testGetLongAdHoc() {
    	long hello = props.getLong("lc");
    	System.out.println("lc => " + hello);
    	Assert.assertEquals(798197318129122l, hello);
    	hello = props.getLong("lg", "879871387410328");
    	System.out.println("lg => " + hello);
    	Assert.assertEquals(879871387410328l, hello);
    	hello = props.getLong("h", 79812981209832L);
    	System.out.println("h => " + hello);
    	Assert.assertEquals(79812981209832L, hello);
    }

    @Test
    public void testGetBoolean() {
        boolean hello = props.getBoolean("xxx");
        System.out.println("xxx => " + hello);
        Assert.assertEquals(true, hello);
        hello = props.getBoolean("www", "5");
        System.out.println("www => " + hello);
        Assert.assertEquals(false, hello);
    }

    @Test
    public void testGetBooleanNull() {
        boolean hello = props.getBoolean("k");
        Assert.assertEquals(false, hello);
        hello = props.getBoolean("k", null);
        Assert.assertEquals(false, hello);
    }

    @Test
    public void testGetBoolean3asFalse() {
        boolean hello = props.getBoolean("c");
        Assert.assertEquals(false, hello);
    }

    @Test
    public void testGetBooleanOnOff() {
        boolean hello = props.getBoolean("on");
        Assert.assertEquals(true, hello);
        hello = props.getBoolean("off");
        Assert.assertEquals(false, hello);
    }

    @Test
    public void testGetBoolean01() {
        boolean hello = props.getBoolean("zzz", true);
        System.out.println("zzz => " + hello);
        Assert.assertEquals(false, hello);
        hello = props.getBoolean("yyy", "true");
        System.out.println("yyy => " + hello);
        Assert.assertEquals(true, hello);
    }

    @Test
    public void testGetBooleanYesNo() {
        boolean hello = props.getBoolean("yes", true);
        System.out.println("yes => " + hello);
        Assert.assertEquals(true, hello);
        hello = props.getBoolean("no", "true");
        System.out.println("no => " + hello);
        Assert.assertEquals(false, hello);
    }

    @Test
    public void testGetBooleanInt() {
        boolean hello = false;
        hello = props.getBoolean("b");
        Assert.assertEquals(false, hello);
    }

    @Test
    public void testGetBooleanWithDefault() {
        boolean hello = props.getBoolean("h", true);
        System.out.println("h => " + hello);
        Assert.assertEquals(true, hello);

        hello = props.getBoolean("vvv", false);
        System.out.println("vvv => " + hello);
        Assert.assertEquals(true, hello);
        hello = props.getBoolean("rrr", false);
        System.out.println("rrr => " + hello);
        Assert.assertEquals(true, hello);
    }

    @Test
    public void testGetDouble() {
        double d = 0.0;
        d = props.getDouble("db1");
        Assert.assertEquals(123.321, d, 0.000001);
    }

    @Test
    public void testGetDoubleWithDefaultStr() {
        double d = 0.0;
        d = props.getDouble("db2", "#(#(#(2");
        Assert.assertEquals(456.654, d, 0.000001);
    }

    @Test
    public void testGetDoubleWithDefaultDouble() {
        double d = 0.0;
        d = props.getDouble("db3", 3721.456);
        Assert.assertEquals(789.0, d, 0.000001);
    }

    @Test(expected=NullPointerException.class)
    public void testGetDoubleNull() {
        double d = 0.0;
        d = props.getDouble("db4");
        Assert.assertEquals(789.0, d, 0.000001);
    }

    @Test(expected=NumberFormatException.class)
    public void testGetDoubleNotDouble() {
        double d = 0.0;
        d = props.getDouble("a");
        Assert.assertEquals(789.0, d, 0.000001);
    }

    @Test
    public void testGetDoubleNullWithDefaultStr() {
        double d = 0.0;
        d = props.getDouble("h", "789");
        Assert.assertEquals(789.0, d, 0.000001);
    }

    @Test
    public void testGetDoubleNullWithDefault() {
        double d = 0.0;
        d = props.getDouble("db4", 3721.456);
        Assert.assertEquals(3721.456, d, 0.000001);
    }

}
