/**
 * Rename.java
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
package org.apache.niolex.common.file;

import java.io.File;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-8-14
 */
public class Rename {

    private static final String PATH = "F:\\download\\tmp\\Notifications\\";

    /**
     * @param args
     */
    public static void main(String[] args) {
        File f = new File(PATH);
        File[] files = f.listFiles();
        int j = (int) Math.pow(36, 2) * 1;
        for (File i : files) {
            String name = Integer.toString(j++, 36);
            String oname = i.getName();
            int s = oname.lastIndexOf(".");
            String sufix = oname.substring(s).toLowerCase();
            i.renameTo(new File(PATH + name + sufix));
        }

    }

}
