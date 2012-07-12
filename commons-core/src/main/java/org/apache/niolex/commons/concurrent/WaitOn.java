/**
 * WaitOn.java
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
package org.apache.niolex.commons.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Use this class to wait for the specified result to happen.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-7-12
 */
public class WaitOn<E> {

	/**
	 * The internal managed wait item.
	 */
	private final Condition waitOn;
	private final Lock lock;
	private E result;
	private Exception exc;


	/**
	 * The only constructor. Initialize all fields.
	 *
	 * @param waitOn
	 * @param lock
	 */
	public WaitOn(Condition waitOn, Lock lock) {
		super();
		this.waitOn = waitOn;
		this.lock = lock;
	}


	/**
	 * Wait for result from server.
	 * If result is not ready after the given time, will return null.
	 * If there is any exception thrown from the release side, that exception will be thrown.
	 *
	 * @param key
	 * @param time
	 * @return
	 * @throws InterruptedException
	 */
	public E waitForResult(long time) throws Exception {
		lock.lock();
		try {
			if (result != null)
				return result;
			if (exc != null) {
				// Release with exception.
				throw exc;
			}
			// Not ready yet, let's wait.
			waitOn.await(time, TimeUnit.MILLISECONDS);
			if (exc != null) {
				// Release with exception.
				throw exc;
			}
			// Just return, if not ready, will return null.
			return result;
		} finally {
			lock.unlock();
		}
	}

	public void release(E result) {
		this.result = result;
		lock.lock();
		try {
			waitOn.signalAll();
		} finally {
			lock.unlock();
		}
	}

	public void release(Exception exc) {
		this.exc = exc;
		lock.lock();
		try {
			waitOn.signalAll();
		} finally {
			lock.unlock();
		}
	}
}
