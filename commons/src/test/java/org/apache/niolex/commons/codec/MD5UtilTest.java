/**
 * MD5UtilTest.java
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
package org.apache.niolex.commons.codec;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import junit.framework.Assert;

import org.apache.niolex.commons.codec.MD5Util;
import org.junit.Test;



public class MD5UtilTest {

    @Test
    public void testMD5() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String in = "hello world! welcome veyron!";
        String md5 = MD5Util.md5(in);
        System.out.println("md5 => " + md5);
        Assert.assertTrue(MD5Util.md5Check(md5, in));
        Assert.assertEquals(md5, "b680e9d9cdcaac7b26a82d2109f0ffb8");
    }
    
    @Test
    public void testMD5_2() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String in = "谢谢对我们开发测试中发现的问题的积极反馈，现在MCC项目已经进入最后测试阶段";
        String md5 = MD5Util.md5(in);
        System.out.println("md5 => " + md5);
        Assert.assertTrue(MD5Util.md5Check(md5, in));
        Assert.assertEquals(md5, "9a262d463f78d1b47fb53d5170d40221");
    }
    
    @Test
    public void testMD5_M() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String md5 = MD5Util.md5("baidu", "ajin");
        System.out.println("md5 => " + md5);
        Assert.assertTrue(MD5Util.md5Check(md5, "baidu", "ajin"));
    }
}
