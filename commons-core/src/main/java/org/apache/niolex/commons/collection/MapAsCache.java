/**
 * MapAsCache.java
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

import java.util.Map;

/**
 * Decorate a Java Map to our Cache interface.
 * 
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 2.1.2
 * @since May 30, 2016
 */
public class MapAsCache<K, V> implements Cache<K, V> {
    
    /**
     * Create a new instance of {@link MapAsCache} powered by the specified map.
     * 
     * @param map the map used to backup this cache
     * @return the created cache
     */
    public static final <K, V> Cache<K, V> newInstance(Map<K, V> map) {
        return new MapAsCache<K, V>(map);
    }
    
    private final Map<K, V> map;

    /**
     * Construct a cache powered by the specified map.
     * 
     * @param map the map used to backup this cache
     */
    public MapAsCache(Map<K, V> map) {
        super();
        this.map = map;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public V get(K key) {
        return map.get(key);
    }

    @Override
    public V put(K key, V value) {
        return map.put(key, value);
    }

    @Override
    public V remove(K key) {
        return map.remove(key);
    }

}
