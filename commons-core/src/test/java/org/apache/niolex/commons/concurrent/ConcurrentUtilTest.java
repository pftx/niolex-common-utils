/**
 * ConcurrentUtilTest.java
 *
 * Copyright 2012 The original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.niolex.commons.concurrent;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

/**
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2012-12-20
 */
public class ConcurrentUtilTest {

    /**
     * Test method for {@link org.apache.niolex.commons.concurrent.ConcurrentUtil#initMap(java.util.concurrent.ConcurrentHashMap, java.lang.Object, java.lang.Object)}.
     */
    @Test
    public void testInitMap() {
        new ConcurrentUtil() {};
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
        map.put("a", "bc");
        String v = ConcurrentUtil.initMap(map, "a", "de");
        assertEquals("bc", v);
    }

    /**
     * Test method for {@link org.apache.niolex.commons.concurrent.ConcurrentUtil#initMap(java.util.concurrent.ConcurrentHashMap, java.lang.Object, java.lang.Object)}.
     */
    @Test
    public void testInitMap2() {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
        map.put("a", "bc");
        String v = ConcurrentUtil.initMap(map, "ab", "de");
        assertEquals("de", v);
    }

}
