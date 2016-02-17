package org.apache.niolex.commons.net;


import static org.junit.Assert.*;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Lists;

public class HTTPResultTest {
    Map<String, List<String>> respHeaders = new HashMap<String, List<String>>();
    HTTPClient cli = new HTTPClient("http://finance.sina.com.cn/realstock/company/sh000001/nc.shtml", "utf8");
    String bodyStr = "U4vVYR4O4NBkZXUj";
    HTTPResult r = new HTTPResult(202, respHeaders, bodyStr.getBytes(), cli);

    @Test
    public void testHTTPResult() throws Exception {
        //int respCode, Map<String, List<String>> respHeaders, byte[] respBody, HTTPClient client
        assertEquals(cli, r.client());
    }

    @Test
    public void testGetRespCode() throws Exception {
        assertEquals(202, r.getRespCode());
    }

    @Test
    public void testGetRespHeaders() throws Exception {
        assertEquals(respHeaders, r.getRespHeaders());
    }

    @Test
    public void testGetRespBody() throws Exception {
        assertArrayEquals(bodyStr.getBytes(), r.getRespBody());
    }

    @Test
    public void testGetRespBodyStr() throws Exception {
        String q = r.getRespBodyStr();
        assertEquals(bodyStr, q);
        assertEquals(bodyStr, r.getRespBodyStr());
        assertFalse(q == bodyStr);
        assertTrue(q == r.getRespBodyStr());
    }

    @Test
    public void testClient() throws Exception {
        URL u = new URL("HTTPS://localhost/cgi-bin/frame_html?sid=U4vVYR4O4NBkZXUj&t=newwin_frame");
        System.out.println(u.getProtocol());
        System.out.println(u.getAuthority());
        System.out.println(u.getFile());
        System.out.println(u.getHost());
        System.out.println(u.getPath());
        System.out.println(u.getPort());
        System.out.println(u.getQuery());
        System.out.println(u.getRef());
        System.out.println(u.toExternalForm());
        System.out.println(u.toString());


        String s1 = cli.generateURL("/../../..//../");
        System.out.println(s1);
        String s2 = cli.generateURL("/../../..//../../../../");
        assertEquals(s1, s2);

        String s3 = cli.generateURL("../index.html");
        System.out.println(s3);
        String s4 = cli.generateURL("/../index.html");
        assertEquals(s3, s4);

        String s5 = cli.generateURL("../../index.html");
        System.out.println(s5);
        String s6 = cli.generateURL("./././../../index.html");
        assertEquals(s5, s6);
    }

    @Test
    public void testGetHeader() throws Exception {
        respHeaders.put("Content-Type", Lists.newArrayList("text/html; encoding=gzip"));
        assertNull(r.getHeader("abc"));
        assertNotNull(r.getHeader("Content-Type"));
    }

    @Test
    public void testToString1() throws Exception {
        respHeaders.put(null, Lists.newArrayList("HTTP 1.1 200 OK"));
        assertEquals("[HTTP 1.1 200 OK] Body Size [16]", r.toString());
    }

    @Test
    public void testToString2() throws Exception {
        HTTPResult r2 = new HTTPResult(202, respHeaders, null, cli);
        assertEquals("null Body Size [-1]", r2.toString());
    }

}
