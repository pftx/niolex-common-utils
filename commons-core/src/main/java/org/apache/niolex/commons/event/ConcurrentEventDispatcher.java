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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.niolex.commons.concurrent.ConcurrentUtil;

/**
 * The implementation of {@link Dispatcher} which supports fully concurrent operations on all methods.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-6-26
 */
public class ConcurrentEventDispatcher implements Dispatcher {

    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<Listener<?>>> handlerMap =
            new ConcurrentHashMap<String, ConcurrentLinkedQueue<Listener<?>>>();

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.event.Dispatcher#addListener(java.lang.Class, org.apache.niolex.commons.event.Listener)
     */
    @Override
    public void addListener(Class<?> eventType, Listener<?> eListener) {
        addListener(eventType.getName(), eListener);
    }

    /**
     * Override super method
     * @see org.apache.niolex.commons.event.Dispatcher#addListener(String, Listener)
     */
    @Override
    public void addListener(String eventType, Listener<?> eListener) {
        ConcurrentLinkedQueue<Listener<?>> queue = handlerMap.get(eventType);
        if (queue == null) {
            queue = ConcurrentUtil.initMap(handlerMap, eventType, new ConcurrentLinkedQueue<Listener<?>>());
        }
        queue.add(eListener);
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.event.Dispatcher#removeListener(java.lang.Class, org.apache.niolex.commons.event.Listener)
     */
    @Override
    public void removeListener(Class<?> eventType, Listener<?> eListener) {
        removeListener(eventType.getName(), eListener);
    }

    /**
     * Override super method
     * @see org.apache.niolex.commons.event.Dispatcher#removeListener(String, Listener)
     */
    @Override
    public void removeListener(String eventType, Listener<?> eListener) {
        ConcurrentLinkedQueue<Listener<?>> queue = handlerMap.get(eventType);
        if (queue != null) {
            queue.remove(eListener);
        }
    }

    /**
     * Directly fire the event.
     *
     * @param e the event
     */
    public void internalFireEvent(Event<?> e) {
        ConcurrentLinkedQueue<Listener<?>> queue = handlerMap.get(e.getEventType());
        if (queue != null) {
            for (Listener<?> l : queue) {
                EventUtil.dispatch(l, e);
            }
        }
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.event.Dispatcher#fireEvent(java.lang.Object)
     */
    @Override
    public void fireEvent(Object e) {
        if (e instanceof Event) {
            internalFireEvent((Event<?>) e);
        } else {
            internalFireEvent(BaseEvent.create(e));
        }
    }

}
