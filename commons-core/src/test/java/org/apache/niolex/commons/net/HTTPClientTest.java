package org.apache.niolex.commons.net;


import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.niolex.commons.reflect.FieldUtil;
import org.junit.Test;

public class HTTPClientTest {
    
    private static final String BASIC_URL = "http://httpbin.org/";
    
    public static final String tidyStr(String s) {
        return s.replaceAll("\r*\n", "").replaceAll(" *", "");
    }

    @Test
    public void testHTTPClientStringString() throws Exception {
        HTTPClient hc = new HTTPClient(BASIC_URL, "utf8");
        HTTPResult r = hc.get("basic-auth/user/passwd", null);
        System.out.println(r);
        assertEquals(401, r.getRespCode());
        System.out.println("Headers " + r.getRespHeaders());
    }

    @Test
    public void testHTTPClientStringStringIntInt() throws Exception {
        HTTPClient hc = new HTTPClient(BASIC_URL, "utf8", 6000, 6000);
        hc.authorization("user", "passwd");
        HTTPResult r = hc.get("basic-auth/user/passwd", null);
        System.out.println(r);
        assertEquals(200, r.getRespCode());
        assertEquals("{\"authenticated\":true,\"user\":\"user\"}", tidyStr(r.getRespBodyStr()));
        System.out.println("Headers " + r.getRespHeaders());
    }

    @Test
    public void testAuthorization() throws Exception {
        HTTPClient hc = new HTTPClient(BASIC_URL, "utf8");
        Map<String, String> params = new HashMap<String, String>();
        params.put("usa", "good");
        params.put("oo", "lex");
        HTTPResult r = hc.get("get", params);
        System.out.println(r);
        assertEquals(200, r.getRespCode());
        assertTrue(tidyStr(r.getRespBodyStr()).contains("{\"oo\":\"lex\",\"usa\":\"good\"}"));
        System.out.println("Headers " + r.getRespHeaders());
    }

    @Test
    public void testGet() throws Exception {
        HTTPClient hc = new HTTPClient("http://www.w3school.com.cn/tiy", "utf8");
        HTTPResult r = hc.post("..//example/jquery/demo_test_post.asp?name=lex", "{city=\"chengdu\", name=\"lex\"}");
        System.out.println("Headers " + r.getRespHeaders());
        assertEquals("Dear . Hope you live well in .", r.getRespBodyStr());
    }

    @Test
    public void testPostStringString() throws Exception {
        HTTPClient hc = new HTTPClient("http://www.w3school.com.cn/tiy", "utf8");
        Map<String, String> params = new HashMap<String, String>();
        params.put("city", "www.zju.edu.cn");
        params.put("name", "lex");
        HTTPResult r = hc.post("..//example/jquery/demo_test_post.asp", params);
        System.out.println("Headers " + r.getRespHeaders());
        assertEquals("Dear lex. Hope you live well in www.zju.edu.cn.", r.getRespBodyStr());
    }

    @Test
    public void testProcessCookieNull() throws Exception {
        HTTPClient hc = new HTTPClient(BASIC_URL, "utf8");
        Map<String, String> reqHeaders = FieldUtil.getValue(hc, "reqHeaders");
        Map<String, List<String>> respHeaders = new HashMap<String, List<String>>();
        respHeaders.put("Set-Cookie", null);
        hc.processCookie(respHeaders);
        assertNull(reqHeaders.get("Cookie"));
    }

    @Test
    public void testProcessCookieBig() throws Exception {
        HTTPClient hc = new HTTPClient(BASIC_URL, "utf8");
        Map<String, String> reqHeaders = FieldUtil.getValue(hc, "reqHeaders");
        Map<String, List<String>> respHeaders = new HashMap<String, List<String>>();
        List<String> cc = new ArrayList<String>();
        cc.add("user_session=Y6Uqq5e1Nrb92vnlTczbaN4xRasFZxK-9OY6QqUjhPxgbR3QVsFkJ6V_eyko_RP8JGqvVhWwqnaVVjdh; path=/; expires=Mon, 29 Feb 2016 05:37:43 -0000; secure; HttpOnly");
        respHeaders.put("Set-Cookie", cc);
        hc.processCookie(respHeaders);
        assertEquals(reqHeaders.get("Cookie"), "user_session=Y6Uqq5e1Nrb92vnlTczbaN4xRasFZxK-9OY6QqUjhPxgbR3QVsFkJ6V_eyko_RP8JGqvVhWwqnaVVjdh");
    }

