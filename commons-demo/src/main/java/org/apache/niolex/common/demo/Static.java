/**
 * Static.java
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
package org.apache.niolex.common.demo;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-4-15
 */
public class Static extends God {

    public static int cnt() {
        return 9;
    }

    public static void println() {
        System.out.println("From Son.. " + cnt());
    }

    public static void main(String[] args) {
        System.out.println("-----Son------");
        main();
        System.out.println("-----God------");
        God.main();
    }

    public static void main() {
        Static.println();
        Static.print();
    }

}

class God {

    public static int cnt() {
        return 6;
    }

    public static void print() {
        System.out.println("From God__ " + cnt());
        new Throwable().printStackTrace();
    }

    public static void println() {
        System.out.println("From God.. " + cnt());
    }

    public static void main() {
        println();
        print();
    }

}
