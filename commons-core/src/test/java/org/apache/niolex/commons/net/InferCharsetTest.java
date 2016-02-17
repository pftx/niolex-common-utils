/**
 * InferCharsetTest.java
 *
 * Copyright 2016 the original author or authors.
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

import static org.junit.Assert.assertEquals;

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;
import java.util.Map;

import org.apache.niolex.commons.codec.StringUtil;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2016-2-16
 */
public class InferCharsetTest extends HTTPUtil {

    @Test
    public void testInferCharsetFromHeader1() throws Exception {
        byte[] body = StringUtil.strToUtf8Byte("<!DOCTYPE html><!--STATUS OK--><html><head><meta http-equiv=\"X-UA-Compatible\" content=\"IE=7\"><meta http-equiv=\"content-type\"" +
                " content=\"text/html;charset=utf-8\"><title>谢佶芸_百度搜索</title><style >body{color:#000;background:#fff;padding:6px 0 0;margin:0;position:relative}body,th,td,.p1,.p" +
                "2{font-family:arial}p,form,ol,ul,li,dl,dt,dd,h3{margin:0;padding:0;list-style:none}input{padding-top:0;padding-bottom:0;-moz-box-sizing:border-box;-webkit-box-sizin" +
                "g:border-box;box-sizing:border-box}table,img{border:0}td{font-size:9pt;line-height:18px}em{font-style:normal;color:#cc0000}a em{text-decoration:underline}cite{font-" +
                "style:normal;color:#008000}.m,a.m{color:#666}a.m:visited{color:#606}.g,a.g{color:#008000}.c{color:#77c}.f14{font-size:14px}.f10{font-size:10.5pt}.f16{font-size:16px" +
                "}.f13{font-size:13px}#u,#head,#tool,#search,#foot{font-size:12px}.p1{line-height:120%;margin-left:-12pt}");
        Map<String, List<String>> headers = Maps.newHashMap();
        headers.put("Content-Type", Lists.newArrayList("text/html;charset=gbk"));
        Charset cs = inferCharset(headers, body);
        assertEquals(cs, StringUtil.GBK);
    }

    @Test
    public void testInferCharsetFromHeader2() throws Exception {
        byte[] body = StringUtil.strToUtf8Byte("<!DOCTYPE html><!--STATUS OK--><html><head><meta http-equiv=\"X-UA-Compatible\" content=\"IE=7\"><meta http-equiv=\"content-type\"" +
                " content=\"text/html;charset=utf-8\"><title>谢佶芸_百度搜索</title><style >body{color:#000;background:#fff;padding:6px 0 0;margin:0;position:relative}body,th,td,.p1,.p" +
                "2{font-family:arial}p,form,ol,ul,li,dl,dt,dd,h3{margin:0;padding:0;list-style:none}input{padding-top:0;padding-bottom:0;-moz-box-sizing:border-box;-webkit-box-sizin" +
                "g:border-box;box-sizing:border-box}table,img{border:0}td{font-size:9pt;line-height:18px}em{font-style:normal;color:#cc0000}a em{text-decoration:underline}cite{font-" +
                "style:normal;color:#008000}.m,a.m{color:#666}a.m:visited{color:#606}.g,a.g{color:#008000}.c{color:#77c}.f14{font-size:14px}.f10{font-size:10.5pt}.f16{font-size:16px" +
                "}.f13{font-size:13px}#u,#head,#tool,#search,#foot{font-size:12px}.p1{line-height:120%;margin-left:-12pt}");
        Map<String, List<String>> headers = Maps.newHashMap();
        headers.put("Content-Type", Lists.newArrayList("text/html;charset = gbk"));
        Charset cs = inferCharset(headers, body);
        assertEquals(cs, StringUtil.GBK);
    }

