/**
 * StreamUtilTest.java
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

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.apache.niolex.commons.test.MockUtil;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-7-4
 */
public class StreamUtilTest {

	/**
	 * Test method for {@link org.apache.niolex.commons.stream.StreamUtil#closeStream(java.io.InputStream)}.
	 */
	@Test
	public void testCloseStreamInputStream() {
		InputStream in = null;
		StreamUtil.closeStream(in);
		in = new PipedInputStream();
		StreamUtil.closeStream(in);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.stream.StreamUtil#closeStream(java.io.InputStream)}.
	 * @throws IOException
	 */
	@Test
	public void testCloseStreamInputStreamError() throws IOException {
	    InputStream in = mock(InputStream.class);
	    doThrow(new IOException("Just 4 test")).when(in).close();
	    StreamUtil.closeStream(in);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.stream.StreamUtil#closeStream(java.io.OutputStream)}.
	 */
	@Test
	public void testCloseStream() {
		OutputStream out = null;
		StreamUtil.closeStream(out);
		out = new PipedOutputStream();
		StreamUtil.closeStream(out);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.stream.StreamUtil#closeStream(java.io.OutputStream)}.
	 */
	@Test
	public void testCloseStreamError() throws IOException {
	    OutputStream out = mock(OutputStream.class);
        doThrow(new IOException("Just 4 test")).when(out).close();
        assertNotNull(StreamUtil.closeStream(out));
	}

	@Test
	public void testReadData() throws Exception {
	    byte[] buf = MockUtil.randByteArray(20);
	    byte[] data = new byte[20];
        InputStream in = new ByteArrayInputStream(buf);
        StreamUtil.readData(in, data);
        assertArrayEquals(buf, data);
	}

	@Test
	public void testReadDataIF() throws Exception {
	    byte[] buf = MockUtil.randByteArray(6);
        byte[] data = new byte[20];
        InputStream in = new ByteArrayInputStream(buf);
        int s = StreamUtil.readData(in, data);
        assertEquals(6, s);
	}

	@Test
	public void testWriteString() throws IOException {
		PipedOutputStream out = null;
		StreamUtil.closeStream(out);
		out = new PipedOutputStream();
		PipedInputStream snk = new PipedInputStream();
		out.connect(snk);
		System.out.println(snk.available());
		StreamUtil.writeString(out, "This is so good");
		System.out.println(snk.available());
		assertEquals(15, snk.available());
	}

	@Test
	public void testWriteStringError() throws IOException {
	    OutputStream out = mock(OutputStream.class);
        doThrow(new IOException("Just 4 test")).when(out).write(any(byte[].class));
	    StreamUtil.writeString(out, "This is so good");
	    new StreamUtil();
	}

}
