/**
 * SHAUtilTest.java
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

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.junit.Assert;

import org.apache.niolex.commons.codec.SHAUtil;
import org.junit.Test;



public class SHAUtilTest {

    @Test
    public void testSHA() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String in = "hello world! welcome Beijing!";
        String sha1 = SHAUtil.sha1(in);
        System.out.println("sha1 => " + sha1);
        Assert.assertTrue(SHAUtil.sha1Check(sha1, in));
        Assert.assertEquals(sha1, "99ed18fb96bab7e5926d617aa61c21dadb97b7f2");
    }

    @Test
    public void testSHA_2() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String in = "谢谢对我们开发测试中发现的问题的积极反馈，现在A++项目已经进入最后测试阶段";
        String sha1 = SHAUtil.sha1(in);
        System.out.println("sha1 => " + sha1);
        Assert.assertTrue(SHAUtil.sha1Check(sha1, in));
        Assert.assertEquals(sha1, "d1e53310435807b7aa1f48ec522909ef00945149");
    }

    @Test
    public void testSHA_M() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String sha1 = SHAUtil.sha1("gmail", "ajin");
        System.out.println("sha1 => " + sha1);
        Assert.assertTrue(SHAUtil.sha1Check(sha1, "gmail", "ajin"));
    }
}
