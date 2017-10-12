package org.apache.niolex.commons.collection;

import static org.apache.niolex.commons.test.Assert.assertIntEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.niolex.commons.collection.SegmentLFUCache.ItemEntry;
import org.apache.niolex.commons.reflect.FieldUtil;
import org.junit.Test;

public class SegmentLFUCacheTest {

    @Test
    public void testItemEntry() {
        ItemEntry<String, String> ie = new ItemEntry<String, String>();
        assertIntEquals(-1, FieldUtil.getValue(ie, "hash"));
        assertIntEquals(0, FieldUtil.getValue(ie, "visits"));
        assertNull(FieldUtil.getValue(ie, "key"));
        assertNull(FieldUtil.getValue(ie, "value"));
        assertNull(FieldUtil.getValue(ie, "linkPrev"));
        assertNull(FieldUtil.getValue(ie, "linkNext"));
        assertEquals(ie, FieldUtil.getValue(ie, "mapPrev"));
        assertEquals(ie, FieldUtil.getValue(ie, "mapNext"));
        
        ie = new ItemEntry<String, String>("a", "b", 3);
        assertIntEquals(3, FieldUtil.getValue(ie, "hash"));
        assertIntEquals(1, FieldUtil.getValue(ie, "visits"));
        assertEquals("a", FieldUtil.getValue(ie, "key"));
        assertEquals("b", FieldUtil.getValue(ie, "value"));
        assertNull(FieldUtil.getValue(ie, "linkPrev"));
        assertNull(FieldUtil.getValue(ie, "linkNext"));
        assertNull(FieldUtil.getValue(ie, "mapPrev"));
        assertNull(FieldUtil.getValue(ie, "mapNext"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNewSegmentLFUCacheTooSmall() throws Exception {
        new SegmentLFUCache<String, String>(100, 1);
    }
    
    @Test
    public void testNewSegmentLFUCacheTooConc() throws Exception {
        SegmentLFUCache<String, String> seg = new SegmentLFUCache<String, String>(3000000, 1000000000);
        assertIntEquals(16383, FieldUtil.getValue(seg, "segmentMask"));
        assertIntEquals(3000000, FieldUtil.getValue(seg, "maxSize"));
    }
    
    @Test
    public void testNewSegmentLFUCacheSmallConc() throws Exception {
        SegmentLFUCache<String, String> seg = new SegmentLFUCache<String, String>(2048, 16);
        assertIntEquals(15, FieldUtil.getValue(seg, "segmentMask"));
        assertIntEquals(2048, FieldUtil.getValue(seg, "maxSize"));
        assertEquals(0, seg.size());
        
        String s = seg.put("nice", "you");
        assertNull(s);
        
        s = seg.get("nice");
        assertEquals("you", s);
        s = seg.get("n1ce");
        assertNull(s);
        assertEquals(1, seg.size());
    }
    
    @Test
    public void testNewSegmentLFUCache() throws Exception {
        SegmentLFUCache<String, String> seg = new SegmentLFUCache<String, String>(2048, 16);
        
        for (int i = 0; i < 2048; ++i) {
            seg.put("nice" + i, "you" + i);
        }
        
        String s;
        for (int i = 0; i < 2048; ++i) {
            if (i == 1071) continue;
            s = seg.get("nice" + i);
            assertEquals(s, "you" + i);
        }
        
        assertEquals(2048, seg.size());
        seg.put("final", "go");
        
        for (int i = 0; i < 2048; ++i) {
            s = seg.get("nice" + i);
            if (s == null) {
                System.out.println("nice" + i + " is missing.");
                assertEquals(i, 1071);
            } else {
                assertEquals(s, "you" + i);
            }
        }
        
        assertEquals(2048, seg.size());
    }

    @Test(expected=NullPointerException.class)
    public void testSize() throws Exception {
        SegmentLFUCache<String, String> seg = new SegmentLFUCache<String, String>(2048, 2);
        String s = seg.put("nice", "you");
        assertNull(s);
        assertEquals(1, seg.size());
        seg.get(null);
    }

    @Test(expected=NullPointerException.class)
    public void testGet() throws Exception {
        SegmentLFUCache<String, String> seg = new SegmentLFUCache<String, String>(2048, 2);
        String s = seg.put(null, "you");
        assertNull(s);
        assertEquals(1, seg.size());
    }
    
    @Test
    public void testPutTooMany() throws Exception {
        SegmentLFUCache<String, String> seg = new SegmentLFUCache<String, String>(4096, 17);
        
        for (int i = 0; i < 10000; ++i) {
            seg.put("nice" + i, "you" + i);
        }
        
        String s;
        assertEquals(4096, seg.size());
        
        int h = 0, m = 0;
        for (int i = 5880; i < 10000; ++i) {
            s = seg.get("nice" + i);
            if (s == null) {
                // System.out.println("nice" + i + " is missing.");
                ++m;
            } else {
                ++h;
                assertEquals(s, "you" + i);
            }
        }
        
        seg.put("final", "go");
        assertEquals(4096, seg.size());
        seg.put("final", "yeah");
        assertEquals(4096, seg.size());
        
        System.out.println("Hit Rate: " + (h * 100 / (h + m)) + "%");
        
        s = seg.remove("final");
        assertEquals("yeah", s);
        assertEquals(4095, seg.size());
    }
    
    @Test(expected=NullPointerException.class)
    public void testPut() throws Exception {
        SegmentLFUCache<String, String> seg = new SegmentLFUCache<String, String>(2048, 2);
        String s = seg.put("nice", null);
        assertNull(s);
        assertEquals(1, seg.size());
    }
    
    @Test
    public void testRemove() throws Exception {
        SegmentLFUCache<String, String> seg = new SegmentLFUCache<String, String>(2048, 2);
        String s = seg.put("nice", "you");
        assertNull(s);
        assertEquals(1, seg.size());
        s = seg.remove("nice");
        assertEquals("you", s);
        assertEquals(0, seg.size());
        
        s = seg.remove("nice");
        assertNull(s);
        assertEquals(0, seg.size());
    }

    @Test(expected=NullPointerException.class)
    public void testRemoveNull() throws Exception {
        SegmentLFUCache<String, String> seg = new SegmentLFUCache<String, String>(2048, 2);
        String s = seg.put("nice", "you");
        assertNull(s);
        assertEquals(1, seg.size());
        seg.remove(null);
    }

}
