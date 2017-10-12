package org.apache.niolex.commons.coder;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CoderExceptionTest {

    @Test
    public void testCoderExceptionStringThrowable() throws Exception {
        CoderException r = new CoderException("not yet implemented");
        assertEquals("not yet implemented", r.getMessage());
    }

    @Test
    public void testCoderExceptionString() throws Exception {
        RuntimeException r = new RuntimeException("not yet implemented");
        CoderException c = new CoderException("This is a joke!", r);
        assertEquals("This is a joke!", c.getMessage());
        assertEquals(r, c.getCause());
    }

}
