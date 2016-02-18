/**
 * SimpleHttpServer.java
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.Executors;

import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.file.FileUtil;
import org.apache.niolex.commons.stream.StreamUtil;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-7-16
 */
@SuppressWarnings("restriction")
public class SimpleHttpServer {

    public static final byte[] DATA = FileUtil.getBinaryFileContentFromClassPath("nav.jpg.txt", NetExceptionTest.class);;
    public static HttpServer server;

    public static void main(String[] args) throws Exception {
        //addr, backlog
        server = HttpServer.create(new InetSocketAddress(8985), 100);
        server.createContext("/", new RedirectHandler());
        server.createContext("/info", new InfoHandler());
        server.createContext("/utf8", new UTF8Handler());
        server.createContext("/gbk", new GBKHandler());
        server.createContext("/get", new GetHandler());
        server.createContext("/post", new PostHandler());
        server.createContext("/baidu1", new BiduHandler1());
        server.createContext("/baidu2", new BiduHandler2());
        server.createContext("/zero", new ZeroLengthHandler());
        server.createContext("/chunk", new ChunkHandler());
        // creates a default executor
        server.setExecutor(Executors.newFixedThreadPool(10));
        server.start();
    }

    public static void stop() {
        if (server != null) {
            //delay
            server.stop(0);
            server = null;
        }
    }

    static class RedirectHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            cleanBody(t.getRequestBody());

            Headers headers = t.getResponseHeaders();
            headers.add("Location", "/info");
            t.sendResponseHeaders(302, -1);
            t.getResponseBody().close();
        }
    }

    static class InfoHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            String response = "Use /get to download a JPG";
            cleanBody(t.getRequestBody());

            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class UTF8Handler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            String response = "广大程序员普遍拥有如下特性：缺乏运动、想象力丰富、技术卓越";
            cleanBody(t.getRequestBody());

            Headers h = t.getResponseHeaders();
            h.add("Content-Type", " text/html;charset=utf-8");
            byte[] data = response.getBytes(StringUtil.UTF_8);
            t.sendResponseHeaders(200, data.length);
            OutputStream os = t.getResponseBody();
            os.write(data);
            os.close();
        }
    }

    static class GBKHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            String response = ";charset='gbk';今年的夏日夜空格外灿烂---水星之夜、超级月亮之夜，还有牧夫座流星雨之夜！还有伟大祖国的神十遨游在夜空！";
            cleanBody(t.getRequestBody());

            Headers h = t.getResponseHeaders();
            h.add("Content-Type", " text/html");
            byte[] data = response.getBytes("GBK");
            t.sendResponseHeaders(200, data.length);
            OutputStream os = t.getResponseBody();
            os.write(data);
            os.close();
        }
    }

    static class GetHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            cleanBody(t.getRequestBody());

            String method = t.getRequestMethod();
            if (!method.equals("GET")) {
                Headers headers = t.getResponseHeaders();
                headers.set("Location", "/post");
                headers.set("Content-Type", "text/html");
                t.sendResponseHeaders(307, -1);
                t.getResponseBody().close();
                return;
            }

            // add the required response header for a PDF file
            Headers h = t.getResponseHeaders();
            h.add("Content-Type", "image/jpeg");
            // ok, we are ready to send the response.
            t.sendResponseHeaders(200, DATA.length);
            OutputStream os = t.getResponseBody();
            os.write(DATA, 0, DATA.length);
            os.close();
        }
    }

    static class PostHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            String method = t.getRequestMethod();
            if (!method.equals("POST")) {
                badRequest(t);
                return;
            }
            Headers r = t.getRequestHeaders();
            Headers h = t.getResponseHeaders();
            for(Entry<String, List<String>> entry : r.entrySet()) {
                for (String s : entry.getValue()) {
                    h.add(entry.getKey(), s);
                }
            }
            h.set("Content-Type", "text/html");
            InputStream in = t.getRequestBody();
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
            StreamUtil.transferAndClose(in, bos, 1024);
            // ok, we are ready to send the response.
            byte[] data = bos.toByteArray();
            t.sendResponseHeaders(200, data.length);
            OutputStream os = t.getResponseBody();
            os.write(data, 0, data.length);
            os.close();
        }
    }

    static void badRequest(HttpExchange t) throws IOException {
        cleanBody(t.getRequestBody());

        t.sendResponseHeaders(400, -1);
        t.getResponseBody().close();
    }

    static class BiduHandler1 implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            cleanBody(t.getRequestBody());

            Headers headers = t.getResponseHeaders();
            headers.add("Location", "https://www.baidu.com/");
            t.sendResponseHeaders(301, -1);
            t.getResponseBody().close();
        }
    }

    static class BiduHandler2 implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            cleanBody(t.getRequestBody());

            Headers headers = t.getResponseHeaders();
            headers.add("Location", "https://www.baidu.com/");
            t.sendResponseHeaders(302, -1);
            t.getResponseBody().close();
        }
    }

    static void cleanBody(InputStream in) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
        StreamUtil.transferAndClose(in, bos, 1024);
    }

    static class ZeroLengthHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            cleanBody(t.getRequestBody());

            t.sendResponseHeaders(201, -1);
            Headers headers = t.getResponseHeaders();
            headers.add("Location", "/info");
            OutputStream os = t.getResponseBody();
            os.close();
        }
    }

    static class ChunkHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            cleanBody(t.getRequestBody());

            t.sendResponseHeaders(200, 0);
            Headers headers = t.getResponseHeaders();
            headers.add("Location", "/info");
            OutputStream os = t.getResponseBody();

            String data = "This is chunk, no length.";
            os.write(data.getBytes());
            os.close();
        }
    }

}
