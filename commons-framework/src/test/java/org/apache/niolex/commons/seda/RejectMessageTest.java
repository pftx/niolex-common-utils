/**
 * SedaTest.java
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

import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5, $Date: 2012-11-16$
 */
public class RejectMessageTest {

	@Test
	public void description() {
		System.out.println("This class need run main, and a long time, for human watch.");
	}

	/**
	 * A test scenario.
	 *
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		SleepStage s = new SleepStage("S1", 6);
		Adjuster adj = new Adjuster();
		adj.addStage(s);
		adj.startAdjust();
		int it = 10000;
		//
		System.out.println("stage 1. send 10 request and sleep 10.");
		while (it-- > 0) {
			s.addInput(new TInput());
			Thread.sleep(1);
		}
		//
		System.out.println("stage 2. send 60 request and sleep 10.");
		it = 5000;
		while (it-- > 0) {
			for (int i = 0; i < 60; ++i) {
				s.addInput(new TInput());
			}
			Thread.sleep(10);
		}
		it = 20000;
		//
		System.out.println("stage 3. send 20 request and sleep 10.");
		while (it-- > 0) {
			s.addInput(new TInput());
			s.addInput(new TInput());
			Thread.sleep(1);
		}
		//
		it = 10000;
		System.out.println("stage 4. send 10 request and sleep 10.");
		while (it-- > 0) {
			s.addInput(new TInput());
			Thread.sleep(1);
		}
		//
		System.out.println("stage 5. shutdown now.");
		Thread.sleep(100);
		s.shutdown();
		adj.stopAdjust();
	}

}
