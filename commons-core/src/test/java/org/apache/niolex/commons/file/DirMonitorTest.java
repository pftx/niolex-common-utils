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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.FileOutputStream;
import java.util.List;

import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.concurrent.Blocker;
import org.apache.niolex.commons.concurrent.ThreadUtil;
import org.apache.niolex.commons.concurrent.WaitOn;
import org.apache.niolex.commons.file.DirMonitor.ChildrenListener;
import org.apache.niolex.commons.file.FileMonitor.EventType;
import org.apache.niolex.commons.stream.StreamUtil;
import org.apache.niolex.commons.test.AnnotationOrderedRunner;
import org.apache.niolex.commons.test.AnnotationOrderedRunner.Order;
import org.apache.niolex.commons.test.Counter;
import org.apache.niolex.commons.util.SystemUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.1
 * @since 2013-7-23
 */
@SuppressWarnings("incomplete-switch")
@RunWith(AnnotationOrderedRunner.class)
public class DirMonitorTest {

    static final String TMP = System.getProperty("user.home") + "/tmpd";
    static final String FILE = TMP + "/dir-monitor";
    static DirMonitor monitor;
    static boolean isLinux;

    @BeforeClass
    public static void testDirMonitor() throws Exception {
        DirUtil.mkdirsIfAbsent(TMP);
        DirUtil.delete(FILE, true);
        monitor = new DirMonitor(10, FILE);
        assertNull(monitor.currentChildren());
        assertNull(monitor.isDir);
        String osName = SystemUtil.getSystemProperty("os.name");
        if (osName.equalsIgnoreCase("Linux") || osName.contains("Mac")) {
        	isLinux = true;
        }
    }

    @AfterClass
    public static void stop() {
        monitor.stop();
        DirUtil.delete(TMP, true);
    }

    final Blocker<String> blocker = new Blocker<String>();
    final Counter cr = new Counter();
    final Counter ad = new Counter();
    final Counter rm = new Counter();
    final Counter de = new Counter();
    final Counter addCld = new Counter();
    final Counter rmCld = new Counter();

    ChildrenListener cli = new ChildrenListener() {

		@Override
        public void notify(EventType type, long happenTime) {
            switch (type) {
                case CREATE:
                    cr.inc();
                    blocker.release("su", "");
                    break;
                case DELETE:
                    de.inc();
                    blocker.release("su", "");
                    break;
            }
            System.out.println(type + " " + happenTime);
        }

        @Override
        public void childrenChange(EventType type, List<String> list) {
            switch (type) {
                case CREATE:
                    cr.inc();
                    break;
                case ADD_CHILDREN:
                    ad.inc();
                    addCld.set(list.size());
                    break;
                case REMOVE_CHILDREN:
                    rm.inc();
                    rmCld.set(list.size());
                    break;
                case DELETE:
                    de.inc();
                    break;
            }
            blocker.release("s", "");
            System.out.println(type + " " + list + " ALL " + monitor.currentChildren());
        }
    };


    @Test
    @Order(1)
    public void testCreate() throws Exception {
        monitor.addListener(cli);
        WaitOn<String> wait = blocker.init("su");
        DirUtil.mkdirsIfAbsent(FILE);
        wait.waitForResult(1000);
        assertEquals(1, cr.cnt());
        monitor.removeListener(cli);
        assertTrue(monitor.isDir());
        assertTrue(monitor.currentChildren().isEmpty());
    }

    @Test
    @Order(2)
    public void testAddDir() throws Exception {
    	if (isLinux) ThreadUtil.sleepAtLeast(1001);
    	
        monitor.addListener(cli);
        WaitOn<String> wait = blocker.init("s");
        DirUtil.mkdirsIfAbsent(FILE + "/a");
        wait.waitForResult(1000);
        assertEquals(1, ad.cnt());
        assertEquals(1, addCld.cnt());
        monitor.removeListener(cli);
        assertTrue(monitor.isDir());
        assertEquals(1, monitor.currentChildren().size());
    }

