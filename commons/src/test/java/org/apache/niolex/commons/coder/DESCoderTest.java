/**
 * DESCoderTest.java
 *
 * Copyright 2010 @company@, Inc.
 *
 * @company@ licenses this file to you under the Apache License, version 2.0
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

import org.apache.niolex.commons.coder.DESCoder;
import org.junit.Test;


/**
 * @author @author@ (@author-email@)
 * 
 * @version @version@, $Date: 2010-8-31$
 * 
 */
public class DESCoderTest {
    private static String key = null;
    
    static {
        try {
            key = DESCoder.genKey("I am a baidu good man");
            System.out.println("key => " + key);
            DESCoder.initKey("UadMJuXO98g=");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDES_1() throws Exception {
        System.out.println("原始的 加密-解密");
        // B6i6jFFtQLo=
        final String b = "我是百度好人";
        byte[] a = DESCoder.encrypt(b.getBytes(), key);
        final byte[] o = DESCoder.decrypt(a, key);
        System.out.println("加密前：" + b + "\n解密后：" + new String(o));
        System.out.println("中间结果：" + Arrays.toString(a));
        assertEquals(b, new String(o));
        assertTrue(Arrays.equals(b.getBytes(), o));
    }
    
    @Test
    public void testDES_2() throws Exception {
        System.out.println("原始的 加密-解密");
        // B6i6jFFtQLo=
        final String b = "XX*^SJzz唉说好话，、。‘【；、【【】）（地方";
        byte[] a = DESCoder.encrypt(b.getBytes(), key);
        final byte[] o = DESCoder.decrypt(a, key);
        System.out.println("加密前：" + b + "\n解密后：" + new String(o));
        System.out.println("中间结果：" + Arrays.toString(a));
        assertEquals(b, new String(o));
        assertTrue(Arrays.equals(b.getBytes(), o));
    }

    @Test
    public void testEnhance_1() {
        final String b = "Bzclf+y5p6s=";
        String a = DESCoder.encode(b);
        String c = DESCoder.decode(a);
        System.out.println("加密前：" + b + "\n解密后：" + c);
        System.out.println("中间结果：" + a);
        assertEquals('[' +b + ']', c);
    }
    
    @Test
    public void testEnhance_2() {
        final String[] b = {"Bzclf+y5p6s=", "12398", "---", "我是中文"};
        String a = DESCoder.encode(b);
        String c = DESCoder.decode(a);
        
        String d = "[Bzclf+y5p6s=]0[12398]1[---]2[我是中文]";
        
        System.out.println("加密前：" + d + "\n解密后：" + c);
        System.out.println("中间结果：" + a);
        assertEquals(d, c);
    }

    @Test
    public void testEnhance_3() {
        final String b = "$";
        String a = DESCoder.encode(b);
        String c = DESCoder.decode(a);
        System.out.println("加密前：" + b + "\n解密后：" + c);
        System.out.println("中间结果：" + a);
        assertEquals(b, c.substring(1, b.length() + 1));
    }

    @Test
    public void doDESFull_1() {
        final String b = "[郑控电器]0[20100831 12:13:14]1[tc-api-00]2[123]";
        String a = DESCoder.encode("郑控电器", "20100831 12:13:14", "tc-api-00", "123");
        String c = DESCoder.decode(a);
        
        System.out.println("加密前：" + b + "\n解密后：" + c);
        System.out.println("中间结果：" + a);
        assertEquals(b, c);
    }

    @Test
    public void doDESFull_2() {
        final String b = "[郑控电器]0[20100831 12:13:14]1[tc-api-00]2[123##$$%]";
        String a = DESCoder.encode("郑控电器", "20100831 12:13:14", "tc-api-00", "123##$$%");
        String c = DESCoder.decode(a);
        
        System.out.println("加密前：" + b + "\n解密后：" + c);
        System.out.println("中间结果：" + a);
        assertEquals(b, c);
    }
    
    @Test
    public void doDESFull_3() {
        String b = DESCoder
        .decode("t7LrBCNVF+xKbc4Lsq73JAI6M0jhGS+JyXzPTGQQkNme7jE5WKVeKGOapLEfVsAHxpxyS2byvSc+F1wc53pkHo5zudIdb6aYv2ju6rWQ4dM-1");
        assertEquals(b, "[郑控电器]0[2010-09-06 20:32:02.628]1[db-sf-ra00.db01.baidu.com]2[23]");
    }

}
