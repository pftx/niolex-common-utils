/**
 * WordReverseTest.java
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
 * @since 2012-10-19
 */
public class WordReverseTest {

	@Test
	public void testNormal6() {
		String s = "I am a student.";
		String b = WordReverse.wordReverse(s);
		System.out.println(b);
		assertEquals(b, "student. a am I");
	}

	@Test
	public void testNormal5() {
		String s = "Prints a String and then terminate the line.";
		String b = WordReverse.wordReverse(s);
		System.out.println(b);
		assertEquals(b, "line. the terminate then and String a Prints");
	}
}
