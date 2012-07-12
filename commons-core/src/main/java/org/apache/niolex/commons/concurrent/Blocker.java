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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.niolex.commons.util.Pair;

/**
 * This is a waiting utility for clients to wait for the response and in the mean time hold the thread.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-7-12
 */
public class Blocker<E> {

	/**
	 * The lock to stop threads.
	 */
	private final Lock lock = new ReentrantLock();

	/**
	 * The current waiting map.
	 */
	private final ConcurrentHashMap<Object, WaitOn<E>> waitMap = new ConcurrentHashMap<Object, WaitOn<E>>();

	/**
	 * Initialize an internal wait structure, and return it.
	 * If there is already another one waiting on the same key, that old structure will be returned.
	 * Use this method if for anyone want to wait on the same key concurrently.
	 *
	 * @param key
	 * @return Pair.a true if the wait on object is newly created. Pair.b the wait on object.
	 */
	public Pair<Boolean, WaitOn<E>> init(Object key) {
		lock.lock();
		try {
			Condition waitOn = lock.newCondition();
			WaitOn<E> value = new WaitOn<E>(waitOn, lock);
			Pair<Boolean, WaitOn<E>> p = new Pair<Boolean, WaitOn<E>>();
			p.b = waitMap.putIfAbsent(key, value);
			if (p.b == null) {
				p.a = Boolean.TRUE;
				p.b = value;
			} else {
				p.a = Boolean.FALSE;
			}
			return p;
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Initialize an internal wait structure, and return it.
	 * When use this method, the application need to make sure only one thread waiting for one key at the
	 * same time, or the old wait on object will be replaced and that thread can not get result at all.
	 *
	 * @see init(Object key)
	 *
	 * @param key
	 * @return
	 */
	public WaitOn<E> initWait(Object key) {
		lock.lock();
		try {
			Condition waitOn = lock.newCondition();
			WaitOn<E> value = new WaitOn<E>(waitOn, lock);
			waitMap.put(key, value);
			return value;
		} finally {
			lock.unlock();
		}
	}

	/**
	 * A short method for initWait(key).waitForResult(time).
	 * Just for some one do not care about initialization.
	 *
	 * @param key
	 * @param time
	 * @return
	 * @throws InterruptedException
	 */
	public E waitForResult(Object key, long time) throws Exception {
		return initWait(key).waitForResult(time);
	}

	/**
	 * Release the thread waiting on the key with this result.
	 *
	 * @param key
	 * @param value
	 * @return true if success to release, false if no thread waiting on it.
	 */
	public boolean release(Object key, E value) {
		WaitOn<E> it = waitMap.get(key);
		if (it != null) {
			// Remove the key from wait map.
			waitMap.remove(key);
			it.release(value);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Release the thread waiting on the key with this result.
	 *
	 * @param key
	 * @param value
	 * @return true if success to release, false if no thread waiting on it.
	 */
	public boolean release(Object key, Exception value) {
		WaitOn<E> it = waitMap.get(key);
		if (it != null) {
			// Remove the key from wait map.
			waitMap.remove(key);
			it.release(value);
			return true;
		} else {
			return false;
		}
	}

}
