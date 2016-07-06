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
    
    @Test
    public void testEqObj() throws Exception {
        eq("a", "a", "abc");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testEqualObjEx() throws Exception {
        equal(new Integer(55), new Integer(56), "not yet implemented");
    }
    
    @Test
    public void testEqualObj() throws Exception {
        equal(new Integer(55), new Integer(55), "not yet implemented");
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

    @Test
    public void testLt() throws Exception {
        lt(4, 5, "not yet implemented");
        lt(0, 1, "not yet implemented");
        lt(-1, 0, "not yet implemented");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testLessThan() throws Exception {
        lessThan(0, 0, "not yet implemented");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testLessThanG() throws Exception {
        lessThan(5, 4, "not yet implemented");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testBt1() throws Exception {
        bt(3, 6, 5, "not yet implemented");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testBt2() throws Exception {
        bt(3, 2, 5, "not yet implemented");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testBt3() throws Exception {
        bt(7, 6, 5, "not yet implemented");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testBt4() throws Exception {
        bt(6, 6, 5, "not yet implemented");
    }

    @Test
    public void testBetween() throws Exception {
        bt(6, 6, 6, "not yet implemented");
        between(6, 6, 7, "not yet implemented");
        between(6, 7, 7, "not yet implemented");
        between(6, 7, 8, "not yet implemented");
    }

    @Test
    public void testBetweenDouble() throws Exception {
        between(6.0, 6.0, 6d, "not yet implemented");
        between(6d, 6d, 7d, "not yet implemented");
        between(6d, 7d, 7d, "not yet implemented");
        between(6d, 7d, 8d, "not yet implemented");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testBtDouble1() throws Exception {
        between(0.1, 0.10000000000001, 0.1, "not yet implemented");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testBtDouble2() throws Exception {
        between(0.1000000002, 0.10000000000001, 0.11, "not yet implemented");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testBtDouble3() throws Exception {
        between(0.1000000001, 0.10000000000001, 0.100001, "not yet implemented");
    }

}
