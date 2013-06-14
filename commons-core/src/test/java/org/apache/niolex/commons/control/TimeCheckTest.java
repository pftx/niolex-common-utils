/**
 * TimeCheckTest.java
 *
 * Copyright 2012 The original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.niolex.commons.control;

import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;

import org.apache.niolex.commons.util.Runner;
import org.apache.niolex.commons.util.SystemUtil;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2012-12-26
 */
public class TimeCheckTest {

    @Test
    public void testTimeCheck() throws Exception {
        TimeCheck tc = new TimeCheck(6, 2, 100);
        tc.lastCheckTime = System.currentTimeMillis() - 3;
        tc.counter.set(101);
        assertTrue(tc.check());
        assertFalse(tc.lastCheckStatus());
        assertEquals(1, tc.getCounter().get());
        tc.counter.set(50);
        boolean b = tc.check();
        if (b) {
            assertEquals(System.currentTimeMillis(), tc.getLastCheckTime());
        }
        assertFalse(b);
        tc.lastCheckTime = System.currentTimeMillis() - 3;
        assertTrue(tc.check());
        assertEquals(0, tc.controler.getHead());
        assertEquals(101, tc.controler.getArray()[0]);
        assertEquals(50, tc.controler.getArray()[1]);
    }

    public void sync(TimeCheck tc, CountDownLatch l) {
        synchronized (tc.counter) {
            l.countDown();
            long in = System.currentTimeMillis(), out;
            do {
                SystemUtil.sleep(1);
                out = System.currentTimeMillis();
            } while (out - in == 0);
            tc.lastCheckTime = System.currentTimeMillis();
        }
    }

    @Test
    public void testTimeCheckCover() throws Exception {
        TimeCheck tc = new TimeCheck(2, 2, 2);
        assertTrue(tc.check());
        tc.lastCheckStatus = false;
        assertFalse(tc.check());
        tc.counter.set(0);
        assertTrue(tc.check());
        assertFalse(tc.check());
        // ---
        tc.lastCheckTime = System.currentTimeMillis() - 1;
        tc.counter.set(3);
        tc.lastCheckStatus = true;
        CountDownLatch l = new CountDownLatch(1);
        Runner.run(this, "sync", tc, l);
        l.await();
        assertTrue(tc.check());
        assertTrue(tc.lastCheckStatus);
        // ---
        assertTrue(tc.lastCheckStatus());
        assertEquals(4, tc.getCounter().get());
        tc.counter.set(50);
        assertTrue(tc.check());
        assertEquals(0, tc.controler.getHead());
        assertEquals(0, tc.controler.getArray()[0]);
        assertEquals(0, tc.controler.getArray()[1]);
    }

    @Test
    public void testTimeCheckNormal() throws Exception {
        TimeCheck tc = new TimeCheck(2, 2, 2);
        assertTrue(tc.check());
        tc.lastCheckStatus = false;
        assertFalse(tc.check());
        tc.counter.set(0);
        assertTrue(tc.check());
        assertFalse(tc.check());
        // ---
        tc.lastCheckTime = System.currentTimeMillis() - 1;
        tc.counter.set(3);
        tc.lastCheckStatus = true;
        assertTrue(tc.check());
        assertFalse(tc.lastCheckStatus);
        // ---
        assertFalse(tc.lastCheckStatus());
        assertEquals(1, tc.getCounter().get());
        tc.counter.set(50);
        assertFalse(tc.check());
        assertEquals(1, tc.controler.getHead());
        assertEquals(3, tc.controler.getArray()[0]);
        assertEquals(0, tc.controler.getArray()[1]);
    }

}
