/**
 * Round.java
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
public class Round {

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("ROUND(6.5) => " + Math.round(6.5));
        System.out.println("ROUND(6.4) => " + Math.round(6.4));
        System.out.println("ROUND(-6.6) => " + Math.round(-6.6));
        System.out.println("ROUND(-6.5) => " + Math.round(-6.5));
        System.out.println("ROUND(-6.4) => " + Math.round(-6.4));

        System.out.println("对称性不成立！记住：逢5上调");
        System.out.println("ROUND( 0.6) => " + Math.round(0.6));
        System.out.println("ROUND( 0.5) => " + Math.round(0.5));
        System.out.println("ROUND( 0.4) => " + Math.round(0.4));
        System.out.println("ROUND(-0.4) => " + Math.round(-0.4));
        System.out.println("ROUND(-0.5) => " + Math.round(-0.5));
        System.out.println("ROUND(-0.6) => " + Math.round(-0.6));
    }

}
