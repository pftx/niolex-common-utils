/**
 * ConstTest.java
 *
 * Copyright 2013 The original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.niolex.commons.util;

import org.junit.Test;

/**
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2013-1-22
 */
public class ConstTest implements Const {

    @Test
    public void test() {
        Bi.go();
    }

    static class A {
        static void print() {
            System.out.println("One M " + M);
            System.out.println("One G " + G);
        }
    }

    static class Bi extends A {
        static void go() {
            print();
        }
    }
}
