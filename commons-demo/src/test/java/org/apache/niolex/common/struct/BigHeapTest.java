/**
 * BigHeapTest.java
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
package org.apache.niolex.common.struct;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.apache.niolex.commons.test.MockUtil;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-10-24
 */
public class BigHeapTest {

	@Test
	public void testNormal5() {
		BigHeap<Integer> big = new BigHeap<Integer>(5);
		big.push(5);
		big.push(9);
		big.push(3);
		assertEquals(3, big.size());
		assertEquals(9, big.pop().intValue());
		assertEquals(2, big.size());
		assertEquals(5, big.pop().intValue());
		assertEquals(1, big.size());
		assertEquals(3, big.pop().intValue());
		assertEquals(0, big.size());
	}

	@Test
	public void testNormal6() {
		BigHeap<Integer> big = new BigHeap<Integer>(5);
		big.push(5);
		big.push(9);
		big.push(3);
		big.push(13);
		assertTrue(big.push(22));
		assertFalse(big.push(56));
		assertEquals(5, big.size());
		assertEquals(22, big.pop().intValue());
		assertTrue(big.push(56));
		assertEquals(5, big.size());
		assertEquals(56, big.pop().intValue());
		assertEquals(13, big.pop().intValue());
	}

	@Test
	public void testNormal7() {
		final int SIZE = 1000;
		BigHeap<Integer> big = new BigHeap<Integer>(SIZE);
		int[] arr = MockUtil.randIntArray(SIZE);
		for (int i = 0; i < SIZE; ++i) {
			big.push(arr[i]);
		}
		Arrays.sort(arr);
		for (int i = SIZE - 1; i > -1; --i) {
			if(arr[i] != big.pop().intValue()) {
				break;
			}
			assertEquals(i, big.size());
		}
	}

}
