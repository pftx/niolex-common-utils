/**
 * PalindromeTest.java
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
 * @Date: 2012-10-22
 */
public class PalindromeTest {

	@Test
	public void testNormal5() {
		assertEquals(6, Palindrome.palindrome("abccba"));
	}

	@Test
	public void testNormal6() {
		assertEquals(6, Palindrome.palindrome("abaaba"));
	}

	@Test
	public void testNormal1() {
		assertEquals(1, Palindrome.palindrome("g"));
	}

	@Test
	public void testNormal2() {
		assertEquals(9, Palindrome.palindrome("babcbabcbaccba"));
	}

	@Test
	public void testNormal3() {
		assertEquals(3, Palindrome.palindrome("aga"));
	}

	@Test
	public void testNormal4() {
		assertEquals(4, Palindrome.palindrome("agga"));
	}

}
