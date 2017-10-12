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

import org.apache.niolex.commons.util.SystemUtil;

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

	/**
	 * Create a new Performance instance.
	 *
	 * @param innerIteration the iteration to run as a measure unit
	 * @param outerIteration the numbers of iteration to run the measure unit
	 */
	public Performance(int innerIteration, int outerIteration) {
		super();
		this.innerIteration = innerIteration;
		this.outerIteration = outerIteration;
	}

	/**
	 * Implements this method to do your work.
	 */
	protected abstract void run();

	/**
     * This is run one batch.
     */
    protected void oneRun() {
        for (int j = 0; j < innerIteration; ++j) {
            run();
        }
    }

	/**
	 * Start to test now.
	 */
	public void start() {
		/**
		 * This is for preheat the JVM.
		 */
	    oneRun();

		long total, in, cu;
		/**
		 * The real thing begin now.
		 */
		max = Integer.MIN_VALUE;
		min = Integer.MAX_VALUE;
		total = 0;
		long ein = System.currentTimeMillis();
		for (int i = 0; i < outerIteration; ++i) {
			in = System.currentTimeMillis();
			oneRun();
			cu = System.currentTimeMillis() - in;
			if (cu > max) max = cu;
			if (cu < min) min = cu;
			total += cu;
		}
		// Done.
		cu = total / outerIteration;
		System.out.println("PERF done, total time " + (System.currentTimeMillis() - ein) + "ms.");
        SystemUtil.printTable(new int[] {10, 10, 10, 10},
                new String[] {"ITERATIONS", "AVG", "MAX", "MIN"}, outerIteration, cu, max, min);
	}

}
