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
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * 
 * @version 1.0.0, $Date: 2011-6-2$
 * 
 */
public class LimitRateInputStream extends InputStream {

    private static final Logger log = LoggerFactory.getLogger(LimitRateInputStream.class);
    private static final int CHECK_SIZE_THRESHOLD = 1048576;
    private final InputStream delegate;
    private final long expect;

    private long start = System.currentTimeMillis();
    private long cnt = 0;

    /**
     * @param rate
     *            MB/s
     * @param delegate
     */
    public LimitRateInputStream(InputStream delegate, double rate) {
        super();
        this.delegate = delegate;
        this.expect = (long) (CHECK_SIZE_THRESHOLD * 1000 / (rate * 1024 * 1024));
    }

    /**
     * @param delegate
     */
    public LimitRateInputStream(InputStream delegate) {
        super();
        this.delegate = delegate;
        this.expect = 20;
    }

    private void checkSize(int size) {
        cnt += size;
        if (cnt > CHECK_SIZE_THRESHOLD) {
            start = System.currentTimeMillis() - start;
            if (start < expect - 3) {
                try {
                    Thread.sleep(expect - start - 1, 888);
                    System.out.println("Sleep: " + start + " " + expect);
                } catch (Exception e) {
                    log.info("Exception while sleep - ", e.getMessage());
                }
            }
            start = System.currentTimeMillis();
            cnt = 0;
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
