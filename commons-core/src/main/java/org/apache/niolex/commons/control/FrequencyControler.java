/**
 * FrequencyControler.java
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
package org.apache.niolex.commons.control;

import org.apache.niolex.commons.collection.CyclicIntArray;

/**
 * This class controls the invoke frequency.
 *
 * The frequency is controlled by two factor: the splitCnt and the totalNum.
 * The total interval is split by the splitCnt, and user need to update the
 * number of each split slot from time to time. This class will check whether
 * We have already exceeds the totoalNum. We return true if it's good and
 * false if exceeds the totoalNum.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2012-12-25
 */
public class FrequencyControler {

    /**
     * Store the value of each slot.
     */
    private final CyclicIntArray cyclic;

    /**
     * The total number.
     */
    private final int totalNum;

    /**
     * The current number.
     */
    private int currentNum;

    /**
     * Create a FrequencyControler.
     *
     * @param splitCnt the total interval will be split by this count.
     * @param totalNum the total number.
     */
    public FrequencyControler(int splitCnt, int totalNum) {
        super();
        this.cyclic = new CyclicIntArray(splitCnt);
        this.totalNum = totalNum;
        this.currentNum = 0;
    }

    /**
     * Check the validity of this split interval with this split number.
     *
     * @param splitNum the current split interval event happen number.
     * @return true if OK, false if out of control.
     */
    public boolean check(int splitNum) {
        currentNum += splitNum;
        currentNum -= cyclic.push(splitNum);
        if (currentNum > totalNum) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Get the split slots data array.
     *
     * @return the split slots data array.
     */
    public int[] getArray() {
        return cyclic.getArray();
    }

    /**
     * Get the head pointer for the slots data array.
     *
     * @return the head pointer position.
     */
    public int getHead() {
        return cyclic.getHead();
    }

}
