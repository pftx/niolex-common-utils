/**
 * IEventDispatcher.java
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

/**
 * The interface of event dispatcher.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, Date: 2012-6-26
 */
public interface IEventDispatcher {

	/**
	 * Add an event listener who care this event.
	 * @param eventType
	 * @param eListener
	 */
	public void addListener(String eventType, EventListener<?> eListener);

	/**
	 * Remove the specified event listener.
	 * @param eventType
	 * @param eListener
	 */
	public void removeListener(String eventType, EventListener<?> eListener);

	/**
	 * Fire the specified event to all the listeners registered to this dispatcher.
	 * @param e
	 */
	public void fireEvent(Event<?> e);

}
