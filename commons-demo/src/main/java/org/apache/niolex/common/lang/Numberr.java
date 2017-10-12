/**
 * Number.java
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
public class Numberr {

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("\n**** isAssignableFrom");
        System.out.println("Long isAssignableFrom Number ? " + (Long.class.isAssignableFrom(Number.class)));
        System.out.println("Number isAssignableFrom Long(t) ? " + (Number.class.isAssignableFrom(Long.class)));
        Long ll = 129012l;
        System.out.println("Number.class isInstance Long Object(t) ? " + (Number.class.isInstance(ll)));
        System.out.println("Long instanceof Number(t) ? " + (ll instanceof Number));
        System.out.println("long.class isInstance Long Object ? " + (long.class.isInstance(ll)));

        System.out.println("\n**** Test >> & >>>");
        System.out.println("-1 >>  3 => " + (-1 >> 3));
        System.out.println("-1 >>> 3 => " + (-1 >>>3));

        System.out.println("\n**** Test char range");
        System.out.println("MAX_CHAR => " + (int)Character.MAX_VALUE);
        System.out.println("MIN_CHAR => " + (int)Character.MIN_VALUE);
        System.out.println("MAX_SHORT => " + (int)Short.MAX_VALUE);
        System.out.println("MIN_SHORT => " + (int)Short.MIN_VALUE);
        System.out.println("-Integer.MAX_VALUE => " + (-Integer.MAX_VALUE));
        System.out.println("Integer.MIN_VALUE => " + (Integer.MIN_VALUE));

        int j = 0;
        for (int i = 1; i > 0; i *= 10) {
            System.out.print(i + " ");
            if (++j > 40) break;
        }
        System.out.println("Max *10 Loop - " + j);
    }
}
