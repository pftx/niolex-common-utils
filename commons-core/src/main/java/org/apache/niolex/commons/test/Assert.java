/**
 * Assert.java
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
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 2.1.3
 * @since Sep 1, 2016
 */
public class Assert extends org.junit.Assert {

    public static final void assertIntEquals(int a, Object b) {
        if (b instanceof Integer) {
            int c = ((Integer) b).intValue();
            assertEquals(a, c);
        } else {
            throw new AssertionError("Object b is not Integer.");
        }
    }

    public static final void assertLongEquals(long a, Object b) {
        if (b instanceof Long) {
            long c = ((Long) b).longValue();
            assertEquals(a, c);
        } else {
            throw new AssertionError("Object b is not Long.");
        }
    }

}
