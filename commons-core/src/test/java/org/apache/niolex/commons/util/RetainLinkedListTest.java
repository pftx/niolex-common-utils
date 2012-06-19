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
package org.apache.niolex.commons.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-6-19
 */
public class RetainLinkedListTest {

	/**
	 * Test method for {@link org.apache.niolex.commons.util.RetainLinkedList#RetainLinkedList(int)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testRetainLinkedList() {
		new RetainLinkedList<String>(0);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.util.RetainLinkedList#add(java.lang.Object)}.
	 */
	@Test
	public void testAdd() {
		RetainLinkedList<String> a = new RetainLinkedList<String>(3);
		a.add("NIce");
		a.add("to");
		a.add("meet");
		a.add("Not yet implemented");
		assertEquals("NIce", a.handleNext());
		assertEquals("to", a.handleNext());
		assertEquals("meet", a.handleNext());
		assertEquals("Not yet implemented", a.handleNext());
		assertEquals(null, a.handleNext());
		a.add("You");
		assertEquals("You", a.handleNext());
		assertEquals(3, a.size());
		String[] arr = new String[3];
		a.toArray(arr);
		assertEquals("meet", arr[0]);
		assertEquals("Not yet implemented", arr[1]);
		assertEquals("You", arr[2]);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.util.RetainLinkedList#handleNext()}.
	 */
	@Test
	public void testHandleNext() {
		RetainLinkedList<String> a = new RetainLinkedList<String>(1);
		a.add("NIce");
		a.add("to");
		a.add("meet");
		a.add("Not yet implemented");
		assertEquals(4, a.size());
		assertEquals("NIce", a.handleNext());
		assertEquals(4, a.size());
		assertEquals("to", a.handleNext());
		assertEquals("meet", a.handleNext());
		assertEquals("Not yet implemented", a.handleNext());
		assertEquals(1, a.size());
		assertEquals(null, a.handleNext());
		a.add("You");
		assertEquals(2, a.size());
		assertEquals("You", a.handleNext());
		assertEquals(1, a.size());
		String[] arr = new String[1];
		a.toArray(arr);
		assertEquals("You", arr[0]);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.util.RetainLinkedList#addAll(org.apache.niolex.commons.util.RetainLinkedList)}.
	 */
	@Test
	public void testAddAllRetainLinkedListOfE() {
		RetainLinkedList<Integer> inn = new RetainLinkedList<Integer>(1);
		inn.add(5234);
		inn.add(4);
		inn.add(6);
		inn.add(132);
		assertEquals(4, inn.size());
		RetainLinkedList<Integer> a = new RetainLinkedList<Integer>(5);
		a.addAll(inn);
		assertEquals(1, inn.size());
		assertEquals(4, a.size());
		Integer[] arr = new Integer[4];
		a.toArray(arr);
		assertEquals(5234, arr[0].intValue());
		assertEquals(4, arr[1].intValue());
		assertEquals(6, arr[2].intValue());
		assertEquals(132, arr[3].intValue());
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.util.RetainLinkedList#addAll(java.util.Collection)}.
	 */
	@Test
	public void testAddAllCollectionOfE() {
		List<Integer> inn = new LinkedList<Integer>();
		inn.add(5234);
		inn.add(4);
		inn.add(6);
		inn.add(132);
		assertEquals(4, inn.size());
		RetainLinkedList<Integer> a = new RetainLinkedList<Integer>(5);
		a.addAll(inn);
		assertEquals(4, inn.size());
		assertEquals(4, a.size());
		Integer[] arr = new Integer[4];
		a.toArray(arr);
		assertEquals(5234, arr[0].intValue());
		assertEquals(4, arr[1].intValue());
		assertEquals(6, arr[2].intValue());
		assertEquals(132, arr[3].intValue());
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.util.RetainLinkedList#toArray(E[])}.
	 */
	@Test
	public void testToArray() {
		RetainLinkedList<Integer> inn = new RetainLinkedList<Integer>(1);
		inn.add(5234);
		inn.add(4);
		inn.add(6);
		inn.add(132);
		inn.add(45234);
		inn.add(65234);
		assertEquals(6, inn.size());
		Integer[] arr = new Integer[4];
		inn.toArray(arr);
		assertEquals(5234, arr[0].intValue());
		assertEquals(4, arr[1].intValue());
		assertEquals(6, arr[2].intValue());
		assertEquals(132, arr[3].intValue());
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.util.RetainLinkedList#isEmpty()}.
	 */
	@Test
	public void testIsEmpty() {
		RetainLinkedList<Integer> inn = new RetainLinkedList<Integer>(1);
		assertTrue(inn.isEmpty());
		inn.add(5234);
		assertFalse(inn.isEmpty());
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.util.RetainLinkedList#size()}.
	 */
	@Test
	public void testSize() {
		RetainLinkedList<Integer> inn = new RetainLinkedList<Integer>(1);
		inn.add(5234);
		inn.add(4);
		inn.add(6);
		assertEquals(3, inn.size());
		Integer[] arr = new Integer[4];
		inn.toArray(arr);
		assertEquals(5234, arr[0].intValue());
		assertEquals(4, arr[1].intValue());
		assertEquals(6, arr[2].intValue());
		assertEquals(null, arr[3]);
	}

}
