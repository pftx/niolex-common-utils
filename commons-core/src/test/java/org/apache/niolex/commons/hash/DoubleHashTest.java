/**
 * DoubleHashTest.java
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

import org.apache.niolex.commons.bean.Pair;
import org.junit.Test;

import com.google.common.hash.Funnel;
import com.google.common.hash.Hashing;
import com.google.common.hash.PrimitiveSink;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-6-3
 */
public class DoubleHashTest {

    @Test
    public void testDoubleHash() throws Exception {
        Collection<String> nodeList = Arrays.asList("10.214.133.100:8087", "10.214.133.100:8088", "10.214.65.11:8087", "10.214.65.11:8088");
        DoubleHash<String> dHash = new DoubleHash<String>(Hashing.murmur3_128(), Hashing.crc32(), nodeList);
        Pair<String,String> pair;
        pair = dHash.getPairNodes(33);
        assertEquals("{a=10.214.65.11:8088, b=10.214.133.100:8087}", pair.toString());
        pair = dHash.getPairNodes(34);
        assertEquals("{a=10.214.133.100:8088, b=10.214.65.11:8087}", pair.toString());
        pair = dHash.getPairNodes(35);
        assertEquals("{a=10.214.65.11:8087, b=10.214.65.11:8088}", pair.toString());
        pair = dHash.getPairNodes(36);
        assertEquals("{a=10.214.65.11:8087, b=10.214.133.100:8087}", pair.toString());
        // -- add a new node
        dHash.add("10.214.65.12:8088");
        pair = dHash.getPairNodes(33);
        assertEquals("{a=10.214.65.11:8088, b=10.214.133.100:8087}", pair.toString());
        pair = dHash.getPairNodes(34);
        assertEquals("{a=10.214.133.100:8088, b=10.214.65.12:8088}", pair.toString());//*
        pair = dHash.getPairNodes(35);
        assertEquals("{a=10.214.65.11:8087, b=10.214.65.11:8088}", pair.toString());
        pair = dHash.getPairNodes(36);
        assertEquals("{a=10.214.65.12:8088, b=10.214.65.11:8088}", pair.toString());//*
        // -- remove the added node
        dHash.remove("10.214.65.12:8088");
        pair = dHash.getPairNodes(33);
        assertEquals("{a=10.214.65.11:8088, b=10.214.133.100:8087}", pair.toString());
        pair = dHash.getPairNodes(34);
        assertEquals("{a=10.214.133.100:8088, b=10.214.65.11:8087}", pair.toString());
        pair = dHash.getPairNodes(35);
        assertEquals("{a=10.214.65.11:8087, b=10.214.65.11:8088}", pair.toString());
        pair = dHash.getPairNodes(36);
        assertEquals("{a=10.214.65.11:8087, b=10.214.133.100:8087}", pair.toString());
    }

    @Test
    public void testAdd() throws Exception {
        Collection<String> nodeList = Arrays.asList("10.214.133.100:8087", "10.214.133.100:8088", "10.214.65.11:8087", "10.214.65.11:8088");
        DoubleHash<String> dHash = new DoubleHash<String>(Hashing.murmur3_128(), Hashing.crc32(), nodeList);
        dHash.add("not yet implemented");
        assertEquals(5, dHash.size());
        dHash.remove("not yet implemented");
        assertEquals(4, dHash.size());
        dHash.remove("10.214.133.100:8087");
        assertEquals(3, dHash.size());
        Pair<String,String> pair;
        pair = dHash.getPairNodes(36);

        assertEquals("{a=10.214.65.11:8087, b=10.214.65.11:8088}", pair.toString());
    }

    @Test
    public void testRemove() throws Exception {
        Collection<String> nodeList = Arrays.asList("10.214.133.100:8087", "10.214.133.100:8088", "10.214.65.11:8087", "10.214.65.11:8088");
        DoubleHash<String> dHash = new DoubleHash<String>(Hashing.murmur3_128(), Hashing.crc32(), nodeList);
        assertEquals(4, dHash.size());
        dHash.remove("not yet implemented");
        assertEquals(4, dHash.size());
        dHash.remove("10.214.133.100:8087");
        assertEquals(3, dHash.size());
    }

