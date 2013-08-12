/**
 * DirUtilTest.java
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

import java.io.File;

import org.apache.niolex.commons.codec.StringUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-7-22
 */
public class DirUtilTest extends DirUtil {

    static final String PREX = System.getProperty("user.home");
    static final String TMP = PREX + "/tmp/dirtest";

    @BeforeClass
    public static void setUp() {
        // -- Make sure we have this directory.
        mkdirsIfAbsent(TMP);
    }

    @AfterClass
    public static void tearDown() {
        delete(TMP, true);
    }

    @Test
    public void testIsDir() throws Exception {
        assertTrue(isDir(PREX));
    }

    @Test
    public void testIsDirTmp() throws Exception {
        assertTrue(isDir(TMP));
    }

    @Test
    public void testIsDirNotExist() throws Exception {
        assertFalse(isDir(PREX + "/893jd832kaf83412"));
    }

    @Test
    public final void testMkdirsIfAbsent1() {
        // -- Create a temp file for this test.
        File f = new File(TMP + "/tmp2");
        boolean b;
        b = delete(f, true);
        assertTrue(b);
        // No file
        b = mkdirsIfAbsent(TMP + "/tmp2");
        assertTrue(b);
        // Have file
        b = mkdirsIfAbsent(TMP + "/tmp2");
        assertTrue(b);
        // --- Done here. We create a test file.
        String str1 = "如果未指定NULL属性，将列设置为NULL设置则会将它设置为当前的时间戳";
        b = FileUtil.setCharacterFileContentToFileSystem(TMP + "/tmp2/tmp_file.txt", str1, StringUtil.UTF_8);
        assertTrue(b);
        // File OK
        String str2 = FileUtil.getCharacterFileContentFromFileSystem(TMP + "/tmp2/tmp_file.txt", StringUtil.UTF_8);
        assertEquals(str1, str2);
        // File Not Found
        String str3 = FileUtil.getCharacterFileContentFromFileSystem(TMP + "/tmp2/not_found.txt", StringUtil.UTF_8);
        assertNull(str3);
    }

    @Test
    public void testMkdirsIfAbsent2() throws Exception {
        boolean b;
        // Make file under file, should return false.
        b = mkdirsIfAbsent(TMP + "/tmp2/tmp_file.txt/cba");
        assertFalse(b);
    }

    @Test
    public void testDeleteDir() throws Exception {
        boolean b;
        b = mkdirsIfAbsent(PREX + "/tmp2/tmp_file.txt/cba");
        assertTrue(b);
        b = delete(PREX + "/tmp2", false);
        assertFalse(b);
        b = delete(PREX + "/tmp2", true);
        assertTrue(b);
        // Already deleted, we delete again.
        b = delete(PREX + "/tmp2", false);
        assertTrue(b);
    }

}
