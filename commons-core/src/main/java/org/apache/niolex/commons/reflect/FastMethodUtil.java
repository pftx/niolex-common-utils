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
 * <p>中： FastMethodUtil是一个通过Reflect ASM来操作Java方法的工具类
 * 注意！本类只能操作public/protected/package修饰的方法，私有方法请通过MethodUtil操作
 * </p><p>
 * En: FastMethodUtil using Reflect ASM to operate on Java bean to achieve high speed.
 * Notion! This utility can only operate on public/protected/package methods. For other
 * private methods, please use {@link org.apache.niolex.commons.reflect.MethodUtil}
 * </p><pre>
 * 目前提供的功能如下：
 * 1. public static final String[] getMethods(Class&lt;?&gt; clazz)
 * 获取一个Java类定义的所有非私有方法
 *
 * 2. public static final MethodAccess getMethodAccess(Class&lt;?&gt; clazz)
 * 获取一个Java类所对应的方法操作类
 *
 * 3. public static final Object invokeMethod(String methodName, Object host, Object[] args)
 * 在指定Java对象上调用指定的方法</pre>
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @see org.apache.niolex.commons.reflect.MethodUtil
 */
public class FastMethodUtil {
    
    private static final ConcurrentHashMap<Class<?>, MethodAccess> METHOD_ACCESS_MAP = new ConcurrentHashMap<Class<?>, MethodAccess>();

    /**
     * En: Retrieve all the non-private methods defined in this class.<br>
     * 中： 获取一个Java类定义的所有非私有方法
     *
     * @param clazz 需要获取的类
     * @return 所有方法数组
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final String[] getMethods(Class<?> clazz) {
        return getMethodAccess(clazz).getMethodNames();
    }

    /**
     * En: Get the method access object of this class.<br>
     * 中： 获取一个Java类所对应的方法操作类
     *
     * @param clazz 需要获取的类
     * @return 所对应的方法操作类
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
     * En: Invoke this method on the host object.<br>
     * 中： 在指定Java对象上调用指定的方法
     *
     * @param host 用来调用指定方法的对象
     * @param methodName 需要调用的方法
     * @param args 用来调用指定方法的参数，如果指定方法不使用参数，则不输入
     * @return 调用指定的方法的返回值如果接口方法的声明返回类型是基本类型，则此值一定
     * 是相应基本包装对象类的实例；否则，它一定是可分配到声明返回类型的类型。如果此值为 null则
     * 接口方法的返回类型是void或者接口方法返回了null
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
