/**
 * TimeControlerTest.java
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

import org.apache.niolex.commons.concurrent.ThreadUtil;
import org.apache.niolex.commons.util.SystemUtil;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2012-12-26
 */
public class TimeControlerTest {

    /**
     * Test method for {@link org.apache.niolex.commons.control.TimeControler#initTimeCheck(java.lang.String, int, int, int)}.
     */
    @Test
    public void testInitTimeCheck() {
        TimeControler tc = new TimeControler();
        tc.initTimeCheck("abc", 2, 2, 20);
        for (int i = 0; i < 21; ++i) {
            assertTrue(tc.check("abc"));
        }
        long in = System.currentTimeMillis(), out;
        do {
            SystemUtil.sleep(1);
            out = System.currentTimeMillis();
        } while (out - in == 0);
        System.out.println(out - in);
        for (int i = 0; i < 10; ++i) {
            assertTrue(tc.check("abc"));
        }
        assertFalse(tc.check("abc"));
        assertFalse(tc.check("abc"));
    }

    /**
     * Test method for {@link org.apache.niolex.commons.control.TimeControler#initTimeCheck(java.lang.String, int, int, int)}.
     */
    @Test(expected=IllegalStateException.class)
    public void testInitTimeCheckException() {
        TimeControler tc = new TimeControler();
        assertTrue(tc.check("abcd"));
    }

    /**
     * Test method for {@link org.apache.niolex.commons.control.TimeControler#check(java.lang.String)}.
     */
    @Test
    public void testCheck() {
        TimeControler tc = new TimeControler();
        tc.initTimeCheck("abc", 2, 2, 100);
        for (int i = 0; i < 20; ++i) {
            assertTrue(tc.check("abc"));
        }
        ThreadUtil.sleepAtLeast(1);
        for (int i = 0; i < 100; ++i) {
            assertTrue(tc.check("abc"));
        }
        assertTrue(tc.check("abc"));
    }

}
