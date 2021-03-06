/**
 * Triple.java
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
 * A common utility class to store three variables into one object.
 *
 * @param <X> the first data type
 * @param <Y> the second data type
 * @param <Z> the third data type
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5, $Date: 2012-12-5$
 */
public class Triple<X, Y, Z> {

    /**
     * Create a Triple with the specified value.
     *
     * @param <X> the first data type
     * @param <Y> the second data type
     * @param <Z> the third data type
     *
     * @param x the first data
     * @param y the second data
     * @param z the third data
     *
     * @return the created bean
     */
    public static <X, Y, Z> Triple<X, Y, Z> create(X x, Y y, Z z) {
        return new Triple<X, Y, Z>(x, y, z);
    }

    public X x;

    public Y y;

    public Z z;

    /**
     * Create an empty Triple.
     */
    public Triple() {
        super();
    }

    /**
     * Create a Triple with the specified value.
     *
     * @param x the first data
     * @param y the second data
     * @param z the third data
     */
    public Triple(X x, Y y, Z z) {
        super();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * This is the override of super method.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("{x=%s, y=%s, z=%s}", x, y, z);
    }

}
