/**
 * HTTPUtilTest.java
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
import static org.mockito.Mockito.*;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLHandshakeException;

import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.net.NetException.ExCode;
import org.apache.niolex.commons.util.SystemUtil;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-7-15
 */
public class HTTPUtilTest extends HTTPUtil {

    @Test
    public void testGet() throws Exception {
        if (SystemUtil.defined("download", "download.http")) return;
        Map<String, String> params = Maps.newHashMap();
        params.put("list", "sh000001");
        String s = get("http://hq.sinajs.cn/", params);
        System.out.println(s);
    }

    @Test(expected=NetException.class)
    public void testGet404() throws Exception {
        if (SystemUtil.defined("download", "download.http")) return;
        String s = get("http://hometown.scau.edu.cn/course/lqbz1/0410251.html");
        System.out.println(s);
    }

    @Test
    public void testGetWithoutEnc() throws Exception {
        if (SystemUtil.defined("download", "download.http")) return;
        String s = get("http://www.zju.edu.cn/");
        assertTrue(s.length() > 1024);
        assertTrue(s.contains("浙江大学"));
    }

    @Test
    public void testGetHasEnc() throws Exception {
        if (SystemUtil.defined("download", "download.http")) return;
        try {
            String s = get("http://www.sogou.com/");
            assertTrue(s.length() > 1024);
            assertTrue(s.contains("搜狗搜索"));
        } catch (NetException e) {
            assertEquals(e.getCode(), ExCode.IOEXCEPTION);
            assertTrue(e.getCause() instanceof SSLHandshakeException);
        }
    }

    @Test
    public void testGetStringParam() throws Exception {
        if (SystemUtil.defined("download", "download.http")) return;
        Map<String, String> params = Maps.newHashMap();
        params.put("wd", "谢佶芸");
        String s = get("http://www.baidu.com/baidu", params);
        assertTrue(s.length() > 1024);
        assertTrue(s.contains("谢佶芸_百度搜索"));
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
        params.put("wd", "commons-core");
        String s = post("http://www.baidu.com/baidu", params);
        assertTrue(s.length() > 1024);
        assertTrue(s.contains("页面不存在_百度搜索"));
    }

    @Test(expected=NetException.class)
    public void testDoHTTPInvalidURL() throws Exception {
        try {
            doHTTP(DownloadUtilTest.JAR, null, null, "", HTTPMethod.GET);
        } catch (NetException e) {
            assertEquals(e.getCode(), NetException.ExCode.INVALID_URL_TYPE);
            throw e;
        }
    }

    @Test(expected=NetException.class)
    public final void testDoHTTPIOE() throws Exception, Throwable {
        try {
            doHTTP("http://search.maven.org/#search%7Cga%7C1%7Cprotobuf-java",
                            1000, 50, HTTPMethod.POST);
        } catch (NetException et) {
            System.out.println("MSG " + et.getMessage());
            assertEquals(et.getCode(), NetException.ExCode.IOEXCEPTION);
            throw et;
        }
    }

    @Test
    public void testDoHTTPNuLLHeader() throws Exception {
        if (SystemUtil.defined("download", "download.http")) return;
        byte[] body = doHTTP("http://view.163.com/", 5000, 3000, HTTPMethod.GET).z;
        String s = StringUtil.gbkByteToStr(body);
        assertTrue(s.length() > 1024);
        assertTrue(s.contains("网易评论频道是网易新闻中心一个包含有另一面"));
    }

    @Test
    public void testInferCharset() throws Exception {
        byte[] body = StringUtil.strToUtf8Byte("<!DOCTYPE html><!--STATUS OK--><html><head><meta http-equiv=\"X-UA-Compatible\" content=\"IE=7\"><meta http-equiv=\"content-type\"" +
                " content=\"text/html;charset=utf-8\"><title>谢佶芸_百度搜索</title><style >body{color:#000;background:#fff;padding:6px 0 0;margin:0;position:relative}body,th,td,.p1,.p" +
                "2{font-family:arial}p,form,ol,ul,li,dl,dt,dd,h3{margin:0;padding:0;list-style:none}input{padding-top:0;padding-bottom:0;-moz-box-sizing:border-box;-webkit-box-sizin" +
                "g:border-box;box-sizing:border-box}table,img{border:0}td{font-size:9pt;line-height:18px}em{font-style:normal;color:#cc0000}a em{text-decoration:underline}cite{font-" +
                "style:normal;color:#008000}.m,a.m{color:#666}a.m:visited{color:#606}.g,a.g{color:#008000}.c{color:#77c}.f14{font-size:14px}.f10{font-size:10.5pt}.f16{font-size:16px" +
                "}.f13{font-size:13px}#u,#head,#tool,#search,#foot{font-size:12px}.p1{line-height:120%;margin-left:-12pt}");
        Map<String, List<String>> headers = Maps.newHashMap();
        headers.put("Content-Type", Lists.newArrayList("text/html;charset=utf-8"));
        Charset cs = inferCharset(headers, body);
        assertEquals(cs, Charsets.UTF_8);
    }

