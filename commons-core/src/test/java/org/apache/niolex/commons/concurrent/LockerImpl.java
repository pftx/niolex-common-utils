/**
 * LockerImpl.java
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
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-8-22
 */
public class LockerImpl implements Locker {

    private AtomicInteger readCnt = new AtomicInteger(0);
    private int writeCnt = 0;

    private final CountDownLatch readLatch;
    private final CountDownLatch writeLatch;
    private final CountDownLatch anoReadLatch;
    private final CountDownLatch anoWriteLatch;

    /**
     * Constructor
     * @param readLatch
     * @param writeLatch
     * @param anoReadLatch
     * @param anoWriteLatch
     */
    public LockerImpl(CountDownLatch readLatch, CountDownLatch writeLatch, CountDownLatch anoReadLatch,
            CountDownLatch anoWriteLatch) {
        super();
        this.readLatch = readLatch;
        this.writeLatch = writeLatch;
        this.anoReadLatch = anoReadLatch;
        this.anoWriteLatch = anoWriteLatch;
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.concurrent.Locker#read(int)
     */
    @Override
    public void read(int k) {
        readCnt.addAndGet(k);
        readLatch.countDown();
        try {
            writeLatch.await();
        } catch (InterruptedException e) {
        }
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.concurrent.Locker#write(int)
     */
    @Override
    public void write(int k) {
        writeCnt += k;
        writeLatch.countDown();
        try {
            readLatch.await();
        } catch (InterruptedException e) {
        }
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.concurrent.Locker#anoRead(int)
     */
    @Override
    public void anoRead(int k) {
        readCnt.addAndGet(k);
        anoReadLatch.countDown();
        try {
            anoWriteLatch.await();
        } catch (InterruptedException e) {
        }
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.concurrent.Locker#anoWrite(int)
     */
    @Override
    public void anoWrite(int k) {
        writeCnt += k;
        anoWriteLatch.countDown();
        try {
            anoReadLatch.await();
        } catch (InterruptedException e) {
        }
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.concurrent.Locker#getReadCnt()
     */
    @Override
    public int getReadCnt() {
        return readCnt.get();
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.concurrent.Locker#getWriteCnt()
     */
    @Override
    public int getWriteCnt() {
        return writeCnt;
    }

}
