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
import java.util.Collection;
import java.util.List;

import org.junit.Assert;

import org.apache.niolex.commons.reflect.MethodUtil;
import org.junit.Test;


public class MethodUtilTest extends MethodUtil {


    @Test
    public void testAllMethodsInterface() throws Throwable {
        List<Method> list = getAllMethods(EchoName.class);
        System.out.println(list);
    }
    @Test
    public void testAllMethods() throws Throwable {
        List<Method> arr = MethodUtil.getAllMethods(MethodTestBean.class);
        Assert.assertTrue(arr.length >= 5);
    }

    @Test
    public void testMethodsName() throws Throwable {
        Method[] arr = MethodUtil.getMethods(MethodTestBean.class, "echoName");
        Assert.assertEquals(arr.length, 4);
    }

    @Test
    public void testGetAllTypesObject() throws Exception {
        MethodTestBean t = new MethodTestBean("Lex");
        Assert.assertEquals(getAllTypes(t).size(), 4);
    }

    @Test
    public void testGetAllMethods() throws Exception {
        MethodTestBean t = new MethodTestBean("Lex");
        Collection<Method> arr = MethodUtil.getAllMethods(t, "echoName");
        Assert.assertEquals(arr.size(), 6);
    }

    @Test
    public void testGetMethodWithArgType() throws Exception {
        Method m = MethodUtil.getMethod(MethodTestBean.class, "echoName", String.class);
        Assert.assertEquals(m.getName(), "echoName");
        Assert.assertEquals(m.getParameterTypes().length, 1);
        Assert.assertEquals(m.getParameterTypes()[0], String.class);
        Assert.assertEquals(m.getReturnType(), String.class);
    }

    @Test(expected=ItemNotFoundException.class)
    public void testGetMethodWithArgTypeNotFound() throws Exception {
        Method m = MethodUtil.getMethod(MethodTestBean.class, "echoName", Integer.class);
        Assert.assertEquals(m.getName(), "echoName");
        Assert.assertEquals(m.getParameterTypes().length, 1);
        Assert.assertEquals(m.getParameterTypes()[0], String.class);
        Assert.assertEquals(m.getReturnType(), String.class);
    }

    @Test
    public void testMethodFromObj() throws Throwable {
        Method m = null;
        MethodTestBean host = new MethodTestBean("niolex-common-utils");
        m = MethodUtil.getMethod(host, "echoName", int.class);
        Assert.assertEquals(m.getName(), "echoName");
        Assert.assertEquals(m.getParameterTypes().length, 1);
        Assert.assertEquals(m.getParameterTypes()[0], int.class);
        Assert.assertEquals(m.getReturnType(), String.class);
    }

    @Test(expected=ItemNotFoundException.class)
    public void testMethodFromObjNotFound() throws Throwable {
        Method m = null;
        MethodTestBean host = new MethodTestBean("niolex-common-utils");
        m = MethodUtil.getMethod(host, "echoName", boolean.class);
        Assert.assertEquals(m.getName(), "echoName");
        Assert.assertEquals(m.getParameterTypes().length, 1);
        Assert.assertEquals(m.getParameterTypes()[0], int.class);
        Assert.assertEquals(m.getReturnType(), String.class);
    }

    @Test
    public void testInvokeMethodObj() throws Throwable {
        Method m = null;
        MethodTestBean host = new MethodTestBean("niolex-common-utils");
        Object ret = MethodUtil.invokeMethod(host, "echoName", "Xie, Jiyun");
        Assert.assertEquals(ret, "Xie, Jiyun");

        m = MethodUtil.getMethod(MethodTestBean.class, "echoName");
        ret = MethodUtil.invokeMethod(m, host);
        Assert.assertEquals(ret, "niolex-common-utils");

        ret = MethodUtil.invokeMethod(host, "echoName");
        Assert.assertEquals(ret, "niolex-common-utils");

        ret = MethodUtil.invokeMethod(host, "echoName", (Object[]) null);
        Assert.assertEquals(ret, "niolex-common-utils");
    }

    @Test
    public void testInvokeMethodObjAotuCast() throws Throwable {
        MethodTestBean host = new MethodTestBean("lex");
        Object ret = MethodUtil.invokeMethod(host, "echoName", (byte)6);
        Assert.assertEquals(ret, "lex");

        ret = MethodUtil.invokeMethod(host, "echoName", "God Like", (short)9);
        Assert.assertEquals(ret, "God Like");
    }

    @Test(expected=ItemNotFoundException.class)
    public void testInvokeMethodNotFound() throws Throwable {
        MethodTestBean host = new MethodTestBean("lex");
        Object ret = MethodUtil.invokeMethod(host, "echoName", true);
        Assert.assertEquals(ret, "lex");
    }

    public static interface Inter {
        public void find(int k);
    }

    public static interface Cool extends Inter {
        public int cool();
    }

    public static class Base extends MethodUtil implements Cool {
        private int coo = 0;

        public void find() {}

        public void find(int k) {
            coo = k;
        }

        public int cool() {
            return coo * joke();
        }

        private int joke() {
            return 6;
        }
    }

    public static class Con extends Base implements Inter {

        public void find(int k) {
        }

        public int find(int k, int m) {
            return k * m;
        }
    }

    @Test
        public void testGetAllMethodsObjectString() throws Exception {
            Object obj = new Con();
            Collection<Method> arr = MethodUtil.getAllMethods(obj, "find");
            Assert.assertEquals(arr.size(), 5);
        }

    @Test
        public void testGetAllMethodsObjectStringArrayList() throws Exception {
            List<Method> outLst = new ArrayList<Method>();
            Collection<Method> arr = MethodUtil.getAllMethods(outLst, "toArray");
            Assert.assertEquals(arr.size(), 8);
        }

    @Test
    public void testInvokeMethodStringObjectObjectArray() throws Exception {
        Object obj = new Con();
        Object res = MethodUtil.invokeMethod(obj, "find", 921, 83);
        Assert.assertEquals(76443, res);
    }

    @Test
    public void testInvokePublicMethod() throws Exception {
        MethodTestBean host = new MethodTestBean("niolex-common-utils");
        Object ret = MethodUtil.invokePublicMethod(host, "echoName", "Xie, Jiyun", 8334);
        Assert.assertEquals(ret, "Xie, Jiyun");
    }

    @Test
    public void testInvokePrivateMethod() throws Exception {
        Object obj = new Con();
        Object res = MethodUtil.invokeMethod(obj, "joke");
        Assert.assertEquals(6, res);
    }

    @Test(expected=NoSuchMethodException.class)
    public void testInvokePrivateMethodFalse() throws Exception {
        Object obj = new Con();
        Object res = MethodUtil.invokePublicMethod(obj, "joke");
        Assert.assertEquals(6, res);
    }

}
