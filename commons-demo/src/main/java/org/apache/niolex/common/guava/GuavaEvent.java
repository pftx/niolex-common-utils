/**
 * GuavaEvent.java
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
package org.apache.niolex.common.guava;

import javax.swing.event.ChangeEvent;

import org.apache.niolex.commons.test.MockUtil;
import org.apache.niolex.commons.util.SystemUtil;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-5-10
 */
public class GuavaEvent {

    // Class is typically registered by the container.
    public static class EventBusChangeRecorder {

        @Subscribe
        public void recordCustomerChange(ChangeEvent e) {
            recordChange(e.getSource());
        }

        /**
         * @param source
         */
        private void recordChange(Object source) {
            System.out.println("Change => " + source);
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        EventBus eventBus = new EventBus("test");

        // somewhere during initialization
        eventBus.register(new EventBusChangeRecorder());

        int i = 30;

        while (i-- > 0) {
            SystemUtil.sleep(200);
            changeCustomer(eventBus);
        }
    }

    // much later
    public static void changeCustomer(EventBus eventBus) {
        ChangeEvent event = getChangeEvent();
        eventBus.post(event);
    }

    /**
     * @return
     */
    private static ChangeEvent getChangeEvent() {
        return new ChangeEvent(MockUtil.randUUID());
    }

}
