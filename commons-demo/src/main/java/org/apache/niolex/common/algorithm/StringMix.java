/**
 * StringMix.java
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
package org.apache.niolex.common.algorithm;

import org.apache.niolex.commons.test.MockUtil;
import org.apache.niolex.commons.test.TidyUtil;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-11-14
 */
public class StringMix extends MockUtil {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String str1 = "123e4c657890!@#$%^&*()_+-=";
        String str2 = "abcdefghijklmnopqrstuvwxyz";

        int i = 100000;

        while (i-- > 0) {
            String str3 = randomMix(str1, str2);

            if (!isMix(str1, str2, str3)) {
                System.out.println("str3 = " + str3);
            }

            int x = randInt(str3.length());
            int y = randInt(str3.length());

            if (x != y) {
                char[] tmp = str3.toCharArray();
                char t = tmp[x];
                tmp[x] = tmp[y];
                tmp[y] = t;

                String str4 = String.valueOf(tmp);

                if (isMix(str1, str2, str4)) {
                    if (x > y) {
                        int z = y;
                        y = x;
                        x = z;
                    }

                    if (x - y == -1) {
                        continue;
                    }

                    System.out.println(String.format("str4 = %s, x = %d, y = %d", str4, x, y));

                    StringBuilder sb = new StringBuilder();
                    sb.append("1234567");

                    TidyUtil.generateChar(sb, ' ', x);
                    sb.append("^");
                    TidyUtil.generateChar(sb, '-', y - x - 1);
                    sb.append("^");

                    System.out.println(sb);
                }
            }
        }
    }

    public static String randomMix(String str1, String str2) {
        StringBuilder sb = new StringBuilder();

        int i = 0, j = 0;
        int len1 = str1.length(), len2 = str2.length();
        while (i < len1 && j < len2) {
            if (randInt(2) == 0) {
                sb.append(str1.charAt(i++));
            } else {
                sb.append(str2.charAt(j++));
            }
        }

        while (i < len1) {
            sb.append(str1.charAt(i++));
        }

        while (j < len2) {
            sb.append(str2.charAt(j++));
        }

        return sb.toString();
    }

    public static boolean isMix(String str1, String str2, String str3) {
        if (str1 == null || str2 == null || str3 == null) {
            return false;
        }

        int len1 = str1.length();
        int len2 = str2.length();
        int len3 = str3.length();

        if (len1 + len2 != len3) {
            return false;
        }

        if (len1 == 0) {
            return str2.equals(str3);
        }
        if (len2 == 0) {
            return str1.equals(str3);
        }

        boolean[][] matrix = new boolean[len1 + 1][len2 + 1];
        char[] arr1 = str1.toCharArray();
        char[] arr2 = str2.toCharArray();
        char[] arr3 = str3.toCharArray();

        if (arr1[0] == arr3[0]) {
            matrix[1][0] = true;
        }
        if (arr2[0] == arr3[0]) {
            matrix[0][1] = true;
        }

        // z is the length of the current matched string.
        for (int z = 2; z <= len3; ++z) {
            boolean flag = false;
            // x is the length of the substring from str1.
            for (int x = 0; x <= len1 && x < z; ++x) {
                int y = z - x - 1;
                // We found a previous match.
                if (y <= len2 && matrix[x][y]) {
                    // Match str1.
                    if (x < len1 && arr1[x] == arr3[z - 1]) {
                        matrix[x + 1][y] = true;
                        flag = true;
                    }
                    // Match str2.
                    if (y < len2 && arr2[y] == arr3[z - 1]) {
                        matrix[x][y + 1] = true;
                        flag = true;
                    }
                }
            }
            if (!flag) {
                return false;
            }
        }

        return true;
    }

}
