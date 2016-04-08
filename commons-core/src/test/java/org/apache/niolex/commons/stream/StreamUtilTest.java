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
import java.io.ByteArrayOutputStream;
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
public class StreamUtilTest extends StreamUtil {

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
	public void testWriteUTF8() throws IOException {
	    PipedOutputStream out = null;
	    closeStream(out);
	    out = new PipedOutputStream();
	    PipedInputStream snk = new PipedInputStream();
	    out.connect(snk);
	    System.out.println(snk.available());
	    writeUTF8(out, "This is so good");
	    System.out.println(snk.available());
	    assertEquals(15, snk.available());
	}

	@Test(expected=IOException.class)
	public void testWriteUTF8Error() throws IOException {
	    OutputStream out = mock(OutputStream.class);
	    doThrow(new IOException("Just 4 test")).when(out).write(any(byte[].class));
	    writeUTF8(out, "This is so good");
	}

	@Test
	public void testWriteUTF8IgnoreExceptionOK() throws Exception {
	    PipedOutputStream out = null;
	    closeStream(out);
	    out = new PipedOutputStream();
	    PipedInputStream snk = new PipedInputStream();
	    out.connect(snk);
	    System.out.println(snk.available());
	    writeUTF8IgnoreException(out, "This is so good");
	    System.out.println(snk.available());
	    assertEquals(15, snk.available());
	}

	@Test
	public void testWriteUTF8IgnoreExceptionEx() throws Exception {
	    OutputStream out = mock(OutputStream.class);
	    doThrow(new IOException("Just 4 test")).when(out).write(any(byte[].class));
	    writeUTF8IgnoreException(out, "This is so good");
	}

	@Test
	public void testWriteAndCloseOK() throws Exception {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    byte[] arr = MockUtil.randByteArray(15);

	    writeAndClose(out, arr);
	    assertArrayEquals(out.toByteArray(), arr);
	}

    @Test(expected=IOException.class)
    public void testWriteAndClose() throws Exception {
        OutputStream out = mock(OutputStream.class);
        doThrow(new IOException("This")).when(out).write(null);
        try {
        writeAndClose(out, null);
        } finally {
        verify(out).close();
        }
    }

    @Test(expected=IOException.class)
    public void testTransferAndClose() throws Exception {
        InputStream in = mock(InputStream.class);
        doThrow(new IOException("Mock")).when(in).read(any(byte[].class));
        doThrow(new IOException("Mock")).when(in).close();
        OutputStream out = mock(OutputStream.class);
        doThrow(new IOException("Mock")).when(out).close();
        try {
        transferAndClose(in, out, 1024);
        } finally {
        verify(in).close();
        verify(out).close();
        }
    }

    @Test
    public void testTransferAndCloseOK() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] arr = MockUtil.randByteArray(15);
        ByteArrayInputStream in = new ByteArrayInputStream(arr);
        ByteArrayInputStream spyin = spy(in);

        transferAndClose(spyin, out, 100);
        assertArrayEquals(out.toByteArray(), arr);

        verify(spyin).close();
    }

    @Test
    public void testWriteAndCloseIgnoreExceptionOK() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] arr = MockUtil.randByteArray(15);

        writeAndCloseIgnoreException(out, arr);
        assertArrayEquals(out.toByteArray(), arr);
    }

    @Test
    public void testWriteAndCloseIgnoreExceptionNOK() throws Exception {
        OutputStream out = mock(OutputStream.class);
        doThrow(new IOException("This")).when(out).write(null);
        writeAndCloseIgnoreException(out, null);
        verify(out).write(null);
        verify(out).close();
    }

    @Test(expected=NullPointerException.class)
    public void testWriteAndCloseIgnoreException() throws Exception {
        writeAndCloseIgnoreException(null, null);
    }

}