    @Test
    public void testInferCharsetAlias() throws Exception {
        byte[] body = StringUtil.strToUtf8Byte("<!DOCTYPE html><!--STATUS OK--><html><head><meta http-equiv=\"X-UA-Compatible\" content=\"IE=7\"><meta http-equiv=\"content-type\"" +
                " content=\"text/html;charset=utf-8\"><title>谢佶芸_百度搜索</title><style >body{color:#000;background:#fff;padding:6px 0 0;margin:0;position:relative}body,th,td,.p1,.p" +
                "2{font-family:arial}p,form,ol,ul,li,dl,dt,dd,h3{margin:0;padding:0;list-style:none}input{padding-top:0;padding-bottom:0;-moz-box-sizing:border-box;-webkit-box-sizin" +
                "g:border-box;box-sizing:border-box}table,img{border:0}td{font-size:9pt;line-height:18px}em{font-style:normal;color:#cc0000}a em{text-decoration:underline}cite{font-" +
                "style:normal;color:#008000}.m,a.m{color:#666}a.m:visited{color:#606}.g,a.g{color:#008000}.c{color:#77c}.f14{font-size:14px}.f10{font-size:10.5pt}.f16{font-size:16px" +
                "}.f13{font-size:13px}#u,#head,#tool,#search,#foot{font-size:12px}.p1{line-height:120%;margin-left:-12pt}");
        Map<String, List<String>> headers = Maps.newHashMap();
        headers.put("content-type", Lists.newArrayList("text/html;charset=utf8"));
        Charset cs = inferCharset(headers, body);
        assertEquals(cs, Charsets.UTF_8);
    }

    @Test
    public void testInferCharsetIso() throws Exception {
        byte[] body = StringUtil.strToUtf8Byte("<!DOCTYPE html><!--STATUS OK--><html><head><meta http-equiv=\"X-UA-Compatible\" content=\"IE=7\"><meta http-equiv=\"content-type\"" +
                " content=\"text/html;charset=utf-8\"><title>谢佶芸_百度搜索</title><style >body{color:#000;background:#fff;padding:6px 0 0;margin:0;position:relative}body,th,td,.p1,.p" +
                "2{font-family:arial}p,form,ol,ul,li,dl,dt,dd,h3{margin:0;padding:0;list-style:none}input{padding-top:0;padding-bottom:0;-moz-box-sizing:border-box;-webkit-box-sizin" +
                "g:border-box;box-sizing:border-box}table,img{border:0}td{font-size:9pt;line-height:18px}em{font-style:normal;color:#cc0000}a em{text-decoration:underline}cite{font-" +
                "style:normal;color:#008000}.m,a.m{color:#666}a.m:visited{color:#606}.g,a.g{color:#008000}.c{color:#77c}.f14{font-size:14px}.f10{font-size:10.5pt}.f16{font-size:16px" +
                "}.f13{font-size:13px}#u,#head,#tool,#search,#foot{font-size:12px}.p1{line-height:120%;margin-left:-12pt}");
        Map<String, List<String>> headers = Maps.newHashMap();
        headers.put("Content-Length", Lists.newArrayList("12321"));
        headers.put("CONTENT-TYPE", Lists.newArrayList("text/html;charset=ISO-8859-1"));
        Charset cs = inferCharset(headers, body);
        assertEquals(cs, Charsets.ISO_8859_1);
    }

