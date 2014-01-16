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
    public void testAllMethods() throws Throwable {
        List<Method> arr = MethodUtil.getAllMethods(MethodTestBean.class);
        System.out.println(arr.size());
        Assert.assertTrue(arr.size() >= 19);
        Assert.assertTrue(arr.size() <= 21);
    }

    @Test
    public void testGetAllTypesObject() throws Exception {
        MethodTestBean t = new MethodTestBean("Lex");
        Assert.assertEquals(getAllTypes(t).size(), 8);
    }

    @Test
    public void testAllTypes() {
        Collection<Class<?>> types = getAllTypes("Not Yet.");
        System.out.println(types);
        Assert.assertEquals(types.size(), 5);
    }

    @Test
    public void testAllTypesComplicate() {
        Collection<Class<?>> types = getAllTypes(MethodTestBean.class);
        System.out.println(types);
        Assert.assertEquals(types.size(), 8);
    }

    @Test
    public void testAllMethodsOfInterface() throws Throwable {
        List<Method> list = getAllMethods(EchoName.class);
        Assert.assertEquals(list.size(), 1);
    }

    @Test
    public void testAllMethodsIncludeInterface() throws Throwable {
        List<Method> list = getAllMethodsIncludeInterfaces(EchoName.class);
        Assert.assertEquals(list.size(), 3);
    }

    @Test
    public void testMethods() {
        List<Method> arr = getMethods(MethodTestBean.class);
        System.out.println(arr);
        Assert.assertTrue(arr.size() >= 5);
        Assert.assertTrue(arr.size() <= 6);
    }

    @Test
    public void testMethodsThisOnly() throws Throwable {
        List<Method> arr = MethodUtil.getMethods(MethodTestBean.class, MethodFilter.create().methodName("echoName"));
        Assert.assertEquals(arr.size(), 4);
    }

    @Test
    public void testMethodsHasSuper() throws Throwable {
        List<Method> arr = MethodUtil.getMethods(MethodTestBean.class, MethodFilter.create().
                includeSuper().methodName("echoName"));
        Assert.assertEquals(arr.size(), 5);
    }

    @Test
    public void testMethodsWithInterfaces() throws Throwable {
        List<Method> arr = MethodUtil.getMethods(MethodTestBean.class, MethodFilter.create().
                includeInterfaces().methodName("echoName"));
        Assert.assertEquals(arr.size(), 8);
    }

    @Test
    public void testMethodsObjectString() throws Throwable {
        MethodTestBean t = new MethodTestBean("Lex");
        List<Method> arr = MethodUtil.getMethods(t.getClass(), "echoName");
        Assert.assertEquals(arr.size(), 5);
    }

    @Test
    public void testGetFirstMethod() throws Throwable {
        MethodTestBean t = new MethodTestBean("Lex");
        Method m = getFirstMethod(t, "echoName");
        Assert.assertEquals(m.getName(), "echoName");
        Assert.assertEquals(m.getParameterTypes().length, 0);
        Assert.assertEquals(m.getReturnType(), String.class);
    }

    @Test
    public void testGetFirstMethodConc() throws Throwable {
        Method m = getFirstMethod(Conc.class, "find");
        Assert.assertEquals(m.getName(), "find");
        Assert.assertEquals(m.getParameterTypes().length, 1);
        Assert.assertEquals(m.getParameterTypes()[0], int.class);
        Assert.assertEquals(m.getReturnType(), void.class);
    }

    @Test
    public void testGetFirstMethodFromSuper() throws Throwable {
        Method m = getFirstMethod(Conc.class, "cool");
        Assert.assertEquals(m.getName(), "cool");
        Assert.assertEquals(m.getParameterTypes().length, 0);
        Assert.assertEquals(m.getReturnType(), int.class);
    }

    @Test(expected=ItemNotFoundException.class)
    public void testGetFirstMethodNotFound() throws Throwable {
        Method m = getFirstMethod(Jenny.class, "cool");
        Assert.assertEquals(m.getName(), "cool");
        Assert.assertEquals(m.getParameterTypes().length, 0);
        Assert.assertEquals(m.getReturnType(), int.class);
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
    public void testGetMethodSmallInt() throws Throwable {
        Method m = MethodUtil.getMethod(MethodTestBean.class, "echoName", int.class);
        Assert.assertEquals(m.getName(), "echoName");
        Assert.assertEquals(m.getParameterTypes().length, 1);
        Assert.assertEquals(m.getParameterTypes()[0], int.class);
        Assert.assertEquals(m.getReturnType(), String.class);
    }

    @Test
    public void testGetMethodFromSuper() throws Throwable {
        Method m = MethodUtil.getMethod(MethodTestBean.class, "echoLevel");
        Assert.assertEquals(m.getName(), "echoLevel");
        Assert.assertEquals(m.getParameterTypes().length, 0);
        Assert.assertEquals(m.getReturnType(), int.class);
    }

    @Test
    public void testGetMethodFromSuperAgain() throws Throwable {
        Sub a = new Sub();
        Super b = new Super();
        Assert.assertEquals(8, a.inc());
        List<Method> arr = MethodUtil.getMethods(Sub.class, MethodFilter.create().includeSuper()
                .n("inc"));
        int i = (Integer) arr.get(0).invoke(a);
        int j = (Integer) arr.get(1).invoke(a);
        int k = (Integer) arr.get(1).invoke(b);
        System.out.println(arr);
        Assert.assertEquals(9, i);
        Assert.assertEquals(10, j);
        Assert.assertEquals(3, k);
        Method m = MethodUtil.getMethod(Sub.class, "isSuper");
        Assert.assertEquals(m.getName(), "isSuper");
        Assert.assertEquals(m.getParameterTypes().length, 0);
        Assert.assertEquals(m.getReturnType(), boolean.class);
        Assert.assertFalse((Boolean)m.invoke(a));
        Assert.assertTrue((Boolean)m.invoke(b));
    }

    @Test
    public void testInvokeMethodObj() throws Throwable {
        Method m = null;
        MethodTestBean host = new MethodTestBean("niolex-common-utils");
        Object ret = MethodUtil.invokeMethod(host, "echoName", "Xie, Jiyun");
        Assert.assertEquals(ret, "Xie, Jiyun");

        m = MethodUtil.getMethod(MethodTestBean.class, "echoName");
        ret = MethodUtil.invokeMethod(host, m);
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
        Assert.assertEquals(ret, "lex-6");

        ret = MethodUtil.invokeMethod(host, "echoName", "God Like", (short)9);
        Assert.assertEquals(ret, "God Like");
    }

    @Test(expected=ItemNotFoundException.class)
    public void testInvokeMethodNotFound() throws Throwable {
        MethodTestBean host = new MethodTestBean("lex");
        Object ret = MethodUtil.invokeMethod(host, "echoName", true);
        Assert.assertEquals(ret, "lex");
    }

    @Test
    public void testGetAllMethodsObjectString() throws Exception {
        Object obj = new Conc();
        Collection<Method> arr = MethodUtil.getMethods(obj.getClass(), "find");
        Assert.assertEquals(arr.size(), 4);
    }

    @Test
    public void testGetAllMethodsObjectStringArrayList() throws Exception {
        List<Method> outLst = new ArrayList<Method>();
        Collection<Method> arr = MethodUtil.getMethods(outLst.getClass(),
                MethodFilter.create().methodName("toArray").includeInterfaces());
        Assert.assertEquals(arr.size(), 8);
    }

    @Test
    public void testInvokeMethodStringObjectObjectArray() throws Exception {
        Object obj = new Conc();
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
        Object obj = new Conc();
        Object res = MethodUtil.invokeMethod(obj, "joke");
        Assert.assertEquals(6, res);
    }

    @Test(expected=NoSuchMethodException.class)
    public void testInvokePrivateMethodFalse() throws Exception {
        Object obj = new Conc();
        Object res = MethodUtil.invokePublicMethod(obj, "joke");
        Assert.assertEquals(6, res);
    }

}

interface Inter {
    public void find(int k);
}

interface Cool extends Inter {
    public int cool();
}

class Base implements Cool {
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

class Conc extends Base implements Inter {

    public void find(int k) {
    }

    public int find(int k, int m) {
        return k * m;
    }
}
