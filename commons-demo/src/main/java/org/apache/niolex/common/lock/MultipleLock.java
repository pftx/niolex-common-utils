/**
 * MultipleLock.java
 *
 * Copyright 2013 The original author or authors.
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
package org.apache.niolex.common.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.niolex.commons.test.Counter;
import org.apache.niolex.commons.test.MockUtil;
import org.apache.niolex.commons.util.Runner;
import org.apache.niolex.commons.util.SystemUtil;

/**
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2013-1-21
 */
public class MultipleLock {
    private static Map<Integer, Counter> hashMap = new HashMap<Integer, Counter>();
    private static AtomicInteger atom = new AtomicInteger();
    private static int THREADS = 80;
    private static int MAX = 160;

    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < MAX; ++i) {
            hashMap.put(i, new Counter());
        }
        for (int i = 0; i < 20; ++i) {
            atom.set(0);
            main();
        }
    }

    public static void main() throws InterruptedException {
        // Start 10 threads to increase.
        Thread[] ts = new Thread[THREADS];
        long in = System.currentTimeMillis();
        for (int i = 0; i < THREADS; ++i) {
            ts[i] = Runner.run(new MultipleLock(), "run");
        }
        for (int i = 0; i < THREADS; ++i) {
            ts[i].join();
        }
        System.out.println("Time - " + (System.currentTimeMillis() - in));
    }

    public void run() {
        while (atom.incrementAndGet() < 2000) {
            inc();
        }
    }

    public static void inc() {
        int key = MockUtil.randInt(MAX);
        Counter c = hashMap.get(key);
        synchronized (c) {
            c.set(c.cnt() + 1);
            SystemUtil.sleep(1);
        }
    }

}
