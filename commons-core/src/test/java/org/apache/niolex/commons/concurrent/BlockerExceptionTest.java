package org.apache.niolex.commons.concurrent;


import org.junit.Test;

public class BlockerExceptionTest {

    @Test
    public void testBlockerExceptionStringThrowable() throws Exception {
        new BlockerException("not yet implemented", new Exception());
    }

}
