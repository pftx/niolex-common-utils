/**
 * EventListenerTest.java
 *
 * Copyright 2012 The original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.niolex.commons.event;

import org.junit.Test;

/**
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2012-12-20
 */
public class EventListenerTest {

    /**
     * Test method for {@link org.apache.niolex.commons.event.EventListener#eventHappened(org.apache.niolex.commons.event.Event)}.
     */
    @Test
    public void testEventHappened() {
        PrintEventListener e = new PrintEventListener();
        e.internalEventHappened(new StringEvent("A", "Event Fired."));
        e.internalEventHappened(null);
    }

    /**
     * Test method for {@link org.apache.niolex.commons.event.EventListener#internalEventHappened(org.apache.niolex.commons.event.Event)}.
     */
    @Test
    public void testInternalEventHappened() {
        EventListener<StringEvent> e = new EventListener<StringEvent>() {

            @Override
            public void eventHappened(StringEvent e) {
                System.out.println("El: " + e);
            }
        };
        e.internalEventHappened(new Event<String>("A", "Event Fired."));
        e.internalEventHappened(null);
    }

}
