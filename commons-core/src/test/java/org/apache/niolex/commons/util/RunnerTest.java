/**
 * RunnerTest.java
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
package org.apache.niolex.commons.util;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.naming.NamingException;

import org.apache.niolex.commons.bean.One;
import org.apache.niolex.commons.reflect.ItemNotFoundException;
import org.apache.niolex.commons.reflect.MethodUtil;
import org.apache.niolex.commons.test.Counter;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-7-12
 */
public class RunnerTest extends Runner implements Runnable {
	private Counter c = new Counter();

	/**
	 * Override super method
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
	    c.inc();c.inc();c.inc();
	}

    /**
     * Test method for {@link org.apache.niolex.commons.util.Runner#run(java.lang.Runnable)}.
     * @throws InterruptedException
     */
    @Test
    public void testRunRunnable() throws InterruptedException {
        c.set(0);
        Runner.run(this).join();
        assertEquals(3, c.cnt());
    }

	public int runme() {
		c.inc();
		return 1;
	}

	public void runme(String str) {
	    int time = str.length();
	    while (time-- > 0)
	        c.inc();
	}

	public int runme(long time) {
		while (time-- > 0)
			c.inc();
		return 2;
	}

	public void runme(int time, Exception e) throws Exception {
		while (time-- > 0)
			c.inc();
		throw e;
	}

	Method runme = MethodUtil.getMethod(getClass(), "runme", String.class);
	Method runmeEx = MethodUtil.getMethod(getClass(), "runme", int.class, Exception.class);

	@Test
	public void testRunMethodName() throws Exception {
	    c.set(0);
	    Runner.run(this, "runme", "ab").join();
	    assertEquals(2, c.cnt());
	}

	@Test
	public void testRunMethodInstance() throws Exception {
	    c.set(0);
	    Runner.run(this, runme, "abcd").join();
	    assertEquals(4, c.cnt());
	}

    @Test(expected=NamingException.class)
    public void testRunMethodEx() throws Throwable {
        c.set(0);
        One<Thread> one = new One<Thread>();
        Future<Object> future = Runner.run(one, this, "runme", 6, new NamingException("K"));
        try {
            future.get();
        } catch (ExecutionException e) {
            assertEquals(6, c.cnt());
            throw e.getCause();
        }
        assertNull(one.a);
    }

    @Test(expected=NamingException.class)
    public void testRunMethodInstanceEx() throws Throwable {
        c.set(0);
        One<Thread> one = new One<Thread>();
        Future<Object> future = Runner.run(one, this, runmeEx, 6, new NamingException("K"));
        try {
            future.get();
        } catch (ExecutionException e) {
            assertEquals(6, c.cnt());
            throw e.getCause();
        }
        assertNull(one.a);
    }

    @Test(expected=ItemNotFoundException.class)
    public void testRunMethodNotFound() throws Throwable {
        c.set(0);
        One<Thread> one = new One<Thread>();
        Future<Object> future = Runner.run(one, this, "run", 2, new Exception("K"));
        try {
            future.get();
        } catch (ExecutionException e) {
            assertEquals(0, c.cnt());
            throw e.getCause();
        } finally {
            one.a.join();
        }
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRunMethodInvArgEx() throws Throwable {
        c.set(0);
        One<Thread> one = new One<Thread>();
        Future<Object> future = Runner.run(one, this, runme, 2, new Exception("K"));
        try {
            future.get();
        } catch (ExecutionException e) {
            assertEquals(0, c.cnt());
            throw e.getCause();
        } finally {
            one.a.join();
        }
    }

	/**
	 * Test method for {@link org.apache.niolex.commons.util.Runner#run(java.lang.Object, java.lang.String, java.lang.Object[])}.
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@Test
	public void testRunObject() throws InterruptedException, ExecutionException {
	    c.set(0);
	    One<Thread> one = new One<Thread>();
		Future<Integer> future = Runner.run(one, this, "runme");
		assertEquals(1, future.get().intValue());
		assertEquals(1, c.cnt());
		future = Runner.run(one, this, "runme", 3);
		assertEquals(2, future.get().intValue());
		assertEquals(4, c.cnt());
	}

}
