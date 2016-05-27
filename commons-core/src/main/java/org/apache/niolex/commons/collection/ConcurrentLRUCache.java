/**
 * ConcurrentLRUCache.java
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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.niolex.commons.test.Check;

/**
 * <br>
 * We use 3Q algorithm to simplify LRU:<br>
 * The single LRU list is split to three parts. Every part is compared with it's header.
 * If it's visited after the header creation, then is's kept during finding victim.
 * Otherwise, we remove it and place a new item in to the list.
 * <br>
 * We use lock-free technique to get item from cache, so it's super fast to read cache.
 * We only add lock when update cache.
 * 
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 2.1.2
 * @since May 26, 2016
 */
public class ConcurrentLRUCache<K, V> implements Cache<K, V> {
    
    /**
     * The ItemEntry class used to store KV data into a hash table.
     * 
     * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
     * @version 2.1.2
     * @since May 26, 2016
     * @param <K> the key type
     * @param <V> the value type
     */
    protected static class ItemEntry<K, V> {
        private K key;
        private volatile V value;
        
        /**
         * The key hash code.
         */
        private int hash;
        
        /**
         * The last visit time of this entry.
         */
        private long lastVisitAt;
        
        /**
         * The map entry linked list.
         */
        private ItemEntry<K, V> mapPrev;
        private volatile ItemEntry<K, V> mapNext;
        
        /**
         * The LRU linked list.
         */
        private ItemEntry<K, V> linkPrev;
        private ItemEntry<K, V> linkNext;
    }
    
    /**
     * The table entry class, used to store linked list header and the lock to guard operations
     * on this list.
     * 
     * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
     * @version 2.1.2
     * @since May 26, 2016
     * @param <K> the key type
     * @param <V> the value type
     */
    protected static class TableEntry<K, V> {
        private final Lock w = new ReentrantLock();
        private volatile ItemEntry<K, V> head = null;
    }
    
    /**
     * The 3Q algorithm implementation.
     * 
     * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
     * @version 2.1.2
     * @since May 27, 2016
     * @param <K> the key type
     * @param <V> the value type
     */
    protected static class ThreeQLRUList<K, V> {
        
        /**
         * Use this lock to guard operations on the LRU linked list.
         */
        private final Lock linkLock = new ReentrantLock();
        
        /**
         * The size walk through the list.
         */
        private int walkThroughSize = 0;
        
        /**
         * The LRU linked list head.
         */
        private ItemEntry<K, V> head = null;
        
        /**
         * The LRU linked list tail.
         */
        private ItemEntry<K, V> tail = null;
        
        /**
         * The last round header time. entries elder than this time will be replaced.
         */
        private volatile long lastRoundHeaderTime = 0;
        private volatile long middleRoundHeaderTime = 0;
        private volatile long firstRoundHeaderTime = 0;
        
        /**
         * Push the new header time.
         * 
         * @param time the new header time
         */
        public void pushHeaderTime(long time) {
            lastRoundHeaderTime = middleRoundHeaderTime;
            middleRoundHeaderTime = firstRoundHeaderTime;
            firstRoundHeaderTime = time;
        }
        
        /**
         * Find a victim. If we found one, we will remove it from the LRU list.
         * 
         * @return the victim
         */
        public ItemEntry<K, V> findVictim(int victimSize) {
            linkLock.lock();
            try {
                ItemEntry<K, V> cur = tail;
                long time = lastRoundHeaderTime;
                while (cur != null) {
                    if (cur.lastVisitAt <= time) {
                        // Victim found. Deal with re-construct the list.
                        
                        // 1. Paste the walked list to the head.
                        tail.linkNext = head;
                        head.linkPrev = tail;
                        head = cur.linkNext;
                        cur.linkNext.linkPrev = null;
                        
                        // 2. Re construct the tail.
                        tail = cur.linkPrev;
                        cur.linkPrev.linkNext = null;
                        
                        // 3. Return victim.
                        cur.linkPrev = cur.linkNext = null;
                        return cur;
                    }
                    ++walkThroughSize;
                    // If we waking too long away, we need to update the victim compare time.
                    if (walkThroughSize >= victimSize) {
                        walkThroughSize = 0;
                        pushHeaderTime(cur.lastVisitAt);
                        time = lastRoundHeaderTime;
                    }
                    cur = cur.linkPrev;
                }
                
                // Can not find any victim.
                return null;
            } finally {
                linkLock.unlock();
            }
        }
        
