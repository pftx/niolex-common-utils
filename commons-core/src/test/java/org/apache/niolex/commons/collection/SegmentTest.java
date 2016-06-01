/**
 * SegmentTest.java
 *
 * Copyright 2016 the original author or authors.
 *
 * We licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.apache.niolex.commons.collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.niolex.commons.collection.SegmentLFUCache.ItemEntry;
import org.apache.niolex.commons.collection.SegmentLFUCache.Segment;
import org.apache.niolex.commons.reflect.FieldUtil;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 2.1.2
 * @since May 31, 2016
 */
public class SegmentTest {

    @Test(expected=IllegalArgumentException.class)
    public void testContorInv() {
        Segment<String, String> seg = new Segment<String, String>(66);
        ItemEntry<String, String> ie = seg.entryFor(0);
        assertEquals(-1, FieldUtil.getValue(ie, "hash"));
        assertEquals(0, FieldUtil.getValue(ie, "visits"));
        assertNull(FieldUtil.getValue(ie, "key"));
        assertNull(FieldUtil.getValue(ie, "value"));
        assertNull(FieldUtil.getValue(ie, "linkPrev"));
        assertNull(FieldUtil.getValue(ie, "linkNext"));
        assertEquals(ie, FieldUtil.getValue(ie, "mapPrev"));
        assertEquals(ie, FieldUtil.getValue(ie, "mapNext"));
    }
    
    @Test
    public void testContor() {
        Segment<String, String> seg = new Segment<String, String>(8);
        ItemEntry<String, String> ie = seg.entryFor(0);
        assertEquals(-1, FieldUtil.getValue(ie, "hash"));
        assertEquals(0, FieldUtil.getValue(ie, "visits"));
        assertNull(FieldUtil.getValue(ie, "key"));
        assertNull(FieldUtil.getValue(ie, "value"));
        assertNull(FieldUtil.getValue(ie, "linkPrev"));
        assertNull(FieldUtil.getValue(ie, "linkNext"));
        assertEquals(ie, FieldUtil.getValue(ie, "mapPrev"));
        assertEquals(ie, FieldUtil.getValue(ie, "mapNext"));
    }
    
    @Test
    public void testFindItem() {
        Segment<String, String> seg = new Segment<String, String>(8);
        seg.put(0, "a", "b");
        ItemEntry<String, String> ie1 = seg.findItem(0, "a");
        ItemEntry<String, String> ie2 = seg.findItem(8, "a");
        assertNull(ie2);
        assertEquals("b", FieldUtil.getValue(ie1, "value"));
        assertEquals(1, FieldUtil.getValue(ie1, "visits"));
        assertEquals(0, FieldUtil.getValue(ie1, "hash"));
        seg.put(0, "a", "c");
        ItemEntry<String, String> ie3 = seg.findItem(0, "a");
        assertEquals(ie1, ie3);
        ItemEntry<String, String> ie4 = seg.findItem(0, "b");
        assertNull(ie4);
        assertEquals("c", FieldUtil.getValue(ie3, "value"));
        assertEquals("c", FieldUtil.getValue(ie1, "value"));
        
        ItemEntry<String, String> ie = seg.entryFor(0);
        assertNull(FieldUtil.getValue(ie, "key"));
        assertNull(FieldUtil.getValue(ie, "value"));
        assertNull(FieldUtil.getValue(ie, "linkPrev"));
        assertNull(FieldUtil.getValue(ie, "linkNext"));
        assertEquals(ie1, FieldUtil.getValue(ie, "mapPrev"));
        assertEquals(ie1, FieldUtil.getValue(ie, "mapNext"));
        
        String s = seg.remove(0, "a");
        assertEquals("c", s);
        assertEquals(ie, FieldUtil.getValue(ie, "mapPrev"));
        assertEquals(ie, FieldUtil.getValue(ie, "mapNext"));
    }
    
