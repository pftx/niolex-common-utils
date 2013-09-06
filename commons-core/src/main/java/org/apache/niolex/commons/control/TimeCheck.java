/**
 * TimeCheck.java
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

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class manage the frequency check of one item, used only in {@link TimeControler}
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2012-12-26
 */
public class TimeCheck {

    /**
     * The counter to count the event happen times.
     */
    final AtomicInteger counter;

    /**
     * The check interval of each split slot.
     */
    final int intervalTime;

    /**
     * The average interval count.
     */
    final int intervalCnt;

    /**
     * The frequency check.
     */
    final FrequencyCheck freqCheck;

    /**
     * Store the last check status.
     */
    volatile boolean lastCheckStatus;

    /**
     * The last check time snapshot.
     */
    volatile long lastCheckTime;

    /**
     * Create a new TimeCheck.
     *
     * @param checkInterval the time interval to check in milliseconds.
     * @param splitCnt the slot number to split, must be a factor of checkInterval.
     * @param totalNum the total number of events you can tolerate in the checkInterval.
     */
    public TimeCheck(int checkInterval, int splitCnt, int totalNum) {
        super();
        this.counter = new AtomicInteger();
        this.intervalTime = checkInterval / splitCnt;
        this.intervalCnt = totalNum / splitCnt;
        this.freqCheck = new FrequencyCheck(splitCnt, totalNum);
        this.lastCheckStatus = true;
        this.lastCheckTime = System.currentTimeMillis();
    }

    /**
     * Check the frequency.
     * If it's already too intensive, we will work in the downgrade mode.
     * In the downgrade mode, we will only let the number of check less than
     * intervalCnt return true.
     *
     * @return true if OK, false if you need to reject the event.
     */
    public boolean check() {
        long t = System.currentTimeMillis();
        // Need to check whether the interval is long enough to split.
        if (t - lastCheckTime >= intervalTime) {
            // Check will happen in synchronized area.
            checkIntervalCnt(t);
        }
        // We judge according to the last check status.
        if (lastCheckStatus) {
            counter.incrementAndGet();
            return true;
        } else {
            if (counter.get() < intervalCnt) {
                counter.incrementAndGet();
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Check the time interval in a synchronized block.
     *
     * @param time the current time stamp
     */
    public synchronized void checkIntervalCnt(final long time) {
        if (time - lastCheckTime >= intervalTime) {
            // It's not necessary to do normalization: normal count to time interval.
            lastCheckTime = time;
            // Because there will be at most only 1 count not in the interval.
            // Let's check it.
            lastCheckStatus = freqCheck.check(counter.getAndSet(0));
        }
    }

    /**
     * Get the event counter.
     *
     * @return the event counter.
     */
    public AtomicInteger getCounter() {
        return counter;
    }

    /**
     * Get the last check status.
     *
     * @return the last check status.
     */
    public boolean lastCheckStatus() {
        return lastCheckStatus;
    }

    /**
     * @return the lastCheckTime
     */
    public long getLastCheckTime() {
        return lastCheckTime;
    }

}