        /**
         * Add an entry to the head of the LRU list.
         * 
         * @param cur the current entry to be added
         */
        public void addEntry(ItemEntry<K, V> cur) {
            linkLock.lock();
            try {
                cur.linkPrev = null;
                cur.linkNext = head;
                if (head != null)
                    head.linkPrev = cur;
                head = cur;
                if (tail == null)
                    tail = cur;
            } finally {
                linkLock.unlock();
            }
        }
        
        /**
         * Remove this entry from the LRU list.
         * 
         * @param cur the current entry to be removed
         */
        public void removeEntry(ItemEntry<K, V> cur) {
            linkLock.lock();
            try {
                if (cur.linkPrev == null && cur.linkNext == null) {
                    if (cur == head) {
                        head = tail = null;
                    }
                    // Already removed, just return.
                    return;
                }
                
                if (cur == head) {
                    head = cur.linkNext;
                    head.linkPrev = null;
                } else if (cur == tail) {
                    tail = cur.linkPrev;
                    tail.linkNext = null;
                } else {
                    cur.linkNext.linkPrev = cur.linkPrev;
                    cur.linkPrev.linkNext = cur.linkNext;
                }
                
                cur.linkPrev = cur.linkNext = null;
            } finally {
                linkLock.unlock();
            }
        }
    }

    /**
     * Applies a supplemental hash function to a given hashCode, which defends against poor
     * quality hash functions. <p> This is copied from JDK's HashMap.</p>
     * 
     * @param h the old hash code
     * @return the new hash code
     */
    static int hash(int h) {
        // This function ensures that hashCodes that differ only by
        // constant multiples at each bit position have a bounded
        // number of collisions (approximately 8 at default load factor).
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }
    
    /**
     * Returns index for hash code h.
     * 
     * @param h the hash code
     * @param length the table length
     * @return the hash entry index
     */
    static int indexFor(int h, int length) {
        return h > 0 ? (h % length) : -(h % length);
    }
    
    /**
     * Use this integer to store size of this cache.
     */
    private final AtomicInteger size = new AtomicInteger();
    
    /**
     * Use this integer to record visits.
     */
    private final AtomicInteger visitTime = new AtomicInteger();

    /**
     * The 3Q list used to store all the entries.
     */
    private final ThreeQLRUList<K, V> lruList = new ThreeQLRUList<K, V>();
    
    /**
     * The max KV records size.
     */
    private final int maxSize;
    
    /**
     * The main table entry size.
     */
    private final int entrySize;
    
    /**
     * The size of victim batch. Used in 3Q algorithm.
     */
    private final int victimSize;
    
    /**
     * The main table used to store hash entry.
     */
    private final TableEntry<K, V>[] table;

