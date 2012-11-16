/**
 * SystemInfoTest.java
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

import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-7-26
 */
@RunWith(OrderedRunner.class)
public class SystemInfoTest {
	SystemInfo info = SystemInfo.getInstance();
	String osName = System.getProperties().getProperty("os.name");

	/**
	 * Test method for {@link org.apache.niolex.commons.test.SystemInfo#refreshSystemInfo()}.
	 * @throws InterruptedException
	 */
	@Test
	public void testARefreshSystemInfo() throws InterruptedException {
		info.autoRefresh(10);
		info.refreshSystemInfo();
		int num = info.getTotalThreadCount();
		final CountDownLatch startSignal = new CountDownLatch(1);
		final CountDownLatch doneSignal = new CountDownLatch(1);

		new Thread() { public void run() {try {
			startSignal.countDown();
			doneSignal.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}}}.start();
		startSignal.await();
		Thread.sleep(20);
		info.refreshSystemInfo();
		int k = info.getTotalThreadCount();
		doneSignal.countDown();
		assertEquals(k, num + 1);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.SystemInfo#getHeapMem()}.
	 */
	@Test
	public void testGetHeapMem() {
		System.out.println(info.getHeapMem().getCommitted());
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.SystemInfo#getNonHeapMem()}.
	 */
	@Test
	public void testGetNonHeapMem() {
		System.out.println(info.getNonHeapMem().getCommitted());
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.SystemInfo#getCpuNumber()}.
	 */
	@Test
	public void testGetCpuNumber() {
		assertTrue(2 <= info.getCpuNumber());
		assertTrue(2 <= info.getGcList().size());
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.SystemInfo#getLoadAverage()}.
	 */
	@Test
	public void testGetLoadAverage() {
		if (osName.contains("Win"))
			assertEquals(-1.00, info.getLoadAverage(), 0.01);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.SystemInfo#getActiveThreadCount()}.
	 */
	@Test
	public void testGetActiveThreadCount() {
		assertTrue(info.getTotalThreadCount() >= info.getActiveThreadCount());
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.SystemInfo#getUsedRatio()}.
	 */
	@Test
	public void testGetUsedRatio() {
		assertTrue(info.getUsedRatio() > 1);
	}

}
