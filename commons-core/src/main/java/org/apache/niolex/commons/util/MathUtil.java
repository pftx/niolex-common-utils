/**
 * MathUtil.java
 *
 * Copyright 2014 the original author or authors.
 *
 * We licenses this file to you under the Apache License, version 2.0
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
package org.apache.niolex.commons.util;

import java.util.Collection;

import org.apache.niolex.commons.bean.Pair;
import org.apache.niolex.commons.test.Check;
import org.apache.niolex.commons.test.MockUtil;

/**
 * Do all the math work here!
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-7-2
 */
public class MathUtil extends MockUtil {

    /**
     * Calculate the standard deviation of the input array.
     *
     * @param array the input array of data
     * @return the pair of results, first the average, second the standard deviation
     */
    public static Pair<Integer, Double> calcStandardDeviation(int ...array) {
        Check.lt(0, array.length, "the input array must contain as least one element.");
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
     * Calculate the standard deviation of the specified collection.
     *
     * @param coll the input collection of data
     * @return the pair of results, first the average, second the standard deviation
     */
    public static Pair<Integer, Double> calcStandardDeviation(Collection<Integer> coll) {
        Check.lt(0, coll.size(), "the input collection must contain as least one element.");
        long total = 0;
        for (int i : coll) {
            total += i;
        }
        int avg = (int) (total / coll.size());
        double msr = 0.0;
        for (int i : coll) {
            msr += Math.pow(avg - i, 2);
        }
        return Pair.create(avg, Math.sqrt(msr / coll.size()));
    }

    /**
     * Find the maximum value among the three parameters.
     *
     * @param a the parameter
     * @param b the parameter
     * @param c the parameter
     * @return the maximum value
     */
    public static final int max(int a, int b, int c) {
        return a > b ? (a > c ? a : c) : (b > c ? b : c);
    }

    /**
     * Find the maximum value of the specified array.
     *
     * @param values the values array
     * @return the maximum value
     */
    public static final int max(int ...values) {
        int r = Integer.MIN_VALUE;
        for (int v : values) {
            if (v > r) {
                r = v;
            }
        }
        return r;
    }

    /**
     * Find the minimum value among the three parameters.
     *
     * @param a the parameter
     * @param b the parameter
     * @param c the parameter
     * @return the minimum value
     */
    public static final int min(int a, int b, int c) {
        return a < b ? (a < c ? a : c) : (b < c ? b : c);
    }

    /**
     * Find the minimum value from the specified array.
     *
     * @param values the values array
     * @return the minimum value
     */
    public static final int min(int ...values) {
        int r = Integer.MAX_VALUE;
        for (int v : values) {
            if (v < r) {
                r = v;
            }
        }
        return r;
    }

    /**
     * Calculate the sum of the values from the specified array.
     *
     * @param values the values array
     * @return the sum of the values
     */
    public static final long sum(int ...values) {
        long r = 0;
        for (int v : values) {
            r += v;
        }
        return r;
    }

    /**
     * Calculate the average of the values from the specified array.
     *
     * @param values the values array
     * @return the average of the values
     */
    public static final double avg(int ...values) {
        long s = sum(values);
        return ((double)s / values.length);
    }

    /**
     * Round the target value to the nearest value divisible by the divide value, with ties rounding up.
     * 
     * @param target the target value to be rounded
     * @param divide the divide vale
     * @return the target value to the nearest value divisible by the divide value, with ties rounding up
     */
    public static final long halfUp(long target, long divide) {
        Check.lt(0, divide, "divide must greater than 0");
        long rem = target % divide;
        target -= rem;
        if (rem < 0) {
            rem = divide + rem;
            target -= divide;
        }
        if (rem * 2 >= divide) {
            target += divide;
        }
        return target;
    }
}
