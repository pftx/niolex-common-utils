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
    }

    @Test
    public final void testFileE() {
    	String prex = System.getProperty("java.io.tmpdir");
    	File f = new File(prex + "/tmp_file.txt");
    	if (f.exists()) {
    		f.delete();
    	}
    	String str2 = FileUtil.getCharacterFileContentFromFileSystem(prex + "/tmp_file.txt", "utf-8");
    	FileUtil.setCharacterFileContentToFileSystem(prex + "/tmp\\_f^i$le:.txt", str2, "utf-9");
    	FileUtil.setCharacterFileContentToFileSystem(null, str2, "utf-9");

    	FileUtil.getCharacterFileContentFromClassPath(null, FileUtilTest.class, "utf-8");
    }
}
