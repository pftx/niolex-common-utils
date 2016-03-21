/**
 * InvokableExecutorService.java
 *
 * Copyright 2015 the original author or authors.
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

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * This class wrap the ExecutorService, user can invoke any method through
 * this class concurrently, leverage the thread pool in ExecutorService.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2015-5-26
 */
public class InvokableExecutorService implements ExecutorService {

    /**
     * The method {@link InvokableExecutorService#invoke(Object, Method, Object[])} will use this
     * class to submit the method invocation into the thread pool.
     *
     * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
     * @version 1.0.0
     * @since 2015-5-26
     * @param <V> the return value type
     */
    private static class Invoker<V> implements Runnable {

        private final Method method;
        private final Object host;
        private final Object[] args;
        private final SimpleFuture<V> fu;

        /**
         * Construct a new Invoker.
         *
         * @param method the method to be invoked
         * @param host the object the underlying method is invoked from
         * @param args the arguments used for the method call
         * @param fu the future used to store result
         */
        public Invoker(Method method, Object host, Object[] args, SimpleFuture<V> fu) {
            super();
            this.method = method;
            this.host = host;
            this.args = args;
            this.fu = fu;
        }
        /**
         * This is the override of super method.
         * @see java.lang.Runnable#run()
         */
        @SuppressWarnings("unchecked")
        @Override
        public void run() {
            try {
                Object ret = method.invoke(host, args);
                fu.setDone((V) ret);
            } catch (Throwable t) {
                fu.setAbort(t);
            }
        }

    }

    private final ExecutorService pool;

    /**
     * Construct a InvokableExecutorService over the pool.
     *
     * @param pool the thread pool to be used
     */
    public InvokableExecutorService(ExecutorService pool) {
        super();
        this.pool = pool;
    }

    /**
     * Invoke the given method in the internal thread pool.
     * User can get the result (or exception) by the returned future.
     *
     * @param <V> the return value type
     *
     * @param host the host object of the method
     * @param method the method to be invoked
     * @param args the arguments to be used
     * @return the future used to get the result
     */
    public <V> Future<V> invoke(Object host, Method method, Object... args) {
        SimpleFuture<V> fu = new SimpleFuture<V>();
        Invoker<V> inv = new Invoker<V>(method, host, args, fu);
        this.submit(inv);
        return fu;
    }

    /**
     * @param command the command to be executed
     * @see java.util.concurrent.Executor#execute(java.lang.Runnable)
     */
    public void execute(Runnable command) {
        pool.execute(command);
    }

    /**
     *
     * @see java.util.concurrent.ExecutorService#shutdown()
     */
    public void shutdown() {
        pool.shutdown();
    }

    /**
     * @return the result
     * @see java.util.concurrent.ExecutorService#shutdownNow()
     */
    public List<Runnable> shutdownNow() {
        return pool.shutdownNow();
    }

    /**
     * @return the result
     * @see java.util.concurrent.ExecutorService#isShutdown()
     */
    public boolean isShutdown() {
        return pool.isShutdown();
    }

    /**
     * @return the result
     * @see java.util.concurrent.ExecutorService#isTerminated()
     */
    public boolean isTerminated() {
        return pool.isTerminated();
    }

    /**
     * @param timeout the timeout
     * @param unit the timeout unit
     * @return the result
     * @throws InterruptedException if interrupted
     * @see java.util.concurrent.ExecutorService#awaitTermination(long, java.util.concurrent.TimeUnit)
     */
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return pool.awaitTermination(timeout, unit);
    }

    /**
     * @param task the task to be executed
     * @return the result
     * @see java.util.concurrent.ExecutorService#submit(java.util.concurrent.Callable)
     */
    public <T> Future<T> submit(Callable<T> task) {
        return pool.submit(task);
    }

    /**
     * @param task the task to be executed
     * @param result the result to be returned
     * @return the result
     * @see java.util.concurrent.ExecutorService#submit(java.lang.Runnable, java.lang.Object)
     */
    public <T> Future<T> submit(Runnable task, T result) {
        return pool.submit(task, result);
    }

    /**
     * @param task the task to be executed
     * @return the result
     * @see java.util.concurrent.ExecutorService#submit(java.lang.Runnable)
     */
    public Future<?> submit(Runnable task) {
        return pool.submit(task);
    }

    /**
     * @param tasks the tasks to be executed
     * @return the result
     * @throws InterruptedException if interrupted
     * @see java.util.concurrent.ExecutorService#invokeAll(java.util.Collection)
     */
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return pool.invokeAll(tasks);
    }

    /**
     * @param tasks the tasks to be executed
     * @param timeout the timeout
     * @param unit the timeout unit
     * @return the result
     * @throws InterruptedException if interrupted
     * @see java.util.concurrent.ExecutorService#invokeAll(java.util.Collection, long, java.util.concurrent.TimeUnit)
     */
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
            throws InterruptedException {
        return pool.invokeAll(tasks, timeout, unit);
    }

    /**
     * @param tasks the task to be executed
     * @return the result
     * @throws InterruptedException if interrupted
     * @throws ExecutionException if exception thrown during method invoke
     * @see java.util.concurrent.ExecutorService#invokeAny(java.util.Collection)
     */
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return pool.invokeAny(tasks);
    }

    /**
     * @param tasks the task to be executed
     * @param timeout the timeout
     * @param unit the timeout unit
     * @return the result
     * @throws InterruptedException if interrupted
     * @throws ExecutionException if exception thrown during method invoke
     * @throws TimeoutException if timeout
     * @see java.util.concurrent.ExecutorService#invokeAny(java.util.Collection, long, java.util.concurrent.TimeUnit)
     */
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        return pool.invokeAny(tasks, timeout, unit);
    }


}
