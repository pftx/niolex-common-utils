/**
 * BeanServerTest.java
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
package org.apache.niolex.commons.remote;

import static org.junit.Assert.assertNull;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.niolex.commons.test.Benchmark;
import org.apache.niolex.commons.test.SystemInfo;
import org.apache.niolex.commons.test.Benchmark.Bean;
import org.apache.niolex.commons.test.Benchmark.Group;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-7-25
 */
public class BeanServerTest {
	BeanServer beanS = new BeanServer();

	class B implements Invokable {

		private String msg = "Please invoke me!";

		/**
		 * Override super method
		 * @see org.apache.niolex.commons.remote.Invokable#invoke()
		 */
		@Override
		public void invoke() {
			System.out.println("I am invoked.");
		}

		public String getMsg() {
			return msg;
		}

	}

	class A {
		int[] ids = new int[] {1, 2, 3, 4, 5};
		String[] names = new String[] {"Adam", "Shalve", "Bob"};
		Group group = Group.makeGroup();
		Integer i = new Integer(128);
		final Boolean b = Boolean.FALSE;
		Byte by = new Byte((byte) 3);
		Map<Integer, String> map = new HashMap<Integer, String>();
		Map<String, String> smap = new HashMap<String, String>();
		Map<String, Object> bmap = new HashMap<String, Object>();
		Map<Object, Object> imap = new HashMap<Object, Object>();
		Set<String> set = new HashSet<String>();

		public A() {
			map.put(1, "Good");
			smap.put("test", "but");
			smap.put("this.[is].good", "See You!");
			bmap.put("b", new Bean(3, "Bean", 12212, new Date()));
			bmap.put("c", Benchmark.makeBenchmark());
			bmap.put("invoke", new B());
			imap.put(new Date(), new Bean(3, "Bean", 12212, new Date()));
			set.add("Goog Morning");
			set.add("This is Good");
			set.add("中文");
		}
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.remote.BeanServer#putIfAbsent(java.lang.String, java.lang.Object)}.
	 */
	@Test
	public void testPutIfAbsent() {
		Object o = beanS.putIfAbsent("fail", "Not yet implemented");
		assertNull(o);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.remote.BeanServer#remove(java.lang.Object)}.
	 */
	@Test
	public void testRemove() {
		beanS.remove("fail");
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.remote.BeanServer#replace(java.lang.String, java.lang.Object, java.lang.Object)}.
	 */
	@Test
	public void testReplace() {
		beanS.replace("fail", "Not yet implemented", "New value");
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.remote.BeanServer#start()}.
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		BeanServerTest test = new BeanServerTest();
		test.beanS.putIfAbsent("bench", Benchmark.makeBenchmark());
		test.beanS.putIfAbsent("group", test.new A());
		test.beanS.putIfAbsent("system", SystemInfo.getInstance());
		test.beanS.start();
		Thread.sleep(3000000);
		test.beanS.stop();
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.remote.BeanServer#setPort(int)}.
	 */
	@Test
	public void testSetPort() {
		beanS.setPort(1234);
	}

}
