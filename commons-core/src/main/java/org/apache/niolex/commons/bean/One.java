/**
 * One.java
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
 * A common utility class to store one object.
 *
 * @param <T> the data type
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5, $Date: 2012-12-5$
 */
public class One<T> {

    /**
     * Create an One with this specified value.
     *
     * @param t the value inside
     * @return the created object
     */
    public static <T> One<T> create(T t) {
        return new One<T>(t);
    }

    public T a;

    /**
     * Create an empty One.
     */
    public One() {
        super();
    }

    /**
     * Create an One with this specified value.
     *
     * @param a the parameter
     */
    public One(T a) {
        super();
        this.a = a;
    }

    /**
     * Returns true if this instance contains a null reference.
     */
    public boolean absent() {
        return a == null;
    }

    /**
     * This is the override of super method.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("{%s}", a);
    }

}
