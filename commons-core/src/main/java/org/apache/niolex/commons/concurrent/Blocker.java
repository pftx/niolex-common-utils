/**
 * Blocker.java
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

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * This is a waiting utility for clients to wait for the response and in the mean time hold the thread.
 * <br><pre>
 * The correct use sequence will be:
 * 1. call WaitOn&lt;E&gt; init(Object key) to generate the wait on object.
 * 2. start the other part to process your request.
 * 3. call E waitForResult(Object key, long time) to wait for the result.
 * </pre>
 *
 * @param <E> the data type user expected as the result of wait
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-7-12
 */
public class Blocker<E> {

	/**
	 * The current waiting map.
	 */
	private final ConcurrentHashMap<Object, WaitOn<E>> waitMap = new ConcurrentHashMap<Object, WaitOn<E>>();

	/**
	 * Initialize an internal wait structure, and return it.
	 * <br>
	 * If there is another one waiting on the same key, that old structure will be returned;
	 * otherwise we create a new one.
	 * <br><b>
	 * Note: Please call this method before process your request, or you may lose your result.
	 * </b>
	 *
	 * @param key the key to be used for wait on
	 * @return the wait on object
	 */
	public WaitOn<E> init(Object key) {
	    WaitOn<E> w = waitMap.get(key);
		if (w == null) {
			w = ConcurrentUtil.initMap(waitMap, key, new WaitOn<E>(new CountDownLatch(1)));
		}
		return w;
	}

	/**
	 * A concise alias for init(key).waitForResult(time).
	 * <br>
	 * Just for some one do not care about initialization. If the result returned before calling
	 * {@link #init(Object)}, this method can not get your result.
	 * <br><b>
	 * Use this method judiciously.
	 * </b>
	 *
	 * @param key the key to wait on
	 * @param time the time to wait for in milliseconds
	 * @return the result or null if timeout
	 * @throws InterruptedException if interrupted by any other thread
	 * @throws BlockerException if user release this key by an exception
	 */
	public E waitForResult(Object key, long time) throws InterruptedException, BlockerException {
		return init(key).waitForResult(time);
	}

	/**
	 * Release the thread waiting on the key with this result.
	 * The result must not be {@link BlockerException} or it's subclass.
	 * Release with an exception has different meaning.
	 *
	 * @see #release(Object, BlockerException)
	 *
	 * @param key the key to be released
	 * @param value the result to be send to the wait thread
	 * @return true if success to release, false if no thread waiting on it.
	 */
	public boolean release(Object key, E value) {
		// Remove the key from wait map, and return the value.
		WaitOn<E> it = waitMap.remove(key);
		if (it != null) {
			it.release(value);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Release the thread waiting on the key with this exception.
	 * The exception will be thrown to user application waiting on this key.
	 *
	 * @param key the key to be released
	 * @param ex the exception to be thrown to the wait thread
	 * @return true if success to release, false if no thread waiting on it.
	 */
	public boolean release(Object key, BlockerException ex) {
		// Remove the key from wait map, and return the value.
		WaitOn<E> it = waitMap.remove(key);
		if (it != null) {
			it.release(ex);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Release all the keys under control with IllegalStateException as the value.
	 * and then clean the internal map.
	 */
	public void releaseAll() {
		Iterator<WaitOn<E>> iter = waitMap.values().iterator();
		BlockerException e = new BlockerException("User triggered release all.");
		while (iter.hasNext()) {
			iter.next().release(e);
		}
		waitMap.clear();
	}

}
