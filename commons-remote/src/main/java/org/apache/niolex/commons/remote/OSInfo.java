/**
 * OSInfo.java
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
package org.apache.niolex.commons.remote;

import static org.apache.niolex.commons.codec.IntegerUtil.formatSize;
import static org.apache.niolex.commons.remote.ConnectionWorker.endl;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.test.SystemInfo;
import org.apache.niolex.commons.util.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Print the OS information into the command console for anyone who need it.
 * User application need to add an instance of this class into BeanServer
 * in order to have this functionality.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-7-28
 */
public class OSInfo implements Invokable {
	private static final Logger LOG = LoggerFactory.getLogger(OSInfo.class);
	private static final int CPUTIME = 30;

	private SystemInfo systemInfo = SystemInfo.getInstance();
	private String osName;
	private String osArch;
	private String osVersion;

	/**
	 * Get the operating system information. Constructor
	 */
	public OSInfo() {
		super();
		Properties props = System.getProperties(); // 获得系统属性集
		osName = props.getProperty("os.name"); // 操作系统名称
		osArch = props.getProperty("os.arch"); // 操作系统构架
		osVersion = props.getProperty("os.version"); // 操作系统版本
	}

	/**
	 * This is the override of super method.
	 *
	 * @see org.apache.niolex.commons.remote.Invokable#invoke(java.io.OutputStream, java.lang.String[])
	 */
	@Override
	public void invoke(OutputStream out, String[] args) throws IOException {
		final String endLine = endl();
		StringBuilder sb = new StringBuilder();
		sb.append("================").append(osName).append(' ').append(osArch);
		sb.append(" Version ").append(osVersion).append("================").append(endLine);
		systemInfo.refreshSystemInfo();

		// Cat 1. Memory.
		sb.append("1. Memory Info:").append(endLine);

		// Next is Linux memory info and disk info, windows if ignored.
		String[] cmsArr = null;
		if (osName.toLowerCase().startsWith("win")) {
		    //
		} else {
		    cmsArr = getLinuxCpuAndMemInfo();
		}
		if (cmsArr != null) {
		    sb.append("    ").append(cmsArr[1]).append(endLine);
		    sb.append("    ").append(cmsArr[2]).append(endLine);
		}
		MemoryUsage heapMem = systemInfo.getHeapMem();
		long freeMem = heapMem.getCommitted() - heapMem.getUsed();

		sb.append(String.format("    JVM %-8s Total %7sB, Used %7sB, Free %7sB", "Heap",
		        formatSize(heapMem.getCommitted()), formatSize(heapMem.getUsed()), formatSize(freeMem)));
		sb.append(endLine);

		MemoryUsage nonHeap = systemInfo.getNonHeapMem();
		freeMem = nonHeap.getCommitted() - nonHeap.getUsed();
		sb.append(String.format("    JVM %-8s Total %7sB, Used %7sB, Free %7sB", "Non-Heap",
		        formatSize(nonHeap.getCommitted()), formatSize(nonHeap.getUsed()), formatSize(freeMem)));
		sb.append(endLine);

		// Cat 2. GC.
		sb.append(endLine).append("2. GC Info:").append(endLine);
		List<GarbageCollectorMXBean> gcList = ManagementFactory.getGarbageCollectorMXBeans();
		for (GarbageCollectorMXBean bean : gcList) {
		    sb.append(String.format("    %-20s Collection Count %4d, Time %6dms", bean.getName() + " GC",
		            bean.getCollectionCount(), bean.getCollectionTime()));
		    sb.append(endLine);
		}

		// Cat 3. CPU.
		sb.append(endLine).append("3. CPU Info:").append(endLine);
		if (osName.toLowerCase().startsWith("win")) {
			int retn = getWindowsCpuIdle();
			sb.append("    CPU Number ").append(systemInfo.getCpuNumber());
			sb.append(" Idle ").append(formatPercent(retn)).append(endLine);
		} else if (cmsArr != null) {
			// We just think it's Linux.
			sb.append("    ").append(cmsArr[0]).append(endLine);
			sb.append("    Load Average ").append(systemInfo.getLoadAverage()).append(endLine);
		}

		// Cat 4. Disk.
		sb.append(endLine).append("4. Disk Info:").append(endLine);
		final long gSize = Const.G;
		if (osName.toLowerCase().startsWith("win")) {
			File[] roots = File.listRoots();// 获取磁盘分区列表
			for (int i = 0; i < roots.length; i++) {
				File root = roots[i];
				long free = root.getFreeSpace() / gSize;
				long total = root.getTotalSpace() / gSize;
				long usable = root.getUsableSpace() / gSize;
				long usePer = total == 0 ? 0 : (total - free) * 100 / total;
				sb.append("    [").append(root).append("]:");
				sb.append(String.format(" Total %5dG, Usable %5dG, Used %2d%%",
				        total, usable, usePer));
				sb.append(endLine);
			}
		} else {
			List<String> list = getLinuxDiskInfo();
			for (String s : list) {
				sb.append("    ").append(s).append(endLine);
			}
		}

		// Cat 5. Threads.
		sb.append(endLine).append("5. Threads Info:").append(endLine);
		sb.append("    Total Threads ").append(systemInfo.getTotalThreadCount());
		sb.append(", Active ").append(systemInfo.getActiveThreadCount()).append(endLine);

		// At the end.
		out.write(StringUtil.strToUtf8Byte(sb.toString()));
	}

