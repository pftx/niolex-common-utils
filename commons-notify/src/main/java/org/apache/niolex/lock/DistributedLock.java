/**
 * DistributedLock.java
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
package org.apache.niolex.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * The base class for DistributedLock. The method {@link #newCondition()} is not available. Please do not use
 * it. All other common lock methods are implemented.<p>
 *
 * The implementor need to implement {@link #initLock()}, {@link #isLockReady()}, {@link #watchLock()} and
 * {@link #releaseLock()}.<p>
 * The basic scenario is like this:
 * <pre>
 * initLock();
 * ...
 * while !isLockReady()) {
 *     watchLock();
 * }
 * ...
 * //for any kind of exception
 * releaseLock();
 * </pre>
 * For every lock method, we first use {@link #initLock()} to init lock resources. Then we try {@link #isLockReady()},
 * if this method returns true, then we already have the lock. Otherwise, we may call {@link #watchLock()} to watch for
 * the lock to be ready, or we may call {@link #releaseLock()} to release the internal resources.
 * <br>
 * The implementor should not throw any runtime <b>ERROR</b> in these three methods. But if you really want to throw,
 * you must clear the internal resources yourself.
 * <br>
 * If something wrong happened during the wait stage, throw a proper runtime exception, we will clear the internal resources.
 * <br>
 * When unlock, we will call {@link #releaseLock()} to tell you to clear the internal resources and release the lock.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2016-4-13
 */
public abstract class DistributedLock implements Lock {

    /**
     * This is the override of super method.
     * @see java.util.concurrent.locks.Lock#lock()
     */
    @Override
    public synchronized void lock() {
        initLock();

        try {
            while (!isLockReady()) {
                try {
                    watchLock();
                } catch (InterruptedException e) {
                    // If exception occurred, we ignore it.
                }
            }
        } catch (RuntimeException re) {
            // Release lock resources for any runtime exception.
            releaseLock();
            throw re;
        }
    }

    /**
     * This is the override of super method.
     * @see java.util.concurrent.locks.Lock#lockInterruptibly()
     */
    @Override
    public synchronized void lockInterruptibly() throws InterruptedException {
        initLock();

        try {
            while (!isLockReady()) {
                try {
                    watchLock();
                } catch (InterruptedException e) {
                    // If exception occurred, we need to release resources.
                    releaseLock();
                    throw e;
                }
            }
        } catch (RuntimeException re) {
            // Release lock resources for any runtime exception.
            releaseLock();
            throw re;
        }
    }

    /**
     * This is the override of super method.
     * @see java.util.concurrent.locks.Lock#tryLock()
     */
    @Override
    public synchronized boolean tryLock() {
        initLock();

        try {
            if (!isLockReady()) {
                releaseLock();
                return false;
            } else {
                return true;
            }
        } catch (RuntimeException re) {
            // Release lock resources for any runtime exception.
            releaseLock();
            throw re;
        }
    }

    /**
     * This is the override of super method.
     * @see java.util.concurrent.locks.Lock#tryLock(long, java.util.concurrent.TimeUnit)
     */
    @Override
    public synchronized boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        initLock();

        try {
            if (!isLockReady()) {
                watchLock(time, unit);
                if (isLockReady())
                    return true;
                else {
                    releaseLock();
                    return false;
                }
            } else {
                return true;
            }
        } catch (RuntimeException re) {
            // Release lock resources for any runtime exception.
            releaseLock();
            throw re;
        } catch (InterruptedException e) {
            // If exception occurred, we need to release resources.
            releaseLock();
            throw e;
        }
    }

    /**
     * This is the override of super method.
     * @see java.util.concurrent.locks.Lock#newCondition()
     */
    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException("newCondition() not supported in DistributedLock");
    }

    /**
     * This is the override of super method.
     * @see java.util.concurrent.locks.Lock#unlock()
     */
    @Override
    public synchronized void unlock() {
        releaseLock();
    }

    /**
     * Init the distributed lock, acquire resources used to record the lock.
     */
    protected abstract void initLock();

    /**
     * Check the current lock status.
     *
     * @return true if we got the lock, false if we need to wait
     */
    protected abstract boolean isLockReady();

    /**
     * Attach a watch to the lock, wait for lock status changes.
     *
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    protected abstract void watchLock() throws InterruptedException;

    /**
     * Attach a watch to the lock, wait for lock status changes.
     *
     * @param timeout the maximum time to wait
     * @param unit the time unit of the {@code timeout} argument
     * @return {@code true} if the lock status changed and {@code false} if the waiting time elapsed
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    protected abstract boolean watchLock(long timeout, TimeUnit unit) throws InterruptedException;

    /**
     * Release the internal resources used to record the lock.
     */
    protected abstract void releaseLock();

    /**
     * @return the current lock status
     */
    public abstract boolean locked();

}
