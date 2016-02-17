/**
 * ConsistentHash.java
 *
 * Copyright 2013 the original author or authors.
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
package org.apache.niolex.commons.hash;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import com.google.common.hash.Hashing;

/**
 * The implementation of ConsistentHash.
 * This experiment shows that a figure of one or two hundred replicas achieves
 * an acceptable balance (a standard deviation that is roughly between 5% and 10% of the mean).
 *
 * This class is mean for use in high concurrency, Applications developers need to call prepare
 * to supply all the nodes(or supply in the constructor). And call add or remove at runtime
 * to adjust the hash ring. We will copy the whole hash ring in add &amp; remove method to achieve
 * high concurrency without using any lock.
 *
 * It was originally copied from Tom White's implementation found here:
 * https://weblogs.java.net/blog/tomwhite/archive/2007/11/consistent_hash.html
 *
 * @param <T> the server node type
 * @author original version by Tom White, enhanced by <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-5-11
 */
public class ConsistentHash<T> {

    /**
     * The hash function interface.
     *
     * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
     * @version 1.0.0
     * @since 2013-5-19
     */
    public static interface HashFunction {

        /**
         * The hash method. take the object as input, and return hash code of it.
         *
         * @param o the object to be hashed
         * @return the hash code
         */
        public int hashCode(Object o);

        /**
         * The hash method. take the object and seed as input, and return hash code of it.
         *
         * @param o the object to be hashed
         * @param seed the seed to affect the result
         * @return the hash code
         */
        public int hashCode(Object o, int seed);
    }

    /**
     * A Hash function implementation by JVM default hash method.
     *
     * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
     * @version 1.0.0
     * @since 2013-5-19
     */
    public static class JVMHash implements HashFunction {
        public static final JVMHash INSTANCE = new JVMHash();

        /**
         * This is the override of super method.
         * @see org.apache.niolex.commons.hash.ConsistentHash.HashFunction#hashCode(java.lang.Object)
         */
        @Override
        public int hashCode(Object o) {
            return o.hashCode();
        }

        /**
         * This is the override of super method.
         * @see org.apache.niolex.commons.hash.ConsistentHash.HashFunction#hashCode(java.lang.Object, int)
         */
        @Override
        public int hashCode(Object o, int seed) {
            StringBuilder sb = new StringBuilder();
            sb.append(seed).append(o).append(seed);
            return sb.toString().hashCode();
        }

    }

    /**
     * A Hash function implementation Using guava murmur3_32 as hash method, and use toString to
     * digest the object.
     *
     * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
     * @version 1.0.0
     * @since 2013-5-25
     */
    public static class GuavaHash implements HashFunction {
        public static final GuavaHash INSTANCE = new GuavaHash();

        /**
         * This is the override of super method.
         * @see org.apache.niolex.commons.hash.ConsistentHash.HashFunction#hashCode(java.lang.Object)
         */
        @Override
        public int hashCode(Object o) {
            return Hashing.murmur3_32().hashString(o.toString()).asInt();
        }

        /**
         * This is the override of super method.
         * @see org.apache.niolex.commons.hash.ConsistentHash.HashFunction#hashCode(java.lang.Object, int)
         */
        @Override
        public int hashCode(Object o, int seed) {
            return Hashing.murmur3_32(seed).hashString(o.toString()).asInt();
        }

    }

    /**
     * The hash function
     */
    private final HashFunction hashFunction;

    /**
     * The number of replicas per node
     */
    private final int numberOfReplicas;

    /**
     * The hash ring
     */
    private volatile TreeMap<Integer, T> circle = new TreeMap<Integer, T>();


    /**
     * Create a ConsistentHash with default JVM hash and 100 replicas per node.
     */
    public ConsistentHash() {
        this(100);
    }

    /**
     * Create a ConsistentHash with default JVM hash.
     *
     * @param numberOfReplicas the number of replicas per node
     */
    public ConsistentHash(int numberOfReplicas) {
        this(JVMHash.INSTANCE, numberOfReplicas);
    }

    /**
     * Create a ConsistentHash with the specified hash function and number of replicas.
     *
     * @param hashFunction the hash function
     * @param numberOfReplicas the number of replicas per node
     */
    public ConsistentHash(HashFunction hashFunction, int numberOfReplicas) {
        super();
        this.hashFunction = hashFunction;
        this.numberOfReplicas = numberOfReplicas;
    }

