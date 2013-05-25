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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * The implementation of ConsistentHash.
 * This experiment shows that a figure of one or two hundred replicas achieves
 * an acceptable balance (a standard deviation that is roughly between 5% and 10% of the mean).
 *
 * It was originally copied from Tom White's implementation found here:
 * https://weblogs.java.net/blog/tomwhite/archive/2007/11/consistent_hash.html
 *
 * @author original version by Tom White, enhanced by <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-5-11
 * @param <T>
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
     * A Hash funtion implementation by JVM default hash method.
     *
     * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
     * @version 1.0.0
     * @since 2013-5-19
     */
    public static class JVMHash implements HashFunction {
        public static final JVMHash INSTANCE = new JVMHash();

        /**
         * This is the override of super method.
         * @see org.apache.niolex.commons.util.ConsistentHash.HashFunction#hashCode(java.lang.Object)
         */
        @Override
        public int hashCode(Object o) {
            return o.hashCode();
        }

        /**
         * This is the override of super method.
         * @see org.apache.niolex.commons.util.ConsistentHash.HashFunction#hashCode(java.lang.Object, int)
         */
        @Override
        public int hashCode(Object o, int seed) {
            return (seed + o.toString()).hashCode();
        }

    }

    private final HashFunction hashFunction;
    private final int numberOfReplicas;
    private final TreeMap<Integer, T> circle = new TreeMap<Integer, T>();


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
     * Create a ConsistentHash with default JVM hash.
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
     * Create a ConsistentHash with default JVM hash.
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
     * Prepare the hash ring.
     *
     * @param nodes the nodes to be prepared into the hash ring
     */
    public void prepare(Collection<T> nodes) {
        for (T node : nodes) {
            add(node);
        }
    }

    /**
     * Add this node into the hash ring.
     *
     * @param node the node to be added
     */
    public synchronized void add(T node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            circle.put(hashFunction.hashCode(node, i), node);
        }
    }

    /**
     * Remove this node from the hash ring.
     *
     * @param node the node to be removed
     */
    public synchronized void remove(T node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            circle.remove(hashFunction.hashCode(node, i));
        }
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
     * @param numberOfNodes the number of different nodes needed
     * @return the node list
     */
    public Collection<T> getNodeList(Object key, final int numberOfNodes) {
        HashSet<T> set = new HashSet<T>();
        if (circle.isEmpty()) {
            return set;
        }
        int hash = hashFunction.hashCode(key);
        // Find the next entry.
        SortedMap<Integer, T> tail = circle.tailMap(hash);
        Iterator<T> iter = tail.values().iterator();
        boolean isTail = true;
        while (set.size() < numberOfNodes) {
            if (iter.hasNext()) {
                set.add(iter.next());
            } else {
                if (isTail) {
                    iter = circle.values().iterator();
                    isTail = false;
                } else {
                    throw new IllegalArgumentException("There are only " + set.size() + " different nodes, but request "
                            + numberOfNodes + " in total.");
                }
            }
        }
        return set;
    }

}
