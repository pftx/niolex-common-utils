/**
 * dCoderTest.java
 *
 * Copyright 2010 Niolex, Inc.
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

import static org.junit.Assert.assertEquals;

import org.apache.niolex.commons.codec.Base16Util;
import org.apache.niolex.commons.codec.StringUtil;
import org.junit.Test;


/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 *
 * @version 1.0.0, $Date: 2010-8-31$
 *
 */
public class DESCoderTest {

    private static DESCoder dCoder;
    static {
        try {
        	dCoder = new DESCoder();
            dCoder.initKey("UadMJuXO98g=");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDES_1() throws Exception {
        System.out.println("原始的 加密-解密");
        // B6i6jFFtQLo=
        final String b = "高兴，就笑，让大家都知道。悲伤，就假装什么也没发生";
        byte[] a = dCoder.encrypt(b.getBytes(StringUtil.UTF_8));
        final byte[] o = dCoder.decrypt(a);
        final String t = new String(o, StringUtil.UTF_8);
        System.out.println("加密前：" + b + "\n解密后：" + t);
        System.out.println("中间结果：" + Base16Util.byteToBase16(a));
        assertEquals(b, t);
    }

    @Test
    public void testDES_2() throws Exception {
        System.out.println("原始的 加密-解密");
        // B6i6jFFtQLo=
        final String b = "XX*^SJzz唉说好话，、。‘【；、【【】）（地方";
        byte[] a = dCoder.encrypt(b.getBytes(StringUtil.UTF_8));
        final byte[] o = dCoder.decrypt(a);
        final String t = new String(o, StringUtil.UTF_8);
        System.out.println("加密前：" + b + "\n解密后：" + t);
        System.out.println("中间结果：" + Base16Util.byteToBase16(a));
        assertEquals(b, t);
    }

    @Test
    public void testEnhance_1() {
        final String b = "Bzclf+y5p6s=";
        String a = dCoder.encodes(b);
        String c = dCoder.decodes(a);
        System.out.println("加密前：" + b + "\n解密后：" + c);
        System.out.println("中间结果：" + a);
        assertEquals('[' +b + ']', c);
    }

    @Test
    public void testEnhance_2() {
        final String[] b = {"Bzclf+y5p6s=", "12398", "---", "我是中文"};
        String a = dCoder.encodes(b);
        String c = dCoder.decodes(a);

        String d = "[Bzclf+y5p6s=]0[12398]1[---]2[我是中文]";

        System.out.println("加密前：" + d + "\n解密后：" + c);
        System.out.println("中间结果：" + a);
        assertEquals(d, c);
    }

    @Test
    public void testEnhance_3() {
        final String b = "$";
        String a = dCoder.encodes(b);
        String c = dCoder.decodes(a);
        System.out.println("加密前：" + b + "\n解密后：" + c);
        System.out.println("中间结果：" + a);
        assertEquals(b, c.substring(1, b.length() + 1));
    }

    @Test
    public void doDESFull_1() {
        final String b = "[郑控电器]0[20100831 12:13:14]1[多好-00]2[123]";
        String a = dCoder.encodes("郑控电器", "20100831 12:13:14", "多好-00", "123");
        String c = dCoder.decodes(a);

        System.out.println("加密前：" + b + "\n解密后：" + c);
        System.out.println("中间结果：" + a);
        assertEquals(b, c);
    }

    @Test
    public void doDESFull_2() {
        final String b = "[郑控电器]0[20100831 12:13:14]1[宽容-00]2[123##$$%]";
        String a = dCoder.encodes("郑控电器", "20100831 12:13:14", "宽容-00", "123##$$%");
        String c = dCoder.decodes(a);

        System.out.println("加密前：" + b + "\n解密后：" + c);
        System.out.println("中间结果：" + a);
        assertEquals(b, c);
    }

    @Test
    public void testEncodes() throws Exception {
        String s = dCoder.encodes();
        assertEquals("", s);
    }

    @Test
    public void testEncodesNull() throws Exception {
        String s = dCoder.encodes((String[])null);
        assertEquals("", s);
    }

    @Test
    public void testEncodesError() {
        DESCoder c = new DESCoder();
        final String b = "[郑控电器]0[20100831 12:13:14]1[宽容-00]2[123##$$%]";
        String a = dCoder.encodes("郑控电器", "20100831 12:13:14", "宽容-00", "123##$$%");
        String c2 = c.decodes(a);

        System.out.println("加密前：" + b + "\n解密后：" + c2);
        System.out.println("中间结果：" + a);
        assertEquals("", c2);
        assertEquals("t7LrBCNVF+xKbc4Lsq73JHDQ0ZySEL3rD4LjQKSHUTdmiLW+au522jdW67kpntX0bO4kL2eEuTy/aO7qtZDh0w-2", a);
    }

    @Test
    public void testDecodes() throws Exception {
        String s = dCoder.decodes(null);
        assertEquals("", s);
    }

    @Test
    public void testDecodesSmall() throws Exception {
        String s = dCoder.decodes("8D");
        assertEquals("", s);
    }

    @Test
    public void testDecodesError() {
        DESCoder c = new DESCoder();
        final String b = "[郑控电器]0[20100831 12:13:14]1[宽容-00]2[123##$$%]";
        String a = c.encodes("郑控电器", "20100831 12:13:14", "宽容-00", "123##$$%");
        String c2 = dCoder.decodes(a);

        System.out.println("加密前：" + b + "\n解密后：" + c2);
        System.out.println("中间结果：" + a);
        assertEquals("", c2);
        assertEquals("", a);
    }

}
