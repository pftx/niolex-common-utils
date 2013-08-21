/**
 * StringFormal.java
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
package org.apache.niolex.common.text;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-8-19
 */
public class StringFormal {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String str1 = "GoodMorning";
        String str2 = new String("GoodMorning");
        System.out.print(str1 == str2);
        System.out.print(",");
        final String str3 = "Good";
        String str4 = str3 + "Morning";
        System.out.print(str1 == str4);
        String str5 = "Good" + "Morning";
        System.out.print(",");
        System.out.print(str1 == str5);
    }

}
