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
     * Test method for
     * {@link org.apache.niolex.commons.event.ConcurrentEventDispatcher#addListener(org.apache.niolex.commons.event.EventListener)}
     * .
     */
    @Test
    public void testAddListener() {
        IEventDispatcher dis = new ConcurrentEventDispatcher();
        final AtomicInteger au = new AtomicInteger(1);
        final AtomicInteger bu = new AtomicInteger(1);
        EventListener<StringEvent> el = new EventListener<StringEvent>() {

            @Override
            public void eventHappened(StringEvent e) {
                System.out.println(au.getAndIncrement() + ": " + e);
            }

            /**
             * This is the override of super method.
             * @see org.apache.niolex.commons.event.EventListener#onClassCastException(org.apache.niolex.commons.event.Event, java.lang.ClassCastException)
             */
            @Override
            protected void onClassCastException(Event<?> e, ClassCastException ex) {
                bu.incrementAndGet();
            }

        };
        dis.addListener("A", el);
        dis.fireEvent(new Event<String>("B", "Event Fired."));
        dis.fireEvent(new StringEvent("A", "The Second."));
        assertEquals(2, au.intValue());
        dis.fireEvent(new StringEvent("A", "Event Fired."));
        dis.fireEvent(new Event<String>("A", "Event Fired."));
        assertEquals(3, au.intValue());
        assertEquals(2, bu.intValue());
    }

    /**
     * Test method for {@link org.apache.niolex.commons.event.EventListener#onClassCastException(org.apache.niolex.commons.event.Event, java.lang.ClassCastException)}.
     */
    @Test
    public void testOnClassCastException() {
        IEventDispatcher dis = new ConcurrentEventDispatcher();
        final AtomicInteger au = new AtomicInteger(1);
        final AtomicInteger bu = new AtomicInteger(1);
        EventListener<Event<String>> el = new EventListener<Event<String>>() {

            @Override
            public void eventHappened(Event<String> e) {
                String s = e.getEventValue();
                // -- This line maybe not printed.
                System.out.println(au.getAndIncrement() + ": " + s);
            }

            /**
             * This is the override of super method.
             * @see org.apache.niolex.commons.event.EventListener#onClassCastException(org.apache.niolex.commons.event.Event, java.lang.ClassCastException)
             */
            @Override
            protected void onClassCastException(Event<?> e, ClassCastException ex) {
                bu.incrementAndGet();
            }
        };
        dis.addListener("A", new PrintEventListener());
        dis.addListener("A", el);
        dis.fireEvent(new IntEvent("A", 4));
        dis.fireEvent(new StringEvent("A", "In Event"));
        // -- StringEvent is compatible with Event<String>
        // -- IntEvent is compatible with Event<String>, but you can not get a String as EventValue!!!
        assertEquals(2, au.intValue());
        assertEquals(2, bu.intValue());
        // -----
        dis.fireEvent(new StringEvent("A", "Event Fired."));
        assertEquals(3, au.intValue());
        // -- Test remove listener
        dis.removeListener("A", el);
        dis.fireEvent(new Event<Integer>("A", 6));
        dis.fireEvent(new StringEvent("A", "Event Fired After."));
        assertEquals(3, au.intValue());
        assertEquals(2, bu.intValue());
    }

    /**
     * Test method for
     * {@link org.apache.niolex.commons.event.ConcurrentEventDispatcher#addListener(org.apache.niolex.commons.event.EventListener)}
     * .
     */
    @Test
    public void testRemoveFromNothing() {
        IEventDispatcher dis = new ConcurrentEventDispatcher();
        dis.removeListener("A", new PrintEventListener());
        dis.fireEvent(new StringEvent("A", "Event Fired."));
    }

    /**
     * Test method for
     * {@link org.apache.niolex.commons.event.ConcurrentEventDispatcher#removeListener(org.apache.niolex.commons.event.EventListener)}
     * .
     */
    @Test
    public void testRemoveListener() {
        ConcurrentEventDispatcher dis = new ConcurrentEventDispatcher();
        final AtomicInteger au = new AtomicInteger(1);
        EventListener<Event<String>> el = new EventListener<Event<String>>() {

            @Override
            public void eventHappened(Event<String> e) {
                System.out.println(au.getAndIncrement() + ": " + e);
            }
        };
        dis.addListener("A",el);
        dis.fireEvent(new StringEvent("A", "Not yet implemented"));
        assertEquals(2, au.intValue());
        dis.removeListener("A",el);
        dis.fireEvent(new StringEvent("A", "Event Fired."));
        assertEquals(2, au.intValue());
    }

    /**
     * Test method for {@link org.apache.niolex.commons.event.ConcurrentEventDispatcher#fireEvent(java.lang.Object)}.
     */
    @Test
    public void testFireEventWithNothing() {
        ConcurrentEventDispatcher dis = new ConcurrentEventDispatcher();
        dis.fireEvent(new Event<String>("B", "Event Fired."));
    }

}
