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
public class EventUtilTest extends EventUtil {

    /**
     * Test method for {@link org.apache.niolex.commons.event.EventUtil#eventHappened(org.apache.niolex.commons.event.Event)}.
     */
    @Test
    public void testDispatch() {
        PrintEventListener e = new PrintEventListener();
        onClassCastException(e, new StringEvent("A", "Event Fired."), null);
    }

}
