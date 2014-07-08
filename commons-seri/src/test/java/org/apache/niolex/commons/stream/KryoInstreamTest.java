/**
 * KryoInstreamTest.java
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.niolex.commons.test.Benchmark;
import org.apache.niolex.commons.test.Benchmark.Bean;
import org.apache.niolex.commons.util.DateTimeUtil;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-7-23
 */
public class KryoInstreamTest {

	/**
	 * Test method for {@link org.apache.niolex.commons.stream.KryoInstream#readObject(java.lang.Class)}.
	 * @throws IOException
	 */
	@Test
	public void testReadObject() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		KryoOutstream ooo = new KryoOutstream(bos);
		Bean q = new Bean(5, "Another", 523212, new Date(1338008328334L));
		Benchmark bench = Benchmark.makeBenchmark();
		ooo.writeObject(bench);
		ooo.writeObject(q);
		ooo.close();
		byte[] bs = bos.toByteArray();
		System.out.println("L " + bs.length);
		// ----------
		KryoInstream iii = new KryoInstream(new ByteArrayInputStream(bs));
		Benchmark cp = iii.readObject(Benchmark.class);
		Bean t = iii.readObject(Bean.class);
		System.out.println("I " + t.getId());
		assertEquals(t.getBirth(), q.getBirth());
		assertEquals(bench, cp);
		iii.close();
	}

    @Test
    public void testKryoInstreamInputStream() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        KryoOutstream ooo = new KryoOutstream(bos);
        ooo.writeObject(123);
        ooo.writeObject("Lex");
        ooo.writeObject(DateTimeUtil.parseDateFromDateStr("2014-06-07"));
        ooo.close();
        byte[] bs = bos.toByteArray();
        System.out.println("L " + bs.length);
        // ----------
        KryoInstream iii = new KryoInstream(new ByteArrayInputStream(bs));
        Integer i = iii.readObject(Integer.class);
        assertEquals(123, i.intValue());
        String s = iii.readObject(String.class);
        assertEquals("Lex", s);
        Date d = iii.readObject(Date.class);
        assertEquals("2014-06-07", DateTimeUtil.formatDate2DateStr(d));
        iii.close();
    }

}
