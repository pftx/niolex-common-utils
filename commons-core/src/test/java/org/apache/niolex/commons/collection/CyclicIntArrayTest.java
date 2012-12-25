/**
 * CyclicIntArrayTest.java
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
package org.apache.niolex.commons.collection;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

/**
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2012-12-25
 */
public class CyclicIntArrayTest {

    /**
     * Test method for {@link org.apache.niolex.commons.collection.CyclicIntArray#CyclicIntArray(int)}.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testCyclic() {
        CyclicIntArray a = new CyclicIntArray(1);
        a.push(3);
    }

    /**
     * Test method for {@link org.apache.niolex.commons.collection.CyclicIntArray#CyclicIntArray(int)}.
     */
    @Test
    public void testCyclicIntArray() {
        CyclicIntArray a = new CyclicIntArray(2);
        assertEquals(0, a.push(3));
        assertEquals(0, a.push(4));
        assertEquals(3, a.push(5));
    }

    /**
     * Test method for {@link org.apache.niolex.commons.collection.CyclicIntArray#push(int)}.
     */
    @Test
    public void testPush() {
        CyclicIntArray a = new CyclicIntArray(3);
        assertEquals(0, a.push(3));
        assertEquals(0, a.push(4));
        assertEquals(0, a.push(5));
        assertEquals(3, a.push(6));
        assertEquals(4, a.push(7));
        assertEquals("[6, 7, 5]", Arrays.toString(a.getArray()));
    }

    /**
     * Test method for {@link org.apache.niolex.commons.collection.CyclicIntArray#getArray()}.
     */
    @Test
    public void testGetArray() {
        CyclicIntArray a = new CyclicIntArray(2);
        assertEquals(0, a.push(3));
        assertEquals(0, a.push(4));
        assertEquals(3, a.push(6));
        assertEquals(4, a.push(7));
        assertEquals("[6, 7]", Arrays.toString(a.getArray()));
        assertEquals(2, a.size());
    }

}