    @Test
    public void testFindItemProfessional() {
        Segment<String, String> seg = new Segment<String, String>(8);
        seg.put(0, "a", "b");
        ItemEntry<String, String> ie1 = seg.findItem(0, "a");
        ItemEntry<String, String> ie2 = seg.findItem(8, "a");
        assertNull(ie2);
        assertEquals("b", FieldUtil.getValue(ie1, "value"));
        assertEquals(1, FieldUtil.getValue(ie1, "visits"));
        assertEquals(0, FieldUtil.getValue(ie1, "hash"));
        seg.put(8, "a", "c");
        ItemEntry<String, String> ie3 = seg.findItem(8, "a");
        assertNotEquals(ie1, ie3);
        ItemEntry<String, String> ie4 = seg.findItem(0, "b");
        assertNull(ie4);
        assertEquals("c", FieldUtil.getValue(ie3, "value"));
        assertEquals("b", FieldUtil.getValue(ie1, "value"));
        
        ItemEntry<String, String> ie = seg.entryFor(0);
        assertNull(FieldUtil.getValue(ie, "key"));
        assertNull(FieldUtil.getValue(ie, "value"));
        assertNull(FieldUtil.getValue(ie, "linkPrev"));
        assertNull(FieldUtil.getValue(ie, "linkNext"));
        assertEquals(ie1, FieldUtil.getValue(ie, "mapPrev"));
        assertEquals(ie3, FieldUtil.getValue(ie, "mapNext"));
        assertEquals(2, seg.size());
        
        String s = seg.remove(0, "a");
        assertEquals("b", s);
        assertEquals(ie3, FieldUtil.getValue(ie, "mapPrev"));
        assertEquals(ie3, FieldUtil.getValue(ie, "mapNext"));
        
        ItemEntry<String, String> ie5 = seg.findItem(8, new String("a"));
        assertEquals(ie5, ie3);
        assertEquals(1, seg.size());
        
        s = seg.remove(8, "a");
        assertEquals("c", s);
        assertEquals(ie, FieldUtil.getValue(ie, "mapPrev"));
        assertEquals(ie, FieldUtil.getValue(ie, "mapNext"));
        assertEquals(0, seg.size());
    }
    
    @Test
    public void testPut() {
        Segment<String, String> seg = new Segment<String, String>(8);
        String s = seg.put(0, "ax", "bc");
        assertNull(s);
        s = seg.put(0, "ax", "d");
        assertEquals("bc", s);
        s = seg.put(0, "li", "d");
        assertNull(s);
        
        ItemEntry<String, String> ie1 = seg.findItem(0, "li");
        assertNotNull(ie1);
        int e = seg.eviction();
        assertEquals(1, e);
        ie1 = seg.findItem(0, "li");
        assertNull(ie1);
        assertEquals(1, seg.size());
        
        ItemEntry<String, String> ie3 = seg.findItem(0, "ax");
        ItemEntry<String, String> ie = seg.entryFor(0);
        assertEquals(ie3, FieldUtil.getValue(ie, "mapPrev"));
        assertEquals(ie3, FieldUtil.getValue(ie, "mapNext"));
        
        e = seg.eviction();
        assertEquals(1, e);
        ie1 = seg.findItem(0, "ax");
        assertNull(ie1);
        assertEquals(ie, FieldUtil.getValue(ie, "mapPrev"));
        assertEquals(ie, FieldUtil.getValue(ie, "mapNext"));
        assertEquals(0, seg.size());
        
        e = seg.eviction();
        assertEquals(0, e);
    }
    
