/**
 * EditorCoderTest.java
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

import org.apache.niolex.commons.coder.EditorCoder;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author @author@ (@author-email@)
 * 
 * @version @version@, $Date: 2011-7-13$
 * 
 */
public class EditorCoderTest {
    private static EditorCoder coder = new EditorCoder();
    
    @BeforeClass
    public static void init() {
        try {
            coder.initKey("ASAAnM0huSjab75vNIA6XFbpP7MfeM39Paxm2aoLi+LQ/UA^Ehus2S77BVfeauJXkkrQKAjpeJYNuidUimlPrdMIs617Wd7NoI+HOA==");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void codeTest() throws Exception {
        String s = "不是，我们要的不是钱。流程上要求必须有水费单子。你明天再来吧";
        String m = coder.encode(s);
        System.out.println("m => " + m);
        String r = coder.decode(m);
        System.out.println("r => " + r);
        Assert.assertEquals(s, r);
    }
    
    @Test
    public void codeTest2() throws Exception {
        String s = "上面是QA从测试角度列出的一些检查点，欢迎各位RDs&FEs多多拍砖s";
        String m = coder.encode(s);
        System.out.println("m => " + m);
        String r = coder.decode(m);
        System.out.println("r => " + r);
        Assert.assertEquals(s, r);
    }
    
    @Test
    public void codeTest22() throws Exception {
        String s = "Baidu Test " + System.nanoTime() + System.currentTimeMillis() + Math.random();
        String m = coder.encode(s);
        System.out.println("m => " + m);
        String r = coder.decode(m);
        System.out.println("r => " + r);
        Assert.assertEquals(s, r);
    }
    
    @Test
    public void codeTest23() throws Exception {
        String s = "Baidu Test " + System.nanoTime() + System.currentTimeMillis() + Math.random();
        String m = coder.encode(s);
        System.out.println("m => " + m);
        String r = coder.decode(m);
        System.out.println("r => " + r);
        Assert.assertEquals(s, r);
    }
    
    @Test
    public void codeTest24() throws Exception {
        String s = "Baidu Test " + System.nanoTime() + System.currentTimeMillis() + Math.random();
        String m = coder.encode(s);
        System.out.println("m => " + m);
        String r = coder.decode(m);
        System.out.println("r => " + r);
        Assert.assertEquals(s, r);
    }
    
    @Test
    public void codeTest25() throws Exception {
        String s = "Baidu Test " + System.nanoTime() + System.currentTimeMillis() + Math.random();
        String m = coder.encode(s);
        System.out.println("m => " + m);
        String r = coder.decode(m);
        System.out.println("r => " + r);
        Assert.assertEquals(s, r);
    }
    
    @Test
    public void codeTestAnti_1() throws Exception {
        String s = "fklfakljflkdajlkdkdlkjdslajklfja###baidueditor###1310611813288";
        String m = "3oHL1UIKzFWJKXsSV2mua4ialWl6DsZiBs9iyLuWT3uW/zsVPPQE+fHgQZlnSkPCxtsNnkVxJ/2kEtDxDmMDnA==#1355216975";
        String r = coder.decode(m);
        System.out.println("r => " + r);
        Assert.assertEquals(s, r);
    }
    
    @Test
    public void codeTestAnti_2() throws Exception {
        String s = "result = RC2Encryption.Instance.Decrypt(Convert.FromBase64String(enData))###baidueditor###1310611977839";
        String m = "AWcABkfkpRAQDzCiga4D6STcW41Sjnt+EcqP4BTp6WMdrKjtpXXUYd6PhB+2zAiV4CUMwivjdEVyMVGRSbTQsZhLykvGFj587esWhPlhnX8LXvM8kzE+7P1kqFj9+soaxmSa6RqUDfXa1Y0=#1538633620";
        String r = coder.decode(m);
        System.out.println("r => " + r);
        Assert.assertEquals(s, r);
    }
    
    @Test
    public void codeTestAnti_3() throws Exception {
        String s = "1590185323560580445###baidueditor###1310612021494";
        String m = "IINOsaY3bPtTZHiJsMMK1RJyCo5bUC3toUJXKbNVenJIP7wOMnOkIyKhmwkYmPbft5d4j0RQhe0=#348335043";
        String r = coder.decode(m);
        System.out.println("r => " + r);
        Assert.assertEquals(s, r);
    }
    
    @Test
    public void codeTestAnti_4() throws Exception {
        String s = "1590185323560580445天堂的道路长度###baidueditor###1310612062326";
        String m = "AUYAnxVzg+d2IgwgVtbhHkFQqZzpp06x7BLzFLv/23Kry5x0I0zSvloTV2iJyq0cEWBjKCr1ZlFc7lXC/2fUaOi8fL5y2IsaMlTR#518549153";
        String r = coder.decode(m);
        System.out.println("r => " + r);
        Assert.assertEquals(s, r);
    }
}
