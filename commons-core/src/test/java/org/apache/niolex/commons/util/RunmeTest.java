/**
 * RunmeTest.java
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
package org.apache.niolex.commons.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-6-28
 */
public class RunmeTest {

	/**
	 * Test run me without initial sleep.
	 *
	 * Test method for {@link org.apache.niolex.commons.util.Runme#start()}.
	 */
	@Test
	public void testStart() throws Throwable {
		final AtomicInteger au = new AtomicInteger(0);
		Runme me = new Runme(){
			@Override
			public void runMe() {
				au.incrementAndGet();
			}};
		me.setSleepInterval(30);
		me.start();
		Thread.sleep(45);
		assertEquals(2, au.intValue());
		me.stopMe();
		me.interrupt();
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.util.Runme#start()}.
	 */
	@Test
	public void testRunmeException() throws Throwable {
		Runme me = new Runme(){
			@Override
			public void runMe() {
			    throw new NullPointerException("abc");
			}};
		me.setSleepInterval(30);
		me.setInitialSleep(false);
		me.start();
		Thread.sleep(45);
		me.stopMe();
		me.interrupt();
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.util.Runme#run()}.
	 */
	@Test
	public void testStopMeInitSleepGo() throws Throwable {
		final AtomicInteger au = new AtomicInteger(0);
		Runme me = new Runme(){
			@Override
			public void runMe() {
				au.incrementAndGet();
			}};
			me.setSleepInterval(100);
			me.setInitialSleep(true);
			me.start();
			Thread.sleep(120);
			assertTrue(3 > au.intValue());
			assertTrue(0 < au.intValue());
			me.stopMe();
			me.interrupt();
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.util.Runme#run()}.
	 */
	@Test
	public void testRun() throws Throwable {
		final AtomicInteger au = new AtomicInteger(0);
		Runme me = new Runme(){
			@Override
			public void runMe() {
				au.incrementAndGet();
			}};
		me.setSleepInterval(10000);
		me.setInitialSleep(false);
		me.start();
		Thread.sleep(10);
		me.stopMe();
		me.interrupt();
		assertEquals(1, au.intValue());
	}


}