    @Test
    public void testGetPairNodesLong() throws Exception {
        Collection<String> nodeList = Arrays.asList("10.214.133.100:8087", "10.214.133.100:8088", "10.214.65.11:8087", "10.214.65.11:8088");
        DoubleHash<String> dHash = new DoubleHash<String>(Hashing.murmur3_128(), Hashing.crc32(), nodeList);
        Pair<String,String> pair;
        pair = dHash.getPairNodes(90437895783433l);
        assertEquals("{a=10.214.133.100:8088, b=10.214.65.11:8087}", pair.toString());
        pair = dHash.getPairNodes(90437895783434l);
        assertEquals("{a=10.214.65.11:8087, b=10.214.133.100:8087}", pair.toString());
        pair = dHash.getPairNodes(90437895783435l);
        assertEquals("{a=10.214.65.11:8087, b=10.214.133.100:8088}", pair.toString());
        pair = dHash.getPairNodes(90437895783436l);
        assertEquals("{a=10.214.65.11:8088, b=10.214.133.100:8087}", pair.toString());
    }

    @Test
    public void testGetPairNodesString() throws Exception {
        Collection<String> nodeList = Arrays.asList("10.214.133.100:8087", "10.214.133.100:8088", "10.214.65.11:8087", "10.214.65.11:8088");
        DoubleHash<String> dHash = new DoubleHash<String>(Hashing.murmur3_128(), Hashing.crc32(), nodeList);
        Pair<String,String> pair;
        pair = dHash.getPairNodes("adsfiekfjie");
        assertEquals("{a=10.214.133.100:8088, b=10.214.133.100:8087}", pair.toString());
        pair = dHash.getPairNodes("adsfiekfjif");
        assertEquals("{a=10.214.65.11:8088, b=10.214.65.11:8087}", pair.toString());
        pair = dHash.getPairNodes("adsfiekfjig");
        assertEquals("{a=10.214.65.11:8088, b=10.214.133.100:8087}", pair.toString());
        pair = dHash.getPairNodes("adsfiekfjih");
        assertEquals("{a=10.214.133.100:8087, b=10.214.65.11:8088}", pair.toString());
    }

    @Test
    public void testSize() throws Exception {
        Collection<String> nodeList = Arrays.asList("10.214.133.100:8087", "10.214.133.100:8088", "10.214.65.11:8087", "10.214.65.11:8088");
        DoubleHash<String> dHash = new DoubleHash<String>(Hashing.murmur3_128(), Hashing.crc32(), nodeList);
        Pair<String,String> pair;
        pair = dHash.getPairNodes(new Person(12315, "Lex", "Xie", 1988), PersonFunnel.INSTANCE);
        System.out.println(pair);
        assertEquals("{a=10.214.65.11:8088, b=10.214.65.11:8087}", pair.toString());
        pair = dHash.getPairNodes(new Person(12316, "Lex", "Xie", 1988), PersonFunnel.INSTANCE);
        System.out.println(pair);
        assertEquals("{a=10.214.133.100:8088, b=10.214.65.11:8088}", pair.toString());
        pair = dHash.getPairNodes(new Person(12317, "Lex", "Xie", 1988), PersonFunnel.INSTANCE);
        System.out.println(pair);
        assertEquals("{a=10.214.65.11:8088, b=10.214.133.100:8088}", pair.toString());
        pair = dHash.getPairNodes(new Person(12318, "Lex", "Xie", 1988), PersonFunnel.INSTANCE);
        System.out.println(pair);
        assertEquals("{a=10.214.133.100:8087, b=10.214.65.11:8088}", pair.toString());
    }

    public static class Person {
        final int id;
        final String firstName;
        final String lastName;
        final int birthYear;

        public Person(int id, String firstName, String lastName, int birthYear) {
            super();
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.birthYear = birthYear;
        }

        public int getId() {
            return this.id;
        }

        public String getFirstName() {
            return this.firstName;
        }

        public String getLastName() {
            return this.lastName;
        }

        public int getBirthYear() {
            return this.birthYear;
        }

    }

    public static class PersonFunnel implements Funnel<Person> {

        /**
         * Gen
         */
        private static final long serialVersionUID = -8662033253118318877L;
        private static final PersonFunnel INSTANCE = new PersonFunnel();

        /**
         * This is the override of super method.
         *
         * @see com.google.common.hash.Funnel#funnel(java.lang.Object, com.google.common.hash.PrimitiveSink)
         */
        @Override
        public void funnel(Person from, PrimitiveSink into) {
            into.putInt(from.id).putString(from.firstName).putChar('&').putString(from.lastName).putInt(from.birthYear);
        }

    }

}
