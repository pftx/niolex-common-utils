/**
 * MethodUtilTest.java
 *
 * Copyright 2011 Baidu, Inc.
 *
 * Baidu licenses this file to you under the Apache License, version 2.0
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

import junit.framework.Assert;

import org.apache.niolex.commons.reflect.MethodUtil;
import org.junit.Test;


class MethodTestBean {
    private String strName;
    
    public MethodTestBean(String strName) {
        super();
        this.strName = strName;
    }

    public String echoName() {
        System.out.println("My Name IS " + strName + ", Welcome to use MethodUtil!");
        return strName;
    }
    
    public String echoName(String strName) {
        System.out.println("My Name IS " + strName + ", Welcome to use MethodUtil!");
        return strName;
    }
    
    public String echoName(int intAge) {
        System.out.println("My Name IS " + strName + ", my age is " + intAge +", Welcome to use MethodUtil!");
        return strName;
    }
    
    public String echoName(String strName, int intAge) {
        System.out.println("My Name IS " + strName + ", my age is " + intAge +", Welcome to use MethodUtil!");
        return strName;
    }
    
    public String echoClass() {
        System.out.println("Welcome to use MethodTestBean!");
        return "MethodTestBean";
    }
}
public class MethodUtilTest {
    
    @Test
    public void testMethods() throws Throwable {
        Method[] arr = MethodUtil.getMethods(MethodTestBean.class);
        Assert.assertEquals(arr.length, 5);
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
        
        MethodTestBean host = new MethodTestBean("Veyron Code");
        Object ret = MethodUtil.invokeMethod(m, host, "Xie, Jiyun");
        Assert.assertEquals(ret, "Xie, Jiyun");
        
        m = MethodUtil.getMethod(MethodTestBean.class, "echoName");
        ret = MethodUtil.invokeMethod(m, host);
        Assert.assertEquals(ret, "Veyron Code");
    }
}
