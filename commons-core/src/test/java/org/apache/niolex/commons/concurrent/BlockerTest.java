/**
 * BlockerTest.java
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
package org.apache.niolex.commons.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;

import org.apache.niolex.commons.test.Counter;
import org.apache.niolex.commons.util.Runner;
import org.apache.niolex.commons.bean.One;
import org.apache.niolex.commons.bean.Pair;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-7-12
 */
public class BlockerTest {

	private final Blocker<Integer> blocker = new Blocker<Integer>();

	/**
	 * Test method for {@link org.apache.niolex.commons.concurrent.Blocker#init(java.lang.Object)}.
	 */
	@Test
	public void testInit() {
		Pair<Boolean, WaitOn<Integer>> a = blocker.init("Not yet implemented");
		Pair<Boolean, WaitOn<Integer>> b = blocker.init("Not yet implemented");
		assertTrue(a.a);
		assertFalse(b.a);
		assertEquals(a.b, b.b);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.concurrent.Blocker#initWait(java.lang.Object)}.
	 * @throws Exception
	 */
	@Test
	public void testInitWait() throws Exception {
		final WaitOn<Integer> on = blocker.initWait(blocker);
		One<Integer> retVal = One.create(0);
		Thread w = Runner.run(retVal , on, "waitForResult", 1000);
		blocker.release(blocker, 156);
		w.join();
		assertEquals(156, retVal.a.intValue());
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.concurrent.Blocker#waitForResult(java.lang.Object, long)}.
	 * @throws InterruptedException
	 */
	@Test
	public void testWaitForResult() throws InterruptedException {
		final Counter c = new Counter();
		final CountDownLatch cl = new CountDownLatch(1);
		Thread t = new Thread() {
			public void run() {
				try {
				    cl.countDown();
					int k = blocker.waitForResult("man", 1000);
					assertEquals(1546, k);
					c.inc();
				} catch (Exception e) {
				}
			}
		};

		t.start();
		cl.await();
		Thread.sleep(10);
		blocker.release("man", 1546);
		t.join();
		assertEquals(1, c.cnt());
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.concurrent.Blocker#release(java.lang.Object, java.lang.Object)}.
	 */
	@Test
	public void testReleaseObjectE() {
		assertFalse(blocker.release("implemented", 89));
		assertFalse(blocker.release("concurrent", new Exception("J")));
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.concurrent.Blocker#release(java.lang.Object, java.lang.Object)}.
	 */
	@Test
	public void testReleaseObjectExist() {
	    blocker.init("implemented");
	    blocker.init("concurrent");
	    assertTrue(blocker.release("implemented", 89));
	    assertTrue(blocker.release("concurrent", new Exception("J")));
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.concurrent.Blocker#release(java.lang.Object, java.lang.Exception)}.
	 * @throws InterruptedException
	 */
	@Test
	public void testReleaseWithException() throws InterruptedException {
		final Counter c = new Counter();
		final WaitOn<Integer> on = blocker.initWait("man");
		Thread t = new Thread() {
			public void run() {
				try {
					int k = on.waitForResult(100);
					assertEquals(1546, k);
					assertFalse(true);
				} catch (Exception e) {
					assertEquals("J", e.getMessage());
					c.inc();
				}
			}
		};
		t.start();
		blocker.release("man", new Exception("J"));
		t.join();
		assertEquals(1, c.cnt());
	}

	@Test(expected=IllegalStateException.class)
	public void testReleaseAll() throws Exception {
		blocker.init("a");
		blocker.init("b");
		WaitOn<Integer> k = blocker.initWait("c");
		blocker.releaseAll();
		k.waitForResult(20);
	}

}
