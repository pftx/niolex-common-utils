/**
 * ConstructTest.java
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
package org.apache.niolex.commons.seda;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.niolex.commons.test.MockUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2012-11-17$
 */
public class ConstructTest {

	public static class C1Stage extends SleepStage {

		private Stage<TInput> s10;
		private Stage<TInput> s1;

		/**
		 * Constructor
		 * @param stageName
		 * @param sleepTime
		 */
		public C1Stage(String stageName, long sleepTime) {
			super(stageName, sleepTime);
		}

		@Override
		public void construct() {
			s10 = dispatcher.getStage("s10");
			s1 = dispatcher.getStage("s1");
		}

		@Override
		protected void process(TInput in, Dispatcher dispatcher) {
			super.process(in, dispatcher);
			if (in.getTag() % 2 == 0) {
				s10.addInput(in);
			} else {
				s1.addInput(in);
			}
		}

	}

	public static class S1Stage extends SleepStage {

		private Stage<TInput> e1;
		private Stage<TInput> e8;
		private Stage<TInput> e20;

		/**
		 * Constructor
		 * @param stageName
		 * @param sleepTime
		 */
		public S1Stage(String stageName, long sleepTime) {
			super(stageName, sleepTime);
		}

		@Override
		public void construct() {
			e1 = dispatcher.getStage("e1");
			e8 = dispatcher.getStage("e8");
			e20 = dispatcher.getStage("e20");
		}

		@Override
		protected void process(TInput in, Dispatcher dispatcher) {
			super.process(in, dispatcher);
			int k = (in.getTag() >> 3) % 3;
			switch (k) {
			case 1:
				e1.addInput(in);
				break;
			case 2:
				e20.addInput(in);
				break;
			case 0:
			default:
				e8.addInput(in);
			}
		}

	}

	public static class S10Stage extends SleepStage {

		private Stage<TInput> e1;

		/**
		 * Constructor
		 * @param stageName
		 * @param sleepTime
		 */
		public S10Stage(String stageName, long sleepTime) {
			super(stageName, sleepTime);
		}

		@Override
		public void construct() {
			e1 = dispatcher.getStage("e1");
		}

		@Override
		protected void process(TInput in, Dispatcher dispatcher) {
			super.process(in, dispatcher);
			e1.addInput(in);
		}

	}

	public static class EndStage extends SleepStage {

		private CountDownLatch latch;

		/**
		 * Constructor
		 * @param stageName
		 * @param sleepTime
		 */
		public EndStage(String stageName, long sleepTime, CountDownLatch latch) {
			super(stageName, sleepTime);
			this.latch = latch;
		}

		@Override
		protected void process(TInput in, Dispatcher dispatcher) {
			super.process(in, dispatcher);
			latch.countDown();
		}

	}


	static Dispatcher disp = Dispatcher.getInstance();

	static CountDownLatch latch;

	static int computeSize = 5000;

	/**
	 * Test method for {@link org.apache.niolex.commons.seda.Dispatcher#register(org.apache.niolex.commons.seda.Stage)}.
	 */
	@BeforeClass
	public static final void register() {
		latch = new CountDownLatch(computeSize);
		disp.register(new EndStage("e8", 8, latch));
		disp.register(new EndStage("e20", 20, latch));
		disp.register(new EndStage("e1", 1, latch));
		disp.register(new S10Stage("s10", 10));
		disp.register(new S1Stage("s1", 1));
		disp.register(new C1Stage("c1", 1));
		disp.construction();
		disp.startAdjust(1005);
	}

	@AfterClass
	public static final void clear() {
		disp.clear();
	}

	@Test
	public void testDispatch() throws Exception {
		for (int i = 0; i < computeSize; ++i) {
			disp.dispatch("c1", new TInput(MockUtil.randInt(1000)));
			Thread.sleep(1);
		}

		latch.await(10, TimeUnit.SECONDS);
		disp.shutdown();

		for (Stage<?>s : disp.getAllStages()) {
			SleepStage ss = (SleepStage)s;
			System.out.println(ss.getStageName() + "\t-> " + ss.getStageStatus() + " -> " + ss.getProcessedCount());
		}
		assertEquals(0, latch.getCount());
	}

	public static void main(String[] args) throws Exception {
		computeSize = 30000;
		register();
		new ConstructTest().testDispatch();
	}
}
