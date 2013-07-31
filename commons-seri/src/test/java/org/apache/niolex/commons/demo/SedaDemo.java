/**
 * SedaDemo.java
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
package org.apache.niolex.commons.demo;

import org.apache.niolex.commons.concurrent.ThreadUtil;
import org.apache.niolex.commons.seda.Dispatcher;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-7-30
 */
public class SedaDemo {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Dispatcher disp = Dispatcher.getInstance();
        ThreadGroup group = ThreadUtil.topGroup();
        disp.register(new InputStage());
        disp.register(new WeightStage());

        disp.construction();
        disp.startAdjust(1000);
        group.list();

        System.out.println("Stage 1 - dispatch 5K/s, 100(1ms) : 1(100ms) weight messages; for 30 sec.");

        for (int i = 0; i < 30; ++i) {
            for (int j = 0; j < 5000; ++j) {
                if (j % 100 == 50) {
                    disp.dispatch("input", new WeightMessage(100));
                } else {
                    disp.dispatch("input", new WeightMessage(1));
                }
                if (j % 5 == 1) {
                    ThreadUtil.sleep(1);
                }
            }
        }
        group.list();

        System.out.println("Stage 2 - dispatch 1K/s, 1(1ms) : 1(10ms) weight messages; for 30 sec.");

        for (int i = 0; i < 30; ++i) {
            for (int j = 0; j < 1000; ++j) {
                if (j % 2 == 1) {
                    disp.dispatch("input", new WeightMessage(10));
                } else {
                    disp.dispatch("input", new WeightMessage(1));
                }
                ThreadUtil.sleep(1);
            }
        }
        group.list();

        System.out.println("Stage 3 - dispatch 10K/s, 1(1ms) : 1(10ms) weight messages; for 30 sec.");

        for (int i = 0; i < 30; ++i) {
            for (int j = 0; j < 10000; ++j) {
                if (j % 2 == 1) {
                    disp.dispatch("input", new WeightMessage(10));
                } else {
                    disp.dispatch("input", new WeightMessage(1));
                }
                if (j % 10 == 1) {
                    ThreadUtil.sleep(1);
                }
            }
        }
        group.list();

        System.out.println("Stage 4 - dispatch 10K/s, 1(1ms) : 1(3ms) weight messages; for 30 sec.");

        for (int i = 0; i < 30; ++i) {
            for (int j = 0; j < 10000; ++j) {
                if (j % 2 == 1) {
                    disp.dispatch("input", new WeightMessage(3));
                } else {
                    disp.dispatch("input", new WeightMessage(1));
                }
                if (j % 10 == 1) {
                    ThreadUtil.sleep(1);
                }
            }
        }
        group.list();

        ThreadUtil.sleep(10000);
        System.out.println("Done.");

    }

}
