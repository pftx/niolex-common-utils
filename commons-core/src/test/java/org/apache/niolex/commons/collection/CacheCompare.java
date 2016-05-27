/**
 * CacheCompare.java
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

import org.apache.niolex.commons.collection.CachePerformance.MulLRUCache;
import org.apache.niolex.commons.test.Check;
import org.apache.niolex.commons.test.MockUtil;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 2.1.2
 * @since May 27, 2016
 */
public class CacheCompare {
    
    public static void put(Cache<String, Integer> c, int size) {
        for (int i = 0x100000; i < 0x100000 + size; ++i) {
            c.put(Integer.toHexString(i), i + 3);
        }
    }
    
    public static void rand(Cache<String, Integer> c1, Cache<String, Integer> c2, int size) {
        int kks = size;
        int put = 0, get = 0;
        while (kks-- > 0) {
            int s = MockUtil.randInt(100, 200);
            if (s > 195) {
                int i = MockUtil.randInt(100, 200) + 0x100000 + size;
                c1.put(Integer.toHexString(i), i + 3);
                c2.put(Integer.toHexString(i), i + 3);
                ++put;
            } else {
                int i = MockUtil.randInt(0, size) + 0x100000;
                Integer k1 = c1.get(Integer.toHexString(i));
                Integer k2 = c2.get(Integer.toHexString(i));
                if (k1 != null)
                    Check.eq(i + 3, k1, "Not eq 1: " + (i + 3) + " " + k1);
                if (k2 != null)
                    Check.eq(i + 3, k2, "Not eq 2: " + (i + 3) + " " + k2);
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
                Check.eq(i + 3, k, "Not eq: " + (i + 3) + " " + k);
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
                Check.eq(i + 3, k, "Not eq: " + (i + 3) + " " + k);
        }
        return nul;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        int size = 100000;
        @SuppressWarnings("unchecked")
        Cache<String, Integer>[] ca = new Cache[2];
        ca[0] = new ConcurrentLRUCache<String, Integer>(size);
        ca[1] = new MulLRUCache<String, Integer>(new LRUHashMap<String, Integer>(size));
        
        System.out.println("1 - Map, 0 - Cache");
        
        System.out.println("Insertion time:");
        for (int i = 0; i < 2; ++i) {
            long in = System.nanoTime();
            put(ca[i], size + 100);
            System.out.println(i + " " + (System.nanoTime() - in));
        }
        
        System.out.println("Size:");
        for (int i = 0; i < 2; ++i) {
            System.out.println(i + " " + ca[i].size());
        }
        
        rand(ca[0], ca[1], size);
        
        System.out.println("Query time:");
        for (int i = 0; i < 2; ++i) {
            long in = System.nanoTime();
            int c = get(ca[i], size);
            System.out.println(i + " " + (System.nanoTime() - in) + " MISSED: " + c);
        }
        
        System.out.println("Remove time:");
        for (int i = 0; i < 2; ++i) {
            long in = System.nanoTime();
            int c = remove(ca[i], size);
            System.out.println(i + " " + (System.nanoTime() - in) + " MISSED: " + c);
        }
        
        System.out.println("Size:");
        for (int i = 0; i < 2; ++i) {
            System.out.println(i + " " + ca[i].size());
        }
        
    }

}
