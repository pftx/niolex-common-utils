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

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.niolex.commons.collection.CollectionUtil;

/**
 * MethodUtil is a utility class help programmers call methods reflectively.
 * <b>Reflection is time consuming operation, please use them cautiously.</b>
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 */
public class MethodUtil {

    /**
     * Retrieve all the types, including the class, super classes and interfaces of this object.
     *
     * @param obj the object to be used
     * @return the list contains all the types
     * @throws SecurityException if a security manager is present and the reflection is rejected
     */
    public static final List<Class<?>> getAllTypes(Object obj) {
        return getAllTypes(obj.getClass());
    }

    /**
     * Retrieve all the types, including this class, super classes and interfaces of this class.
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
     * Retrieve all the methods including static methods of this class and it's super classes and
     * all of it's interfaces.
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
     * Retrieve all the methods including static methods of this class and it's super classes.
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
     * Retrieve all the methods including static methods of this class, neither get super methods nor
     * interface methods.
     *
     * @param clazz the class to be used
     * @return the list contains all the methods including static methods of this class
     * @throws SecurityException if a security manager is present and the reflection is rejected
     * @see #getAllMethods(Class)
     * @see #getAllMethodsIncludeInterfaces(Class)
     */
    public static final List<Method> getThisMethods(Class<?> clazz) {
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
            raw = getThisMethods(clazz);
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
     * Retrieve all the methods with the specified method name from this class and
     * all of it's super classes including static methods.
     *
     * @param clazz the class to be used to find methods
     * @param methodName the method name
     * @return the list contains all the methods with the specified name
     * @throws SecurityException if a security manager is present and the reflection is rejected
     */
    public static final List<Method> getMethods(Class<?> clazz, String methodName) {
        return getMethods(clazz, MethodFilter.create().includeSuper().includeStatic()
                .methodName(methodName));
    }

    /**
     * Get the first found method with the specified method name from this class and
     * all of it's super classes including static methods.
     *
     * @param clazz the class to be used to find methods
     * @param methodName the method name
     * @return the first found method
     * @throws ItemNotFoundException if method not found in this class and all of it's super classes
     */
    public static final Method getFirstMethod(Class<?> clazz, String methodName) {
        List<Method> list = getMethods(clazz, methodName);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            throw new ItemNotFoundException("Method not found.", null);
        }
    }

    /**
     * Get the first found method with the specified method name from the host object including static methods.
     *
     * @param host the host object used to find method
     * @param methodName the method name
     * @return the first found method
     * @throws ItemNotFoundException if method not found in this class and all of it's super classes
     */
    public static final Method getFirstMethod(Object host, String methodName) {
        return getFirstMethod(host.getClass(), methodName);
    }

    /**
     * Retrieve the method with the specified name and parameter types from this class including static method.
     * If this method is not found, we will try to look at it from the super classes too.
     *
     * @param clazz the class to be used for reflection
     * @param methodName the method name
     * @param parameterTypes the method parameter types
     * @return the method if found
     * @throws SecurityException if a security manager is present and the reflection is rejected
     * @throws ItemNotFoundException if method not found in this class and all of it's super classes
     */
    public static final Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        try {
            return clazz.getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            clazz = clazz.getSuperclass();
            if (clazz != null) {
                return getMethod(clazz, methodName, parameterTypes);
            } else {
                throw new ItemNotFoundException("Method not found.", e);
            }
        }
    }

    /**
     * Retrieve the methods including static methods with the specified name and parameter types can be
     * assigned from the specified parameter types.
     * 
     * <pre>
     * We have mainly three kinds of relax considered here:
     *  1. long &lt;= int &lt;= short (Small type can be relaxed to a larger type for primitives)
     *  2. int &lt;= Integer or Integer &lt;= int (Auto boxing and un-boxing)
     *  3. A extends B then a &lt;= b (class hierarchy)
     * </pre>
     * 
     * <b>Caution! We can not do relax on wrapper types!</b>
     *
     * @param clazz the class to be used for reflection
     * @param methodName the method name
     * @param parameterTypes the parameter types used to call this method
     * @return the list contains all the methods satisfy the condition
     */
    public static final List<Method> getMethodsParamRelax(Class<?> clazz, String methodName,
            Class<?>... parameterTypes) {
        return getMethods(clazz, MethodFilter.create().includeSuper().includeStatic()
                .methodName(methodName).parameterTypes(parameterTypes));
    }

    /**
     * Invoke this method on the specified host object.
     * If it's a static method, the host object could be null.
     * We will set accessible to true to make sure we can access the specified method.
     *
     * @param host the host object
     * @param m the method to be invoked
     * @param args the parameters used to invoke method
     * @return the result from invoking the method
     * @throws IllegalArgumentException if the arguments are not correct for the method
     * @throws IllegalAccessException this exception will not be thrown
     * @throws InvocationTargetException if exception was thrown from the method
     * @throws SecurityException if failed to set accessible to true
     */
    public static final Object invokeMethod(Object host, Method m, Object... args)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        m.setAccessible(true);
        return m.invoke(host, args);
    }

    /**
     * Invoke the method with this specified method name and arguments on the host. We support
     * auto boxing and primitive relax.
     *
     * @param host the object to be used to invoke method
     * @param methodName the name of the method to be invoked
     * @param args the arguments to be used to invoke the specified method
     * @return the result from invoking the method
     * @throws ItemNotFoundException if there is no method with the specified name and arguments
     * @throws IllegalArgumentException if the arguments are not correct for the method
     * @throws IllegalAccessException this exception will not be thrown
     * @throws InvocationTargetException if exception was thrown from the method
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
     * Invoke the method with this specified method name and arguments on the host. We support
     * auto boxing and primitive relax.
     * We will set accessible to true to make sure we can access the specified method.
     *
     * @param host the object to be used to invoke method
     * @param methodName the name of the method to be invoked
     * @param parameterTypes the parameter types the method to be invoked
     * @param args the arguments to be used to invoke the method
     * @return the result from invoking the method or null if there is no return from the method
     * @throws ItemNotFoundException if there is no method with the specified name and parameter types
     * @throws IllegalArgumentException if the arguments are not correct for the method
     * @throws IllegalAccessException this exception will not be thrown
     * @throws InvocationTargetException if exception was thrown during the method execution
     */
    public static final Object invokeMethod(Object host, String methodName, Class<?>[] parameterTypes,
            Object... args) throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        // Check all methods to find the correct one.
        List<Method> methods = getMethodsParamRelax(host.getClass(), methodName, parameterTypes);
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
     *
     * @see #invokeMethod(Object, String, Object...)
     */
    public static Object invokePublicMethod(Object object, String methodName, Object... args)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return MethodUtils.invokeMethod(object, methodName, args);
    }

}
