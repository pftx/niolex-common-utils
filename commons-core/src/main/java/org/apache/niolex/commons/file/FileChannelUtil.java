/**
 * FileChannelUtil.java
 *
 * Copyright 2016 the original author or authors.
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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Read data from file channel and write into file channel.
 * 
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 2.1.3
 * @since Sep 6, 2016
 */
public class FileChannelUtil {

    /**
     * Read bytes from the file channel to fulfill the given byte buffer.
     * 
     * @param channel the file channel used to read data
     * @param buf the buffer used to put data
     * @param position the file position to start read
     * @return the number of bytes read
     * @throws IOException if I/O error occurs
     */
    public static int readFromPosition(FileChannel channel, ByteBuffer buf, long position) throws IOException {
        int total = 0, k = 0;
        while (buf.hasRemaining()) {
            k = channel.read(buf, position + total);
            if (k < 0) {
                return total;
            } else {
                total += k;
            }
        }
        return total;
    }

    /**
     * Write all the bytes in the specified byte buffer into the file channel start from the given position.
     * 
     * @param channel the file channel used to write data
     * @param buf the buffer used to get data
     * @param position the file position to start write
     * @return the number of bytes written
     * @throws IOException if I/O error occurs
     */
    public static int writeToPosition(FileChannel channel, ByteBuffer buf, long position) throws IOException {
        int total = 0, k = 0;
        while (buf.hasRemaining()) {
            k = channel.write(buf, position + total);
            if (k < 0) {
                return total;
            } else {
                total += k;
            }
        }
        return total;
    }

}