    @Test
    @Order(3)
    public void testAddFile() throws Exception {
    	if (isLinux) ThreadUtil.sleepAtLeast(1001);
    	
        monitor.addListener(cli);
        WaitOn<String> wait = blocker.init("s");
        FileUtil.setCharacterFileContentToFileSystem(FILE + "/tmp.txt", "File", StringUtil.US_ASCII);
        wait.waitForResult(1000);
        assertEquals(1, ad.cnt());
        assertEquals(1, addCld.cnt());
        monitor.removeListener(cli);
        assertTrue(monitor.isDir());
        assertEquals(2, monitor.currentChildren().size());
    }

    @Test
    @Order(4)
    public void testAddAnother() throws Exception {
    	if (isLinux) ThreadUtil.sleepAtLeast(1001);
    	
        System.out.println("---------------");
        monitor.addListener(cli);
        WaitOn<String> wait = blocker.init("s");
        FileUtil.setCharacterFileContentToFileSystem(FILE + "/a/dir.txt", "Lex is the Best!!", StringUtil.US_ASCII);
        wait.waitForResult(30);
        assertEquals(0, ad.cnt());
        assertEquals(0, addCld.cnt());
        boolean b = monitor.removeListener(cli);
        assertTrue(b);
        assertEquals(2, monitor.currentChildren().size());
        System.out.println("---------------");
    }

    @Test
    @Order(5)
    public void testRemove() throws Exception {
        monitor.addListener(cli);
        WaitOn<String> wait = blocker.init("s");
        DirUtil.delete(FILE + "/a", true);
        wait.waitForResult(1500);
        assertEquals(1, rm.cnt());
        assertEquals(1, rmCld.cnt());
        boolean b = monitor.removeListener(cli);
        assertTrue(b);
        assertEquals(1, monitor.currentChildren().size());
    }

    @Test
    @Order(6)
    public void testDelete() throws Exception {
        monitor.addListener(cli);
        WaitOn<String> wait = blocker.init("su");
        while (!DirUtil.delete(TMP + "/dir-monitor", true)) ThreadUtil.sleepAtLeast(1);
        wait.waitForResult(2000);
        assertEquals(1, de.cnt());
        boolean b = monitor.removeListener(cli);
        assertTrue(b);
    }

    @Test
    @Order(7)
    public void testCheckNotDIR() throws Exception {
        monitor.addListener(cli);
        WaitOn<String> wait = blocker.init("s");
        DirUtil.mkdirsIfAbsent(FILE + "/a");
        wait.waitForResult(3000);
        assertEquals(1, ad.cnt());
        assertEquals(1, addCld.cnt());
        assertEquals(1, monitor.currentChildren().size());
        monitor.removeListener(cli);

        final Counter cld = new Counter();
        final Blocker<String> blocker = new Blocker<String>();
        ChildrenListener cli = new ChildrenListener() {

            @Override
            public void notify(EventType type, long happenTime) {
                blocker.release("q", "");
            }

            @Override
            public void childrenChange(EventType type, List<String> list) {
                if (type == EventType.NOT_DIR)
                    cld.set(1);
                blocker.release("s", "");
                System.out.println(type + " " + list);
            }
        };
        DirMonitor monitor2 = new DirMonitor(1, TMP + "/dir-monitor/not-dir.txt");
        monitor2.addListener(cli);
        wait = blocker.init("s");

        FileUtil.setCharacterFileContentToFileSystem(TMP + "/dir-monitor/not-dir.txt", "It's not a DIR", StringUtil.US_ASCII);
        wait.waitForResult(3000);
        assertEquals(1, cld.cnt());
        assertFalse(monitor2.isDir());

        wait = blocker.init("q");
        FileOutputStream fo = new FileOutputStream(TMP + "/dir-monitor/not-dir.txt");
        StreamUtil.writeAndClose(fo, "Add More".getBytes());
        wait.waitForResult(2000);
        assertFalse(monitor2.isDir());

        boolean b = monitor2.removeListener(cli);
        assertTrue(b);
    }

    @Test
    @Order(8)
    public void testNotify() throws Exception {
        monitor.notify(EventType.NOT_DIR, 0);
        monitor.notify(EventType.ADD_CHILDREN, 0);
    }

    @Test
    @Order(9)
    public void testNotifyNull() throws Exception {
        monitor.notify(null, 0);
    }

}
