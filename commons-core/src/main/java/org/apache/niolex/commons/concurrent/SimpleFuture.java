/**
 * SimpleFuture.java
 *
 * Copyright 2014 the original author or authors.
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
package org.apache.niolex.commons.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * A simple future implementation.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-7-4
 */
public class SimpleFuture<V> implements Future<V> {

    private final CountDownLatch latch = new CountDownLatch(1);
    private volatile V value;
    private volatile Throwable cause;

    /**
     * This is the override of super method.
     * @see java.util.concurrent.Future#cancel(boolean)
     */
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    /**
     * This is the override of super method.
     * @see java.util.concurrent.Future#isCancelled()
     */
    @Override
    public boolean isCancelled() {
        return false;
    }

    /**
     * This is the override of super method.
     * @see java.util.concurrent.Future#isDone()
     */
    @Override
    public boolean isDone() {
        return latch.getCount() == 0;
    }

    /**
     * Set the execution done with this return value.
     *
     * @param v the return value
     */
    public void setDone(V v) {
        value = v;
        latch.countDown();
    }

    /**
     * Set the execution aborted by this cause.
     *
     * @param cause the abort cause
     */
    public void setAbort(Throwable cause) {
        this.cause = cause;
        latch.countDown();
    }

    /**
     * This is the override of super method.
     * @see java.util.concurrent.Future#get()
     */
    @Override
    public V get() throws InterruptedException, ExecutionException {
        latch.await();
        if (cause == null)
            return value;
        else
            throw new ExecutionException(cause);
    }

    /**
     * This is the override of super method.
     * @see java.util.concurrent.Future#get(long, java.util.concurrent.TimeUnit)
     */
    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (!latch.await(timeout, unit)) throw new TimeoutException();
        if (cause == null)
            return value;
        else
            throw new ExecutionException(cause);
    }

}
