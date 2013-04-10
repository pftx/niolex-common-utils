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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import org.apache.niolex.commons.test.MockUtil;
import org.apache.niolex.commons.test.StopWatch;
import org.apache.niolex.commons.test.StopWatch.Stop;
import org.apache.niolex.commons.util.SystemUtil;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-9-12
 */
public class StreamWrite {
	static final int WRITE_BATCH = 100;
	static final int WRITE_ONE = 102400;
	static final int RUN_ITER = 60;
	static final int BUFFER_SIZE = 10240;

	byte[][] data = new byte[WRITE_BATCH][];

	public void directWrite() throws IOException {
		FileOutputStream out = new FileOutputStream("D:\\home\\tmp\\" + System.nanoTime());
		for (int i = 0; i < data.length; i++)
			out.write(data[i]);
		out.close();
	}

	public void bufferWrite() throws IOException {
		FileOutputStream out = new FileOutputStream("D:\\home\\tmp\\" + System.nanoTime());
		BufferedOutputStream outb = new BufferedOutputStream(out, BUFFER_SIZE);
		for (int i = 0; i < data.length; i++)
			outb.write(data[i]);
		outb.close();
		out.close();
	}

	public void randomWrite() throws IOException {
		RandomAccessFile file = new RandomAccessFile("D:\\home\\tmp\\" + System.nanoTime(), "rw");
		for (int i = 0; i < data.length; i++)
			file.write(data[i]);
		file.close();
	}

	public void channelWrite() throws IOException {
	    RandomAccessFile file = new RandomAccessFile("D:\\home\\tmp\\" + System.nanoTime(), "rw");
	    FileChannel channel = file.getChannel();
	    for (int i = 0; i < data.length; i++)
	        channel.write(ByteBuffer.wrap(data[i]));
	    channel.close();
	    file.close();
	}

	public void mmapWrite() throws IOException {
		RandomAccessFile file = new RandomAccessFile("D:\\home\\tmp\\" + System.nanoTime(), "rw");
		FileChannel channel = file.getChannel();
		MappedByteBuffer buffer = null;
		buffer = channel.map(MapMode.READ_WRITE, 0, WRITE_BATCH * WRITE_ONE);
		for (int i = 0; i < data.length; i++) {
		    buffer.put(data[i]);
		}
		buffer.force();
		channel.close();
		file.close();
	}

	public void clean() throws IOException {
		File f = new File("D:\\home\\tmp\\");
		for (File t : f.listFiles()) {
			t.delete();
		}
	}

	public void init() {
		for (int i = 0; i < data.length; i++)
			data[i] = MockUtil.randByteArray(WRITE_ONE);
	}

	public static void main(String[] argv) throws IOException {
		StopWatch sw = new StopWatch(10);
		StreamWrite wr = new StreamWrite();
		wr.init();
		System.out.println("Parameters: [" + WRITE_BATCH + "][" + WRITE_ONE +"]");
		wr.clean();

		sw.begin(true);
		System.out.println("bufferWrite");
		for (int i = 0; i < RUN_ITER; ++i) {
			Stop s = sw.start();
			wr.bufferWrite();
			s.stop();
		}
		sw.done();
		sw.print();
		wr.clean();

		sw.begin(true);
		System.out.println("directWrite");
		for (int i = 0; i < RUN_ITER; ++i) {
			Stop s = sw.start();
			wr.directWrite();
			s.stop();
		}
		sw.done();
		sw.print();
		wr.clean();

		sw.begin(true);
		System.out.println("randomWrite");
		for (int i = 0; i < RUN_ITER; ++i) {
			Stop s = sw.start();
			wr.randomWrite();
			s.stop();
		}
		sw.done();
		sw.print();
		wr.clean();

		sw.begin(true);
		System.out.println("channelWrite");
		for (int i = 0; i < RUN_ITER; ++i) {
			Stop s = sw.start();
			wr.channelWrite();
			s.stop();
		}
		sw.done();
		sw.print();
		wr.clean();

		SystemUtil.sleep(1000);
		wr.clean();
	}

}
