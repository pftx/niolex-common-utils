/**
 * TidyUtilTest.java
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

import static org.junit.Assert.*;

import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.file.FileUtil;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-5-27
 */
public class TidyUtilTest {

	/**
	 * Test method for {@link org.apache.niolex.commons.test.TidyUtil#removePrefix(java.lang.String, int)}.
	 */
	@Test
	public final void testRemovePrefix() {
	    new TidyUtil() {};
		String str = FileUtil.getCharacterFileContentFromClassPath("Data.txt", TidyUtilTest.class, StringUtil.UTF_8);
		str = TidyUtil.removePrefix(str, 2);
        System.out.println("SL " + str.length());
        assertEquals(654, str.length());
	}

}
