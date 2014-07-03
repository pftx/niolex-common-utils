/**
 * DoubleHashBalanceTest.java
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
package org.apache.niolex.commons.hash;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.niolex.commons.bean.Pair;
import org.apache.niolex.commons.util.MathUtil;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.common.hash.Hashing;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-6-4
 */
public class DoubleHashBalanceTest {

    @Test
    public void testAddBalance() throws Exception {
        Collection<String> nodeList = Arrays.asList("10.214.133.100:8087", "10.214.133.101:8088", "10.214.65.11:8087", "10.214.65.12:8088",
                "10.214.133.102:8087", "10.214.133.103:8088", "10.214.65.13:8087", "10.214.65.14:8088");
        DoubleHash<String> dHash = new DoubleHash<String>(Hashing.murmur3_128(), Hashing.crc32(), nodeList);
        Map<String, Integer> map = Maps.newHashMap();
        Map<String, String> trap = Maps.newHashMap();
        for (int i = 0; i < 20000; ++i) {
            String key = MathUtil.randString();
            String node = dHash.getPairNodes(key).a;
            Integer in = map.get(node);
            in = in == null ? 1 : in + 1;
            map.put(node, in);
            trap.put(key, node);
        }
        Pair<Integer,Double> pair = MathUtil.calcMeanSquareError(map.values());
        System.out.println("BalanceA => " + pair.toString("avg", "MSE") + ", PCT=" + (int)(pair.b / pair.a * 100) + "%");
        dHash.add("10.13.65.131:8088");
        int cnt = 0;
        for (Entry<String, String> o : trap.entrySet()) {
            String node = dHash.getPairNodes(o.getKey()).a;
            if (!node.equals(o.getValue())) {
                ++cnt;
            }
        }
        System.out.println("ChangedA => " + cnt + ", PCT=" + (cnt * 100 / 20000) + "%");
    }

    @Test
    public void testRemoveBalance() throws Exception {
        Collection<String> nodeList = Arrays.asList("10.214.133.100:8087", "10.214.133.101:8088", "10.214.65.11:8087", "10.214.65.12:8088",
                "10.214.133.102:8087", "10.214.133.103:8088", "10.214.65.13:8087", "10.214.65.14:8088");
        DoubleHash<String> dHash = new DoubleHash<String>(Hashing.murmur3_128(), Hashing.crc32(), nodeList);
        Map<String, String> trap = Maps.newHashMap();
        for (int i = 0; i < 20000; ++i) {
            String key = MathUtil.randString();
            String node =dHash.getPairNodes(key).a;
            trap.put(key, node);
        }
        dHash.remove("10.214.133.101:8088");
        int cnt = 0;
        Map<String, Integer> map = Maps.newHashMap();
        for (Entry<String, String> o : trap.entrySet()) {
            String node = dHash.getPairNodes(o.getKey()).a;
            Integer in = map.get(node);
            in = in == null ? 1 : in + 1;
            map.put(node, in);
            if (!node.equals(o.getValue())) {
                ++cnt;
            }
        }
        Pair<Integer,Double> pair = MathUtil.calcMeanSquareError(map.values());
        System.out.println("BalanceR => " + pair.toString("avg", "MSE") + ", PCT=" + (int)(pair.b / pair.a * 100) + "%");
        System.out.println("ChangedR => " + cnt + ", PCT=" + (cnt * 100 / 20000) + "%");
    }

    @Test
    public void testRemoveFirstBalance() throws Exception {
        Collection<String> nodeList = Arrays.asList("10.214.133.100:8087", "10.214.133.101:8088", "10.214.65.11:8087", "10.214.65.12:8088",
                "10.214.133.102:8087", "10.214.133.103:8088", "10.214.65.13:8087", "10.214.65.14:8088");
        DoubleHash<String> dHash = new DoubleHash<String>(Hashing.murmur3_128(), Hashing.crc32(), nodeList);
        Map<String, String> trap = Maps.newHashMap();
        for (int i = 0; i < 20000; ++i) {
            String key = MathUtil.randString();
            String node = dHash.getPairNodes(key).a;
            trap.put(key, node);
        }
        dHash.remove("10.214.133.100:8087");
        int cnt = 0;
        Map<String, Integer> map = Maps.newHashMap();
        for (Entry<String, String> o : trap.entrySet()) {
            String node = dHash.getPairNodes(o.getKey()).a;
            Integer in = map.get(node);
            in = in == null ? 1 : in + 1;
            map.put(node, in);
            if (!node.equals(o.getValue())) {
                ++cnt;
            }
        }
        Pair<Integer,Double> pair = MathUtil.calcMeanSquareError(map.values());
        System.out.println("BalanceR => " + pair.toString("avg", "MSE") + ", PCT=" + (int)(pair.b / pair.a * 100) + "%");
        System.out.println("ChangedR => " + cnt + ", PCT=" + (cnt * 100 / 20000) + "%");
    }

    @Test
    public void testRemoveLastBalance() throws Exception {
        Collection<String> nodeList = Arrays.asList("10.214.133.100:8087", "10.214.133.101:8088", "10.214.65.11:8087", "10.214.65.12:8088",
                "10.214.133.102:8087", "10.214.133.103:8088", "10.214.65.13:8087", "10.214.65.14:8088");
        DoubleHash<String> dHash = new DoubleHash<String>(Hashing.murmur3_128(), Hashing.crc32(), nodeList);
        Map<String, String> trap = Maps.newHashMap();
        for (int i = 0; i < 20000; ++i) {
            String key = MathUtil.randString();
            String node = dHash.getPairNodes(key).a;
            trap.put(key, node);
        }
        dHash.remove("10.214.65.14:8088");
        int cnt = 0;
        Map<String, Integer> map = Maps.newHashMap();
        for (Entry<String, String> o : trap.entrySet()) {
            String node = dHash.getPairNodes(o.getKey()).a;
            Integer in = map.get(node);
            in = in == null ? 1 : in + 1;
            map.put(node, in);
            if (!node.equals(o.getValue())) {
                ++cnt;
            }
        }
        Pair<Integer,Double> pair = MathUtil.calcMeanSquareError(map.values());
        System.out.println("BalanceL => " + pair.toString("avg", "MSE") + ", PCT=" + (int)(pair.b / pair.a * 100) + "%");
        System.out.println("ChangedL => " + cnt + ", PCT=" + (cnt * 100 / 20000) + "%");
    }
}
