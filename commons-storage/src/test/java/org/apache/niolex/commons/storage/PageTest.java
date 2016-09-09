package org.apache.niolex.commons.storage;

import static org.junit.Assert.assertEquals;

import org.apache.niolex.commons.storage.Page.Size;
import org.junit.Test;

public class PageTest {

    @Test
    public void testPageSize16() throws Exception {
        Size a = Size.S16K;
        long address = a.maskAddress(1092109);
        System.out.println(String.format("%s %d(%x), %x <- %x", a, a.size(), a.size(), address, 1092109));
        assertEquals(0x108000, address);
    }

    @Test
    public void testPageSize64() throws Exception {
        Size a = Size.S64K;
        long address = a.maskAddress(1092109);
        System.out.println(String.format("%s %d(%x), %x <- %x", a, a.size(), a.size(), address, 1092109));
        assertEquals(0x100000, address);
    }

    @Test
    public void testPageSize256() throws Exception {
        Size a = Size.S256K;
        long address = a.maskAddress(1892109);
        System.out.println(String.format("%s %d(%x), %x <- %x", a, a.size(), a.size(), address, 1892109));
        assertEquals(0x1c0000, address);
    }

    @Test
    public void testPageSizeInt() throws Exception {
        Page a = new Page(Size.S16K, 33);
        assertEquals(0, a.getAddress());
    }

    @Test
    public void testGetFileName() throws Exception {
        Page a = new Page(Size.S16K, 0x180033);
        assertEquals(0x180000, a.getAddress());
        a.setFileName("/a/b/c");
        assertEquals("/a/b/c", a.getFileName());
    }

    @Test
    public void testSetFileName() throws Exception {
        Page a = new Page(Size.decode(2));
        assertEquals(Size.S256K, a.getSize());
    }

    @Test
    public void testGetAddress() throws Exception {
        Page a = new Page(Size.decode(1), 0x921830);
        assertEquals(0x920000, a.getAddress());
        a.setAddress(0x129831);
        assertEquals(0x120000, a.getAddress());
    }

    @Test
    public void testSetAddress() throws Exception {
        assertEquals(Size.S4M, Size.decode(4));
        assertEquals(Size.S16K, Size.decode(5));
        assertEquals(Size.S16K, Size.decode(6));
        assertEquals(Size.S16K, Size.decode(0));
    }

    @Test
    public void testSizeEncode() {
        assertEquals(0, Size.encode(Size.S16K));
        assertEquals(1, Size.encode(Size.S64K));
        assertEquals(2, Size.encode(Size.S256K));
        assertEquals(3, Size.encode(Size.S1M));
        assertEquals(4, Size.encode(Size.S4M));
    }

    @Test(expected = NullPointerException.class)
    public void testSizeEncodeNull() {
        assertEquals(0, Size.encode(null));
    }

    @Test
    public void testGetSize() throws Exception {
        Page a = new Page(Size.decode(3));
        assertEquals(Size.S1M, a.getSize());
    }

    @Test
    public void testGetBuf() throws Exception {
        Page a = new Page(Size.decode(3));
        assertEquals(a.getBuf().length, 1 << 20);
    }

}