    @Test
    public void testEviction() {
        Segment<String, String> seg = new Segment<String, String>(8);
        int e;
        ItemEntry<String, String> ie1;
        ItemEntry<String, String> ie2;
        
        e = seg.eviction();
        assertEquals(0, e);
        
        String s = seg.put(0, "ax", "bc");
        assertNull(s);
        ie1 = seg.findItem(0, "ax");
        assertNotNull(ie1);
        
        e = seg.eviction();
        assertEquals(1, e);
        ie2 = seg.findItem(0, "ax");
        assertNull(ie2);
        
        s = seg.put(0, "ax", "bc");
        assertNull(s);
        s = seg.put(0, "ax", "de");
        assertEquals("bc", s);
        
        e = seg.eviction();
        assertEquals(0, e);
        ie2 = seg.findItem(0, "ax");
        assertNotNull(ie2);
        assertNotEquals(ie1, ie2);
        
        e = seg.eviction();
        assertEquals(1, e);
        ie2 = seg.findItem(0, "ax");
        assertNull(ie2);
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void testEvictionExceedsBatchSize() {
        Segment<String, String> seg = new Segment<String, String>(8);
        // batchSize = 5
        for (int i = 0; i < 16; ++i) {
            int j = i % 8;
            seg.put(j, "ev-" + j, "tar" + j);
        }
        int e;
        ItemEntry<String, String> LFUp1, LFUp2, LFUn1, LFUn2, ie2;
        ItemEntry<String, String>[] en = new ItemEntry[8];
        ItemEntry<String, String>[] ie = new ItemEntry[8];
        
        for (int i = 0; i < 8; ++i) {
            en[i] = seg.entryFor(i);
            ie[i] = FieldUtil.getValue(en[i], "mapPrev");
            ie2 = FieldUtil.getValue(en[i], "mapNext");
            assertEquals(ie[i], ie2);
        }
        
        ItemEntry<String, String> LFU = FieldUtil.getValue(seg, "LFUHead");
        LFUp1 = FieldUtil.getValue(LFU, "linkPrev");
        LFUn1 = FieldUtil.getValue(LFU, "linkNext");
        
        int cnt = 0;
        ie2 = LFUp1;
        while (ie2 != LFU) {
            ++cnt;
            ie2 = FieldUtil.getValue(ie2, "linkPrev");
        }
        assertEquals(8, cnt);
        cnt = 0;
        ie2 = LFUn1;
        while (ie2 != LFU) {
            ++cnt;
            ie2 = FieldUtil.getValue(ie2, "linkNext");
        }
        assertEquals(8, cnt);
        
        assertEquals(LFUp1, ie[0]);
        assertEquals(LFUn1, ie[7]);
        assertEquals(8, seg.size());
        
        e = seg.eviction();
        assertEquals(0, e);
        
        LFUp2 = FieldUtil.getValue(LFU, "linkPrev");
        LFUn2 = FieldUtil.getValue(LFU, "linkNext");
        assertNotEquals(LFUp1, LFUp2);
        assertNotEquals(LFUn1, LFUn2);
        assertEquals(LFUp2, ie[5]);
        assertEquals(LFUn2, ie[4]);
        assertEquals(8, seg.size());
        
        cnt = 0;
        ie2 = LFUp2;
        while (ie2 != LFU) {
            ++cnt;
            ie2 = FieldUtil.getValue(ie2, "linkPrev");
        }
        assertEquals(8, cnt);
        cnt = 0;
        ie2 = LFUn2;
        while (ie2 != LFU) {
            ++cnt;
            ie2 = FieldUtil.getValue(ie2, "linkNext");
        }
        assertEquals(8, cnt);
        
        // Really
        e = seg.eviction();
        assertEquals(1, e);
        
        LFUp2 = FieldUtil.getValue(LFU, "linkPrev");
        LFUn2 = FieldUtil.getValue(LFU, "linkNext");
        assertNotEquals(LFUp1, LFUp2);
        assertEquals(LFUn1, LFUn2);
        assertEquals(LFUp2, ie[1]);
        assertEquals(LFUn2, ie[7]);
        assertEquals(7, seg.size());
        
        cnt = 0;
        ie2 = LFUp2;
        while (ie2 != LFU) {
            ++cnt;
            int v = FieldUtil.getValue(ie2, "visits");
            ie2 = FieldUtil.getValue(ie2, "linkPrev");
            assertEquals(1, v);
        }
        assertEquals(7, cnt);
        cnt = 0;
        ie2 = LFUn2;
        while (ie2 != LFU) {
            ++cnt;
            int v = FieldUtil.getValue(ie2, "visits");
            ie2 = FieldUtil.getValue(ie2, "linkNext");
            assertEquals(1, v);
        }
        assertEquals(7, cnt);
    }
    
    @Test(expected=NullPointerException.class)
    public void testPutInvalid() {
        Segment<String, String> seg = new Segment<String, String>(8);
        FieldUtil.setValue(seg, "LFUHead", null);
        seg.put(-2, "a", "b");
    }
    
    @Test
    public void testRemoveValid() {
        Segment<String, String> seg = new Segment<String, String>(8);
        seg.put(-2, "a", "b");
        FieldUtil.setValue(seg, "LFUHead", null);
        String s = seg.remove(-2, "a");
        assertEquals("b", s);
        s = seg.remove(-2, "a");
        assertNull(s);
        FieldUtil.setValue(seg, "table", null);
    }
    
    @Test(expected=NullPointerException.class)
    public void testRemoveInvalid() {
        Segment<String, String> seg = new Segment<String, String>(8);
        seg.put(-2, "a", "b");
        FieldUtil.setValue(seg, "table", null);
        String s = seg.remove(-2, "a");
        assertEquals("b", s);
        s = seg.remove(-2, "a");
        assertNull(s);
    }
    
    @Test(expected=NullPointerException.class)
    public void testEvictionInvalid() {
        Segment<String, String> seg = new Segment<String, String>(8);
        seg.put(-2, "a", "b");
        String s = seg.remove(-2, "a");
        assertEquals("b", s);
        s = seg.remove(-2, "a");
        assertNull(s);
        FieldUtil.setValue(seg, "LFUHead", null);
        seg.eviction();
    }
}
