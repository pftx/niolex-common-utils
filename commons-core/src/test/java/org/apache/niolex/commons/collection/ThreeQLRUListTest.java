/**
 * ThreeQLRUListTest.java
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

import static org.apache.niolex.commons.test.Assert.assertLongEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.niolex.commons.collection.ConcurrentLRUCache.ItemEntry;
import org.apache.niolex.commons.collection.ConcurrentLRUCache.ThreeQLRUList;
import org.apache.niolex.commons.reflect.FieldUtil;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 2.1.2
 * @since May 27, 2016
 */
public class ThreeQLRUListTest {
    
    ThreeQLRUList<String, String> list = new ThreeQLRUList<String, String>();
    
    @Test
    public void testPushHeaderTime() throws Exception {
        list.pushHeaderTime(66);
        assertLongEquals(0l, FieldUtil.getValue(list, "lastRoundHeaderTime"));
        assertLongEquals(0l, FieldUtil.getValue(list, "middleRoundHeaderTime"));
        assertLongEquals(66l, FieldUtil.getValue(list, "firstRoundHeaderTime"));
        list.pushHeaderTime(88);
        assertLongEquals(0l, FieldUtil.getValue(list, "lastRoundHeaderTime"));
        assertLongEquals(66l, FieldUtil.getValue(list, "middleRoundHeaderTime"));
        assertLongEquals(88l, FieldUtil.getValue(list, "firstRoundHeaderTime"));
        list.pushHeaderTime(123);
        assertLongEquals(66l, FieldUtil.getValue(list, "lastRoundHeaderTime"));
        assertLongEquals(88l, FieldUtil.getValue(list, "middleRoundHeaderTime"));
        assertLongEquals(123l, FieldUtil.getValue(list, "firstRoundHeaderTime"));
    }
    
    @Test
    public void testHash() throws Exception {
        assertNull(FieldUtil.getValue(list, "head"));
        assertNull(FieldUtil.getValue(list, "tail"));
        
        ItemEntry<String, String> cur1 = new ItemEntry<String, String>();
        ItemEntry<String, String> cur2 = new ItemEntry<String, String>();
        ItemEntry<String, String> cur3 = new ItemEntry<String, String>();
        list.addEntry(cur1);
        list.addEntry(cur2);
        list.addEntry(cur3);
        assertEquals(cur3, FieldUtil.getValue(list, "head"));
        assertEquals(cur1, FieldUtil.getValue(list, "tail"));
        
        assertEquals(cur2, FieldUtil.getValue(cur3, "linkNext"));
        assertNull(FieldUtil.getValue(cur3, "linkPrev"));
        assertEquals(cur1, FieldUtil.getValue(cur2, "linkNext"));
        assertEquals(cur3, FieldUtil.getValue(cur2, "linkPrev"));
        assertEquals(cur2, FieldUtil.getValue(cur1, "linkPrev"));
        assertNull(FieldUtil.getValue(cur1, "linkNext"));
        
        list.removeEntry(cur1);
        assertEquals(cur3, FieldUtil.getValue(list, "head"));
        assertEquals(cur2, FieldUtil.getValue(list, "tail"));
        assertEquals(cur2, FieldUtil.getValue(cur3, "linkNext"));
        assertNull(FieldUtil.getValue(cur3, "linkPrev"));
        assertNull(FieldUtil.getValue(cur2, "linkNext"));
        assertEquals(cur3, FieldUtil.getValue(cur2, "linkPrev"));
        
        list.removeEntry(cur3);
        list.removeEntry(cur3);
        list.removeEntry(cur1);
        list.addEntry(cur1);
        list.addEntry(cur3);
        list.removeEntry(cur1);
        assertEquals(cur3, FieldUtil.getValue(list, "head"));
        assertEquals(cur2, FieldUtil.getValue(list, "tail"));
        assertEquals(cur2, FieldUtil.getValue(cur3, "linkNext"));
        assertNull(FieldUtil.getValue(cur3, "linkPrev"));
        assertNull(FieldUtil.getValue(cur2, "linkNext"));
        assertEquals(cur3, FieldUtil.getValue(cur2, "linkPrev"));
        
        list.removeEntry(cur3);
        list.addEntry(cur1);
        assertEquals(cur1, FieldUtil.getValue(list, "head"));
        assertEquals(cur2, FieldUtil.getValue(list, "tail"));
        assertEquals(cur2, FieldUtil.getValue(cur1, "linkNext"));
        assertNull(FieldUtil.getValue(cur1, "linkPrev"));
        assertNull(FieldUtil.getValue(cur2, "linkNext"));
        assertEquals(cur1, FieldUtil.getValue(cur2, "linkPrev"));
        
        list.removeEntry(cur1);
        assertEquals(cur2, FieldUtil.getValue(list, "head"));
        assertEquals(cur2, FieldUtil.getValue(list, "tail"));
        assertNull(FieldUtil.getValue(cur2, "linkPrev"));
        assertNull(FieldUtil.getValue(cur2, "linkNext"));
        
        list.removeEntry(cur2);
        assertNull(FieldUtil.getValue(list, "head"));
        assertNull(FieldUtil.getValue(list, "tail"));
    }
    
