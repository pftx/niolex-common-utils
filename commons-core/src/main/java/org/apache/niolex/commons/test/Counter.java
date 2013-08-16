/**
 * Counter.java
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

import java.util.Collection;

import org.apache.niolex.commons.bean.Pair;

/**
 * This is used for count.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-7-12
 */
public class Counter {

	// The internal count.
	private volatile int cnt = 0;

	/**
	 * Increment the current count.
	 * This method is synchronized.
	 */
	public synchronized void inc() {
		++cnt;
	}

	/**
	 * Get the current count.
	 *
	 * @return the result
	 */
	public int cnt() {
		return cnt;
	}

	/**
	 * Set the current count.
	 *
	 * @param cnt the new count you want to set.
	 */
	public void set(int cnt) {
	    this.cnt = cnt;
	}

	/**
	 * Calculate the mean square error
	 *
	 * @param array the array of data input
	 * @return the pair of results, first the average, second the mean square error
	 */
	public static Pair<Integer, Double> calcMeanSquareError(int ...array) {
	    long total = 0;
	    for (int i : array) {
	        total += i;
	    }
	    int avg = (int) (total / array.length);
	    double msr = 0.0;
	    for (int i : array) {
	        msr += Math.pow(avg - i, 2);
	    }
	    return Pair.create(avg, Math.sqrt(msr / array.length));
	}

	/**
     * Calculate the mean square error
     *
     * @param array the array of data input
     * @return the pair of results, first the average, second the mean square error
     */
	public static Pair<Integer, Double> calcMeanSquareError(Collection<Integer> array) {
        long total = 0;
        for (int i : array) {
            total += i;
        }
        int avg = (int) (total / array.size());
        double msr = 0.0;
        for (int i : array) {
            msr += Math.pow(avg - i, 2);
        }
        return Pair.create(avg, Math.sqrt(msr / array.size()));
    }

}
