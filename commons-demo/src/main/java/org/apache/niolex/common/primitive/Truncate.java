/**
 * Truncate.java
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
package org.apache.niolex.common.primitive;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-1-20
 */
public class Truncate {

    /**
     * @param args
     */
    public static void main(String[] args) {
        int i = Short.MAX_VALUE - 2;
        short s;
        for (int k = 0; k < 10; ++k) {
            s = (short) i;
            System.out.println("INT " + i + ", SHT " + s);
            ++i;
        }
        i = Integer.MAX_VALUE - 2;
        for (int k = 0; k < 10; ++k) {
            s = (short) i;
            System.out.println("INT " + i + ", SHT " + s);
            ++i;
        }
    }

}
