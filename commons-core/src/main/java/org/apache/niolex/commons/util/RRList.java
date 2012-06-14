/**
 * RRList.java
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

import java.util.AbstractList;

/**
 * The round robin array list.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-6-13
 */
public class RRList<E> extends AbstractList<E> {

	/**
	 * The array current size.
	 */
	private int size;

	/**
	 * The current item index.
	 */
	private int currIdx;

	/**
	 * The backed array.
	 */
	private Object[] array;

	/**
	 * Create a round robin list with the specified fixed size.
	 * @param size
	 */
	public RRList(int size) {
		super();
		this.array = new Object[size];
		this.size = 0;
		this.currIdx = 0;
	}

	public void add(int index, E element) {
		array[currIdx] = element;
		currIdx = (currIdx + 1) % array.length;
		++size;
	}

	/**
	 * Override super method
	 * @see java.util.AbstractList#get(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E get(int index) {
		if (index < array.length) {
			return (E) array[index];
		} else {
			throw new IndexOutOfBoundsException("Index {" + index + "} is out of range.");
		}
	}

	/**
	 * Override super method
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public int size() {
		return size;
	}

}
