/**
 * CyclicBarrierTest.java
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
package org.apache.niolex.common.lang;

import java.util.concurrent.CyclicBarrier;

import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-6-15
 */
public class CyclicBarrierTest {
	private boolean isFin = false;

	class Worker implements Runnable {
	     int myRow;
	     CyclicBarrier cb;
	     Worker(int row, CyclicBarrier ccb) { myRow = row; cb = ccb; }
	     public void run() {
	    	 System.out.println("Current " + myRow);
	    	 try {
				cb.await();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	 System.out.println("Mid " + myRow);
	    	 try {
	    		 cb.await();
	    	 } catch (Exception e) {
	    		 e.printStackTrace();
	    	 }
	    	 System.out.println("Done " + myRow);
	     }
	}


	@Test
	public void test() {
		CyclicBarrier cb = new CyclicBarrier(4, new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Finished.");
				isFin = true;
			}});

		for (int i = 0; i < 4; ++i) {
			new Thread(new Worker(i, cb)).start();
		}
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Is fin. " + isFin);
	}

}
