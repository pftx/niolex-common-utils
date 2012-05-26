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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Restrict the input rate.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 *
 * @version 1.0.0, $Date: 2011-6-2$
 *
 */
public class LimitRateInputStream extends InputStream {

    private static final Logger LOG = LoggerFactory.getLogger(LimitRateInputStream.class);
    // We check when we received every 1K for rate.
    private static final int CHECK_SIZE_THRESHOLD = 10240;
    private final InputStream delegate;
    // The rate of 1 Byte/Nanosecond.
    private final double expectedRate;

    private final long startedTime;
    private long cnt = 0;
    private long chunk = CHECK_SIZE_THRESHOLD;

    /**
     * Create a LimitRateInputStream with the given rate
     * @param delegate
     * @param rate
     *            MB/s
     */
    public LimitRateInputStream(InputStream delegate, double rate) {
        super();
        this.delegate = delegate;
        this.expectedRate = (rate * 1024 * 1024) / 1000000000L;
        startedTime = System.nanoTime();
    }

    /**
     * Create a LimitRateInputStream with the rate of 20 MB/seconds
     * @param delegate
     */
    public LimitRateInputStream(InputStream delegate) {
        this(delegate, 20);
    }

    /**
     * This is the core method to restrict the rate.
     * @param size
     */
    private void checkSize(int size) {
        cnt += size;
        if (cnt > chunk) {
            long usedTime = System.nanoTime() - startedTime;
            long needTime = (long) (cnt / expectedRate);
            if (usedTime < needTime - 300000) {
                try {
                    Thread.sleep((needTime - usedTime) / 1000000 + 1);
                } catch (Exception e) {
                    LOG.info("Exception while sleep - ", e.getMessage());
                }
            }
            chunk += CHECK_SIZE_THRESHOLD;
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
