/**
 * PerfTest.java
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
package org.apache.niolex.commons.seri;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.niolex.commons.stream.KryoInstream;
import org.apache.niolex.commons.stream.KryoOutstream;
import org.apache.niolex.commons.stream.SmileProxy;
import org.apache.niolex.commons.test.Benchmark;
import org.apache.niolex.commons.test.Benchmark.Bean;
import org.apache.niolex.commons.test.Performance;
import org.junit.Test;

import com.esotericsoftware.kryo.Kryo;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-7-23
 */
public class PerfTest {

	private static int innerIteration = 100;
	private static int outerIteration = 100;

	class Kryoo extends Performance {
		Kryo kryo = new Kryo();
		Benchmark bench = Benchmark.makeBenchmark();
		byte[] bs;

		/**
		 * @param innerIteration
		 * @param outerIteration
		 */
		public Kryoo() {
			super(innerIteration, outerIteration);
			System.out.print("Kryoo\t");
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			KryoOutstream ooo = new KryoOutstream(kryo, bos);
			Bean q = new Bean(5, "Another", 523212, new Date(1338008328334L));
			ooo.writeObject(bench);
			ooo.writeObject(q);
			ooo.close();
			bs = bos.toByteArray();
		}

		/**
		 * Override super method
		 * @see org.apache.niolex.commons.test.Performance#run()
		 */
		@Override
		protected void run() {
			KryoInstream iii = new KryoInstream(kryo, new ByteArrayInputStream(bs));
			Benchmark cp = iii.readObject(Benchmark.class);
			Bean t = iii.readObject(Bean.class);
			assertTrue(t.getId() != 0);
			assertTrue(t.getBirth().getTime() == 1338008328334L);
			assertTrue(bench.equals(cp));
		}
	}

	class Smile extends Performance {
		Benchmark bench = Benchmark.makeBenchmark();
		byte[] bs;

		/**
		 * @param innerIteration
		 * @param outerIteration
		 */
		public Smile() {
			super(innerIteration, outerIteration);
			System.out.print("Smile\t");
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			Bean q = new Bean(5, "Another", 523212, new Date(1338008328334L));
			try {
				SmileUtil.writeObj(bos, bench);
				SmileUtil.writeObj(bos, q);
			} catch (IOException e) {
				e.printStackTrace();
			}
			bs = bos.toByteArray();
		}

		/**
		 * Override super method
		 * @see org.apache.niolex.commons.test.Performance#run()
		 */
		@Override
		protected void run() {
			try {
				SmileProxy iii = new SmileProxy(new ByteArrayInputStream(bs));
				Benchmark cp = iii.readObject(Benchmark.class);
				Bean t = iii.readObject(Bean.class);
				assertTrue(t.getId() != 0);
				assertTrue(t.getBirth().getTime() == 1338008328334L);
				assertTrue(bench.equals(cp));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void test() {
		new SmileUtil() {};
		new Kryoo().start();
		new Smile().start();
	}
}
