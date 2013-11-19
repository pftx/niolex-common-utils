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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.reflect.MethodUtils;

/**
 * MethodUtil is a utility class help programmers call methods reflectively.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 */
public class MethodUtil {

    /**
     * 获取一个Java类定义的所有方法
     * Retrieve all the methods of this class.
     *
     * @param clazz 需要获取的类
     * @return 所有方法数组
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final Method[] getMethods(Class<?> clazz) {
        return clazz.getDeclaredMethods();
    }

    /**
     * 获取一个Java类所有指定名字的方法
     * Retrieve all the methods with the specified name from this class.
     *
     * @param clazz 需要获取的类
     * @param name 指定方法的名字
     * @return 指定名字的方法的数组
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final Method[] getMethods(Class<?> clazz, String name) {
        List<Method> outList = new ArrayList<Method>();
        getMethods(outList, clazz, name);
        return outList.toArray(new Method[outList.size()]);
    }

    /**
     * 获取一个Java类所有指定名字的方法
     * Retrieve all the methods with the specified name from this class.
     *
     * @param outList 用来存放结果的列表
     * @param clazz 需要获取的类
     * @param name 指定方法的名字
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final void getMethods(List<Method> outList, Class<?> clazz, String name) {
        for (Method m : clazz.getDeclaredMethods()) {
            if (name.equals(m.getName())) {
                outList.add(m);
            }
        }
    }

    /**
     * 获取一个Java对象的所有类型和接口
     * Retrieve all the [class]/[super class]/[interfaces] of this object.
     *
     * @param obj 需要获取类型的对象
     * @return 类型集合
     */
    public static final Collection<Class<?>> getAllTypes(Object obj) {
        // Store all the classes and interfaces here.
        HashSet<Class<?>> clsSet = new HashSet<Class<?>>();
        getAllTypes(obj.getClass(), clsSet);
        return clsSet;
    }

    /**
     * 获取一个Java类型的所有父类和接口
     * Retrieve all the [class]/[super class]/[interfaces] of this object.
     *
     * @param cls 需要获取类型的类
     * @param clsSet 类型集合
     */
    public static final void getAllTypes(Class<?> cls, HashSet<Class<?>> clsSet) {
        if (cls != null && !clsSet.contains(cls)) {
            clsSet.add(cls);
            for (Class<?> itf : cls.getInterfaces()) {
                getAllTypes(itf, clsSet);
            }
            getAllTypes(cls.getSuperclass(), clsSet);
        }
    }

    /**
     * 获取一个Java对象中所有指定名字的方法.我们会递归的获取所有父类和接口的方法
     * Retrieve all the methods with the specified name from this object. We will
     * recursively iterate all the methods in super class and interfaces.
     *
     * @param obj 需要获取方法的对象
     * @param name 指定方法的名字
     * @return 指定名字的方法的数组
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final Method[] getMethods(Object obj, String name) {
        List<Method> outList = new ArrayList<Method>();

        Collection<Class<?>> clsSet = getAllTypes(obj);
        // Start to find methods.
        for (Class<?> cl : clsSet) {
            getMethods(outList, cl, name);
        }

        return outList.toArray(new Method[outList.size()]);
    }

    /**
     * 获取一个Java类指定名字和方法签名的方法
     * Retrieve the method with the specified name and parameter types from this class.
     *
     * @param clazz 需要获取的类
     * @param name 指定方法的名字
     * @param parameterTypes 指定方法的签名
     * @return 指定名字和签名的方法
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     * @throws ItemNotFoundException 如果该类当中不存在这样的方法
     */
    public static final Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            return clazz.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new ItemNotFoundException("Method not found.", e);
        }
    }

    /**
     * 获取一个Java对象指定名字和方法签名的方法
     * Retrieve the method with the specified name and parameter types from this object. We will
     * recursively iterate all the methods in super class and interfaces and return if we find one.
     *
     * @param obj 需要获取方法的对象
     * @param name 指定方法的名字
     * @param parameterTypes 指定方法的签名
     * @return 指定名字和签名的方法
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     * @throws ItemNotFoundException 如果该对象当中不存在这样的方法
     */
    public static final Method getMethod(Object obj, String name, Class<?>... parameterTypes) {
        Collection<Class<?>> clsSet = getAllTypes(obj);
        // Start to find method.
        for (Class<?> cl : clsSet) {
            try {
                return cl.getDeclaredMethod(name, parameterTypes);
            } catch (NoSuchMethodException e) {}
        }
        throw new ItemNotFoundException("Method not found.", null);
    }

    /**
     * 在指定Java对象上调用指定的方法
     * Invoke this method on the host.
     *
     * @param m 需要调用的方法
     * @param host 用来调用指定方法的对象
     * @param args 用来调用指定方法的参数，如果指定方法不使用参数，则不输入
     * @return 调用指定的方法的返回值如果接口方法的声明返回类型是基本类型，则此值一定
     * 是相应基本包装对象类的实例；否则，它一定是可分配到声明返回类型的类型。如果此值为 null则
     * 接口方法的返回类型是void或者接口方法返回了null
     * @throws IllegalArgumentException 假如输入的参数和方法的参数签名不匹配
     * @throws IllegalAccessException 假如该方法不能被访问
     * @throws InvocationTargetException 假如该方法在执行过程中抛出了异常
     */
    public static final Object invokeMethod(Method m, Object host, Object... args)
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
        Method m = getMethod(host, methodName, parameterTypes);
        m.setAccessible(true);
        return m.invoke(host, args);
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
