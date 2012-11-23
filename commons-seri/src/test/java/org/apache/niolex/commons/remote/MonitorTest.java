/**
 * MonitorTest.java
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
package org.apache.niolex.commons.remote;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.OutputStream;

import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5, $Date: 2012-11-23$
 */
public class MonitorTest {

	/**
	 * Test method for {@link org.apache.niolex.commons.remote.Monitor#addValue(java.lang.String, int)}.
	 * @throws IOException
	 */
	@Test
	public void testAddValue() throws IOException {
		Monitor m = new Monitor(10);
		m.addValue("tes", 0);
		OutputStream o = mock(OutputStream.class);
		OutputStream o2 = mock(OutputStream.class);
		doThrow(new IOException("")).when(o2).write(any(byte[].class));
		m.doMonitor(o, "tes", "r");
		m.doMonitor(o2, "tes", "R");
		m.addValue("tes", 1);
		m.addValue("tes", 2);
		m.addValue("test", 0);
		verify(o, times(2)).write(any(byte[].class));
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.remote.Monitor#doMonitor(java.io.OutputStream, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testDoMonitor() throws IOException {
		Monitor m = new Monitor(10);
		m.addValue("tes", 0);
		OutputStream o = mock(OutputStream.class);
		OutputStream o2 = mock(OutputStream.class);
		doThrow(new IOException("")).when(o2).write(any(byte[].class));
		m.doMonitor(o, "tes", "h");
		boolean flag = false;
		try {
			m.doMonitor(o2, "tes", "H");
		} catch (IOException e) {
			flag = true;
		}
		assertTrue(flag);
		m.addValue("tes", 1);
		m.addValue("tes", 2);
		m.addValue("test", 0);
		verify(o, times(1)).write(any(byte[].class));
	}

	@Test
	public void testAttachReadTime() throws Exception {
		Monitor m = new Monitor(10);
		m.addValue("tes", 0);
		m.addValue("tes", 8);
		OutputStream o = mock(OutputStream.class);
		OutputStream o2 = mock(OutputStream.class);
		doThrow(new IOException("")).when(o2).write(any(byte[].class));
		m.doMonitor(o, "tes", "w");
		boolean flag = false;
		try {
			m.doMonitor(o2, "tes", "W");
		} catch (IOException e) {
			flag = true;
		}
		assertTrue(flag);
		m.addValue("tes", 1);
		m.addValue("tes", 2);
		m.addValue("qwt", 0);
		verify(o, times(3)).write(any(byte[].class));
		m.doMonitor(o, "test", "h");
		m.addValue("test", 0);
		m.addValue("test", 1);
		verify(o, times(4)).write(any(byte[].class));
	}

}
