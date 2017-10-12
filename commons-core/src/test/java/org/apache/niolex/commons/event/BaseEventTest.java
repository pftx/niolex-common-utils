package org.apache.niolex.commons.event;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BaseEventTest {

    @Test
    public void testCreate() throws Exception {
        Event<String> e = BaseEvent.create("not yet implemented");
        assertEquals("Event[java.lang.String]=not yet implemented", e.toString());
    }

    @Test
    public void testBaseEvent() throws Exception {
        BaseEvent<Integer> i = new BaseEvent<Integer>("not yet implemented", 3);
        assertEquals("not yet implemented", i.getEventType());
    }

    @Test
    public void testGetEventType() throws Exception {
        Event<BaseEventTest> e = BaseEvent.create(this);
        assertEquals("org.apache.niolex.commons.event.BaseEventTest", e.getEventType());
    }

    @Test
    public void testGetEventValue() throws Exception {
        BaseEvent<Integer> i = new BaseEvent<Integer>("not yet implemented", 45);
        assertEquals(45, i.getEventValue().intValue());
    }

    @Test
    public void testToString() throws Exception {
        System.out.println(BaseEvent.create(this));
    }

}
