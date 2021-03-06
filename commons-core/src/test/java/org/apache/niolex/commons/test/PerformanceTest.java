/**
 * PerformanceTest.java
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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.niolex.commons.reflect.FastFieldUtil;
import org.apache.niolex.commons.reflect.FastMethodUtil;
import org.apache.niolex.commons.reflect.FieldUtil;
import org.apache.niolex.commons.reflect.MethodUtil;
import org.junit.Test;

import com.esotericsoftware.reflectasm.FieldAccess;
import com.esotericsoftware.reflectasm.MethodAccess;

/**
 * --------------------------------------------------------
 * DirectMethod PERF DONE, TOTAL TIME - 33ms.
 * PrimitiveField PERF DONE, TOTAL TIME - 35ms.
 * DirectField PERF DONE, TOTAL TIME - 40ms.
 * FastPrimitiveField PERF DONE, TOTAL TIME - 75ms.
 * FastField    PERF DONE, TOTAL TIME - 84ms.
 * FastMethod PERF DONE, TOTAL TIME - 127ms.
 * --------------------------------------------------------
 * ReflectMethod    MULT PERF DONE, TOTAL TIME - 289ms.
 * ReflectField MULT PERF DONE, TOTAL TIME - 3110ms.
 *
 * setAccessable = true then:
 * ReflectField MULT PERF DONE, TOTAL TIME - 262ms.
 * ReflectMethod    MULT PERF DONE, TOTAL TIME - 99ms.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-7-23
 */
public class PerformanceTest {

	public interface KTestI {
		public Integer getB();
		public int getI();
	}

	public class KTestBean implements KTestI {
		Integer b;
		int i;

		public Integer getB() {
			return b;
		}

		public void setB(Integer b) {
			this.b = b;
		}

		public int getI() {
			return i;
		}

		public void setI(int i) {
			this.i = i;
		}

	}

	private static int innerIteration = 100000;
	private static int outerIteration = 100;

	class DirectMethod extends Performance {
		KTestBean bean = new KTestBean();

		/**
		 * @param innerIteration
		 * @param outerIteration
		 */
		public DirectMethod() {
			super(innerIteration, outerIteration);
			bean.setB(8475);
			System.out.print("DirectMethod ");
		}

		/**
		 * Override super method
		 * @see org.apache.niolex.commons.test.Performance#run()
		 */
		@Override
		protected void run() {
			if (bean.getB().intValue() != 8475) {
				throw new RuntimeException("This is so bad!");
			}
		}

	}

	class DirectField extends Performance {
		KTestBean bean = new KTestBean();

		/**
		 * @param innerIteration
		 * @param outerIteration
		 */
		public DirectField() {
			super(innerIteration, outerIteration);
			bean.setB(8877);
			System.out.print("DirectField ");
		}

		/**
		 * Override super method
		 * @see org.apache.niolex.commons.test.Performance#run()
		 */
		@Override
		protected void run() {
			if (bean.b.intValue() != 8877) {
				throw new RuntimeException("This is so bad!");
			}
		}

	}

	class FastMethod extends Performance {
		KTestBean bean = new KTestBean();
		MethodAccess acc = FastMethodUtil.getMethodAccess(KTestBean.class);
		int idx = acc.getIndex("getB");

		/**
		 * @param innerIteration
		 * @param outerIteration
		 */
		public FastMethod() {
			super(innerIteration, outerIteration);
			bean.setB(7245);
			System.out.print("FastMethod ");
		}

		/**
		 * Override super method
		 * @see org.apache.niolex.commons.test.Performance#run()
		 */
		@Override
		protected void run() {
			if (((Integer)acc.invoke(bean, idx)).intValue() != 7245) {
				throw new RuntimeException("This is so bad!");
			}
		}

	}

	class FastField extends Performance {
		KTestBean bean = new KTestBean();
		FieldAccess acc = FastFieldUtil.getFieldAccess(KTestBean.class);
		int idx = acc.getIndex("b");