    /**
     * Create a ConcurrentLRUCache which can store as many as {@code maxSize} cache items.
     * 
     * @param maxSize the max number of cache items
     */
    @SuppressWarnings("unchecked")
    public ConcurrentLRUCache(int maxSize) {
        Check.lt(50, maxSize, "The parameter 'maxSize' must greater than 50");
        this.maxSize = maxSize;
        this.entrySize = (int) (maxSize / 0.75);
        victimSize = (maxSize - 4) / 3;
        table = new TableEntry[entrySize];
        
        for (int i = 0; i < entrySize; ++i) {
            table[i] = new TableEntry<K, V>();
        }
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.collection.Cache#size()
     */
    @Override
    public int size() {
        return size.get();
    }

    /**
     * When get, we only update the last visit time. We will not reorder the LRU list here.
     * So it's completely lock-free.
     * 
     * This is the override of super method.
     * @see org.apache.niolex.commons.collection.Cache#get(java.lang.Object)
     */
    @Override
    public V get(K key) {
        if (key == null) {
            throw new NullPointerException("The parameter 'key' should not be null.");
        }
        int hash = hash(key.hashCode());
        TableEntry<K, V> en = table[indexFor(hash, entrySize)];
        
        // We use lock-free technique to visit map linked list.
        ItemEntry<K,V> e = findItemFromMapEntry(en, hash, key);
        if (e != null) {
            e.lastVisitAt = System.currentTimeMillis();
            addVisit();
            return e.value;
        }
        
        return null;
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.collection.Cache#put(java.lang.Object, java.lang.Object)
     */
    @Override
    public V put(K key, V value) {
        if (key == null) {
            throw new NullPointerException("The parameter 'key' should not be null.");
        }
        if (value == null) {
            throw new NullPointerException("The parameter 'value' should not be null.");
        }
        
        int hash = hash(key.hashCode());
        TableEntry<K, V> en = table[indexFor(hash, entrySize)];
        ItemEntry<K,V> e = null;
        // The whole operation must be done under lock.
        en.w.lock();
        try {
            // 1. Find the item in the map.
            e = findItemFromMapEntry(en, hash, key);
            if (e != null) {
                addVisit();
                e.lastVisitAt = System.currentTimeMillis();
                V o = e.value;
                // This is volatile, so instruction reorder can not happen.
                e.value = value;
                return o;
            }
            
            // 2. Not found. We create a new item.
            e = new ItemEntry<K,V>();
         
            // 3. Set key and value.
            e.key = key;
            e.lastVisitAt = System.currentTimeMillis();
            e.hash = hash;
            e.value = value;
            
            // 4. Put the item into 3Q list.
            lruList.addEntry(e);
            
            // 5. Put the item into map.
            e.mapNext = en.head;
            e.mapPrev = null;
            if (en.head != null)
                en.head.mapPrev = e;
            en.head = e;
        } finally {
            en.w.unlock();
        }
        
        // If we are here, the linked list is ready, but we still need to check the capacity.
        addVisit();
        if (size.incrementAndGet() > maxSize) {
            // Capacity exceeded, too many items. We need to pick a victim.
            e = lruList.findVictim(victimSize);
            if (e != null) {
                // Delete victim from map.
                int h2 = hash(e.key.hashCode());
                en = table[indexFor(h2, entrySize)];
                
                // Operated under lock.
                en.w.lock();
                try {
                    // Check whether this item was already removed.
                    if (en.head == e || e.mapPrev.mapNext == e) {
                        removeEntryFromMap(en, e);
                        size.decrementAndGet();
                    }
                } finally {
                    en.w.unlock();
                }
            }
        }
        
        return null;
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.collection.Cache#remove(java.lang.Object)
     */
    @Override
    public V remove(K key) {
        if (key == null) {
            throw new NullPointerException("The parameter 'key' should not be null.");
        }
        
        int hash = hash(key.hashCode());
        TableEntry<K, V> en = table[indexFor(hash, entrySize)];
        // We must operate map linked list under lock.
        en.w.lock();
        try {
            // 1. Find the item in the map.
            ItemEntry<K,V> e2 = findItemFromMapEntry(en, hash, key);
            
            // 2. Remove item.
            if (e2 != null) {
                removeEntryFromMap(en, e2);
                lruList.removeEntry(e2);
                size.decrementAndGet();
                return e2.value;
            }
        } finally {
            en.w.unlock();
        }
        
        return null;
    }
    
    /**
     * Remove the specified entry from map.
     * 
     * @param en the table entry
     * @param e2 the hash entry
     */
    protected void removeEntryFromMap(TableEntry<K, V> en, ItemEntry<K,V> e2) {
        /**
         * Must operate carefully, because we are using lock-free read. The both links of e2 will
         * not be changed in this method.
         */
        // First, we link the previous item to the next item.
        if (e2.mapPrev == null) {
            en.head = e2.mapNext;
        } else {
            e2.mapPrev.mapNext = e2.mapNext;
        }
        
        // Second, we link the next item to the previous item.
        if (e2.mapNext != null) {
            e2.mapNext.mapPrev = e2.mapPrev;
        }
    }
    
    /**
     * Find an item entry with the specified key from the map linked list.
     * 
     * @param en the table entry
     * @param hash the key hash
     * @param key the key
     * @return the item entry
     */
    protected ItemEntry<K,V> findItemFromMapEntry(TableEntry<K, V> en, int hash, K key) {
        K k;
        for (ItemEntry<K,V> e = en.head; e != null; e = e.mapNext) {
            if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
                return e;
            }
        }
        return null;
    }
    
    /**
     * A new visit to this cache.
     */
    protected void addVisit() {
        if (visitTime.incrementAndGet() == victimSize) {
            visitTime.set(0);
            lruList.pushHeaderTime(System.currentTimeMillis());
        }
    }
    
}
