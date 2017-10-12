/**
 * CommonStatistics.java
 *
 * Copyright 2016 the original author or authors.
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

import java.util.Arrays;

import org.apache.niolex.commons.test.Check;
import org.apache.niolex.commons.test.Tester;

/**
 * Calculate the commonly used statistics for a given data set and a given percentile.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2016-3-10
 */
public class CommonStatistics {

    /**
     * The data structure used to return statistics. Please do not take it as a class, it a C structure.
     *
     * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
     * @version 1.0.0
     * @since 2016-3-10
     */
    public static class Statistics {

        /**
         * The average (mean).
         */
        public double avg = -1;

        /**
         * The standard deviation.
         */
        public double sd = -1;

        public double variance = -1;

        /**
         * <pre>
         * If the data set size is odd, then median is the middle number.
         * i.e. 1 2 3 4 5 =&gt; median = 3
         * If the data set size is even, then median is the average of the two middle number.
         * i.e. 1 2 3 4 5 6 =&gt; median = 3.5
         * </pre>
         */
        public double median = -1;

        public double sum = 0;
        public double max = -1;
        public double min = -1;

        /**
         * The actual data set size which participates calculation.
         * If it equals 0, then there is no statistics.
         */
        public int size;
    }

    /**
     * Calculate the power of 2 of the given number.
     *
     * @param v the number
     * @return the power of 2 of the given number
     */
    public static final double pow2(double v) {
        return v * v;
    }

    /**
     * The internal data set used to calculate statistics.
     */
    private double[] value;

    /**
     * The current data set size.
     */
    private int size;

    /**
     * Whether the data set is sorted.
     */
    private boolean sorted = false;

    /**
     * Create a default object.
     */
    public CommonStatistics() {
        value = new double[100];
        size = 0;
    }

    /**
     * Create a common statistics object using the specified data set.
     * The data set will be copied and leave alone.
     *
     * @param dataSet the data set to use
     */
    public CommonStatistics(double[] dataSet) {
        size = dataSet.length;
        value = Arrays.copyOf(dataSet, size);
    }

    /**
     * Adds the value to the data set.
     *
     * @param v the value to be added
     */
    public void addValue(double v) {
        if (size == value.length) {
            value = Arrays.copyOf(value, size * 2);
        }
        sorted = false;
        value[size++] = v;
    }

    /**
     * Sort the current internal data.
     */
    public void sortData() {
        if (sorted) return;

        Arrays.sort(value, 0, size);
        sorted = true;
    }

    /**
     * Calculate the sub-part statistics by the given percentile.
     * With a percentile of 100 means the whole data set.
     *
     * @param percentile the percentile in range [0, 100]
     * @return the sub-part statistics
     */
    public Statistics percentileStatistics(double percentile) {
        Check.between(0.0, percentile, 100.0, "percentile muster in the range of [0.0, 100.0]");
        sortData();

        Statistics s = new Statistics();

        int len = (int)(percentile * (size - 1) / 100.0) + 1;
        s.size = len;

        double sum = 0.0;
        for (int i = 0; i < len; ++ i) {
            sum += value[i];
        }

        double avg = sum / len;
        double variance = 0.0;
        for (int i = 0; i < len; ++ i) {
            variance += pow2(value[i] - avg);
        }

        variance = variance / len;

        s.avg = avg;
        s.sd = Math.pow(variance, 0.5);
        s.variance = variance;

        if (len % 2 == 0) {
            s.median = (value[len / 2 - 1] + value[len / 2]) / 2;
        } else {
            s.median = value[len / 2];
        }

        s.sum = sum;
        s.min = value[0];
        s.max = value[len - 1];

        return s;
    }

    /**
     * Calculate the percentile value from the given percentile.
     *
     * Using formula Pi = (Idx / MaxIdx) * 100 for percentile.
     * Then we could find the index by formula Idx = Pi * MaxIdx / 100
     * if the index is not integer, we will use interpolation.
     *
     * @param percentile the percentile in range [0, 100]
     * @return the calculated percentile value
     */
    public double percentileValue(double percentile) {
        Check.between(0.0, percentile, 100.0, "percentile muster in the range of [0.0, 100.0]");
        sortData();

        double dIdx = percentile * (size - 1) / 100.0;
        int idx = (int)(dIdx);

        if (Tester.equal(dIdx, idx)) {
            // The index is a integer.
            return value[idx];
        } else {
            // The index is not integer, map to the integer part, and interpolate the fragment part.
            double f = dIdx - idx;
            return value[idx] * (1 - f) + value[idx + 1] * f;
        }
    }

}
