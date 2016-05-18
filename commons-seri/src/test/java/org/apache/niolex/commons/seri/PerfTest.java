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
import java.lang.reflect.Type;
import java.util.Date;

import org.apache.niolex.commons.compress.JacksonUtil;
import org.apache.niolex.commons.stream.JsonProxy;
import org.apache.niolex.commons.stream.KryoInstream;
import org.apache.niolex.commons.stream.KryoOutstream;
import org.apache.niolex.commons.stream.SmileProxy;
import org.apache.niolex.commons.test.Benchmark;
import org.apache.niolex.commons.test.Benchmark.Bean;
import org.apache.niolex.commons.test.MultiPerformance;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-7-23
 */
public class PerfTest {

    private static int threadsCount = 4;
	private static int innerIteration = 100;
	private static int outerIteration = 100;

	abstract class MyPerformance extends MultiPerformance {
	    
	    public int size;

        public MyPerformance() {
            super(threadsCount, innerIteration, outerIteration);
        }

        @Override
        public void start() {
            super.start();
            System.out.println("\tSize " + size);
        }

	}

	class Kryoo extends MyPerformance {
		Benchmark bench = Benchmark.makeBenchmark();
		Bean q = new Bean(5, "Another", 523212, new Date(1338008328334L));

		public Kryoo() {
			System.out.print("Kryoo ");
		}

		/**
		 * Override super method
		 * @see org.apache.niolex.commons.test.Performance#run()
		 */
		@Override
		protected void run() {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			KryoOutstream ooo = new KryoOutstream(bos);
			ooo.writeObject(bench);
			ooo.writeObject(q);
			ooo.close();
			byte[] bs = bos.toByteArray();
			if (size == 0) size = bs.length;
			KryoInstream iii = new KryoInstream(new ByteArrayInputStream(bs));
			Benchmark cp = iii.readObject(Benchmark.class);
			Bean t = iii.readObject(Bean.class);
			assertTrue(t.getId() != 0);
			assertTrue(t.getBirth().getTime() == 1338008328334L);
			assertTrue(bench.equals(cp));
		}
	}

	class Smile extends MyPerformance {
		Benchmark bench = Benchmark.makeBenchmark();
		Bean q = new Bean(5, "Another", 523212, new Date(1338008328334L));

		/**
		 * @param innerIteration
		 * @param outerIteration
		 */
		public Smile() {
			System.out.print("Smile ");
		}

		/**
		 * Override super method
		 * @see org.apache.niolex.commons.test.Performance#run()
		 */
		@Override
		protected void run() {
			try {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				SmileUtil.writeObj(bos, bench);
				SmileUtil.writeObj(bos, q);
				byte[] bs = bos.toByteArray();
				if (size == 0) size = bs.length;
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

	class Json extends MyPerformance {
		Benchmark bench = Benchmark.makeBenchmark();
		Bean q = new Bean(5, "Another", 523212, new Date(1338008328334L));

		/**
		 * @param innerIteration
		 * @param outerIteration
		 */
		public Json() {
			System.out.print("Json ");
		}

		/**
		 * Override super method
		 * @see org.apache.niolex.commons.test.Performance#run()
		 */
		@Override
		protected void run() {
			try {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				JacksonUtil.writeObj(bos, bench);
				JacksonUtil.writeObj(bos, q);
				byte[] bs = bos.toByteArray();
				if (size == 0) size = bs.length;
				JsonProxy iii = new JsonProxy(new ByteArrayInputStream(bs));
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

	class Stuff extends MyPerformance {
	    Benchmark bench = Benchmark.makeBenchmark();
	    Bean q = new Bean(674, "Another", 452723, new Date(5354482831545L));

	    public Stuff() {
	        System.out.print("Stuff ");
	    }

	    /**
	     * Override super method
	     * @see org.apache.niolex.commons.test.Performance#run()
	     */
	    @Override
	    protected void run() {
	        byte[] bs = ProtoStuffUtil.seriMulti(new Object[] {bench, q});
	        if (size == 0) size = bs.length;
	        Type[] types = new Type[] {Benchmark.class, Bean.class};
	        Object[] back = ProtoStuffUtil.parseMulti(bs, types);
	        Benchmark cp = (Benchmark) back[0];
	        Bean t = (Bean) back[1];
	        assertTrue(t.getId() == 452723);
	        assertTrue(t.getBirth().getTime() == 5354482831545L);
	        assertTrue(bench.equals(cp));
	    }
	}

	@Test
	public void testPerf() {
		new Kryoo().start();
		new Smile().start();
		new Json().start();
		new Stuff().start();
	}
}
