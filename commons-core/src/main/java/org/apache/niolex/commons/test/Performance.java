/**
 * Performance.java
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

/**
 * This class is for test code performance.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-7-23
 */
public abstract class Performance {

	private final int innerIteration;
	private final int outerIteration;
	private long max;
	private long min;
	private long total, in, cu;

	/**
	 * Create a Performance class.
	 *
	 * @param innerIteration The iteration to run as a measure unit.
	 * @param outerIteration The iteration to run as a total iteration.
	 */
	public Performance(int innerIteration, int outerIteration) {
		super();
		this.innerIteration = innerIteration;
		this.outerIteration = outerIteration;
	}

	protected abstract void run();

	/**
	 * Start to test now.
	 */
	public void start() {
		/**
		 * This is for preheat the JVM.
		 */
		for (int j = 0; j < innerIteration; ++j) {
			run();
		}

		/**
		 * The real thing begin now.
		 */
		max = Integer.MIN_VALUE;
		min = Integer.MAX_VALUE;
		total = 0;
		long ein = System.currentTimeMillis();
		for (int i = 0; i < outerIteration; ++i) {
			in = System.currentTimeMillis();
			for (int j = 0; j < innerIteration; ++j) {
				run();
			}
			cu = System.currentTimeMillis() - in;
			if (cu > max) max = cu;
			if (cu < min) min = cu;
			total += cu;
		}
		cu = total / outerIteration;
		System.out.println("Performance Done, Total Time - " + (System.currentTimeMillis() - ein));
		System.out.println("Iter " + outerIteration + ", Avg " + cu + ", Max "
				+ max + ", Min " + min);
	}
}
