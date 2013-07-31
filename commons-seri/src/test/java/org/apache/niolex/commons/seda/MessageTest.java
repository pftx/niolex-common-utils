/**
 * MessageTest.java
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

import org.apache.niolex.commons.seda.Message.RejectType;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2012-11-17$
 */
public class MessageTest {

	Dispatcher disp = new Dispatcher();
	RejectMessage inp;
	Stage<RejectMessage> stage = new Stage<RejectMessage>(RejectMessage.class.toString()) {

		@Override
		public void addInput(RejectMessage in) {
			inp = in;
		}

		@Override
		protected void process(RejectMessage in, Dispatcher dispatcher) {
		}

	};
	Message in = new Message() {
	};

	@Before
	public void add() {
		disp.register(stage);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.seda.Message#reject(org.apache.niolex.commons.seda.Message.RejectType, java.lang.Object)}.
	 */
	@Test
	public final void testReject1() {
		in.reject(RejectType.PROCESS_ERROR, in, disp);
		assertEquals(RejectType.PROCESS_ERROR, inp.getType());
	}

	@Test
	public final void testReject2() {
		in.reject(RejectType.STAGE_BUSY, in, disp);
		assertEquals(in, inp.getInfo());
	}

	@Test
	public final void testReject3() {
		in.reject(RejectType.STAGE_SHUTDOWN, in, disp);
		assertEquals(in, inp.getRejected());
		assertEquals(RejectType.STAGE_SHUTDOWN.toString(), "STAGE_SHUTDOWN");
	}

	@Test
	public final void testReject4() {
		in.reject(RejectType.USER_REJECT, in, disp);
		assertEquals(RejectType.valueOf("USER_REJECT"), RejectType.USER_REJECT);
	}

}
