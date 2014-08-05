/**
 * SleepStage.java
 *
 * Copyright 2012 Niolex, Inc.
 *
 * Niolex licenses this file to you under the Apache License, version 2.0
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
package org.apache.niolex.commons.test;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.niolex.commons.concurrent.ThreadUtil;
import org.apache.niolex.commons.seda.Dispatcher;
import org.apache.niolex.commons.seda.Message;
import org.apache.niolex.commons.seda.Stage;
import org.apache.niolex.commons.seda.StageTest;
import org.apache.niolex.commons.seda.RejectMessage.RejectType;

/**
 * A test stage, for test and demo usage.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5, $Date: 2012-11-16$
 */
public class SleepStage extends Stage<TInput> {

	private final long sleepTime;
	private final AtomicInteger cnt = new AtomicInteger(0);

	public SleepStage(String stageName, long sleepTime) {
		super(stageName, 2000);
		this.sleepTime = sleepTime;
	}

    public SleepStage(String stageName, int maxTolerableDelay, long sleepTime) {
        super(stageName, maxTolerableDelay);
        this.sleepTime = sleepTime;
    }

    public SleepStage(String stageName, Dispatcher dispatcher) {
        super(stageName, new LinkedBlockingQueue<TInput>(), dispatcher, 1, 6, 2560);
        this.sleepTime = 6;
    }

    public SleepStage(String stageName, Dispatcher dispatcher, long sleepTime) {
		super(stageName, new LinkedBlockingQueue<TInput>(), dispatcher, 1, 6, 2560);
		this.sleepTime = sleepTime;
	}

	/**
	 * Override super method
	 * @see org.apache.niolex.commons.seda.Stage#process(org.apache.niolex.commons.seda.Message, org.apache.niolex.commons.seda.Dispatcher)
	 */
	@Override
	protected void process(TInput in, Dispatcher dispatcher) {
		cnt.incrementAndGet();
		if (in.getTag() == 65432) {
			throw new RuntimeException("We need it.");
		}
		ThreadUtil.sleep(sleepTime);
	}

	@Override
    public void reject(RejectType type, Object info, Message msg) {
        if (type != RejectType.STAGE_BUSY)
            System.out.println("x get rejected by " + type);
    }

	public int getProcessedCount() {
		return cnt.get();
	}

	public Worker getWorker() {
		return StageTest.newWorker(this);
	}

}
