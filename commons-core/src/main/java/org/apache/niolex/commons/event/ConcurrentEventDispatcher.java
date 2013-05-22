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
 * 支持并发的事件分发器实现。此实现是完全高并发的，不需要任何的外部锁机制。
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, Date: 2012-6-26
 */
public class ConcurrentEventDispatcher implements IEventDispatcher {

    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<EventListener<?>>> handlerMap =
            new ConcurrentHashMap<String, ConcurrentLinkedQueue<EventListener<?>>>();

    /**
     * Override super method
     * @see org.apache.niolex.commons.event.IEventDispatcher#addListener(java.lang.String, org.apache.niolex.commons.event.EventListener)
     */
    @Override
    public void addListener(String eventType, EventListener<?> eListener) {
        ConcurrentLinkedQueue<EventListener<?>> queue = handlerMap.get(eventType);
        if (queue == null) {
            queue = ConcurrentUtil.initMap(handlerMap, eventType, new ConcurrentLinkedQueue<EventListener<?>>());
        }
        queue.add(eListener);
    }

    /**
     * Override super method
     * @see org.apache.niolex.commons.event.IEventDispatcher#removeListener(java.lang.String, org.apache.niolex.commons.event.EventListener)
     */
    @Override
    public void removeListener(String eventType, EventListener<?> eListener) {
        ConcurrentLinkedQueue<EventListener<?>> queue = handlerMap.get(eventType);
        if (queue != null) {
            queue.remove(eListener);
        }
    }

    /**
     * Override super method
     * @see org.apache.niolex.commons.event.IEventDispatcher#fireEvent(org.apache.niolex.commons.event.Event)
     */
    @Override
    public void fireEvent(Event<?> e) {
        ConcurrentLinkedQueue<EventListener<?>> queue = handlerMap.get(e.getEventType());
        if (queue != null) {
            for (EventListener<?> eLi : queue) {
                eLi.internalEventHappened(e);
            }
        }
    }

}
