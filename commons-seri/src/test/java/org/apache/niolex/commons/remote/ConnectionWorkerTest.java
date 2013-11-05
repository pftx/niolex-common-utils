/**
 * ConnectionWorkerTest.java
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
package org.apache.niolex.commons.remote;


import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.niolex.commons.stream.StreamUtil;
import org.apache.niolex.commons.test.AnnotationOrderedRunner;
import org.apache.niolex.commons.test.Benchmark;
import org.apache.niolex.commons.util.SystemUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Maps;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-11-5
 */
@RunWith(AnnotationOrderedRunner.class)
public class ConnectionWorkerTest {

    public static class Fun extends Executer {

        private Object o;

        /**
         * This is the override of super method.
         * @see org.apache.niolex.commons.remote.Executer#execute(java.lang.Object, java.io.OutputStream, java.lang.String[])
         */
        @Override
        public void execute(Object o, OutputStream out, String[] args) throws IOException {
            this.o = o;
            StreamUtil.writeString(out, "got you ----<<<<<<\n");
            out.flush();
        }

        public Object o() {
            return o;
        }
    }

    static PipedOutputStream src = new PipedOutputStream();

    static Scanner scan;

    static PipedInputStream snk = new PipedInputStream(1024);

    static ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<String, Object>();

    static AtomicInteger connectionNumber = new AtomicInteger(9);

    static Socket so;

    static ConnectionWorker conn;

    static Thread t;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        final PipedInputStream in = new PipedInputStream(src, 1024);
        final PipedOutputStream out = new PipedOutputStream(snk);
        so = new Socket() {

            /**
             * This is the override of super method.
             * @see java.net.Socket#getInputStream()
             */
            @Override
            public InputStream getInputStream() throws IOException {
                return in;
            }

            /**
             * This is the override of super method.
             * @see java.net.Socket#getOutputStream()
             */
            @Override
            public OutputStream getOutputStream() throws IOException {
                return out;
            }
        };

