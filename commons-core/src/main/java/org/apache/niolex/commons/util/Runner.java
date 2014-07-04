/**
 * Runner.java
 *
 * Copyright 2012 Niolex, Inc.
 *
 * Niolex licenses this file to you under the Apache License, version 2.0
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
package org.apache.niolex.commons.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Future;

import org.apache.niolex.commons.bean.One;
import org.apache.niolex.commons.concurrent.SimpleFuture;
import org.apache.niolex.commons.reflect.MethodUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the utility class to run a method in a newly created thread.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, Date: 2012-7-12
 */
public class Runner {
	private static final Logger LOG = LoggerFactory.getLogger(Runner.class);

	/**
	 * Run the given method on the specified object in a new thread.
	 *
	 * @param host the host object
	 * @param methodName the method name
	 * @param args the arguments of the given method
	 * @return the thread used to run this method
	 */
	public static Thread run(final Object host, final String methodName, final Object ...args) {
	    One<Thread> one = new One<Thread>();
	    run(one, host, methodName, args);
	    return one.a;
	}

	/**
     * Run the given method on the specified object in a new thread.
     *
     * @param threadVal the bean to store the thread used to run this method
     * @param host the host object
     * @param methodName the method name
     * @param args the arguments of the given method
     * @param <T> the method return type
     * @return the future used to get the return value of the given method
     */
	public static <T> Future<T> run(final One<Thread> threadVal, final Object host, final String methodName,
	        final Object ...args) {
	    final SimpleFuture<T> future = new SimpleFuture<T>();

		Thread t = new Thread("Runner"){
			@SuppressWarnings("unchecked")
            public void run() {
				try {
				    future.setDone((T) MethodUtil.invokeMethod(host, methodName, args));
				} catch (InvocationTargetException e) {
				    future.setAbort(e.getCause());
				} catch (Exception e) {
				    future.setAbort(e);
					LOG.warn("Error occured in Runner#run method.", e);
				}
			}
		};
		t.start();
		// Return the thread.
		threadVal.a = t;

		return future;
	}

	/**
	 * Run the given method on the specified object in a new thread.
	 *
	 * @param host the host object
	 * @param method the method instance
	 * @param args the arguments of the given method
	 * @return the thread used to run this method
	 */
	public static Thread run(final Object host, final Method method, final Object ...args) {
	    One<Thread> one = new One<Thread>();
	    run(one, host, method, args);
        return one.a;
	}

	/**
	 * Run the given method on the specified object in a new thread.
	 *
	 * @param threadVal the bean to store the thread used to run this method
	 * @param host the host object
	 * @param method the method instance
	 * @param args the arguments of the given method
	 * @param <T> the method return type
	 * @return the future used to get the return value of the given method
	 */
	public static <T> Future<T> run(final One<Thread> threadVal, final Object host, final Method method,
	        final Object ...args) {
	    final SimpleFuture<T> future = new SimpleFuture<T>();

	    Thread t = new Thread("Runner"){
	        @SuppressWarnings("unchecked")
	        public void run() {
	            try {
	                future.setDone((T) MethodUtil.invokeMethod(host, method, args));
	            } catch (InvocationTargetException e) {
                    future.setAbort(e.getCause());
                } catch (Exception e) {
                    future.setAbort(e);
	                LOG.warn("Error occured in Runner@run method.", e);
	            }
	        }
	    };
	    t.start();
	    // Return the thread.
        threadVal.a = t;

	    return future;
	}

	/**
	 * Run the given runnable in a new thread.
	 *
	 * @param run the runnable
	 * @return the thread used to run this method
	 */
	public static Thread run(Runnable run) {
		Thread t = new Thread(run, "Runner");
		t.start();
		return t;
	}
}
