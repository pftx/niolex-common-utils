/**
 * Event.java
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

/**
 * The basic Event class, user can extend this class to implement there own Event type.
 * Every event is recognized by there event type property, not the class type.
 * So it's recommended to use the full qualified class name as the event type.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2012-12-20
 */
public class Event<O> {

    /**
     * The event type, event dispatcher will use this to distinguish events.
     */
    private final String eventType;

    /**
     * The event data is set here.
     */
    private final O eventValue;

    /**
     * The only constructor.
     *
     * @param eventType
     * @param eventValue
     */
    public Event(String eventType, O eventValue) {
        super();
        this.eventType = eventType;
        this.eventValue = eventValue;
    }

    public String getEventType() {
        return eventType;
    }

    public O getEventValue() {
        return eventValue;
    }

    @Override
    public String toString() {
        return "Event[" + eventType + "]=" + eventValue;
    }

}
