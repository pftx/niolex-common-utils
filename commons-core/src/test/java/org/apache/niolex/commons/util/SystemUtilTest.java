/**
 * SystemUtilTest.java
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

import static org.junit.Assert.assertTrue;

import java.net.InetAddress;
import java.util.Set;

import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-8-1
 */
public class SystemUtilTest {

	/**
	 * Test method for {@link org.apache.niolex.commons.util.SystemUtil#getAllLocalAddresses()}.
	 * @throws Exception
	 */
	@Test
	public void testGetAllLocalAddresses() throws Exception {
		Set<InetAddress> set = SystemUtil.getAllLocalAddresses();
		System.out.println(set);
		InetAddress test = InetAddress.getByName("localhost");
		assertTrue(set.contains(test));
	}

}
