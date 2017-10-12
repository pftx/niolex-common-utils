/**
 * ReflectTest.java
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

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-7-4
 */
public class ReflectTest {

    /**
     * @throws Exception
     */
    @Test
    public void testReal() throws Exception {
        Method m = MethodTestBean.class.getDeclaredMethod("echoName", int.class);
        System.out.println(m);
    }

    /**
     * Auto boxing failed.
     *
     * @throws Exception
     */
    @Test(expected=NoSuchMethodException.class)
    public void testAutoBox() throws Exception {
        Method m = MethodTestBean.class.getDeclaredMethod("echoName", Integer.class);
        System.out.println(m);
    }

    /**
     * Auto boxing OK.
     *
     * @throws Exception
     */
    @Test
    public void testMyAutoBox() throws Exception {
        List<Method> list = MethodUtil.getMethodsParamRelax(MethodTestBean.class, "echoName", Integer.class);
        assertEquals(1, list.size());
        System.out.println(list.get(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testStatic() throws Exception {
        Method m = Jenny.class.getDeclaredMethod("set", int.class);
        System.out.println(m);
    }

    /**
     * Auto boxing OK.
     *
     * @throws Exception
     */
    @Test
    public void testMyStaticAutoBox() throws Exception {
        List<Method> list = MethodUtil.getMethodsParamRelax(Jenny.class, "set", Short.class);
        assertEquals(1, list.size());
        System.out.println(list.get(0));
    }

    /**
     * Auto boxing failed.
     *
     * @throws Exception
     */
    @Test(expected=NoSuchMethodException.class)
    public void testStaticAutoBox() throws Exception {
        Method m = Jenny.class.getDeclaredMethod("set", Integer.class);
        System.out.println(m);
    }

}
