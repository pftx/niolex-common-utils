/**
 * CollectionUtils.java
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A collect of utility methods operating on collections.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-5-31
 */
public abstract class CollectionUtils {

	/**
	 * Concatenating all the parameters into one list.
	 * @param src
	 * @param args
	 * @return
	 */
	public static final <E> List<E> concat(Collection<E> src, E ...args) {
		List<E> dest = new ArrayList<E>(src.size() + args.length);
		for (E e : src) {
			dest.add(e);
		}
		for (E e : args) {
			dest.add(e);
		}
		return dest;
	}

	/**
	 * Concatenating all the parameters into one list.
	 * @param args
	 * @return
	 */
	public static final <E> List<E> concat(E ...args) {
		List<E> dest = new ArrayList<E>(args.length);
		for (E e : args) {
			dest.add(e);
		}
		return dest;
	}

	/**
	 * Concatenating all the parameters into one list.
	 * @param src
	 * @param args
	 * @return
	 */
	public static final <E> List<E> concat(Collection<E> src, Collection<E> args) {
		List<E> dest = new ArrayList<E>(src.size() + args.size());
		for (E e : src) {
			dest.add(e);
		}
		for (E e : args) {
			dest.add(e);
		}
		return dest;
	}

	/**
	 * Concatenating all the parameters into one list.
	 * @param src
	 * @param args
	 * @return
	 */
	public static final <E> List<E> concat(E src, Collection<E> args) {
		List<E> dest = new ArrayList<E>(1 + args.size());
		dest.add(src);
		for (E e : args) {
			dest.add(e);
		}
		return dest;
	}

	/**
	 * Make a copy of the parameter.
	 * @param args
	 * @return
	 */
	public static final <E> List<E> copy(Collection<E> args) {
		List<E> dest = new ArrayList<E>(args.size());
		for (E e : args) {
			dest.add(e);
		}
		return dest;
	}

}
