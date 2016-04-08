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

import java.util.EnumMap;

import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.concurrent.Blocker;
import org.apache.niolex.commons.concurrent.WaitOn;
import org.apache.niolex.commons.file.FileMonitor.EventListener;
import org.apache.niolex.commons.file.FileMonitor.EventType;
import org.apache.niolex.commons.test.AnnotationOrderedRunner;
import org.apache.niolex.commons.test.AnnotationOrderedRunner.Order;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Maps;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-7-23
 */
@RunWith(AnnotationOrderedRunner.class)
public class FileMonitorTest {

    static final String TMP = System.getProperty("user.home") + "/tmpf";
    static FileMonitor monitor;

    @BeforeClass
    public static void testFileMonitor() throws Exception {
        DirUtil.mkdirsIfAbsent(TMP);
        DirUtil.delete(TMP + "/file-monitor", true);
        monitor = new FileMonitor(10, TMP + "/file-monitor");
    }

    @AfterClass
    public static void stop() {
        monitor.stop();
        DirUtil.delete(TMP, true);
    }

    static final EnumMap<EventType, Integer> map = Maps.newEnumMap(EventType.class);
    static final Blocker<String> blocker = new Blocker<String>();

    static final EventListener listn = new EventListener() {

        @Override
        public void notify(EventType type, long happenTime) {
            map.put(type, map.containsKey(type) ? map.get(type) + 1 : 1);
            blocker.release("s", "");
            System.out.println(type + " " + happenTime);
        }
    };

    private void setFile(String str) {
        FileUtil.setCharacterFileContentToFileSystem(TMP + "/file-monitor", str, StringUtil.US_ASCII);
    }

    @Test
    @Order(1)
    public void testCreateFile() throws Exception {
        monitor.addListener(listn);
        WaitOn<String> wait = blocker.init("s");
        setFile("now");
        wait.waitForResult(1000);
        assertEquals(1, map.get(EventType.CREATE).intValue());
        boolean b = monitor.removeListener(listn);
        assertTrue(b);
    }

    @Test
    @Order(2)
    public void testUpdateFile() throws Exception {
        boolean b = monitor.removeListener(listn);
        assertFalse(b);
        monitor.addListener(listn);
        WaitOn<String> wait = blocker.init("s");

        setFile("update");
        wait.waitForResult(1000);
        assertEquals(1, map.get(EventType.CREATE).intValue());
        assertEquals(1, map.get(EventType.UPDATE).intValue());
    }

    @Test
    @Order(3)
    public void testDeleteNotify() throws Exception {
        WaitOn<String> wait = blocker.init("s");
        DirUtil.delete(TMP + "/file-monitor", true);
        wait.waitForResult(1000);
        assertEquals(1, map.get(EventType.CREATE).intValue());
        assertEquals(1, map.get(EventType.UPDATE).intValue());
        assertEquals(1, map.get(EventType.DELETE).intValue());
        boolean b = monitor.removeListener(listn);
        assertTrue(b);
    }

    @Test
    @Order(4)
    public void testCreateDir() throws Exception {
        monitor.addListener(listn);
        WaitOn<String> wait = blocker.init("s");

        DirUtil.mkdirsIfAbsent(TMP + "/file-monitor");
        wait.waitForResult(1000);
        assertEquals(2, map.get(EventType.CREATE).intValue());
        assertEquals(1, map.get(EventType.UPDATE).intValue());
        assertEquals(1, map.get(EventType.DELETE).intValue());
    }

    @Test
    @Order(5)
    public void testUpdateDir() throws Exception {
        WaitOn<String> wait = blocker.init("s");

        DirUtil.mkdirsIfAbsent(TMP + "/file-monitor/tmp");
        wait.waitForResult(1000);
        assertEquals(2, map.get(EventType.CREATE).intValue());
        assertEquals(2, map.get(EventType.UPDATE).intValue());
        assertEquals(1, map.get(EventType.DELETE).intValue());
    }

    @Test
    @Order(6)
    public void testUpdateInnerFile() throws Exception {
        WaitOn<String> wait = blocker.init("s");

        FileUtil.setCharacterFileContentToFileSystem(TMP + "/file-monitor/tmp.txt", "Lex", StringUtil.US_ASCII);
        wait.waitForResult(1000);
        assertEquals(2, map.get(EventType.CREATE).intValue());
        assertEquals(3, map.get(EventType.UPDATE).intValue());
        assertEquals(1, map.get(EventType.DELETE).intValue());
    }

    @Test
    @Order(7)
    public void testDeleteDir() throws Exception {
        WaitOn<String> wait = blocker.init("s");
        DirUtil.delete(TMP + "/file-monitor", true);

        wait.waitForResult(1000);
        assertEquals(2, map.get(EventType.CREATE).intValue());
        int k = map.get(EventType.UPDATE).intValue() + map.get(EventType.DELETE).intValue();
        assertTrue(k > 4);
    }

    @Test
    @Order(8)
    public void testRemoveListener() throws Exception {
        assertEquals(1, EventType.DELETE.compareTo(EventType.CREATE));
        assertEquals(EventType.valueOf("DELETE"), EventType.DELETE);
    }

}
