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
package org.apache.niolex.commons.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-5-31
 */
public class CollectionUtilsTest {

	/**
	 * Test method for {@link org.apache.niolex.commons.util.CollectionUtils#concat(java.util.Collection, E[])}.
	 */
	@Test
	public void testConcatCollectionOfEEArray() {
		List<String> dest = new ArrayList<String>(3);
		dest.add("methods1");
		dest.add("methods2");
		dest.add("methods3");
		dest = CollectionUtils.concat(dest, "Nice", "Meet");
		assertEquals(5, dest.size());
		assertEquals("Nice", dest.get(3));
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.util.CollectionUtils#concat(E[])}.
	 */
	@Test
	public void testConcatEArray() {
		List<String> dest = CollectionUtils.concat("You", "Nice", "Meet");
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
		dest = CollectionUtils.concat(dest, dest2);
		assertEquals(6, dest.size());
		assertEquals("methods22", dest.get(4));
	}

}
