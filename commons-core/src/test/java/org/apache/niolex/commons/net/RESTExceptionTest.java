package org.apache.niolex.commons.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.apache.niolex.commons.net.RESTException.ErrorInfo;
import org.junit.Test;

public class RESTExceptionTest {

    @Test
    public void testErrorInfo() throws Exception {
        ErrorInfo a = new ErrorInfo();
        a.setError("OEFpof");
        assertEquals("OEFpof", a.getError());
        a.setErrorCode("10203");
        assertEquals("10203", a.getError());

        a.setErrorMessage("Server internal Err.");
        assertEquals("Server internal Err.", a.getMessage());
        a.setMessage("This is great!");
        assertEquals("This is great!", a.getMessage());

        a.setException("java.lang.IllegalArgumentException");
        assertEquals("java.lang.IllegalArgumentException", a.getException());

        a.setExtra("Find it over the sky.");
        assertEquals("Find it over the sky.", a.getExtra());

        a.setPath("/a/b/c");
        assertEquals("/a/b/c", a.getPath());

        a.setStatus(20203);
        assertEquals(20203, a.getStatus());

        a.setTimestamp(new Date(12345));
        assertEquals(new Date(12345), a.getTimestamp());

        System.out.println(a);
    }

    @Test
    public void testRESTException() throws Exception {
        ErrorInfo i = new ErrorInfo();
        RESTException e = new RESTException(i);
        assertNotNull(e);
        System.out.println(e);
        assertEquals(i, e.getInfo());
    }

}
