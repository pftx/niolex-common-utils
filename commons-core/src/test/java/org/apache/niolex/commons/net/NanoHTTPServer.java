/**
 * NanoHTTPServer.java
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.file.FileUtil;
import org.apache.niolex.commons.net.NanoHTTPD.Response.Status;
import org.apache.niolex.commons.stream.StreamUtil;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 2.1.3
 * @since Sep 1, 2016
 */
public class NanoHTTPServer extends NanoHTTPD {

    public static final byte[] DATA = FileUtil.getBinaryFileContentFromClassPath("nanohttpd_logo.png", NanoHTTPServer.class);
    public static final NanoHTTPServer s = new NanoHTTPServer();

    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
        // Add handlers.
        handlerMap.put("/", new RedirectHandler(Status.FOUND, "info"));
        handlerMap.put("/info", new TextHandler(Status.OK, "Use /get to download a JPG"));
        handlerMap.put("/utf8", new UTF8Handler());
        handlerMap.put("/gbk", new GBKHandler());
        handlerMap.put("/get", new GetHandler());
        handlerMap.put("/post", new PostHandler());
        handlerMap.put("/baidu1", new RedirectHandler(Status.REDIRECT, "https://www.baidu.com/"));
        handlerMap.put("/baidu2", new RedirectHandler(Status.TEMPORARY_REDIRECT, "http://dict.cn/"));
        handlerMap.put("/chunk", new ChunkHandler());
        handlerMap.put("/zero", new TextHandler(Status.OK, ""));

        try {
            s.start(SOCKET_READ_TIMEOUT, false);
            System.out.println("\nNanoHTTPServer Running! Point your browsers to http://localhost:9090/\n");
        } catch (IOException ioe) {
            System.err.println("Couldn't start NanoHTTPServer:\n" + ioe);
        }
    }

    public static final void stopIt() {
        s.stop();
        System.out.println("\nNanoHTTPServer stoped!");
    }

    /**
     * Constructor
     */
    public NanoHTTPServer() {
        super(9090);
    }

    @Override
    public Response serve(IHTTPSession session) {
        System.out.println("Requested uri -> " + session.getUri());
        Handler h = handlerMap.get(session.getUri());

        if (h != null) {
            return h.serve(session);
        }

        return newFixedLengthResponse(Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, "Not Found");
    }

    public static interface Handler {
        Response serve(IHTTPSession session);
    }

    private static final Map<String, Handler> handlerMap = new HashMap<String, Handler>();

    public static class TextHandler implements Handler {

        private final Status code;
        private final String body;

        public TextHandler(Status code, String body) {
            super();
            this.code = code;
            this.body = body;
        }

        /**
         * This is the override of super method.
         * 
         * @see org.apache.niolex.commons.net.NanoHTTPServer.Handler#serve(fi.iki.elonen.NanoHTTPD.IHTTPSession)
         */
        @Override
        public Response serve(IHTTPSession session) {
            return newFixedLengthResponse(code, NanoHTTPD.MIME_PLAINTEXT, body);
        }

    }

    public static class RedirectHandler implements Handler {

        private final Status code;
        private final String location;

        public RedirectHandler(Status code, String location) {
            super();
            this.code = code;
            this.location = location;
        }

        /**
         * This is the override of super method.
         * 
         * @see org.apache.niolex.commons.net.NanoHTTPServer.Handler#serve(fi.iki.elonen.NanoHTTPD.IHTTPSession)
         */
        @Override
        public Response serve(IHTTPSession session) {
            Response r = newFixedLengthResponse(code, NanoHTTPD.MIME_PLAINTEXT, "Found.");
            r.addHeader("Location", location);
            return r;
        }

    }

    static class UTF8Handler implements Handler {
        @Override
        public Response serve(IHTTPSession session) {
            String response = "广大程序员普遍拥有如下特性：缺乏运动、想象力丰富、技术卓越";
            byte[] data = response.getBytes(StringUtil.UTF_8);
            Response r = newFixedLengthResponse(Status.OK, NanoHTTPD.MIME_PLAINTEXT, new ByteArrayInputStream(data), data.length);
            r.addHeader("Content-Type", " text/html;charset=utf-8");
            return r;
        }
    }

    static class GBKHandler implements Handler {
        @Override
        public Response serve(IHTTPSession session) {
            String response = ";charset='gbk';今年的夏日夜空格外灿烂---水星之夜、超级月亮之夜，还有牧夫座流星雨之夜！还有伟大祖国的神十遨游在夜空！";
            byte[] data = response.getBytes(StringUtil.GBK);
            Response r = newFixedLengthResponse(Status.OK, NanoHTTPD.MIME_PLAINTEXT, new ByteArrayInputStream(data), data.length);
            r.addHeader("Content-Type", " text/html");
            return r;
        }
    }

    static class GetHandler implements Handler {
        @Override
        public Response serve(IHTTPSession session) {
            Method method = session.getMethod();
            if (method != Method.GET) {
                Response r = newFixedLengthResponse(Status.REDIRECT_SEE_OTHER, NanoHTTPD.MIME_PLAINTEXT, "See Other.");
                r.addHeader("Location", "/get");
                return r;
            }

            return newFixedLengthResponse(Status.OK, "image/png", new ByteArrayInputStream(DATA), DATA.length);
        }
    }

    static class PostHandler implements Handler {
        @Override
        public Response serve(IHTTPSession session) {
            Method method = session.getMethod();
            if (method != Method.POST) {
                return newFixedLengthResponse(Status.METHOD_NOT_ALLOWED, NanoHTTPD.MIME_PLAINTEXT,
                        "Method not allowed, use POST.");
            }

            int contentLength = Integer.valueOf(session.getHeaders().get("content-length"));
            InputStream in = session.getInputStream();
            byte[] data = new byte[contentLength];
            try {
                StreamUtil.readData(in, data);
            } catch (IOException ignore) {
                System.err.println("Couldn't read inputstream in POST handler:\n" + ignore);
            }
            // ok, we are ready to send the response.
            Response r = newFixedLengthResponse(Status.OK, NanoHTTPD.MIME_PLAINTEXT, new ByteArrayInputStream(data), data.length);

            for (Entry<String, String> entry : session.getHeaders().entrySet()) {
                r.addHeader(entry.getKey(), entry.getValue());
            }

            return r;
        }
    }

    static class ChunkHandler implements Handler {
        @Override
        public Response serve(IHTTPSession session) {
            String data = "This is chunk, no length.";
            Response r = newChunkedResponse(Status.OK, NanoHTTPD.MIME_PLAINTEXT, new ByteArrayInputStream(data.getBytes()));
            r.addHeader("Location", "/info");
            return r;
        }
    }
}
