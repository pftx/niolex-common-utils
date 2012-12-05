/**
 * MultiPerformance.java
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
package org.apache.niolex.commons.test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This class is for test code performance in multi-threading environment.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2012-12-5
 */
public abstract class MultiPerformance {

    private final int threadsNumber;
    private final int innerIteration;
    private final int outerIteration;
    private long max;
    private long min;
    private AtomicLong total = new AtomicLong();

    /**
     * Create a MultiPerformance class.
     *
     * @param threadsNumber The number of threads to run this performance test.
     * @param innerIteration The iteration to run as a measure unit.
     * @param outerIteration The iteration to run as a total iteration.
     */
    public MultiPerformance(int threadsNumber, int innerIteration, int outerIteration) {
        super();
        this.threadsNumber = threadsNumber;
        this.innerIteration = innerIteration;
        this.outerIteration = outerIteration;
    }

    /**
     * Implements this method to do your work.
     */
    protected abstract void run();

    /**
     * This is run one batch.
     */
    protected void oneRun() {
        for (int j = 0; j < innerIteration; ++j) {
            run();
        }
    }

    /**
     * Multiple threads will run this class's object.
     *
     * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
     * @version 1.0.5
     * @since 2012-12-5
     */
    protected class Run implements Runnable {

        private AtomicInteger cnt = new AtomicInteger();

        /**
         * Override super method
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            long in, cu;
            while (cnt.getAndIncrement() < outerIteration) {
                in = System.currentTimeMillis();
                oneRun();
                cu = System.currentTimeMillis() - in;
                if (cu > max) max = cu;
                if (cu < min) min = cu;
                total.addAndGet(cu);
            }
        }

    }

    /**
     * Start to test now.
     */
    public void start() {
        /**
         * This is for preheat the JVM.
         */
        oneRun();

        /**
         * The real thing begin now.
         */
        max = Integer.MIN_VALUE;
        min = Integer.MAX_VALUE;
        total.set(0);
        Run run = new Run();
        long ein = System.currentTimeMillis();
        Thread[] threads = new Thread[threadsNumber];
        // Start threads.
        for (int i = 0; i < threadsNumber; ++i) {
            threads[i] = new Thread(run);
            threads[i].start();
        }
        // Wait for result.
        for (int i = 0; i < threadsNumber; ++i) {
            try { threads[i].join(); } catch (InterruptedException e) { }
        }
        // Done.
        long cu = total.get() / outerIteration;
        System.out.println("Performance Done, Total Time - " + (System.currentTimeMillis() - ein));
        System.out.println("Iter " + outerIteration + ", Avg " + cu + ", Max "
                + max + ", Min " + min);
    }

}
