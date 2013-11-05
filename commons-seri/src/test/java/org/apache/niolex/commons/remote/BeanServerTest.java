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

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.test.AnnotationOrderedRunner;
import org.apache.niolex.commons.test.Benchmark;
import org.apache.niolex.commons.test.MockUtil;
import org.apache.niolex.commons.test.SystemInfo;
import org.apache.niolex.commons.test.Benchmark.Bean;
import org.apache.niolex.commons.test.Benchmark.Group;
import org.apache.niolex.commons.util.Runme;
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

	class B implements Invokable {

		private String msg = "Please invoke me!";

		/**
		 * Override super method
		 * @throws IOException
		 * @see org.apache.niolex.commons.remote.Invokable#invoke()
		 */
		@Override
		public void invoke(OutputStream out, String[] args) throws IOException {
			System.out.println("I am invoked.");
			out.write(StringUtil.strToAsciiByte("I am invoked." + ConnectionWorker.endl()));
		}

		public String getMsg() {
			return msg;
		}

	}

	class A {
		int[] ids = new int[] {1, 2, 3, 4, 5};
		String[] names = new String[] {"Adam", "Shalve", "Bob"};
		Group group = Group.makeGroup();
		Integer i = new Integer(128);
		final Boolean b = Boolean.FALSE;
		Byte by = new Byte((byte) 3);
		Map<Integer, String> map = new HashMap<Integer, String>();
		Map<String, String> smap = new HashMap<String, String>();
		Map<String, Object> bmap = new HashMap<String, Object>();
		Map<Object, Object> imap = new HashMap<Object, Object>();
		Set<String> set = new HashSet<String>();

		public A() {
			map.put(1, "Good");
			smap.put("test", "but");
			smap.put("this.[is].good", "See You!");
			bmap.put("b", new Bean(3, "Bean", 12212, new Date()));
			bmap.put("c", Benchmark.makeBenchmark());
			bmap.put("invoke", new B());
			bmap.put("os", new OSInfo());
			imap.put(new Date(), new Bean(3, "Bean", 12212, new Date()));
			set.add("Goog Morning");
			set.add("This is Good");
			set.add("中文");
		}
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.remote.BeanServer#putIfAbsent(java.lang.String, java.lang.Object)}.
	 */
	@Test
	public void testPutIfAbsent() {
		Object o = beanS.putIfAbsent("fail2", "Not yet implemented");
		assertNull(o);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.remote.BeanServer#remove(java.lang.Object)}.
	 */
	@Test
	public void testRemove() {
		beanS.remove("fail");
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.remote.BeanServer#replace(java.lang.String, java.lang.Object, java.lang.Object)}.
	 */
	@Test
	public void testReplace() {
		beanS.replace("fail", "Not yet implemented", "New value");
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.remote.BeanServer#start()}.
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		ConnectionWorker.setAuthInfo("abcD");
		BeanServerTest test = new BeanServerTest();
		test.beanS.putIfAbsent("bench", Benchmark.makeBenchmark());
		test.beanS.putIfAbsent("group", test.new A());
		test.beanS.putIfAbsent("system", SystemInfo.getInstance());
		final Monitor m = new Monitor(10);
		test.beanS.putIfAbsent("cdc", m);
		Runme rme = new Runme() {

			@Override
			public void runMe() {
				m.addValue("test.me", MockUtil.randInt(200));
			}

		};
		rme.setSleepInterval(1000);
		rme.start();
		test.beanS.start();
		Thread.sleep(3000000);
		test.beanS.stop();
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
        try {
            so.connect(new InetSocketAddress("localhost", 8373), 1000);
        } finally {
            byte[] b = new byte[200];
            int k = so.getInputStream().read(b);
            String s = new String(b, 0, k, StringUtil.US_ASCII);
            System.out.println("x get result: " + s);
            assertEquals("Too many connections.\n", s);
        }
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
