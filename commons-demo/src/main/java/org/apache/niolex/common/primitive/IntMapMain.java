/**
 * IntMapMain.java
 *
 * Copyright 2013 the original author or authors.
 *
 * We licenses this file to you under the Apache License, version 2.0
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

import gnu.trove.map.TIntCharMap;
import gnu.trove.map.hash.TIntCharHashMap;

import it.unimi.dsi.fastutil.ints.Int2CharMap;
import it.unimi.dsi.fastutil.ints.Int2CharOpenHashMap;

import java.util.Map;

import org.apache.niolex.commons.test.Check;
import org.apache.niolex.commons.test.StopWatch;
import org.apache.niolex.commons.test.StopWatch.Stop;

import com.google.common.collect.Maps;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-12-26
 */
public class IntMapMain {

    /**
     * @param args
     */
    public static void main(String[] args) {
        StopWatch w = new StopWatch(1);
        w.begin(true);
        for (int i = 0; i < 1000; ++i) {
            Map<Integer, Boolean> map = Maps.newHashMap();
            Stop s = w.start();
            for (int j = 0; j < 10000; ++j) {
                map.put(j, j % 2 == 0);
            }
            for (int j = 0; j < 10000; ++j) {
                Check.isTrue(map.get(j).booleanValue() == (j % 2 == 0));
            }
            s.stop();
        }
        w.done();
        w.print();
        // --
        w.begin(true);
        for (int i = 0; i < 1000; ++i) {
            TIntCharMap primary = new TIntCharHashMap();
            Stop s = w.start();
            for (int j = 0; j < 10000; ++j) {
                primary.put(j, (char)((j % 2) + '0'));
            }
            for (int j = 0; j < 10000; ++j) {
                Check.isTrue(primary.get(j) == (char)((j % 2) + '0'));
            }
            s.stop();
        }
        w.done();
        w.print();
        // --
        w.begin(true);
        for (int i = 0; i < 1000; ++i) {
            Int2CharMap int2c = new Int2CharOpenHashMap();
            Stop s = w.start();
            for (int j = 0; j < 10000; ++j) {
                int2c.put(j, (char)((j % 2) + '0'));
            }
            for (int j = 0; j < 10000; ++j) {
                Check.isTrue(int2c.get(j) == (char)((j % 2) + '0'));
            }
            s.stop();
        }
        w.done();
        w.print();
    }

}