        conn = new ConnectionWorker(so, map, connectionNumber);
        t = new Thread(conn);
        t.start();
        scan = new Scanner(snk, "UTF-8");
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        SystemUtil.close(so);
        SystemUtil.close(src);
        SystemUtil.close(snk);
    }

    static Fun fun = new Fun();

    @Test
    @AnnotationOrderedRunner.Order(0)
    public void testAddCommand() throws Exception {
        ConnectionWorker.addCommand("fun", fun);
        map.put("joy", so);
        map.put("ben", Benchmark.makeBenchmark());
        Map<Integer, String> imap = Maps.newHashMap();
        imap.put(3, "three");
        Map<Long, String> lmap = Maps.newHashMap();
        lmap.put(1234567890l, "1234567890l");
        Map<Socket, String> omap = Maps.newHashMap();
        Map<Socket, String> emap = Maps.newHashMap();
        omap.put(so, "socket");
        map.put("imap", imap);
        map.put("lmap", lmap);
        map.put("omap", omap);
        map.put("emap", emap);
    }

    @Test
    @AnnotationOrderedRunner.Order(1)
    public void testEndl() throws Exception {
        ConnectionWorker.endl();
        StreamUtil.writeString(src, "windows\n");
        src.flush();
        scan.nextLine();
        StreamUtil.writeString(src, "linux\n");
        src.flush();
        scan.nextLine();
        StreamUtil.writeString(src, "\n");
        src.flush();
    }

    @Test
    @AnnotationOrderedRunner.Order(2)
    public void testSetAuthInfo() throws Exception {
        ConnectionWorker.setAuthInfo("isck");
        StreamUtil.writeString(src, "auth abcc\n");
        src.flush();
        String r = null;
        r = scan.nextLine();
        assertEquals("Authenticate Failed.", r);
        StreamUtil.writeString(src, "go linux\n");
        src.flush();
        r = scan.nextLine();
        assertEquals("Please authenticate.", r);
        StreamUtil.writeString(src, "auth isck\n");
        src.flush();
        r = scan.nextLine();
        System.out.println(r);
        assertEquals("Authenticate Success.", r);
    }

    @Test
    @AnnotationOrderedRunner.Order(3)
    public void testConnectionWorker() throws Exception {
        String r = null;
        StreamUtil.writeString(src, "have fun please\n");
        src.flush();
        r = scan.nextLine();
        System.out.println(r);
        assertEquals("Invalid Command.", r);
    }

    @Test
    @AnnotationOrderedRunner.Order(4)
    public void testRun() throws Exception {
        String r = null;
        StreamUtil.writeString(src, "get fun[me].good\n");
        src.flush();
        r = scan.nextLine();
        System.out.println(r);
        assertEquals("Invalid Path at fun[me^", r);
    }

    @Test
    @AnnotationOrderedRunner.Order(5)
    public void testExecuteInvalidPath() throws Exception {
        String r = null;
        StreamUtil.writeString(src, "get ben.list[5].groupName.value[3].good\n");
        src.flush();
        r = scan.nextLine();
        System.out.println(r);
        assertEquals("Invalid Path started at 5.good", r);
    }

    @Test
    @AnnotationOrderedRunner.Order(6)
    public void testExecuteNotArr() throws Exception {
        String r = null;
        StreamUtil.writeString(src, "get ben.name[1]\n");
        src.flush();
        r = scan.nextLine();
        System.out.println(r);
        assertEquals("Invalid Path started at 2.name Not Array.", r);
    }

    @Test
    @AnnotationOrderedRunner.Order(7)
    public void testExecuteArray() throws Exception {
        String r = null;
        StreamUtil.writeString(src, "get ben.list[5].groupName.value[77]\n");
        src.flush();
        r = scan.nextLine();
        System.out.println(r);
        assertEquals("Invalid Path started at 4.value Array Out of Bound.", r);
    }

    @Test
    @AnnotationOrderedRunner.Order(8)
    public void testExecuteCollection() throws Exception {
        String r = null;
        StreamUtil.writeString(src, "get ben.list[100]\n");
        src.flush();
        r = scan.nextLine();
        System.out.println(r);
        assertEquals("Invalid Path started at 2.list Array Out of Bound.", r);
    }

    @Test
    @AnnotationOrderedRunner.Order(9)
    public void testExecuteMapErr() throws Exception {
        String r = null;
        StreamUtil.writeString(src, "get joy{name}\n");
        src.flush();
        r = scan.nextLine();
        System.out.println(r);
        assertEquals("Invalid Path started at 1.joy Not Map.", r);
    }

    @Test
    @AnnotationOrderedRunner.Order(10)
    public void testExecuteMap() throws Exception {
        String r = null;
        StreamUtil.writeString(src, "get ben.list[3].beanMap{Qute}.name[8]\n");
        src.flush();
        r = scan.nextLine();
        System.out.println(r);
        assertEquals("Invalid Path started at 4.name Not Array.", r);
    }

    @Test
    @AnnotationOrderedRunner.Order(11)
    public void testExecuteiMap() throws Exception {
        String r = null;
        StreamUtil.writeString(src, "get imap{3}.value.length\n");
        src.flush();
        r = scan.nextLine();
        System.out.println(r);
        assertEquals("Invalid Path started at 3.length", r);
    }

    @Test
    @AnnotationOrderedRunner.Order(12)
    public void testExecuteiMapErr() throws Exception {
        String r = null;
        StreamUtil.writeString(src, "get imap{8f}\n");
        src.flush();
        r = scan.nextLine();
        System.out.println(r);
        assertEquals("Invalid Map Key at 1.imap", r);
    }

    @Test
    @AnnotationOrderedRunner.Order(13)
    public void testExecutelMap() throws Exception {
        String r = null;
        StreamUtil.writeString(src, "get lmap{1234567890}.value.length\n");
        src.flush();
        r = scan.nextLine();
        System.out.println(r);
        assertEquals("Invalid Path started at 3.length", r);
    }

    @Test
    @AnnotationOrderedRunner.Order(14)
    public void testExecutelMapErr() throws Exception {
        String r = null;
        StreamUtil.writeString(src, "get lmap{12345x67890}.value.length\n");
        src.flush();
        r = scan.nextLine();
        System.out.println(r);
        assertEquals("Invalid Map Key at 1.lmap", r);
    }

    @Test
    @AnnotationOrderedRunner.Order(15)
    public void testExecuteOMapErr() throws Exception {
        String r = null;
        StreamUtil.writeString(src, "get omap{12345x67890}.value.length\n");
        src.flush();
        r = scan.nextLine();
        System.out.println(r);
        assertEquals("This Map Key Type  at 1.omap Is Not Supported.", r);
    }

    @Test
    @AnnotationOrderedRunner.Order(16)
    public void testExecuteEMap() throws Exception {
        String r = null;
        StreamUtil.writeString(src, "get emap{12345x67890}.value.length\n");
        src.flush();
        r = scan.nextLine();
        System.out.println(r);
        assertEquals("Map at 1.emap Is Empty.", r);
    }

    @Test
    @AnnotationOrderedRunner.Order(17)
    public void testExecuteNotFound() throws Exception {
        String r = null;
        StreamUtil.writeString(src, "get test.value.length\n");
        src.flush();
        r = scan.nextLine();
        System.out.println(r);
        assertEquals("Path Not Found.", r);
    }

    @Test
    @AnnotationOrderedRunner.Order(18)
    public void testExecute() throws Exception {
        String r = null;
        StreamUtil.writeString(src, "fun ben.name\n");
        src.flush();
        r = scan.nextLine();
        System.out.println(r);
        assertEquals("got you ----<<<<<<", r);
        assertEquals("This is the compress test benchmark.", fun.o);
    }

    @Test
    @AnnotationOrderedRunner.Order(99)
    public void testQuit() throws Exception {
        StreamUtil.writeString(src, "exit\n");
        src.flush();
        System.out.println(scan.nextLine());
    }
}
