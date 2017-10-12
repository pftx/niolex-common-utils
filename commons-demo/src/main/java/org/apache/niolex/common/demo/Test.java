/**
 * Test.java
 *
 * Copyright 2014 the original author or authors.
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
package org.apache.niolex.common.demo;

import java.security.MessageDigest;

import org.apache.niolex.commons.codec.Base16Util;
import org.apache.niolex.commons.codec.CipherUtil;
import org.apache.niolex.commons.file.FileUtil;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-11-13
 */
public class Test extends CipherUtil {

    /**
     * @param args
     */
    public static void main(String[] args) {
        MessageDigest md = getInstance("MD5");
        byte[] input = FileUtil.getBinaryFileContentFromFileSystem("C:\\Users\\Administrator\\Downloads\\spdbsign_v5_x86.zip", 15778199);
        md.update(input);
        byte bytes[] = md.digest();

        System.out.println(Base16Util.byteToBase16(bytes));

    }

}
