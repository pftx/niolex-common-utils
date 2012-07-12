/**
 * LRUHashMapTest.java
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

import org.apache.niolex.commons.collection.LRUHashMap;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-5-31
 */
public class LRUHashMapTest {

	/**
	 * Test method for {@link org.apache.niolex.commons.collection.LRUHashMap#removeEldestEntry(java.util.Map.Entry)}.
	 */
	@Test
	public void testRemoveEldestEntryEntryOfKV() {
		LRUHashMap<String, String> map = new LRUHashMap<String, String>(5);
		map.put("12345", "12345");
		map.put("1234", "12345");
		map.put("123", "12345");
		map.put("12", "12345");
		map.put("1", "12345");
		assertEquals("12345", map.get("12345"));
		assertEquals("12345", map.get("1234"));
		map.put("0", "12345");
		assertNull(map.get("12345"));
		assertEquals("12345", map.get("1234"));
	}

}
