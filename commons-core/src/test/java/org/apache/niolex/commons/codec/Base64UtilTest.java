/**
 * Base64UtilTest.java
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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.niolex.commons.test.MockUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 *
 * @version 1.0.0, $Date: 2011-1-13$
 */
public class Base64UtilTest extends Base64Util {

    @Test
    public void testBase64() throws Exception {
        String in = "Intel VS AMD 便携笔记本平台全面解析";
        String base64 = Base64Util.byteToBase64(in.getBytes());
        String out = new String(Base64Util.base64ToByte(base64));
        System.out.println("base64 => " + base64);
        System.out.println("out => " + out);
        Assert.assertEquals(in, out);
    }

    @Test
    public void testBase64Sutie() throws Exception {
        String out = testBase64Iter("I");
        out += "," + testBase64Iter("In");
        out += "," + testBase64Iter("Int");
        out += "," + testBase64Iter("Inte");
        out += "," + testBase64Iter("Intel");
        System.out.println("suite => " + out);
    }

    public String testBase64Iter(String in) {
        byte[] bytes = StringUtil.strToAsciiByte(in);
        return testBase64Iter(bytes);
    }

    @Test
    public void testRandomSutie() throws Exception {
        for (int i = 0; i < 1000; ++i) {
            int len = MockUtil.randInt(10, 57);
            byte[] in = MockUtil.randByteArray(len);
            testBase64Iter(in);
        }
    }

    public String testBase64Iter(byte[] bytes) {
        String base64 = byteToBase64(bytes);
        String base642 = byteToBase64LF(bytes);
        String base643 = byteToBase64URL(bytes);
        Assert.assertEquals(base64, base642);
        String base646 = base64.replace('+', '-').replace('/', '_');
        int e = base646.indexOf('=');
        if (e > 0)
            Assert.assertEquals(base646.substring(0, e), base643);
        else
            Assert.assertEquals(base646, base643);
        byte[] out = base64ToByte(base64);
        byte[] out2 = base64ToByteURL(base643);
        Assert.assertArrayEquals(bytes, out);
        Assert.assertArrayEquals(bytes, out2);

        return base64;
    }

    @Test
    public void testBase64I() throws Exception {
        byte[] in = new byte[1];
        String base64 = Base64Util.byteToBase64(in);
        System.out.println("base64I => " + base64);
        Assert.assertEquals("AA==", base64);
    }

    @Test
    public void testBase64R() throws Exception {
        String in = "谢谢对我们开发测试中发现的问题的积极反馈，现在项目已经进入最后测试阶段";
        String base64 = "6LCi6LCi5a+55oiR5Lus5byA5Y+R5rWL6K+V5Lit5Y+R546w55qE6Zeu6aKY55qE56ev5p6B5Y+N6aaI77yM546w5Zyo6aG555uu5bey57uP6L+b5YWl5pyA5ZCO5rWL6K+V6Zi25q61";
        String out = new String(Base64Util.base64ToByte(base64), "UTF-8");
        System.out.println("base64R => " + base64);
        System.out.println("outR => " + out);
        Assert.assertEquals(in, out);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testError1() throws Exception {
        Base64Util.base64ToByte(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testError2() throws Exception {
        Base64Util.base64ToByteURL(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testError3() throws Exception {
        byte[] out = Base64Util.base64ToByte("A?");
        System.out.println("? => " + Base16Util.byteToBase16(out));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testError4() throws Exception {
        Base64Util.base64ToByteURL("z+");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testError5() throws Exception {
        Base64Util.base64ToByte("nice(R)");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testError6() throws Exception {
        Base64Util.base64ToByteURL("Qt+++");
    }

    @Test
    public void testNoERR() {
        byte[] bs = base64ToByte("n+i/c");
        byte[] b2 = base64ToByteURL("n-i_c");
        assertArrayEquals(bs, b2);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRError() throws Exception {
    	Base64Util.byteToBase64(null);
    }

    @Test
    public void testBase64LF() throws Exception {
        String input = "We recommend you use a mirror to download our release builds, but you must verify the integrity of the downloaded files using signatures downloaded from our main distribution directories. Recent releases (48 hours) may not yet be available from the mirrors.";
        String lf = byteToBase64LF(StringUtil.strToAsciiByte(input));
        System.out.println("====GPG====");
        System.out.println(lf);
        System.out.println("====GPG====");
        String out = StringUtil.asciiByteToStr(base64ToByte(lf));
        assertEquals(input, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testByteToBase64LFN() throws Exception {
        byteToBase64LF(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testByteToBase64URLN() throws Exception {
        byteToBase64URL(null);
    }

    @Test
    public void testByteToBase64LF() throws Exception {
        byteToBase64LF(MockUtil.randByteArray(100));
    }

    @Test
    public void testByteToBase64URL() throws Exception {
        byteToBase64URL(MockUtil.randByteArray(100));
    }

    @Test
    public void testByteToBase64LFE() throws Exception {
        String s = byteToBase64LF(MockUtil.randByteArray(4));
        assertEquals(8, s.length());
    }

    @Test
    public void testByteToBase64URLE() throws Exception {
        String s = byteToBase64URL(MockUtil.randByteArray(4));
        assertEquals(6, s.length());
    }
}
