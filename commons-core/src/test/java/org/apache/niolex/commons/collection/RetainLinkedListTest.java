/**
 * RetainLinkedListTest.java
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

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.niolex.commons.reflect.FieldUtil;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5, $Date: 2012-11-23$
 */
public class RetainLinkedListTest {

	/**
	 * Test method for {@link org.apache.niolex.commons.collection.RetainLinkedList#RetainLinkedList(int)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testRetainLinkedList() {
		new RetainLinkedList<String>(0).getClass();
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.collection.RetainLinkedList#add(java.lang.Object)}.
	 */
	@Test
	public void testAddAndSize() {
		RetainLinkedList<String> list = new RetainLinkedList<String>(3);
		list.add("It ");
		assertEquals(list.size(), 1);
		list.add("is ");
		assertEquals(list.size(), 2);
		assertEquals(list.handleSize(), 2);
		list.add("me");
		assertEquals(list.handleNext(), "It ");
		assertEquals(list.size(), 3);
		assertEquals(list.handleSize(), 2);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.collection.RetainLinkedList#handleNext()}.
	 */
	@Test
	public void testHandleNext() {
		RetainLinkedList<String> list = new RetainLinkedList<String>(3);
		list.add("It ");
		list.add("is ");
		list.add("a ");
		list.add("big ");
		list.add("world!");
		String w = "";
		while (!list.handleEmpty()) {
			w += list.handleNext();
		}
		assertEquals(w, "It is a big world!");
		assertEquals(list.size(), 3);
		assertEquals(list.handleSize(), 0);
	}

	/**
     * Test method for {@link org.apache.niolex.commons.collection.RetainLinkedList#handleNext()}.
     */
    @Test(expected=NullPointerException.class)
    public void testHandleNextCover() throws Exception {
        RetainLinkedList<String> other = new RetainLinkedList<String>(3);
        other.add("It ");
        other.add("is ");
        other.add("a ");
        other.add("big ");
        other.add("world!");
        RetainLinkedList<String> list = new RetainLinkedList<String>(3);
        Field field = FieldUtil.getField(RetainLinkedList.class, "headPointerSize");
        FieldUtil.setFieldValue(field, list, 5);
        // ---
        field = FieldUtil.getField(RetainLinkedList.class, "pointer");
        Object ppt = FieldUtil.getFieldValue(field, other);
        FieldUtil.setFieldValue(field, list, ppt);
        list.handleNext();
    }

	/**
	 * Test method for {@link org.apache.niolex.commons.collection.RetainLinkedList#handleRetain()}.
	 */
	@Test
	public void testHandleRetain() {
		RetainLinkedList<String> list = new RetainLinkedList<String>(3);
		list.add("It ");
		list.add("is ");
		list.add("a ");
		list.add("big ");
		list.add("world!");
		String w = "";
		while (list.handleNext() != null) {
			list.handleNext();
		}
		while (!list.isEmpty()) {
			w += list.handleRetain();
		}
		assertEquals(w, "a big world!");
		assertEquals(list.size(), 0);
		assertEquals(list.handleSize(), 0);
	}

	/**
     * Test method for {@link org.apache.niolex.commons.collection.RetainLinkedList#handleRetain()}.
     */
    @Test(expected=NullPointerException.class)
    public void testHandleRetainCover() throws Exception {
        RetainLinkedList<String> list = new RetainLinkedList<String>(3);
        Field field = FieldUtil.getField(RetainLinkedList.class, "headPointerSize");
        FieldUtil.setFieldValue(field, list, 2);
        list.handleRetain();
    }

	/**
	 * Test method for {@link org.apache.niolex.commons.collection.RetainLinkedList#addAll(org.apache.niolex.commons.collection.RetainLinkedList)}.
	 */
	@Test
	public void testAddAllRetainLinkedListOfE() {
		List<String> list = new ArrayList<String>();
		list.add("It ");
		list.add("is ");
		list.add("a ");
		list.add("big ");
		list.add("world!");
		RetainLinkedList<String> list2 = new RetainLinkedList<String>(3);
		list2.addAll(list);
		assertEquals(null, list2.handleRetain());
		String w = "";
		while (list2.handleNext() != null) {
			list2.handleNext();
		}
		while (!list2.isEmpty()) {
			w += list2.handleRetain();
		}
		assertEquals(w, "a big world!");
		assertEquals(list2.size(), 0);
		assertEquals(list2.handleSize(), 0);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.collection.RetainLinkedList#addAll(java.util.Collection)}.
	 */
	@Test
	public void testAddAllCollectionOfE() {
		RetainLinkedList<String> list = new RetainLinkedList<String>(3);
		list.add("It ");
		list.add("is ");
		list.add("a ");
		list.add("big ");
		list.add("world!");
		RetainLinkedList<String> list2 = new RetainLinkedList<String>(5);
		list2.addAll(list);
		list2.add("Good");
		list2.add("Not yet implemented");
		while (list2.handleNext() != null) {
			list2.handleNext();
		}
		assertTrue(list2.handleEmpty());
		assertEquals(list2.handleSize(), 0);
		assertFalse(list2.isEmpty());
		assertEquals(list2.size(), 5);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.collection.RetainLinkedList#toArray(E[])}.
	 */
	@Test
	public void testToArray() {
		RetainLinkedList<String> list = new RetainLinkedList<String>(3);
		list.add("It ");
		list.add("is ");
		list.add("a ");
		list.add("big ");
		list.add("world!");
		String[] arr = new String[6];
		arr[5] = "not me.";
		list.toArray(arr);
		assertEquals(arr[5], "not me.");
		assertEquals(arr[4], "world!");
		String w = "";
		while (!list.handleEmpty()) {
			list.handleNext();
		}
		assertEquals(list.size(), 3);
		assertEquals(list.handleSize(), 0);
		arr = new String[2];
		list.toArray(arr);
		assertEquals(arr[0], "a ");
		assertEquals(arr[1], "big ");
		while (!list.isEmpty()) {
			w += list.handleRetain();
		}
		assertEquals(w, "a big world!");
		assertEquals(list.size(), 0);
		assertEquals(list.handleSize(), 0);
	}

	/**
     * Test method for {@link org.apache.niolex.commons.collection.RetainLinkedList#toArray(E[])}.
     */
    @Test(expected=NullPointerException.class)
    public void testToArrayCover() {
        RetainLinkedList<String> list = new RetainLinkedList<String>(3);
        list.toArray(null);
    }

	/**
	 * Test method for {@link org.apache.niolex.commons.collection.RetainLinkedList#isEmpty()}.
	 */
	@Test
	public void testIsEmpty() {
		RetainLinkedList<String> list = new RetainLinkedList<String>(3);
		list.add("It ");
		list.add("is ");
		list.add("a ");
		list.add("big ");
		list.add("world!");
		assertFalse(list.isEmpty());
		assertFalse(list.handleEmpty());
		String w = "";
		while (!list.handleEmpty()) {
			list.handleNext();
		}
		assertFalse(list.isEmpty());
		assertTrue(list.handleEmpty());
		assertEquals(list.size(), 3);
		assertEquals(list.handleSize(), 0);
		while (!list.isEmpty()) {
			w += list.handleRetain();
		}
		assertEquals(w, "a big world!");
		assertEquals(list.size(), 0);
		assertEquals(list.handleSize(), 0);
		assertTrue(list.isEmpty());
		assertTrue(list.handleEmpty());
	}

    @Test(expected=NullPointerException.class)
    public void testAdd() throws Exception {
        RetainLinkedList<String> list = new RetainLinkedList<String>(3);
        Field field = FieldUtil.getField(RetainLinkedList.class, "tail");
        FieldUtil.setFieldValue(field, list, null);
        list.add("hello");
    }

}
