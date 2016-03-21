/**
 * Pair.java
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
package org.apache.niolex.commons.bean;

/**
 * A common utility class to store two variables into one object.
 *
 * @param <A> the first data type
 * @param <B> the second data type
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, Date: 2012-7-12
 */
public class Pair<A, B> {

    /**
     * Create a Pair with this specified value.
     *
     * @param <T> the first data type
     * @param <P> the second data type
     *
     * @param t the first parameter
     * @param p the second parameter
     * @return the created object
     */
    public static <T, P> Pair<T, P> create(T t, P p) {
        return new Pair<T, P>(t, p);
    }

	public A a;

	public B b;

	/**
	 * Create an empty Pair.
	 */
	public Pair() {
		super();
	}

	/**
	 * Create a Pair with the specified value.
	 *
	 * @param a the first value
	 * @param b the second value
	 */
	public Pair(A a, B b) {
		super();
		this.a = a;
		this.b = b;
	}

    /**
     * This is the override of super method.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("{a=%s, b=%s}", a, b);
    }

    /**
     * Generate the string format of this object, using the labels in parameters.
     *
     * @param labelA the label for field a
     * @param labelB the label for field b
     * @return the formated string
     */
    public String toString(String labelA, String labelB) {
        StringBuilder sb = new StringBuilder();
        sb.append("{").append(labelA).append("=").append(a).append(", ").append(labelB).append("=").append(b).append("}");
        return sb.toString();
    }

}
