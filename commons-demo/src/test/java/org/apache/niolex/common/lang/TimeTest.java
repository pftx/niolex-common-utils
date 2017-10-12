/**
 * TimeTest.java
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
 * @since 2012-6-21
 */
public class TimeTest {

	static class A {
		public A() {
			System.out.println("New A");
		}
	}

	static class B {
		public A a = new A();
		public B() {
			System.out.println("New B");
		}

		public String[] foo() {
			System.out.println("Invoke");
			return new String[] {"A", "B", "C"};
		}
	}

	static class C {
		public static B BB = new B();

		static {
			System.out.println("Static C");
		}
	}

	@Test
	public void test() {
		final int SIZE = 1 << 28;
		long in = System.currentTimeMillis();
		for (int i = 0; i < SIZE; ++i) {
			System.currentTimeMillis();
		}
		long t = System.currentTimeMillis() - in;
		System.out.println("Mill Time: " + t);
		in = System.currentTimeMillis();
		for (int i = 0; i < SIZE; ++i) {
			System.nanoTime();
		}
		t = System.currentTimeMillis() - in;
		System.out.println("Nano Time: " + t);
		for (String s : C.BB.foo()) {
			System.out.println("It‘s: " + s);
		}
	}

}
