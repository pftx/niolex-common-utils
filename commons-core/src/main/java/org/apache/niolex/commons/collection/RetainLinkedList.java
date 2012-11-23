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
 * Retain a number of items in this list after all data has been consumed.
 * This list can be used when someone want to remember the last K elements
 * consumed latest.
 *
 * There are two pointers maintained in this list, one for the retain header,
 * which is before the current head pointer, keep them moving as the head
 * pointer is moving; the other is head pointer. Finally you will get the
 * latest retainSize number of items left in the list.
 *
 * This list is thread safe.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, Date: 2012-6-19
 */
public class RetainLinkedList<E> {

	/**
	 * The number of items need to be retained.
	 */
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
	 *
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
	 *
	 * @param e the element to add
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
	 * size if greater than the retainSize, otherwise the
	 * item will be retained in the retain buffer of this
	 * list.
	 *
	 * @return the next available item
	 */
	public E handleNext() {
		headLock.lock();
		try {
			if (pointer.next == null) {
				return null;
			} else {
				Link<E> tmp = pointer.next;
				pointer = tmp;

				if (headPointerSize >= retainSize) {
					Link<E> t = head.next;
					head.next = t.next;
					t.next = null; // Help GC
					size.decrementAndGet();
				} else {
					++headPointerSize;
				}
				return tmp.e;
			}
		} finally {
			headLock.unlock();
		}
	}

	/**
	 * Handle the next retain item in the retain area. If there is no more
	 * item in the retain area, this method will return null.
	 *
	 * Note that there is no item in the retain area do not mean there is
	 * no item in this list.
	 *
	 * Item will be removed automatically in this method.
	 *
	 * @return the next retain item.
	 */
	public E handleRetain() {
		headLock.lock();
		try {
			if (headPointerSize == 0) {
				return null;
			} else {
				Link<E> tmp = head.next;
				--headPointerSize;
				head.next = tmp.next;
				size.decrementAndGet();
				return tmp.e;
			}
		} finally {
			headLock.unlock();
		}
	}

	/**
	 * Add all the items in the other RetainLinkedList into this list
	 * This method automatically will remove items from the other RetainLinkedList
	 * until it's empty.
	 *
	 * @param other the other retain linked list
	 */
	public void addAll(RetainLinkedList<E> other) {
		E e;
		while ((e = other.handleNext()) != null) {
			this.add(e);
		}
	}

	/**
	 * Add all the items in the other Collection into this list
	 *
	 * @param other the other collection
	 */
	public void addAll(Collection<E> other) {
		Iterator<E> it = other.iterator();
		while (it.hasNext()) {
			this.add(it.next());
		}
	}

	/**
	 * Dump all the items in this list including the retained area into the specified array.
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
	 * @param a the array need to be filled
	 * @return the filled array
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
	 * Return whether this list is empty including the retain area.
	 *
	 * @return true if it's empty
	 */
	public boolean isEmpty() {
		return size.intValue() == 0;
	}

	/**
	 * Return whether there is any data to handle.
	 *
	 * @return true if all data is handled
	 */
	public boolean handleEmpty() {
		return pointer.next == null;
	}

	/**
	 * Return the size of this list including the retain area.
	 *
	 * @return the size
	 */
	public int size() {
		return size.intValue();
	}

	/**
	 * Return the number of items need to be handled.
	 *
	 * @return the size
	 */
	public int handleSize() {
		return size.intValue() - headPointerSize;
	}

	/**
	 * The internal data structure, please keep away.
	 * The Link List data structure.
	 *
	 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
	 * @version 1.0.0, Date: 2012-6-1
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
