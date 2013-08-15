/**
 * FileMonitorTest.java
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

import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.concurrent.Blocker;
import org.apache.niolex.commons.concurrent.WaitOn;
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
public class FileMonitorTest {

    static final String TMP = System.getProperty("user.home") + "/tmp";
    static FileMonitor monitor;

    @BeforeClass
    public static void testFileMonitor() throws Exception {
        DirUtil.delete(TMP + "/file-monitor", true);
        monitor = new FileMonitor(1, TMP + "/file-monitor");
    }

    @AfterClass
    public static void stop() {
        monitor.stop();
    }

    @Test
    public void testAddRun() throws Exception {
        final Counter cnt = new Counter();
        final Blocker<String> blocker = new Blocker<String>();
        EventListener add = new EventListener() {

            @Override
            public void notify(EventType type, long happenTime) {
                blocker.release("S", "");
                if (type == EventType.CREATE) cnt.inc();
                System.out.println(type + " " + happenTime);
            }};
        monitor.addListener(add);
        WaitOn<String> wait = blocker.initWait("s");
        DirUtil.mkdirsIfAbsent(TMP + "/file-monitor");
        wait.waitForResult(100);
        assertEquals(1, cnt.cnt());
        boolean b = monitor.removeListener(add);
        assertTrue(b);
    }

    @Test
    public void testBupdateAddListener() throws Exception {
        final Counter cnt = new Counter();
        final Blocker<String> blocker = new Blocker<String>();
        EventListener update = new EventListener() {

            @Override
            public void notify(EventType type, long happenTime) {
                blocker.release("S", "");
                if (type == EventType.UPDATE) cnt.inc();
                System.out.println(type + " " + happenTime);
            }};
        monitor.addListener(update);
        WaitOn<String> wait = blocker.initWait("s");
        FileUtil.setCharacterFileContentToFileSystem(TMP + "/file-monitor/tmp.txt", "FileMonitor", StringUtil.US_ASCII);
        wait.waitForResult(100);
        assertEquals(1, cnt.cnt());
        boolean b = monitor.removeListener(update);
        assertTrue(b);
    }

    @Test
    public void testDeleteNotify() throws Exception {
        final Counter cnt = new Counter();
        final Blocker<String> blocker = new Blocker<String>();
        EventListener delete = new EventListener() {

            @Override
            public void notify(EventType type, long happenTime) {
                blocker.release("S", "");
                if (type == EventType.DELETE) cnt.inc();
                System.out.println(type + " " + happenTime);
            }};
        monitor.addListener(delete);
        WaitOn<String> wait = blocker.initWait("s");
        DirUtil.delete(TMP + "/file-monitor", true);
        wait.waitForResult(100);
        assertEquals(1, cnt.cnt());
        boolean b = monitor.removeListener(delete);
        assertTrue(b);
    }

    @Test
    public void testRemoveListener() throws Exception {
        assertEquals(1, EventType.DELETE.compareTo(EventType.CREATE));
        assertEquals(EventType.valueOf("DELETE"), EventType.DELETE);
    }

}