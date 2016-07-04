/**
 * FastNewInstance.java
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
package org.apache.niolex.commons.reflect;

import java.util.concurrent.ConcurrentHashMap;

import com.esotericsoftware.reflectasm.ConstructorAccess;

/**
 * 50% faster to create new instance than using Java reflection.
 * 
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 2.1.2
 * @since Jul 1, 2016
 */
public class FastNewInstance {
    
    private static final ConcurrentHashMap<Class<?>, ConstructorAccess<?>> CSTR_ACCESS_MAP = new ConcurrentHashMap<Class<?>, ConstructorAccess<?>>();
    
    /**
     * Get the constructor access of this class.
     * 
     * @param clazz the class
     * @return the constructor access
     */
    public static final <T> ConstructorAccess<T> getConstructorAccess(Class<T> clazz) {
        @SuppressWarnings("unchecked")
        ConstructorAccess<T> ca = (ConstructorAccess<T>) CSTR_ACCESS_MAP.get(clazz);
        if (ca == null) {
            ca = ConstructorAccess.get(clazz);
            CSTR_ACCESS_MAP.putIfAbsent(clazz, ca);
        }
        return ca;
    }
    
    /**
     * Create a new instance of this class.
     * 
     * @param clazz the class
     * @return the created new instance
     */
    public static final <T> T newInstance(Class<T> clazz) {
        return getConstructorAccess(clazz).newInstance();
    }
    
}
