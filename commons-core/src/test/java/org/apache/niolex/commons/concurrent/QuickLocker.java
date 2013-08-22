/**
 * QuickLocker.java
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
public class QuickLocker implements Locker {

    private int readCnt = 0;
    private int writeCnt = 0;

    private boolean sleep = false;

    public QuickLocker(boolean sleep) {
        super();
        this.sleep = sleep;
    }

    public QuickLocker() {
        super();
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.concurrent.Locker#read(int)
     */
    @Override
    public void read(int k) {
        readCnt += k;
        if (readCnt == k && sleep) ThreadUtil.sleep(40000);
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.concurrent.Locker#write(int)
     */
    @Override
    public void write(int k) {
        writeCnt += k;
        if (writeCnt == k && sleep) ThreadUtil.sleep(40000);
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.concurrent.Locker#ano1(int)
     */
    @Override
    @Syncer.Read
    public void anoRead(int k) {
        readCnt += k;
        if (readCnt == k && sleep) ThreadUtil.sleep(40000);
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.concurrent.Locker#ano2(int)
     */
    @Override
    @Syncer.Write
    public void anoWrite(int k) {
        writeCnt += k;
        if (writeCnt == k && sleep) ThreadUtil.sleep(40000);
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.concurrent.Locker#getReadCnt()
     */
    @Override
    public int getReadCnt() {
        return readCnt;
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
