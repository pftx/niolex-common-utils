/**
 * ConcurrentEventDispatcherTest.java
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
package org.apache.niolex.commons.event;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-6-26
 */
public class ConcurrentEventDispatcherTest {

	/**
	 * Test method for {@link org.apache.niolex.commons.event.ConcurrentEventDispatcher#addListener(org.apache.niolex.commons.event.EventListener)}.
	 */
	@Test
	public void testAddListener() {
		ConcurrentEventDispatcher<String> dis = new ConcurrentEventDispatcher<String>();
		final AtomicInteger au = new AtomicInteger(1);
		EventListener<String> el = new EventListener<String>() {

			@Override
			public void eventHappened(String e) {
				System.out.println(au.getAndIncrement() + ": " + e);
			}};
		dis.addListener(el);
		dis.fireEvent("Not yet implemented");
		assertEquals(2, au.intValue());
		dis.fireEvent("Event Fired.");
		assertEquals(3, au.intValue());
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.event.ConcurrentEventDispatcher#removeListener(org.apache.niolex.commons.event.EventListener)}.
	 */
	@Test
	public void testRemoveListener() {
		ConcurrentEventDispatcher<String> dis = new ConcurrentEventDispatcher<String>();
		final AtomicInteger au = new AtomicInteger(1);
		EventListener<String> el = new EventListener<String>() {

			@Override
			public void eventHappened(String e) {
				System.out.println(au.getAndIncrement() + ": " + e);
			}};
		dis.addListener(el);
		dis.fireEvent("Not yet implemented");
		assertEquals(2, au.intValue());
		dis.removeListener(el);
		dis.fireEvent("Event Fired.");
		assertEquals(2, au.intValue());
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.event.ConcurrentEventDispatcher#fireEvent(java.lang.Object)}.
	 */
	@Test
	public void testFireEvent() {
		ConcurrentEventDispatcher<String> dis = new ConcurrentEventDispatcher<String>();
		dis.fireEvent("Event Fired.");
	}

}
