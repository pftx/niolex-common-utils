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


import java.util.List;

import org.apache.niolex.commons.bean.MutableOne.DataChangeListener;
import org.apache.niolex.commons.concurrent.Blocker;
import org.apache.niolex.commons.concurrent.WaitOn;
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

}
