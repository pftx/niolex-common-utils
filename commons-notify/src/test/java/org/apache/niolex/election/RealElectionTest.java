/**
 * RealElectionTest.java
 *
 * Copyright 2016 the original author or authors.
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
package org.apache.niolex.election;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.apache.niolex.commons.concurrent.ThreadUtil;
import org.apache.niolex.notify.AppTest;
import org.apache.niolex.zookeeper.core.ZKConnectorExceTest;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 2.1.2
 * @since Jul 12, 2016
 */
public class RealElectionTest {
    
    private static String BS = "/election/zkc/real-elect";
    
    public static class LeaderLinstener implements Elector.Listener {
        
        private String leaderAddr;
        private boolean isLeader;

        /**
         * This is the override of super method.
         * @see org.apache.niolex.election.Elector.Listener#leaderChange(java.lang.String)
         */
        @Override
        public void leaderChange(String address) {
            leaderAddr = address;
            isLeader = false;
            System.out.println(" [x] => new leader: " + address);
        }

        /**
         * This is the override of super method.
         * @see org.apache.niolex.election.Elector.Listener#runAsLeader()
         */
        @Override
        public void runAsLeader() {
            leaderAddr = null;
            isLeader = true;
        }

        public String getLeaderAddr() {
            return leaderAddr;
        }

        public boolean isLeader() {
            return isLeader;
        }
        
    }
    
    static {
        AppTest.cleanZK(BS);
    }
    
    @Test
    public void testElector() throws Exception {
        Elector.Listener li = mock(Elector.Listener.class);
        LeaderLinstener ll = new LeaderLinstener();
        
        Elector el1 = new Elector(AppTest.URL, 10000, BS + "/", li);
        Elector el2 = new Elector(AppTest.URL, 10000, BS, li);
        Elector el3 = new Elector(AppTest.URL, 10000, BS + "/", li);
        Elector el4 = new Elector(AppTest.URL, 10000, BS, ll);
        
        el1.register("local1");
        el2.register("local2");
        el3.register("local3");
        el4.register("local4");
        
        assertEquals("local1", ll.leaderAddr);
        assertFalse(ll.isLeader);
        el1.close();
        ThreadUtil.sleepAtLeast(100);
        assertEquals("local2", ll.leaderAddr);
        assertFalse(ll.isLeader);
        el2.close();
        ThreadUtil.sleepAtLeast(100);
        assertEquals("local3", ll.leaderAddr);
        assertFalse(ll.isLeader);
        el3.close();
        ThreadUtil.sleepAtLeast(100);
        assertNull(ll.leaderAddr);
        assertTrue(ll.isLeader);
        System.out.println("============Start reconnect.================");
        ZKConnectorExceTest.reconn(el2);
        ZKConnectorExceTest.reconn(el3);
        el4.close();
        ZKConnectorExceTest.reconn(el4);
        ZKConnectorExceTest.reconn(el1);
        ThreadUtil.sleepAtLeast(100);
        assertEquals("local2", ll.leaderAddr);
        assertFalse(ll.isLeader);
        
        el1.close();
        el2.close();
        el3.close();
        el4.close();
    }

}
