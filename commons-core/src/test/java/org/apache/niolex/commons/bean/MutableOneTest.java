/**
 * MutableOneTest.java
 *
 * Copyright 2013 The original author or authors.
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

import org.apache.niolex.commons.test.Counter;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2013-1-6
 */
public class MutableOneTest {

    /**
     * Test method for {@link org.apache.niolex.commons.bean.MutableOne#MutableOne()}.
     */
    @Test
    public void testMutableOne() {
        MutableOne<String> one = new MutableOne<String>();
        final Counter cnt = new Counter();
        MutableOne.DataChangeListener<String> li = new MutableOne.DataChangeListener<String>() {
            @Override
            public void onDataChange(String one) {
                System.out.println("New data -- " + one);
                cnt.inc();
            }
        };
        one.addListener(li);
        one.updateData("Not yet implemented");
        one.removeListener(li);
        one.updateData("DOIFDOIFDOIJFDOJFDO");
        assertEquals(1, cnt.cnt());
        assertEquals("DOIFDOIFDOIJFDOJFDO", one.data());
    }

    /**
     * Test method for {@link org.apache.niolex.commons.bean.MutableOne#MutableOne(java.lang.Object)}.
     */
    @Test
    public void testMutableOneT() {
        MutableOne<String> one = new MutableOne<String>("A");
        final Counter cnt = new Counter();
        MutableOne.DataChangeListener<String> li = new MutableOne.DataChangeListener<String>() {
            @Override
            public void onDataChange(String one) {
                System.out.println("New data -- " + one);
                cnt.inc();
            }
        };
        one.addListener(li);
        one.updateData("Not yet implemented");
        one.removeListener(li);
        one.updateData("DOIFDOIFDOIJFDOJFDO");
        assertEquals(1, cnt.cnt());
        assertEquals("DOIFDOIFDOIJFDOJFDO", one.data());
    }

    /**
     * Test method for {@link org.apache.niolex.commons.bean.MutableOne#addListener(org.apache.niolex.commons.bean.MutableOne.DataChangeListener)}.
     */
    @Test
    public void testAddListener() {
        MutableOne<String> one = new MutableOne<String>();
        final Counter cnt = new Counter();
        MutableOne.DataChangeListener<String> li = new MutableOne.DataChangeListener<String>() {
            @Override
            public void onDataChange(String one) {
                System.out.println("New data -- " + one);
                cnt.inc();
            }
        };
        one.addListener(li);
        one.addListener(li);
        one.updateData("Not yet implemented");
        one.removeListener(li);
        one.updateData("DOIFDOIFDOIJFDOJFDO");
        assertEquals(3, cnt.cnt());
        assertEquals("DOIFDOIFDOIJFDOJFDO", one.data());
    }

    /**
     * Test method for {@link org.apache.niolex.commons.bean.MutableOne#removeListener(org.apache.niolex.commons.bean.MutableOne.DataChangeListener)}.
     */
    @Test
    public void testRemoveListener() {
        MutableOne<String> one = new MutableOne<String>();
        final Counter cnt = new Counter();
        MutableOne.DataChangeListener<String> li = new MutableOne.DataChangeListener<String>() {
            @Override
            public void onDataChange(String one) {
                System.out.println("New data -- " + one);
                cnt.inc();
            }
        };
        one.addListener(li);
        one.updateData("Not yet implemented");
        assertTrue(one.removeListener(li));
        assertFalse(one.removeListener(li));
        one.updateData("DOIFDOIFDOIJFDOJFDO");
        assertEquals(1, cnt.cnt());
        assertEquals("DOIFDOIFDOIJFDOJFDO", one.data());
    }

    /**
     * Test method for {@link org.apache.niolex.commons.bean.MutableOne#updateData(java.lang.Object)}.
     */
    @Test
    public void testUpdateData() {
        MutableOne<String> one = new MutableOne<String>("Hello W!");
        assertEquals("Hello W!", one.data());
        one.updateData("Not yet implemented");
        assertEquals("Not yet implemented", one.data());
    }

}