    @Test
    public void testInferCharsetBody() throws Exception {
        byte[] body = StringUtil.strToUtf8Byte("<!DOCTYPE html><!--STATUS OK--><html><head><meta http-equiv=\"X-UA-Compatible\" content=\"IE=7\"><meta http-equiv=\"content-type\"" +
                " content=\"text/html;charset=gbk\"><title>谢佶芸_百度搜索</title><style >body{color:#000;background:#fff;padding:6px 0 0;margin:0;position:relative}body,th,td,.p1,.p" +
                "2{font-family:arial}p,form,ol,ul,li,dl,dt,dd,h3{margin:0;padding:0;list-style:none}input{padding-top:0;padding-bottom:0;-moz-box-sizing:border-box;-webkit-box-sizin" +
                "g:border-box;box-sizing:border-box}table,img{border:0}td{font-size:9pt;line-height:18px}em{font-style:normal;color:#cc0000}a em{text-decoration:underline}cite{font-" +
                "style:normal;color:#008000}.m,a.m{color:#666}a.m:visited{color:#606}.g,a.g{color:#008000}.c{color:#77c}.f14{font-size:14px}.f10{font-size:10.5pt}.f16{font-size:16px" +
                "}.f13{font-size:13px}#u,#head,#tool,#search,#foot{font-size:12px}.p1{line-height:120%;margin-left:-12pt}");
        Map<String, List<String>> headers = Maps.newHashMap();
        headers.put("Server", Lists.newArrayList("A-Nginx"));
        headers.put("Content-Type", Lists.newArrayList("text/html"));
        Charset cs = inferCharset(headers, body);
        assertEquals(cs, Charset.forName("GBK"));
    }

    @Test
    public void testInferCharsetBodyAnother() throws Exception {
        byte[] body = StringUtil.strToUtf8Byte("<!DOCTYPE html><!--STATUS OK--><html><head><meta http-equiv=\"X-UA-Compatible\" content=\"IE=7\"><meta http-equiv=\"content-type\"" +
                " content=\"text/html;charset=\"ascii\"><title>谢佶芸_百度搜索</title><style >body{color:#000;background:#fff;padding:6px 0 0;margin:0;position:relative}body,th,td,.p1,.p" +
                "2{font-family:arial}p,form,ol,ul,li,dl,dt,dd,h3{margin:0;padding:0;list-style:none}input{padding-top:0;padding-bottom:0;-moz-box-sizing:border-box;-webkit-box-sizin" +
                "g:border-box;box-sizing:border-box}table,img{border:0}td{font-size:9pt;line-height:18px}em{font-style:normal;color:#cc0000}a em{text-decoration:underline}cite{font-" +
                "style:normal;color:#008000}.m,a.m{color:#666}a.m:visited{color:#606}.g,a.g{color:#008000}.c{color:#77c}.f14{font-size:14px}.f10{font-size:10.5pt}.f16{font-size:16px" +
                "}.f13{font-size:13px}#u,#head,#tool,#search,#foot{font-size:12px}.p1{line-height:120%;margin-left:-12pt}");
        Map<String, List<String>> headers = Maps.newHashMap();
        Charset cs = inferCharset(headers, body);
        assertEquals(cs, Charset.forName("ASCII"));
    }

    @Test
    public void testInferCharsetBodySingleQuote() throws Exception {
        byte[] body = StringUtil.strToUtf8Byte("<!DOCTYPE html><!--STATUS OK--><html><head><meta http-equiv=\"X-UA-Compatible\" content=\"IE=7\"><meta http-equiv=\"content-type\"" +
                " content=\"text/html;charset='ascii'\"><title>谢佶芸_百度搜索</title><style >body{color:#000;background:#fff;padding:6px 0 0;margin:0;position:relative}body,th,td,.p1,.p" +
                "2{font-family:arial}p,form,ol,ul,li,dl,dt,dd,h3{margin:0;padding:0;list-style:none}input{padding-top:0;padding-bottom:0;-moz-box-sizing:border-box;-webkit-box-sizin" +
                "g:border-box;box-sizing:border-box}table,img{border:0}td{font-size:9pt;line-height:18px}em{font-style:normal;color:#cc0000}a em{text-decoration:underline}cite{font-" +
                "style:normal;color:#008000}.m,a.m{color:#666}a.m:visited{color:#606}.g,a.g{color:#008000}.c{color:#77c}.f14{font-size:14px}.f10{font-size:10.5pt}.f16{font-size:16px" +
                "}.f13{font-size:13px}#u,#head,#tool,#search,#foot{font-size:12px}.p1{line-height:120%;margin-left:-12pt}");
        Map<String, List<String>> headers = Maps.newHashMap();
        Charset cs = inferCharset(headers, body);
        assertEquals(cs, Charset.forName("ASCII"));
    }

