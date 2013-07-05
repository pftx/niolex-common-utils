/**
 * ByteArrayTest.java
 *
 * Copyright 2013 Niolex, Inc.
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
package org.apache.niolex.notify;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2013-1-5
 */
public class ByteArrayTest {

    /**
     * Test method for {@link org.apache.niolex.notify.ByteArray#hashCode()}.
     */
    @Test
    public void testHashCode() {
        byte[] a = "Hello World!".getBytes();
        byte[] b = "Hello World!".getBytes();
        System.out.println("Hash a " + a.hashCode());
        System.out.println("Hash b " + b.hashCode());
        assertFalse(b.equals(a));
        ByteArray t1 = new ByteArray(a);
        ByteArray t2 = new ByteArray(b);
        System.out.println("Hash t1 " + t1.hashCode());
        System.out.println("Hash t2 " + t2.hashCode());
        assertEquals(t1.hashCode, t2.hashCode());
    }

    /**
     * Test method for {@link org.apache.niolex.notify.ByteArray#equals(java.lang.Object)}.
     */
    @Test
    public void testEqualsObject() {
        byte[] a = "Hello World!".getBytes();
        byte[] b = "Hello World!".getBytes();
        assertFalse(b.equals(a));
        ByteArray t1 = new ByteArray(a);
        ByteArray t2 = new ByteArray(b);
        assertEquals(t1, t2);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testByteArray() throws Exception {
        new ByteArray(null);
    }

}
