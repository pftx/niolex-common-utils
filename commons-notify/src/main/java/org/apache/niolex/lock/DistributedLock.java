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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.apache.niolex.commons.concurrent.ThreadUtil;

/**
 * The base class for DistributedLock. The method {@link #newCondition()} is not available. Please do not use
 * it. All other common lock methods are implemented.<p>
 *
 * The implementor need to implement {@link #initLock()}, {@link #watchLock()} and {@link #release()}.
 * <br>
 * For every lock method, we first try {@link #initLock()}, if this method returns true, then we already
 * have the lock. Otherwise, we may call {@link #watchLock()} to watch for the {@link #lockReady(RuntimeException)}
 * to be invoked, or we may call {@link #release()} to release the internal resources.
 * <br>
 * The implementor should not throw any exception in these three methods. But if you really want to throw,
 * you must clear the internal resources yourself.
 * <br>
 * If something wrong happened during the wait stage, call {@link #lockReady(RuntimeException)} with the proper
 * exception as parameter and clear the internal resources yourself.
 * <br>
 * When unlock, we will call {@link #release()} to tell you to clear the internal resources and release the lock.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2016-4-13
 */
public abstract class DistributedLock implements Lock {

    // The latch used to wait for the lock to be ready.
    private CountDownLatch latch = null;

    // The exception occurred during the wait stage.
    private RuntimeException exception = null;

    /**
     * This is the override of super method.
     * @see java.util.concurrent.locks.Lock#lock()
     */
    @Override
    public synchronized void lock() {
        if (!initLock()) {
            initWait();
            watchLock();

            // Wait until death.
            while (!ThreadUtil.waitFor(latch)) {}

            // Something happened.
            if (exception != null) throw exception;
        }
    }

    /**
     * This is the override of super method.
     * @see java.util.concurrent.locks.Lock#lockInterruptibly()
     */
    @Override
    public synchronized void lockInterruptibly() throws InterruptedException {
        if (!initLock()) {
            initWait();
            watchLock();

            try {
                latch.await();
            } catch (InterruptedException e) {
                // If exception occurred, we need to release resources.
                release();
                throw e;
            }

            if (exception != null) throw exception;
        }
    }

    /**
     * This is the override of super method.
     * @see java.util.concurrent.locks.Lock#tryLock()
     */
    @Override
    public synchronized boolean tryLock() {
        if (!initLock()) {
            release();
            return false;
        } else {
            return true;
        }
    }

    /**
     * This is the override of super method.
     * @see java.util.concurrent.locks.Lock#tryLock(long, java.util.concurrent.TimeUnit)
     */
    @Override
    public synchronized boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        if (!initLock()) {
            initWait();
            watchLock();

            try {
                // If the count reaches zero then the method returns with the value true.
                if (!latch.await(time, unit)) {
                    release();
                    return false;
                } else {
                    if (exception != null) throw exception;

                    return true;
                }
            } catch (InterruptedException e) {
                // If exception occurred, we need to release resources.
                release();
                throw e;
            }
        } else {
            return true;
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
        release();
    }

    /**
     * Initialize the wait variables.
     */
    protected void initWait() {
        exception = null;
        latch = new CountDownLatch(1);
    }

    /**
     * The distributed lock implementor call this method to notify us the ready of the lock.
     *
     * @param exception the exception occurred during the wait stage
     */
    protected void lockReady(RuntimeException exception) {
        this.exception = exception;
        if (latch != null) {
            latch.countDown();
        }
    }

    /**
     * Init the distributed lock and try to get the lock without blocking.
     *
     * @return true if got the lock, false if needs waiting
     */
    protected abstract boolean initLock();

    /**
     * Attach a watch to the distributed cluster, when the lock is ready, implementor need to call
     * {@link #lockReady(RuntimeException)} to notify us.
     */
    protected abstract void watchLock();

    /**
     * Release the internal resources used to record the lock.
     */
    protected abstract void release();

}
