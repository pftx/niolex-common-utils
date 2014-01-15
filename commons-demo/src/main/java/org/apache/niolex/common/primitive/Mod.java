/**
 * Mod.java
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
package org.apache.niolex.common.primitive;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-8-6
 */
public class Mod {

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("5%3 => " + (5%3));
        System.out.println("5%-3 => " + (5%-3));
        System.out.println("-5%3 => " + (-5%3));
        System.out.println("-5%-3 => " + (-5%-3));
        System.out.println("5%3 => " + (5%3));
        int i = Integer.MAX_VALUE - 3, j = 0;
        while (j++ < 8) {
            int k = i % 7;
            System.out.println(i + "%7 => " + (k < 0 ? k + 7 : k));
            ++i;
        }
    }

}