    @Test
    public void testInferCharsetDefault() throws Exception {
        byte[] body = StringUtil.strToUtf8Byte("<!DOCTYPE html><!--STATUS OK--><html><head><meta http-equiv=\"X-UA-Compatible\" content=\"IE=7\"><meta http-equiv=\"content-type\"" +
                " content=\"text/html;chars$et=gbk\"><title>谢佶芸_百度搜索</title><style >body{color:#000;background:#fff;padding:6px 0 0;margin:0;position:relative}body,th,td,.p1,.p" +
                "2{font-family:arial}p,form,ol,ul,li,dl,dt,dd,h3{margin:0;padding:0;list-style:none}input{padding-top:0;padding-bottom:0;-moz-box-sizing:border-box;-webkit-box-sizin" +
                "g:border-box;box-sizing:border-box}table,img{border:0}td{font-size:9pt;line-height:18px}em{font-style:normal;color:#cc0000}a em{text-decoration:underline}cite{font-" +
                "style:normal;color:#008000}.m,a.m{color:#666}a.m:visited{color:#606}.g,a.g{color:#008000}.c{color:#77c}.f14{font-size:14px}.f10{font-size:10.5pt}.f16{font-size:16px" +
                "}.f13{font-size:13px}#u,#head,#tool,#search,#foot{font-size:12px}.p1{line-height:120%;margin-left:-12pt}");
        Map<String, List<String>> headers = Maps.newHashMap();
        headers.put("Server", Lists.newArrayList("A-Nginx"));
        headers.put("Content-Type", null);
        Charset cs = inferCharset(headers, body);
        assertEquals(cs, Charsets.UTF_8);
    }

    @Test
    public void testInferCharsetDefaultMax() throws Exception {
        byte[] body = StringUtil.strToUtf8Byte("<!DOCTYPE html><!--STATUS OK--><html><head><meta http-equiv=\"X-UA-Compatible\" content=\"IE=7\"><meta http-equiv=\"content-type\"" +
                " content=\"text/html;chars$et=gbk\"><title>谢佶芸_百度搜索</title><style >body{color:#000;background:#fff;padding:6px 0 0;margin:0;position:relative}body,th,td,.p1,.p" +
                "2{font-family:arial}p,form,ol,ul,li,dl,dt,dd,h3{margin:0;padding:0;list-style:none}input{pcharset='gb2312");
        Map<String, List<String>> headers = Maps.newHashMap();
        headers.put("Server", Lists.newArrayList("A-Nginx"));
        headers.put("Content-Type", null);
        Charset cs = inferCharset(headers, body);
        assertEquals(cs, Charset.forName("gb2312"));
    }

    @Test
    public void testInferCharsetSogou() throws Exception {
        byte[] body = StringUtil.strToUtf8Byte("<!DOCTYPE HTML><script>var _speedMark=new Date</script><meta charset=utf-8><title>搜狗搜索引擎 - 上网从搜狗开始</title><link href=" +
                    "\"/images/logo/new/favicon.ico?v=2\" rel=\"shortcut icon\" type=image/x-icon><meta content=\"IE=Edge\" http-equiv=X-UA-Compatible><meta content=搜狗搜索,网页" +
                "搜索,微信搜索,视频搜索,图片搜索,音乐搜索,新闻搜索,软件搜索,问答搜索,百科搜索,购物搜索 name=keywords><meta content=中国最领先的中文搜索引擎，支持微信公众号、" +
                    "文章搜索，通过独有的SogouRank技术及人工智能算法为您提供最快、最准、最全的搜索服务。 name=description><style>");
        Map<String, List<String>> headers = Maps.newHashMap();
        headers.put("Server", Lists.newArrayList("A-Nginx"));
        headers.put("Content-Type", null);
        Charset cs = inferCharset(headers, body);
        assertEquals(cs, Charset.forName("utf8"));
    }

    @Test
    public void testPrepareWwwFormUrlEncoded() throws Exception {
        Map<String, String> params = Maps.newTreeMap();
        params.put("cache-control", "no-cache");
        params.put("XXS", "Lex");
        params.put("Host", "apache");
        String s = prepareWwwFormUrlEncoded(params, "ASCII");
        assertEquals(s, "Host=apache&XXS=Lex&cache-control=no-cache");
    }

