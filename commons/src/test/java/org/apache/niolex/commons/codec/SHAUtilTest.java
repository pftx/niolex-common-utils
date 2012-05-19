/**
 * SHAUtilTest.java
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

import org.apache.niolex.commons.codec.SHAUtil;
import org.junit.Test;



public class SHAUtilTest {

    @Test
    public void testSHA() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String in = "hello world! welcome veyron!";
        String sha1 = SHAUtil.sha1(in);
        System.out.println("sha1 => " + sha1);
        Assert.assertTrue(SHAUtil.sha1Check(sha1, in));
        Assert.assertEquals(sha1, "678a9f44ac5bbf0231b9a44b3c7cd16df67e1734");
    }
    
    @Test
    public void testSHA_2() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String in = "谢谢对我们开发测试中发现的问题的积极反馈，现在MCC项目已经进入最后测试阶段";
        String sha1 = SHAUtil.sha1(in);
        System.out.println("sha1 => " + sha1);
        Assert.assertTrue(SHAUtil.sha1Check(sha1, in));
        Assert.assertEquals(sha1, "4f8732a3ff3495d20888c09cf8f5fa1a464b1a61");
    }
    
    @Test
    public void testSHA_M() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String sha1 = SHAUtil.sha1("baidu", "ajin");
        System.out.println("sha1 => " + sha1);
        Assert.assertTrue(SHAUtil.sha1Check(sha1, "baidu", "ajin"));
    }
}
