/**
 * NewHTTPUtilTest.java
 *
 * Copyright 2013 the original author or authors.
 *
 * We licenses this file to you under the Apache License, version 2.0
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
package org.apache.niolex.commons.net;

import static org.junit.Assert.*;

import java.net.HttpURLConnection;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Maps;

/**
 *
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-7-16
 */
public class NewHTTPUtilTest extends HTTPUtil {

    private static final String PREFIX = "http://localhost:8985/";

    @BeforeClass
    public static void setUp() throws Exception {
        SimpleHttpServer.main(null);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        SimpleHttpServer.stop();
    }

    @Test
    public void testGetRedirect() throws Exception {
        HttpURLConnection.setFollowRedirects(false);
        String s = get(PREFIX);
        String response = "Use /get to download a JPG";
        assertEquals(s, response);
    }

    @Test
    public void testGetHeaderUtf8() throws Exception {
        String s = get(PREFIX + "utf8");
        String response = "广大程序员普遍拥有如下特性：缺乏运动、想象力丰富、技术卓越";
        assertEquals(s, response);
    }

    @Test
    public void testGetInferGBK() throws Exception {
        String s = get(PREFIX + "gbk");
        String response = ";charset='gbk';今年的夏日夜空格外灿烂---水星之夜、超级月亮之夜，还有牧夫座流星雨之夜！还有伟大祖国的神十遨游在夜空！";
        assertEquals(s, response);
    }

    @Test(expected=NetException.class)
    public void testGetError() throws Exception {
        get(PREFIX + "post");
    }

    @Test
    public void testPostRedirect() throws Exception {
        Map<String, String> params = Maps.newHashMap();
        params.put("inputT", "18400");
        byte[] b = doHTTP(PREFIX + "get", params, null, "iso8859-1", null, 500, 500, HTTPMethod.POST).z;
        assertArrayEquals(b, "inputT=18400".getBytes());
    }

    @Test
    public void testPostJson() throws Exception {
        String body = "This needs to be json JIFE(#$R.";
        String s = post(PREFIX + "post", body);
        assertEquals(s, body);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testPostInvalid1() throws Exception {
        Map<String, String> params = Maps.newHashMap();
        params.put("inputT", "18400");
        doHTTP(PREFIX + "get", params, "FillIn this too.", "iso8859-1", null, 500, 500, HTTPMethod.GET);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testPostInvalid2() throws Exception {
        Map<String, String> params = Maps.newHashMap();
        params.put("inputT", "18400");
        doHTTP(PREFIX + "get", params, "FillIn this too.", "iso8859-1", null, 500, 500, HTTPMethod.POST);
    }

    @Test
    public void testPost() throws Exception {
        Map<String, String> params = Maps.newHashMap();
        params.put("wd", "谢佶芸");
        params.put("ie", "gb2312");
        params.put("inputT", "18400");
        params.put("rsv_bp", "0");
        params.put("rsv_spt", "3");
        params.put("rsv_sug", "0");
        params.put("rsv_sug1", "8");
        params.put("rsv_sug3", "11");
        params.put("rsv_sug4", "624");
        params.put("tn", "baidu");
        String s = post(PREFIX + "post", params);
        String response = "tn=baidu&rsv_sug1=8&rsv_sug=0&inputT=18400&ie=gb2312&rsv_spt=3&rsv_sug4=624&wd=%E8%B0%A2%E4%BD%B6%E8%8A%B8&rsv_sug3=11&rsv_bp=0";
        assertEquals(s, response);
    }

    @Test
    public void testGetBaidu1() throws Exception {
        String s = get(PREFIX + "baidu1");
        assertTrue(s.contains("百度一下，你就知道"));
    }

    @Test
    public void testGetBaidu2() throws Exception {
        String s = get(PREFIX + "baidu2");
        assertTrue(s.contains("百度一下，你就知道"));
    }

    @Test(expected=NetException.class)
    public void testGetZeroLength() throws Exception {
        String s = get(PREFIX + "zero");
        System.out.println(s);
    }

    @Test(expected=NetException.class)
    public void testPostZeroLength() throws Exception {
        String s = post(PREFIX + "zero", "Hello, World!");
        System.out.println(s);
    }

}
