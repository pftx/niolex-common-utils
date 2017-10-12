/**
 * ConsistentHashBalanceText.java
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

import java.util.Collection;
import java.util.Map;

import org.apache.niolex.commons.bean.Pair;
import org.apache.niolex.commons.test.MockUtil;
import org.apache.niolex.commons.util.MathUtil;
import org.junit.Test;

import com.google.common.collect.Maps;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-6-1
 */
public class ConsistentHashBalanceTest {

    @Test
    public void testBalanceInt() {
        ConsistentHash<String> cHash = new ConsistentHash<String>();
        cHash.add("10.214.133.100:8087");
        cHash.add("10.214.133.100:8088");
        cHash.add("10.214.133.101:8087");
        cHash.add("10.214.133.101:8088");
        cHash.add("10.214.65.11:8087");
        cHash.add("10.214.65.11:8088");
        cHash.add("10.214.65.12:8087");
        cHash.add("10.214.65.12:8088");
        // Now we have four cache machines, each run two instances, a total of 8 instances.
        Map<String, Integer> map = Maps.newHashMap();
        for (int i = 110001; i < 190001; ++i) {
            Collection<String> nodeList = cHash.getNodeList(i);
            for (String key : nodeList) {
                Integer integer = map.get(key);
                if (integer == null) {
                    integer = 1;
                } else {
                    integer += 1;
                }
                map.put(key, integer);
            }
        }
        System.out.println("BalanceInt => " + MathUtil.calcStandardDeviation(map.values()).toString("avg", "MSE"));
        System.out.println("BalanceInt => " + map);
    }

    @Test
    public void testBalanceIntGuava() {
        ConsistentHash<String> cHash = new ConsistentHash<String>(ConsistentHash.GuavaHash.INSTANCE, 100);
        cHash.add("10.214.133.100:8087");
        cHash.add("10.214.133.100:8088");
        cHash.add("10.214.133.101:8087");
        cHash.add("10.214.133.101:8088");
        cHash.add("10.214.65.11:8087");
        cHash.add("10.214.65.11:8088");
        cHash.add("10.214.65.12:8087");
        cHash.add("10.214.65.12:8088");
        // Now we have four cache machines, each run two instances, a total of 8 instances.
        Map<String, Integer> map = Maps.newHashMap();
        for (int i = 110001; i < 190001; ++i) {
            Collection<String> nodeList = cHash.getNodeList(i);
            for (String key : nodeList) {
                Integer integer = map.get(key);
                if (integer == null) {
                    integer = 1;
                } else {
                    integer += 1;
                }
                map.put(key, integer);
            }
        }
        Pair<Integer,Double> pair = MathUtil.calcStandardDeviation(map.values());
        System.out.println("BalanceIntG => " + pair.toString("avg", "MSE") + ", PCT=" + (int)(pair.b / pair.a * 100) + "%");
        System.out.println("BalanceIntG => " + map);
    }

    @Test
    public void testBalanceString() {
        ConsistentHash<String> cHash = new ConsistentHash<String>();
        cHash.prepare("10.214.133.100:8087");
        cHash.prepare("10.214.133.100:8088");
        cHash.prepare("10.214.133.101:8087");
        cHash.prepare("10.214.133.101:8088");
        cHash.prepare("10.214.65.11:8087");
        cHash.prepare("10.214.65.11:8088");
        cHash.prepare("10.214.65.12:8087");
        cHash.prepare("10.214.65.12:8088");
        // Now we have four cache machines, each run two instances, a total of 8 instances.
        Map<String, Integer> map = Maps.newHashMap();
        for (int i = 10001; i < 90001; ++i) {
            Collection<String> nodeList = cHash.getNodeList(MockUtil.randUUID());
            for (String key : nodeList) {
                Integer integer = map.get(key);
                if (integer == null) {
                    integer = 1;
                } else {
                    integer += 1;
                }
                map.put(key, integer);
            }
        }
        Pair<Integer,Double> pair = MathUtil.calcStandardDeviation(map.values());
        System.out.println("Balances100 => " + pair.toString("avg", "MSE") + ", PCT=" + (int)(pair.b / pair.a * 100) + "%");
        System.out.println("Balances100 => " + map);
    }

    @Test
    public void testBalanceStringGuava() {
        ConsistentHash<String> cHash = new ConsistentHash<String>(ConsistentHash.GuavaHash.INSTANCE, 100);
        cHash.prepare("10.214.133.100:8087");
        cHash.prepare("10.214.133.100:8088");
        cHash.prepare("10.214.133.101:8087");
        cHash.prepare("10.214.133.101:8088");
        cHash.prepare("10.214.65.11:8087");
        cHash.prepare("10.214.65.11:8088");
        cHash.prepare("10.214.65.12:8087");
        cHash.prepare("10.214.65.12:8088");
        // Now we have four cache machines, each run two instances, a total of 8 instances.
        Map<String, Integer> map = Maps.newHashMap();
        for (int i = 10001; i < 90001; ++i) {
            Collection<String> nodeList = cHash.getNodeList(MockUtil.randUUID());
            for (String key : nodeList) {
                Integer integer = map.get(key);
                if (integer == null) {
                    integer = 1;
                } else {
                    integer += 1;
                }
                map.put(key, integer);
            }
        }
        Pair<Integer,Double> pair = MathUtil.calcStandardDeviation(map.values());
        System.out.println("Balances100G => " + pair.toString("avg", "MSE") + ", PCT=" + (int)(pair.b / pair.a * 100) + "%");
        System.out.println("Balances100G => " + map);
    }

    @Test
    public void testBalanceString500() {
        ConsistentHash<String> cHash = new ConsistentHash<String>(500);
        cHash.prepare("10.214.133.100:8087");
        cHash.prepare("10.214.133.100:8088");
        cHash.prepare("10.214.133.101:8087");
        cHash.prepare("10.214.133.101:8088");
        cHash.prepare("10.214.65.11:8087");
        cHash.prepare("10.214.65.11:8088");
        cHash.prepare("10.214.65.12:8087");
        cHash.prepare("10.214.65.12:8088");
        // Now we have four cache machines, each run two instances, a total of 8 instances.
        Map<String, Integer> map = Maps.newHashMap();
        for (int i = 10001; i < 90001; ++i) {
            Collection<String> nodeList = cHash.getNodeList(MockUtil.randUUID());
            for (String key : nodeList) {
                Integer integer = map.get(key);
                if (integer == null) {
                    integer = 1;
                } else {
                    integer += 1;
                }
                map.put(key, integer);
            }
        }
        Pair<Integer,Double> pair = MathUtil.calcStandardDeviation(map.values());
        System.out.println("Balances500 => " + pair.toString("avg", "MSE") + ", PCT=" + (int)(pair.b / pair.a * 100) + "%");
        System.out.println("Balances500 => " + map);
    }

}
