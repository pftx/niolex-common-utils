/**
 * DemoLocker.java
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

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-7-27
 */
public class DemoLocker implements Locker {

    private int readCnt = 0;
    private int writeCnt = 0;

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.concurrent.Locker#read(int)
     */
    @Override
    public void read(int k) {
        readCnt += k;
        ThreadUtil.sleepAtLeast(k * 5);
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.concurrent.Locker#write(int)
     */
    @Override
    public void write(int k) {
        writeCnt += k;
        ThreadUtil.sleepAtLeast(k * 5);
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.concurrent.Locker#ano1(int)
     */
    @Override
    @Syncer.Read
    public void ano1(int k) {
        readCnt += k * 2;
        ThreadUtil.sleepAtLeast(k * 5);
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.concurrent.Locker#ano2(int)
     */
    @Override
    @Syncer.Write
    public void ano2(int k) {
        writeCnt += k * 2;
        ThreadUtil.sleepAtLeast(k * 5);
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.concurrent.Locker#getReadCnt()
     */
    @Override
    public int getReadCnt() {
        return this.readCnt;
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.concurrent.Locker#getWriteCnt()
     */
    @Override
    public int getWriteCnt() {
        return this.writeCnt;
    }

}
