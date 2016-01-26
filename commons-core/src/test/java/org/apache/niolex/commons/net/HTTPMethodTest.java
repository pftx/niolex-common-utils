package org.apache.niolex.commons.net;


import static org.junit.Assert.*;

import org.junit.Test;

public class HTTPMethodTest {

    @Test
    public void testHTTPMethod() throws Exception {
        assertEquals(HTTPMethod.valueOf("GET"), HTTPMethod.GET);
    }

    @Test
    public void testPassParametersInURL() throws Exception {
        assertEquals("DELETE", HTTPMethod.DELETE.name());
        assertTrue(HTTPMethod.GET.passParametersInURL());
        assertFalse(HTTPMethod.POST.passParametersInURL());
    }

}
