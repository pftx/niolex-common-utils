/**
 * CollectionUtil.java
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.niolex.commons.bean.Pair;

/**
 * A collect of utility methods operating on collections.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, Date: 2012-5-31
 */
public abstract class CollectionUtil {

	/**
	 * Concatenating all the parameters into one list.
	 * @param src
	 * @param args
	 * @return the result
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
	 * @return the result
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
	 * @return the result
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
	 * @return the result
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
	 * @return the result
	 */
	public static final <E> List<E> copy(Collection<E> args) {
		List<E> dest = new ArrayList<E>(args.size());
		for (E e : args) {
			dest.add(e);
		}
		return dest;
	}

	/**
	 * Find the intersection of this two list, and remove them from each list, then get the result.
	 * We will keep the input list unchanged, and create two new ArrayList for return.
	 * Pair.a will be the left list minus the intersection
	 * Pair.b will be the right list minus the intersection
	 *
	 * @param left
	 * @param right
	 * @return the Pair of left results.
	 */
	public static final <E> Pair<List<E>, List<E>> intersection(Collection<E> left, Collection<E> right) {
	    List<E> a = new ArrayList<E>(left);
	    a.removeAll(right);
	    List<E> b = new ArrayList<E>(right);
	    b.removeAll(left);
	    return new Pair<List<E>, List<E>>(a, b);
	}

	/**
     * Return <code>true</code> if the supplied Collection is <code>null</code>
     * or empty. Otherwise, return <code>false</code>.<br>
     *
     * @param collection the Collection to check
     * @return whether the given Collection is empty
     */
    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    /**
     * Return <code>true</code> if the supplied Map is <code>null</code>
     * or empty. Otherwise, return <code>false</code>.<br>
     *
     * @param map the Map to check
     * @return whether the given Map is empty
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }

    /**
     * Return <code>true</code> if the supplied Collection is not <code>null</code>
     * and contain only one element. Otherwise, return <code>false</code>.<br>
     *
     * @param collection the Collection to check
     * @return whether the given Collection is Single
     */
    public static boolean isSingle(Collection<?> collection) {
        return (collection != null && collection.size() == 1);
    }

    /**
     * Return <code>true</code> if the supplied Map is not <code>null</code>
     * and contain only one element. Otherwise, return <code>false</code>.<br>
     *
     * @param map the Map to check
     * @return whether the given Map is Single
     */
    public static boolean isSingle(Map<?, ?> map) {
        return (map != null && map.size() == 1);
    }

}