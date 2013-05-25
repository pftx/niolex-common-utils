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
package org.apache.niolex.commons.util;

import static org.junit.Assert.*;

import java.util.Collection;

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
        System.out.println(nodeList);

        nodeList = cHash.getNodeList("30-8ad;klajfdo", 3);
        System.out.println(nodeList);
        assertEquals(nodeList.size(), 3);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testAdd()
     throws Exception {
        ConsistentHash<String> cHash = new ConsistentHash<String>(5);
        cHash.add("2309");
        cHash.add("098df");
        cHash.add("29fda");
        cHash.getNodeList("0-dafk30f", 4);
    }

}
