/**
 * SynchronizedTest.java
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

import java.util.List;

import org.apache.niolex.commons.bean.MutableOne.DataChangeListener;
import org.apache.niolex.commons.concurrent.Blocker;
import org.apache.niolex.commons.concurrent.WaitOn;
import org.apache.niolex.commons.control.TimeCheck;
import org.apache.niolex.commons.util.Runner;
import org.apache.niolex.commons.util.SystemUtil;
import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-7-22
 */
public class SynchronizedTest extends Synchronized {

    final Blocker<String> blocker = new Blocker<String>();

    public void lockOneSec(Object obj) {
        synchronized (obj) {
            blocker.release("s", "s");
            SystemUtil.sleep(100);
        }
    }

    @Test
    public void testNotifyListeners() throws Exception {
        List<DataChangeListener<String>> list = Lists.newArrayList();
        list.add(new DataChangeListener<String>() {

            @Override
            public void onDataChange(String newData) {
                System.out.println("PR\\" + newData);
            }});
        WaitOn<String> on = blocker.initWait("s");
        Runner.run(this, "lockOneSec", list);
        // Make sure the lock thread is running
        on.waitForResult(100);
        notifyListeners(list, "This will happen in 100 msec latter.");
    }

    @Test
    public void testNotifyListenersWaitFor() throws Exception {
        final List<DataChangeListener<String>> list = Lists.newArrayList();
        list.add(new DataChangeListener<String>() {

            @Override
            public void onDataChange(String newData) {
                System.out.println("PR\\" + newData);
                Runner.run(SynchronizedTest.this, "lockOneSec", list);
                SystemUtil.sleep(10);
            }});
        notifyListeners(list, "This will happen now.");
    }

    @Test
    public void testGetIntervalCnt() throws Exception {
        TimeCheck check = new TimeCheck(100, 10, 1000);
        check.getCounter().set(120);
        WaitOn<String> on = blocker.initWait("s");
        Runner.run(this, "lockOneSec", check.getCounter());
        // Make sure the lock thread is running
        on.waitForResult(100);
        int k = getIntervalCnt(10, System.currentTimeMillis() + 10, check);
        assertEquals(120, k);
    }

    @Test
    public void testGetIntervalCntWaitFor() throws Exception {
        final TimeCheck check = new TimeCheck(100, 10, 1000) {

            /**
             * This is the override of super method.
             * @see org.apache.niolex.commons.control.TimeCheck#getLastCheckTime()
             */
            @Override
            public long getLastCheckTime() {
                Runner.run(SynchronizedTest.this, "lockOneSec", this.getCounter());
                SystemUtil.sleep(10);
                return super.getLastCheckTime();
            }

        };
        check.getCounter().set(120);

        int k = getIntervalCnt(10, System.currentTimeMillis() + 10, check);
        assertEquals(120, k);
    }

}
