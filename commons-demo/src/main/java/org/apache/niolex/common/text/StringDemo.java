/**
 * StringDemo.java
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-4-19
 */
public class StringDemo {

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
        System.out.println("abcdecbc => " + replaceAll("abcdecbc", "c", "*"));
        System.out.println("abcdecbc => " + replaceAll("abcdecbc", "bc", "*"));
        System.out.println("abcdecbc => " + replaceAll("abcdecbc", "b", "*"));
        System.out.println("Good Morning! => " + replaceAll("Good Morning!", "o", "/"));
    }

}
