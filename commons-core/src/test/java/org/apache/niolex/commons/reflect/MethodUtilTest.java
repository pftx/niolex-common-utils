/**
 * MethodUtilTest.java
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
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import org.apache.niolex.commons.reflect.MethodUtil;
import org.junit.Test;


public class MethodUtilTest {

    @Test
    public void testMethods() throws Throwable {
        Method[] arr = MethodUtil.getMethods(MethodTestBean.class);
        Assert.assertTrue(arr.length >= 5);
    }

    @Test
    public void testMethodsName() throws Throwable {
        Method[] arr = MethodUtil.getMethods(MethodTestBean.class, "echoName");
        Assert.assertEquals(arr.length, 4);
    }

    @Test
    public void testMethodsNameParam() throws Throwable {
        Method m = MethodUtil.getMethod(MethodTestBean.class, "echoName", String.class);
        Assert.assertEquals(m.getName(), "echoName");

        MethodTestBean host = new MethodTestBean("niolex-common-utils");
        Object ret = MethodUtil.invokeMethod(m, host, "Xie, Jiyun");
        Assert.assertEquals(ret, "Xie, Jiyun");

        m = MethodUtil.getMethod(MethodTestBean.class, "echoName");
        ret = MethodUtil.invokeMethod(m, host);
        Assert.assertEquals(ret, "niolex-common-utils");
    }

    public static class Base extends MethodUtil {
        public void find() {}
    }

    public static interface Inter {
        public void find(int k);
    }

    public static class Con extends Base implements Inter {

        /**
         * This is the override of super method.
         * @see org.apache.niolex.commons.reflect.MethodUtilTest.Inter#find(int)
         */
        @Override
        public void find(int k) {
        }

        public int find(int k, int m) {
            return k * m;
        }

    }

    @Test
    public void testGetMethodsObjectString() throws Exception {
        Object obj = new Con();
        Method[] arr = MethodUtil.getMethods(obj, "find");
        System.out.println("All find in Con:");
        for (Method m : arr)
            System.out.println("\t" + m);
        Assert.assertEquals(arr.length, 4);
    }

    @Test
    public void testGetMethodsObjectStringArrayList() throws Exception {
        List<Method> outLst = new ArrayList<Method>();
        Method[] arr = MethodUtil.getMethods(outLst, "toArray");
        System.out.println("All toArray in ArrayList:");
        for (Method m : arr)
            System.out.println("\t" + m);
        Assert.assertEquals(arr.length, 8);
    }
}
