/**
 * TripleTest.java
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

import java.util.Date;

import org.junit.Test;

/**
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2012-12-5
 */
public class TripleTest {

    /**
     * Test method for {@link org.apache.niolex.commons.bean.Triple#Triple()}.
     */
    @Test
    public void testTriple() {
        Triple<String, Date, Long> tp = new Triple<String, Date, Long>();
        tp.z = 12888l;
        assertEquals(12888l, tp.z.longValue());
        assertEquals("{x=null, y=null, z=12888}", tp.toString());
    }

    /**
     * Test method for {@link org.apache.niolex.commons.bean.Triple#Triple(java.lang.Object, java.lang.Object, java.lang.Object)}.
     */
    @Test
    public void testTripleXYZ() {
        Triple<String, Date, Long> tp = new Triple<String, Date, Long>("Not yet implemented",
                new Date(), 68456564l);
        assertEquals("Not yet implemented", tp.x);
        assertEquals(68456564l, tp.z.longValue());
    }

    /**
     * Test method for {@link org.apache.niolex.commons.bean.Triple#Triple(java.lang.Object, java.lang.Object, java.lang.Object)}.
     */
    @Test
    public void testTripleCreate() {
        Triple<String, Date, Long> tp = Triple.create("Not yet implemented",
                new Date(), 68456564l);
        assertEquals("Not yet implemented", tp.x);
        assertEquals(68456564l, tp.z.longValue());
    }

}
