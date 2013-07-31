/**
 * StageTest.java
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import org.apache.niolex.commons.seda.Message.RejectType;
import org.junit.AfterClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2012-11-18$
 */
public class StageTest {

	static Dispatcher dispatcher = new Dispatcher();

	@AfterClass
	public static void clear() {
		Dispatcher.getInstance().clear();
		dispatcher.clear();
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.seda.Stage#Stage(java.lang.String)}.
	 */
	@Test
	public final void testShutdownSend() {
		SleepStage ss = new SleepStage("abc", dispatcher);
		ss.shutdown();
		TInput in = mock(TInput.class);
		ss.addInput(in);
		ArgumentCaptor<RejectType> cap = ArgumentCaptor.forClass(RejectType.class);
		verify(in).reject(cap.capture(), eq("abc"), eq(dispatcher));
		assertEquals(Message.RejectType.STAGE_SHUTDOWN, cap.getValue());
		assertEquals(ss.getInputSize(), 0);
	}

	/**
	 * Test method for
	 * {@link org.apache.niolex.commons.seda.Stage#Stage(java.lang.String, java.util.concurrent.BlockingQueue, org.apache.niolex.commons.seda.Dispatcher, int, int, int)}
	 * .
	 *
	 * @throws InterruptedException
	 */
	@Test
	public final void testProcessError() throws InterruptedException {
		SleepStage ss = new SleepStage("abc", dispatcher);
		TInput in = mock(TInput.class);
		when(in.getTag()).thenReturn(65432);
		ss.addInput(in);
		Thread.sleep(100);
		ArgumentCaptor<RejectType> cap = ArgumentCaptor.forClass(RejectType.class);
		verify(in).reject(cap.capture(), anyObject(), eq(dispatcher));
		assertEquals(Message.RejectType.PROCESS_ERROR, cap.getValue());
		assertEquals(ss.getInputSize(), 0);
		ss.shutdown();
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.seda.Stage#construct()}.
	 */
	@Test
	public final void testWorker() {
		SleepStage ss = new SleepStage("abc", dispatcher);
		SleepStage.Worker w = ss.getWorker();
		assertTrue(w.isWorking());
		w.setWorking(false);
		w.interrupt();
		assertFalse(w.isWorking());
		ss.shutdown();
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.seda.Stage#dropMessage()}.
	 *
	 * @throws Exception
	 */
	@Test
	public final void testDropMessage() throws Exception {
		SleepStage ss = new SleepStage("abc", dispatcher);
		TInput in = mock(TInput.class);
		for (int i = 0; i < 20000; ++i) {
			ss.addInput(in);
		}
		Field f = Stage.class.getDeclaredField("lastAdjustTime");
		System.out.println(f);
		f.setAccessible(true);
		f.setLong(ss, 100);
		System.out.println(ss.getInputSize());
		int a = ss.getInputSize();
		ss.adjustThreadPool();
		System.out.println(ss.getInputSize());
		a -= ss.getInputSize();
		assertTrue(a > 10000);
		ss.shutdown();
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.seda.Stage#startPool()}.
	 */
	public final void startAdjust(SleepStage ss, Field f) throws Exception {
		f.setLong(ss, System.currentTimeMillis() - 1001);
		ss.adjustThreadPool();
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.seda.Stage#addThread()}.
	 */
	@Test
	public final void testAddThreadToMax() throws Exception {
		SleepStage ss = new SleepStage("abc", dispatcher);
		Field f = Stage.class.getDeclaredField("lastAdjustTime");
		f.setAccessible(true);
		TInput in = mock(TInput.class);
		// Prepare ready.
		// stage 1.
		for (int i = 0; i < 60; ++i) {
			ss.addInput(in);
		}
		ss.exeCnt.addAndGet(30);
		this.startAdjust(ss, f);
		// stage 2.
		for (int i = 0; i < 210; ++i) {
			ss.addInput(in);
		}
		ss.exeCnt.addAndGet(100);
		this.startAdjust(ss, f);
		// stage 3.
		for (int i = 0; i < 550; ++i) {
			ss.addInput(in);
		}
		ss.exeCnt.addAndGet(200);
		this.startAdjust(ss, f);
		// stage 4.
		for (int i = 0; i < 1300; ++i) {
			ss.addInput(in);
		}
		ss.exeCnt.addAndGet(500);
		this.startAdjust(ss, f);
		// stage 5.
		System.out.println("up stage finished. we start to drop.");
		// No input, But we adjust the executed item.
		for (int i = 0; i < 500; ++i) {
			ss.inputQueue.poll();
		}
		ss.exeCnt.addAndGet(500);
		this.startAdjust(ss, f);
		for (int i = 0; i < 500; ++i) {
			ss.inputQueue.poll();
		}
		ss.exeCnt.addAndGet(500);
		this.startAdjust(ss, f);
		// stage 6.
		// No input, We adjust even more executed item.
		ss.exeCnt.addAndGet(600);
		for (int i = 0; i < 600; ++i) {
			ss.inputQueue.poll();
		}
		System.out.println("stage6 " + ss.currentPoolSize);
		int a = ss.currentPoolSize;
		this.startAdjust(ss, f);
		System.out.println("stage6 " + ss.currentPoolSize);
		a -= ss.currentPoolSize;
		// We are in tremble.
		assertTrue(a == 0);
		// stage 7.
		for (int i = 0; i < 600; ++i) {
			ss.inputQueue.poll();
		}
		ss.exeCnt.addAndGet(600);
		this.startAdjust(ss, f);
		System.out.println("stage7 " + ss.currentPoolSize);
		// stage 8.
		ss.exeCnt.addAndGet(200);
		this.startAdjust(ss, f);
		System.out.println("stage8 " + ss.currentPoolSize);
		// stage 9.
		ss.exeCnt.addAndGet(3);
		this.startAdjust(ss, f);
		System.out.println("stage9 " + ss.currentPoolSize);
		// stage 10.
		ss.exeCnt.addAndGet(3);
		this.startAdjust(ss, f);
		System.out.println("stage10 " + ss.currentPoolSize);
		assertEquals(1, ss.currentPoolSize);
		ss.shutdown();
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.seda.Stage#adjustThreadPool()}.
	 */
	@Test
	public final void testAdjustTremble() throws Exception {
		SleepStage ss = new SleepStage("abc", dispatcher, 600);
		Field f = Stage.class.getDeclaredField("lastAdjustTime");
		f.setAccessible(true);
		TInput in = mock(TInput.class);
		ss.processRate = 0.030;
		// Prepare ready.
		// stage 1.
		for (int i = 0; i < 100; ++i) {
			ss.addInput(in);
		}
		ss.exeCnt.addAndGet(3);
		this.startAdjust(ss, f);
		int psize = ss.currentPoolSize;
		System.out.println("stage1 " + ss.currentPoolSize);
		assertEquals(psize, 2);
		// stage 2. we drop fast.
		ss.exeCnt.addAndGet(60);
		ss.inputQueue.clear();
		for (int i = 0; i < 41; ++i) {
            ss.addInput(in);
        }
		this.startAdjust(ss, f);
		psize = ss.currentPoolSize;
		System.out.println("stage2 " + ss.currentPoolSize);
		assertEquals(psize, 1);
		// stage 3. we rise very fast.
		ss.exeCnt.addAndGet(30);
		for (int i = 0; i < 100; ++i) {
			ss.addInput(in);
		}
		this.startAdjust(ss, f);
		psize = ss.currentPoolSize;
		System.out.println("stage3 " + ss.currentPoolSize);
		assertEquals(psize, 2);
		// stage 4. we drop slowly.
		ss.exeCnt.addAndGet(70);
		for (int i = 0; i < 30; ++i) {
			ss.addInput(in);
		}
		this.startAdjust(ss, f);
		psize = ss.currentPoolSize;
		System.out.println("stage4 " + ss.currentPoolSize);
		assertEquals(psize, 3);
		// stage 5. we drop faster.
		ss.exeCnt.addAndGet(70);
		for (int i = 0; i < 10; ++i) {
			ss.inputQueue.poll();
		}
		this.startAdjust(ss, f);
		psize = ss.currentPoolSize;
		System.out.println("stage5 " + ss.currentPoolSize);
		assertEquals(psize, 3);
		// stage 6. we rise slowly.
		ss.exeCnt.addAndGet(70);
		for (int i = 0; i < 15; ++i) {
			ss.addInput(in);
		}
		this.startAdjust(ss, f);
		psize = ss.currentPoolSize;
		System.out.println("stage6 " + ss.currentPoolSize);
		assertEquals(psize, 3);
		// stage 7. we have nothing.
		this.startAdjust(ss, f);
		psize = ss.currentPoolSize;
		System.out.println("stage7 " + ss.currentPoolSize);
		assertEquals(psize, 1);
		// done.
		ss.shutdown();
	}

}
