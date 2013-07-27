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


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.niolex.commons.concurrent.ThreadUtil;
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

    @Test(expected=IOException.class)
    public void testWriteAndClose() throws Exception {
        OutputStream out = mock(OutputStream.class);
        doThrow(new IOException("This")).when(out).write(null);
        writeAndClose(out, null);
    }

    @Test(expected=IOException.class)
    public void testTransferAndClose() throws Exception {
        InputStream in = mock(InputStream.class);
        doThrow(new IOException("Mock")).when(in).read(any(byte[].class));
        transferAndClose(in, mock(OutputStream.class), 1024);
    }

    public void readTest(int k) {
        readCnt += k;
        ThreadUtil.sleep(50);
    }

    public void writeTest(int k) {
        writeCnt += k;
        ThreadUtil.sleep(50);
    }

    private Method r = MethodUtil.getMethods(getClass(), "readTest")[0];
    private Method w = MethodUtil.getMethods(getClass(), "writeTest")[0];
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

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
        Runner.run(this, "letsRead", 3);
        Runner.run(this, "letsRead", 4);
        ThreadUtil.sleep(5);
        Runner.run(this, "letsWrite", 5);
        ThreadUtil.sleep(5);
        assertEquals(7, readCnt);
        assertEquals(0, writeCnt);
    }

    @Test
    public void testUseWriteLock() throws Exception {
        Runner.run(this, "letsWrite", 2);
        ThreadUtil.sleep(5);
        Runner.run(this, "letsRead", 3);
        Runner.run(this, "letsRead", 4);
        Runner.run(this, "letsWrite", 5);
        ThreadUtil.sleep(5);
        assertEquals(0, readCnt);
        assertEquals(2, writeCnt);
    }

}
