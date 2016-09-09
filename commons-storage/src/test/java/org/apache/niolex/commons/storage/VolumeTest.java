package org.apache.niolex.commons.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.apache.niolex.commons.codec.IntegerUtil;
import org.apache.niolex.commons.file.DirUtil;
import org.apache.niolex.commons.reflect.FieldUtil;
import org.apache.niolex.commons.storage.Page.Size;
import org.apache.niolex.commons.test.AnnotationOrderedRunner;
import org.apache.niolex.commons.test.AnnotationOrderedRunner.Order;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AnnotationOrderedRunner.class)
public class VolumeTest {

    static String PREX = System.getProperty("user.home") + "/storage";
    static String fileName = PREX + "/v00.dat";

    @BeforeClass
    public static void setUp() {
        DirUtil.mkdirsIfAbsent(PREX);
        File f = new File(fileName);
        if (f.exists()) {
            f.delete();
        }
    }

    @AfterClass
    public static void tearDown() {
        DirUtil.delete(PREX, true);
    }

    @Test
    @Order(1)
    public void testVolume() throws Exception {
        Volume a = new Volume(fileName);
        a.initialize(Size.S16K);

        Page p = new Page(Size.S16K);
        byte[] buf = p.getBuf();
        String s = "software framework, for scalable cross-language services development, combines a software stack"
                + " with a code generation engine to build services that work efficiently and seamlessly between C++"
                + ", Java, Python, PHP, Ruby, Erlang, Perl, Haskell, C#, Cocoa, JavaScript, Node.js, Smalltalk, OCaml"
                + " and Delphi and other languages.";

        int len = s.length();
        byte[] bs = IntegerUtil.toFourBytes(len);
        System.arraycopy(bs, 0, buf, 0, 4);
        System.arraycopy(s.getBytes(), 0, buf, 4, s.length());

        a.writePage(p);
        a.close();
    }

    @Test
    public void testInitializeSize() throws Exception {
        Volume a = new Volume(fileName);
        a.initialize(Size.S64K);
        try {
            assertEquals(a.getPageSize(), Size.S16K);
        } finally {
            a.close();
        }
    }

    @Test
    public void testInitialize() throws Exception {
        Volume a = new Volume(fileName);
        a.initialize();
        try {
            assertEquals(a.getPageSize(), Size.S16K);
        } finally {
            a.close();
        }
    }

    @Test
    public void testClose() throws Exception {
        Volume a = new Volume(fileName);
        a.initialize();
        Page page = a.readPage(50);
        byte[] buf = page.getBuf();

        int len = IntegerUtil.fourBytes(buf, 0);
        String s = new String(buf, 4, len);
        try {
            assertTrue(s.startsWith(
                    "software framework, for scalable cross-language services development, combines a software stack"));
            assertTrue(s.endsWith(" and Delphi and other languages."));
        } finally {
            a.close();
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testReadPageLong() throws Exception {
        Volume a = new Volume(fileName + "2");
        try {
            a.initialize();
        } finally {
            a.close();
            a.close();
        }
    }

    @Test(expected = EOFException.class)
    public void testReadPagePage() throws Exception {
        Volume a = new Volume(fileName);
        a.initialize();
        try {
            Page page = a.readPage(0x921350);
            byte[] buf = page.getBuf();

            int len = IntegerUtil.fourBytes(buf, 0);
            String s = new String(buf, 4, len);
            throw new RuntimeException(s);
        } finally {
            a.close();
        }
    }

    @Test(expected = IOException.class)
    public void testWritePage() throws Exception {
        Volume a = new Volume(fileName);
        FieldUtil.setValue(a, "initialized", true);
        FileChannel channel = mock(FileChannel.class);
        when(channel.write(any(ByteBuffer.class), anyInt())).thenReturn(3, -1);
        FieldUtil.setValue(a, "channel", channel);
        FieldUtil.setValue(a, "pageSize", Size.S16K);
        try {
            Page p = new Page(Size.S16K);
            a.writePage(p);
        } finally {
            a.close();
        }
    }

}
