/**
 * WaitOnTest.java
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
package org.apache.niolex.commons.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.concurrent.CountDownLatch;

import org.apache.commons.codec.DecoderException;
import org.apache.niolex.commons.bean.One;
import org.apache.niolex.commons.util.Runner;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-7-12
 */
public class WaitOnTest {

	private CountDownLatch latch = new CountDownLatch(1);

	private WaitOn<String> waitOn;

	@Before
	public void createWaitOn() throws Exception {
		waitOn = new WaitOn<String>(latch);
	}

	/**
     * Test method for {@link org.apache.niolex.commons.concurrent.WaitOn#waitForResult(long)}.
     * @throws Exception
     */
    @Test
    public void testWaitForResultDirect() throws Exception {
        waitOn.release("Direct-Result");
        assertEquals("Direct-Result", waitOn.waitForResult(5));
    }

    /**
     * Test method for {@link org.apache.niolex.commons.concurrent.WaitOn#waitForResult(long)}.
     * @throws Exception
     */
    @Test(expected=DecoderException.class)
    public void testWaitForResultEx() throws Throwable {
        waitOn.release(new BlockerException(new DecoderException("I am here to meet you!")));

        try {
            assertEquals("Direct-Result", waitOn.waitForResult(5));
        } catch (BlockerException e) {
            throw e.getCause();
        }
    }

	/**
	 * Test method for {@link org.apache.niolex.commons.concurrent.WaitOn#waitForResult(long)}.
	 * @throws InterruptedException
	 */
	@Test
	public void testWaitForResultNormal() throws InterruptedException {
	    One<String> retVal = One.create("N/A");
        Thread w = Runner.run(retVal , waitOn, "waitForResult", 1000);
        Thread.sleep(10);
        waitOn.release("Good");
        w.join();
        assertEquals("Good", retVal.a);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.concurrent.WaitOn#waitForResult(long)}.
	 * @throws InterruptedException
	 */
	@Test
	public void testWaitForResult2() throws Throwable {
		Thread t = new Thread() {
			public void run() {
				try {
					String s = waitOn.waitForResult(200);
					System.out.println(s);
					assertFalse(true);
					assertEquals("Good", s);
				} catch (Exception e) {
					assertEquals("I am here to meet you!", e.getMessage());
				}
			}
		};
		t.start();
		Thread.sleep(10);
		waitOn.release(new BlockerException("I am here to meet you!"));
		t.join();
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.concurrent.WaitOn#waitForResult(long)}.
	 * @throws InterruptedException
	 */
	@Test
	public void testWaitForResult3() throws Throwable {
		Thread t = new Thread() {
			public void run() {
				try {
					String s = waitOn.waitForResult(200);
					System.out.println(s);
					assertFalse(true);
					assertEquals("Good", s);
				} catch (Exception e) {
					assertEquals("I am here to meet you!", e.getMessage());
				}
			}
		};
		waitOn.release(new BlockerException("I am here to meet you!"));
		t.start();
		t.join();
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.concurrent.WaitOn#waitForResult(long)}.
	 * @throws InterruptedException
	 */
	@Test
	public void testWaitForResult4() throws InterruptedException {
		Thread t = new Thread() {
			public void run() {
				try {
					String s = waitOn.waitForResult(200);
					System.out.println(s);
					assertEquals("Good", s);
				} catch (Exception e) {
					e.printStackTrace();
					assertFalse(true);
				}
			}
		};
		waitOn.release("Good");
		t.start();
		t.join();
	}

}
