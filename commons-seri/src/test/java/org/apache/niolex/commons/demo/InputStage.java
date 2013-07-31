/**
 * InputStage.java
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

import org.apache.niolex.commons.seda.Dispatcher;
import org.apache.niolex.commons.seda.Stage;
import org.apache.niolex.commons.test.StopWatch;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-7-30
 */
public class InputStage extends Stage<WeightMessage> {

    private WeightStage w;
    private LightStage l;
    private StopWatch sw;

    /**
     * Constructor
     */
    public InputStage(StopWatch sw) {
        super("input");
        this.sw = sw;
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.seda.Stage#construct()
     */
    @Override
    public void construct() {
        w = super.dispatcher.getStage("weight");
        l = super.dispatcher.getStage("light");
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.seda.Stage#process(org.apache.niolex.commons.seda.Message, org.apache.niolex.commons.seda.Dispatcher)
     */
    @Override
    protected void process(WeightMessage in, Dispatcher dispatcher) {
        in.setStop(sw.start());
        if (in.getWeight() > 1) {
            w.addInput(in);
        } else {
            l.addInput(in);
        }
    }

}