	/**
	 * Format integer into percent format.
	 *
	 * @param k the integer to be formatted
	 * @return the percent in string format
	 */
	public String formatPercent(int k) {
		return k / 100 + "." + k % 100 + "%";
	}

	/**
	 * Get the windows CPU idle time.
	 *
	 * @return the percent of CPI idle time
	 */
	public int getWindowsCpuIdle() {
		try {
			String procCmd = "wmic.exe process get Caption, KernelModeTime, UserModeTime";
			// 取进程信息
			long[] c0 = getWindowsCPUTime(Runtime.getRuntime().exec(procCmd));
			Thread.sleep(CPUTIME);
			long[] c1 = getWindowsCPUTime(Runtime.getRuntime().exec(procCmd));
			long total = c1[0] - c0[0];
			long idle = c1[1] - c0[1];
			return (int) (idle * 10000 / total);
		} catch (Exception e) {
			LOG.error("Failed to get windows CPU idle.", e);
			return -1;
		}
	}

	/**
	 * Get windows CPU time.
	 *
	 * @param proc the process
	 * @return CPU status
	 */
	public long[] getWindowsCPUTime(final Process proc) {
		long[] retn = new long[2];
		Scanner scan = null;
		try {
			proc.getOutputStream().close();
			scan = new Scanner(proc.getInputStream());
			String line = scan.nextLine();
			int nameIdx = line.indexOf("Caption");
			int kmtIdx = line.indexOf("KernelModeTime");
			int umtIdx = line.indexOf("UserModeTime");
			long idleTime = 0;
			long totalTime = 0;
			while (scan.hasNextLine()) {
				line = scan.nextLine();
				if (line.isEmpty()) {
					continue;
				}
				String name = line.substring(nameIdx, kmtIdx - 1).trim();
				if (name.equalsIgnoreCase("WMIC.exe")) {
					continue;
				}
				long curTime = Long.parseLong(line.substring(kmtIdx, umtIdx - 1).trim())
						+ Long.parseLong(line.substring(umtIdx).trim());
				if (name.equalsIgnoreCase("System Idle Process") || name.equalsIgnoreCase("System")) {
					idleTime += curTime;
				}
				totalTime += curTime;
			}
			retn[0] = totalTime;
			retn[1] = idleTime;
		} catch (Exception e) {
			LOG.error("Failed to get windows CPU time.", e);
		} finally {
			if (scan != null) {
				scan.close();
			}
		}
		return retn;
	}

	/**
	 * Get Linux CPU Info, Memory Info, and Swap Info all at the same time.
	 *
	 * @return the information from top result
	 */
	public String[] getLinuxCpuAndMemInfo() {
		Scanner scan = null;
		try {
			Process process = Runtime.getRuntime().exec("top -b -n 1");
			process.getOutputStream().close();
			scan = new Scanner(process.getInputStream());

			if (osVersion.equals("2.4")) {
				scan.nextLine();
				scan.nextLine();
				scan.nextLine();
				scan.nextLine();
				return new String[] {scan.nextLine(), scan.nextLine(), scan.nextLine()};
			} else {
				scan.nextLine();
				scan.nextLine();
				return new String[] {scan.nextLine(), scan.nextLine(), scan.nextLine()};
			}

		} catch (IOException ioe) {
			return null;
		} finally {
			if (scan != null) {
				scan.close();
			}
		}

	}

	/**
	 * Get the Linux disk free information.
	 *
	 * @return the result from "df -h"
	 */
	public List<String> getLinuxDiskInfo() {
		Scanner scan = null;
		try {
			Process process = Runtime.getRuntime().exec("df -h");
			process.getOutputStream().close();
			scan = new Scanner(process.getInputStream());

			List<String> list = new ArrayList<String>();
			while (scan.hasNext()) {
				list.add(scan.nextLine());
			}
			return list;
		} catch (IOException ioe) {
			return null;
		} finally {
			if (scan != null) {
				scan.close();
			}
		}

	}

	public SystemInfo getSystemInfo() {
		return systemInfo;
	}

	public String getOsName() {
		return osName;
	}

	public String getOsArch() {
		return osArch;
	}

	public String getOsVersion() {
		return osVersion;
	}

}