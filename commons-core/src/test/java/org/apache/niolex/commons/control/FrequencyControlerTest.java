/**
 * FrequencyControlerTest.java
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
package org.apache.niolex.commons.control;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2012-12-25
 */
public class FrequencyControlerTest {

    /**
     * Test method for {@link org.apache.niolex.commons.control.FrequencyControler#FrequencyControler(int, int)}.
     */
    @Test
    public void testFrequencyControler() {
        FrequencyControler fc = new FrequencyControler(10, 100);
        assertTrue(fc.check(50));
        assertTrue(fc.check(30));
        assertTrue(fc.check(20));
        assertFalse(fc.check(1));
        assertFalse(fc.check(10));
        assertFalse(fc.check(30));
        assertFalse(fc.check(50));
    }

    /**
     * Test method for {@link org.apache.niolex.commons.control.FrequencyControler#check(int)}.
     */
    @Test
    public void testCheck() {
        FrequencyControler fc = new FrequencyControler(4, 100);
        assertTrue(fc.check(50));
        assertTrue(fc.check(30));
        assertTrue(fc.check(10));
        assertFalse(fc.check(11));
        assertTrue(fc.check(49));
        assertFalse(fc.check(31));
        assertTrue(fc.check(9));
    }

}
