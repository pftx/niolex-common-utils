/**
 * FinallyTest.java
 *
 * Copyright 2013 the original author or authors.
 *
 * We licenses this file to you under the Apache License, version 2.0
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
package org.apache.niolex.commons.internal;


import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.niolex.commons.concurrent.Blocker;
import org.apache.niolex.commons.concurrent.ThreadUtil;
import org.apache.niolex.commons.concurrent.WaitOn;
import org.apache.niolex.commons.reflect.MethodUtil;
import org.apache.niolex.commons.util.Runner;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-6-26
 */
public class FinallyTest extends Finally {

    private int readCnt = 0;
    private int writeCnt = 0;

    public void readTest(int k) {
        readCnt += k;
        blocker.release("r", "1");
        ThreadUtil.sleep(80);
    }

    public void writeTest(int k) {
        writeCnt += k;
        blocker.release("w", "1");
        ThreadUtil.sleep(80);
    }

    private Method r = MethodUtil.getMethod(getClass(), "readTest", int.class);
    private Method w = MethodUtil.getMethod(getClass(), "writeTest", int.class);
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private Blocker<String> blocker = new Blocker<String>();

    public void letsRead(int k) throws Throwable {
        useReadLock(lock, this, r, new Object[] {k});
    }

    public void letsWrite(int k) throws Throwable {
        useWriteLock(lock, this, w, new Object[] {k});
    }

    @Before
    public void before() {
        readCnt = 0;
        writeCnt = 0;
    }

    @Test
    public void testUseReadLock() throws Exception {
        WaitOn<String> wait = blocker.init("r");
        Runner.run(this, "letsRead", 3);
        Runner.run(this, "letsRead", 4);
        wait.waitForResult(150);
        ThreadUtil.sleep(5);
        Runner.run(this, "letsWrite", 5);
        ThreadUtil.sleep(5);
        assertEquals(7, readCnt);
        assertEquals(0, writeCnt);
    }

    @Test
    public void testUseWriteLock() throws Exception {
        WaitOn<String> wait = blocker.init("w");
        Runner.run(this, "letsWrite", 2);
        wait.waitForResult(50);
        Runner.run(this, "letsRead", 3);
        Runner.run(this, "letsRead", 4);
        Runner.run(this, "letsWrite", 5);
        ThreadUtil.sleep(5);
        assertEquals(0, readCnt);
        assertEquals(2, writeCnt);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testErrReadLock() throws Throwable {
        useReadLock(lock, this, r, new Object[0]);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testErrWriteLock() throws Throwable {
        useWriteLock(lock, this, w, new Object[0]);
    }

}
