/**
 * KryoOutstreamTest.java
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

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;

import org.apache.niolex.commons.test.Benchmark;
import org.apache.niolex.commons.test.Benchmark.Bean;
import org.junit.Test;

import com.esotericsoftware.kryo.Kryo;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-7-23
 */
public class KryoOutstreamTest {

	/**
	 * Test method for {@link org.apache.niolex.commons.stream.KryoOutstream#writeObject(java.lang.Object)}.
	 */
	@Test
	public void testWriteObject() {
		Kryo kryo = new Kryo();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		KryoOutstream ooo = new KryoOutstream(kryo, bos);
		Bean q = new Bean(5, "Another", 523212, new Date(1338008328334L));
		Benchmark bench = Benchmark.makeBenchmark();
		ooo.writeObject(bench);
		ooo.writeObject(q);
		ooo.close();
		byte[] bs = bos.toByteArray();
		System.out.println("L " + bs.length);
		KryoInstream iii = new KryoInstream(kryo, new ByteArrayInputStream(bs));
		Benchmark cp = iii.readObject(Benchmark.class);
		Bean t = iii.readObject(Bean.class);
		iii.close();
		System.out.println("I " + t.getId());
		assertEquals(t.getBirth(), q.getBirth());
		assertEquals(bench, cp);
	}

}
