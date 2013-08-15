/**
 * CollectionUtilsTest.java
 *
 * Copyright 2012 Niolex, Inc.
 *
 * Niolex licenses this file to you under the Apache License, version 2.0
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
package org.apache.niolex.commons.collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.niolex.commons.bean.Pair;
import org.apache.niolex.commons.collection.CollectionUtil;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-5-31
 */
public class CollectionUtilTest {

	/**
	 * Test method for {@link org.apache.niolex.commons.collection.CollectionUtil#concat(java.util.Collection, E[])}.
	 */
	@Test
	public void testConcatCollectionOfEEArray() {
		List<String> dest = new ArrayList<String>(3);
		dest.add("methods1");
		dest.add("methods2");
		dest.add("methods3");
		dest = CollectionUtil.concat(dest, "Nice", "Meet");
		assertEquals(5, dest.size());
		assertEquals("Nice", dest.get(3));
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.collection.CollectionUtil#concat(E[])}.
	 */
	@Test
	public void testConcatEArray() {
		List<String> dest = CollectionUtil.concat("You", "Nice", "Meet");
		assertEquals(3, dest.size());
		assertEquals("Nice", dest.get(1));
	}

	@Test
	public void testConcat() throws Exception {
		List<String> dest = new ArrayList<String>(3);
		dest.add("methods1");
		dest.add("methods2");
		dest.add("methods3");
		List<String> dest2 = new ArrayList<String>(3);
		dest2.add("methods21");
		dest2.add("methods22");
		dest2.add("methods23");
		dest = CollectionUtil.concat(dest, dest2);
		assertEquals(6, dest.size());
		assertEquals("methods22", dest.get(4));
	}

	@Test
	public void testConcatCollec() {
		List<String> dest = new ArrayList<String>(3);
		dest.add("methods1");
		dest.add("methods2");
		dest.add("methods3");
		dest = CollectionUtil.concat("Nice", dest);
		assertEquals(4, dest.size());
		assertEquals("Nice", dest.get(0));
	}

	@Test
	public void testColec() {
		List<String> dest = new ArrayList<String>(3);
		dest.add("methods1");
		dest.add("methods2");
		dest.add("methods3");
		List<String> ddest = CollectionUtil.copy(dest);
		assertEquals(3, ddest.size());
		assertEquals("methods1", dest.get(0));
		assertEquals("methods2", dest.get(1));
		assertEquals("methods3", dest.get(2));
	}

	@Test
	public void testColec0() {
		List<String> dest = new ArrayList<String>(3);
		List<String> ddest = CollectionUtil.copy(dest);
		assertEquals(0, ddest.size());
	}

    @Test
    public void testIntersection() {
        List<String> l = new ArrayList<String>(3);
        l.add("a");
        l.add("b");
        l.add("c");
        List<String> r = new ArrayList<String>(3);
        r.add("c");
        r.add("d");
        r.add("e");
        Pair<List<String>,List<String>> pair = CollectionUtil.intersection(l, r);
        assertEquals("a", pair.a.get(0));
        assertEquals("b", pair.a.get(1));
        assertEquals("d", pair.b.get(0));
        assertEquals("e", pair.b.get(1));
        assertEquals(2, pair.a.size());
        assertEquals(2, pair.b.size());
    }

    @Test
    public void testIntersection1() {
        List<String> l = new ArrayList<String>(3);
        l.add("a");
        l.add("b");
        l.add("c");
        l.add("d");
        List<String> r = new ArrayList<String>(3);
        r.add("c");
        r.add("d");
        r.add("e");
        r.add("b");
        Pair<List<String>,List<String>> pair = CollectionUtil.intersection(l, r);
        assertEquals("a", pair.a.get(0));
        assertEquals("e", pair.b.get(0));
        assertEquals(1, pair.a.size());
        assertEquals(1, pair.b.size());
    }

    @Test
    public void testIntersection2() {
        List<String> l = new ArrayList<String>(3);
        l.add("a");
        l.add("b");
        l.add("c");
        l.add("d");
        List<String> r = new ArrayList<String>(3);
        r.add("e");
        r.add("f");
        Pair<List<String>,List<String>> pair = CollectionUtil.intersection(l, r);
        assertEquals("a", pair.a.get(0));
        assertEquals("b", pair.a.get(1));
        assertEquals("c", pair.a.get(2));
        assertEquals("e", pair.b.get(0));
        assertEquals("f", pair.b.get(1));
        assertEquals(4, pair.a.size());
        assertEquals(2, pair.b.size());
    }

    @Test
    public void testIntersectionEmpty() {
        new CollectionUtil(){};
        List<String> l = new ArrayList<String>(3);
        l.add("a");
        l.add("b");
        l.add("c");
        Pair<List<String>,List<String>> pair = CollectionUtil.intersection(l, l);
        assertEquals(0, pair.a.size());
        assertEquals(0, pair.b.size());
    }

    @Test
    public void testIsEmptyColNull() {
        List<String> col = null;
        assertTrue(CollectionUtil.isEmpty(col));
    }

    @Test
    public void testIsEmptyMapNull() {
        Map<String, ?> col = null;
        assertTrue(CollectionUtil.isEmpty(col));
    }

    @Test
    public void testIsEmptyColEmpty() {
        List<String> col = Collections.emptyList();
        assertTrue(CollectionUtil.isEmpty(col));
    }

    @Test
    public void testIsEmptyMapEmpty() {
        Map<String, ?> col = Collections.emptyMap();
        assertTrue(CollectionUtil.isEmpty(col));
    }

    @Test
    public void testIsEmptyColFalse() {
        Collection<String> col = Collections.singleton("Lex");
        assertFalse(CollectionUtil.isEmpty(col));
    }

    @Test
    public void testIsEmptyMapFalse() {
        Map<String, ?> col = Collections.singletonMap("Tei mail", "Hap");
        assertFalse(CollectionUtil.isEmpty(col));
    }

    @Test
    public void testIsSingleColNull() {
        Collection<String> col = null;
        assertFalse(CollectionUtil.isSingle(col));
    }

    @Test
    public void testIsSingleMapNull() {
        Map<String, ?> col = null;
        assertFalse(CollectionUtil.isSingle(col));
    }

    @Test
    public void testIsSingleColSingle() {
        Collection<String> col = Collections.singleton("Lex");
        assertTrue(CollectionUtil.isSingle(col));
    }

    @Test
    public void testIsSingleMapSingle() {
        Map<String, ?> col = Collections.singletonMap("Tei mail", "Hap");
        assertTrue(CollectionUtil.isSingle(col));
    }

    @Test
    public void testIsSingleColFalse() {
        Collection<String> col = Sets.newHashSet();
        assertFalse(CollectionUtil.isSingle(col));
    }

    @Test
    public void testIsSingleMapFalse() {
        Map<String, ?> col = Maps.newHashMap();
        assertFalse(CollectionUtil.isSingle(col));
    }

    @Test
    public void testIsSingleColMore() {
        Collection<String> col = Sets.newHashSet();
        col.add("G");
        col.add("More");
        assertFalse(CollectionUtil.isSingle(col));
    }

    @Test
    public void testIsSingleMapMore() {
        Map<String, String> col = Maps.newHashMap();
        col.put("Nice", "Girl");
        col.put("Happy", "Girl");
        assertFalse(CollectionUtil.isSingle(col));
    }

}