    @Test
    public void testPrepareWwwFormUrlEncodedNullKey() throws Exception {
        String s = prepareWwwFormUrlEncoded(null, "GBK");
        assertEquals(s, "");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testPrepareWwwFormUrlEncodedInvalidCharset() throws Exception {
        Map<String, String> params = Maps.newHashMap();
        params.put("cache-control", "no-cache");
        params.put("XXS", "Lex");
        params.put("Host", "apache");
        String s = prepareWwwFormUrlEncoded(params, "ASCIII");
        assertEquals(s, "Host=apache&XXS=Lex");
    }

    @Test
    public void testPrepareWwwFormUrlEncodedEmpty() throws Exception {
        Map<String, String> params = Maps.newHashMap();
        String s = prepareWwwFormUrlEncoded(params, "utf8");
        assertTrue(s.isEmpty());
    }

    @Test
    public void testPrepareWwwFormUrlSingle() throws Exception {
        Map<String, String> params = Maps.newHashMap();
        params.put("Host", "apache");
        String s = prepareWwwFormUrlEncoded(params, "ascii");
        assertEquals(s, "Host=apache");
    }

    @Test
    public void testCleanupHttpURLConnection() throws Exception {
        HttpURLConnection con = mock(HttpURLConnection.class);
        cleanupHttpURLConnection(true, null);
        cleanupHttpURLConnection(false, null);
        cleanupHttpURLConnection(true, con);
        cleanupHttpURLConnection(false, con);
        verify(con, times(1)).disconnect();
    }

    @Test
    public void testCheckAndDownloadDataNull() throws Exception {
        byte[] ret = checkAndDownloadData("not yet implemented", 500, null);
        assertNull(ret);
    }

    @Test
    public void testCheckAndDownloadDataZero() throws Exception {
        InputStream input = mock(InputStream.class);
        byte[] ret = checkAndDownloadData("not yet implemented", 0, input);
        assertNull(ret);
    }

    @Test
    public void testRetrieveCharsetFromString() throws Exception {
        Charset charset = retrieveCharsetFromString("=utf-8>");
        assertEquals(StringUtil.UTF_8, charset);
    }

    @Test(expected=IllegalCharsetNameException.class)
    public void testRetrieveCharsetFromStringInvalid() throws Exception {
        retrieveCharsetFromString("'='");
    }

    @Test
    public final void testCheckServerStatusLenMinus1() {
        int b = checkServerStatus("http://www.baidu.com", 4000, 4000);
        assertEquals(200, b);
    }

    @Test
    public final void testCheckServerStatusLenMinus2() {
        int b = checkServerStatus("http://httpbin.org/stream/20", 4000, 4000);
        assertEquals(200, b);
    }

    @Test
    public final void testCheckServerStatusLessThan2() {
        String url = HTTPUtilTest.class.getResource("onebyte.txt").toExternalForm();
        int d = checkServerStatus(url, 4000, 4000);
        assertEquals(-2, d);
    }

    @Test
    public final void testCheckServerStatusEx403() {
        int c = checkServerStatus("http://httpbin.org/status/403", 4000, 4000);
        assertEquals(403, c);
    }

    @Test
    public final void testCheckServerStatusEx404() {
        int e = checkServerStatus("http://www.cs.zju.edu.cn/org/codes/404.html", 4000, 4000);
        assertEquals(404, e);
    }

    @Test
    public final void testCheckServerStatusEx404Third() {
        int c = checkServerStatus("http://httpbin.org/status/404", 4000, 4000);
        assertEquals(404, c);
    }

    @Test
    public final void testCheckServerStatusIOEx() {
        int f = checkServerStatus("http://www.facebook.com", 1000, 1000);
        assertEquals(-1, f);
    }

    @Test(expected=IllegalArgumentException.class)
    public final void testCheckServerStatusNull() {
        int f = checkServerStatus("http://www.facebook.com", -1000, -1000);
        assertEquals(404, f);
    }

    @Test
    public void testCheckServerStatus() throws Exception {
        int f = checkServerStatus("http://httpbin.org/ip", 2000, 3000);
        assertEquals(200, f);
    }

}
