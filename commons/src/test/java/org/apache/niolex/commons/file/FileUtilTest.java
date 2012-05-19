/**
 * FileUtilTest.java
 *
 * Copyright 2011 Baidu, Inc.
 *
 * Baidu licenses this file to you under the Apache License, version 2.0
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

import junit.framework.Assert;

import org.apache.niolex.commons.file.FileUtil;
import org.junit.Test;


/**
 * @author @author@ (@author-email@)
 * 
 * @version @version@, $Date: 2011-9-13$
 * 
 */
public class FileUtilTest {

    @Test
    public final void testClass() {
        String str = FileUtil.getCharacterFileContentFromClassPath("/com/baidu/api/core/file/request_template", "utf-8");
        System.out.println("SL " + str.length() + "\n" + str);
        Assert.assertEquals(834, str.length());
    }
    
    @Test
    public final void testFile() {
        String str = FileUtil.getCharacterFileContentFromFileSystem("/home/work/darwin/api-gateway/conf/apiquota", "utf-8");
        System.out.println("SL " + str.length() + "\n" + str);
        Assert.assertEquals(13, str.length());
        Assert.assertTrue(FileUtil.setCharacterFileContentToFileSystem("c:/tdown/a.txt", str, "utf-8"));
    }
}
