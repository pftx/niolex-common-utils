/**
 * FileUtilTest.java
 *
 * Copyright 2011 Niolex, Inc.
 *
 * Niolex licenses this file to you under the Apache License, version 2.0
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

import java.nio.charset.Charset;

import org.apache.niolex.commons.codec.StringUtil;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 *
 * @version 1.0.0, $Date: 2011-9-13$
 *
 */
public class FileUtilTest {

    static Charset UTF8 = StringUtil.UTF_8;
    static String PREX = System.getProperty("user.home") + "/tmp";

    @BeforeClass
    public static void setUp() {
        DirUtil.mkdirsIfAbsent(PREX);
        String str1 = "可以对每个连接设置当前的时区，相关描述参见5.10.8节，“MySQL服务器时区支持”。TIMESTAMP值"
                + "结构复杂数据的可视化：欧文提供数据库结构，管理界面的容易简单，图形显示对视觉复杂。";
        FileUtil.setCharacterFileContentToFileSystem(PREX + "/tmp_file.txt", str1, UTF8);
    }

    @Test(expected=IllegalStateException.class)
    public void testGetBinaryFileContentFromFileSystemStringInt() throws Exception {
        try {
            FileUtil.getBinaryFileContentFromFileSystem(PREX + "/tmp_file.txt", 5);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "File too large. max 5, file size 245");
            throw e;
        }
    }

    @Test
    public final void testCharacterFileContent() {
    	String str2 = FileUtil.getCharacterFileContentFromFileSystem(PREX + "/tmp_file.txt", UTF8);
    	System.out.println("filee " + str2);
        String str1 = "可以对每个连接设置当前的时区，相关描述参见5.10.8节，“MySQL服务器时区支持”。TIMESTAMP值"
                + "结构复杂数据的可视化：欧文提供数据库结构，管理界面的容易简单，图形显示对视觉复杂。";
    	assertEquals(str1, str2);
    	boolean b;
    	b = FileUtil.setCharacterFileContentToFileSystem(PREX + "'.../_f^i$le:.txt", str2, UTF8);
    	assertFalse(b);
    }

    @Test
    public final void testFileReadAndWrite() {
        String str = FileUtil.getCharacterFileContentFromClassPath("Data.txt", FileUtilTest.class, UTF8);
        Assert.assertTrue(FileUtil.setCharacterFileContentToFileSystem(PREX + "/tmp_file.txt", str, UTF8));

        String str2 = FileUtil.getCharacterFileContentFromFileSystem(PREX + "/tmp_file.txt", UTF8);
        System.out.println("SL1 " + str2.length());
        Assert.assertEquals(str, str2);

        byte[] a = FileUtil.getBinaryFileContentFromFileSystem(PREX + "/t/m/p/_/file.txt");
        Assert.assertEquals(null, a);

        str = "TIMESTAMP NULL DEFAULT NULL";
        Assert.assertTrue(FileUtil.setCharacterFileContentToFileSystem(PREX + "/tmp_file.txt", str, UTF8));
        str2 = FileUtil.getCharacterFileContentFromFileSystem(PREX + "/tmp_file.txt", UTF8);
        System.out.println("SL2 " + str2.length());
        Assert.assertEquals(str, str2);
    }

    // -------------------------------------------------------------------------
    // FromClassPath
    // -------------------------------------------------------------------------

    @Test
    public void testGetBinaryFileContentFromClassPathNormal() throws Exception {
        byte[] data = FileUtil.getBinaryFileContentFromClassPath("Data.txt", FileUtilTest.class);
        System.out.println("Binary File SL " + data.length);
        Assert.assertEquals(1783, data.length);
    }

    @Test
    public void testGetBinaryFileContentFromClassPathNotFound() throws Exception {
        byte[] data = FileUtil.getBinaryFileContentFromClassPath("NoFile.txt", FileUtilTest.class);
        Assert.assertNull(data);
    }

    @Test
    public void testGetBinaryFileContentFromClassPathException() throws Exception {
        byte[] data = FileUtil.getBinaryFileContentFromClassPath(null, FileUtilTest.class);
        Assert.assertNull(data);
    }

    @Test
    public void testGetCharacterFileContentFromClassPathNormal() {
        String str = FileUtil.getCharacterFileContentFromClassPath("Data.txt", FileUtilTest.class, StringUtil.UTF_8);
        System.out.println("SL " + str.length());
        Assert.assertEquals(1783, str.length());
        assertTrue(str.startsWith("HOW-TOs, samples"));
    }

    @Test
    public void testGetCharacterFileContentFromClassPathNotFound() throws Exception {
        String str = FileUtil.getCharacterFileContentFromClassPath("NoFile.txt", FileUtilTest.class, StringUtil.UTF_8);
        Assert.assertNull(str);
    }

}
