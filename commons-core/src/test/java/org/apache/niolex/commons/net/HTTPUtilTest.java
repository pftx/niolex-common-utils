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

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.niolex.commons.codec.StringUtil;
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
    public void testGetWithoutEnc() throws Exception {
        String s = get("http://www.zju.edu.cn/");
        assertTrue(s.length() > 1024);
        assertTrue(s.contains("浙江大学"));
    }

    @Test
    public void testGetHasEnc() throws Exception {
        String s = get("http://www.baidu.com/");
        assertTrue(s.length() > 1024);
        assertTrue(s.contains("百度一下，你就知道"));
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
    public void testGetStringParam() throws Exception {
        Map<String, String> params = Maps.newHashMap();
        params.put("wd", "谢佶芸");
        String s = get("http://www.baidu.com/baidu", params);
        assertTrue(s.length() > 1024);
        assertTrue(s.contains("谢佶芸_百度搜索"));
    }

    @Test
    public void testGet() throws Exception {
        Map<String, String> params = Maps.newHashMap();
        params.put("list", "sh000001");
        String s = get("http://hq.sinajs.cn/", params);
        System.out.println(s);
    }

    @Test
    public void testPostServerE() throws Exception {
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
        assertTrue(s.contains("百度--您的访问出错了"));
    }

    @Test(expected=NetException.class)
    public void testIOE() throws Exception {
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
        String s = post("http://www.baidu.com/s", params);
        System.out.println(s);
    }

    @Test(expected=NetException.class)
    public void testHTTP() throws Exception {
        doHTTP("ftp://ftp:ftp@ftp.speed.hinet.net/test_001m.zip", null, true);
    }

    @Test
    public void testHTTPNuLLHeader() throws Exception {
        byte[] body = doHTTP("http://www.360.cn/", null, null, 3000, 3000, false).b;
        String s = StringUtil.gbkByteToStr(body);
        assertTrue(s.length() > 1024);
        assertTrue(s.contains("360安全中心"));
    }

    @Test
    public void testPrepareWwwFormUrlEncoded() throws Exception {
        Map<String, String> params = Maps.newHashMap();
        String s = prepareWwwFormUrlEncoded(params);
        assertTrue(s.isEmpty());
    }

    @Test
    public void testPrepareWwwFormUrlEncodedNullKey() throws Exception {
        Map<String, String> params = Maps.newHashMap();
        params.put(null, "no-cache");
        params.put("XXS", "Lex");
        params.put("Host", "apache");
        String s = prepareWwwFormUrlEncoded(params);
        assertEquals(s, "Host=apache&XXS=Lex");
    }

    @Test
    public void testPrepareWwwFormUrlSingle() throws Exception {
        Map<String, String> params = Maps.newHashMap();
        params.put("Host", "apache");
        String s = prepareWwwFormUrlEncoded(params);
        assertEquals(s, "Host=apache");
    }

}