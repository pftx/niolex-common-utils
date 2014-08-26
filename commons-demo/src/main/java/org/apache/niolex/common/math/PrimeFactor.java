/**
 * PrimeFactor.java
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
package org.apache.niolex.common.math;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-8-19
 */
public class PrimeFactor {

    /**
     * @param args
     */
    public static void main(String[] args) {
        long input = 18069432665l;

        for (int i = 2; i <= input / 2; ++i) {
            boolean p = false;
            while (input % i == 0) {
                input /= i;
                if (!p) {
                    p = true;
                    System.out.print(i + ", ");
                }
            }
        }

        System.out.print(input);
    }

}
