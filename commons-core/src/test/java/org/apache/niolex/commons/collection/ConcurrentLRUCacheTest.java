package org.apache.niolex.commons.collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

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

    @Test
    public void testRemove() throws Exception {
        assertNull(cache.remove("duie"));
    }

    @Test
    public void testRemoveEntryFromMap() throws Exception {
        throw new RuntimeException("not yet implemented");
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
        throw new RuntimeException("not yet implemented");
    }

}
