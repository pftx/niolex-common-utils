package org.apache.niolex.commons.collection;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class MapAsCacheTest {
    
    Map<String, Integer> map = new HashMap<String, Integer>();
    

    @Test
    public void testNewInstance() throws Exception {
        Cache<String, Integer> c = MapAsCache.newInstance(map);
        c.put("not yet implemented", 33);
        assertEquals(33, c.get("not yet implemented").intValue());
    }

    @Test
    public void testMapAsCache() throws Exception {
        Cache<String, Integer> c = MapAsCache.newInstance(map);
        c.put("not yet implemented", 44);
        assertEquals(44, c.get("not yet implemented").intValue());
        assertEquals(1, c.size());
    }

    @Test
    public void testRemove() throws Exception {
        Cache<String, Integer> c = MapAsCache.newInstance(map);
        c.put("not yet implemented", 44);
        assertEquals(44, c.get("not yet implemented").intValue());
        assertEquals(44, c.remove("not yet implemented").intValue());
        assertEquals(0, c.size());
    }

}
