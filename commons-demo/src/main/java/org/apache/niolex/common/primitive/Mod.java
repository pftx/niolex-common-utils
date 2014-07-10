/**
 * Mod.java
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

import org.apache.niolex.commons.util.SystemUtil;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-8-6
 */
public class Mod {

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("求余数的法则：越界以后，余数没有任何连续性,反而是中心对称的。");
        int i = Integer.MAX_VALUE - 3, j = 0;
        while (j++ < 9) {
            int k = i % 7;
            System.out.println((i > 0 ? "+" : "") + i
                    + "%7 => " + (k >= 0 ? "+" : "") + k + " 修正  " + (k < 0 ? -k : k));
            ++i;
        }
        System.out.println();

        System.out.println("求余数的法则：被除数和余数的符号保持不变！除数和商的符号凑单！");
        countMod(5, 3);
        countMod(5, -3);
        countMod(-5, 3);
        countMod(-5, -3);
    }

    public static void countMod(int p, int r) {
        int s = (p / r);
        int y = (p % r);
        SystemUtil.println("% d / % d = 商 %02d 除数 %02d", p, r, s, y);
    }

}
