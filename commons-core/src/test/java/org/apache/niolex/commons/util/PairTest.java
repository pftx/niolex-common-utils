/**
 * PairTest.java
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

import java.util.Date;

import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2012-11-12$
 */
public class PairTest {

	/**
	 * Test method for {@link org.apache.niolex.commons.util.Pair#Pair(java.lang.Object, java.lang.Object)}.
	 */
	@Test
	public void testPairAB() {
		Pair<String, Date> p = new Pair<String, Date>("gogo", new Date());
		assertEquals("gogo", p.a);
	}

}
