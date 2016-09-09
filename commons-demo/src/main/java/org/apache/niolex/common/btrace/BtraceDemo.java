/**
 * BtraceDemo.java
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
package org.apache.niolex.common.btrace;

import java.util.Random;
import java.util.Scanner;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-8-28
 */
public class BtraceDemo {
	private Random random = new Random();
	private int totalSleepTime = 0;

	/**
	 * Generate hash code, sleep some random time.
	 * @param s
	 * @return
	 * @throws InterruptedException
	 */
	public int inputHashCode(String s) throws InterruptedException {
		int k = 0;
		k += s.hashCode();
		k = (k << 3) + s.length();
		int i = random.nextInt(1000);
		Thread.sleep(i);
		totalSleepTime += i;
		return k;
	}

	/**
	 * Get the current total sleep time.
	 * @return
	 */
	public int getTotalSleepTime() {
		return totalSleepTime;
	}

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		final Scanner scan = new Scanner(System.in);
		System.out.println("Welcome to BtraceDemo Console.");
		BtraceDemo bd = new BtraceDemo();
		while (true) {
			System.out.print("#> ");
			String line = scan.nextLine();
			int k = bd.inputHashCode(line);
			System.out.println("Your hash code :" + k + ", tst: " + bd.getTotalSleepTime());
            if (k == 0) {
                break;
            }
		}
        scan.close();
	}

}
