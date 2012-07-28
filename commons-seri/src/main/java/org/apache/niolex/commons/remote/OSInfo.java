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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.util.Properties;
import java.util.Scanner;

import org.apache.niolex.commons.codec.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.management.OperatingSystemMXBean;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-7-28
 */
@SuppressWarnings("restriction")
public class OSInfo implements Invokable {
	private static final Logger LOG = LoggerFactory.getLogger(OSInfo.class);
	private static final int CPUTIME = 30;

	private String osName;
	private String osArch;
	private String osVersion;
	private OperatingSystemMXBean osmxb;

	/**
	 * Get the operating system information. Constructor
	 */
	public OSInfo() {
		super();
		Properties props = System.getProperties(); // 获得系统属性集
		osName = props.getProperty("os.name"); // 操作系统名称
		osArch = props.getProperty("os.arch"); // 操作系统构架
		osVersion = props.getProperty("os.version"); // 操作系统版本
		osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
	}

	/**
	 * This is the override of super method.
	 *
	 * @see org.apache.niolex.commons.remote.Invokable#invoke(java.io.OutputStream, java.lang.String[])
	 */
	@Override
	public void invoke(OutputStream out, String[] args) throws IOException {
		final String endLine = ConnectionWorker.END_LINE;
		StringBuilder sb = new StringBuilder();
		sb.append("----------------").append(osName).append(' ').append(osArch);
		sb.append(' ').append(osVersion).append("----------------").append(endLine);

		sb.append("Memory Info:").append(endLine);
		final long gBsize = 1024 * 1024 * 1024;
		final long halfg = gBsize / 2;
		long totalMemorySize = (halfg + osmxb.getTotalPhysicalMemorySize()) / gBsize;// 总的物理内存
		long freePhysicalMemorySize = (halfg + osmxb.getFreePhysicalMemorySize()) / gBsize;// 剩余的物理内存
		long usedMemorySize = totalMemorySize - freePhysicalMemorySize;// 已使用的物理内存
		sb.append("    Total ").append(totalMemorySize).append("G, Used ").append(usedMemorySize);
		sb.append("G, Free ").append(freePhysicalMemorySize).append("G").append(endLine);

		sb.append(endLine).append("CPU Info:").append(endLine);
		if (osName.toLowerCase().startsWith("win")) {
			int retn = getCpuIdleForWindows();
			sb.append("    CPU Idle ").append(formatPercent(retn)).append(endLine);
		} else {
			// We just think it's Linux.
			sb.append("    ").append(getCpuRateForLinux()).append(endLine);
		}
		sb.append("    Load Average ").append(osmxb.getSystemLoadAverage()).append(endLine);

		sb.append(endLine).append("Disk Info:").append(endLine);
		File[] roots = File.listRoots();// 获取磁盘分区列表
		for (int i = 0; i < roots.length; i++) {
			File root = roots[i];
			long freeSpace = root.getFreeSpace() / gBsize;
			long totalSpace = root.getTotalSpace() / gBsize;
			long usableSpace = root.getUsableSpace() / gBsize;
			sb.append("    Info of [").append(root).append("]:").append(endLine);
			sb.append("        Free ").append(freeSpace).append("G, Total ").append(totalSpace);
			sb.append("G, Usable ").append(usableSpace).append("G").append(endLine);
		}
		// At the end.
		out.write(StringUtil.strToUtf8Byte(sb.toString()));
	}

	/**
	 * Format integer into percent format.
	 *
	 * @param k
	 * @return
	 */
	public String formatPercent(int k) {
		return k / 100 + "." + k % 100 + "%";
	}

	/**
	 * 获得CPU空闲率.
	 *
	 * @return 返回cpu空闲率
	 */
	public int getCpuIdleForWindows() {
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
			LOG.error("Failed to get windows CPU info.", e);
			return -1;
		}
	}

	/**
	 * 获取CPU使用时间分配情况
	 *
	 * @param proc
	 * @return
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
			LOG.error("Failed to get windows CPU info.", e);
		} finally {
			if (scan != null) {
				scan.close();
			}
		}
		return retn;
	}

	public String getCpuRateForLinux() {
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
				return scan.nextLine();
			} else {
				scan.nextLine();
				scan.nextLine();
				return scan.nextLine();
			}

		} catch (IOException ioe) {
			return "Error Occured.";
		} finally {
			if (scan != null) {
				scan.close();
			}
		}

	}

}
