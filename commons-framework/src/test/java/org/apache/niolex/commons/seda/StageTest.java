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
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.niolex.commons.bean.One;
import org.apache.niolex.commons.concurrent.ThreadUtil;
import org.apache.niolex.commons.reflect.MethodUtil;
import org.apache.niolex.commons.seda.RejectMessage.RejectType;
import org.apache.niolex.commons.test.SleepStage;
import org.apache.niolex.commons.test.TInput;
import org.junit.AfterClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2012-11-18$
 */
public class StageTest {

    public static <Input extends Message> Stage<Input>.Worker newWorker(Stage<Input> s) {
        return s.new Worker();
    }

	static Dispatcher dispatcher = new Dispatcher();

	@AfterClass
	public static void clear() {
		Dispatcher.getInstance().clear();
		dispatcher.clear();
	}

    @Test
    public void testStageString() throws Exception {
        Stage<TInput> s = new Stage<TInput>("not yet implemented"){

            @Override
            protected void process(TInput in, Dispatcher dispatcher) {
                System.out.println("x get input with tag " + (in.getTag() == 0 ? in.hashCode() : in.getTag()));
            }};
        assertEquals("not yet implemented", s.getStageName());
        s.addInput(new TInput(3));
        s.shutdown();
        assertEquals(0, s.getInputSize());
    }

	/**
	 * Test method for {@link org.apache.niolex.commons.seda.Stage#Stage(java.lang.String)}.
	 */
	@Test
	public final void testShutdownSend() {
		SleepStage ss = new SleepStage("abc", dispatcher);
		ss.shutdown();
		ss = spy(ss);
		TInput in = mock(TInput.class);
		ss.addInput(in);
		ArgumentCaptor<RejectType> cap = ArgumentCaptor.forClass(RejectType.class);
		verify(ss).reject(cap.capture(), eq("abc"), eq(in));
		assertEquals(RejectType.STAGE_SHUTDOWN, cap.getValue());
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
	    TInput in = mock(TInput.class);
	    final One<RejectType> one = One.create(null);
		SleepStage ss = new SleepStage("abc", dispatcher){
		    @Override
		    public void reject(RejectType type, Object info, Message msg) {
		        one.a = type;
		    }
		};
		when(in.getTag()).thenReturn(65432);
		ss.addInput(in);
		Thread.sleep(100);
		assertEquals(RejectType.PROCESS_ERROR, one.a);
		assertEquals(ss.getInputSize(), 0);
		ss.shutdown();
		assertNull(ss.takeMessage());
		Thread.sleep(100);
		assertEquals(3, ss.getStageStatus());
		assertNull(ss.takeMessage());
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.seda.Stage#construct()}.
	 */
	@Test
	public final void testWorker() {
		SleepStage ss = new SleepStage("abcdef", 123);
		// Scenario 1. interrupt
		SleepStage.Worker w = ss.getWorker();
		assertTrue(w.isWorking());
		w.setWorking(false);
		w.interrupt();
		assertFalse(w.isWorking());
		ss.construct();
		ss.subtractThread();
		ss.subtractThread();
		ss.subtractThread();
		ss.shutdown();
	}

    @Test
    public void testAdjustThreadPool() throws Exception {
        SleepStage ss = new SleepStage("abcdef", 123);
        ss.adjustThreadPool();
        ss.adjustThreadPool();
        ss.adjustThreadPool();
        ss.shutdown();
        Field f = Stage.class.getDeclaredField("lastAdjustTime");
        f.setAccessible(true);
        f.setLong(ss, 100);
        ss.adjustThreadPool();
        ss.adjustThreadPool();
    }

	/**
	 * Test method for {@link org.apache.niolex.commons.seda.Stage#dropMessage()}.
	 *
	 * @throws Exception
	 */
	@Test
	public final void testDropMessage() throws Exception {
		Stage<Message> ss = new Stage<Message>("abc", new LinkedBlockingQueue<Message>(), dispatcher,
		        1, 1, 200){

            @Override
            protected void process(Message in, Dispatcher dispatcher) {
                ThreadUtil.sleep(1);
            }};
		TInput in = mock(TInput.class);
		RejectMessage r = new RejectMessage(RejectType.USER_REJECT, "By Drop Message", in);
		for (int i = 0; i < 20000; ++i) {
			ss.addInput(in);
			ss.addInput(r);
		}
		Field f = Stage.class.getDeclaredField("lastAdjustTime");
		System.out.println(f);
		f.setAccessible(true);
		f.setLong(ss, 100);
		System.out.println(ss.getInputSize());
		int a = ss.getInputSize();

		// Scenario 1. drop it.
		ss.adjustThreadPool();
		System.out.println(ss.getInputSize());
		a -= ss.getInputSize();
		assertTrue(a > 10000);

		// Scenario 2. no drop.
		ss.dropMessage(-2);

		// Scenario 3. drop it.
		for (int i = 0; i < 20000; ++i) {
            ss.addInput(in);
		}
		MethodUtil.invokeMethod(ss, "tryTerminate");
		ss.dropMessage(10);
		ss.shutdown();
	}

	@Test(expected=NullPointerException.class)
    public final void testDropMessageEx() throws Exception {
	    Stage<Message> ss = new Stage<Message>("abc"){

            @Override
            protected void process(Message in, Dispatcher dispatcher) {
                ThreadUtil.sleep(1);
            }

            /**
             * This is the override of super method.
             * @see org.apache.niolex.commons.seda.Stage#reject(org.apache.niolex.commons.seda.RejectMessage.RejectType, java.lang.Object, org.apache.niolex.commons.seda.Message)
             */
            @Override
            protected void reject(RejectType type, Object info, Message msg) {
                throw new NullPointerException();
            }

	    };

	    TInput in = mock(TInput.class);
        RejectMessage r = new RejectMessage(RejectType.USER_REJECT, "By Drop Message", in);
        ss.addInput(r);
        ss.addInput(r);
        ss.addInput(in);
        ss.addInput(r);
        ss.dropMessage(1);
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
