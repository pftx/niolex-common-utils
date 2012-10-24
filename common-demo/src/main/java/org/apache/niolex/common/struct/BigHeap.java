/**
 * BigHeap.java
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
package org.apache.niolex.common.struct;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-10-24
 */
@SuppressWarnings("unchecked")
public class BigHeap<T extends Comparable<T>> {

	private final int heapSize;
	private int size;
	private Object[] data;

	public BigHeap(int heapSize) {
		super();
		if (heapSize < 1) {
			throw new IllegalArgumentException("heapSize must > 0");
		}
		this.heapSize = heapSize;
		size = 0;
		data = new Object[heapSize];
	}

	public boolean push(T t) {
		if (size == heapSize) {
			return false;
		}
		data[size++] = t;
		// start to roll in the deep.
		int c = size - 1;
		int p = (c - 1) / 2;
		while (c > 0 && ((T)data[c]).compareTo((T)data[p]) > 0) {
			swap(c, p);
			c = p;
			p = (c - 1) / 2;
		}
		return true;
	}

	public T max() {
		if (size == 0) {
			return null;
		}
		return (T)data[0];
	}

	public T pop() {
		if (size == 0) {
			return null;
		}
		T ret = (T)data[0];
		--size;
		if (size > 0) {
			data[0] = data[size];
			int p = 0;
			int l = (p + 1) * 2 - 1;
			int r = l + 1;
			// max p l r must in p.
			while (l < size) {
				// check max child.
				int k;
				if (r == size || ((T)data[l]).compareTo((T)data[r]) > 0)
					k = l;
				else
					k = r;
				if (((T)data[k]).compareTo((T)data[p]) > 0)
					swap(p, k);
				else
					break;
				p = k;
				l = (p + 1) * 2 - 1;
				r = l + 1;
			}
		}
		return ret;
	}

	public int size() {
		return size;
	}

	private void swap(int c, int p) {
		Object t = data[c];
		data[c] = data[p];
		data[p] = t;
	}

}
