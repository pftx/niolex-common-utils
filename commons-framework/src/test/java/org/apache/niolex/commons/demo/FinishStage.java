/**
 * FinishStage.java
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

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-7-31
 */
public class FinishStage extends Stage<WeightMessage> {

    /**
     * Constructor
     * @param stageName
     */
    public FinishStage() {
        super("fin", 10000);
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.commons.seda.Stage#process(org.apache.niolex.commons.seda.Message, org.apache.niolex.commons.seda.Dispatcher)
     */
    @Override
    protected void process(WeightMessage in, Dispatcher dispatcher) {
        in.stop();
    }
}
