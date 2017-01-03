/**
 * ReflectionTest.java
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
package org.apache.niolex.common.lang;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.niolex.commons.reflect.FastFieldUtil;
import org.apache.niolex.commons.reflect.FieldUtil;
import org.apache.niolex.commons.reflect.MethodUtil;
import org.apache.niolex.commons.reflect.ProxyUtil;
import org.apache.niolex.commons.reflect.ProxyUtil.ProxyHandler;
import org.junit.Test;

import com.esotericsoftware.reflectasm.FieldAccess;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-6-18
 */
public class ReflectionTest {

	public interface KTestI {
		public Integer getB();
	}

	public class KTestBean implements KTestI {
		Integer b;

		public Integer getB() {
			return b;
		}

		public void setB(Integer b) {
			this.b = b;
		}

	}

	@Test
	public void testReflect() throws Throwable {
		KTestBean test = new KTestBean();
		test.setB(123);
		final int SIZE = 2 << 20;
		final Integer TEST = 123;
		Object param[] = new Object[0];
		Method m = MethodUtil.getMethod(KTestBean.class, "getB", new Class<?>[0]);

		long in = System.currentTimeMillis();
		for (int i = 0; i < SIZE; ++i) {
			Integer k = (Integer) m.invoke(test, param);
			assertTrue(k == TEST);
		}

		long t = System.currentTimeMillis() - in;
		System.out.println("Method Time: " + t);

		Field f = FieldUtil.getField(KTestBean.class, "b");
		in = System.currentTimeMillis();
		for (int i = 0; i < SIZE; ++i) {
			Integer k = (Integer) f.get(test);
			assertTrue(k == TEST);
		}
		t = System.currentTimeMillis() - in;
		System.out.println("Field Time: " + t);

		FieldAccess a = FastFieldUtil.getFieldAccess(KTestBean.class);
		int idx = a.getIndex("b");
		in = System.currentTimeMillis();
		for (int i = 0; i < SIZE; ++i) {
			Integer k = (Integer)a.get(test, idx);
			assertTrue(k == TEST);
		}
		t = System.currentTimeMillis() - in;
		System.out.println("Fast Field Time: " + t);

		in = System.currentTimeMillis();
		for (int i = 0; i < SIZE; ++i) {
			Integer k = test.getB();
			assertTrue(k == TEST);
		}
		t = System.currentTimeMillis() - in;
		System.out.println("Direct m Time: " + t);

		in = System.currentTimeMillis();
		for (int i = 0; i < SIZE; ++i) {
			Integer k = test.b;
			assertTrue(k == TEST);
		}
		t = System.currentTimeMillis() - in;
		System.out.println("Direct f Time: " + t);
	}

    public Integer compare(ProxyHandler<Void> h, KTestBean test) {
		h.invokeBefore(test, null, null);
		Integer i = test.getB();
        h.invokeAfter(h, null, null, test, null);
		return i;
	}

	@Test
	public void testProxy() {
		KTestBean test = new KTestBean();
		test.setB(123);

		final int SIZE = 2 << 22;
		final Integer TEST = 123;

        ProxyHandler<Void> h = new ProxyHandler<Void>() {

            @Override
            public Void invokeBefore(Object host, Method method, Object[] args) {
                return null;
            }

            @Override
            public Object invokeAfter(Object host, Method method, Object[] args, Object ret, Void before) {
                return ret;
            }
		};

		KTestI proxy = ProxyUtil.newProxyInstance(test, h);

		long in = System.currentTimeMillis();
		for (int i = 0; i < SIZE; ++i) {
			Integer k = proxy.getB();
			assertTrue(k == TEST);
		}
		long t = System.currentTimeMillis() - in;
		System.out.println("Proxy Time: " + t);

		in = System.currentTimeMillis();
		for (int i = 0; i < SIZE; ++i) {
			Integer k = test.getB();
			assertTrue(k == TEST);
		}
		t = System.currentTimeMillis() - in;
		System.out.println("Direct Time: " + t);

		in = System.currentTimeMillis();
		for (int i = 0; i < SIZE; ++i) {
			Integer k = compare(h, test);
			assertTrue(k == TEST);
		}
		t = System.currentTimeMillis() - in;
		System.out.println("Compare Time: " + t);
	}
}
