/**
 * MethodUtil.java
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.apache.niolex.commons.collection.CollectionUtil;

/**
 * MethodUtil is a utility class help programmers call methods reflectively.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 */
public class MethodUtil {

    /**
     * Retrieve all the methods of this class and it's super classes.
     * <p>
     * We don't get methods of interfaces, because every method in a interface must have
     * the real definition in the classes.
     * </p>
     *
     * @param clazz the class to be used
     * @return the list contains all the methods
     * @throws SecurityException if a security manager is present and the reflection is rejected
     * @see #getAllMethodsIncludeInterfaces(Class)
     */
    public static final List<Method> getAllMethods(Class<?> clazz) {
        List<Method> outList = new ArrayList<Method>();
        do {
            CollectionUtil.addAll(outList, clazz.getDeclaredMethods());
            clazz = clazz.getSuperclass();
        } while (clazz != null);
        return outList;
    }

    /**
     * Retrieve all the [class]/[super class]/[interfaces] of this object.
     *
     * @param obj the object to be used
     * @return the list contains all the types
     * @throws SecurityException if a security manager is present and the reflection is rejected
     */
    public static final List<Class<?>> getAllTypes(Object obj) {
        return getAllTypes(obj.getClass());
    }


    /**
     * Retrieve all the [class]/[super class]/[interfaces] of this class.
     *
     * @param clazz the class to be used
     * @return the list contains all the types
     * @throws SecurityException if a security manager is present and the reflection is rejected
     */
    public static final List<Class<?>> getAllTypes(Class<?> clazz) {
        // Store all the classes and interfaces here.
        List<Class<?>> list = new ArrayList<Class<?>>();
        List<Class<?>> interfaces = new ArrayList<Class<?>>();
        // Step 1. Add this class and all the super classes.
        do {
            list.add(clazz);
            CollectionUtil.addAll(interfaces, clazz.getInterfaces());
            clazz = clazz.getSuperclass();
        } while (clazz != null);
        // Step 2. Add all the interfaces.
        HashSet<Class<?>> clsSet = new HashSet<Class<?>>();
        for (int i = 0; i < interfaces.size(); ++i) {
            clazz = interfaces.get(i);
            // Use this hash set to filter duplicates.
            if (clsSet.contains(clazz)) {
                continue;
            }
            clsSet.add(clazz);
            list.add(clazz);
            CollectionUtil.addAll(interfaces, clazz.getInterfaces());
        }
        return list;
    }

    /**
     * Retrieve all the methods of this class and it's super classes and all of it's interfaces.
     *
     * @param clazz the class to be used
     * @return the list contains all the methods
     * @throws SecurityException if a security manager is present and the reflection is rejected
     * @see #getAllMethods(Class)
     */
    public static final List<Method> getAllMethodsIncludeInterfaces(Class<?> clazz) {
        List<Method> outList = new ArrayList<Method>();
        for (Class<?> type : getAllTypes(clazz)) {
            CollectionUtil.addAll(outList, type.getDeclaredMethods());
        }
        return outList;
    }

    /**
     * Retrieve all the methods of this class, don't get super methods and interface methods.
     *
     * @param clazz the class to be used
     * @return the list contains all the methods
     * @throws SecurityException if a security manager is present and the reflection is rejected
     * @see #getAllMethods(Class)
     * @see #getAllMethodsIncludeInterfaces(Class)
     */
    public static final List<Method> getMethods(Class<?> clazz) {
        List<Method> outList = new ArrayList<Method>();
        CollectionUtil.addAll(outList, clazz.getDeclaredMethods());
        return outList;
    }

    /**
     * The interface used to filter methods.
     *
     * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
     * @version 1.0.0
     * @since 2014-1-6
     */
    public static interface Filter {

        /**
         * Test whether we should include interface methods.
         *
         * @return true if include interfaces, false otherwise
         */
        public boolean isIncludeInterfaces();

        /**
         * Test whether we should include super methods.
         *
         * @return true if include super, false otherwise
         */
        public boolean isIncludeSuper();

        /**
         * Test whether this method is valid for return.
         *
         * @param m the method to be tested
         * @return true if valid, false otherwise
         */
        public boolean isValid(Method m);

    }

    /**
     * Retrieve all the methods with the specified filter.
     *
     * @param clazz the class to be used
     * @param filter the filter used to filter methods
     * @return the list contains all the methods which satisfy the filter
     * @throws SecurityException if a security manager is present and the reflection is rejected
     */
    public static final List<Method> getMethods(Class<?> clazz, Filter filter) {
        List<Method> raw = null;
        if (filter.isIncludeInterfaces()) {
            raw = getAllMethodsIncludeInterfaces(clazz);
        } else if (filter.isIncludeSuper()) {
            raw = getAllMethods(clazz);
        } else {
            raw = getMethods(clazz);
        }
        List<Method> outList = new ArrayList<Method>();
        for (Method m : raw) {
            if (filter.isValid(m)) {
                outList.add(m);
            }
        }
        return outList;
    }

