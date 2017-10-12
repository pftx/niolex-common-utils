/**
 * Wrapper.java
 *
 * Copyright 2014 the original author or authors.
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
package org.apache.niolex.common.grammar.inner;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-12-10
 */
public class Wrapper {

    public static class Base {
        // No one can visit this.
        private int pri;
        // Package can visit this.
        int pkg;
        // Package and children can visit this.
        protected int pro;

        public int pri() {
            return ++pri;
        }
    }

    public static class Child extends Base {

        public Child() {
            // Not work!!
            // pri = 1;
            pro = 3;
            pkg = 5;
        }
    }

}

class Child2 extends Wrapper.Base {

    public Child2() {
        pro = 7;
        pkg = 9;
    }
}
