/**
 * IntListMain.java
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
package org.apache.niolex.common.primitive;

import java.util.ArrayList;

import org.apache.commons.collections.primitives.ArrayIntList;
import org.apache.commons.collections.primitives.IntList;
import org.apache.niolex.commons.test.MockUtil;
import org.apache.niolex.commons.test.StopWatch;
import org.apache.niolex.commons.test.StopWatch.Stop;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5, $Date: 2012-11-26$
 */
public class IntListMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		StopWatch w = new StopWatch(1);
		IntList inList = new ArrayIntList();
		w.begin(true);
		for (int i = 0; i < 1000; ++i) {
			Stop s = w.start();
			for (int j = 0; j < 10000; ++j) {
				inList.add(MockUtil.ranInt(1239087231));
			}
			s.stop();
		}
		w.done();
		w.print();

		ArrayList<Integer> oList = new ArrayList<Integer>();
		w.begin(true);
		for (int i = 0; i < 1000; ++i) {
			Stop s = w.start();
			for (int j = 0; j < 10000; ++j) {
				oList.add(MockUtil.ranInt(1239087231));
			}
			s.stop();
		}
		w.done();
		w.print();
	}

}
