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

import static org.apache.niolex.commons.seri.SmileUtil.writeObj;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;

import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.test.Benchmark.Bean;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-7-23
 */
public class SmileProxyTest {

	/**
	 * Test method for {@link org.apache.niolex.commons.stream.JsonProxy#readObject(java.lang.Class)}.
	 * @throws Exception
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	@Test
	public void testReadObject() throws Exception {
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
        Bean s = proxy.readObject(new com.fasterxml.jackson.core.type.TypeReference<Bean>() {
        });
		assertEquals(t, r);
		assertEquals(q, s);
		System.out.println("q => " + r.getLikely() + ", " + s.getLikely());
	}

    @Test
    public void testSmileProxy() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeObj(out, "Lex");
        writeObj(out, 2);
        writeObj(out, 4);
        writeObj(out, " Hello");
        System.out.println("s => " + StringUtil.utf8ByteToStr(out.toByteArray()));
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        SmileProxy proxy = new SmileProxy(in);
        String a = proxy.readObject(String.class);
        assertEquals("Lex", a);
        Integer b = proxy.readObject(Integer.class);
        assertEquals(2, b.intValue());
        b = proxy.readObject(Integer.class);
        assertEquals(4, b.intValue());
        String c = proxy.readObject(String.class);
        assertEquals(" Hello", c);
    }

}
