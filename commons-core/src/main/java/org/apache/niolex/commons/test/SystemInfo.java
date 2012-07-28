/**
 * SystemInfo.java
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

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;
import java.util.List;

import org.apache.niolex.commons.util.Runme;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-7-26
 */
public class SystemInfo {

	// Memory
	private MemoryUsage heapMem;
	private int usedRatio;
	private MemoryUsage nonHeapMem;
	// GC
	private List<GarbageCollectorMXBean> gcList;
	// CPU
	private int cpuNumber;
	private double loadAverage;
	// Threads
	private int totalThreadCount;
	private int activeThreadCount;

	private static final SystemInfo INSTANCE = new SystemInfo();
	private static ThreadGroup TOP_GROUP;

	static {
		ThreadGroup group = Thread.currentThread().getThreadGroup();

		// 遍历线程组树，获取根线程组
		while (group != null) {
			TOP_GROUP = group;
			group = group.getParent();
		}
	}

	public static SystemInfo getInstance() {
		return INSTANCE;
	}

	/**
	 * The private constructor.
	 */
	private SystemInfo() {
		super();
		new Runme() {
			@Override
			public void runMe() {
				refreshSystemInfo();
			}}.start();
	}



	public void refreshSystemInfo() {
		MemoryMXBean m = ManagementFactory.getMemoryMXBean();
		heapMem = m.getHeapMemoryUsage();
		usedRatio = (int) ((heapMem.getUsed()) * 100 / heapMem.getCommitted());
		nonHeapMem = m.getNonHeapMemoryUsage();

		gcList = ManagementFactory.getGarbageCollectorMXBeans();

		OperatingSystemMXBean o = ManagementFactory.getOperatingSystemMXBean();
		cpuNumber = o.getAvailableProcessors();
		loadAverage = o.getSystemLoadAverage();

		ThreadMXBean t = ManagementFactory.getThreadMXBean();
		totalThreadCount = t.getThreadCount();
		activeThreadCount = TOP_GROUP.activeCount();
	}

	public MemoryUsage getHeapMem() {
		return heapMem;
	}

	public MemoryUsage getNonHeapMem() {
		return nonHeapMem;
	}

	public int getCpuNumber() {
		return cpuNumber;
	}

	public List<GarbageCollectorMXBean> getGcList() {
		return gcList;
	}

	public double getLoadAverage() {
		return loadAverage;
	}

	public int getTotalThreadCount() {
		return totalThreadCount;
	}

	public int getActiveThreadCount() {
		return activeThreadCount;
	}

	public int getUsedRatio() {
		return usedRatio;
	}

}
