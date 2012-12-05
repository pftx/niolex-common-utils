/**
 * LangTest.java
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

import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-7-17
 */
public class LangTest {

	@Test
	public void test() throws Throwable {
		// Class for name will execute.
		Class<?> cls = Class.forName("org.apache.niolex.common.lang.VolatileTest");
		// Load class will not execute static block.
//		Class<?> cls = this.getClass().getClassLoader().loadClass("org.apache.niolex.network.demo.lang.VolatileTest");
		System.out.println(cls.getConstructors().length);
	}

	@Test
	public void testByte() {
		byte a = -128;
		byte b = 127;
		System.out.println("127 + 1 = " + (byte)(b + 1));
		System.out.println("-128 - 1 = " + (byte)(a - 1));
		int c = a - b;
		System.out.println("-128 - 127 = " + (c));
	}

	class A extends B {
		public int a = 100;

		public A() {
			super();
			System.out.println("Con " + a);
			a = 200;
		}

	}

	class B {
		public B() {
			System.out.println("Su " + ((A) this).a);
		}
	}

	@Test
	public void tem() {
		System.out.println("Rem " + new A().a);
	}

	@SuppressWarnings("finally")
	public int go() {
		try {
			return 1;
		} finally {
			System.out.println("Let's be here -1.");
			return -1;
		}
	}

	@Test
	public void tFinal() {
		System.out.println("Final " + go());
	}
}
