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

}
