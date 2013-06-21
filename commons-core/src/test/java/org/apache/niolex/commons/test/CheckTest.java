/**
 * CheckTest.java
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

import static org.apache.niolex.commons.test.Check.*;

import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-6-20
 */
public class CheckTest {

    @Test
    public void testEq() throws Exception {
        eq(2, 2, "abc");
        new Check();
    }

    @Test
    public void testTrue() throws Exception {
        isTrue(true, "abc");
        new C().inName();
    }

    @Test(expected=IllegalArgumentException.class)
    public void testEqual() throws Exception {
        equal(3, 4, "not yet implemented");
    }

    static class A {
        public int i = 4;
        static String name() {
            return "A";
        }
    }

    static class B extends A {
        public int i = 8;
        static String name() {
            return "B";
        }
    }

    static class C extends B {
        public void inName() {
            System.out.println(name() + " " + ((A)this).i);
            System.out.println(name() + " " + i);
        }
    }

}