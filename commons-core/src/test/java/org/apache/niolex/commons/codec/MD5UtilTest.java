/**
 * MD5UtilTest.java
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
package org.apache.niolex.commons.codec;

import org.junit.Assert;

import org.apache.niolex.commons.codec.MD5Util;
import org.junit.Test;



public class MD5UtilTest {

    @Test
    public void testMD5() {
        String in = "hello world! welcome jess calen!";
        String md5 = MD5Util.md5(in);
        System.out.println("md5 => " + md5);
        Assert.assertTrue(MD5Util.md5Check(md5, in));
        Assert.assertEquals(md5, "a554d028dd912347d8daaed66df3cb57");
    }

    @Test
    public void testMD5_2() {
        String in = "谢谢对我们开发测试中发现的问题的积极反馈，现在Aaa项目已经进入最后测试阶段";
        String md5 = MD5Util.md5(in);
        System.out.println("md5 => " + md5);
        Assert.assertTrue(MD5Util.md5Check(md5, in));
        Assert.assertEquals(md5, "d6623ed5f967c6861968fc3886166f66");
    }

    @Test
    public void testMD5_M() {
        String md5 = MD5Util.md5("gmail", "ajin");
        System.out.println("md5 => " + md5);
        Assert.assertTrue(MD5Util.md5Check(md5, "gmail", "ajin"));
    }

    @Test
    public void testMD5_Null() {
    	String md5 = MD5Util.md5("gmail", null, "ajin");
    	System.out.println("md5 => " + md5);
    	Assert.assertFalse(MD5Util.md5Check(md5, "gmail", "ajin"));
    	Assert.assertTrue(MD5Util.md5Check(md5, "gmail", null, "ajin"));
    }
}
