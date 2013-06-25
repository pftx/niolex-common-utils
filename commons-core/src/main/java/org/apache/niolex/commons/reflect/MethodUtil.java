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

import org.apache.commons.lang.reflect.MethodUtils;

/**
 * MethodUtil是一个通过反射机制来操作Java方法的工具类
 *
 * 目前提供的功能如下：
 * 1. public static final Method[] getMethods(Class<?> clazz)
 * 获取一个Java类定义的所有方法
 *
 * 2. public static final Method[] getMethods(Class<?> clazz, String name)
 * 获取一个Java类所有指定名字的方法
 *
 * 3. public static final Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes)
 * 获取一个Java类指定名字和方法签名的方法
 *
 * 4. public static final Object invokeMethod(Method m, Object host, Object[] args)
 * 在指定Java对象上调用指定的方法
 *
 *
 * @used 暂无项目使用
 * @category niolex-common-utils -> 公共库 -> 反射处理
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 */
public class MethodUtil extends MethodUtils {

    /**
     * 获取一个Java类定义的所有方法
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
     *
     * @param clazz 需要获取的类
     * @param name 指定方法的名字
     * @return 指定名字的方法的数组
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final Method[] getMethods(Class<?> clazz, String name) {
        Method[] oriArr = clazz.getDeclaredMethods();
        List<Method> outLst = new ArrayList<Method>();

        for (Method m : oriArr) {
            if (m != null && m.getName().equals(name)) {
                outLst.add(m);
            }
        }
        return outLst.toArray(new Method[outLst.size()]);
    }

    /**
     * 获取一个Java对象的所有类型和接口
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
     * 获取一个Java对象中所有指定名字的方法
     * 我们会递归的获取所有父类和接口的方法
     * 注意：接口只访问直接接口中的方法，接口的父接口不递归访问
     *
     * @param obj 需要获取方法的对象
     * @param name 指定方法的名字
     * @return 指定名字的方法的数组
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     */
    public static final Method[] getMethods(Object obj, String name) {
        List<Method> outLst = new ArrayList<Method>();

        Collection<Class<?>> clsSet = getAllTypes(obj);
        // Start to find methods.
        for (Class<?> cl : clsSet) {
            Method[] oriArr = cl.getDeclaredMethods();
            for (Method m : oriArr) {
                if (m != null && m.getName().equals(name)) {
                    outLst.add(m);
                }
            }
        }

        return outLst.toArray(new Method[outLst.size()]);
    }

    /**
     * 获取一个Java类指定名字和方法签名的方法
     * @param clazz 需要获取的类
     * @param name 指定方法的名字
     * @param parameterTypes 指定方法的签名
     * @return 指定名字和签名的方法
     * @throws SecurityException 如果设置了安全检查并拒绝对这个类使用反射
     * @throws NoSuchMethodException 如果该类当中不存在这样的方法
     */
    public static final Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes)
            throws SecurityException, NoSuchMethodException {
        return clazz.getDeclaredMethod(name, parameterTypes);
    }

    /**
     * 在指定Java对象上调用指定的方法
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
     *
     * @param methodName 需要调用的方法
     * @param host 用来调用指定方法的对象
     * @param args 用来调用指定方法的参数，如果指定方法不使用参数，则不输入
     * @return 调用指定的方法的返回值如果接口方法的声明返回类型是基本类型，则此值一定
     * 是相应基本包装对象类的实例；否则，它一定是可分配到声明返回类型的类型。如果此值为 null则
     * 接口方法的返回类型是void或者接口方法返回了null
     * @throws IllegalArgumentException 假如输入的参数和方法的参数签名不匹配
     * @throws IllegalAccessException 假如该方法不能被访问
     * @throws InvocationTargetException 假如该方法在执行过程中抛出了异常
     */
    public static final Object invokeMethod(String methodName, Object host, Object... args)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return invokeMethod(host, methodName, args);
    }
}
