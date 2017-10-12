package org.apache.niolex.commons.collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.apache.niolex.commons.collection.ConcurrentLRUCache.ItemEntry;
import org.apache.niolex.commons.collection.ConcurrentLRUCache.TableEntry;
import org.apache.niolex.commons.collection.ConcurrentLRUCache.ThreeQLRUList;
import org.apache.niolex.commons.concurrent.ThreadUtil;
import org.apache.niolex.commons.reflect.FieldUtil;

public class ConcurrentLRUCacheTest {

    private ConcurrentLRUCache<String, Integer> cache = new ConcurrentLRUCache<String, Integer>(100);

    @Test
    public void testHash() throws Exception {
        cache.put("demo", 33);
        assertEquals(1, cache.size());
        Integer i = cache.remove("demo");
        assertEquals(33, i.intValue());
        assertEquals(0, cache.size());
    }

    @Test
    public void testIndexFor() throws Exception {
        assertEquals(21, ConcurrentLRUCache.indexFor(65, 22));
        assertEquals(21, ConcurrentLRUCache.indexFor(-65, 22));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConcurrentLRUCache() throws Exception {
        new ConcurrentLRUCache<String, Integer>(50);
    }

    @Test(expected=NullPointerException.class)
    public void testSize1() throws Exception {
        cache.get(null);
    }

    @Test(expected=NullPointerException.class)
    public void testSize21() throws Exception {
        cache.put(null, null);
    }

    @Test(expected=NullPointerException.class)
    public void testSize22() throws Exception {
        cache.put("abcd", null);
    }

    @Test(expected=NullPointerException.class)
    public void testSize3() throws Exception {
        cache.remove(null);
    }

    @Test
    public void testGet() throws Exception {
        Integer i = cache.get("not yet implemented");
        assertNull(i);
        cache.put("nice", 7788);
        i = cache.get("nice");
        assertEquals(7788, i.intValue());

        Integer j = cache.remove("nice");
        assertEquals(i, j);

        i = cache.get("nice");
        assertNull(i);
        assertEquals(0, cache.size());
    }

    @Test
    public void testPut() throws Exception {
        Integer i = cache.put("nice", 7788);
        assertNull(i);
        Integer j = cache.put("nice", 6688);
        assertEquals(7788, j.intValue());
        i = cache.put("nlce", 7788);
        assertNull(i);
        assertEquals(2, cache.size());
    }

    @Test(expected=NullPointerException.class)
    public void testPutEx() throws Exception {
        FieldUtil.setValue(cache, "lruList", null);
        cache.put("duie", 44);
    }

    @Test
    public void testPutNoVictim() throws Exception {
        for (int i = 100; i < 201; ++ i) {
            cache.put("duie-" + i, i);
        }
        assertEquals(100, cache.size());
        ThreeQLRUList<String, Integer> tq = new ThreeQLRUList<String, Integer>();
        FieldUtil.setValue(cache, "lruList", tq);
        cache.put("duie", 44);
        assertEquals(101, cache.size());
    }

    @Test(expected=NullPointerException.class)
    public void testPutBadVictim() throws Exception {
        ConcurrentLRUCache<String, Integer> cache2 = new ConcurrentLRUCache<String, Integer>(100);
        for (int i = 100; i < 201; ++ i) {
            cache2.put("duie-" + i, i);
        }
        assertEquals(100, cache2.size());
        ThreeQLRUList<String, Integer> tq = FieldUtil.getValue(cache2, "lruList");

        for (int i = 100; i < 201; ++ i) {
            cache.put("duie-" + i, i);
        }
        assertEquals(100, cache.size());
        FieldUtil.setValue(cache, "lruList", tq);
        cache.put("duie", 44);
        assertEquals(100, cache.size());
    }

    @Test
    public void testPutBadVictim2() throws Exception {
        ConcurrentLRUCache<String, Integer> cache2 = new ConcurrentLRUCache<String, Integer>(100);
        for (int i = 100; i < 201; ++ i) {
            cache2.put("duie-" + i, i);
        }
        assertEquals(100, cache2.size());
        ThreeQLRUList<String, Integer> tq = FieldUtil.getValue(cache2, "lruList");

        for (int i = 100; i < 201; ++ i) {
            cache.put("duie-" + i, i);
        }
        assertEquals(100, cache.size());
        FieldUtil.setValue(cache, "lruList", tq);

        ItemEntry<String, Integer> e2 = FieldUtil.getValue(tq, "tail");
        ItemEntry<String, Integer> e3 = new ItemEntry<String, Integer>();

        FieldUtil.setValue(e2, "mapPrev", e2);
        FieldUtil.setValue(e2, "mapNext", e3);

        cache.put("duie", 44);
        assertEquals(101, cache.size());
    }

    @Test
    public void testRemove() throws Exception {
        assertNull(cache.remove("duie"));
    }

    @Test(expected=NullPointerException.class)
    public void testRemoveEx() throws Exception {
        cache.put("duie", 44);
        FieldUtil.setValue(cache, "lruList", null);
        assertNull(cache.remove("duie"));
    }

    @Test
    public void testThreeQLRUList() throws Exception {
        ThreeQLRUList<String, Integer> tq = new ThreeQLRUList<String, Integer>();
        ItemEntry<String,Integer> entry = tq.findVictim(3);
        assertNull(entry);
    }

    @Test(expected=NullPointerException.class)
    public void testThreeQLRUListNullHeader() throws Exception {
        ThreeQLRUList<String, Integer> tq = new ThreeQLRUList<String, Integer>();
        ItemEntry<String,Integer> e1 = new ItemEntry<String,Integer>();
        FieldUtil.setValue(tq, "tail", e1);
        tq.pushHeaderTime(100);
        tq.pushHeaderTime(1000);
        tq.pushHeaderTime(10000);
        ItemEntry<String,Integer> entry = tq.findVictim(3);
        assertNull(entry);
    }

    @Test(expected=NullPointerException.class)
    public void testThreeQLRUListNullEntryAdd() throws Exception {
        ThreeQLRUList<String, Integer> tq = new ThreeQLRUList<String, Integer>();
        ItemEntry<String,Integer> e1 = new ItemEntry<String,Integer>();
        FieldUtil.setValue(tq, "head", e1);
        tq.pushHeaderTime(100);
        tq.pushHeaderTime(1000);
        tq.pushHeaderTime(10000);
        tq.addEntry(null);
    }

    @Test(expected=NullPointerException.class)
    public void testThreeQLRUListNullEntryRm() throws Exception {
        ThreeQLRUList<String, Integer> tq = new ThreeQLRUList<String, Integer>();
        ItemEntry<String,Integer> e1 = new ItemEntry<String,Integer>();
        FieldUtil.setValue(tq, "head", e1);
        tq.pushHeaderTime(100);
        tq.pushHeaderTime(1000);
        tq.pushHeaderTime(10000);
        tq.removeEntry(null);
    }

    @Test
    public void testRemoveEntryFromMap() throws Exception {
        TableEntry<String, Integer> en = new TableEntry<String, Integer>();
        ItemEntry<String, Integer> e2 = new ItemEntry<String, Integer>();
        FieldUtil.setValue(e2, "mapNext", e2);
        cache.removeEntryFromMap(en, e2);
    }

    @Test
    public void testMissRate() throws Exception {
        int s = 0;
        for (int i = 0; i < 100; ++i) {
            s += testFindItemFromMapEntry();
        }

        System.out.println("My mis rate is: " + (s / 100) + ", it's " + ((s < 2600) ? "better" : "worse") + " than the default.");
    }

    public int testFindItemFromMapEntry() throws Exception {
        Cache<Integer, Integer> c = new ConcurrentLRUCache<Integer, Integer>(66);
        for (int i = 0; i < 66; ++i) {
            c.put(i, i);
            c.put(66 - i, 66 - i);
        }

        for (int i = 66; i < 99; ++i) {
            c.put(i, i);
            c.put(i - 34, i - 34);
        }

        for (int i = 100; i < 130; ++i) {
            c.put(i - 100, i - 100);
            c.put(i - 34, i - 34);
        }

        Cache<Integer, Integer> old_c = c;
        c = new LRUHashMap<Integer, Integer>(66);
        for (int i = 0; i < 66; ++i) {
            c.put(i, i);
            c.put(66 - i, 66 - i);
        }

        for (int i = 66; i < 99; ++i) {
            c.put(i, i);
            c.put(i - 34, i - 34);
        }

        for (int i = 100; i < 130; ++i) {
            c.put(i - 100, i - 100);
            c.put(i - 34, i - 34);
        }

        int nul1 = 0, nul2 = 0;
        for (int i = 0; i < 66; ++i) {
            Integer i1 = old_c.get(i);
            Integer i2 = c.get(i);

            //System.out.println(i1 + "\t" + i2);

            if (i1 == null)
                ++nul1;
            if (i2 == null)
                ++nul2;
        }

        System.out.println("MIS rate: " + nul1 + ", " + nul2);
        assertEquals(26, nul2);
        assertEquals(66, old_c.size());
        assertEquals(66, c.size());
        return nul1;
    }

    @Test
    public void testAddVisit() throws Exception {
        TableEntry<String, Integer> en = new TableEntry<String, Integer>();
        ItemEntry<String, Integer> e2 = new ItemEntry<String, Integer>();
        ItemEntry<String, Integer> e3 = new ItemEntry<String, Integer>();
        ItemEntry<String, Integer> e4 = new ItemEntry<String, Integer>();
        ItemEntry<String, Integer> e5 = new ItemEntry<String, Integer>();
        FieldUtil.setValue(en, "head", e2);
        FieldUtil.setValue(e2, "mapNext", e3);
        FieldUtil.setValue(e3, "mapNext", e4);
        FieldUtil.setValue(e4, "mapNext", e5);
        FieldUtil.setValue(e5, "mapNext", e2);
        FieldUtil.setValue(e2, "hash", 66);
        FieldUtil.setValue(e3, "hash", 77);
        FieldUtil.setValue(e4, "hash", 77);
        FieldUtil.setValue(e5, "hash", 77);
        FieldUtil.setValue(e2, "key", "Good");
        FieldUtil.setValue(e3, "key", "good");
        FieldUtil.setValue(e4, "key", "Good");
        FieldUtil.setValue(e5, "key", new String("Goode"));
        ItemEntry<String, Integer> f1 = cache.findItemFromMapEntry(en, 77, "Good");
        ItemEntry<String, Integer> f2 = cache.findItemFromMapEntry(en, 77, "Goode");

        assertEquals(e4, f1);
        assertEquals(e5, f2);
    }

    @Test
    public void testGetHit() throws Exception {
        cache.put("lex", 66);
        for (int i = 100; i < 199; ++ i) {
            cache.put("duie-" + i, i);
        }
        ThreadUtil.sleepAtLeast(1);
        assertEquals(66, cache.get("lex").intValue());
        ThreadUtil.sleepAtLeast(1);
        for (int i = 200; i < 299; ++ i) {
            cache.put("duie-" + i, i);
        }
        assertEquals(66, cache.get("lex").intValue());
        assertEquals(100, cache.size());

        TableEntry<String, Integer>[] table = FieldUtil.getValue(cache, "table");
        Integer entrySize = FieldUtil.getValue(cache, "entrySize");

        int cnt = 0;
        for (int i = 0; i < entrySize.intValue(); ++i) {
            TableEntry<String, Integer> en = table[i];
            for (ItemEntry<String, Integer> e2 = FieldUtil.getValue(en, "head"); e2 != null; e2 = FieldUtil.getValue(e2, "mapNext")) {
                ++cnt;
            }
        }
        assertEquals(100, cnt);
    }
}
