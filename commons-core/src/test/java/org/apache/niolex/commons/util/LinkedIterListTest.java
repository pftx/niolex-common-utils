/**
 * LinkedIterListTest.java
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
package org.apache.niolex.commons.util;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.niolex.commons.util.LinkedIterList.Iter;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-6-1
 */
public class LinkedIterListTest {

	/**
	 * Test method for {@link org.apache.niolex.commons.util.LinkedIterList#add(java.lang.Object)}.
	 */
	@Test
	public void testAdd() {
		LinkedIterList<String> a = new LinkedIterList<String>();
		Iter<String> it = a.iterator();
		a.add("NIce");
		a.add("to");
		a.add("meet");
		a.add("Not yet implemented");
		assertEquals("NIce", it.next());
		assertEquals("to", it.next());
		assertEquals("meet", it.next());
		assertEquals("Not yet implemented", it.next());
		assertEquals(null, it.next());
		a.offer("You");
		assertEquals("You", it.next());
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.util.LinkedIterList#offer(java.lang.Object)}.
	 */
	@Test
	public void testOffer() {
		LinkedIterList<Integer> inn = new LinkedIterList<Integer>();
		Iter<Integer> it = inn.iterator();
		inn.add(4);
		inn.add(6);
		assertEquals(it.next().intValue(), 4);
		assertEquals(it.next().intValue(), 6);
		assertEquals(it.next(), null);
		inn.add(8);
		assertEquals(it.next().intValue(), 8);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.util.LinkedIterList#poll()}.
	 * @throws Exception
	 */
	@Test
	public void testPoll() throws Exception {
		final LinkedIterList<Integer> inn = new LinkedIterList<Integer>();
		Runnable rn = new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 12392; ++i) {
					inn.add(i);
					if (i % 2000 == 0) {
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
						}
					}
				}

			}};
		Thread t = new Thread(rn);
		Thread q = new Thread(rn);
		Thread r = new Thread(rn);
		Thread s = new Thread(rn);
		Thread v = new Thread(rn);
		t.start();
		q.start();
		r.start();
		s.start();
		v.start();
		t.join();
		q.join();
		r.join();
		s.join();
		v.join();
		int[] ree = new int[12392];
		Iter<Integer> it = inn.iterator();
		int k = 0;
		while (it.hasNext()) {
			++ree[it.next()];
			++k;
		}
		for (int i = 0; i < 12392; ++i) {
			assertEquals(ree[i], 5);
		}
		assertEquals(61960, k);

	}
	AtomicInteger zkz = new AtomicInteger(0);

	/**
	 * Test method for {@link org.apache.niolex.commons.util.LinkedIterList#iterator()}.
	 */
	@Test
	public void testIterator() throws Exception {
		final LinkedIterList<Integer> inn = new LinkedIterList<Integer>();
		for (int i = 0; i < 12392; ++i) {
			inn.add(i);
			inn.add(i);
			inn.add(i);
			inn.add(i);
			inn.add(i);
		}
		final int[] ree = new int[12392];
		Runnable rn = new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 12393; ++i) {
					Integer k = inn.poll();
					if (k == null)
						return;
					synchronized(ree) {
						++ree[k];
					}
					zkz.incrementAndGet();
					if (i % 2000 == 0) {
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
						}
					}
				}
			}};
		System.out.println(">> " + inn.getSize());
		Thread t = new Thread(rn);
		Thread q = new Thread(rn);
		Thread r = new Thread(rn);
		Thread s = new Thread(rn);
		Thread v = new Thread(rn);
		t.start();
		q.start();
		r.start();
		s.start();
		v.start();
		t.join();
		q.join();
		r.join();
		s.join();
		v.join();
		System.out.println(">> " + inn.getSize());
		System.out.println(">> " + zkz);
		assertEquals(61960, zkz.intValue());
		for (int i = 0; i < 12392; ++i) {
			assertEquals(ree[i], 5);
		}
		Iter<Integer> it = inn.iterator();
		assertFalse(it.hasNext());
		assertFalse(it.hasNext());
		assertEquals(null, it.next());
		inn.add(9123);
		assertTrue(it.hasNext());
		assertEquals(9123, it.next().intValue());
		assertEquals(null, it.next());
		inn.poll();
		assertEquals(0, inn.getSize());
		assertFalse(it.hasNext());
		inn.add(231423);
		assertTrue(it.hasNext());
		assertEquals(231423, it.next().intValue());
	}

}
