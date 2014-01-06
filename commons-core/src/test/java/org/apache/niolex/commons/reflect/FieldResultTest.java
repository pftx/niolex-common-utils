/**
 * FieldResultTest.java
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
package org.apache.niolex.commons.reflect;


import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-1-6
 */
public class FieldResultTest extends FieldUtil {


    private FieldResult<Integer> three = FieldFilter.c().host(new Sub()).exactType(int.class).find();
    private FieldResult<Integer> one = FieldFilter.c().host(new Sub()).exactType(int.class).name("age").find();
    private FieldResult<Character> empty = FieldFilter.c().host(new Sub()).exactType(char.class).find();

    @Test
    public void testFieldResult() throws Exception {
        assertFalse((Boolean)getValue(three, "onlyOne"));
        assertTrue((Boolean)getValue(one, "onlyOne"));
        assertFalse((Boolean)getValue(empty, "onlyOne"));
    }

    @Test
    public void testHost() throws Exception {
        empty.host("not yet implemented");
    }

    @Test
    public void testResults() throws Exception {
        assertEquals(3, three.results().size());
        assertEquals(1, one.results().size());
        assertEquals(0, empty.results().size());
        one.result();
    }

    @Test(expected=IllegalStateException.class)
    public void testResultE1() throws Exception {
        three.result();
    }

    @Test(expected=IllegalStateException.class)
    public void testResultE2() throws Exception {
        empty.result();
    }

    @Test(expected=IllegalStateException.class)
    public void testFirst() throws Exception {
        empty.first();
    }

    @Test(expected=IllegalStateException.class)
    public void testLast() throws Exception {
        empty.last();
    }

    @Test(expected=IllegalStateException.class)
    public void testGet() throws Exception {
        three.get();
    }

    @Test(expected=IllegalStateException.class)
    public void testSet() throws Exception {
        three.set(555);
    }

    @Test
    public void testOneLast() throws Exception {
        assertEquals(0, one.get().intValue());
        one.set(6566);
        assertEquals(6566, one.get().intValue());
    }

    @Test
    public void testNoStatic() throws Exception {
        FieldResult<Object> find = FieldFilter.c().host(new Sub()).name("mark").find();
        assertEquals(3, find.last().get());
        assertEquals(8, find.first().get());
    }

}
