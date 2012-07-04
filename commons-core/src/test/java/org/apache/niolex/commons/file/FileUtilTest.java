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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import junit.framework.Assert;

import org.apache.niolex.commons.file.FileUtil;
import org.junit.Test;


/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 *
 * @version 1.0.0, $Date: 2011-9-13$
 *
 */
public class FileUtilTest {

    @Test
    public final void testClass() {
        String str = FileUtil.getCharacterFileContentFromClassPath("Data.txt", FileUtilTest.class, "utf-8");
        System.out.println("SL " + str.length());
        Assert.assertEquals(1833, str.length());
        assertTrue(str.startsWith("HOW-TOs, samples"));
    }

    @Test
    public final void testClassE() {
    	String str = FileUtil.getCharacterFileContentFromClassPath("Data.txt", FileUtilTest.class, "utf-9");
    	System.out.println("SL " + str.length());
    	Assert.assertEquals(0, str.length());
    }

    @Test
    public final void testFile() {
    	String str = FileUtil.getCharacterFileContentFromClassPath("Data.txt", FileUtilTest.class, "utf-8");
    	String prex = System.getProperty("java.io.tmpdir");
    	Assert.assertTrue(FileUtil.setCharacterFileContentToFileSystem(prex + "/tmp_file.txt", str, "utf-8"));

        String str2 = FileUtil.getCharacterFileContentFromFileSystem(prex + "/tmp_file.txt", "utf-8");
        System.out.println("SL " + str2.length());
        Assert.assertEquals(str, str2);

        str2 = FileUtil.getCharacterFileContentFromFileSystem(prex + "/tmp_file.txt", "utf-10");
        Assert.assertEquals("", str2);

        byte[] a = FileUtil.getBinaryFileContentFromFileSystem(prex + "/t/m/p/_/file.txt");
        Assert.assertEquals(null, a);
    }

    @Test
    public final void testFileE() {
    	String prex = System.getProperty("java.io.tmpdir");
    	File f = new File(prex + "/tmp_file.txt");
    	if (f.exists()) {
    		f.delete();
    	}
    	String str1 = "可以对每个连接设置当前的时区，相关描述参见5.10.8节，“MySQL服务器时区支持”。TIMESTAMP值";
    	FileUtil.setCharacterFileContentToFileSystem(prex + "/tmp_file.txt", str1, "utf-8");

    	String str2 = FileUtil.getCharacterFileContentFromFileSystem(prex + "/tmp_file.txt", "utf-8");
    	System.out.println("filee " + str2);
    	assertEquals(str1, str2);
    	boolean b;
    	b = FileUtil.setCharacterFileContentToFileSystem(prex + "'_f^i$le:.txt", str2, "utf-9");
    	assertFalse(b);
    	b = FileUtil.setCharacterFileContentToFileSystem(prex + "'.../_f^i$le:.txt", str2, "utf-8");
    	assertFalse(b);

    	FileUtil.getCharacterFileContentFromClassPath(null, FileUtilTest.class, "utf-8");
    }

    @Test
    public final void testMkdirs() {
    	String prex = "/home/work/data/sync";
    	File f = new File(prex + "/tmp/tmp2/tmp_file.txt");
    	if (f.exists()) {
    		f.delete();
    	}
    	f = new File(prex + "/tmp/tmp2");
    	if (f.exists()) {
    		f.delete();
    	}
    	f = new File(prex + "/tmp");
    	if (f.exists()) {
    		f.delete();
    	}
    	boolean b;
    	b = FileUtil.mkdirsIfAbsent(prex + "/tmp/tmp2");
    	assertTrue(b);
    	b = FileUtil.mkdirsIfAbsent(prex + "/tmp/tmp2");
    	assertTrue(b);
    	String str1 = "如果未指定NULL属性，将列设置为NULL设置则会将它设置为当前的时间戳";
    	FileUtil.setCharacterFileContentToFileSystem(prex + "/tmp/tmp2/tmp_file.txt", str1, "utf-8");

    	String str2 = FileUtil.getCharacterFileContentFromFileSystem(prex + "/tmp/tmp2/tmp_file.txt", "utf-8");
    	System.out.println("mkdir " + str2);
    	assertEquals(str1, str2);
    	b = FileUtil.mkdirsIfAbsent(prex + "/tmp/tmp2/tmp_file.txt/c");
    	assertFalse(b);
    }
}