    /**
     * Create a ConsistentHash with the specified hash function and number of replicas.
     *
     * @param hashFunction the hash function
     * @param numberOfReplicas the number of replicas per node
     * @param nodes the nodes to be prepared into the hash ring
     */
    public ConsistentHash(HashFunction hashFunction, int numberOfReplicas, Collection<T> nodes) {
        this(hashFunction, numberOfReplicas);

        prepare(nodes);
    }

    /**
     * Prepare the hash ring.<br>
     * This method must be called before using the hash ring.
     *
     * @param nodes the nodes to be prepared into the hash ring
     */
    public void prepare(Collection<T> nodes) {
        for (T node : nodes) {
            internalAdd(circle, node);
        }
    }

    /**
     * Prepare the hash ring.<br>
     * This method must be called before using the hash ring.
     *
     * @param nodes the nodes to be prepared into the hash ring
     */
    public void prepare(T ...nodes) {
        for (T node : nodes) {
            internalAdd(circle, node);
        }
    }

    /**
     * Add this node into the hash ring at runtime.<br>
     * We will clone the hash ring inside this method, so we will need more
     * memory and create more objects. It's better to call prepare to add all the nodes
     * before start. And use this method only after the system is running.
     *
     * @param node the node to be added
     */
    public synchronized void add(T node) {
        // We clone the hash ring.
        @SuppressWarnings("unchecked")
        TreeMap<Integer, T> ring = (TreeMap<Integer, T>) circle.clone();
        internalAdd(ring, node);
        // We replace the old hash ring with this new ring.
        this.circle = ring;
    }

    /**
     * Add this node into the hash ring.
     *
     * @param ring the hash ring
     * @param node the node to be added
     */
    private void internalAdd(TreeMap<Integer, T> ring, T node) {
        final int START = findStart(node);

        for (int i = START; i < numberOfReplicas + START; ++i) {
            ring.put(hashFunction.hashCode(node, i), node);
        }
    }

    /**
     * Remove this node from the hash ring at runtime.<br>
     * We will clone the hash ring inside this method, in exchange for high
     * concurrency.
     *
     * @param node the node to be removed
     */
    public synchronized void remove(T node) {
        // We clone the hash ring.
        @SuppressWarnings("unchecked")
        TreeMap<Integer, T> ring = (TreeMap<Integer, T>) circle.clone();
        final int START = findStart(node);

        for (int i = START; i < numberOfReplicas + START; ++i) {
            ring.remove(hashFunction.hashCode(node, i));
        }
        // We replace the old hash ring with this new ring.
        this.circle = ring;
    }

    /**
     * Find the start index to add or remove this node.
     *
     * @param node the node to be added or removed
     * @return the start index
     */
    private int findStart(T node) {
        final int START = hashFunction.hashCode(node);
        return (START > Integer.MAX_VALUE - numberOfReplicas) ? START - numberOfReplicas * 79 : START;
    }

    /**
     * Get the node for this key.
     *
     * @param key the key
     * @return the node next to this key in the hash ring
     */
    public T getNode(Object key) {
        if (circle.isEmpty()) {
            return null;
        }
        int hash = hashFunction.hashCode(key);
        // Find the next entry.
        Entry<Integer, T> en = circle.ceilingEntry(hash);
        en = en == null ? circle.firstEntry() : en;
        return en.getValue();
    }


    /**
     * Get the node list for this key.
     *
     * @param key the key
     * @return the node list, which contains only two nodes
     */
    public List<T> getNodeList(Object key) {
        return getNodeList(key, 2);
    }

    /**
     * Get the node list for this key.
     *
     * @param key the key
     * @param numberOfNodes the number of different nodes needed
     * @return the node list
     */
    public List<T> getNodeList(Object key, final int numberOfNodes) {
        HashSet<T> set = new HashSet<T>(numberOfNodes);
        List<T> list = new ArrayList<T>(numberOfNodes);
        if (circle.isEmpty()) {
            return list;
        }
        int hash = hashFunction.hashCode(key);
        // Find the next entry.
        SortedMap<Integer, T> tail = circle.tailMap(hash);
        Iterator<T> iter = tail.values().iterator();
        boolean isTail = true;
        while (set.size() < numberOfNodes) {
            if (iter.hasNext()) {
                T t = iter.next();
                if (set.add(t)) {
                    list.add(t);
                }
            } else {
                if (isTail) {
                    iter = circle.values().iterator();
                    isTail = false;
                } else {
                    throw new IllegalStateException("There are only " + set.size() + " different nodes, but request "
                            + numberOfNodes + " in total.");
                }
            }
        }
        return list;
    }

}
