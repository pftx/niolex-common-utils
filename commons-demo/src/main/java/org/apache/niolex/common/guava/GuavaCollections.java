/**
 * GuavaCollections.java
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
package org.apache.niolex.common.guava;

import java.util.List;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import com.google.common.primitives.Ints;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-4-23
 */
public class GuavaCollections {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Multiset<String> wordsMultiset = HashMultiset.create();
        wordsMultiset.add("abc");
        wordsMultiset.add("abc");
        wordsMultiset.add("abcd");
        System.out.println("count => " + wordsMultiset.count("abc"));
        System.out.println("count => " + wordsMultiset.count("abcd"));

        BiMap<String, String> biMap = HashBiMap.create();
        biMap.put("good", "morning");
        biMap.put("bad", "afternoon");
        System.out.println("good => " + biMap.get("good"));
        System.out.println("afternoon => " + biMap.inverse().get("afternoon"));

        RangeMap<Integer, String> rangeMap = TreeRangeMap.create();
        rangeMap.put(Range.closed(1, 11), "Nice");
        rangeMap.put(Range.openClosed(11, 15), "Girl");
        System.out.println("11 => " + rangeMap.get(11));
        System.out.println("12 => " + rangeMap.get(12));
        System.out.println("15 => " + rangeMap.get(15));
        System.out.println("16 => " + rangeMap.get(16));

        List<Integer> countUp = Ints.asList(1, 2, 3, 4, 5);
        List<Integer> countDown = Lists.reverse(countUp); // {5, 4, 3, 2, 1}
        System.out.println("countUp => " + countUp);
        System.out.println("countDown => " + countDown);
    }

}
