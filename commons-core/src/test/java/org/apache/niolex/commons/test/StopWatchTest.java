/**
 * StopWatchTest.java
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
package org.apache.niolex.commons.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.niolex.commons.test.StopWatch.Stop;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-8-14
 */
@RunWith(OrderedRunner.class)
public class StopWatchTest {

	static StopWatch sw = new StopWatch(1);

	/**
	 * Test method for {@link org.apache.niolex.commons.test.StopWatch#begin()}.
	 * @throws InterruptedException
	 */
	@Test
	public void testBegin() throws InterruptedException {
		try {
		sw.done();
		} catch (Exception e) {}
		sw.begin(true);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.StopWatch#start()}.
	 * @throws InterruptedException
	 */
	@Test
	public void testCStart() throws InterruptedException {
		Stop s = sw.start();
		Stop s1 = sw.start();
		Thread.sleep(10);
		s.stop();
		s1.stop();
		s = sw.start();
		Thread.sleep(20);
		s.stop();
		s = sw.start();
		Thread.sleep(1500);
		s.stop();
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.StopWatch#done()}.
	 */
	@Test
	public void testDone() {
		System.out.println(System.currentTimeMillis());
		sw.done();
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.StopWatch#print()}.
	 */
	@Test
	public void testEPrint() {
		sw.print();
	}

	@Test
	public void testFBegin() throws InterruptedException {
		sw.begin(false);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.StopWatch#start()}.
	 * @throws InterruptedException
	 */
	@Test
	public void testFCStart() throws InterruptedException {
		Stop s = sw.start();
		Stop s1 = sw.start();
		Thread.sleep(10);
		s.stop();
		s1.stop();
		s = sw.start();
		Thread.sleep(20);
		s.stop();
		s = sw.start();
		Thread.sleep(1500);
		s.stop();
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.StopWatch#done()}.
	 */
	@Test
	public void testFDone() {
		System.out.println(System.currentTimeMillis());
		sw.done();
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.StopWatch#print()}.
	 */
	@Test
	public void testFEPrint() {
		sw.print();
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.StopWatch#getDistributions()}.
	 */
	@Test
	public void testGetDistributions() {
		int[] ds = sw.getDistributions();
		int r = 0;
		for (int i = 0; i < ds.length; ++i) {
			r += ds[i];
		}
		assertEquals(r, 4);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.StopWatch#getAvg()}.
	 */
	@Test
	public void testGetAvg() {
		assertTrue(sw.getAvg() < 550);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.StopWatch#getMax()}.
	 */
	@Test
	public void testGetMax() {
		assertTrue(sw.getMax() < 1700);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.StopWatch#getMin()}.
	 */
	@Test
	public void testGetMin() {
		assertTrue(sw.getMin() >= 0);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.StopWatch#getRps()}.
	 */
	@Test
	public void testGetRps() {
		assertTrue(sw.getRps() < 100);
		assertTrue(sw.getRpsList().size() < 3);
	}

}