    /**
     * Retrieve all the methods with the specified method name from this object class and
     * all of it's super classes.
     *
     * @param obj the object to be used to find methods
     * @param methodName the method name
     * @return the list contains all the methods with this name
     * @throws SecurityException if a security manager is present and the reflection is rejected
     */
    public static final List<Method> getMethods(Object obj, String methodName) {
        return getMethods(obj.getClass(), MethodFilter.create().includeSuper()
                .methodName(methodName));
    }

    /**
     * Retrieve the method with the specified name and parameter types from this class.
     * If this method is not found, we will try to look at it from the super class too.
     *
     * @param clazz the class to be used for reflection
     * @param name the method name
     * @param parameterTypes the method parameter types
     * @return the method if found
     * @throws SecurityException if a security manager is present and the reflection is rejected
     * @throws ItemNotFoundException if method not found in this class and all of it's super classes
     */
    public static final Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            return clazz.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            clazz = clazz.getSuperclass();
            if (clazz != null) {
                return getMethod(clazz, name, parameterTypes);
            } else {
                throw new ItemNotFoundException("Method not found.", e);
            }
        }
    }

    /**
     * Invoke this method on the specified host object.
     * If it's a static method, the host object could be null.
     *
     * @param host the host object
     * @param m the method to be invoked
     * @param args the parameters used to invoke method
     * @return the result of invoking the method
     * @throws IllegalArgumentException if the arguments are not correct for the method
     * @throws IllegalAccessException if the method can not be accessed
     * @throws InvocationTargetException if exception was thrown from the method
     */
    public static final Object invokeMethod(Object host, Method m, Object... args)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        m.setAccessible(true);
        return m.invoke(host, args);
    }

    /**
     * 在指定Java对象上调用指定的方法
     * Invoke the method with this specified method name on the host.
     *
     * @param host 用来调用指定方法的对象
     * @param methodName 需要调用的方法
     * @param args 用来调用指定方法的参数，如果指定方法不使用参数，则不输入
     * @return 调用指定的方法的返回值如果接口方法的声明返回类型是基本类型，则此值一定
     * 是相应基本包装对象类的实例；否则，它一定是可分配到声明返回类型的类型。如果此值为 null则
     * 接口方法的返回类型是void或者接口方法返回了null
     * @throws ItemNotFoundException 假如输入的参数和方法的参数签名不匹配
     * @throws IllegalArgumentException 假如出现其他参数问题
     * @throws IllegalAccessException 假如该方法不能被访问
     * @throws InvocationTargetException 假如该方法在执行过程中抛出了异常
     */
    public static final Object invokeMethod(Object host, String methodName, Object... args)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (args == null) {
            args = ArrayUtils.EMPTY_OBJECT_ARRAY;
        }
        int arguments = args.length;
        Class<?>[] parameterTypes = new Class<?>[arguments];
        for (int i = 0; i < arguments; i++) {
            parameterTypes[i] = args[i].getClass();
        }
        return invokeMethod(host, methodName, parameterTypes, args);
    }

    /**
     * 在指定Java对象上调用指定的方法
     * Invoke the method with this specified method name on the host.
     *
     * @param host 用来调用指定方法的对象
     * @param methodName 需要调用的方法
     * @param parameterTypes 指定方法的签名
     * @param args 用来调用指定方法的参数，如果指定方法不使用参数，则不输入
     * @return 调用指定的方法的返回值如果接口方法的声明返回类型是基本类型，则此值一定
     * 是相应基本包装对象类的实例；否则，它一定是可分配到声明返回类型的类型。如果此值为 null则
     * 接口方法的返回类型是void或者接口方法返回了null
     * @throws ItemNotFoundException 假如输入的参数和方法的参数签名不匹配
     * @throws IllegalArgumentException 假如出现其他参数问题
     * @throws IllegalAccessException 假如该方法不能被访问
     * @throws InvocationTargetException 假如该方法在执行过程中抛出了异常
     */
    public static final Object invokeMethod(Object host, String methodName, Class<?>[] parameterTypes,
            Object... args) throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        // Check all methods to find the correct one.
        List<Method> methods = getMethods(host.getClass(), MethodFilter.create().includeSuper()
                .methodName(methodName).parameterTypes(parameterTypes));
        if (methods.size() > 0) {
            Method m = methods.get(0);
            m.setAccessible(true);
            return m.invoke(host, args);
        }
        throw new ItemNotFoundException("Method not found.", null);
    }

    /**
     * <p>Invoke a named method whose parameter type matches the object type.</p>
     *
     * <p>This method delegates directly to {@link MethodUtils#invokeMethod(Object, String, Object[])}.</p>
     *
     * <p>This method supports calls to methods taking primitive parameters
     * via passing in wrapping classes. So, for example, a <code>Boolean</code> object
     * would match a <code>boolean</code> primitive.</p>
     *
     * <b>This method only work for public/protected/package access methods.</b>
     *
     * @param object invoke method on this object
     * @param methodName get method with this name
     * @param args use these arguments
     * @return The value returned by the invoked method
     *
     * @throws NoSuchMethodException if there is no such accessible method
     * @throws InvocationTargetException wraps an exception thrown by the method invoked
     * @throws IllegalAccessException if the requested method is not accessible via reflection
     */
    public static Object invokePublicMethod(Object object, String methodName, Object... args)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return MethodUtils.invokeMethod(object, methodName, args);
    }

}
