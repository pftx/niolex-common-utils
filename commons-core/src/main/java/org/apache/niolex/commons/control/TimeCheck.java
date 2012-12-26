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
     * The frequency controler.
     */
    final FrequencyControler controler;

    /**
     * Store the last check status.
     */
    boolean lastCheckStatus;

    /**
     * The last check time snapshot.
     */
    long lastCheckTime;

    /**
     * Create a new TimeCheck.
     *
     * @param checkInterval
     * @param splitCnt
     * @param totoalNum
     */
    public TimeCheck(int checkInterval, int splitCnt, int totalNum) {
        super();
        this.counter = new AtomicInteger();
        this.intervalTime = checkInterval / splitCnt;
        this.intervalCnt = totalNum / splitCnt;
        this.controler = new FrequencyControler(splitCnt, totalNum);
        this.lastCheckStatus = true;
        this.lastCheckTime = System.currentTimeMillis();
    }

    /**
     * Check the status of this object, and trigger a event if it's OK.
     * If it's already too frequency, we will work in the downgrade mode.
     *
     * @return true if OK, false if you need to reject the event.
     */
    public boolean check() {
        long t = System.currentTimeMillis();
        // Need to check when interval is enough.
        if (t - lastCheckTime >= intervalTime) {
            int cnt = 0;
            // Check will happen in synchronized area.
            synchronized (counter) {
                if (t - lastCheckTime >= intervalTime) {
                    // It's not necessary to do normalization
                    cnt = counter.getAndSet(0);
                    lastCheckTime = t;
                }
            }
            if (cnt != 0) {
                // Let's check it.
                lastCheckStatus = controler.check(cnt);
            }
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

}
