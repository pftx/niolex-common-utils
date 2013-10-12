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

	public void runme() {
		c.inc();
	}

	public void runme(String str) {
	    int time = str.length();
	    while (time-- > 0)
	        c.inc();
	}

	public void runme(long time) {
		while (time-- > 0)
			c.inc();
	}

	public void runme(int time, Exception e) throws Exception {
		while (time-- > 0)
			c.inc();
		throw e;
	}

    @Test
    public void testRunMethodEx() throws Exception {
        c.set(0);
        Runner.run(this, "runme", 2, new Exception("K")).join();
        assertEquals(2, c.cnt());
    }

    @Test
    public void testRunMethod() throws Exception {
        c.set(0);
        Runner.run(this, "runme", "ab").join();
        assertEquals(2, c.cnt());
    }

    @Test
    public void testRunMethodNotFound() throws Exception {
        c.set(0);
        Runner.run(this, "run", 2, new Exception("K")).join();
        assertEquals(0, c.cnt());
    }

	/**
	 * Test method for {@link org.apache.niolex.commons.util.Runner#run(java.lang.Object, java.lang.String, java.lang.Object[])}.
	 * @throws InterruptedException
	 */
	@Test
	public void testRunObject() throws InterruptedException {
	    c.set(0);
		Runner.run(this, "runme").join();
		assertEquals(1, c.cnt());
		Runner.run(this, "runme", 3).join();
		assertEquals(4, c.cnt());
	}

}
