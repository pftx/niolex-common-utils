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
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-7-23
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

	private static int innerIteration = 1000000;
	private static int outerIteration = 10;

	class DirectMethod extends Performance {
		KTestBean bean = new KTestBean();

		/**
		 * @param innerIteration
		 * @param outerIteration
		 */
		public DirectMethod() {
			super(innerIteration, outerIteration);
			bean.setB(8475);
			System.out.print("DirectMethod\t");
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
			bean.setB(8475);
			System.out.print("DirectField\t");
		}

		/**
		 * Override super method
		 * @see org.apache.niolex.commons.test.Performance#run()
		 */
		@Override
		protected void run() {
			if (bean.b.intValue() != 8475) {
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
			bean.setB(8475);
			System.out.print("FastMethod\t");
		}

		/**
		 * Override super method
		 * @see org.apache.niolex.commons.test.Performance#run()
		 */
		@Override
		protected void run() {
			if (((Integer)acc.invoke(bean, idx)).intValue() != 8475) {
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
			bean.setB(8475);
			System.out.print("FastField\t");
		}

		/**
		 * Override super method
		 * @see org.apache.niolex.commons.test.Performance#run()
		 */
		@Override
		protected void run() {
			if (((Integer)acc.get(bean, idx)).intValue() != 8475) {
				throw new RuntimeException("This is so bad!");
			}
		}

	}

	class ReflectMethod extends Performance {
		KTestBean bean = new KTestBean();
		Method m;

		/**
		 * @param innerIteration
		 * @param outerIteration
		 */
		public ReflectMethod() {
			super(innerIteration, outerIteration);
			bean.setB(8475);
			try {
				m = MethodUtil.getMethod(KTestBean.class, "getB");
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
				if (((Integer) m.invoke(bean)).intValue() != 8475) {
					throw new RuntimeException("This is so bad!");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	class ReflectField extends Performance {
		KTestBean bean = new KTestBean();
		Field f;

		/**
		 * @param innerIteration
		 * @param outerIteration
		 */
		public ReflectField() {
			super(innerIteration, outerIteration);
			bean.setB(8475);
			try {
				f = FieldUtil.getField(KTestBean.class, "b");
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
				if (((Integer) f.get(bean)) != 8475) {
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
	public void testRun() {
		new DirectMethod().start();
		new DirectField().start();
		new FastMethod().start();
		new FastField().start();
		new ReflectMethod().start();
		new ReflectField().start();
	}

	class Direct2Field extends Performance {
		KTestBean bean = new KTestBean();

		/**
		 * @param innerIteration
		 * @param outerIteration
		 */
		public Direct2Field() {
			super(innerIteration, outerIteration);
			bean.setI(8475);
			System.out.print("Direct2Field\t");
		}

		/**
		 * Override super method
		 * @see org.apache.niolex.commons.test.Performance#run()
		 */
		@Override
		protected void run() {
			if (bean.i != 8475) {
				throw new RuntimeException("This is so bad!");
			}
		}

	}

	class Fast2Field extends Performance {
		KTestBean bean = new KTestBean();
		FieldAccess acc = FastFieldUtil.getFieldAccess(KTestBean.class);
		int idx = acc.getIndex("i");

		/**
		 * @param innerIteration
		 * @param outerIteration
		 */
		public Fast2Field() {
			super(innerIteration, outerIteration);
			bean.setI(8475);
			System.out.print("Fast2Field\t");
		}

		/**
		 * Override super method
		 * @see org.apache.niolex.commons.test.Performance#run()
		 */
		@Override
		protected void run() {
			if (acc.getInt(bean, idx) != 8475) {
				throw new RuntimeException("This is so bad!");
			}
		}

	}

	@Test
	public void testStart()
	 throws Exception {
		new Direct2Field().start();
		new Fast2Field().start();
	}

}
