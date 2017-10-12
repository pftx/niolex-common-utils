/**
 * BlockThread.java
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
package org.apache.niolex.commons.concurrent;

import java.util.concurrent.CountDownLatch;

/**
 * This thread class extends the standard Thread and provide block functionality.
 * When user call {@link #start()}, we use a count down latch to wait until the
 * thread is running and then return.
 * We have two kinds of use cases:
 * <pre>
 * 1. Make sure thread already running:
 *      Extends this class and override the method {@link #run0()}
 * 2. Run this thread and sleep some time:
 *      Use constructor {@link #BlockThread(String, int)} and call {@link #start()}
 * </pre>
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-12-5
 */
public class BlockThread extends Thread {

    private final CountDownLatch latch = new CountDownLatch(1);
    private final int sleepTime;

    /**
     * Default Constructor.
     */
    public BlockThread() {
        super();
        sleepTime = 0;
    }

    /**
     * Construct block thread with this thread name.
     *
     * @param name the thread name
     */
    public BlockThread(String name) {
        super(name);
        sleepTime = 0;
    }

    /**
     * Construct block thread with this thread name and sleep time.
     *
     * @param name the thread name
     * @param sleepTime the time to sleep before run in milliseconds
     */
    public BlockThread(String name, int sleepTime) {
        super(name);
        this.sleepTime = sleepTime;
    }

    /**
     * We will wait until this thread is running and then return.
     *
     * @see java.lang.Thread#start()
     */
    @Override
    public synchronized void start() {
        super.start();
        ThreadUtil.waitFor(latch);
    }

    /**
     * This is the override of super method.
     * @see java.lang.Thread#run()
     */
    @Override
    public final void run() {
        latch.countDown();
        if (sleepTime > 0) {
            ThreadUtil.sleep(sleepTime);
        }
        run0();
    }

    /**
     * User need to override this method instead of {@link #run()}
     */
    public void run0() {
        super.run();
    }

}
