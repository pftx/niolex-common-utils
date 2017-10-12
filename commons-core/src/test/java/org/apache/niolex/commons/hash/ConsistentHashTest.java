/**
 * ConsistentHashTest.java
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

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.niolex.commons.hash.ConsistentHash.GuavaHash;
import org.apache.niolex.commons.test.MockUtil;
import org.apache.niolex.commons.util.SystemUtil;
import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-5-19
 */
public class ConsistentHashTest {

    /**
     * Test the provided hash function for spread.
     *
     * @param func
     */
    protected void testFunc(ConsistentHash.HashFunction func) {
        String root = "test-hash";
        List<Integer> list = Lists.newArrayList(101);
        list.add(func.hashCode(root));
        for (int i = 0; i < 100; ++i) {
            list.add(func.hashCode(root, i));
        }
        Collections.sort(list);
        long inc = list.get(1) - list.get(0);
        long avg = 0, cur;
        for (int i = 2; i < 101; ++i) {
            avg = inc / (i - 1);
            cur = list.get(i) - list.get(i - 1);
            cur = cur > avg * 4 ? avg * 3 : cur;
            inc += cur;
        }
        System.out.println("Spread for [" + func.getClass().getSimpleName() + "] is - " + inc);
    }

    @Test
    public final void testJVMHash() {
        testFunc(ConsistentHash.JVMHash.INSTANCE);
    }

    @Test
    public final void testGuavaHash() {
        testFunc(ConsistentHash.GuavaHash.INSTANCE);
    }

    /**
     * Test method for {@link org.apache.niolex.commons.util.ConsistentHash#getNode(java.lang.Object)}.
     */
    @Test
    public final void testGetNodeJVM() {
        ConsistentHash<String> cHash = new ConsistentHash<String>();
        cHash.add("ididje3i840-92");
        cHash.add("e323dsfg45zad-");
        cHash.add("098023lasdalks");
        cHash.add("lads-m;sadna'd");
        cHash.add("32ja0f;qlkafkf");
        // ----------------------------------------
        String key = "This-First-Key";
        System.out.print("testGetNode--JVM\n\t ");
        for (int j = 0; j < 10; ++j) {
            String i = cHash.getNode(j + key + j + key);
            System.out.print(i.charAt(0) + " ");
        }
        String i = cHash.getNode("This-First-Va");
        assertEquals("e323dsfg45zad-", i);
        i = cHash.getNode("This-First");
        assertEquals("ididje3i840-92", i);
        i = cHash.getNode("xie-ji-yun");
        assertEquals("098023lasdalks", i);
        i = cHash.getNode("let-s-go");
        assertEquals("32ja0f;qlkafkf", i);
        SystemUtil.println("let's go => %s", i);
    }

    /**
     * Test method for {@link org.apache.niolex.commons.util.ConsistentHash#getNode(java.lang.Object)}.
     */
    @Test
    public final void testGetNodeGuava() {
        ConsistentHash<String> cHash = new ConsistentHash<String>(GuavaHash.INSTANCE, 5);
        cHash.add("ididje3i840-92");
        cHash.add("e323dsfg45zad-");
        cHash.add("098023lasdalks");
        cHash.add("lads-m;sadna'd");
        cHash.add("32ja0f;qlkafkf");
        // ----------------------------------------
        String key = "This-First-Key";
        System.out.print("testGetNode--Guava\n\t ");
        for (int j = 0; j < 10; ++j) {
            String i = cHash.getNode(j + key + j + key);
            System.out.print(i.charAt(0) + " ");
        }
        String i = cHash.getNode("This-First-Va");
        assertEquals("lads-m;sadna'd", i);
        i = cHash.getNode("This-First");
        assertEquals("098023lasdalks", i);
        i = cHash.getNode("xie-ji-yun");
        assertEquals("e323dsfg45zad-", i);
        i = cHash.getNode("let-s-go");
        assertEquals("ididje3i840-92", i);
        SystemUtil.println("let's go => %s", i);
    }

