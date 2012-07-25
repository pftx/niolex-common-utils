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

import static org.junit.Assert.*;

import org.apache.niolex.commons.test.Benchmark;
import org.apache.niolex.commons.test.Benchmark.Group;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-7-25
 */
public class BeanServerTest {
	BeanServer beanS = new BeanServer();

	class A {
		int[] ids = new int[] {1, 2, 3, 4, 5};
		String[] names = new String[] {"Adam", "Shalve", "Bob"};
		Group group = Group.makeGroup();
		Integer i = new Integer(128);
		final Boolean b = Boolean.FALSE;
		Byte by = new Byte((byte) 3);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.remote.BeanServer#putIfAbsent(java.lang.String, java.lang.Object)}.
	 */
	@Test
	public void testPutIfAbsent() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.remote.BeanServer#remove(java.lang.Object)}.
	 */
	@Test
	public void testRemove() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.remote.BeanServer#replace(java.lang.String, java.lang.Object, java.lang.Object)}.
	 */
	@Test
	public void testReplace() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.remote.BeanServer#start()}.
	 * @throws InterruptedException
	 */
	@Test
	public void testStart() throws InterruptedException {
		beanS.putIfAbsent("bench", Benchmark.makeBenchmark());
		beanS.putIfAbsent("group", new A());
		beanS.start();
		Thread.sleep(3000000);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.remote.BeanServer#run()}.
	 */
	@Test
	public void testRun() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.remote.BeanServer#setPort(int)}.
	 */
	@Test
	public void testSetPort() {
		fail("Not yet implemented");
	}

}
