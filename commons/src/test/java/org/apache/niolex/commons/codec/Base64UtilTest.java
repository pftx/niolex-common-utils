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

import org.apache.niolex.commons.codec.Base64Util;
import org.junit.Assert;
import org.junit.Test;


/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 *
 * @version 1.0.0, $Date: 2011-1-13$
 *
 */
public class Base64UtilTest {

    @Test
    public void testBase64() throws Exception {
        String in = "Intel VS AMD 便携笔记本平台全面解析";
        String base64 = Base64Util.byteToBase64(in.getBytes());
        String out = new String(Base64Util.base64toByte(base64));
        System.out.println("base64 => " + base64);
        System.out.println("out => " + out);
        Assert.assertEquals(in, out);
    }

    @Test
    public void testBase64R() throws Exception {
        String in = "谢谢对我们开发测试中发现的问题的积极反馈，现在项目已经进入最后测试阶段";
        String base64 = "6LCi6LCi5a+55oiR5Lus5byA5Y+R5rWL6K+V5Lit5Y+R546w55qE6Zeu6aKY55qE56ev5p6B5Y+N6aaI77yM546w5Zyo6aG555uu5bey57uP6L+b5YWl5pyA5ZCO5rWL6K+V6Zi25q61";
        String out = new String(Base64Util.base64toByte(base64), "UTF-8");
        System.out.println("base64 => " + base64);
        System.out.println("out => " + out);
        Assert.assertEquals(in, out);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testError() throws Exception {
    	Base64Util.base64toByte(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRError() throws Exception {
    	Base64Util.byteToBase64(null);
    }

}
