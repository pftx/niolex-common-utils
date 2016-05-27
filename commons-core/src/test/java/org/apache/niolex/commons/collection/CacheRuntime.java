/**
 * CacheRuntime.java
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

import org.apache.niolex.commons.collection.CachePerformance.MulLRUCache;
import org.apache.niolex.commons.concurrent.ThreadUtil;
import org.apache.niolex.commons.test.MockUtil;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 2.1.2
 * @since May 27, 2016
 */
public class CacheRuntime {
    private static final int CACHE_SIZE = 20000;
    private static final Cache<Integer, Integer> cache1 = new ConcurrentLRUCache<Integer, Integer>(CACHE_SIZE);
    private static final Cache<Integer, Integer> cache2 = new MulLRUCache<Integer, Integer>(new LRUHashMap<Integer, Integer>(CACHE_SIZE));
    
    private static int calcV(int k) {
        return (k + 2) * 5;
    }
    
    private static long From1Time;
    private static long FromMaxTime;
    
    private static class From1 implements Runnable {
        
        private Cache<Integer, Integer> cache;

        public From1(Cache<Integer, Integer> cache) {
            super();
            this.cache = cache;
        }

        /**
         * This is the override of super method.
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            long in = System.currentTimeMillis();
            for (int i = 0; i < CACHE_SIZE; ++i) {
                cache.put(i, calcV(i));
            }
            ThreadUtil.sleep(100);
            for (int i = 0; i < CACHE_SIZE; ++i) {
                cache.put(i, calcV(i));
            }
            From1Time = System.currentTimeMillis() - in;
            System.out.println("From1 done.");
        }
        
    }
    
    private static class FromMax implements Runnable {
        
        private Cache<Integer, Integer> cache;
        
        public FromMax(Cache<Integer, Integer> cache) {
            super();
            this.cache = cache;
        }
        
        /**
         * This is the override of super method.
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            long in = System.currentTimeMillis();
            for (int i = CACHE_SIZE; i > 0; --i) {
                cache.put(i, calcV(i));
            }
            ThreadUtil.sleep(100);
            for (int i = CACHE_SIZE; i > 0; --i) {
                cache.put(i, calcV(i));
            }
            FromMaxTime = System.currentTimeMillis() - in;
            System.out.println("FromMax done.");
        }
        
    }
    
    private static class Evil implements Runnable {
        
        private Cache<Integer, Integer> cache;
        protected volatile boolean isWorking = true;
        
        public Evil(Cache<Integer, Integer> cache) {
            super();
            this.cache = cache;
        }
        
        /**
         * This is the override of super method.
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            while (isWorking) {
                int k = MockUtil.randInt(CACHE_SIZE * 10);
                if (k < CACHE_SIZE * 5)
                    ThreadUtil.sleep(1);
                cache.put(k, calcV(k));                
            }
            System.out.println("Evil done.");
        }
        
    }
    
    private static final AtomicInteger VALID = new AtomicInteger();
    private static final AtomicInteger WRONG = new AtomicInteger();
    private static final AtomicInteger NIL = new AtomicInteger();
    
    private static class Query implements Runnable {
        private long time;
        
        private Cache<Integer, Integer> cache;
        private boolean put;
        
        public Query(Cache<Integer, Integer> cache, boolean put) {
            super();
            this.cache = cache;
            this.put = put;
        }
        
        /**
         * This is the override of super method.
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            int run = put ? CACHE_SIZE * 5 : CACHE_SIZE * 20;
            long in = System.currentTimeMillis();
            while (run-- > 0) {
                int k = MockUtil.randInt(CACHE_SIZE);
                Integer v = cache.get(k);
                if (v == null) {
                    NIL.incrementAndGet();
                    if (put)
                        cache.put(k, calcV(k));                
                    ThreadUtil.sleep(1);
                } else if (v == calcV(k)) {
                    VALID.incrementAndGet();
                } else {
                    WRONG.incrementAndGet();
                }
            }
            time = System.currentTimeMillis() - in;
        }
        
        public long time() {
            return time;
        }
        
    }
    
    

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        main1(cache1);
        main1(cache2);
    }
    
    public static void main1(Cache<Integer, Integer> cache) throws Exception {
        Thread thr1 = new Thread(new From1(cache));
        Thread thr2 = new Thread(new FromMax(cache));
        Evil e = new Evil(cache);
        Thread thr3 = new Thread(e);
        thr3.setDaemon(true);
        
        thr1.start();
        thr2.start();
        ThreadUtil.sleep(10);
        thr3.start();

        ThreadUtil.sleep(10);
        
        Thread[] th = new Thread[5];
        Query[] qu = new Query[5];
        
        for (int i = 0; i < 5; ++i) {
            qu[i] = new Query(cache1, (i == 0 ? true : false));
            th[i] = new Thread(qu[i]);
            th[i].start();
        }
        
        long qTime = 0;
        for (int i = 0; i < 5; ++i) {
            th[i].join();
            qTime += qu[i].time();
        }
        
        e.isWorking = false;
        thr1.join();
        thr2.join();
        
        System.out.println("INSERT " + (From1Time + FromMaxTime) + ", QRY " + qTime);
        long v = VALID.getAndSet(0), n = NIL.getAndSet(0);
        System.out.println("HIT " + v + ", WRG " + WRONG.getAndSet(0) + ", MIS " + n + " HIT RATE " + (v / (double)(v + n)));
    }

}
