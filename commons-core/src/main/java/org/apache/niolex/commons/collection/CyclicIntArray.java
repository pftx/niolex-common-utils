/**
 * CyclicIntArray.java
 *
 * Copyright 2012 The original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.niolex.commons.collection;

import java.util.NoSuchElementException;

/**
 * This class encapsulate an int array, one can get the head and tail at any time.
 * When there are more data than the capacity, we override the eldest element.
 * <br><b>
 * This implementation is not thread safe.
 * </b>
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2012-12-25
 * @see CircularList
 */
public class CyclicIntArray {

    /**
     * The internal array.
     */
    private final int[] array;

    /**
     * The current array size.
     */
    private int size;

    /**
     * The current head.
     */
    private int head;

    /**
     * Create a CyclicIntArray with this specified size.
     *
     * @param size
     */
    public CyclicIntArray(int size) {
        super();
        if (size < 2) {
            throw new IllegalArgumentException("The size must greater than 1.");
        }
        this.array = new int[size];
        this.size = 0;
        head = 0;
    }

    /**
     * Push the parameter into the array, and return the old value in the slot.
     *
     * @param k the parameter to put.
     * @return the previous value in the slot.
     */
    public int push(int k) {
        int r = array[head];
        array[head] = k;
        head = (head + 1) % array.length;
        if (size < array.length) ++size;
        return r;
    }

    /**
     * Return the eldest item in the array, or throw NoSuchElementException if the
     * array is empty.
     *
     * @return the eldest item in the array
     */
    public int pop() {
        if (size > 0) {
            --size;
            return array[(head + array.length - size - 1) % array.length];
        } else {
            throw new NoSuchElementException("The array is empty.");
        }
    }

    /**
     * Get the internal int array.
     *
     * @return the internal int array.
     */
    public int[] getArray() {
        return array;
    }

    /**
     * Get the current head position.
     *
     * @return the current head position.
     */
    public int getHead() {
        return head;
    }

    /**
     * Get the size of the internal int array.
     *
     * @return the size of the internal int array.
     */
    public int size() {
        return size;
    }

}
