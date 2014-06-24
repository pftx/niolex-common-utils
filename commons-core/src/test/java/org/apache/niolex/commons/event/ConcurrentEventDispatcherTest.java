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
     * {@link org.apache.niolex.commons.event.ConcurrentEventDispatcher#addListener(String, Listener)}
     * .
     */
    @Test
    public void testAddListener() {
        Dispatcher dis = new ConcurrentEventDispatcher();
        final AtomicInteger au = new AtomicInteger(0);
        Listener<String> el = new Listener<String>() {

            @Override
            public void eventHappened(Event<String> e) {
                System.out.println(au.getAndIncrement() + ": " + e);
            }

        };
        dis.addListener("A", el);
        dis.addListener("B", el);
        dis.fireEvent(new BaseEvent<String>("B", "Event Fired."));
        dis.fireEvent(new StringEvent("A", "The Second."));
        assertEquals(2, au.intValue());
        dis.removeListener("A", el);
        dis.fireEvent(new StringEvent("A", "Event Fired."));
        dis.fireEvent(new BaseEvent<String>("A", "Event Fired."));
        assertEquals(2, au.intValue());
        dis.fireEvent(new StringEvent("B", "The Third."));
        assertEquals(3, au.intValue());
    }

    /**
     * Test method for
     * {@link org.apache.niolex.commons.event.ConcurrentEventDispatcher#removeListener(String, Listener)}
     * .
     */
    @Test
    public void testRemoveListenerFromNothing() {
        Dispatcher dis = new ConcurrentEventDispatcher();
        dis.removeListener("A", new PrintEventListener());
        dis.fireEvent(new StringEvent("A", "Event Fired."));
    }

    /**
     * Test method for
     * {@link org.apache.niolex.commons.event.ConcurrentEventDispatcher#internalFireEvent(Event)}
     * .
     */
    @Test
    public void testInternalFireEvent() {
        ConcurrentEventDispatcher dis = new ConcurrentEventDispatcher();
        final AtomicInteger au = new AtomicInteger(1);
        Listener<String> el = new Listener<String>() {

            @Override
            public void eventHappened(Event<String> e) {
                String s = e.getEventValue();
                System.out.println(au.getAndIncrement() + ": I" + s);
            }
        };
        dis.addListener("A",el);
        dis.fireEvent(new StringEvent("A", "Not yet implemented"));
        assertEquals(2, au.intValue());
        // ---
        dis.fireEvent(new IntEvent("A", 88));
        assertEquals(2, au.intValue());
        // ---
        dis.removeListener("A",el);
        dis.fireEvent(new StringEvent("A", "Event Fired."));
        assertEquals(2, au.intValue());
    }

    /**
     * Test method for {@link org.apache.niolex.commons.event.ConcurrentEventDispatcher#fireEvent(java.lang.Object)}.
     */
    @Test
    public void testFireEvent() {
        ConcurrentEventDispatcher dis = new ConcurrentEventDispatcher();
        final AtomicInteger au = new AtomicInteger(1);
        Listener<String> el = new Listener<String>() {

            @Override
            public void eventHappened(Event<String> e) {
                System.out.println(au.getAndIncrement() + ": " + e);
            }
        };
        dis.addListener(String.class, el);
        dis.addListener(String.class, new PrintEventListener());
        dis.fireEvent(new BaseEvent<String>("java.lang.String", "Event Fired."));
        assertEquals(2, au.intValue());
        dis.fireEvent("Good Again.");
        assertEquals(3, au.intValue());
        dis.removeListener(String.class, el);
        dis.fireEvent("Bad.");
        assertEquals(3, au.intValue());
    }

}
