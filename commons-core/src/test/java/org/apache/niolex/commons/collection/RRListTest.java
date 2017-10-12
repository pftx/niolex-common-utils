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

import static org.junit.Assert.*;

import org.apache.niolex.commons.collection.CircularList;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-6-14
 */
public class RRListTest {

	/**
	 * Test method for {@link org.apache.niolex.commons.collection.CircularList#size()}.
	 */
	@Test
	public void testSize() {
		CircularList<Integer> rr = new CircularList<Integer>(9);
		assertEquals(0, rr.size());
		rr.add(123);
		assertEquals(1, rr.size());
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.collection.CircularList#add(int, java.lang.Object)}.
	 */
	@Test
	public void testAddIntE() {
		CircularList<Integer> rr = new CircularList<Integer>(9);
		assertEquals(0, rr.size());
		rr.add(123);
		rr.add(345);
		rr.add(1234);
		rr.add(4);
		rr.add(134);
		rr.add(6453);
		rr.add(1235623);
		rr.add(67213);
		rr.add(1534);
		assertEquals(9, rr.size());
		assertEquals(123, rr.get(0).intValue());
		assertEquals(6453, rr.get(5).intValue());
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.collection.CircularList#get(int)}.
	 */
	@Test
	public void testGetInt() {
		CircularList<Integer> rr = new CircularList<Integer>(5);
		assertEquals(0, rr.size());
		rr.add(123);
		rr.add(345);
		rr.add(1234);
		rr.add(4);
		rr.add(134);
		rr.add(6453);
		rr.add(1235623);
		rr.add(67213);
		rr.add(1534);
		assertEquals(5, rr.size());
		assertEquals(134, rr.get(0).intValue());
		assertEquals(67213, rr.get(3).intValue());
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.collection.CircularList#get(int)}.
	 */
	@Test(expected=IndexOutOfBoundsException.class)
	public void testGetIntExc() {
		CircularList<Integer> rr = new CircularList<Integer>(5);
		assertEquals(0, rr.size());
		rr.add(123);
		rr.add(345);
		rr.add(1234);
		assertEquals(3, rr.size());
		rr.add(4);
		rr.add(134);
		rr.add(6453);
		rr.add(1235623);
		rr.add(67213);
		rr.add(1534);
		assertEquals(5, rr.size());
		assertEquals(6453, rr.get(5).intValue());
	}
}
