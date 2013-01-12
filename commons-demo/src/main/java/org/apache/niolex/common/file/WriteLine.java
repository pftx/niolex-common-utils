/**
 * WriteLine.java
 *
 * Copyright 2012 Niolex, Inc.
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
package org.apache.niolex.common.file;

import java.io.FileWriter;
import java.io.IOException;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2012-12-24$
 */
public class WriteLine {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String s = "D:\\home\\tmp\\" + System.nanoTime();
        FileWriter w = new FileWriter(s);
        w.write("This is good!\r\n");
        w.write("This is end!\n");
        System.out.print(s);
        w.close();
    }

}
