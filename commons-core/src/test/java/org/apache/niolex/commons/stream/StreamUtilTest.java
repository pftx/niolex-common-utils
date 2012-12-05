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

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

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
	 * Test method for {@link org.apache.niolex.commons.stream.StreamUtil#closeStream(java.io.OutputStream)}.
	 */
	@Test
	public void testCloseStreamOutputStream() {
		OutputStream out = null;
		StreamUtil.closeStream(out);
		out = new PipedOutputStream();
		StreamUtil.closeStream(out);
	}

	@Test
	public void testWriteStringOutputStream() throws IOException {
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

}
