/**
 * OSInfoTest.java
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
package org.apache.niolex.commons.remote;

import java.io.IOException;

import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-8-2
 */
public class OSInfoTest {

	/**
	 * Test method for {@link org.apache.niolex.commons.remote.OSInfo#invoke(java.io.OutputStream, java.lang.String[])}.
	 * @throws IOException
	 */
	@Test
	public void testInvoke() throws IOException {
		OSInfo os = new OSInfo();
		os.invoke(System.out, null);
		System.err.println("----------------");
	}

	@Test
	public void testGetter() throws IOException {
		OSInfo os = new OSInfo();
		System.out.println(os.getOsName());
		System.out.println(os.getOsmxb());
		System.out.println(os.getSystemInfo());
		System.out.println(os.getOsArch());
		System.out.println(os.getOsVersion());
	}

}