		/**
		 * @param innerIteration
		 * @param outerIteration
		 */
		public FastField() {
			super(innerIteration, outerIteration);
			bean.setB(78345);
			System.out.print("FastField ");
		}

		/**
		 * Override super method
		 * @see org.apache.niolex.commons.test.Performance#run()
		 */
		@Override
		protected void run() {
			if (((Integer)acc.get(bean, idx)).intValue() != 78345) {
				throw new RuntimeException("This is so bad!");
			}
		}

	}

	class ReflectMethod extends MultiPerformance {
		KTestBean bean = new KTestBean();
		Method m;

		/**
		 * @param innerIteration
		 * @param outerIteration
		 */
		public ReflectMethod() {
			super(4, innerIteration, outerIteration);
			bean.setB(6465);
			try {
				m = MethodUtil.getMethod(KTestBean.class, "getB");
				m.setAccessible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.print("ReflectMethod\t");
		}

		/**
		 * Override super method
		 * @see org.apache.niolex.commons.test.Performance#run()
		 */
		@Override
		protected void run() {
			try {
				if (((Integer) m.invoke(bean)).intValue() != 6465) {
					throw new RuntimeException("This is so bad!");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	class ReflectField extends MultiPerformance {
		KTestBean bean = new KTestBean();
		Field f;

		/**
		 * @param innerIteration
		 * @param outerIteration
		 */
		public ReflectField() {
			super(4, innerIteration, outerIteration);
			bean.setB(783234);
			try {
				f = FieldUtil.getField(KTestBean.class, "b");
				f.setAccessible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.print("ReflectField\t");
		}

		/**
		 * Override super method
		 * @see org.apache.niolex.commons.test.Performance#run()
		 */
		@Override
		protected void run() {
			try {
				if (((Integer) f.get(bean)).intValue() != 783234) {
					throw new RuntimeException("This is so bad!");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.Performance#run()}.
	 */
	@Test
	public void testDirM() {
		new DirectMethod().start();
	}

	@Test
	public void testDirF() {
	    new DirectField().start();
	}

	@Test
	public void testFastM() {
	    new FastMethod().start();
	}

	@Test
	public void testFastF() {
	    new FastField().start();
	}

	@Test
	public void testRefM() {
	    new ReflectMethod().start();
	}

	@Test
	public void testRefF() {
	    new ReflectField().start();
	}

	class PrimitiveField extends Performance {
		KTestBean bean = new KTestBean();

		/**
		 * @param innerIteration
		 * @param outerIteration
		 */
		public PrimitiveField() {
			super(innerIteration, outerIteration);
			bean.setI(5956);
			System.out.print("PrimitiveField ");
		}

		/**
		 * Override super method
		 * @see org.apache.niolex.commons.test.Performance#run()
		 */
		@Override
		protected void run() {
			if (bean.i != 5956) {
				throw new RuntimeException("This is so bad!");
			}
		}

	}

	class FastPrimitiveField extends Performance {
		KTestBean bean = new KTestBean();
		FieldAccess acc = FastFieldUtil.getFieldAccess(KTestBean.class);
		int idx = acc.getIndex("i");

		/**
		 * @param innerIteration
		 * @param outerIteration
		 */
		public FastPrimitiveField() {
			super(innerIteration, outerIteration);
			bean.setI(89456);
			System.out.print("FastPrimitiveField ");
		}

		/**
		 * Override super method
		 * @see org.apache.niolex.commons.test.Performance#run()
		 */
		@Override
		protected void run() {
			if (acc.getInt(bean, idx) != 89456) {
				throw new RuntimeException("This is so bad!");
			}
		}

	}

	@Test
	public void testPrimitiveField() throws Exception {
		new PrimitiveField().start();
	}

	@Test
	public void testFastPrimitiveField() throws Exception {
	    new FastPrimitiveField().start();
	}

}
