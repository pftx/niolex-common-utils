/**
 * CompareTest.java
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
package org.apache.niolex.commons.compress;

import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 3.0.1
 * @since Dec 30, 2016
 */
public class CompareTest {

    static byte[] data = GZipUtilTest.data;

    @Test
    public void test() {
        byte[] c1 = null, c2 = null;

        long t1 = System.nanoTime();

        for (int i = 0; i < 100; ++i)
            c1 = GZipUtil.compress(data);

        long t2 = System.nanoTime();

        for (int i = 0; i < 100; ++i)
            c2 = ZLibUtil.compress(data);

        long t3 = System.nanoTime();

        System.out.println("GZip: " + c1.length + ", ZLib: " + c2.length);
        System.out.println("GZip: " + (t2 - t1) + ", ZLib: " + (t3 - t2));
    }

}
