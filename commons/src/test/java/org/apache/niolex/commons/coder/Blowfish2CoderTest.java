/**
 * Blowfish2CoderTest.java
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
package org.apache.niolex.commons.coder;

import junit.framework.Assert;

import org.apache.niolex.commons.codec.Base64Util;
import org.apache.niolex.commons.coder.Blowfish2Coder;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author @author@ (@author-email@)
 * 
 * @version @version@, $Date: 2011-7-12$
 * 
 */
public class Blowfish2CoderTest {
    private static Blowfish2Coder coder = new Blowfish2Coder();
    
    @BeforeClass
    public static void init() {
        try {
            coder.initKey("BaiduEditor");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void codeTest() throws Exception {
        String s = "不是，我们要的不是钱。流程上要求必须有水费单子。你明天再来吧";
        byte[] data = coder.encrypt(s.getBytes());
        data = coder.decrypt(data);
        String r = new String(data);
        System.out.println("r => " + r);
        Assert.assertEquals(s, r);
    }
    
    @Test
    public void codeTestCombine() throws Exception {
        String s = "ASIApKdj5HHBkbTSPyzMfUVJ4OFvGP4Sf8rBPEZqyIEpk8XJ33oa1mziFA==";
        byte[] data = Base64Util.base64toByte(s);
        data = coder.decrypt(data);
        String r = new String(data, "utf-8");
        System.out.println("r => " + r);
        Assert.assertEquals("http://www.w3.org/2000/09/xmldsig#", r);
    }
}
