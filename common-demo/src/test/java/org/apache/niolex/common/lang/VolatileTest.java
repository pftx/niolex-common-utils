/**
 * VolatileTest.java
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
package org.apache.niolex.common.lang;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-6-18
 */
public class VolatileTest {

	static {
		System.out.println("VolatileTest static");
	}

	private volatile int size;

	@Test
	public void test() {
		B.test();
		Runnable r = new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 2000; ++i) {
					++size;
					Thread.yield();
				}

			}

		};

		final int SIZE = 6;
		Thread[] ts = new Thread[SIZE];
		for (int i = 0; i < SIZE; ++i) {
			(ts[i] = new Thread(r)).start();
		}

		for (int i = 0; i < SIZE; ++i) {
			try {
				ts[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("volatile size: " + size);
		if (size == 12000)
			assertEquals(12000, size);
	}

	@Test
	public void lockTest() {
		Runnable r = new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 200000; ++i) {
					synchronized (VolatileTest.this) {
						++size;
					}
				}

			}

		};

		final int SIZE = 6;
		Thread[] ts = new Thread[SIZE];
		// ----------------------
		size = 0;
		long in = System.currentTimeMillis();
		for (int i = 0; i < SIZE; ++i) {
			(ts[i] = new Thread(r)).start();
		}

		for (int i = 0; i < SIZE; ++i) {
			try {
				ts[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		long t = System.currentTimeMillis() - in;
		System.out.println("Sync Time: " + t);
		assertEquals(1200000, size);

		final Lock lc = new ReentrantLock();
		Runnable s = new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 200000; ++i) {
					lc.lock();
					try {
						++size;
					} finally {
						lc.unlock();
					}
				}

			}

		};

		// ----------------------
		size = 0;
		in = System.currentTimeMillis();
		for (int i = 0; i < SIZE; ++i) {
			(ts[i] = new Thread(s)).start();
		}

		for (int i = 0; i < SIZE; ++i) {
			try {
				ts[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		t = System.currentTimeMillis() - in;
		System.out.println("Lock Time: " + t);
		assertEquals(1200000, size);
	}
}

class B {
	static {
		System.out.println("Linke Me static");
	}

	static void test() {}
}
