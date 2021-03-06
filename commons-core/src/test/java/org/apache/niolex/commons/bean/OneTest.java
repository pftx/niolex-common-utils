/**
 * OneTest.java
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
package org.apache.niolex.commons.bean;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2012-12-5
 */
public class OneTest {

    /**
     * Test method for {@link org.apache.niolex.commons.bean.One#One()}.
     */
    @Test
    public void testOne() {
        One<String> o = new One<String>();
        assertEquals("{null}", o.toString());
        assertTrue(o.absent());
        o.a = "Not yet implemented";
        assertFalse(o.absent());
        assertEquals("Not yet implemented", o.a);
    }

    /**
     * Test method for {@link org.apache.niolex.commons.bean.One#One(java.lang.Object)}.
     */
    @Test
    public void testOneT() {
        One<String> o = new One<String>("Not yet implemented");
        assertEquals("Not yet implemented", o.a);
    }

    /**
     * Test method for {@link org.apache.niolex.commons.bean.One#One(java.lang.Object)}.
     */
    @Test
    public void testCreate() {
        One<String> o = One.create("Xie, Jiyun");
        assertEquals("Xie, Jiyun", o.a);
        assertFalse(o.absent());
    }

}
