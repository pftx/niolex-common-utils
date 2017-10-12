/**
 * CircularListTest.java
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

import java.util.Iterator;

import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5, $Date: 2012-11-23$
 */
public class CircularListTest {

	@Test(expected=IllegalArgumentException.class)
	public void testSmall() {
		new CircularList<String>(1);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.collection.CircularList#size()}.
	 */
	@Test
	public void testSize() {
		CircularList<String> sList = new CircularList<String>(5);
		sList.add("Well,");
		sList.add("My");
		sList.add(0, "Country");
		assertEquals(3, sList.size());
		sList.add("Is");
		sList.add("Very");
		sList.add("Huge");
		Iterator<String> iter = sList.iterator();
		String w = "";
		while(iter.hasNext()) {
			w += iter.next();
		}
		assertEquals("MyCountryIsVeryHuge", w);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.collection.CircularList#get(int)}.
	 */
	@Test
	public void testGetInt() {
		CircularList<String> sList = new CircularList<String>(5);
		sList.add("Well,");
		sList.add("My");
		sList.add(0, "Country");
		assertEquals(3, sList.size());
		sList.add("Is");
		sList.add("Very");
		sList.add("Huge");
		sList.add("Yes");
		assertEquals(sList.get(0), "Country");
		assertEquals(sList.get(1), "Is");
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void testGetExc() {
		CircularList<String> sList = new CircularList<String>(5);
		sList.add("Well,");
		sList.add("My");
		sList.add(0, "Country");
		assertEquals(3, sList.size());
		sList.add("Is");
		sList.add("Very");
		sList.add("Huge");
		sList.add("Yes");
		assertEquals(sList.get(6), "Yes");
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void testGetExce() {
		CircularList<String> sList = new CircularList<String>(5);
		sList.add("Well,");
		sList.add("My");
		sList.add(0, "Country");
		assertEquals(3, sList.size());
		sList.add("Is");
		sList.add("Very");
		sList.add("Huge");
		sList.add("Yes");
		assertEquals(sList.get(-1), "Yes");
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.collection.CircularList#set(int, java.lang.Object)}.
	 */
	@Test
	public void testSetIntE() {
		CircularList<String> sList = new CircularList<String>(5);
		sList.add("Well,");
		sList.add("My");
		sList.add(0, "Country");
		assertEquals(3, sList.size());
		sList.add("Is");
		sList.add("Very");
		sList.add("Huge");
		sList.add("Yes");
		assertEquals(sList.set(3, "Big"), "Huge");
		assertEquals(sList.set(4, "One"), "Yes");
		Iterator<String> iter = sList.iterator();
		String w = "";
		while(iter.hasNext()) {
			w += iter.next();
		}
		assertEquals("CountryIsVeryBigOne", w);
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void testSetIntEx() {
		CircularList<String> sList = new CircularList<String>(5);
		sList.add("Well,");
		sList.add("My");
		sList.add(0, "Country");
		assertEquals(3, sList.size());
		sList.add("Is");
		sList.add("Very");
		sList.add("Huge");
		sList.add("Yes");
		assertEquals(sList.set(3, "Big"), "Huge");
		assertEquals(sList.set(-1, "One"), "Yes");
		Iterator<String> iter = sList.iterator();
		String w = "";
		while(iter.hasNext()) {
			w += iter.next();
		}
		assertEquals("CountryIsVeryBigOne", w);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.collection.CircularList#add(int, java.lang.Object)}.
	 */
	@Test
	public void testAddIntE() {
		CircularList<String> sList = new CircularList<String>(5);
		sList.add("Well,");
		sList.add("My");
		sList.add(0, "Country");
		assertEquals(3, sList.size());
		sList.add("Is");
		sList.add("Very");
		sList.add("Huge");
		sList.add("Yes");
		assertEquals(sList.set(3, "Big"), "Huge");
		Iterator<String> iter = sList.iterator();
		assertEquals(sList.set(4, "One"), "Yes");
		sList.add("Make");
		String w = "";
		while(iter.hasNext()) {
			w += iter.next();
		}
		assertEquals("IsVeryBigOneMake", w);
	}

}
