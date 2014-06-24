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
public abstract class EventUtil {

    protected static final Logger LOG = LoggerFactory.getLogger(EventUtil.class);

    /**
     * The event dispatch method, handle the class cast problem.
     *
     * @param l the listener
     * @param e the event
     */
	@SuppressWarnings("unchecked")
	public static final void dispatch(Listener<?> l, Event<?> e) {
	    castEvent((Listener<Object>)l, (Event<Object>)e);
	}

	/**
	 * The internal method.
	 *
	 * @param l the listener
	 * @param e the event
	 */
    private static final <V> void castEvent(Listener<V> l, Event<V> e) {
	    try {
	        l.eventHappened(e);
	    } catch (ClassCastException ex) {
	        onClassCastException(l, e, ex);
	    }
	}

	/**
	 * We just log the ClassCastException.
	 *
	 * @param l the listener
	 * @param e the event
	 * @param ex the exception
	 */
	public static final void onClassCastException(Listener<?> l, Event<?> e, ClassCastException ex) {
	    LOG.warn("ClassCastException occured for event type [{}] class: {}; listener: {}.", e.getEventType(),
	            e.getClass(), l, ex);
	}

}
