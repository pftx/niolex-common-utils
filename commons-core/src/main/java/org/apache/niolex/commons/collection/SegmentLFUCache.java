/**
 * SegmentLFUCache.java
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

/**
 * This SegmentLFUCache is much like {@link java.util.concurrent.ConcurrentHashMap}. We
 * create lots of small hash tables named as segment, all the operations are delegated
 * into segments. When we need locking, we consider a segment as a locking unit.
 * <br>
 * We use Clock-rotate algorithm to simplify LFU:<br>
 * All the cache items are stored in a circle. Every time we need a victim, we start from
 * the place we stopped last time, when the item count reaches 0, we remove it, otherwise,
 * we decrease the count.
 * <br>
 * We use lock-free technique to get item from cache, so it's super fast to read cache.
 * We only use lock when update cache.
 * 
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 2.1.2
 * @since May 30, 2016
 * @param <K> the key type
 * @param <V> the value type
 */
public class SegmentLFUCache<K, V> implements Cache<K, V> {
    
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
        
        /**
         * The key hash code.
         */
        private final int hash;
        private final K key;
        private volatile V value;
        
        /**
         * The number of visits of this entry.
         */
        private volatile int visits;
        
        /**
         * The map entry linked list.
         */
        private ItemEntry<K, V> mapPrev;
        private volatile ItemEntry<K, V> mapNext;
        
        /**
         * The LFU linked list.
         */
        private ItemEntry<K, V> linkPrev;
        private ItemEntry<K, V> linkNext;
        
        public ItemEntry() {
            this.key = null;
            this.value = null;
            this.hash = -1;
            this.visits = 0;
            this.mapPrev = this.mapNext = this;
            this.linkPrev = this.linkNext = null;
        }
        
