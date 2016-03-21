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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Use this class to wait for the specified result to happen.
 *
 * @param <E> the data type user expected as the result of {@link #waitForResult(long)}
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, Date: 2012-7-12
 * @see Blocker
 */
public class WaitOn<E> {

	/**
	 * The internal managed wait latch.
	 */
	private final CountDownLatch latch;

	/**
	 * The expected result.
	 */
	private volatile E result;

	/**
	 * The unexpected exception.
	 */
	private volatile BlockerException exc;


	/**
	 * The only constructor. Initialize with the wait latch.
	 *
	 * @param latch the latch to be used to count down
	 */
	public WaitOn(CountDownLatch latch) {
		super();
		this.latch = latch;
	}


	/**
	 * Wait for result from the release side.
	 * If result is not ready after the given time, will return null.
	 * If there is any exception thrown from the release side, that exception will
	 * be thrown to you.
	 *
	 * @param time the time to wait in milliseconds
	 * @return the result, or null if timeout
	 * @throws InterruptedException If interrupted by any other thread.
     * @throws BlockerException If user release this key by an exception.
	 */
	public E waitForResult(long time) throws InterruptedException, BlockerException {
		// First, let's check whether data is ready for now?
		if (result != null)
			return result;
		if (exc != null) {
			// Release with exception.
			throw exc;
		}
		// Not ready yet, let's wait.
		latch.await(time, TimeUnit.MILLISECONDS);
		if (exc != null) {
			// Release with exception.
			throw exc;
		}
		// Just return, if not ready, will return null.
		return result;
	}

	/**
	 * Release the wait thread with result.
	 *
	 * @param result the result to be returned
	 */
	public void release(E result) {
		this.result = result;
		latch.countDown();
	}

	/**
	 * Release the wait thread with exception.
	 *
	 * @param exc the exception
	 */
	public void release(BlockerException exc) {
		this.exc = exc;
		latch.countDown();
	}
}