    @Test
    public void testProcessCookieLittle() throws Exception {
        HTTPClient hc = new HTTPClient(BASIC_URL, "utf8");
        Map<String, String> reqHeaders = FieldUtil.getValue(hc, "reqHeaders");
        Map<String, List<String>> respHeaders = new HashMap<String, List<String>>();
        List<String> cc = new ArrayList<String>();
        cc.add("user_session=Y6Uqq5e1Nrb92vnlTczbaN4xRasFZxK; path=/; expires=Mon, 29 Feb 2016 05:37:43 -0000; secure; HttpOnly");
        cc.add("user_salt=9dj*jfla0fk[a; path=/; expires=Mon, 29 Feb 2016 05:37:43 -0000; secure; HttpOnly");
        respHeaders.put("set-cookie", cc);
        hc.processCookie(respHeaders);
        assertEquals(reqHeaders.get("Cookie"), "user_session=Y6Uqq5e1Nrb92vnlTczbaN4xRasFZxK; user_salt=9dj*jfla0fk[a");
    }

    @Test
    public void testProcessCookieEpty() throws Exception {
        HTTPClient hc = new HTTPClient(BASIC_URL, "utf8");
        Map<String, String> reqHeaders = FieldUtil.getValue(hc, "reqHeaders");
        Map<String, List<String>> respHeaders = new HashMap<String, List<String>>();
        List<String> s = Collections.emptyList();
        respHeaders.put("Set-Cookie", s);
        hc.processCookie(respHeaders);
        assertNull(reqHeaders.get("Cookie"));
    }

    @Test
    public void testGenerateURL() throws Exception {
        HTTPClient hc = new HTTPClient("https://httpbin.org/head", "utf8");
        String s1 = hc.generateURL("");
        String s2 = hc.generateURL("abc");
        String s3 = hc.generateURL("../abc");
        String s4 = hc.generateURL("../abc/./../def");
        String s5 = hc.generateURL("../abc/./../../../../def");

        assertEquals("https://httpbin.org/head", s1);
        assertEquals("https://httpbin.org/head/abc", s2);
        assertEquals("https://httpbin.org/abc", s3);
        assertEquals("https://httpbin.org/def", s4);
        assertEquals("https://httpbin.org/def", s5);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testProcessEndPointInvalid1() throws Exception {
        HTTPClient hc = new HTTPClient("https://httpbin.org/head", "utf8");
        String s1 = hc.processEndPoint("");

        assertEquals("https://httpbin.org/head", s1);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testProcessEndPointInvalid2() throws Exception {
        HTTPClient hc = new HTTPClient("https://httpbin.org/head", "utf8");
        String s1 = hc.processEndPoint("ftp://httpbin.org/head.zip");

        assertEquals("https://httpbin.org/head", s1);
    }

    @Test
    public void testProcessEndPoint() throws Exception {
        HTTPClient hc = new HTTPClient("https://httpbin.org/head", "utf8");
        String s1 = hc.processEndPoint("http://www.pftx.org/index.html");

        assertEquals("http://www.pftx.org/index.html", s1);
    }

    @Test
    public void testProcessEndPoint2() throws Exception {
        HTTPClient hc = new HTTPClient("https://httpbin.org/head", "utf8");
        String s1 = hc.processEndPoint("http://www.pftx.org/");

        assertEquals("http://www.pftx.org", s1);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testProcessEndPointInvalidQ() throws Exception {
        HTTPClient hc = new HTTPClient("https://httpbin.org/head", "utf8");
        String s1 = hc.processEndPoint("http://www.pftx.org/index.html?name=lex");

        assertEquals("http://www.pftx.org/index.html", s1);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testProcessEndPointInvalidR() throws Exception {
        HTTPClient hc = new HTTPClient("https://httpbin.org/head", "utf8");
        String s1 = hc.processEndPoint("http://www.pftx.org/index.html#ref2");

        assertEquals("http://www.pftx.org/index.html", s1);
    }

}
