/**
 * CachePerformance.java
 *
 * Copyright 2016 the original author or authors.
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
package org.apache.niolex.commons.collection;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.niolex.commons.bean.Pair;
import org.apache.niolex.commons.test.MultiPerformance;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 2.1.2
 * @since May 27, 2016
 */
public class CachePerformance {
    
    private static final int ONE_RUN = 1000;
    private static final int ITERATION = 100;
    
    private static int current = 0;
    
    private static synchronized Pair<Integer, Integer> getNextBatch() {
        return Pair.create(current, current = current + 13 * ONE_RUN);
    }
    
    private abstract static class Base extends MultiPerformance {
        protected final Cache<Integer, Integer> cache;
        protected final ThreadLocal<Integer> min = new ThreadLocal<Integer>();
        protected final ThreadLocal<Integer> max = new ThreadLocal<Integer>();

        public Base(Cache<Integer, Integer> mainCache) {
            // super(threadsNumber, innerIteration, outerIteration);
            super(7, 1, ITERATION * 2);
            this.cache = mainCache;
        }
        
        protected void prepareData() {
            Pair<Integer, Integer> p = getNextBatch();
            min.set(p.a);
            max.set(p.b);
        }
    }
    
    private static class Insert extends Base {

        public Insert(Cache<Integer, Integer> mainCache) {
            super(mainCache);
        }

        /**
         * This is the override of super method.
         * @see org.apache.niolex.commons.test.MultiPerformance#run()
         */
        @Override
        protected void run() {
            Integer start = min.get();
            if (start == null) {
                prepareData();
            }
            int a = min.get();
            for (int i = a; i < a + ONE_RUN; ++i) {
                cache.put(i, i * 2);
            }
            min.set(a + ONE_RUN);
        }
        
    }
    
    private static final AtomicInteger VALID = new AtomicInteger();
    private static final AtomicInteger WRONG = new AtomicInteger();
    private static final AtomicInteger NIL = new AtomicInteger();
    
    private static class Query extends Base {
        
        public Query(Cache<Integer, Integer> mainCache) {
            super(mainCache);
        }

        /**
         * This is the override of super method.
         * @see org.apache.niolex.commons.test.MultiPerformance#run()
         */
        @Override
        protected void run() {
            Integer start = min.get();
            if (start == null) {
                prepareData();
            }
            int a = min.get();
            for (int i = a; i < a + ONE_RUN; ++i) {
                Integer k = cache.get(i);
                if (k == null)
                    NIL.incrementAndGet();
                else {
                    int s = k.intValue();
                    if (s == i * 2)
                        VALID.incrementAndGet();
                    else
                        WRONG.incrementAndGet();
                }
            }
            min.set(a + ONE_RUN);
        }
        
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        final int ARR_SIZE = 4;
        final int CACHE_SIZE = 100000;
        Cache<Integer, Integer>[] ca = CacheCompareSingleThread.newArray(CACHE_SIZE, ARR_SIZE);
        
        System.out.println("7 Threads test.");
        
        System.out.println("Insertion.");
        for (int i = 0; i < ARR_SIZE; ++i) {
            current = 0;
            new Insert(ca[i]).start();
            System.out.println("Size: " + ca[i].size());
        }
        
        
        
        System.out.println("Query.");
        for (int i = 0; i < ARR_SIZE; ++i) {
            current = 0;
            new Query(ca[i]).start();
            System.out.println("Size: " + ca[i].size());
            System.out.println("V " + VALID.getAndSet(0) + ", WRG " + WRONG.getAndSet(0) + ", NIL " + NIL.getAndSet(0));
        }
    }
    
}
