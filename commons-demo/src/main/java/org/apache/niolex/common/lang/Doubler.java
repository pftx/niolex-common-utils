/**
 * Doubler.java
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
 * @since 2016-4-7
 */
public class Doubler {

    public String getDescription(Object obj){ return obj.toString(); }

    public String getDescription(String obj){ return obj; }

    public void getDescription(Doubler obj){}

    public static void print(double a, double b) {
        System.out.println(a + ", " + b);

        long c = Double.doubleToLongBits(a);
        long d = Double.doubleToLongBits(b);

        System.out.println("0x" + Long.toHexString(c) + "\n0x" + Long.toHexString(d));
        System.out.println("0b" + Long.toBinaryString(c) + "\n0b" + Long.toBinaryString(d));
        System.out.println();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        double a = 0.1 * 3;
        double b = 0.3;

        print(a, b);
        print(1 / 3.0, 300.0 / 900);

        long e = 0x3fdfffffffffffffl;
        long f = e + 1;

        double g = Double.longBitsToDouble(e);
        double h = Double.longBitsToDouble(f);

        System.out.println(e);
        System.out.println("0x" + Long.toHexString(e) + "\n0x" + Long.toHexString(f));
        System.out.println("0b" + Long.toBinaryString(e) + "\n0b" + Long.toBinaryString(f));
        System.out.println(g + ", " + h);

        double i = 0.0 / 0.0;
        double j = i;

        System.out.println();
        System.out.println(i + ", " + j);
        System.out.println("i > j ? " + (i > j));
        System.out.println("i < j ? " + (i < j));
        System.out.println("i == j ? " + (i == j));
        System.out.println();
        print(i, j);

        print(10d, 10.003d);
    }

}