    @Test
    public void testFindVictim() throws Exception {
        assertNull(FieldUtil.getValue(list, "head"));
        assertNull(FieldUtil.getValue(list, "tail"));
        @SuppressWarnings("unchecked")
        ItemEntry<String, String>[] curArr = new ItemEntry[100];
        
        for (int i = 0; i < 100; ++i) {
            ItemEntry<String, String> cur1 = new ItemEntry<String, String>();
            FieldUtil.setValue(cur1, "lastVisitAt", i + 1L);
            curArr[i] = cur1;
            list.addEntry(cur1);
        }
        
        ItemEntry<String, String> cur = list.findVictim(30);
        assertNull(cur);
        assertEquals(curArr[99], FieldUtil.getValue(list, "head"));
        assertEquals(curArr[0], FieldUtil.getValue(list, "tail"));
        
        list.pushHeaderTime(33);
        list.pushHeaderTime(66);
        list.pushHeaderTime(99);
        cur = list.findVictim(30);
        assertEquals(curArr[0], cur);
        FieldUtil.setValue(curArr[1], "lastVisitAt", 33 + 1L);
        cur = list.findVictim(30);
        assertEquals(curArr[2], cur);
        assertEquals(curArr[1], FieldUtil.getValue(list, "head"));
        assertEquals(curArr[3], FieldUtil.getValue(list, "tail"));
        
        list.pushHeaderTime(3);
        list.pushHeaderTime(22);
        list.pushHeaderTime(53);
        cur = list.findVictim(30);
        assertEquals(curArr[52], cur);
        assertEquals(curArr[51], FieldUtil.getValue(list, "head"));
        assertEquals(curArr[53], FieldUtil.getValue(list, "tail"));
    }
    
    
    @Test
    public void testFindVictimAgain() throws Exception {
        assertNull(FieldUtil.getValue(list, "head"));
        assertNull(FieldUtil.getValue(list, "tail"));
        @SuppressWarnings("unchecked")
        ItemEntry<String, String>[] curArr = new ItemEntry[100];
        
        for (int i = 0; i < 100; ++i) {
            ItemEntry<String, String> cur1 = new ItemEntry<String, String>();
            FieldUtil.setValue(cur1, "lastVisitAt", i + 1L);
            curArr[i] = cur1;
            list.addEntry(cur1);
        }
        
        ItemEntry<String, String> cur = list.findVictim(30);
        assertNull(cur);
        assertEquals(curArr[99], FieldUtil.getValue(list, "head"));
        assertEquals(curArr[0], FieldUtil.getValue(list, "tail"));
        
        cur = list.findVictim(30);
        assertEquals(curArr[99], FieldUtil.getValue(list, "head"));
        assertEquals(curArr[1], FieldUtil.getValue(list, "tail"));
        assertEquals(curArr[0], cur);
    }
}
