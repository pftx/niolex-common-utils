/**
 * DispatcherTest.java
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

import java.util.HashMap;
import java.util.Map;

import org.apache.niolex.commons.test.ConstructStage;
import org.apache.niolex.commons.test.OrderedRunner;
import org.apache.niolex.commons.test.SleepStage;
import org.apache.niolex.commons.test.TInput;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2012-11-17$
 */
@RunWith(OrderedRunner.class)
public class DispatcherTest {

	Dispatcher disp = Dispatcher.getInstance();

	/**
	 * Test method for {@link org.apache.niolex.commons.seda.Dispatcher#register(org.apache.niolex.commons.seda.Stage)}.
	 */
	@Test
	public final void testARegister() {
		disp.register(new SleepStage("S10", 10));
		disp.register(new SleepStage("S20", 20));
		Map<String, String> consMap = new HashMap<String, String>();
		consMap.put(null, "S10");
		consMap.put("s10", "S10");
		consMap.put("S20", "S20");
		consMap.put("s20", "S20");
		disp.register(new ConstructStage("C1", consMap));
		System.out.println(TInput.class.toString());
		disp.register(new ConstructStage(TInput.class.getName(), consMap));
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.seda.Dispatcher#getStage(java.lang.String)}.
	 */
	@Test
	public final void testGetStage() {
	    SleepStage sl = disp.getStage("S10");
	    assertEquals("S10", sl.getStageName());
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.seda.Dispatcher#construction()}.
	 */
	@Test
	public final void testConstruction() {
		disp.construction();
	}

	@Test
	public void testStartAdjust() throws Exception {
	    disp.startAdjust(1024);
	    disp.startAdjust(2048);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.seda.Dispatcher#dispatch(java.lang.String, org.apache.niolex.commons.seda.Message)}.
	 */
	@Test
	public final void testDispatchStringT() {
		boolean a = disp.dispatch("C1", new TInput());
		assertTrue(a);
		a = disp.dispatch("C2", new TInput());
		assertFalse(a);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.seda.Dispatcher#dispatch(org.apache.niolex.commons.seda.Message)}.
	 */
	@Test
	public final void testDispatchT() {
		boolean a = disp.dispatch(new TInput());
		assertTrue(a);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.seda.Dispatcher#getAllStages()}.
	 */
	@Test
	public final void testGetAllStages() {
		int k = disp.getAllStages().size();
		assertEquals(4, k);
	}

	@Test
	public void testYShutdown() throws Exception {
	    disp.shutdown();
	    disp.shutdown();
	}

    @Test
    public final void testZClear() {
        disp.clear();
        int k = disp.getAllStages().size();
        assertEquals(0, k);
    }

}
