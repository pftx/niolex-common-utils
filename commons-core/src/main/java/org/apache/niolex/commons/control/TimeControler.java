/**
 * TimeControler.java
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

import java.util.concurrent.ConcurrentHashMap;

/**
 * This class control the invoke frequency by time and maximum invoke count.
 * We can manage multiple keys together, and each key can have different configuration.
 *
 * Please call {@link #initTimeCheck(String, int, int, int)} before use the
 * key. Or we will throw an Exception if the key has not been initialized yet.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2012-12-25
 */
public class TimeControler {

    private final ConcurrentHashMap<String, TimeCheck> controlerMap = new ConcurrentHashMap<String, TimeCheck>();

    /**
     * Init the key for frequency control. We will manage the check in batch, split by the splitCnt.
     * The bigger the splitCnt, the more preciseness you will get, but consumes more memory meanwhile.
     *
     * @param key the key to be initialized.
     * @param checkInterval the time interval to check in milliseconds.
     * @param splitCnt the slot number to split, must be a factor of checkInterval.
     * @param totalNum the total number of events you can tolerate in the checkInterval.
     */
    public void initTimeCheck(String key, int checkInterval, int splitCnt, int totalNum) {
        TimeCheck tc = new TimeCheck(checkInterval, splitCnt, totalNum);
        controlerMap.put(key, tc);
    }

    /**
     * Check the status of this key.
     *
     * @param key the key to be checked.
     * @return true if check OK, false if it's too frequent, need to be controlled.
     * @throws IllegalStateException If key not initialized.
     */
    public boolean check(String key) {
        TimeCheck tc = controlerMap.get(key);
        if (tc != null) {
            return tc.check();
        } else {
            throw new IllegalStateException("Please init this key first!");
        }
    }

}
