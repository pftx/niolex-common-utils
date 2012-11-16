/**
 * Blowfish2CoderTest.java
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
package org.apache.niolex.commons.coder;

import org.junit.Assert;

import org.apache.niolex.commons.codec.Base64Util;
import org.apache.niolex.commons.coder.Blowfish2Coder;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 *
 * @version 1.0.0, $Date: 2011-7-12$
 *
 */
public class Blowfish2CoderTest {
    private static Blowfish2Coder coder = new Blowfish2Coder();

    @BeforeClass
    public static void init() {
        try {
            coder.initKey("ASIApKdj5HHBkbTSPyzMfUVJ4OFvGP4Sf8rBPEZqyIEpk8XJ33oa1mziFA==");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void codeTest() throws Exception {
        String s = "脖子睡失枕了，好痛啊！脖子睡失枕了，好痛啊！脖子睡失枕了，好痛啊！工作了乖不安逸";
        byte[] data = coder.encrypt(s.getBytes());
        data = coder.decrypt(data);
        String r = new String(data);
        System.out.println("r => " + r);
        Assert.assertEquals(s, r);
    }

    @Test
    public void codeTestCombine() throws Exception {
        String s = "http://www.w3.org/2000/09/xmldsig#";
        String r2 = "ASIAtgWTSPDQBH8CUeTTYOJNwX/OVpHHnhjO/JbLH74a8yzhpgU4Eq3AZg==";
        System.out.println("r => " + r2);
        byte[] data = Base64Util.base64toByte(r2);
        data = coder.decrypt(data);
        String r = new String(data);
        System.out.println("r => " + r);
        Assert.assertEquals(s, r);
    }

}
