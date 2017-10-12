package org.apache.niolex.commons.file;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.HashSet;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class FileChannelUtilTest extends FileChannelUtil {

    static String PREX = System.getProperty("user.home") + "/fchnl";

    @BeforeClass
    public static void setUp() {
        DirUtil.mkdirsIfAbsent(PREX);
    }

    @AfterClass
    public static void tearDown() {
        DirUtil.delete(PREX, true);
    }

    @Test
    public void testReadFromPosition() throws Exception {
        Set<OpenOption> set = new HashSet<OpenOption>();
        set.add(StandardOpenOption.CREATE);
        set.add(StandardOpenOption.TRUNCATE_EXISTING);
        set.add(StandardOpenOption.READ);
        set.add(StandardOpenOption.WRITE);
        set.add(StandardOpenOption.DSYNC);
        set.add(StandardOpenOption.DELETE_ON_CLOSE);
        FileAttribute<?>[] NO_ATTRIBUTES = new FileAttribute[0];

        Path path = FileSystems.getDefault().getPath(PREX, "tmp.f");
        FileChannel channel = FileChannel.open(path, set, NO_ATTRIBUTES);
        String txt = "This is used to test text writing.";
        int s = writeToPosition(channel, ByteBuffer.wrap(txt.getBytes()), 0);
        assertEquals(34, s);

        ByteBuffer buf = ByteBuffer.allocate(34);
        int m = readFromPosition(channel, buf, 0);
        assertEquals(34, m);

        String ret = new String(buf.array());
        assertEquals(txt, ret);

        buf.clear();
        int l = readFromPosition(channel, buf, 3);
        assertEquals(31, l);
        String tic = new String(buf.array(), 0, buf.position());
        assertEquals(txt.substring(3), tic);
    }

    @Test
    public void testWriteToPosition() throws Exception {
        byte[] raw = FileUtil.getBinaryFileContentFromClassPath("Data.txt", FileChannelUtilTest.class);
        String pathname = PREX + "/data.m";
        FileUtil.setBin(pathname, raw);

        Path path = FileSystems.getDefault().getPath(PREX, "data.m");
        Set<OpenOption> set = new HashSet<OpenOption>();
        set.add(StandardOpenOption.READ);
        set.add(StandardOpenOption.DELETE_ON_CLOSE);
        FileAttribute<?>[] NO_ATTRIBUTES = new FileAttribute[0];
        FileChannel channel = FileChannel.open(path, set, NO_ATTRIBUTES);

        ByteBuffer buf = ByteBuffer.allocate(49);
        int m = readFromPosition(channel, buf, 332);
        assertEquals(49, m);
        String ret = new String(buf.array());
        Object txt = "How-to Ignore Unknown properties in JSON content?";
        assertEquals(txt, ret);
    }

    @Test
    public void testWriteToPositionFailure() throws Exception {
        FileChannel channel = mock(FileChannel.class);
        when(channel.write(any(ByteBuffer.class), anyInt())).thenReturn(3, -1);

        ByteBuffer buf = ByteBuffer.allocate(49);
        int m = writeToPosition(channel, buf, 332);
        assertEquals(3, m);
    }

}
