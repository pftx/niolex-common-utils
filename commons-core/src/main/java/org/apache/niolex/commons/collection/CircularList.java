/**
 * CircularList.java
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

import java.util.AbstractList;
import java.util.Collection;

/**
 * This Circular List is backed by an array, the element added to
 * the end of this list will automatically replace the eldest element
 * if it's already full.
 * Remove operation on this list is not supported.
 *
 * This implementation is not thread safe.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5, $Date: 2012-11-22$
 */
public class CircularList<E> extends AbstractList<E> {

	/**
	 * The capacity of this list.
	 */
	private final int capacity;

	/**
     * The array buffer into which the elements of the CircularList are stored.
     * The capacity of the CircularList is the length of this array buffer.
     */
    private final Object[] elementData;

    /**
     * The head of the loop.
     */
	private int head = 0;

	/**
	 * The size of this list.
	 */
	private int size = 0;

	/**
	 * Create a new CircularList with this capacity.
	 * Please note that the capacity of this list is fixed and can not be
	 * altered after creation.
	 *
	 * @param capacity the capacity of this list.
	 */
	public CircularList(int capacity) {
		super();
		if (capacity < 2) {
			throw new IllegalArgumentException("The capacity must greater than 1.");
		}
		this.capacity = capacity;
		this.elementData = new Object[capacity];
	}

	/**
     * Appends the specified element to the end of this list.
     * When the list is full, we will replace the eldest item
     * in this list automatically.
     *
     * @param e element to be appended to this list
     * @return {@code true} (as specified by {@link Collection#add})
     */
	@Override
    public boolean add(E e) {
		if (size < capacity) {
			elementData[size++] = e;
		} else {
			elementData[head] = e;
			head = (head + 1) % capacity;
		}
    	return true;
    }


    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<tt>index &lt; 0 || index &gt;= size()</tt>)
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being casted to the return type
     */
	@SuppressWarnings("unchecked")
	@Override
	public E get(int index) {
		if (index >= size || index < 0)
		    throw new IndexOutOfBoundsException(
			"Index: "+index+", Size: "+size);

		return (E) elementData[(head + index) % capacity];
	}

	/**
	 * Replaces the element at the specified position in this list with the specified element.
	 *
     * @param index index of the element to replace
     * @param element element to be stored at the specified position
     * @return the element previously at the specified position
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this list
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<tt>index &lt; 0 || index &gt;= size()</tt>)
	 */
	@Override
	public E set(int index, E element) {
		if (index >= size || index < 0)
		    throw new IndexOutOfBoundsException(
			"Index: "+index+", Size: "+size);

		index = (head + index) % capacity;
		@SuppressWarnings("unchecked")
		E e = (E) elementData[index];

		elementData[index] = element;
		return e;
	}

	/**
	 * Inserts the specified element at the end of this list.
	 *
	 * <p> Please note that this implementation calls {@code add(e)}, which is
	 * different from the default specification in {@link java.util.List#add(int, E)}.
	 */
	@Override
	public void add(int index, E element) {
		add(element);
	}

	/**
	 * {@inheritDoc}
	 *
	 * Override super method
	 * @see java.util.List#size()
	 */
	@Override
	public int size() {
		return size;
	}

}
