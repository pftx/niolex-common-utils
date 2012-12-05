/**
 * Str2DiffTest.java
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
package org.apache.niolex.common.text;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-10-18
 */
public class Str2DiffTest {

	@Test
	public void testNormal() {
		int k = Str2Diff.diff("GGATCGA", "GAATTCAGTTA");
		System.out.println(k);
		assertEquals(k, 5);
	}

	@Test
	public void testNormal2() {
		int k = Str2Diff.diff("ABC", "ABBC");
		System.out.println(k);
		assertEquals(k, 1);
	}

	@Test
	public void testNormal3() {
		int k = Str2Diff.diff("ABCD", "ABDD");
		System.out.println(k);
		assertEquals(k, 1);
	}

	@Test
	public void testNormal4() {
		int k = Str2Diff.diff("ABCD", "DABC");
		System.out.println(k);
		assertEquals(k, 2);
	}

	@Test
	public void testNormal5() {
		int k = Str2Diff.diff("A", "CCACC");
		System.out.println(k);
		assertEquals(k, 4);
	}

	@Test
	public void testNormal6() {
		int k = Str2Diff.diff("EIDJD", "JD");
		System.out.println(k);
		assertEquals(k, 3);
	}
}
