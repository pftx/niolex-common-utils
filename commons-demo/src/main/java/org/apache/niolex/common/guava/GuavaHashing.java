/**
 * GuavaHashing.java
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

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.hash.PrimitiveSink;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-5-10
 */
public class GuavaHashing {

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

    /**
     * @param args
     */
    public static void main(String[] args) {
        // Common Hash
        HashFunction hf = Hashing.goodFastHash(32);
        HashCode code = hf.hashObject(new Person(1, "Jiyun", "Xie", 1984), PersonFunnel.INSTANCE);
        System.out.println("Code1 => " + code.asInt());
        code = hf.hashObject(new Person(1, "Jiyun", "Xie", 1985), PersonFunnel.INSTANCE);
        System.out.println("Code2 => " + code.asInt());
        // Consistent Hashing
        HashFunction hf2 = Hashing.goodFastHash(64);
        code = hf2.hashObject(new Person(1, "Jiyun", "Xie", 1984), PersonFunnel.INSTANCE);
        System.out.println("Code3 => " + code.asLong());
        long hash = code.asLong();
        int bucket = Hashing.consistentHash(code, 100);
        System.out.println("Bucket1 => " + bucket);
        bucket = Hashing.consistentHash(hash, 101);
        System.out.println("Bucket2 => " + bucket);
        for (int i = 0; i < 10; ++i) {
            System.out.println("HashTo5 => " + Hashing.consistentHash(i, 5));
            System.out.println("HashTo6 => " + Hashing.consistentHash(i, 6));
        }
        // BloomFilter
        BloomFilter<Person> friends = BloomFilter.create(PersonFunnel.INSTANCE, 500, 0.02);
        for (int i = 0; i < 500; ++i) {
            friends.put(new Person(i, "Jiyun", "Xie", 1984 + i));
        }
        int k = 0;
        for (int i = 0; i < 500; ++i) {
            if (!friends.mightContain(new Person(i, "Jiyun", "Xie", 1984 + i))) {
                System.out.println("Error1 => " + i);
                ++k;
            }
        }
        System.out.println("fnp => (should be 0)" + ((double)k / 500));
        k = 0;
        for (int i = 0; i < 1000; i += 2) {
            if (friends.mightContain(new Person(i, "Jiyun", "Xie", 1984 + i))) {
                //System.out.println("Error2 => " + i);
                ++k;
            }
        }
        System.out.println("fpp => " + ((double)k / 500));
    }

}
