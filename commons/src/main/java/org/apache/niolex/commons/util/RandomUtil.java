/**
 * RandomUtil.java
 *
 * Copyright 2011 Baidu, Inc.
 *
 * Baidu licenses this file to you under the Apache License, version 2.0
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

import java.util.Random;

/**
 * Generate random things.
 * 
 * @author @author@ (@author-email@)
 * 
 * @version @version@, $Date: 2011-9-2$
 * 
 */
public abstract class RandomUtil {
    private static Random generator = new Random();
    
    /**
     * Generate a random int array.
     * 
     * @param length the array length.
     * @return the generated array.
     */
    public static final int[] randIntArray(int length) {
        int[] ar = new int[length];
        for (int i = 0; i < length; ++i) {
            ar[i] = i;
        }
        for (int i = length, j, k; i > 1; --i) {
            j = generator.nextInt(i);
            k = ar[i - 1];
            ar[i - 1] = ar[j];
            ar[j] = k;
        }
        return ar;
    }
}
