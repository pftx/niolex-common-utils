/**
 * CacheCompareSingleThread.java
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

import java.util.Collections;
import java.util.Map;

import org.apache.niolex.commons.test.Check;
import org.apache.niolex.commons.test.MockUtil;

import com.google.common.cache.CacheBuilder;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 2.1.2
 * @since May 27, 2016
 */
public class CacheCompareSingleThread {
    
    public static void put(Cache<String, Integer> c, int size) {
        for (int i = 0x100000; i < 0x100000 + size; ++i) {
            c.put(Integer.toHexString(i), i + 3);
        }
    }
    
    public static Integer getValue(int k) {
        return k * 3 + 5;
    }
    
    public static void replace(Cache<String, Integer> c, int size) {
        for (int i = 0x100000; i < 0x100000 + size; ++i) {
            c.put(Integer.toHexString(i), getValue(i));
        }
    }
    
    public static void rand(Cache<String, Integer>[] ca, final int length, int size) {
        int kks = size;
        int put = 0, get = 0;
        while (kks-- > 0) {
            int s = MockUtil.randInt(100, 200);
            if (s > 195) {
                int i = MockUtil.randInt(100, 200) + 0x100000 + size;
                for (int ss = 0; ss < length; ++ss) 
                    ca[ss].put(Integer.toHexString(i), i + 3);
                ++put;
            } else {
                int i = MockUtil.randInt(0, size) + 0x100000;
                for (int ss = 0; ss < length; ++ss) {
                    Integer k1 = ca[ss].get(Integer.toHexString(i));
                    if (k1 != null)
                        Check.eq(getValue(i), k1, "Not eq " + ss + ": " + (i + 3) + " " + k1);
                }
                ++get;
            }
        }
        System.out.println("Random put " + put + ", get " + get);
    }
    
    public static int get(Cache<String, Integer> c, int size) {
        int nul = 0;
        for (int i = 0x100000; i < 0x100000 + size; ++i) {
            Integer k = c.get(Integer.toHexString(i));
            if (k == null)
                ++nul;
            else
                Check.eq(getValue(i), k, "Not eq: " + (i + 3) + " " + k);
        }
        return nul;
    }
    
    public static int remove(Cache<String, Integer> c, int size) {
        int nul = 0;
        for (int i = 0x100000; i < 0x100000 + size; ++i) {
            Integer k = c.remove(Integer.toHexString(i));
            if (k == null)
                ++nul;
            else
                Check.eq(getValue(i), k, "Not eq: " + (i + 3) + " " + k);
        }
        return nul;
    }
    
    public static <K, V> Cache<K, V>[] newArray(final int cacheSize, final int cacheNumber) {
        @SuppressWarnings("unchecked")
        Cache<K, V>[] ca = new Cache[cacheNumber];
        
        ca[0] = new ConcurrentLRUCache<K, V>(cacheSize);
        Map<K, V> sync = Collections.synchronizedMap(new LRUHashMap<K, V>(cacheSize));
        ca[1] = MapAsCache.newInstance(sync);
        ca[2] = new SegmentLFUCache<K, V>(cacheSize, 32);
        com.google.common.cache.Cache<K, V> guawaMap = CacheBuilder.newBuilder().maximumSize(cacheSize).build();
        ca[3] = MapAsCache.newInstance(guawaMap.asMap());
        
        System.out.println("0 - ConcurrentLRUCache, 1 - Sync LRUHashMap, 2 - SegmentLRUCache, 3 - Guawa");
        
        return ca;
    }

    /**
     * @param args
     * 单线程的表现，可以看下表，我们认为1 - Sync LRUHashMap, 2 - SegmentLRUCache是比较好的。
     * 编号   插入替换查询删除
     * Conc 0   2   3   4   3
     * Map  1   3   1   3   1
     * Seg  2   1   2   1   4
     * Guawa3   4   4   2   2
     */
    public static void main(String[] args) {
        final int ARR_SIZE = 4;
        final int CACHE_SIZE = 100000;
        Cache<String, Integer>[] ca = newArray(CACHE_SIZE, ARR_SIZE);
        
        System.out.println("\nSingle Thread test.\n");
        
        System.out.println("Insertion time:");
        for (int i = 0; i < ARR_SIZE; ++i) {
            long in = System.nanoTime();
            put(ca[i], CACHE_SIZE);
            System.out.println(i + " " + (System.nanoTime() - in));
        }
        
        System.out.println("Replace time:");
        for (int i = 0; i < ARR_SIZE; ++i) {
            long in = System.nanoTime();
            replace(ca[i], CACHE_SIZE + 100);
            System.out.println(i + " " + (System.nanoTime() - in));
        }
        
        System.out.println("Size:");
        for (int i = 0; i < ARR_SIZE; ++i) {
            System.out.println(i + " " + ca[i].size());
        }
        
        rand(ca, ca.length, CACHE_SIZE);
        
        System.out.println("Query time:");
        for (int i = 0; i < ARR_SIZE; ++i) {
            long in = System.nanoTime();
            int c = get(ca[i], CACHE_SIZE);
            System.out.println(i + " " + (System.nanoTime() - in) + " MISSED: " + c);
        }
        
        System.out.println("Remove time:");
        for (int i = 0; i < ARR_SIZE; ++i) {
            long in = System.nanoTime();
            int c = remove(ca[i], CACHE_SIZE);
            System.out.println(i + " " + (System.nanoTime() - in) + " MISSED: " + c);
        }
        
        System.out.println("Size:");
        for (int i = 0; i < ARR_SIZE; ++i) {
            System.out.println(i + " " + ca[i].size());
        }
        
    }

}