        public ItemEntry(K key, V value, int hash) {
            super();
            this.key = key;
            this.hash = hash;
            this.visits = 1;
            // Set volatile last, as barrier.
            this.value = value;
        }
        
    }
    
    /**
     * A segment is considered as a LFUHashMap. except than we do special optimization
     * for map read as lock free operation.
     * 
     * <p>We use stub object for map header and LFU link head, so we do not need to consider
     * the null link problem when add item into map and remove item from map.
     * 
     * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
     * @version 2.1.2
     * @since May 30, 2016
     * @param <K> the key type
     * @param <V> the value type
     */
    protected static class Segment<K, V> {
        
        /**
         * Use this lock to guard operations on map and LFU linked list.
         */
        private final Lock w = new ReentrantLock();
        
        /**
         * The main table used to store hash entry.
         */
        private final ItemEntry<K, V>[] table;
        
        /**
         * The LFU linked list head.
         */
        private final ItemEntry<K, V> LFUHead;
        
        /**
         * The segment entry mask.
         */
        private final int entryMask;
        
        /**
         * The number of items in this segment.
         */
        private volatile int itemSize;
        
        /**
         * Construct a segment with the specified entry size.
         * 
         * @param entrySize the segment entry size
         */
        @SuppressWarnings("unchecked")
        public Segment(int entrySize) {
            if ((entrySize & (entrySize - 1)) != 0) {
                throw new IllegalArgumentException("Invlid entry size.");
            }
            
            this.itemSize = 0;
            this.entryMask = entrySize - 1;
            table = new ItemEntry[entrySize];
            
            for (int i = 0; i < entrySize; ++i) {
                table[i] = new ItemEntry<K, V>();
            }
            
            this.LFUHead = new ItemEntry<K, V>();
            this.LFUHead.linkPrev = this.LFUHead.linkNext = this.LFUHead;
        }

        /**
         * Find the hash entry for the specified hash code.
         * 
         * @param hash the hash code
         * @return the hash entry head
         */
        protected final ItemEntry<K, V> entryFor(int hash) {
            return table[hash & entryMask];
        }

        /**
         * Find an item entry with the specified key from the map linked list.
         * 
         * @param hash the key hash
         * @param key the key
         * @return the item entry
         */
        protected ItemEntry<K,V> findItem(int hash, K key) {
            ItemEntry<K, V> head = entryFor(hash);
            K k;
            for (ItemEntry<K,V> e = head.mapNext; e != head; e = e.mapNext) {
                if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
                    return e;
                }
            }
            return null;
        }
        
        /**
         * Add a new Item with the specified key and value into the map.
         * 
         * @param hash the key hash
         * @param key the key
         * @param value the value
         */
        protected final void addNewItemUnderLock(int hash, K key, V value) {
            ItemEntry<K, V> head = entryFor(hash);
            ItemEntry<K,V> e = new ItemEntry<K,V>(key, value, hash);
            
            // Link map.
            e.mapPrev = head;
            e.mapNext = head.mapNext;
            head.mapNext.mapPrev = e;
            head.mapNext = e;
            
            // Link LFU.
            addItemIntoLinkAfterThisUnderLock(e, LFUHead);
            
            ++itemSize;
        }
        
        /**
         * Link the specified item into the LFU linked list after the specified item.
         * 
         * @param e the new item to be inserted into list
         * @param after the item used to insert the new item after this one
         */
        protected final void addItemIntoLinkAfterThisUnderLock(ItemEntry<K,V> e, ItemEntry<K,V> after) {
            e.linkPrev = after;
            e.linkNext = after.linkNext;
            after.linkNext.linkPrev = e;
            after.linkNext = e;
        }
        
        /**
         * Remove the specified item from this map.
         * 
         * @param e the map item to be removed
         */
        protected final void removeItemFromMapUnderLock(ItemEntry<K,V> e) {
            e.mapPrev.mapNext = e.mapNext;
            e.mapNext.mapPrev = e.mapPrev;
            
            --itemSize;
        }
        
        /**
         * Remove the specified item from the related LFU list.
         * 
         * @param e the LFU linked item to be removed
         */
        protected final void removeItemFromLinkUnderLock(ItemEntry<K,V> e) {
            e.linkPrev.linkNext = e.linkNext;
            e.linkNext.linkPrev = e.linkPrev;
        }
        
        /**
         * Put the value with the specified key into this map.
         * 
         * @param hash the key hash
         * @param key the key
         * @param value the value
         * @return the old value if item exists, {@code null} if not found
         */
        protected V put(int hash, K key, V value) {
            w.lock();
            try {
                ItemEntry<K,V> e = findItem(hash, key);
                if (e == null) {
                    // Add new item.
                    addNewItemUnderLock(hash, key, value);
                    return null;
                }
                
                // Modify existing item.
                V old_value = e.value;
                ++e.visits;
                e.value = value;
                
                return old_value;
            } finally {
                w.unlock();
            }
        }
        
        /**
         * Remove the item with the specified key from the map if found.
         * 
         * @param hash the key hash
         * @param key the key
         * @return the value if item exists, {@code null} if not found
         */
        protected V remove(int hash, K key) {
            w.lock();
            try {
                ItemEntry<K,V> e = findItem(hash, key);
                if (e == null) {
                    return null;
                }
                
                // Item found, remove it.
                removeItemFromMapUnderLock(e);
                removeItemFromLinkUnderLock(e);
                return e.value;
            } finally {
                w.unlock();
            }
        }
        
        /**
         * @return the number of items in this segment.
         */
        protected int size() {
            return itemSize;
        }
        
        /**
         * Find a victim by LFU and remove it from this map.
         * Every call to this method will traverse at most 1/3 items in this segment.
         * 
         * @return the number of victim evicted by this operation
         */
        protected int eviction() {
            w.lock();
            try {
                ItemEntry<K,V> tail = LFUHead.linkPrev;
                int batchSize = itemSize / 3 + 3;
                int visits = 0;
                
                while (tail != LFUHead) {
                    if (++visits > batchSize) {
                        // Visit too much, we stop and put head here, so the visited item will not be visited again in the next eviction.
                        
                        // Remove head.
                        removeItemFromLinkUnderLock(LFUHead);
                        // Re-add head.
                        addItemIntoLinkAfterThisUnderLock(LFUHead, tail);
                        
                        return 0;
                    }
                    
                    // Check entry visit count.
                    if (--tail.visits <= 0) {
                        // Victim found, remove it.
                        removeItemFromMapUnderLock(tail);
                        
                        // After remove from map, we need to remove it from LFU linked list.
                        if (tail.linkNext == LFUHead) {
                            // In this case, we just found it the first time, do not need to re-assign head.
                            removeItemFromLinkUnderLock(tail);
                        } else {
                            // In this case, we remove head, and then put head to replace tail in place.
                            // Remove head.
                            removeItemFromLinkUnderLock(LFUHead);
                            
                            // We use LFUHead to replace tail.
                            LFUHead.linkPrev = tail.linkPrev;
                            LFUHead.linkNext = tail.linkNext;
                            // We remove item from linked list manually. 
                            tail.linkPrev.linkNext = LFUHead;
                            tail.linkNext.linkPrev = LFUHead;
                        }
                        return 1;
                    }
                    
                    tail = tail.linkPrev;
                }
            } finally {
                w.unlock();
            }
            return 0;
        }
        
    }
    
    /**
     * The maximum number of segments to allow; used to bound
     * constructor arguments.
     */
    protected static final int MAX_SEGMENTS = 1 << 14; // slightly conservative
    
    /**
     * The minimum number of items stores into one hash segment.
     */
    protected static final int MIN_TABLE_ITEM = 1 << 7; // slightly conservative

    /**
     * Use this integer to store size of this cache.
     */
    private final AtomicInteger size = new AtomicInteger();
    
    /**
     * The next victim segment used for finding victim.
     */
    private final AtomicInteger nextVictimSegment = new AtomicInteger();
    
    /**
     * The max KV records size.
     */
    private final int maxSize;

    /**
     * The segment table used to store all the segments.
     */
    private final Segment<K, V>[] segmentTable;
    
    /**
     * Mask value for indexing into segments. The upper bits of a
     * key's hash code are used to choose the segment.
     */
    private final int segmentMask;

    /**
     * Shift value for indexing within segments.
     */
    private final int segmentShift;

    /**
     * Applies a supplemental hash function to a given hashCode, which
     * defends against poor quality hash functions.  This is critical
     * because SegmentLFUCache uses power-of-two length hash tables,
     * that otherwise encounter collisions for hashCodes that do not
     * differ in lower or upper bits.
     * 
     * <p> copied from ConcurrentHashMap.
     */
    private static int hash(int h) {
        // Spread bits to regularize both segment and index locations,
        // using variant of single-word Wang/Jenkins hash.
        h += (h <<  15) ^ 0xffffcd7d;
        h ^= (h >>> 10);
        h += (h <<   3);
        h ^= (h >>>  6);
        h += (h <<   2) + (h << 14);
        return h ^ (h >>> 16);
    }

    /**
     * Returns the segment that should be used for key with given hash.
     * 
     * @param hash the hash code for the key
     * @return the segment
     */
    private final Segment<K,V> segmentFor(int hash) {
        return segmentTable[(hash >>> segmentShift) & segmentMask];
    }

    /**
     * Creates a new, empty SegmentLFUCache. The {@literal maxSize} must greater than 2048.
     * 
     * @param maxSize the max number of cache items to store
     * @param concurrencyLevel the estimated number of concurrently updating threads.
     *  The implementation performs internal sizing to try to accommodate this many threads.
     *  We may not use you parameter if it's too small or too big.
     * @throws IllegalArgumentException if the {@literal maxSize} is too small
     */
    @SuppressWarnings("unchecked")
    public SegmentLFUCache(int maxSize, int concurrencyLevel) {
        if (concurrencyLevel > MAX_SEGMENTS)
            concurrencyLevel = MAX_SEGMENTS;
        else if (concurrencyLevel < 16) {
            concurrencyLevel = 16;
        }
        
        // Find power-of-two sizes best matching arguments
        int sshift = 0;
        int ssize = 1;
        while (ssize < concurrencyLevel) {
            ++sshift;
            ssize <<= 1;
        }
        
        int esize = MIN_TABLE_ITEM;
        int emax = (int)(maxSize / (ssize * 0.75)) + 1;
        if (emax < esize) {
            throw new IllegalArgumentException("The parameter 'maxSize' must greater than " + ssize * esize);
        }
        while (esize < emax) {
            esize <<= 1;
        }
        
        this.segmentShift = 32 - sshift;
        this.segmentMask = ssize - 1;
        this.maxSize = maxSize;
        this.segmentTable = new Segment[ssize];
        
        for (int i = 0; i < ssize; ++i) {
            this.segmentTable[i] = new Segment<K, V>(esize);
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
     * This is the override of super method.
     * @see org.apache.niolex.commons.collection.Cache#get(java.lang.Object)
     */
    @Override
    public V get(K key) {
        if (key == null) {
            throw new NullPointerException("The parameter 'key' should not be null.");
        }
        
        int hash = hash(key.hashCode());
        ItemEntry<K,V> e = segmentFor(hash).findItem(hash, key);
        if (e != null) {
            ++e.visits;
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
        V v = segmentFor(hash).put(hash, key, value);
        if (v == null && size.incrementAndGet() > maxSize) {
            // A new item added. Check max size failed. So we need to find a victim.
            int delta = 0;
            do {
                int idx = nextVictimSegment.getAndIncrement();
                Segment<K,V> s = segmentTable[idx & segmentMask];
                delta =s.eviction();
            } while (delta == 0);
            size.addAndGet(-delta);
        }
        return v;
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
        V v = segmentFor(hash).remove(hash, key);
        if (v != null) {
            size.decrementAndGet();
        }
        return v;
    }
    
}
