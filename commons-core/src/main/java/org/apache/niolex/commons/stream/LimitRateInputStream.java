/**
 * LimitRateInputStream.java
 *
 * Copyright 2011 Niolex, Inc.
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
package org.apache.niolex.commons.stream;

import java.io.IOException;
import java.io.InputStream;

import org.apache.niolex.commons.util.SystemUtil;

/**
 * Restrict the input rate.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2011-6-2$
 */
public class LimitRateInputStream extends InputStream {

    /**
     * We check for input rate when we received every 10K.
     */
    private static final int CHECK_SIZE_THRESHOLD = 10240;

    /**
     * We check for time in nanoseconds.
     */
    private static final int MILLS_INTO_NANO_SECOND = 1000000;

    /**
     * We keep a round in one hour.
     */
    private static final int HOUR_INTO_NANO_SECOND = MILLS_INTO_NANO_SECOND * 3600000;

    // ========================================================================
    // INSTANCE FIELDS.
    // ========================================================================

    private final InputStream delegate;
    // The rate of 1 Byte/Nanosecond.
    private final double expectedRate;

    private long startedTime = 0;
    private long cnt = 0;
    private long chunk = 0;

    /**
     * Create a LimitRateInputStream with the input rate limited to 20 MB/seconds.
     *
     * @param delegate the input stream to delegate read to
     */
    public LimitRateInputStream(InputStream delegate) {
        this(delegate, 20);
    }

    /**
     * Create a LimitRateInputStream with the given input rate limit.
     *
     * @param delegate the input stream to delegate read to
     * @param rate the maximum input rate, in MB/s
     */
    public LimitRateInputStream(InputStream delegate, double rate) {
        super();
        this.delegate = delegate;
        this.expectedRate = (rate * 1024 * 1024) / 1000000000L;
        reset(0);
    }

    /**
     * Reset the internal rate calculation variables.
     *
     * @param offset the offset left from last round
     */
    private void reset(int offset) {
        startedTime = System.nanoTime();
        cnt = offset;
        chunk = CHECK_SIZE_THRESHOLD;
    }

    /**
     * This is the core method to restrict the input rate.
     *
     * @param size the current read size
     */
    private void checkSize(int size) {
        cnt += size;
        if (cnt > chunk) {
            long usedTime = System.nanoTime() - startedTime;
            long needTime = (long) (cnt / expectedRate);
            long sleepTime = needTime - usedTime + 300000;
            if (sleepTime > MILLS_INTO_NANO_SECOND) {
                SystemUtil.sleep(sleepTime / MILLS_INTO_NANO_SECOND + 1);
            } else if (sleepTime > 0) {
                Thread.yield();
            }
            if (usedTime > HOUR_INTO_NANO_SECOND) {
                reset(0);
            } else {
                chunk += CHECK_SIZE_THRESHOLD;
            }
        }
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int size = delegate.read(b, off, len);
        checkSize(size);
        return size;
    }

    @Override
    public int read(byte[] b) throws IOException {
        int size = delegate.read(b);
        checkSize(size);
        return size;
    }

    @Override
    public int read() throws IOException {
        checkSize(1);
        return delegate.read();
    }

    public int available() throws IOException {
        return delegate.available();
    }

    public void close() throws IOException {
        delegate.close();
    }

    public boolean equals(Object obj) {
        return delegate.equals(obj);
    }

    public int hashCode() {
        return delegate.hashCode();
    }

    public void mark(int readlimit) {
        delegate.mark(readlimit);
    }

    public boolean markSupported() {
        return delegate.markSupported();
    }

    public void reset() throws IOException {
        delegate.reset();
    }

    public long skip(long n) throws IOException {
        return delegate.skip(n);
    }

    public String toString() {
        return delegate.toString();
    }

}
