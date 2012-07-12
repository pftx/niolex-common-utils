/**
 * WaitOnTest.java
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

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-7-12
 */
public class WaitOnTest {

	private Lock lock = new ReentrantLock();

	private WaitOn<String> waitOn;

	@Before
	public void createWaitOn() throws Exception {
		Condition wai2tOn = lock.newCondition();
		waitOn = new WaitOn<String>(wai2tOn, lock);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.concurrent.WaitOn#waitForResult(long)}.
	 * @throws InterruptedException
	 */
	@Test
	public void testWaitForResult1() throws InterruptedException {
		Thread t = new Thread() {
			public void run() {
				try {
					String s = waitOn.waitForResult(200);
					System.out.println(s);
					assertEquals("Good", s);
				} catch (Exception e) {
					e.printStackTrace();
					assertFalse(true);
				}
			}
		};
		Thread.sleep(10);
		t.start();
		waitOn.release("Good");
		t.join();
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.concurrent.WaitOn#waitForResult(long)}.
	 * @throws InterruptedException
	 */
	@Test
	public void testWaitForResult2() throws InterruptedException {
		Thread t = new Thread() {
			public void run() {
				try {
					String s = waitOn.waitForResult(200);
					System.out.println(s);
					assertFalse(true);
					assertEquals("Good", s);
				} catch (Exception e) {
					assertEquals("I am here to meet you!", e.getMessage());
				}
			}
		};
		t.start();
		Thread.sleep(10);
		waitOn.release(new Exception("I am here to meet you!"));
		t.join();
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.concurrent.WaitOn#waitForResult(long)}.
	 * @throws InterruptedException
	 */
	@Test
	public void testWaitForResult3() throws InterruptedException {
		Thread t = new Thread() {
			public void run() {
				try {
					String s = waitOn.waitForResult(200);
					System.out.println(s);
					assertFalse(true);
					assertEquals("Good", s);
				} catch (Exception e) {
					assertEquals("I am here to meet you!", e.getMessage());
				}
			}
		};
		waitOn.release(new Exception("I am here to meet you!"));
		t.start();
		t.join();
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.concurrent.WaitOn#waitForResult(long)}.
	 * @throws InterruptedException
	 */
	@Test
	public void testWaitForResult4() throws InterruptedException {
		Thread t = new Thread() {
			public void run() {
				try {
					String s = waitOn.waitForResult(200);
					System.out.println(s);
					assertEquals("Good", s);
				} catch (Exception e) {
					e.printStackTrace();
					assertFalse(true);
				}
			}
		};
		waitOn.release("Good");
		t.start();
		t.join();
	}

}
