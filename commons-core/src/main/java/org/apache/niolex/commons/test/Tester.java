/**
 * Tester.java
 *
 * Copyright 2016 the original author or authors.
 *
 * We licenses this file to you under the Apache License, version 2.0
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
package org.apache.niolex.commons.test;

/**
 * The tester to help check the value equals and between the range.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2016-3-10
 */
public class Tester {

    /**
     * The float system inaccuracy.
     * The float accuracy: 6 -7 bit.
     */
    public static final double FLOAT_INACCURACY = 1.0e-6f;

    /**
     * The double system inaccuracy.
     * The double accuracy: 15 - 16 bit.
     */
    public static final double DOUBLE_INACCURACY = 1.0e-15;

    /**
     * Test whether two integers equals each other.
     *
     * @param a the value a
     * @param b the value b
     * @return true if equal, false if not
     */
    public static final boolean equal(int a, int b) {
        return a == b;
    }

    /**
     * Test whether two float equals each other.
     *
     * @param a the value a
     * @param b the value b
     * @return true if equal, false if not
     */
    public static final boolean equal(float a, float b) {
        return between(-FLOAT_INACCURACY, (a - b), FLOAT_INACCURACY);
    }

    /**
     * Test whether two double equals each other.
     *
     * @param a the value a
     * @param b the value b
     * @return true if equal, false if not
     */
    public static final boolean equal(double a, double b) {
        return between(-DOUBLE_INACCURACY, (a - b), DOUBLE_INACCURACY);
    }

    /**
     * Test whether the value b set between a and c.
     *
     * @param a the value a
     * @param b the value b
     * @param c the value c
     * @return true if between, false if not
     */
    public static final boolean between(int a, int b, int c) {
        return a <= b && b <= c;
    }

    /**
     * Test whether the value b set between a and c.
     *
     * @param a the value a
     * @param b the value b
     * @param c the value c
     * @return true if between, false if not
     */
    public static final boolean between(double a, double b, double c) {
        return a <= b && b <= c;
    }

}
