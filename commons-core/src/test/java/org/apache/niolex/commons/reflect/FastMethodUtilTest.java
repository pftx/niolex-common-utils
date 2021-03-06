/**
 * FastMethodUtilTest.java
 *
 * Copyright 2012 Niolex, Inc.
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

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.esotericsoftware.reflectasm.MethodAccess;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-7-23
 */
public class FastMethodUtilTest extends FastMethodUtil {

	/**
	 * Test method for {@link org.apache.niolex.commons.reflect.FastMethodUtil#getMethods(java.lang.Class)}.
	 */
	@Test
	public void testGetMethods() {
		String[] arr = FastMethodUtil.getMethods(MethodTestBean.class);
		System.out.println("MethodTestBean methods => " + Arrays.toString(arr));
        Assert.assertTrue(arr.length >= 5);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.reflect.FastMethodUtil#getMethodAccess(java.lang.Class)}.
	 */
	@Test
	public void testGetMethodAccess() {
		MethodAccess ac = FastMethodUtil.getMethodAccess(MethodTestBean.class);
		MethodTestBean host = new MethodTestBean("niolex-common-utils");
		String name = (String)ac.invoke(host, "echoName");
		assertEquals(name, "niolex-common-utils");
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.reflect.FastMethodUtil#invokeMethod(java.lang.String, java.lang.Object, java.lang.Object[])}.
	 */
	@Test
	public void testInvokeMethod() {
		MethodTestBean host = new MethodTestBean("niolex-common-utils");
        Object ret = FastMethodUtil.invokeMethod(host, "echoName", "Xie, Jiyun");
        Assert.assertEquals(ret, "Xie, Jiyun");
        ret = FastMethodUtil.invokeMethod(host, "echoName");
        Assert.assertEquals(ret, "niolex-common-utils");
	}


    /**
     * Test method for {@link org.apache.niolex.commons.reflect.FastMethodUtil#invokeMethod(java.lang.String, java.lang.Object, java.lang.Object[])}.
     * @throws Exception
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    @Test
    public void testInvokeMethodMore() throws NoSuchMethodException, IllegalAccessException, Exception {
        MethodTestBean host = new MethodTestBean("Lex");
        Object ret = FastMethodUtil.invokeMethod(host, "echoName", (Object[]) null);
        Assert.assertEquals(ret, "Lex");
    }
    
    @Test
    public void testSpeed() throws Exception {
        long in, out, t1, t2;
        FastMethodUtil.getMethodAccess(MethodTestBean.class);
        MethodTestBean host = new MethodTestBean("Lex");
        
        in = System.nanoTime();
        for (int i = 0; i < 1000; ++i) {
            String name = (String)FastMethodUtil.invokeMethod(host, "echoName", "Walley");
            assertEquals("Walley", name);
        }
        out = System.nanoTime();
        t1 = out - in;
        
        in = System.nanoTime();
        for (int i = 0; i < 1000; ++i) {
            String name = (String)MethodUtil.invokeMethod(host, "echoName", "Kiddy");
            assertEquals("Kiddy", name);
        }
        out = System.nanoTime();
        t2 = out - in;
        
        System.out.println("GetFieldValue t1 = " + t1 + ", t2 = " + t2 + ", 加速比(t1/t2) = " + ((t1 * 1.0 / t2) * 100) + "%");
    }
}
