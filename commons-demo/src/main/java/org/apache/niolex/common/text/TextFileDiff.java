/**
 * TextFileDiff.java
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
package org.apache.niolex.common.text;

import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.file.FileUtil;

/**
 * Find out the different test cases in two tests.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-10-10
 */
public class TextFileDiff {

    private static final String FILE1 = "E:\\texts\\diff1.txt";
    private static final String FILE2 = "E:\\texts\\diff2.txt";

    /**
     * @param args
     */
    public static void main(String[] args) {
        String[] lines1 = FileUtil.getCharacterFileContentFromFileSystem(FILE1, StringUtil.UTF_8).split("\r?\n");
        String[] lines2 = FileUtil.getCharacterFileContentFromFileSystem(FILE2, StringUtil.UTF_8).split("\r?\n");

        for (int i = 0, j = 0; i < lines1.length && j < lines2.length;) {
            String a = lines1[i];
            String b = lines2[j];
            if (find(a).equals(find(b))) {
                ++i;
                ++j;
            } else if (j + 1 < lines2.length && find(a).equals(find(lines2[j + 1]))) {
                ++j;
                System.out.println("+++ " + j + " " + b);
            } else if (i + 1 < lines1.length && find(b).equals(find(lines1[i + 1]))) {
                ++i;
                System.out.println("--- " + i + " " + a);
            } else {
                ++i;
                ++j;
                System.err.println("*-- " + i + " " + a);
                System.err.println("*++ " + j + " " + b);
            }
        }

    }

    public static String find(String s) {
        int k = s.indexOf('/');
        if (k > 0) k = s.indexOf('/', k + 1);
        return s.substring(k + 1);
    }

}
