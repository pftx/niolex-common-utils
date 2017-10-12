package org.apache.niolex.commons.util;

import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;

import org.junit.Test;

public class BufferUtilTest extends BufferUtil {

    @Test
    public void testCleanNativeMem() throws Exception {
        ByteBuffer bb = ByteBuffer.allocateDirect(10240);
        bb.putInt(3344);
        bb.flip();
        int c = bb.getInt();
        assertEquals(3344, c);

        cleanNativeMem(bb);
        cleanNativeMem(bb);
        cleanNativeMem(bb);

        bb.flip();
        c = bb.getInt();
        if (3344 != c) {
            System.out.println("It's OK: " + c);
        }

        bb.clear();
        bb.putInt(6677);

        bb.flip();
        c = bb.getInt();
        assertEquals(6677, c);
    }

    @Test
    public void testCleanMem() throws Exception {
        ByteBuffer bb = ByteBuffer.allocate(1024);
        bb.putInt(3344);
        bb.flip();
        int c = bb.getInt();
        assertEquals(3344, c);

        cleanNativeMem(bb);
        cleanNativeMem(bb);
        cleanNativeMem(bb);

        bb.flip();
        c = bb.getInt();
        assertEquals(3344, c);

        bb.clear();
        bb.putInt(6677);

        bb.flip();
        c = bb.getInt();
        assertEquals(6677, c);
    }

    @Test
    public void testCleanTDMMem() throws Exception {
        cleanNativeMem(new TDM());
        cleanNativeMem(null);
        cleanNativeMem("restriction");
    }

    @SuppressWarnings("restriction")
    private static class TDM implements sun.nio.ch.DirectBuffer {

        /**
         * This is the override of super method.
         * 
         * @see sun.nio.ch.DirectBuffer#address()
         */
        @Override
        public long address() {
            return 0;
        }

        /**
         * This is the override of super method.
         * 
         * @see sun.nio.ch.DirectBuffer#attachment()
         */
        @Override
        public Object attachment() {
            return null;
        }

        /**
         * This is the override of super method.
         * 
         * @see sun.nio.ch.DirectBuffer#cleaner()
         */
        @Override
        public sun.misc.Cleaner cleaner() {
            return null;
        }

    }

}
