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
import java.util.ArrayList;
import java.util.List;

import org.apache.niolex.commons.codec.IntegerUtil;
import org.apache.niolex.commons.concurrent.ThreadUtil;
import org.apache.niolex.commons.util.Runme;

/**
 * Store system info in this class.
 * User can all {@link #refreshSystemInfo()} to refresh the internal statistics
 * manually, or call {@link #autoRefresh(int)} to refresh automatically at
 * specified interval.<br>
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-7-26
 */
public class SystemInfo {

    // The top level thread group.
    private static final ThreadGroup TOP_GROUP = ThreadUtil.topGroup();

    private static final int[] COL_LEN = new int[] {20, 20, 11};

    private static final String[] TITLES = new String[] {"Category", "Attribute", "Value"};

    // The singleton instance.
    private static final SystemInfo INSTANCE = new SystemInfo();

	// Memory
	private MemoryUsage heapMem;
	private MemoryUsage nonHeapMem;
	private int usedRatio;

	// CPU
	private int cpuNumber;
	private double loadAverage;

	// GC
	private List<GarbageCollectorMXBean> gcList;

	// Threads
	private int totalThreadCount;
	private int activeThreadCount;

	// Internal use, for auto refresh.
	private Runme runme;

	/**
	 * Get the global singleton instance, we will refresh system info before return.
	 *
	 * @return the instance
	 */
	public static final SystemInfo getInstance() {
	    INSTANCE.refreshSystemInfo();
		return INSTANCE;
	}

	/**
	 * The private constructor, prevent others from creation.
	 */
	private SystemInfo() {
	}

	/**
	 * Set automatically refresh system info at the specified time interval.
	 *
	 * @param refreshInterval in milliseconds
	 */
	public synchronized void autoRefresh(int refreshInterval) {
	    if (runme == null) {
	        runme = new Runme() {
    			@Override
    			public void runMe() {
    				refreshSystemInfo();
    			}
    		};
    		runme.setSleepInterval(refreshInterval);
    		runme.start();
	    } else {
	        runme.setSleepInterval(refreshInterval);
	    }
	}

	/**
	 * Stop auto refresh the system info.
	 */
	public synchronized void stopRefresh() {
	    if (runme != null) {
	        runme.stopMe();
	        runme = null;
	    }
	}

	/**
	 * Refresh the system info.
	 */
	public void refreshSystemInfo() {
	    gcList = ManagementFactory.getGarbageCollectorMXBeans();
		MemoryMXBean m = ManagementFactory.getMemoryMXBean();
		heapMem = m.getHeapMemoryUsage();
		nonHeapMem = m.getNonHeapMemoryUsage();
		usedRatio = (int) ((heapMem.getUsed()) * 100 / heapMem.getCommitted());

		OperatingSystemMXBean o = ManagementFactory.getOperatingSystemMXBean();
		cpuNumber = o.getAvailableProcessors();
		loadAverage = o.getSystemLoadAverage();

		ThreadMXBean t = ManagementFactory.getThreadMXBean();
		totalThreadCount = t.getThreadCount();
		activeThreadCount = TOP_GROUP.activeCount();
	}

	/**
	 * Format all the system info and generate a table as string.
	 *
	 * @return the generated table as string
	 */
	public String generateSystemInfo() {
        ArrayList<Object> list = new ArrayList<Object>();
        list.add("Heap Memory");
        list.add("Used");
        list.add(IntegerUtil.formatSize(heapMem.getUsed()));
        // ----
        list.add("Heap Memory");
        list.add("Committed");
        list.add(IntegerUtil.formatSize(heapMem.getCommitted()));
        // ----
        list.add("Heap Memory");
        list.add("Used Ratio");
        list.add(usedRatio + "%");
        // ----
        list.add("nonHeap Memory");
        list.add("Used");
        list.add(IntegerUtil.formatSize(nonHeapMem.getUsed()));
        // ----
        list.add("nonHeap Memory");
        list.add("Committed");
        list.add(IntegerUtil.formatSize(nonHeapMem.getCommitted()));
        // ----
        list.add("CPU");
        list.add("Number");
        list.add(cpuNumber);
        // ----
        list.add("CPU");
        list.add("Load Average");
        list.add(loadAverage);
        // ----
        for (GarbageCollectorMXBean bean : gcList) {
            list.add("GC");
            list.add("Name");
            list.add(bean.getName());
            // ----
            list.add("GC");
            list.add("Collection Count");
            list.add(bean.getCollectionCount());
            // ----
            list.add("GC");
            list.add("Collection Time");
            list.add(bean.getCollectionTime());
        }
        // ----
        list.add("Threads");
        list.add("Total Count");
        list.add(totalThreadCount);
        // ----
        list.add("Threads");
        list.add("Active Count");
        list.add(activeThreadCount);
        // ----

        return TidyUtil.generateTable(COL_LEN, TITLES, list.toArray());
    }

	public MemoryUsage getHeapMem() {
		return heapMem;
	}

	public MemoryUsage getNonHeapMem() {
		return nonHeapMem;
	}

	public int getUsedRatio() {
	    return usedRatio;
	}

	public int getCpuNumber() {
		return cpuNumber;
	}

	public double getLoadAverage() {
	    return loadAverage;
	}

	public List<GarbageCollectorMXBean> getGcList() {
		return gcList;
	}

	public int getTotalThreadCount() {
		return totalThreadCount;
	}

	public int getActiveThreadCount() {
		return activeThreadCount;
	}

}
