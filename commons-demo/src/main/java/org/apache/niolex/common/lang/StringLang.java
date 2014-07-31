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
package org.apache.niolex.common.lang;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-8-19
 */
public class StringLang {

    public static String replaceAll(String oringin, String pattern, String replace) {
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(oringin);
        int start = 0, end = 0;
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            start = matcher.start();
            sb.append(oringin.substring(end, start));
            sb.append(replace);
            end = matcher.end();
        }
        sb.append(oringin.substring(end));
        return sb.toString();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        String str2 = new String("GoodMorning");
        System.out.println("\n**** String ==");
        String str1 = "GoodMorning";
        System.out.println("a == new String(\"a\") ? " + (str1 == str2));
        String str3 = "Good";
        String str4 = str3 + "Morning";
        System.out.println("ab == not final \"a\" + \"b\" ? " + (str1 == str4));
        final String str5 = "Good";
        String str6 = str5 + "Morning";
        System.out.println("ab == \"a\" + \"b\" ? " + (str1 == str6));
        System.out.println("a == new String(\"a\").intern() ? " + (str1 == str2.intern()));

        System.out.println("\n**** My replaceAll");
        System.out.println("abcdecbc => " + replaceAll("abcdecbc", "c", "*"));
        System.out.println("abcdecbc => " + replaceAll("abcdecbc", "bc", "*"));
        System.out.println("abcdecbc => " + replaceAll("abcdecbc", "b", "*"));
        System.out.println("Good Morning! => " + replaceAll("Good Morning!", "o", "/"));
    }

}
