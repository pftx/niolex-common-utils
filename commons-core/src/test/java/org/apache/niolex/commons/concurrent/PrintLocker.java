/**
 * PrintLocker.java
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

import org.apache.niolex.commons.util.Runner;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-7-27
 */
public class PrintLocker implements Locker {

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.concurrent.Locker#read(int)
     */
    @Override
    public void read(int k) {
        for (int i = 0; i < k; ++i) {
            System.out.println("Read " + i);
            ThreadUtil.sleep(1000);
        }
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.concurrent.Locker#write(int)
     */
    @Override
    public void write(int k) {
        for (int i = 0; i < k; ++i) {
            System.out.println("Write " + i);
            ThreadUtil.sleep(1000);
        }
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.concurrent.Locker#ano1(int)
     */
    @Override
    public void ano1(int k) {
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.concurrent.Locker#ano2(int)
     */
    @Override
    public void ano2(int k) {
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.concurrent.Locker#getReadCnt()
     */
    @Override
    public int getReadCnt() {
        return 0;
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.concurrent.Locker#getWriteCnt()
     */
    @Override
    public int getWriteCnt() {
        return 0;
    }

    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        Locker print = Syncer.syncByRegex(new PrintLocker(), "r.*", "w.*");
        Runner.run(print, "read", 10);
        Runner.run(print, "read", 10);
        Runner.run(print, "read", 10);
        ThreadUtil.sleep(1000);
        Runner.run(print, "read", 10);
        Thread t2 = Runner.run(print, "write", 10);
        Thread t1 = Runner.run(print, "write", 10);
        t1.join();
        t2.join();
    }

}
