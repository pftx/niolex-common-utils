/**
 * RpcServiceHandlerTest.java
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
package org.apache.niolex.commons.rpc;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.niolex.commons.rpc.RpcServiceHandler;
import org.junit.Test;


/**
 * @author @author@ (@author-email@)
 * 
 * @version @version@, $Date: 2011-9-15$
 * 
 */
public class RpcServiceHandlerTest {
    protected static List<RpcServiceHandler> listHandlers = new ArrayList<RpcServiceHandler>();
    
    static {
        listHandlers.add(new RpcServiceHandler("5", new A("5"), 15, true));
        listHandlers.add(new RpcServiceHandler("6", new A("6"), 16, true));
        listHandlers.add(new RpcServiceHandler("7", new A("7"), 17, true));
        listHandlers.add(new RpcServiceHandler("8", new A("8"), 5000, true));
    }

    @Test
    public void testName() {
        Assert.assertEquals("5", listHandlers.get(0).getServiceUrl());
    }
    
    @Test
    public void testReady() {
        RpcServiceHandler a =listHandlers.get(0);
        Assert.assertTrue(a.isReady());
        a.notReady(new IOException("Failed to connect when server initialize."));
        Assert.assertFalse(a.isReady());
        try {
            Thread.sleep(15);
        } catch (Throwable t) {}
        Assert.assertTrue(a.isReady());
    }
    
}

class A implements InvocationHandler {
    private final String name;
    
    public A(String name) {
        super();
        this.name = name;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Call invoke for: " + name);
        return name;
    }
    
}
