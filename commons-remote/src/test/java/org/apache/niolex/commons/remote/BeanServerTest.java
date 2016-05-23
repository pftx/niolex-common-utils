/**
 * BeanServerTest.java
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
package org.apache.niolex.commons.remote;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.test.AnnotationOrderedRunner;
import org.apache.niolex.commons.util.SystemUtil;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-7-25
 */
@RunWith(AnnotationOrderedRunner.class)
public class BeanServerTest {
	BeanServer beanS = new BeanServer();

	/**
	 * Test method for {@link org.apache.niolex.commons.remote.BeanServer#putIfAbsent(String, Object)}.
	 */
	@Test
	public void testPutIfAbsent() {
		Object o = beanS.putIfAbsent("fail2", "Not yet implemented");
		assertNull(o);
		o = beanS.putIfAbsent("fail2", "Not this.");
		assertEquals("Not yet implemented", o);
		Object s = beanS.remove("fail2");
		assertEquals("Not yet implemented", s);
		o = beanS.remove("fail2");
		assertNull(o);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.remote.BeanServer#remove(java.lang.Object)}.
	 */
	@Test
	public void testRemove() {
	    Object o = beanS.remove("fail");
	    assertNull(o);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.remote.BeanServer#replace(String, Object, Object)}.
	 */
	@Test
	public void testReplace() {
		assertFalse(beanS.replace("fail", "Not yet implemented", "New value"));
		Object o = beanS.putIfAbsent("fail", "Not yet implemented");
        assertNull(o);
		assertTrue(beanS.replace("fail", "Not yet implemented", "New value"));
		Object s = beanS.remove("fail");
		assertEquals("New value", s);
	}

    @Test
    public void testStartError() throws Exception {
        beanS.setPort(18373);
        assertTrue(beanS.start());
        assertFalse(beanS.start());
        beanS.stop();
    }
	/**
	 * Test method for {@link org.apache.niolex.commons.remote.BeanServer#setPort(int)}.
	 */
	@Test
	public void testSetPort() {
		beanS.setPort(1234);
	}

    @Test
    @AnnotationOrderedRunner.Order(1)
    public void testStart() throws Exception {
        beanS.setPort(8373);
        beanS.start();
    }

    @Test
    @AnnotationOrderedRunner.Order(2)
    public void testRun() throws Exception {
        Socket[] socArr = new Socket[10];
        for (int i = 0; i < 10; ++i) {
            Socket so = new Socket();
            so.connect(new InetSocketAddress("localhost", 8373), 1000);
            socArr[i] = so;
        }
        Socket so = new Socket();
        so.connect(new InetSocketAddress("localhost", 8373), 1000);
        byte[] b = new byte[200];
        int k = so.getInputStream().read(b);
        String s = new String(b, 0, k, StringUtil.US_ASCII);
        System.out.println("x get result: " + s);
        assertEquals("Too many connections.\n", s);
        SystemUtil.close(so);
        
        for (int i = 0; i < 10; ++i) {
            SystemUtil.close(socArr[i]);
        }
    }

    @Test
    @AnnotationOrderedRunner.Order(3)
    public void testStop() throws Exception {
        beanS.stop();
    }

}
