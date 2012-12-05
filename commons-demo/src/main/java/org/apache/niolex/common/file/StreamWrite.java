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
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import org.apache.niolex.commons.test.MockUtil;
import org.apache.niolex.commons.test.StopWatch;
import org.apache.niolex.commons.test.StopWatch.Stop;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-9-12
 */
public class StreamWrite {
	static final int WRITE_BATCH = 10000;
	static final int WRITE_ONE = 1024;
	static final int RUN_ITER = 200;

	byte[][] data = new byte[WRITE_BATCH][];

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
			outb.write(data[i]);
		outb.close();
		out.close();
	}

	public void randomWrite() throws IOException {
		RandomAccessFile file = new RandomAccessFile("D:\\data\\tmp\\" + System.nanoTime(), "rw");
		for (int i = 0; i < data.length; i++)
			file.write(data[i]);
		file.close();
	}

	public void mmapWrite() throws IOException {
		RandomAccessFile file = new RandomAccessFile("D:\\data\\tmp\\" + System.nanoTime(), "rw");
		FileChannel channel = file.getChannel();
		channel.map(MapMode.READ_WRITE, 0, WRITE_ONE);
		for (int i = 0; i < data.length; i++)
			channel.write(ByteBuffer.wrap(data[i]));
		channel.close();
		file.close();
	}

	public void clean() throws IOException {
		File f = new File("D:\\data\\tmp\\");
		for (File t : f.listFiles()) {
			t.delete();
		}
	}

	public void init() {
		for (int i = 0; i < data.length; i++)
			data[i] = MockUtil.randByteArray(WRITE_ONE);
	}

	/**
     * A方法追加文件：使用RandomAccessFile
     */
    public static void appendMethodA(String fileName, String content) {
        try {
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            //将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.writeBytes(content);
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * B方法追加文件：使用FileWriter
     */
    public static void appendMethodB(String fileName, String content) {
        try {
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

		sw.begin(true);
		System.out.println("directWrite");
		for (int i = 0; i < RUN_ITER; ++i) {
			Stop s = sw.start();
			wr.directWrite();
			s.stop();
		}
		sw.done();
		sw.print();

		sw.begin(true);
		System.out.println("randomWrite");
		for (int i = 0; i < RUN_ITER; ++i) {
			Stop s = sw.start();
			wr.randomWrite();
			s.stop();
		}
		sw.done();
		sw.print();

		sw.begin(true);
		System.out.println("mmapWrite");
		for (int i = 0; i < RUN_ITER; ++i) {
			Stop s = sw.start();
			wr.mmapWrite();
			s.stop();
		}
		sw.done();
		sw.print();

		wr.clean();
	}

}
