/**
 * EventTest.java
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

import static org.junit.Assert.*;

import org.junit.Test;

/**
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2012-12-20
 */
public class EventTest {

    /**
     * Test method for {@link org.apache.niolex.commons.event.Event#getEventType()}.
     */
    @Test
    public void testGetEventType() {
        Event<Integer> iEvn = new BaseEvent<Integer>("a", 3);
        assertEquals("a", iEvn.getEventType());
        assertEquals(3, iEvn.getEventValue().intValue());
        System.out.println(iEvn.toString());
    }

    /**
     * Test method for {@link org.apache.niolex.commons.event.Event#getEventValue()}.
     */
    @Test
    public void testGetEventValue() {
        StringEvent iEvn = new StringEvent("a", "commons");
        assertEquals("a", iEvn.getEventType());
        assertEquals("commons", iEvn.getEventValue());
    }

}
