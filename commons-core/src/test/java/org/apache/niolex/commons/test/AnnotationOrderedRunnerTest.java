/**
 * AnnotationOrderedRunnerTest.java
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
package org.apache.niolex.commons.test;


import static org.junit.Assert.*;

import org.apache.niolex.commons.test.AnnotationOrderedRunner.Order;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-7-24
 */
@RunWith(AnnotationOrderedRunner.class)
public class AnnotationOrderedRunnerTest {

    private static int cnt = 0;

    @Test
    public void testAnnotationOrderedRunner() throws Exception {
        ++cnt;
        assertTrue(cnt > 10);
    }

    @Test
    public void testComputeTestMethods() throws Exception {
        ++cnt;
        assertTrue(cnt > 10);
    }

    @Test
    @Order(0)
    public void testPrepare1() {
        ++cnt;
    }

    @Test
    @Order(0)
    public void testPrepare2() {
        ++cnt;
    }

    @Test
    @Order(5)
    public void testAFQW() {
        assertEquals(6, cnt);
        ++cnt;
    }

    @Test
    @Order(6)
    public void testERAOI() {
        assertEquals(7, cnt);
        ++cnt;
    }

    @Test
    @Order(3)
    public void testWEOI() {
        assertEquals(4, cnt);
        ++cnt;
    }

    @Test
    @Order(2)
    public void testSDFI() {
        assertEquals(3, cnt);
        ++cnt;
    }

    @Test
    @Order(1)
    public void testZZZ() {
        assertEquals(2, cnt);
        ++cnt;
    }

    @Test
    @Order(4)
    public void testIAWE() {
        assertEquals(5, cnt);
        ++cnt;
    }

    @Test
    @Order(7)
    public void test23434() {
        assertEquals(8, cnt);
        ++cnt;
    }

    @Test
    @Order(8)
    public void test65SFD() {
        assertEquals(9, cnt);
        ++cnt;
    }

}
