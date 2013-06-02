/**
 * DoubleHash.java
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

import org.apache.niolex.commons.bean.Pair;

import com.google.common.hash.Funnel;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

/**
 * The DoubleHash is to replace ConsistentHash in some conditions.
 * The <code>ConsistentHash</code> is using the random hash ring, so there
 * are some kind of uncertainty. In order to minimize this uncertainty, we need
 * to add many replicas for one node.<br>
 *
 * But this DoubleHash is very simple and has no uncertainty. But it can only
 * provide two candidates, which is enough in most of the times.<br>
 *
 * We use two independent hash functions to find two candidates.
 *
 * User can add and remove nodes at runtime dynamically.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-5-25
 */
public class DoubleHash<T> {

    // the primary hash function
    private final HashFunction primary;
    // the secondary hash function
    private final HashFunction secondary;
    // the server node array
    private Object[] nodeArray;

    /**
     * The only * Constructor to create this object.
     *
     * @param primary the primary hash function
     * @param secondary the secondary hash function
     * @param nodeList the server node list
     */
    public DoubleHash(HashFunction primary, HashFunction secondary, Collection<T> nodeList) {
        super();
        this.primary = primary;
        this.secondary = secondary;
        this.nodeArray = nodeList.toArray();
    }

    /**
     * Add this node into the candidate list.<br>
     * We will use Guava's consistent hash method, so only 1/n hash values will be affected by
     * adding new nodes.
     *
     * @param node the node to be added
     */
    public synchronized void add(T node) {
        Object[] tmpArray = new Object[this.nodeArray.length + 1];
        tmpArray[this.nodeArray.length] = node;
        this.nodeArray = tmpArray;
    }

    /**
     * Remove the first occurrence of this node from the candidate list at runtime.<br>
     * We will use Guava's consistent hash method, so only 1/n hash values will be affected by
     * remove this node.
     *
     * @param node the node to be removed
     */
    public synchronized void remove(T node) {
        int i = 0;
        final int length = this.nodeArray.length;
        for (; i < length; ++i) {
            if (node.equals(nodeArray[i])) {
                break;
            }
        }

        if (i != length) {
            Object[] tmpArray = new Object[length - 1];
            System.arraycopy(this.nodeArray, 0, tmpArray, 0, i);
            System.arraycopy(this.nodeArray, i + 1, tmpArray, i, tmpArray.length - i);
            this.nodeArray = tmpArray;
        }
    }

    /**
     * Get the pair of server nodes by this key. We guarantee the first and second node are
     * not the same.<br>
     *
     * @param key the key to be hashed
     * @return the pair of server nodes
     */
    @SuppressWarnings("unchecked")
    public <K> Pair<T, T> getPairNodes(String key) {
        final Object[] tmpArray = this.nodeArray;
        final int length = tmpArray.length;
        final int idx1 = Hashing.consistentHash(primary.hashString(key), length);
        int idx2 = Hashing.consistentHash(secondary.hashString(key), length - 1);
        if (idx2 >= idx1) {
            ++idx2;
        }
        return (Pair<T, T>) Pair.create(tmpArray[idx1], tmpArray[idx2]);
    }

    /**
     * Get the pair of server nodes by this key. We guarantee the first and second node are
     * not the same.<br>
     *
     * @param key the key to be hashed
     * @return the pair of server nodes
     */
    @SuppressWarnings("unchecked")
    public <K> Pair<T, T> getPairNodes(long key) {
        final Object[] tmpArray = this.nodeArray;
        final int length = tmpArray.length;
        final int idx1 = Hashing.consistentHash(primary.hashLong(key), length);
        int idx2 = Hashing.consistentHash(secondary.hashLong(key), length - 1);
        if (idx2 >= idx1) {
            ++idx2;
        }
        return (Pair<T, T>) Pair.create(tmpArray[idx1], tmpArray[idx2]);
    }

    /**
     * Get the pair of server nodes by this key. We guarantee the first and second node are
     * not the same.<br>
     *
     * @param key the key to be hashed
     * @return the pair of server nodes
     */
    @SuppressWarnings("unchecked")
    public <K> Pair<T, T> getPairNodes(int key) {
        final Object[] tmpArray = this.nodeArray;
        final int length = tmpArray.length;
        final int idx1 = Hashing.consistentHash(primary.hashInt(key), length);
        int idx2 = Hashing.consistentHash(secondary.hashInt(key), length - 1);
        if (idx2 >= idx1) {
            ++idx2;
        }
        return (Pair<T, T>) Pair.create(tmpArray[idx1], tmpArray[idx2]);
    }

    /**
     * Get the pair of server nodes by this key. We guarantee the first and second node are
     * not the same.<br>
     *
     * @param key the key to be hashed
     * @param funnel the funnel to be used
     * @return the pair of server nodes
     */
    @SuppressWarnings("unchecked")
    public <K> Pair<T, T> getPairNodes(K key, Funnel<? super K> funnel) {
        final Object[] tmpArray = this.nodeArray;
        final int length = tmpArray.length;
        final int idx1 = Hashing.consistentHash(primary.hashObject(key, funnel), length);
        int idx2 = Hashing.consistentHash(secondary.hashObject(key, funnel), length - 1);
        if (idx2 >= idx1) {
            ++idx2;
        }
        return (Pair<T, T>) Pair.create(tmpArray[idx1], tmpArray[idx2]);
    }

}
