/**
 * BitsOp.java
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
package org.apache.niolex.common.lang;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-7-8
 */
public class BitsOp {

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("\n**** Bits Op");
        System.out.println("-128 >> 3 ? " + (-128 >> 3));
        System.out.println("-128 >>> 3 ? " + (-128 >>> 3));
        System.out.println("~-128 ? " + (~-128));
        System.out.println("1L << 63 ? " + (1L << 63));
        System.out.println("1L << 64 ? " + (1L << 64));
        System.out.println("1L << 63 << 1 ? " + (1L << 63 << 1));
        System.out.println("1L << 200 ? " + (1L << 200));

        System.out.println("\n**** Test operator priority");
        System.out.println("7 & ~1 = " + (7 & ~1));
    }

}