    @Test
    public void testInferCharsetFromHeader3() throws Exception {
        byte[] body = StringUtil.strToUtf8Byte("<!DOCTYPE html><!--STATUS OK--><html><head><meta http-equiv=\"X-UA-Compatible\" content=\"IE=7\"><meta http-equiv=\"content-type\"" +
                " content=\"text/html;charset=utf-8\"><title>谢佶芸_百度搜索</title><style >body{color:#000;background:#fff;padding:6px 0 0;margin:0;position:relative}body,th,td,.p1,.p" +
                "2{font-family:arial}p,form,ol,ul,li,dl,dt,dd,h3{margin:0;padding:0;list-style:none}input{padding-top:0;padding-bottom:0;-moz-box-sizing:border-box;-webkit-box-sizin" +
                "g:border-box;box-sizing:border-box}table,img{border:0}td{font-size:9pt;line-height:18px}em{font-style:normal;color:#cc0000}a em{text-decoration:underline}cite{font-" +
                "style:normal;color:#008000}.m,a.m{color:#666}a.m:visited{color:#606}.g,a.g{color:#008000}.c{color:#77c}.f14{font-size:14px}.f10{font-size:10.5pt}.f16{font-size:16px" +
                "}.f13{font-size:13px}#u,#head,#tool,#search,#foot{font-size:12px}.p1{line-height:120%;margin-left:-12pt}");
        Map<String, List<String>> headers = Maps.newHashMap();
        headers.put("Content-Type", Lists.newArrayList("text/json"));
        headers.put("content-type", Lists.newArrayList("text/html;charset = \"gbk\""));
        Charset cs = inferCharset(headers, body);
        assertEquals(cs, StringUtil.GBK);
    }

    @Test
    public void testInferCharsetFromHeader4() throws Exception {
        byte[] body = StringUtil.strToUtf8Byte("<!DOCTYPE html><!--STATUS OK--><html><head><meta http-equiv=\"X-UA-Compatible\" content=\"IE=7\"><meta http-equiv=\"content-type\"" +
                " content=\"text/html;charset=utf-8\"><title>谢佶芸_百度搜索</title><style >body{color:#000;background:#fff;padding:6px 0 0;margin:0;position:relative}body,th,td,.p1,.p" +
                "2{font-family:arial}p,form,ol,ul,li,dl,dt,dd,h3{margin:0;padding:0;list-style:none}input{padding-top:0;padding-bottom:0;-moz-box-sizing:border-box;-webkit-box-sizin" +
                "g:border-box;box-sizing:border-box}table,img{border:0}td{font-size:9pt;line-height:18px}em{font-style:normal;color:#cc0000}a em{text-decoration:underline}cite{font-" +
                "style:normal;color:#008000}.m,a.m{color:#666}a.m:visited{color:#606}.g,a.g{color:#008000}.c{color:#77c}.f14{font-size:14px}.f10{font-size:10.5pt}.f16{font-size:16px" +
                "}.f13{font-size:13px}#u,#head,#tool,#search,#foot{font-size:12px}.p1{line-height:120%;margin-left:-12pt}");
        Map<String, List<String>> headers = Maps.newHashMap();
        headers.put("Auth-scheme", Lists.newArrayList("go again"));
        headers.put("COntent-type", Lists.newArrayList("text/html;charset = 'gbk'"));
        Charset cs = inferCharset(headers, body);
        assertEquals(cs, StringUtil.GBK);
    }

    @Test(expected=UnsupportedCharsetException.class)
    public void testInferCharsetFromHeader5() throws Exception {
        byte[] body = StringUtil.strToUtf8Byte("<!DOCTYPE html><!--STATUS OK--><html><head><meta http-equiv=\"X-UA-Compatible\" content=\"IE=7\"><meta http-equiv=\"content-type\"" +
                " content=\"text/html;charset=utf-8\"><title>谢佶芸_百度搜索</title><style >body{color:#000;background:#fff;padding:6px 0 0;margin:0;position:relative}body,th,td,.p1,.p" +
                "2{font-family:arial}p,form,ol,ul,li,dl,dt,dd,h3{margin:0;padding:0;list-style:none}input{padding-top:0;padding-bottom:0;-moz-box-sizing:border-box;-webkit-box-sizin" +
                "g:border-box;box-sizing:border-box}table,img{border:0}td{font-size:9pt;line-height:18px}em{font-style:normal;color:#cc0000}a em{text-decoration:underline}cite{font-" +
                "style:normal;color:#008000}.m,a.m{color:#666}a.m:visited{color:#606}.g,a.g{color:#008000}.c{color:#77c}.f14{font-size:14px}.f10{font-size:10.5pt}.f16{font-size:16px" +
                "}.f13{font-size:13px}#u,#head,#tool,#search,#foot{font-size:12px}.p1{line-height:120%;margin-left:-12pt}");
        Map<String, List<String>> headers = Maps.newHashMap();
        headers.put("content-type", Lists.newArrayList("text/html;charset='a'"));
        Charset cs = inferCharset(headers, body);
        assertEquals(cs, StringUtil.GBK);
    }

