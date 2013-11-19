/**
 * ProxyUtil.java
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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * ProxyUtil是一个用来通过反射机制来动态创建Java对象的代理的工具类
 * 请注意用来创建代理的Java对象必须是面向接口的，否则无法成功创建代理
 *
 * 目前提供的功能如下：
 * 1. public static final <T> T newProxyInstance(Object host, ProxyHandler h)
 * 动态创建一个对象的Java代理,请注意用来创建代理的Java对象必须是面向接口的，否则无法成功创建代理
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 */
public class ProxyUtil {

    /**
     * 动态创建一个对象的Java代理时使用的接口
     * 通过该接口用户可以在代理对象的每一个方法调用的前后做一些处理
     *
     * 目前提供的功能如下：
     * 1. public void invokeBefore(Object proxy, Method method, Object[] args)
     * 在代理对象的每一个方法调用的前面做一些处理
     *
     * 2. public void invokeAfter(Object proxy, Method method, Object[] args, Object ret)
     * 在代理对象的每一个方法调用的后面做一些处理
     *
     * @see ProxyUtil
     * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
     * @version 1.0.0
     */
    public static interface ProxyHandler {

        /**
         * 在代理对象的每一个方法调用的前面做一些处理
         *
         * @param proxy 在其上调用方法的代理实例
         * @param method 对应于在代理实例上调用的接口方法的 Method 实例
         * @param args 传入代理实例上方法调用的参数值的对象数组，如果接口方法不使用参数，则为 null
         */
        public void invokeBefore(Object proxy, Method method, Object[] args);

        /**
         * 在代理对象的每一个方法调用的后面做一些处理
         *
         * @param proxy 在其上调用方法的代理实例
         * @param method 对应于在代理实例上调用的接口方法的 Method 实例
         * @param args 传入代理实例上方法调用的参数值的对象数组，如果接口方法不使用参数，则为 null
         * @param ret 从代理实例的方法调用返回的值。如果接口方法的声明返回类型是基本类型，则此值一定
         * 是相应基本包装对象类的实例；否则，它一定是可分配到声明返回类型的类型。如果此值为 null则
         * 接口方法的返回类型是void或者接口方法返回了null
         */
        public void invokeAfter(Object proxy, Method method, Object[] args, Object ret);
    }

    /**
     * 动态创建一个对象的Java代理请注意用来创建代理的Java对象必须是面向接口的，否则无法成功创建代理
     *
     * @param <T> 动态代理的返回类型
     * @param host 用来创建代理的对象，请注意用来创建代理的Java对象必须是面向接口的，否则无法成功创建代理
     * @param h 代理方法的实现对象
     * @return 动态代理对象
     */
    @SuppressWarnings("unchecked")
    public static final <T> T newProxyInstance(T host, ProxyHandler h) {
        // 创建内部桩，实现对宿主对象的调用以及代理方法的调用
        InvocationHandler handler = new ProxyStub(h, host);
        // 通过Java自带的方法创建对宿主对象的动态代理
        Object ret = Proxy.newProxyInstance(ProxyUtil.class.getClassLoader(),
                host.getClass().getInterfaces(), handler);
        // 对动态代理的返回结果进行强制类型转换
        return (T)ret;
    }
}

/**
 * ProxyStub是一个在ProxyUtil内部来动态创建Java对象的代理的工具类
 * 请忽略该类
 *
 * @see ProxyUtil
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 */
class ProxyStub implements InvocationHandler {
    // 代理方法的实现对象
    private ProxyUtil.ProxyHandler handler;
    // 用来创建代理的对象，请注意用来创建代理的Java对象必须是面向接口的，否则无法成功创建代理
    private Object host;


    /**
     * ProxyStub的构造函数
     *
     * @param handler
     * @param host
     */
    public ProxyStub(ProxyUtil.ProxyHandler handler, Object host) {
        super();
        this.handler = handler;
        this.host = host;
    }

    /* (non-Javadoc)
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        handler.invokeBefore(host, method, args);
        Object ret = method.invoke(host, args);
        handler.invokeAfter(host, method, args, ret);
        return ret;
    }

}
