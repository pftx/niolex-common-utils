/**
 * MethodFilterTest.java
 *
 * Copyright 2014 the original author or authors.
 *
 * We licenses this file to you under the Apache License, version 2.0
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


import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-1-6
 */
public class MethodFilterTest {

    private Method m = MethodUtil.getMethod(getClass(), "testCreate");

    @Test
    public void testCreate() throws Exception {
        MethodFilter filter = MethodFilter.create().includeAll();
        assertTrue(filter.isValid(m));
    }

    @Test
    public void testIsIncludeInterfaces() throws Exception {
        MethodFilter filter = MethodFilter.create().methodName("create");
        assertFalse(filter.isValid(m));
    }

    @Test
    public void testIsIncludeSuper() throws Exception {
        MethodFilter filter = MethodFilter.create().n("testCreate").includeStatic();
        assertTrue(filter.isValid(m));
    }

    @Test
    public void testIsValid() throws Exception {
        MethodFilter filter = MethodFilter.create().methodName("testCreate").returnType(int.class);
        assertFalse(filter.isValid(m));
    }

    @Test
    public void testIncludeAll() throws Exception {
        MethodFilter filter = MethodFilter.create().methodName("testCreate").r(void.class).includeSynthetic();
        assertTrue(filter.isValid(m));
    }

    @Test
    public void testIncludeInterfaces() throws Exception {
        MethodFilter filter = MethodFilter.create().methodName("testCreate").returnType(void.class).p(new Class<?>[] {int.class});
        assertFalse(filter.isValid(m));
    }

    @Test
    public void testIncludeSuper() throws Exception {
        MethodFilter filter = MethodFilter.create().methodName("testCreate").returnType(void.class).parameterTypes(new Class<?>[0]);
        assertTrue(filter.isValid(m));
    }

    @Test
    public void testIncludeStatic() throws Exception {
        Method m = MethodUtil.getMethod(Jenny.class, "get");
        MethodFilter filter = MethodFilter.create();
        assertFalse(filter.isValid(m));
    }

    @Test
    public void testIncludeSynthetic() throws Exception {
        Method m = MethodUtil.getMethod(Jenny.class, "get");
        MethodFilter filter = MethodFilter.c().includeStatic();
        assertTrue(filter.isValid(m));
    }

    @Test
    public void testIncludeAbstract() throws Exception {
        Method m = MethodUtil.getMethod(EchoClass.class, "echoClass");
        MethodFilter filter = MethodFilter.create().includeSuper();
        assertFalse(filter.isValid(m));
    }

    @Test
    public void testMethodName() throws Exception {
        Method m = MethodUtil.getMethod(EchoClass.class, "echoClass");
        MethodFilter filter = MethodFilter.create().includeInterfaces().includeAbstract();
        assertTrue(filter.isValid(m));
    }

    private class Inner {
        public String getName() {
            return m.getName();
        }
    }

    @Test
    public void testReturnType() throws Exception {
        Class<?> clazz = getClass();
        MethodFilter filter = MethodFilter.create().includeStatic();
        List<Method> l = MethodUtil.getMethods(clazz, filter);
        assertEquals(13, l.size());
    }

    @Test
    public void testParameterTypes() throws Exception {
        System.out.println(new Inner().getName());
    }

}
