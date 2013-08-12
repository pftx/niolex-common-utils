/**
 * DirMonitorTest.java
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
package org.apache.niolex.commons.file;


import static org.junit.Assert.*;

import java.util.List;

import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.concurrent.Blocker;
import org.apache.niolex.commons.concurrent.ThreadUtil;
import org.apache.niolex.commons.concurrent.WaitOn;
import org.apache.niolex.commons.file.DirMonitor.ChildrenListener;
import org.apache.niolex.commons.file.FileMonitor.EventListener;
import org.apache.niolex.commons.file.FileMonitor.EventType;
import org.apache.niolex.commons.test.Counter;
import org.apache.niolex.commons.test.OrderedRunner;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-7-23
 */
@RunWith(OrderedRunner.class)
public class DirMonitorTest {

    static final String TMP = System.getProperty("user.home") + "/tmp";
    static DirMonitor monitor;

    @BeforeClass
    public static void testDirMonitor() throws Exception {
        DirUtil.delete(TMP + "/dir-monitor", true);
        monitor = new DirMonitor(1, TMP + "/dir-monitor");
    }

    @AfterClass
    public static void stop() {
        monitor.stop();
        DirUtil.delete(TMP + "/dir-monitor", true);
    }

    @Test
    public void testACreate() throws Exception {
        final Counter cnt = new Counter();
        final Counter cld = new Counter();
        final Blocker<String> blocker = new Blocker<String>();
        ChildrenListener cli = new ChildrenListener() {

            @Override
            public void notify(EventType type, long happenTime) {
                blocker.release("S", "");
                if (type == EventType.CREATE) cnt.inc();
                System.out.println(type + " " + happenTime);
            }

            @Override
            public void childrenChange(EventType type, List<String> list) {
                if (type == EventType.ADD_CHILDREN) cld.inc();
                System.out.println(type + " " + list);
            }};
        monitor.addListener(cli);
        WaitOn<String> wait = blocker.initWait("s");
        DirUtil.mkdirsIfAbsent(TMP + "/dir-monitor");
        wait.waitForResult(100);
        assertEquals(1, cnt.cnt());
        assertEquals(1, cld.cnt());
        boolean b = monitor.removeListener(cli);
        assertTrue(b);
        assertTrue(monitor.isDir());
    }

    @Test
    public void testAdd() throws Exception {
        final Counter cnt = new Counter();
        final Counter cld = new Counter();
        final Blocker<String> blocker = new Blocker<String>();
        ChildrenListener cli = new ChildrenListener() {

            @Override
            public void notify(EventType type, long happenTime) {
                blocker.release("S", "");
                if (type == EventType.UPDATE) cnt.inc();
                System.out.println(type + " " + happenTime);
            }

            @Override
            public void childrenChange(EventType type, List<String> list) {
                if (type == EventType.ADD_CHILDREN) cld.set(list.size());
                System.out.println(type + " " + list);
            }};
        monitor.addListener(cli);
        WaitOn<String> wait = blocker.initWait("s");
        FileUtil.setCharacterFileContentToFileSystem(TMP + "/dir-monitor/tmp.txt", "FileMonitor", StringUtil.US_ASCII);
        wait.waitForResult(2000);
        assertEquals(1, cnt.cnt());
        assertEquals(1, cld.cnt());
        boolean b = monitor.removeListener(cli);
        assertTrue(b);
    }

    @Test
    public void testAddAgain() throws Exception {
        final Counter cnt = new Counter();
        final Counter cld = new Counter();
        final Blocker<String> blocker = new Blocker<String>();
        ChildrenListener cli = new ChildrenListener() {

            @Override
            public void notify(EventType type, long happenTime) {
                if (type == EventType.UPDATE)
                    cnt.inc();
                System.out.println(type + " " + happenTime);
            }

            @Override
            public void childrenChange(EventType type, List<String> list) {
                if (type == EventType.ADD_CHILDREN)
                    cld.set(list.size());
                System.out.println(type + " " + list);
            }
        };
        monitor.addListener(cli);

        final Counter unt = new Counter();
        EventListener update = new EventListener() {

            @Override
            public void notify(EventType type, long happenTime) {
                if (type == EventType.UPDATE)
                    unt.inc();
                System.out.println("UNT " + type + " " + happenTime);
                blocker.release(type, "");
            }
        };
        WaitOn<String> wait = blocker.initWait(EventType.CREATE);
        FileMonitor monitor2 = new DirMonitor(1, TMP + "/dir-monitor/tmp.txt");
        monitor2.addListener(update);
        wait.waitForResult(100);
        wait = blocker.initWait(EventType.UPDATE);
        FileUtil.setCharacterFileContentToFileSystem(TMP + "/dir-monitor/tmp.txt", "DirMonitor", StringUtil.US_ASCII);
        wait.waitForResult(100);
        assertEquals(1, unt.cnt());
        assertEquals(0, cnt.cnt());
        assertEquals(0, cld.cnt());
        boolean b = monitor.removeListener(cli);
        assertTrue(b);
        ThreadUtil.topGroup().list();
        monitor2.stop();
    }

