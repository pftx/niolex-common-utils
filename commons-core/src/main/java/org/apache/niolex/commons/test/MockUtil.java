/**
 * MockUtil.java
 *
 * Copyright 2011 Niolex, Inc.
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

import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang.RandomStringUtils;

/**
 * Generate mock datas, for unit test like things.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2011-9-2
 */
public abstract class MockUtil {

    private static Random generator = new Random();

    /**
     * Generate a random int uniformly distributed int value between 0 (inclusive) and the specified value
     * (exclusive), drawn from the inner random number generator's sequence.
     *
     * @param max
     * @return the result
     */
    public static final int randInt(int max) {
    	return generator.nextInt(max);
    }

    /**
     * Generate a random int uniformly distributed int value between <code>from (inclusive)</code> and <code>to</code>
     * (exclusive), drawn from the inner random number generator's sequence.
     *
     * @param from
     * @param to
     * @return the result
     */
    public static final int randInt(int from, int to) {
        return generator.nextInt(to - from) + from;
    }

    /**
     * Returns the next pseudo random, uniformly distributed long value. Because of the restriction of
     * JDK Random utility, not all long value are possible to be returned.
     *
     * @return the result
     */
    public static final long randLong() {
        return generator.nextLong();
    }

    /**
     * Generate a randomly reordered int array.
     * The array will be filled with the sequence [0 .. length)
     *
     * @param length the array length.
     * @return the generated reordered array.
     */
    public static final int[] reorderIntArray(int length) {
        int[] ar = new int[length];
        for (int i = 0; i < length; ++i) {
            ar[i] = i;
        }
        return reorderIntArray(ar);
    }

    /**
     * Randomly reorder the int array, with all data stay the same.
     *
     * @param ar the array to be reordered.
     * @return the reordered array.
     */
    public static final int[] reorderIntArray(int[] ar) {
    	int length = ar.length;
    	for (int i = length, j, k; i > 1; --i) {
    		j = generator.nextInt(i);
    		k = ar[i - 1];
    		ar[i - 1] = ar[j];
    		ar[j] = k;
    	}
    	return ar;
    }

    /**
     * Generate a random array.
     *
     * @param length
     * @return the result
     */
    public static final int[] randIntArray(int length) {
    	int[] ar = new int[length];
        for (int i = 0; i < length; ++i) {
            ar[i] = generator.nextInt();
        }
        return ar;
    }

    /**
     * Generates random bytes and places them into a byte array.
     *
     * @param length
     * @return the result
     */
    public static final byte[] randByteArray(int length) {
    	byte[] ar = new byte[length];
    	generator.nextBytes(ar);
    	return ar;
    }

    /**
     * Generates random UUID.
     *
     * @return the result
     */
    public static final String randUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * Generates random string which contains only alphabetic and number.
     *
     * @return the result with length 8
     */
    public static final String randString() {
        return randString(8);
    }

    /**
     * Generates random string which contains only alphabetic and number.
     *
     * @param length the length of the generated string
     * @return the result
     */
    public static final String randString(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

}
