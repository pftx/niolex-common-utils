/**
 * AESCoderTest.java
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

import org.apache.niolex.commons.coder.AESCoder;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * 
 * @version 1.0.0, $Date: 2011-7-11$
 * 
 */
public class AESCoderTest {
    private static AESCoder coder = new AESCoder();
    
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
        String s = "不是，我们要的不是钱。流程上要求必须有水费单子。你明天再来吧";
        byte[] data = coder.encrypt(s.getBytes());
        data = coder.decrypt(data);
        String r = new String(data);
        System.out.println("r => " + r);
        Assert.assertEquals(s, r);
    }
    
    @Test
    public void codeTest2() throws Exception {
        String s = "上面是QA从测试角度列出的一些检查点，欢迎各位RDs&FEs多多拍砖";
        byte[] data = coder.encrypt(s.getBytes());
        data = coder.decrypt(data);
        String r = new String(data);
        System.out.println("r => " + r);
        Assert.assertEquals(s, r);
    }
}
