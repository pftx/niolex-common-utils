/**
 * FastMethodUtil.java
 *
 * Copyright 2011 Niolex, Inc.
 *
 * Niolex licenses this file to you under the Apache License, version 2.0
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

import com.esotericsoftware.reflectasm.MethodAccess;

/**
 * FastMethodUtil using Reflect ASM to operate on Java bean to achieve high speed.<br>
 * <b>Notion!</b> This utility can only operate on public/protected/package methods. For other
 * private methods, please use {@link org.apache.niolex.commons.reflect.MethodUtil}
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @see org.apache.niolex.commons.reflect.MethodUtil
 */
public class FastMethodUtil {

    private static final ConcurrentHashMap<Class<?>, MethodAccess> METHOD_ACCESS_MAP = new ConcurrentHashMap<Class<?>, MethodAccess>();

    /**
     * Retrieve all the non-private method names defined in the specified class.
     *
     * @param clazz the class to be used to retrieve method names
     * @return all the non-private method names
     * @throws RuntimeException if error occurred when constructing the method access class
     */
    public static final String[] getMethods(Class<?> clazz) {
        return getMethodAccess(clazz).getMethodNames();
    }

    /**
     * Get the method access object of the specified class.
     *
     * @param clazz the class to be used to retrieve method access
     * @return the method access object
     * @throws RuntimeException if error occurred when constructing the method access class
     */
    public static final MethodAccess getMethodAccess(Class<?> clazz) {
        MethodAccess ma = METHOD_ACCESS_MAP.get(clazz);
        if (ma == null) {
            ma = MethodAccess.get(clazz);
            METHOD_ACCESS_MAP.putIfAbsent(clazz, ma);
        }
        return ma;
    }

    /**
     * Invoke the specified method on the host object with the specified arguments.
     *
     * @param host the host object used to invoke the specified method
     * @param methodName the method name
     * @param args the method arguments
     * @return the object returned from invoking the specified method
     */
    public static final Object invokeMethod(Object host, String methodName, Object... args) {
    	MethodAccess access = getMethodAccess(host.getClass());
    	if (args != null && args.length != 0) {
    		Class<?>[] clazz = new Class<?>[args.length];
    		for (int i = 0; i < args.length; ++i) {
    			clazz[i] = args[i].getClass();
    		}
    		return access.invoke(host, access.getIndex(methodName, clazz), args);
    	}
        return access.invoke(host, methodName, args);
    }

}
