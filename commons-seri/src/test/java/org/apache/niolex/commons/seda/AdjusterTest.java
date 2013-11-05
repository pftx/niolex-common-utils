/**
 * AdjusterTest.java
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

import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2012-11-17$
 */
public class AdjusterTest {

	private Adjuster adj = new Adjuster();

	/**
	 * Test method for {@link org.apache.niolex.commons.seda.Adjuster#addStage(org.apache.niolex.commons.seda.Stage)}.
	 */
	@Test
	public final void testAddStage() {
		adj.addStage(mock(Stage.class));
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.seda.Adjuster#startAdjust()}.
	 * @throws InterruptedException
	 */
	@Test
	public final void testStartAdjust() throws InterruptedException {
		adj.setAdjustInterval(10);
		adj.startAdjust();
		// start again.
		adj.startAdjust();
		adj.addStage(new SleepStage("s10", 10));
		Stage<?> s = mock(Stage.class);
		adj.addStage(s);
		Thread.sleep(30);
		adj.stopAdjust();
		verify(s, atLeast(1)).adjustThreadPool();
		// stop again.
		adj.stopAdjust();
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.seda.Adjuster#stopAdjust()}.
	 */
	@Test
	public final void testStopAdjust() {
		adj.stopAdjust();
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.seda.Adjuster#getAdjustInterval()}.
	 */
	@Test
	public final void testGetAdjustInterval() {
		adj.setAdjustInterval(1230);
		assertEquals(adj.getAdjustInterval(), 1230);
	}

}
