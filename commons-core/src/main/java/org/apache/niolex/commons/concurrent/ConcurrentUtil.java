/**
 * ConcurrentUtil.java
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
package org.apache.niolex.commons.concurrent;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Some common concurrent methods.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2012-12-20
 */
public class ConcurrentUtil {

    /**
     * Init the map with this new value if this key is absent in the specified map;
     * otherwise we return the old value associated with this key.
     *
     * @param map the map you want to init
     * @param key the key you want to init
     * @param newValue the new value ready to put into this map
     * @return the new or old value whichever is associated with this key
     */
    public static final <K, V> V initMap(ConcurrentMap<K, V> map, K key, V newValue) {
        V oldValue = map.putIfAbsent(key, newValue);
        return (oldValue == null) ? newValue : oldValue;
    }

    /**
     * Shutdown the thread pool and wait until all the current tasks to be finished despite interruption.
     *
     * @param pool the thread pool to be terminated
     */
    public static final void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown(); // Disable new tasks from being submitted

        while (true) {
            try {
                // Wait a while for existing tasks to terminate
                if (pool.awaitTermination(60, TimeUnit.SECONDS)) {
                    break;
                }
            } catch (InterruptedException ie) {
            }
        }
    }

    /**
     * Shutdown the thread pool and await the specified timeout. If the current tasks can not be finished
     * in time, the remained tasks will be cancelled.
     *
     * @param pool the thread pool to be terminated
     * @param timeout the maximum time to wait
     * @param unit the time unit of the timeout argument
     */
    public static final void shutdownAndAwaitTermination(ExecutorService pool, long timeout, TimeUnit unit) {
        pool.shutdown(); // Disable new tasks from being submitted

        long remain, start = System.currentTimeMillis();
        timeout = unit.toMillis(timeout); // Convert timeout to milliseconds
        while (true) {
            try {
                remain = System.currentTimeMillis() - start; // The elapsed time
                remain = timeout - remain; // The real remained time

                // If timeout, shutdown now.
                if (remain < 1) {
                    pool.shutdownNow();
                    break;
                }

                // Wait a while for existing tasks to terminate
                if (pool.awaitTermination(remain, TimeUnit.MILLISECONDS)) {
                    break;
                }
            } catch (InterruptedException ie) {
            }
        }
    }

}
