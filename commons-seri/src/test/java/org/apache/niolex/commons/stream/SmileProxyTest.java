/**
 * JsonProxyTest.java
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
package org.apache.niolex.commons.stream;

import static org.junit.Assert.*;
import static org.apache.niolex.commons.seri.SmileUtil.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;

import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.test.Benchmark.Bean;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-7-23
 */
public class SmileProxyTest {

	/**
	 * Test method for {@link org.apache.niolex.commons.stream.JsonProxy#readObject(java.lang.Class)}.
	 * @throws Exception
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	@Test
	public void testReadObjectClassOfT() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Bean t = new Bean(3, "Qute", 12212, new Date(1338008328709L));
		Bean q = new Bean(5, "Another", 523212, new Date(1338008328334L));
		writeObj(out, t);
		writeObj(out, q);
		out.close();
		System.out.println("s => " + StringUtil.utf8ByteToStr(out.toByteArray()));
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		SmileProxy proxy = new SmileProxy(in);
		Bean r = proxy.readObject(Bean.class);
		Bean s = proxy.readObject(Bean.class);
		assertEquals(t, r);
		assertEquals(q, s);
		System.out.println("q => " + r.getLikely() + ", " + s.getLikely());
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.stream.JsonProxy#readObject(org.codehaus.jackson.type.TypeReference)}.
	 */
	@Test
	public void testReadObjectTypeReferenceOfQ() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadObject()
	 throws Exception {

	}

}
