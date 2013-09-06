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

import org.apache.niolex.commons.bean.Pair;

/**
 * This is a waiting utility for clients to wait for the response and in the mean time hold the thread.
 *
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
	 * If there is already another one waiting on the same key, that old structure will be returned.
	 * Use this method if anyone want to wait on the same key concurrently.
	 *
	 * @param key the key to wait on
	 * @return Pair.a true if the wait on object is newly created. Pair.b the wait on object.
	 */
	public Pair<Boolean, WaitOn<E>> init(Object key) {
		CountDownLatch latch = new CountDownLatch(1);
		WaitOn<E> value = new WaitOn<E>(latch);
		Pair<Boolean, WaitOn<E>> p = new Pair<Boolean, WaitOn<E>>();
		p.b = waitMap.putIfAbsent(key, value);
		if (p.b == null) {
			p.a = Boolean.TRUE;
			p.b = value;
		} else {
			p.a = Boolean.FALSE;
		}
		return p;
	}

	/**
	 * Initialize an internal wait structure, and return it.
	 * When use this method, the application need to make sure only one thread waiting for one key at the
	 * same time, or the old wait on object will be replaced and that thread can not get result at all.
	 *
	 * @see #init(Object)
	 *
	 * @param key the key to wait on
	 * @return the object to wait on.
	 */
	public WaitOn<E> initWait(Object key) {
		CountDownLatch latch = new CountDownLatch(1);
		WaitOn<E> value = new WaitOn<E>(latch);
		waitMap.put(key, value);
		return value;
	}

	/**
	 * A concise alias for initWait(key).waitForResult(time).
	 * Just for some one do not care about initialization.
	 *
	 * @param key the key to wait on
	 * @param time the time to wait for in milliseconds
	 * @return the result or null if timeout
	 * @throws InterruptedException If interrupted by any other thread.
	 * @throws Exception If user release this key by an exception.
	 */
	public E waitForResult(Object key, long time) throws Exception {
		return initWait(key).waitForResult(time);
	}

	/**
	 * Release the thread waiting on the key with this result.
	 * The result must not be #java.lang.Exception or it's subclass.
	 * Release with an exception has different meaning.
	 *
	 * @see #release(Object, Exception)
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
	 * @param value the exception to be thrown to the wait thread
	 * @return true if success to release, false if no thread waiting on it.
	 */
	public boolean release(Object key, Exception value) {
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
	 * Release all the keys under control with IllegalStateException as the value.
	 * and then clean the internal map.
	 */
	public void releaseAll() {
		Iterator<WaitOn<E>> iter = waitMap.values().iterator();
		IllegalStateException e = new IllegalStateException("User triggered release all.");
		while (iter.hasNext()) {
			iter.next().release(e);
		}
		waitMap.clear();
	}

}
