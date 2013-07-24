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

import org.apache.niolex.commons.codec.IntegerUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-7-26
 */
public class SystemInfoTest {
	static SystemInfo info = SystemInfo.getInstance();
	String osName = System.getProperties().getProperty("os.name");

	@BeforeClass
	public static void testSystemInfo() {
	    info.autoRefresh(10);
	}

	@AfterClass
	public static void testStopRefresh() {
	    info.stopRefresh();
	}

    /**
     * Test method for {@link org.apache.niolex.commons.test.SystemInfo#refreshSystemInfo()}.
     * @throws InterruptedException
     */
    @Test
    public void testAutoRefresh() throws InterruptedException {
        info.autoRefresh(5);
    }

	/**
	 * Test method for {@link org.apache.niolex.commons.test.SystemInfo#refreshSystemInfo()}.
	 * @throws InterruptedException
	 */
	@Test
	public void testRefreshSystemInfo() throws InterruptedException {
	    info.refreshSystemInfo();
	}

	@Test
    public void testGetTotalThreadCount() {
	    int num = info.getTotalThreadCount();
	    System.out.println("TotalThreads " + num + ", Active " + info.getActiveThreadCount());
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.SystemInfo#getHeapMem()}.
	 */
	@Test
	public void testGetHeapMem() {
		System.out.println("Heap " + IntegerUtil.formatSize(info.getHeapMem().getCommitted()));
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.SystemInfo#getNonHeapMem()}.
	 */
	@Test
	public void testGetNonHeapMem() {
		System.out.println("NonHeap " + IntegerUtil.formatSize(info.getNonHeapMem().getCommitted()));
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
		else
		    info.getLoadAverage();
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
	    System.out.println("UsedMem " + info.getUsedRatio() + "%");
		assertTrue(info.getUsedRatio() > 1);
	}

    @Test
    public void testGetInstance() {
        info.stopRefresh();
    }

}
