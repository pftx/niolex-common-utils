/**
 * Hide.java
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
package org.apache.niolex.common.lang;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2016-3-22
 */
public class Hide {

    public Hide over(long a) {
        return this;
    }

    public void sayHi(int i) {
        System.out.println("Say Hi to " + i);
    }

    public static class Chi extends Hide {
        public void sayHi(long i) {
            System.out.println("Say Hi(*) to " + i);
        }

        public Chi over(long a) {
            return this;
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Chi h = new Chi();
        h.sayHi(3);
        h.sayHi(5l);

        byte a = 127;
        byte b = 127;
        //b = a + b; // error : cannot convert from int to byte
        b += a; // ok

        System.out.println(b);
        System.out.println(System.getProperty("sun.arch.data.model"));
    }

}
