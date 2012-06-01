/**
 * LinkedIterList.java
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

/**
 * Maintain a Linked List, you can obtain a Iterator to traverse through the data items.
 * Add or remove data from the Linked List will not make Iterator invalid.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-6-1
 */
public class LinkedIterList<E> {

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

	/**
	 * The internal head, which is a sub to mark the head of this List.
	 */
	private final Link<E> head = new Link<E>();

	/**
	 * The tail will change from time to time.
	 */
	private Link<E> tail = head;

	/**
	 * The size of this List.
	 */
	private int size = 0;

	/**
	 * Initialize head and tail here.
	 */
	public LinkedIterList() {
		super();
		this.tail.next = head;
	}

	/**
	 * Add an item to the tail of this list.
	 * This method is synchronized.
	 * @param e
	 */
	public synchronized void add(E e) {
		tail.next = new Link<E>(e);
		tail = tail.next;
		tail.next = head;
		++size;
	}

	/**
	 * Get the current size of this list.
	 * @return
	 */
	public int size() {
		return size;
	}

	/**
	 * Add an item to the tail of this list.
	 * The same as <code>add(E e)</code>
	 * @param e
	 * @return true all the time.
	 */
	public boolean offer(E e) {
		add(e);
		return true;
	}

	/**
	 * Get the first item in the list, and remove it from the list.
	 * If the list is empty, it will return null.
	 * @return
	 */
	public synchronized E poll() {
		if (head.next == head) {
			return null;
		} else {
			Link<E> r = head.next;
			head.next = r.next;
			if (head.next == head) {
				tail = head;
			}
			--size;
			return r.e;
		}
	}

	/**
	 * Get a lazy fail iterator of this list.
	 * The iterator will not fail on all cases, but there is a known issue:
	 * When this iterator is focus on the tail item and which is then removed, the iterator
	 * can not find the correct tail again, so it will fail.
	 * The price to fix this is very high, so I will just leave it as it is.
	 * @return
	 */
	public Iter<E> iterator() {
		return new LinkIter(head);
	}

	/**
	 * The iterator interface.
	 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
	 * @version 1.0.0
	 * @Date: 2012-6-1
	 */
	public static interface Iter<E> {

		/**
		 * Test whether the backed list has more items.
		 * @return
		 */
		public boolean hasNext();

		/**
		 * Get the next item in the list, will return null if no more exist.
		 * @return
		 */
		public E next();

	}

	/**
	 * The private implementation of the interface Iter.
	 *
	 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
	 * @version 1.0.0
	 * @Date: 2012-6-1
	 */
	private class LinkIter implements Iter<E> {
		/**
		 * The current iterator item.
		 */
		private Link<E> current;

		public LinkIter(Link<E> current) {
			super();
			this.current = current;
		}

		public boolean hasNext() {
			if (current.next != head) {
				return true;
			} else {
				if (head == tail) {
					current = head;
				}
				return false;
			}
		}

		public E next() {
			if (current.next == head) {
				if (head == tail) {
					current = head;
				}
				return null;
			} else {
				current = current.next;
				return current.e;
			}
		}
	}

}
