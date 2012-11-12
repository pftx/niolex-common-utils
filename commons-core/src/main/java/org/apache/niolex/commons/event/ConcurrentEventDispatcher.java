/**
 * ConcurrentEventDispatcher.java
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

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 支持并发的事件分发器实现。
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, Date: 2012-6-26
 */
public class ConcurrentEventDispatcher<E> implements IEventDispatcher<E> {

	private final ConcurrentLinkedQueue<EventListener<E>> queue = new ConcurrentLinkedQueue<EventListener<E>>();


	/**
	 * Override super method
	 * @see org.apache.niolex.commons.event.IEventDispatcher#addListener(org.apache.niolex.commons.event.EventListener)
	 */
	@Override
	public void addListener(EventListener<E> eListener) {
		queue.add(eListener);
	}

	/**
	 * Override super method
	 * @see org.apache.niolex.commons.event.IEventDispatcher#removeListener(org.apache.niolex.commons.event.EventListener)
	 */
	@Override
	public void removeListener(EventListener<E> eListener) {
		queue.remove(eListener);
	}

	/**
	 * Override super method
	 * @see org.apache.niolex.commons.event.IEventDispatcher#fireEvent(java.lang.Object)
	 */
	@Override
	public void fireEvent(E e) {
		for (EventListener<E> eLi : queue) {
			eLi.eventHappened(e);
		}
	}

}
