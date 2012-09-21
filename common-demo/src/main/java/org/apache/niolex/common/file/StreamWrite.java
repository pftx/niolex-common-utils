/**
 * StreamWrite.java
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

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.apache.niolex.commons.test.MockUtil;
import org.apache.niolex.commons.test.StopWatch;
import org.apache.niolex.commons.test.StopWatch.Stop;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-9-12
 */
public class StreamWrite {
	byte[][] data = new byte[10240][];

	public void directWrite() throws IOException {
		FileOutputStream out = new FileOutputStream("D:\\data\\tmp\\" + System.nanoTime());
		for (int i = 0; i < data.length; i++)
			out.write(data[i]);
		out.close();
	}

	public void bufferWrite() throws IOException {
		FileOutputStream out = new FileOutputStream("D:\\data\\tmp\\" + System.nanoTime());
		BufferedOutputStream outb = new BufferedOutputStream(out);
		for (int i = 0; i < data.length; i++)
			out.write(data[i]);
		outb.close();
	}

	public void mmapWrite() throws IOException {
		RandomAccessFile file = new RandomAccessFile("D:\\data\\tmp\\" + System.nanoTime(), "rw");
		FileChannel channel = file.getChannel();
		for (int i = 0; i < data.length; i++)
			channel.write(ByteBuffer.wrap(data[i]));
		channel.close();
		file.close();
	}

	public void init() {
		for (int i = 0; i < data.length; i++)
			data[i] = MockUtil.randByteArray(200);
	}

	public static void main(String[] argv) throws IOException {
		StopWatch sw = new StopWatch(10);
		StreamWrite wr = new StreamWrite();
		wr.init();

		sw.begin(true);
		for (int i = 0; i < 100; ++i) {
			Stop s = sw.start();
			wr.directWrite();
			s.stop();
		}
		sw.done();
		sw.print();

		sw.begin(true);
		for (int i = 0; i < 100; ++i) {
			Stop s = sw.start();
			wr.bufferWrite();
			s.stop();
		}
		sw.done();
		sw.print();

		sw.begin(true);
		for (int i = 0; i < 100; ++i) {
			Stop s = sw.start();
			wr.mmapWrite();
			s.stop();
		}
		sw.done();
		sw.print();
	}

}
