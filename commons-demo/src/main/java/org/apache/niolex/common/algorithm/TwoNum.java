/**
 * TwoNum.java
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
package org.apache.niolex.common.algorithm;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-11-26
 */
public class TwoNum {

    public static class Comp implements Comparator<int[]> {

        /**
         * This is the override of super method.
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(int[] o1, int[] o2) {
            return o1[0] - o2[0];
        }

    }

    public int[] twoSum(int[] numbers, int target) {
        int[][] arr = new int[numbers.length][2];

        for (int i = 0; i < numbers.length; ++i) {
            arr[i][0] = numbers[i];
            arr[i][1] = i + 1;
        }

        Arrays.sort(arr, new Comp());
        int k;

        for (int i = 0, j = numbers.length - 1; i < j;) {
            k = arr[i][0] + arr[j][0];
            if (k < target) {
                ++i;
            } else if (k > target) {
                --j;
            } else {
                int[] ret = new int[2];
                if (arr[i][1] > arr[j][1]) {
                    ret[0] = arr[j][1];
                    ret[1] = arr[i][1];
                } else {
                    ret[0] = arr[i][1];
                    ret[1] = arr[j][1];
                }
                return ret;
            }
        }

        return null;
    }

}
