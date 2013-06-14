/**
 * Synchronized.java
 *
 * Copyright 2013 the original author or authors.
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
package org.apache.niolex.commons.internal;

import java.util.List;

import org.apache.niolex.commons.bean.MutableOne.DataChangeListener;
import org.apache.niolex.commons.control.TimeCheck;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-6-14
 */
public class Synchronized {

    /**
     * Notify all the listeners in a synchronized block.
     *
     * @param list the listeners
     * @param one the new data
     */
    public static <T> void notifyListeners(List<DataChangeListener<T>> list, T one) {
        synchronized (list) {
            for (DataChangeListener<T> li : list) {
                li.onDataChange(one);
            }
        }
    }

    /**
     * Check the time interval in a synchronized block.
     *
     * @param intervalTime
     * @param time
     * @param check
     * @return the count happened in the time interval
     */
    public static int getIntervalCnt(final int intervalTime, final long time, TimeCheck check) {
        int cnt = 0;
        synchronized (check.getCounter()) {
            if (time - check.getLastCheckTime() >= intervalTime) {
                // It's not necessary to do normalization
                cnt = check.getCounter().getAndSet(0);
                check.setLastCheckTime(time);
            }
        }
        return cnt;
    }

}
