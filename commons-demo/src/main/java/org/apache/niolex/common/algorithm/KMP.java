/**
 * KMP.java
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

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-11-26
 */
public class KMP {

    public static void kmpPreprocess(int[] b, char[] p) {
        final int m = p.length;
        int i = 0, j = -1;

        b[i] = j;

        while (i < m) {
            while (j >= 0 && p[i] != p[j])
                j = b[j];
            i++;
            j++;
            b[i] = j;
        }
    }

    public static int kmpSearch(char[] t, char[] p) {
        final int n = t.length, m = p.length;
        int[] b = new int[p.length];

        kmpPreprocess(b, p);

        int i = 0, j = 0;

        while (i < n) {
            while (j >= 0 && t[i] != p[j])
                j = b[j];
            i++;
            j++;
            if (j == m) {
                return (i - j);
            }
        }

        return -1;
    }

}
/*
 * a b ccabc
 * -1 0 0
 */
