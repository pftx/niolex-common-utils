/**
 * TripleDESCoderTest.java
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.niolex.commons.coder.TripleDESCoder;
import org.junit.Test;


/**
 * @author @author@ (@author-email@)
 * 
 * @version @version@, $Date: 2011-7-11$
 * 
 */
public class TripleDESCoderTest {
    
    private static String key = null;
    
    static {
        try {
            key = TripleDESCoder.genKey("I am a baidu good man");
            System.out.println("key => " + key);
            TripleDESCoder.initKey(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDES3_1() throws Exception {
        System.out.println("原始的 加密-解密");
        // B6i6jFFtQLo=
        final String b = "我是百度好人";
        byte[] a = TripleDESCoder.encrypt(b.getBytes());
        final byte[] o = TripleDESCoder.decrypt(a);
        System.out.println("加密前：" + b + "\n解密后：" + new String(o));
        System.out.println("中间结果：" + Arrays.toString(a));
        assertEquals(b, new String(o));
        assertTrue(Arrays.equals(b.getBytes(), o));
    }
    
    @Test
    public void testDES3_2() throws Exception {
        System.out.println("原始的 加密-解密");
        // B6i6jFFtQLo=
        final String b = "不是，我们要的不是钱。流程上要求必须有水费单子。你明天再来吧";
        byte[] a = TripleDESCoder.encrypt(b.getBytes());
        final byte[] o = TripleDESCoder.decrypt(a);
        System.out.println("加密前：" + b + "\n解密后：" + new String(o));
        System.out.println("中间结果：" + Arrays.toString(a));
        assertEquals(b, new String(o));
        assertTrue(Arrays.equals(b.getBytes(), o));
    }
}
