/**
 * Check.java
 *
 * Copyright 2013 the original author or authors.
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

import org.apache.commons.lang.Validate;

/**
 * A collect of utility methods check status, insure methods working as expected.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-6-20
 */
public class Check extends Validate {

    /**
     * Check that a equals b, otherwise we throw IllegalArgumentException
     * @param a
     * @param b
     * @param msg
     */
    public static void eq(int a, int b, String msg) {
        equal(a, b, msg);
    }

    /**
     * Check that a equals b, otherwise we throw IllegalArgumentException
     * @param a
     * @param b
     * @param msg
     */
    public static void equal(int a, int b, String msg) {
        if (a != b) {
            throw new IllegalArgumentException(msg);
        }
    }

}
