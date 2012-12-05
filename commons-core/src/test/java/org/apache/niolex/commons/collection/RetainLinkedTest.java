/**
 * RetainLinkedTest.java
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
package org.apache.niolex.commons.collection;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.niolex.commons.collection.RetainLinkedList;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-6-19
 */
public class RetainLinkedTest {

	private int runSize = 100000;
	private RetainLinkedList<Integer> linked = new RetainLinkedList<Integer>(5);
	private Map<Integer, Integer> check = new ConcurrentHashMap<Integer, Integer>();

	private class LetsRun implements Runnable {

		/**
		 * Override super method
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			int k = (int)Thread.currentThread().getId() * runSize;
			System.out.println(k + " start at " + System.currentTimeMillis());
			for (int i = 0; i < runSize; ++i) {
				linked.add(k + i);
			}
			System.out.println(k + " stop at " + System.currentTimeMillis());
		}

	}

	private class Take implements Runnable {

		/**
		 * Override super method
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			int k = (int)Thread.currentThread().getId();
			System.out.println(k + " Take start at " + System.currentTimeMillis());
			while (true) {
				Integer r = linked.handleNext();
				if (r == null) {
					break;
				}
				if (check.containsKey(r)) {
					System.out.println(k + " --------------------- Take duplicate at " + r);
					break;
				} else {
					check.put(r, r);
				}
			}
			System.out.println(k + " Take stop at " + System.currentTimeMillis());
		}

	}

	@Test
	public void test() throws InterruptedException {
		Thread[] ts = new Thread[20];
		int i = 0;
		for (i = 0; i < 10; ++i) {
			Thread r = new Thread(new LetsRun());
			ts[i] = r;
		}
		for (i = 10; i < 20; ++i) {
			Thread r = new Thread(new Take());
			ts[i] = r;
		}
		long in = System.currentTimeMillis();
		for (i = 0; i < 20; ++i) {
			ts[i].start();
		}
		for (i = 0; i < 20; ++i) {
			ts[i].join();
		}
		long o = System.currentTimeMillis() - in;
		System.out.println(check.size() + " total time at " + o);
		assertEquals(1000000, check.size());
	}
}