    @Test
    public void testInferCharsetFromHeader6() throws Exception {
        byte[] body = StringUtil.strToUtf8Byte("<!DOCTYPE html><!--STATUS OK--><html><head><meta http-equiv=\"X-UA-Compatible\" content=\"IE=7\"><meta http-equiv=\"content-type\"" +
                " content=\"text/html;charset=utf-8\"><title>谢佶芸_百度搜索</title><style >body{color:#000;background:#fff;padding:6px 0 0;margin:0;position:relative}body,th,td,.p1,.p" +
                "2{font-family:arial}p,form,ol,ul,li,dl,dt,dd,h3{margin:0;padding:0;list-style:none}input{padding-top:0;padding-bottom:0;-moz-box-sizing:border-box;-webkit-box-sizin" +
                "g:border-box;box-sizing:border-box}table,img{border:0}td{font-size:9pt;line-height:18px}em{font-style:normal;color:#cc0000}a em{text-decoration:underline}cite{font-" +
                "style:normal;color:#008000}.m,a.m{color:#666}a.m:visited{color:#606}.g,a.g{color:#008000}.c{color:#77c}.f14{font-size:14px}.f10{font-size:10.5pt}.f16{font-size:16px" +
                "}.f13{font-size:13px}#u,#head,#tool,#search,#foot{font-size:12px}.p1{line-height:120%;margin-left:-12pt}");
        Map<String, List<String>> headers = Maps.newHashMap();
        headers.put("Content-Type", Lists.newArrayList("text/html; charset=gbk; encoding=gzip"));
        Charset cs = inferCharset(headers, body);
        assertEquals(cs, StringUtil.GBK);
    }

    @Test
    public void testInferCharsetFromBody1() throws Exception {
        byte[] body = StringUtil.strToUtf8Byte("<!DOCTYPE html><!--STATUS OK--><html><head><meta http-equiv=\"X-UA-Compatible\" content=\"IE=7\"><meta http-equiv=\"content-type\"" +
                " content=\"text/html;charset=utf-8\"><title>谢佶芸_百度搜索</title><style >body{color:#000;background:#fff;padding:6px 0 0;margin:0;position:relative}body,th,td,.p1,.p" +
                "2{font-family:arial}p,form,ol,ul,li,dl,dt,dd,h3{margin:0;padding:0;list-style:none}input{padding-top:0;padding-bottom:0;-moz-box-sizing:border-box;-webkit-box-sizin" +
                "g:border-box;box-sizing:border-box}table,img{border:0}td{font-size:9pt;line-height:18px}em{font-style:normal;color:#cc0000}a em{text-decoration:underline}cite{font-" +
                "style:normal;color:#008000}.m,a.m{color:#666}a.m:visited{color:#606}.g,a.g{color:#008000}.c{color:#77c}.f14{font-size:14px}.f10{font-size:10.5pt}.f16{font-size:16px" +
                "}.f13{font-size:13px}#u,#head,#tool,#search,#foot{font-size:12px}.p1{line-height:120%;margin-left:-12pt}");
        Map<String, List<String>> headers = Maps.newHashMap();
        List<String> list = Lists.newArrayList();
        headers.put("Content-Type", list);
        Charset cs = inferCharset(headers, body);
        assertEquals(cs, StringUtil.UTF_8);
    }

