/**
 * EventListener.java
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The public abstract class to listen event.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-6-26
 */
public abstract class EventListener<E extends Event<?>> {

    protected static final Logger LOG = LoggerFactory.getLogger(EventListener.class);

	/**
	 * The event happened. User need to implement this method to handle the event.
	 *
	 * @param e
	 */
	public abstract void eventHappened(E e);

	/**
	 * The internal method, handle the class cast problem.
	 * We mark this method as final, to prevent subclass from change this method.
	 *
	 * @param e
	 */
	@SuppressWarnings("unchecked")
    protected final void internalEventHappened(Event<?> e) {
	    try {
	        eventHappened((E) e);
	    } catch (ClassCastException ex) {
	        onClassCastException(e, ex);
	    }
	}

	/**
	 * We just log the ClassCastException.
	 * Subclass can override this method to take care of this exception.
	 *
	 * @param e
	 * @param ex
	 */
	protected void onClassCastException(Event<?> e, ClassCastException ex) {
	    LOG.warn("ClassCastException occured for event type [{}] class: {}.", e.getEventType(), e.getClass(), ex);
	}

}
