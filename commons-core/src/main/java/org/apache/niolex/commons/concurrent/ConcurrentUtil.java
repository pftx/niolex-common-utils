/**
 * ConcurrentUtil.java
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

import java.util.concurrent.ConcurrentHashMap;

/**
 * Some common concurrent methods.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2012-12-20
 */
public class ConcurrentUtil {

    /**
     * Init the map with this new value if this key is absent in the specified map.
     * Otherwise we return the old value associated with this key.
     *
     * @param map the map you want to init.
     * @param key the key you want to init.
     * @param newValue the new value ready to put into this map.
     * @return the new or old value whichever is associated with this key.
     */
    public static final <K, V> V initMap(ConcurrentHashMap<K, V> map, K key, V newValue) {
        V oldValue = map.putIfAbsent(key, newValue);
        if (oldValue == null) {
            return newValue;
        } else {
            return oldValue;
        }
    }
}
