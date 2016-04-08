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
	 * Close the specified stream. It's OK if the parameter is null.
	 *
	 * @param s the stream to be closed
	 * @return any execution thrown during close the closeable
	 */
	public static final Exception closeStream(Closeable s) {
		return SystemUtil.close(s);
	}

	/**
	 * Read data from the input stream and put them into the byte array.
	 *
	 * @param in the input stream
	 * @param data the byte array to put data
	 * @return the number of bytes read from the input stream
	 * @throws IOException if necessary
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
	 * Write a string as UTF8 to the output stream.
	 *
	 * @param out the output stream
	 * @param s the string to be written
	 * @throws IOException if an I/O error occurs
	 */
	public static final void writeUTF8(OutputStream out, String s) throws IOException {
	    out.write(StringUtil.strToUtf8Byte(s));
	}

	/**
	 * Write a string as UTF8 to the output stream. Any I/O error occurred during
	 * the write will be ignored.
	 *
	 * @param out the output stream
	 * @param s the string to be written
	 */
	public static final void writeUTF8IgnoreException(OutputStream out, String s) {
	    try {
	        out.write(StringUtil.strToUtf8Byte(s));
	    } catch (IOException e) {
	        LOG.info("IOException occurred in writeUTF8IgnoreException: {}", e.getMessage());
	    }
	}

	/**
	 * Write the data to the output stream and close it.
	 *
	 * @param out the output stream
	 * @param data the data to be written
	 * @throws IOException if necessary
	 */
	public static final void writeAndClose(OutputStream out, byte[] data) throws IOException {
	    try {
	        out.write(data);
	    } finally {
	        out.close();
	    }
	}

	/**
	 * Write the data to the output stream and close it. Any I/O error occurred during the write or close
	 * will be ignored.
	 *
	 * @param out the output stream
	 * @param data the data to be written
	 */
	public static final void writeAndCloseIgnoreException(OutputStream out, byte[] data) {
	    try {
	        out.write(data);
	    } catch (IOException e) {
	        LOG.info("IOException occurred in writeAndCloseIgnoreException: {}", e.getMessage());
	    } finally {
	        closeStream(out);
	    }
	}

	/**
	 * Transfer all the data from the input stream to the output stream and close both of them.
	 *
	 * @param in the input stream
	 * @param out the output stream
	 * @param bufSize the transfer buffer size
	 * @return the number of bytes transfered
	 * @throws IOException if necessary
	 */
	public static int transferAndClose(InputStream in, OutputStream out, final int bufSize) throws IOException {
	    try {
	        byte[] data = new byte[bufSize];
	        int len, cnt = 0;
	        while ((len = in.read(data)) != -1) {
	            out.write(data, 0, len);
	            cnt += len;
	        }

	        return cnt;
	    } finally {
	        closeStream(in);
	        closeStream(out);
	    }
	}

}
