/**
 * Finally.java
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
package org.apache.niolex.commons.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-6-14
 */
public class Finally {

    public static void writeAndClose(OutputStream zout, byte[] data) throws IOException {
        try {
            zout.write(data);
        } finally {
            zout.close();
        }
    }

    public static void transferAndClose(InputStream in, OutputStream out, final int BUF_SIZE) throws IOException {
        try {
            byte[] data = new byte[BUF_SIZE];
            int len;
            while ((len = in.read(data)) != -1) {
                out.write(data, 0, len);
            }
        } finally {
            in.close();
            out.close();
        }
    }

}