    @Test
    public void testInferCharsetFromBody2() throws Exception {
        byte[] body = StringUtil.strToUtf8Byte("<!DOCTYPE html><!--STATUS OK--><html><head><meta http-equiv=\"X-UA-Compatible\" content=\"IE=7\"><meta http-equiv=\"content-type\"" +
                " content=\"text/html;charset=utf8\"><title>谢佶芸_百度搜索</title><style >body{color:#000;background:#fff;padding:6px 0 0;margin:0;position:relative}body,th,td,.p1,.p" +
                "2{font-family:arial}p,form,ol,ul,li,dl,dt,dd,h3{margin:0;padding:0;list-style:none}input{padding-top:0;padding-bottom:0;-moz-box-sizing:border-box;-webkit-box-sizin" +
                "g:border-box;box-sizing:border-box}table,img{border:0}td{font-size:9pt;line-height:18px}em{font-style:normal;color:#cc0000}a em{text-decoration:underline}cite{font-" +
                "style:normal;color:#008000}.m,a.m{color:#666}a.m:visited{color:#606}.g,a.g{color:#008000}.c{color:#77c}.f14{font-size:14px}.f10{font-size:10.5pt}.f16{font-size:16px" +
                "}.f13{font-size:13px}#u,#head,#tool,#search,#foot{font-size:12px}.p1{line-height:120%;margin-left:-12pt}");
        Map<String, List<String>> headers = Maps.newHashMap();
        headers.put("Encoding-Type", Lists.newArrayList("text/html; chars=gbk"));
        Charset cs = inferCharset(headers, body);
        assertEquals(cs, StringUtil.UTF_8);
    }

    @Test
    public void testInferCharsetFromBody3() throws Exception {
        byte[] body = StringUtil.strToUtf8Byte("<!DOCTYPE html><!--STATUS OK--><html><head><meta http-equiv=\"X-UA-Compatible\" content=\"IE=7\"><meta http-equiv=\"content-type\"" +
                " content=\"text/html;charset=UTF16\">:120%;margin-left:-12pt}");
        Map<String, List<String>> headers = Maps.newHashMap();
        Charset cs = inferCharset(headers, body);
        assertEquals(cs, StringUtil.UTF_16);
    }

    @Test
    public void testInferCharsetFromBody4() throws Exception {
        byte[] body = StringUtil.strToUtf8Byte("<!DOCTYPE html><!--STATUS OK--><html><head><meta http-equiv=\"X-UA-Compatible\" content=\"IE=7\"><meta http-equiv=\"content-type\"" +
                " content=\"text/html;chaRseT=UTF-16 \">:120%;margin-left:-12pt}");
        Map<String, List<String>> headers = Maps.newHashMap();
        headers.put("Content-Type", Lists.newArrayList("text/html; encoding=gzip"));
        Charset cs = inferCharset(headers, body);
        assertEquals(cs, StringUtil.UTF_16);
    }

    @Test
    public void testInferCharsetFromBody5() throws Exception {
        byte[] body = StringUtil.strToUtf8Byte("<!DOCTYPE html><!--STATUS OK--><html><head><meta http-equiv=\"X-UA-Compatible\" content=\"IE=7\"><meta http-equiv=\"content-type\"" +
                " content=\"text/html;chaRseT=Utf-16 ");
        Map<String, List<String>> headers = Maps.newHashMap();
        headers.put("Content-Type", Lists.newArrayList("text/html; encoding=gzip"));
        Charset cs = inferCharset(headers, body);
        assertEquals(cs, StringUtil.UTF_16);
    }

    @Test
    public void testInferCharsetFromNothing() throws Exception {
        byte[] body = StringUtil.strToUtf8Byte("<!DOCTYPE html><!--STATUS OK--><html><head><meta http-equiv=\"X-UA-Compatible\" content=\"IE=7\"><meta http-equiv=\"content-type\"" +
                " content=\"text/html;chaReT=UTF-16 \">:120%;margin-left:-12pt}");
        Map<String, List<String>> headers = Maps.newHashMap();
        headers.put("Content-Type", Lists.newArrayList("text/html; encoding=gzip"));
        Charset cs = inferCharset(headers, body);
        assertEquals(cs, StringUtil.UTF_8);
    }


    @Test(expected=IllegalCharsetNameException.class)
    public void testRetrieveCharsetFromString() throws Exception {
        retrieveCharsetFromString("'\" ='");
    }
}
