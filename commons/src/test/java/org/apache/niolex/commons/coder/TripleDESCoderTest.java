/**
 * TripleDESCoderTest.java
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

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;


/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 *
 * @version 1.0.0, $Date: 2011-7-11$
 *
 */
public class TripleDESCoderTest {

    private static TripleDESCoder coder = null;

    static {
        try {
            String key = TripleDESCoder.genKey("I am a gmail good man");
            TripleDESCoder.genKey();
            System.out.println("key => " + key);
            coder = new TripleDESCoder();
            coder.initKey(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDES3_1() throws Exception {
        System.out.println("原始的 加密-解密");
        // B6i6jFFtQLo=
        final String b = "即使输掉了一切，也不要输掉微笑……始终相信，文字是有灵魂的，若绵绵的雨，若轻轻的风";
        byte[] a = coder.encrypt(b.getBytes());
        final byte[] o = coder.decrypt(a);
        System.out.println("加密前：" + b + "\n解密后：" + new String(o));
        System.out.println("中间结果：" + Arrays.toString(a));
        assertEquals(b, new String(o));
        assertTrue(Arrays.equals(b.getBytes(), o));
        assertNotSame(TripleDESCoder.genKey(null), TripleDESCoder.genKey());
    }

    @Test
    public void testDES3_2() throws Exception {
        System.out.println("原始的 加密-解密");
        // B6i6jFFtQLo=
        final String b = "不要因为冲动说一些过激的话，会慢慢氤氲、渗透每一个相知的心灵，并于无声处开出润暖的花来";
        byte[] a = coder.encrypt(b.getBytes());
        final byte[] o = coder.decrypt(a);
        System.out.println("加密前：" + b + "\n解密后：" + new String(o));
        System.out.println("中间结果：" + Arrays.toString(a));
        assertEquals(b, new String(o));
        assertTrue(Arrays.equals(b.getBytes(), o));
    }
}
