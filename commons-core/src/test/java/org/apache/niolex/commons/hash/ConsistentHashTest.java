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

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.apache.niolex.commons.hash.ConsistentHash.GuavaHash;
import org.apache.niolex.commons.test.MockUtil;
import org.apache.niolex.commons.util.SystemUtil;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-5-19
 */
public class ConsistentHashTest {

    /**
     * Test method for {@link org.apache.niolex.commons.util.ConsistentHash#getNode(java.lang.Object)}.
     */
    @Test
    public final void testGetNode() {
        ConsistentHash<String> cHash = new ConsistentHash<String>();
        cHash.add("ididje3i840-92");
        cHash.add("e323dsfg45zad-");
        cHash.add("098023lasdalks");
        cHash.add("lads-m;sadna'd");
        cHash.add("32ja0f;qlkafkf");
        // ----------------------------------------
        String key = "This-First-Key";
        System.out.print("testGetNode  JVM ");
        for (int j = 0; j < 10; ++j) {
            String i = cHash.getNode(j + key + j + key);
            System.out.print(i.charAt(0) + " ");
        }
        String i = cHash.getNode("This-First-Va");
        SystemUtil.println("This-First-Va => %s", i);
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
        System.out.print("testGetNodeGuava ");
        String key = "This-First-Key";
        for (int j = 0; j < 10; ++j) {
            String i = cHash.getNode(j + key + j + key);
            System.out.print(i.charAt(0) + " ");
        }
        String i = cHash.getNode("This-First-Va");
        System.out.println("This-First-Va => " + i);
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
        System.out.print("0-dafk30f =>" + nodeList);
        assertEquals(nodeList.size(), 3);

        nodeList = cHash.getNodeList("0932ldio", 3);
        System.out.println("\t0932ldio =>" + nodeList);
        assertEquals(nodeList.size(), 3);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testAdd() throws Exception {
        ConsistentHash<String> cHash = new ConsistentHash<String>(GuavaHash.INSTANCE, 5, Arrays.asList("23", "ef", "pod"));
        cHash.prepare("9j", "de", "qa");
        Collection<String> nodeList = cHash.getNodeList("@#093naf", 3);
        System.out.println("ef, qa, pod] => " + nodeList);
        cHash.getNodeList("32qrads", 7);
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
        System.out.print("3a3f => " + cHash.getNode("3a3f"));
        cHash.remove("ef");
        System.out.print(" 3a3f => " + cHash.getNode("3a3f"));
        cHash.remove("Nice");
        System.out.print(" 3a3f => " + cHash.getNode("3a3f"));
        cHash.remove("2");
        System.out.print(" 3a3f => " + cHash.getNode("3a3f"));
        cHash.remove("pod");
        System.out.print(" 3a3f => " + cHash.getNode("3a3f"));
        System.out.println(" 3a3f => " + cHash.getNodeList("3a3f"));
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
            String server2 = cHash.getNode(key);
            assertEquals(server1, server2);
        }

        cHash.add("morning");
        dHash.add("morning");

        for (int i = 0; i < 2000; ++i) {
            String key = MockUtil.randString(9);
            String server1 = cHash.getNode(key);
            String server2 = cHash.getNode(key);
            assertEquals(server1, server2);
        }

        cHash.remove("2");
        dHash.remove("2");

        for (int i = 0; i < 2000; ++i) {
            String key = MockUtil.randString(7);
            String server1 = cHash.getNode(key);
            String server2 = cHash.getNode(key);
            assertEquals(server1, server2);
        }
    }

}
