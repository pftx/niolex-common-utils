/**
 * OrderedRunnerTest.java
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
package org.apache.niolex.commons.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-8-14
 */
@RunWith(OrderedRunner.class)
public class OrderedRunnerTest {
	static int k = 0;

	@Test
	public void testD46() {
		assertEquals(k, 6);
		++k;
	}

	@Test
	public void testD45() {
		assertEquals(k, 5);
		++k;
	}

	@Test
	public void testC5() {
		assertEquals(k, 4);
		++k;
	}

	@Test
	public void testB3() {
		assertEquals(k, 2);
		++k;
	}

	@Test
	public void testC2() {
		assertEquals(k, 3);
		++k;
	}

	@Test
	public void testA2() {
		assertEquals(k, 1);
		++k;
	}

	@Test
	public void testA1() {
		assertEquals(k, 0);
		++k;
	}

}
