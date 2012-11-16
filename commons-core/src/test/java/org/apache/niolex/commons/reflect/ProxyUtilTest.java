/**
 * ProxyUtilTest.java
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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;

import org.apache.niolex.commons.reflect.ProxyUtil;
import org.apache.niolex.commons.reflect.ProxyUtil.ProxyHandler;
import org.junit.Test;



public class ProxyUtilTest {

    @Test
    public void testProxy() {
        Map<String, String> mapTest = new HashMap<String, String>();
        ProxyTestHandler h = new ProxyTestHandler();
        mapTest = ProxyUtil.newProxyInstance(mapTest, h);

        mapTest.put("gmail", "Xie, Jiyun");
        String strValue = mapTest.get("gmail");
        Assert.assertEquals(strValue, "Xie, Jiyun");
        Assert.assertEquals(h.getInvokeNum(), 2);
    }

}

/**
 * 用来统计对代理对象的调用次数，以及每次调用消耗的时间
 *
 * @used 暂无项目使用
 * @category niolex-common-utils -> 公共库 -> 反射处理
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 */
class ProxyTestHandler implements ProxyHandler {
    // Record the start time when invoke a method.
    private long start;
    // Record the method invoke number.
    private int invokeNum = 0;

    /* (non-Javadoc)
     * @see com.gmail.veyron.reflect.ProxyUtil.ProxyHandler#invokeAfter(java.lang.Object, java.lang.reflect.Method, java.lang.Object[], java.lang.Object)
     */
    @Override
    public void invokeAfter(Object proxy, Method method, Object[] args, Object ret) {
        System.out.println("Call method => " + method.getName() +
                " consumed " + (System.currentTimeMillis() - start) + "ms.");
    }

    /* (non-Javadoc)
     * @see com.gmail.veyron.reflect.ProxyUtil.ProxyHandler#invokeBefore(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     */
    @Override
    public void invokeBefore(Object proxy, Method method, Object[] args) {
        start = System.currentTimeMillis();
        ++invokeNum;
    }

    public int getInvokeNum() {
        return invokeNum;
    }

}