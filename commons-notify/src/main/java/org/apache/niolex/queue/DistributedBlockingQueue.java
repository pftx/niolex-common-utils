/**
 * DistributedBlockingQueue.java
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
package org.apache.niolex.queue;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * The base class for DistributedBlockingQueue.
 * <ul>
 * <li>For ADD related operations, use {@link #offer(Object)} as basic method.</li>
 * <li>For Delete related operations, use {@link #poll()} as basic method.</li>
 * <li>For Block related operations, use {@link #watchQueue()} and {@link #watchQueue(long, TimeUnit)} as basic method.</li>
 * <li>For {@link #iterator()} and {@link #contains(Object)} related operation, we throw {@link UnsupportedOperationException} </li>
 * </ul>
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.2
 * @since 2016-4-19
 */
public abstract class DistributedBlockingQueue<E> extends AbstractQueue<E> implements BlockingQueue<E> {

    // ========================================================================
    // ADD related operations, use offer(E) as basic method.
    // ========================================================================

    /**
     * This is the override of super method.
     * @see java.util.concurrent.BlockingQueue#put(java.lang.Object)
     */
    @Override
    public void put(E e) throws InterruptedException {
        offer(e);
    }

    /**
     * This is the override of super method.
     * @see java.util.concurrent.BlockingQueue#offer(java.lang.Object, long, java.util.concurrent.TimeUnit)
     */
    @Override
    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        return offer(e);
    }

    // ========================================================================
    // Delete/Watch related operations, use E poll() as basic method.
    // ========================================================================

    /**
     * Attach a watch to the queue, wait for queue status changes.
     *
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    protected abstract void watchQueue() throws InterruptedException;

    /**
     * Attach a watch to the queue, wait for queue status changes.
     *
     * @param timeout the maximum time to wait
     * @param unit the time unit of the {@code timeout} argument
     * @return {@code true} if the queue status changed and {@code false} if the waiting time elapsed
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    protected abstract boolean watchQueue(long timeout, TimeUnit unit) throws InterruptedException;

    /**
     * This is the override of super method.
     * @see java.util.concurrent.BlockingQueue#take()
     */
    @Override
    public E take() throws InterruptedException {
        E e;
        while ((e = poll()) == null) {
            watchQueue();
        }
        return e;
    }

    /**
     * This is the override of super method.
     * @see java.util.concurrent.BlockingQueue#poll(long, java.util.concurrent.TimeUnit)
     */
    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        E e;
        if ((e = poll()) == null) {
            watchQueue(timeout, unit);
            e = poll();
        }
        return e;
    }

    // ========================================================================
    // BlockingQueue remaining operations.
    // ========================================================================

    /**
     * This is the override of super method.
     * @see java.util.concurrent.BlockingQueue#remainingCapacity()
     */
    @Override
    public int remainingCapacity() {
        return Integer.MAX_VALUE;
    }

    /**
     * This is the override of super method.
     * @see java.util.concurrent.BlockingQueue#drainTo(java.util.Collection)
     */
    @Override
    public int drainTo(Collection<? super E> c) {
        return drainTo(c, Integer.MAX_VALUE);
    }

    /**
     * This is the override of super method.
     * @see java.util.concurrent.BlockingQueue#drainTo(java.util.Collection, int)
     */
    @Override
    public int drainTo(Collection<? super E> c, int maxElements) {
        for (int i = 0; i < maxElements; ++i) {
            E e = poll();
            if (e != null) {
                c.add(e);
            } else {
                return i;
            }
        }

        return maxElements;
    }

    /**
     * This is the override of super method.
     * @see java.util.concurrent.BlockingQueue#contains(Object)
     */
    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException("contains(Object) not supported in DistributedBlockingQueue");
    }

    // ========================================================================
    // Collection remaining operations.
    // ========================================================================

    /**
     * This is the override of super method.
     * @see java.util.AbstractCollection#iterator()
     */
    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Iterator<E> iterator() not supported in DistributedBlockingQueue");
    }

}
