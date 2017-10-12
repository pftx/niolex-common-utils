/**
 * MultiPerformanceTest.java
 *
 * Copyright 2012 The original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.niolex.commons.test;

import org.junit.Test;

/**
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2012-12-5
 */
public class MultiPerformanceTest {

    /**
     * Test method for {@link org.apache.niolex.commons.test.MultiPerformance#oneRun()}.
     */
    @Test
    public void testAutoBoxing() {
        MultiPerformance perf = new MultiPerformance(4, 1000000, 100) {
            @Override
            protected void run() {
                Integer i = 129;
                Integer j = i >> 2;
                if (j != 32) {
                    throw new RuntimeException("This is so bad! " + j);
                }
            }};
        System.out.print("AutoBoxing ");
        perf.start();
    }

    /**
     * Test method for {@link org.apache.niolex.commons.test.MultiPerformance#start()}.
     */
    @Test
    public void testPrimitive() {
        MultiPerformance perf = new MultiPerformance(4, 1000000, 100) {
            @Override
            protected void run() {
                int i = 129;
                int j = i >> 2;
                if (j != 32) {
                    throw new RuntimeException("This is so bad! " + j);
                }
            }};
        System.out.print("Primitive ");
        perf.start();
    }

}
