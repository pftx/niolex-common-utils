/**
 * StopWatch.java
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

import java.text.NumberFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.niolex.commons.util.Runme;

/**
 * This class is calculating average time, max time, min time, time distribution.
 * Often needed for pressure test.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-8-14
 */
public class StopWatch {

	private final int distributionInterval;
	private final ConcurrentLinkedQueue<Integer> linkList = new ConcurrentLinkedQueue<Integer>();

	// These three variables are for rps calculation.
	private final LinkedList<Long> rpsList = new LinkedList<Long>();
	private final AtomicLong counter = new AtomicLong(0);
	private Runme rumme;

	private long startTime;
	private int[] distributions;
	private int avg;
	private int max;
	private int min;
	private int rps;

	/**
	 * Construct a new Stop Watch.
	 * @param distributionInterval The interval to calculate distribution.
	 */
	public StopWatch(int distributionInterval) {
		super();
		this.distributionInterval = distributionInterval;
	}

	/**
	 * Begin the time calculation.
	 *
	 * @param printDirectly Whether to print rps directly into console.
	 */
	public void begin(final boolean printDirectly) {
		startTime = System.currentTimeMillis();
		counter.getAndSet(0);

		rumme = new Runme() {

			@Override
			public void runMe() {
				Long l = counter.getAndSet(0);
				if (printDirectly) {
					System.out.println("rps -> " + l);
				} else {
					rpsList.add(l);
				}
			}

		};

		rumme.setInitialSleep(true);
		rumme.start();
	}

	/**
	 * Start a new time counter to count the current unit running time.
	 * This method can be called in parallel.
	 *
	 * @return a Stop object.
	 */
	public Stop start() {
		return new Stop();
	}

	/**
	 * Mark this Stop Watch as done and calculate all the statistics.
	 */
	public void done() {
		if (rumme != null) {
			rumme.stopMe();
		}
		final int distNum = 1000 / distributionInterval + 1;
		final long wholeTime = System.currentTimeMillis() - startTime;
		long totalCnt = 0, totalTime = 0;
		distributions = new int[distNum];
		max = 0;
		min = Integer.MAX_VALUE;
		Integer c;
		while ((c = linkList.poll()) != null) {
			int i = c.intValue();
			++totalCnt;
			totalTime += i;
			if (i > max) max = i;
			if (i < min) min = i;
			int k = i / distributionInterval;
			if (k >= distNum) k = distNum - 1;
			++distributions[k];
		}
		avg = (int) (totalTime / totalCnt);
		rps = (int) (totalCnt * 1000 / wholeTime);
	}

	/**
	 * Print all the statistics into console.
	 */
	public void print() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n\n---------------------------------------\n");
		sb.append("MAX\tMIN\tAVG\tRPS\n");
		sb.append(max).append('\t').append(min).append('\t').append(avg).append('\t');
		sb.append(rps).append('\n');
		sb.append("---------------------------------------\n");
		int l = 0, r = distributionInterval;
		NumberFormat nf = NumberFormat.getIntegerInstance();
		NumberFormat rf = NumberFormat.getIntegerInstance();
		nf.setMinimumIntegerDigits(3);
		rf.setGroupingUsed(true);
		rf.setMinimumIntegerDigits(6);
		for (int i = 0; i < distributions.length; ++i) {
			if (distributions[i] > 0) {
				sb.append(nf.format(l)).append(" to ");
				if (i == distributions.length - 1) {
					sb.append('~');
				} else {
					sb.append(nf.format(r));
				}
				sb.append("\t").append(rf.format(distributions[i])).append('\n');
			}
			l = r;
			r += distributionInterval;
		}
		sb.append("---------------------------------------\n");
		Iterator<Long> it = rpsList.iterator();
		while (it.hasNext()) {
			sb.append(rf.format(it.next())).append('\n');
		}
		sb.append("----------------END--------------------\n");
		System.out.print(sb);
	}

	//------------------------------------------------------------------
	// Getters & Setters
	//------------------------------------------------------------------

	public int[] getDistributions() {
		return distributions;
	}

	public int getAvg() {
		return avg;
	}

	public int getMax() {
		return max;
	}

	public int getMin() {
		return min;
	}

	public int getRps() {
		return rps;
	}

	public LinkedList<Long> getRpsList() {
		return rpsList;
	}

	//------------------------------------------------------------------
	// Other Parts
	//------------------------------------------------------------------

	/**
	 * An internal class to mark time. Just use the stop() to stop time.
	 *
	 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
	 * @version 1.0.0
	 * @since 2012-8-14
	 */
	public class Stop {
		private long startTime;

		/**
		 * Create a stop class with current time as the start Time.
		 */
		private Stop() {
			super();
			this.startTime = System.currentTimeMillis();
		}

		/**
		 * Stop this time marker, and save the result into the mother Stop Watch of this object.
		 */
		public void stop() {
			long time = System.currentTimeMillis() - startTime;
			linkList.add(new Integer((int) time));
			counter.incrementAndGet();
		}
	}
}
