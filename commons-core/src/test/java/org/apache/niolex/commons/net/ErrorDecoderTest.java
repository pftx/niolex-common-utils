package org.apache.niolex.commons.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.niolex.commons.file.FileUtil;
import org.apache.niolex.commons.net.ErrorDecoder.RESTDecoder;
import org.junit.Test;

public class ErrorDecoderTest {

    @Test
    public void testDecode() throws Exception {
        RESTDecoder a = RESTDecoder.INSTANCE;
        byte[] respBody = FileUtil.getBinaryFileContentFromClassPath("rest_error.json", ErrorDecoderTest.class);
        ;
        Exception e = a.decode(6, respBody);
        System.out.println(e);

        assertNotNull(e);
        assertEquals("Invalid name: proj@2", e.getMessage());
        assertTrue(e instanceof RESTException);
        assertEquals(500, ((RESTException) e).getInfo().getStatus());
    }

}