    /**
     * Test method for {@link org.apache.niolex.commons.util.ConsistentHash#getNodeList(java.lang.Object, int)}.
     */
    @Test
    public final void testGetNodeList() {
        ConsistentHash<String> cHash = new ConsistentHash<String>();
        cHash.add("ijd");
        cHash.add("ejf");
        cHash.add("jdf");
        cHash.add("3wl");
        cHash.add("9sd");

        Collection<String> nodeList = cHash.getNodeList("0-dafk30f", 3);
        assertEquals(nodeList.size(), 3);
        Iterator<String> iter = nodeList.iterator();
        assertEquals("jdf", iter.next());
        assertEquals("ejf", iter.next());
        assertEquals("3wl", iter.next());

        nodeList = cHash.getNodeList("0932ldio", 3);
        assertEquals(nodeList.size(), 3);
        iter = nodeList.iterator();
        assertEquals("ijd", iter.next());
        assertEquals("3wl", iter.next());
        assertEquals("9sd", iter.next());
        // Try get 2
        nodeList = cHash.getNodeList("0932ldio");
        assertEquals(nodeList.size(), 2);
        iter = nodeList.iterator();
        assertEquals("ijd", iter.next());
        assertEquals("3wl", iter.next());
    }

    @Test
    public void testAdd() throws Exception {
        ConsistentHash<String> cHash = new ConsistentHash<String>(GuavaHash.INSTANCE, 5,
                Arrays.asList("23", "ef", "pod"));
        cHash.prepare("9j", "de", "qa");
        Collection<String> nodeList = cHash.getNodeList("@#093naf", 3);
        assertEquals(nodeList.size(), 3);
        Iterator<String> iter = nodeList.iterator();
        assertEquals("pod", iter.next());
        assertEquals("9j", iter.next());
        assertEquals("23", iter.next());

        nodeList = cHash.getNodeList("32qrads", 6);
        assertEquals(nodeList.size(), 6);
        iter = nodeList.iterator();
        assertEquals("qa", iter.next());
        assertEquals("23", iter.next());
        assertEquals("pod", iter.next());
        assertEquals("9j", iter.next());
        assertEquals("ef", iter.next());
        assertEquals("de", iter.next());

        nodeList = cHash.getNodeList("lex-next", 6);
        assertEquals(nodeList.size(), 6);
        iter = nodeList.iterator();
        assertEquals("de", iter.next());
        assertEquals("qa", iter.next());
        assertEquals("pod", iter.next());
        assertEquals("9j", iter.next());
        assertEquals("23", iter.next());
        assertEquals("ef", iter.next());

        try {
            cHash.getNodeList("32qrads", 7);
            assertTrue(false);
        } catch (IllegalStateException e) {};
    }

    @Test
    public void testRemove() throws Exception {
        ConsistentHash<String> cHash = new ConsistentHash<String>(new ConsistentHash.HashFunction(){

            @Override
            public int hashCode(Object o) {
                return Integer.MAX_VALUE - o.toString().length();
            }

            @Override
            public int hashCode(Object o, int seed) {
                return Integer.MAX_VALUE - o.toString().length() * seed;
            }
        }, 5, Arrays.asList("2", "ef", "pod"));

        cHash.add("Nice");
        assertEquals("ef", cHash.getNode("3a3f"));
        cHash.remove("ef");
        assertEquals("Nice", cHash.getNode("3a3f"));
        cHash.remove("Nice");
        assertEquals("2", cHash.getNode("3a3f"));
        cHash.remove("2");
        assertEquals("pod", cHash.getNode("3a3f"));
        cHash.remove("pod");
        assertNull(cHash.getNode("3a3f"));
        assertEquals(0, cHash.getNodeList("3a3f").size());
    }

    @Test
    public void testConsistency() throws Exception {
        ConsistentHash<String> cHash = new ConsistentHash<String>();
        ConsistentHash<String> dHash = new ConsistentHash<String>();
        // --- prepare
        cHash.prepare("2", "ef", "pod", "nice", "might", "noodle");
        dHash.prepare("2", "ef", "pod", "nice", "might", "noodle");

        for (int i = 0; i < 2000; ++i) {
            String key = MockUtil.randString(8);
            String server1 = cHash.getNode(key);
            String server2 = dHash.getNode(key);
            assertEquals(server1, server2);
        }

        cHash.add("morning");
        dHash.add("morning");

        for (int i = 0; i < 2000; ++i) {
            String key = MockUtil.randString(9);
            String server1 = cHash.getNode(key);
            String server2 = dHash.getNode(key);
            assertEquals(server1, server2);
        }

        cHash.remove("2");
        dHash.remove("2");

        for (int i = 0; i < 2000; ++i) {
            String key = MockUtil.randString(7);
            String server1 = cHash.getNode(key);
            String server2 = dHash.getNode(key);
            assertEquals(server1, server2);
        }
    }

}
