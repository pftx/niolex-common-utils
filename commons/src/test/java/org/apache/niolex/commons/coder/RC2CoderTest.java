/**
 * RC2CoderTest.java
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

import junit.framework.Assert;

import org.apache.niolex.commons.codec.Base64Util;
import org.apache.niolex.commons.coder.RC2Coder;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 *
 * @version 1.0.0, $Date: 2011-7-11$
 *
 */
public class RC2CoderTest {
    private static RC2Coder coder = new RC2Coder();

    @BeforeClass
    public static void init() {
        try {
            coder.initKey("qQosJcrqeOpW1lNe9jnb6b6orNClgWHtmAQGdkRhUvra");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void codeTest() throws Exception {
        String s = "不要停止学习。不管学习什么，语言，厨艺，各种技能";
        byte[] data = coder.encrypt(s.getBytes());
        System.out.println("m => " + Base64Util.byteToBase64(data));
        data = coder.decrypt(data);
        String r = new String(data);
        System.out.println("r => " + r);
        Assert.assertEquals(s, r);
    }

    @Test
    public void codeTest2() throws Exception {
        String s = "钱很重要，但不能依靠父母，自己一定要保持";
        byte[] data = coder.encrypt(s.getBytes());
        System.out.println("m => " + Base64Util.byteToBase64(data));
        data = coder.decrypt(data);
        String r = new String(data);
        System.out.println("r => " + r);
        Assert.assertEquals(s, r);
    }

    @Test
    public void codeTest3() throws Exception {
    	coder.secureInitKey("zmWE8DVjvWEmymTBIs2ETIzAe5GsSt78Bh+VQxg8cAPKIApm8VKHMzGBcYmuPnTrdBDEbmfNjWOXqa2YVvWq315iWEOFZ5NPPNJ2D8Fi9PA=");
    	String s = "钱很重要，但不能依靠父母，自己一定要保持";
    	byte[] data = coder.encrypt(s.getBytes());
    	System.out.println("m => " + Base64Util.byteToBase64(data));
    	data = coder.decrypt(data);
    	String r = new String(data);
    	System.out.println("r => " + r);
    	Assert.assertEquals(s, r);
    }

    @Test
    public void codeTest4() throws Exception {
    	coder.secureInitKey("zmWE8DVjvWEmymTBIs2ETIzAe5GsSt78Bh+VQxg8cAPKIApm8VKHMzGBcYmuPnTrdBDEbmfNjWOXqa2YVvWq315iWEOFZ5NPPNJ2D8Fi9PA=");
    	String s = "少用一页纸,绿色多一点 小米手机坏了";
    	byte[] data = coder.encrypt(s.getBytes());
    	System.out.println("m => " + Base64Util.byteToBase64(data));
    	data = coder.decrypt(data);
    	String r = new String(data);
    	System.out.println("r => " + r);
    	Assert.assertEquals(s, r);
    }
}