    @Test
    public void testAddAnother() throws Exception {
        final Counter cnt = new Counter();
        final Counter cld = new Counter();
        final Blocker<String> blocker = new Blocker<String>();
        ChildrenListener cli = new ChildrenListener() {

            @Override
            public void notify(EventType type, long happenTime) {
                if (type == EventType.UPDATE) cnt.inc();
                System.out.println(type + " " + happenTime);
                blocker.release("s", "");
            }

            @Override
            public void childrenChange(EventType type, List<String> list) {
                if (type == EventType.ADD_CHILDREN) cld.set(list.size());
                System.out.println(type + " " + list);
            }};
        monitor.addListener(cli);
        WaitOn<String> wait = blocker.initWait("s");
        FileUtil.setCharacterFileContentToFileSystem(TMP + "/dir-monitor/dir.txt", "Lex is the Best!!", StringUtil.US_ASCII);
        wait.waitForResult(100);
        assertEquals(1, cnt.cnt());
        assertEquals(1, cld.cnt());
        boolean b = monitor.removeListener(cli);
        assertTrue(b);
        assertEquals(2, monitor.currentChildren().size());
    }


    @Test
    public void testBRemove() throws Exception {
        final Counter cnt = new Counter();
        final Counter cld = new Counter();
        final Blocker<String> blocker = new Blocker<String>();
        ChildrenListener cli = new ChildrenListener() {

            @Override
            public void notify(EventType type, long happenTime) {
                if (type == EventType.UPDATE) cnt.inc();
                System.out.println(type + " " + happenTime);
                blocker.release("s", "");
            }

            @Override
            public void childrenChange(EventType type, List<String> list) {
                if (type == EventType.REMOVE_CHILDREN) cld.set(list.size());
                System.out.println(type + " " + list);
            }};
        monitor.addListener(cli);
        WaitOn<String> wait = blocker.initWait("s");
        DirUtil.delete(TMP + "/dir-monitor/dir.txt", false);
        wait.waitForResult(1500);
        assertEquals(1, cnt.cnt());
        assertEquals(1, cld.cnt());
        boolean b = monitor.removeListener(cli);
        assertTrue(b);
        assertEquals(1, monitor.currentChildren().size());
    }

    @Test
    public void testCheckCreate() throws Exception {
        final Counter cnt = new Counter();
        final Counter cld = new Counter();
        final Blocker<String> blocker = new Blocker<String>();
        ChildrenListener cli = new ChildrenListener() {

            @Override
            public void notify(EventType type, long happenTime) {
                if (type == EventType.CREATE)
                    cnt.inc();
                System.out.println(type + " " + happenTime);
                blocker.release("s", "");
            }

            @Override
            public void childrenChange(EventType type, List<String> list) {
                if (type == EventType.NOT_DIR)
                    cld.set(1);
                System.out.println(type + " " + list);
            }
        };
        DirMonitor monitor2 = new DirMonitor(1, TMP + "/dir-monitor/not-dir.txt");
        monitor2.addListener(cli);
        WaitOn<String> wait = blocker.initWait("s");

        FileUtil.setCharacterFileContentToFileSystem(TMP + "/dir-monitor/not-dir.txt", "It's not a DIR", StringUtil.US_ASCII);
        wait.waitForResult(100);
        assertEquals(1, cnt.cnt());
        assertEquals(1, cld.cnt());
        boolean b = monitor2.removeListener(cli);
        assertTrue(b);
        ThreadUtil.topGroup().list();
        monitor2.stop();
        assertFalse(monitor2.isDir());
    }

    @Test
    public void testDelete() throws Exception {
        final Counter cnt = new Counter();
        final Counter cld = new Counter();
        final Blocker<String> blocker = new Blocker<String>();
        ChildrenListener cli = new ChildrenListener() {

            @Override
            public void notify(EventType type, long happenTime) {
                if (type == EventType.DELETE) cnt.inc();
                System.out.println(type + " " + happenTime);
                blocker.release(type, "");
            }

            @Override
            public void childrenChange(EventType type, List<String> list) {
                if (type == EventType.REMOVE_CHILDREN) cld.set(list.size());
                System.out.println(type + " " + list);
            }};
        monitor.addListener(cli);
        WaitOn<String> wait = blocker.initWait(EventType.DELETE);
        DirUtil.delete(TMP + "/dir-monitor", true);
        wait.waitForResult(2000);
        assertEquals(1, cnt.cnt());
        boolean b = monitor.removeListener(cli);
        assertTrue(b);
    }

    @Test
    public void testNotify() throws Exception {
        monitor.notify(EventType.NOT_DIR, 0);
        monitor.notify(EventType.ADD_CHILDREN, 0);
    }

    @Test
    public void testNotifyNull() throws Exception {
        monitor.notify(null, 0);
    }

}
