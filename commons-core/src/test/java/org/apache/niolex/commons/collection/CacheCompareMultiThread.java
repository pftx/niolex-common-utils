/**
 * CacheCompareMultiThread.java
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

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.niolex.commons.test.MockUtil;
import org.apache.niolex.commons.test.StopWatch;
import org.apache.niolex.commons.test.StopWatch.Stop;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 2.1.2
 * @since May 30, 2016
 */
public class CacheCompareMultiThread {
    
    protected static class Item {
        int start;
        int end;
        public Item(int start, int end) {
            super();
            this.start = start;
            this.end = end;
        }
    }
    
    private static final ConcurrentLinkedQueue<Item> queue = new ConcurrentLinkedQueue<Item>();
    private static final int BATCH_SIZE = 1000;
    private static final int CACHE_SIZE = 100000;
    private static final StopWatch st = new StopWatch(10);
    
    public static void prepareData() {
        for (int i = 0; i < 2000; ++i) {
            queue.offer(new Item(i * BATCH_SIZE, (i + 1) * BATCH_SIZE));
        }
    }
    
    protected static int runIdx = 0;
    
    protected static class Run implements Runnable {
        private final Cache<Integer, Integer> c;
        
        public Run(Cache<Integer, Integer> c) {
            super();
            this.c = c;
        }

        public void run() {
            while (true) {
                Item it = queue.poll();
                if (it == null)
                    return;
                Stop s = st.start();
                switch (runIdx) {
                    case 0:
                        sequentialPut(c, it);
                        break;
                    case 1:
                        randomPut(c, it);
                        break;
                    case 2:
                        randomQuery(c, it);
                        break;
                }
                s.stop();
            }
        }
    }
    
    public static Integer getValue(int k) {
        return k * 2 + 7;
    }
    
    public static void sequentialPut(Cache<Integer, Integer> c, Item it) {
        for (int i = it.start; i < it.end; ++i) {
            c.put(i, getValue(i));
        }
    }
    
    public static void randomPut(Cache<Integer, Integer> c, Item it) {
        for (int i = it.start; i < it.end; ++i) {
            int k = MockUtil.randInt(CACHE_SIZE * 2);
            c.put(k, getValue(k));
        }
    }
    private static final AtomicInteger VALID = new AtomicInteger();
    private static final AtomicInteger WRONG = new AtomicInteger();
    private static final AtomicInteger NIL = new AtomicInteger();
    
    public static void randomQuery(Cache<Integer, Integer> c, Item it) {
        for (int i = it.start; i < it.end; ++i) {
            int k = MockUtil.randInt(CACHE_SIZE);
            Integer ss = c.get(k);
            if (ss == null)
                NIL.incrementAndGet();
            else if (ss.equals(getValue(k)))
                VALID.incrementAndGet();
            else
                WRONG.incrementAndGet();
        }
    }
    
    public static void runIt(Cache<Integer, Integer> c, int size) throws InterruptedException {
        prepareData();
        
        Thread[] th = new Thread[size];
        st.begin(true);
        
        for (int i = 0; i < size; ++i) {
            th[i] = new Thread(new Run(c));
            th[i].start();
        }
        
        for (int i = 0; i < size; ++i) {
            th[i].join();
        }
        st.done();
        
        System.out.println("Size: " + c.size());
        System.out.println("V " + VALID.getAndSet(0) + ", WRG " + WRONG.getAndSet(0) + ", NIL " + NIL.getAndSet(0));
        
        st.print();
    }

    /**
     * @param args
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {
        final int ARR_SIZE = 4;
        final int THREADS_SIZE = 7;
        Cache<Integer, Integer>[] ca = CacheCompareSingleThread.newArray(CACHE_SIZE, ARR_SIZE);
        
        System.out.println("\n" + THREADS_SIZE + " Threads test.\n");
        
        /**
         * 7 线程顺序插入+替换，效果：
         * 1 Sync Map 4576 >> 2 Segm Cache 1936 > 0 Conc Cache 1787 > 3 Guawa 1230.
         */
        runIdx = 0;
        System.out.println("Sequential insertion & replace.");
        for (int i = 0; i < ARR_SIZE; ++i) {
            runIt(ca[i], THREADS_SIZE);
        }
        
        ca = CacheCompareSingleThread.newArray(CACHE_SIZE, ARR_SIZE);
        /**
         * 7 线程随机乱插入(2倍数据量)，效果：
         * 2 Segm Cache 7168 >> 1 Sync Map 4140 > 0 Conc Cache 2212 >> 3 Guawa 1768
         */
        runIdx = 1;
        System.out.println("Random insertion.");
        for (int i = 0; i < ARR_SIZE; ++i) {
            runIt(ca[i], THREADS_SIZE);
        }
        
        /**
         * 7 线程随机乱查询，效果：
         * 2 Segm Cache 9803 > 0 Conc Cache 9756 > 3 Guawa 8928 >> 1 Sync Map 5464
         */
        runIdx = 2;
        System.out.println("Random query.");
        for (int i = 0; i < ARR_SIZE; ++i) {
            runIt(ca[i], THREADS_SIZE);
        }
    }

}
