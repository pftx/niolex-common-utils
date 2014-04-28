/**
 * Finally.java
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
 * @since 2014-4-18
 */
public class Finally {

    public static void main(String[] args) {
        System.out.println("Ret value = " + retValue());
        System.out.println("Ret Exception = " + retEx());
    }

    @SuppressWarnings("finally")
    public static String retValue() {
        try {
            System.out.println("In try ...");
            return "in try return";
        } finally {
            System.out.println("In finally ...");
            return "in finally";
        }
    }

    @SuppressWarnings({ "finally", "null" })
    public static String retEx() {
        Object o = null;
        try {
            System.out.println("In try ...");
            System.out.println(o.hashCode());
            return "in try return";
        } catch (Exception e) {
            System.out.println("In exception ...");
            return "in catch";
        } finally {
            System.out.println("In finally ...");
            return "in finally";
        }
    }

}
