/**
 * RetainLinkedList.java
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
package org.apache.niolex.commons.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Retain a number of items before the current head pointer, keep them moving
 * as the head pointer is moving. Finally you will get the latest retainSize
 * number of items left.
 *
 * This list is thread safe.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-6-19
 */
public class RetainLinkedList<E> {

	private final int retainSize;

	/**
	 * The internal head, which is a stub to mark the head of this List.
	 */
	private final Link<E> head = new Link<E>();

	/**
	 * The pointer is the current visit head of this list.
	 */
	private transient Link<E> pointer = head;

	/**
	 * The tail will change from time to time.
	 */
	private transient Link<E> tail = head;

	/**
	 * The size of this List.
	 */
	private final AtomicInteger size = new AtomicInteger(0);

	/**
	 * The size between head and pointer
	 */
	private transient int headPointerSize = 0;

	/**
	 * Lock the list head for delete item from head.
	 */
	private final ReentrantLock headLock = new ReentrantLock();

	/**
	 * Lock the list tail for add item.
	 */
	private final ReentrantLock tailLock = new ReentrantLock();


	/**
	 * The only constructor of RetainLinkedList
	 * @param retainSize must greater than 0
	 */
	public RetainLinkedList(int retainSize) {
		super();
		if (retainSize < 1) {
			throw new IllegalArgumentException("retainSize must greater than 0");
		}
		this.retainSize = retainSize;
	}

	/**
	 * Add an element into this list.
	 * @param e
	 */
	public void add(E e) {
		tailLock.lock();
		try {
			tail.next = new Link<E>(e);
			tail = tail.next;
			size.incrementAndGet();
		} finally {
			tailLock.unlock();
		}
	}

	/**
	 * Handle the next available item. If there is no more
	 * available item, this method will return null.
	 *
	 * Old item will be removed automatically if the current
	 * size if greater than the retainSize
	 * @return
	 */
	public E handleNext() {
		headLock.lock();
		try {
			if (pointer.next == null) {
				return null;
			} else {
				Link<E> tmp = pointer.next;
				pointer = tmp;
				++headPointerSize;
				if (headPointerSize > retainSize) {
					Link<E> t = head.next;
					head.next = t.next;
					t.next = null; // Help GC
					size.decrementAndGet();
				}
				return tmp.e;
			}
		} finally {
			headLock.unlock();
		}
	}

	/**
	 * Add all the items in the other RetainLinkedList into this list
	 * This method will remove items from other RetainLinkedList
	 *
	 * @param other
	 */
	public void addAll(RetainLinkedList<E> other) {
		E e;
		while ((e = other.handleNext()) != null) {
			this.add(e);
		}
	}

	/**
	 * Add all the items in the other Collection into this list
	 * @param other
	 */
	public void addAll(Collection<E> other) {
		Iterator<E> it = other.iterator();
		while (it.hasNext()) {
			this.add(it.next());
		}
	}

	/**
	 * Dump all the items into the specified array.
	 *
	 * If the array size is small than this list size, the first K items will be filled
	 * into the array until the array is full.
	 *
	 * If the array size is greater than this list size, all the items will be filled
	 * into the array and the tail of the array will be left unchanged.
	 *
	 * This method is thread safe, but if there are any concurrent add at the tail, this
	 * method will only reflect a snapshot of the time stamp this method is called.
	 *
	 * @param a
	 * @return
	 */
	public E[] toArray(E[] a) {
		headLock.lock();
		try {
			int len = size.intValue();
			if (a.length < len) {
				len = a.length;
			}
			Link<E> tmp = head.next;
			for (int i = 0; i < len; ++i) {
				a[i] = tmp.e;
				tmp = tmp.next;
			}
			return a;
		} finally {
			headLock.unlock();
		}
	}

	/**
	 * Return whether this list is empty
	 * @return
	 */
	public boolean isEmpty() {
		return size.intValue() == 0;
	}

	/**
	 * Return the size of this list
	 * @return
	 */
	public int size() {
		return size.intValue();
	}

	/**
	 * The internal data structure, please keep away.
	 *
	 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
	 * @version 1.0.0
	 * @Date: 2012-6-1
	 */
	private static class Link<E> {
		private E e;
		private Link<E> next;

		public Link() {
			super();
		}

		public Link(E e) {
			super();
			this.e = e;
		}
	}

}
