/**
 * FileRead.java
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

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import org.apache.niolex.commons.test.StopWatch;
import org.apache.niolex.commons.test.StopWatch.Stop;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-10-9
 */
public class FileRead {

	static final int BUF_SIZE = 10240;

	public static int directRead() throws IOException {
		FileInputStream in = new FileInputStream("D:\\data\\miui\\MiFlash20120723.zip");
		byte[] buf = new byte[BUF_SIZE];
		int cnt = 0, tt;
		while ((tt = in.read(buf)) != -1) {
			cnt += tt;
		}
		in.close();
		return cnt;
	}

	public static int bufferRead() throws IOException {
		FileInputStream in = new FileInputStream("D:\\data\\miui\\MiFlash20120723.zip");
		BufferedInputStream inb = new BufferedInputStream(in);
		byte[] buf = new byte[BUF_SIZE];
		int cnt = 0, tt;
		while ((tt = inb.read(buf)) != -1) {
			cnt += tt;
		}
		inb.close();
		in.close();
		return cnt;
	}

	public static int mmapRead() throws IOException {
		RandomAccessFile file = new RandomAccessFile("D:\\data\\miui\\MiFlash20120723.zip", "r");
		FileChannel channel = file.getChannel();
		MappedByteBuffer map = channel.map(MapMode.READ_ONLY, 0, file.length());
		byte[] buf = new byte[BUF_SIZE];
		int cnt = 0;
		while (true) {
		    int k = map.remaining();
		    if (k > buf.length) {
		        map.get(buf);
		        cnt += buf.length;
		    } else {
		        map.get(buf, 0, k);
		        cnt += k;
		        break;
		    }
		}
		channel.close();
		file.close();
		return cnt;
	}

	public static void main(String[] argv) throws IOException {
		StopWatch sw = new StopWatch(10);

		sw.begin(true);
		for (int i = 0; i < 500; ++i) {
			Stop s = sw.start();
			int k = directRead();
			if (k != 9910842) {
				System.out.println("ERR " + k);
				break;
			}
			s.stop();
		}
		sw.done();
		sw.print();

		sw.begin(true);
		for (int i = 0; i < 500; ++i) {
			Stop s = sw.start();
			int k = bufferRead();
			if (k != 9910842) {
				System.out.println("ERR " + k);
				break;
			}
			s.stop();
		}
		sw.done();
		sw.print();

		sw.begin(true);
		for (int i = 0; i < 500; ++i) {
			Stop s = sw.start();
			int k = mmapRead();
			if (k != 9910842) {
				System.out.println("ERR " + k);
				break;
			}
			s.stop();
		}
		sw.done();
		sw.print();
	}

}
