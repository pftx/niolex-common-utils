/**
 * StreamUtil.java
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
package org.apache.niolex.commons.stream;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.util.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Some common function for input and output streams.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, Date: 2012-7-4
 */
public class StreamUtil {
	private static final Logger LOG = LoggerFactory.getLogger(StreamUtil.class);

	/**
	 * Close the specified stream. It's OK the parameter be null.
	 *
	 * @param s the stream to be closed
	 */
	public static final Exception closeStream(Closeable s) {
		return SystemUtil.close(s);
	}

	/**
	 * Read data from the input stream and put them into the byte array.
	 *
	 * @param in the input stream
	 * @param data the byte array to put data
	 * @return the number of bytes read from input stream
	 */
	public static final int readData(InputStream in, byte[] data) throws IOException {
	    int dataPos = 0, length = data.length;
        int count = 0;
        while ((count = in.read(data, dataPos, length - dataPos)) >= 0) {
            dataPos += count;
            if (dataPos == length) {
                break;
            }
        }
        return dataPos;
	}

	/**
	 * Write an string to the output stream.
	 * @param out
	 * @param s
	 */
	public static final void writeString(OutputStream out, String s) {
		try {
			out.write(StringUtil.strToUtf8Byte(s));
		} catch (Exception ie) {
			LOG.info("Failed to write string to stream - " + ie.getMessage());
		}
	}

}
