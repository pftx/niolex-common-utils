/**
 * ProtoStuffUtilTest.java
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
package org.apache.niolex.commons.seri;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.niolex.commons.bean.Pair;
import org.apache.niolex.commons.reflect.MethodUtil;
import org.apache.niolex.commons.seri.Proto.Person;
import org.apache.niolex.commons.seri.Proto.PhoneNumber;
import org.apache.niolex.commons.seri.Proto.PhoneType;
import org.apache.niolex.commons.test.Benchmark;
import org.apache.niolex.commons.test.Benchmark.Bean;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-8-7
 */
public class ProtoStuffUtilTest extends ProtoStuffUtil {

	/**
	 * Test method for {@link org.apache.niolex.commons.seri.ProtoStuffUtil#parseOne(byte[], java.lang.reflect.Type)}.
	 */
	@Test
	public void testParseOne() {
		Bean t = new Bean(3, "Qute", 12212, new Date(1338008328709L));
		byte[] tar = seriOne(t);
		Bean p2 = (Bean)parseOne(tar, Bean.class);
		assertEquals(p2.getBirth(), new Date(1338008328709L));
		assertEquals(p2.getId(), 12212);
		System.out.println("Tar.size " + tar.length);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.seri.ProtoStuffUtil#parseMulti(byte[], java.lang.reflect.Type[])}.
	 */
	@Test
	public void testParseMulti() {
		Bean t = new Bean(3, "Qute", 12212, new Date(1338008328709L));
		Bean q = new Bean(5, "Another", 523212, new Date(1338008328334L));
		Benchmark ben = Benchmark.makeBenchmark();
		byte[] tar = seriMulti(new Object[] {t, ben, q});
		System.out.println("Tar.size " + tar.length);
		@SuppressWarnings("unchecked")
        Object[] r = parseMulti(tar, new Class[] {Bean.class, Benchmark.class, Bean.class});
		assertEquals(r[0], t);
		assertEquals(r[1], ben);
		assertEquals(r[2], q);
		assertEquals(((Benchmark)r[1]).getName(), "This is the compress test benchmark.");
	}

	public void method1(Pair<String, Set<String>> set) {}

	/**
	 * Test method for {@link org.apache.niolex.commons.seri.ProtoStuffUtil#seriOne(java.lang.Object)}.
	 */
	@Test
	public void testSeriOneSet() {
		Set<String> set = new HashSet<String>();
		set.add("abc");
		set.add("cba");
		Pair<String, Set<String>> p1 = new Pair<String, Set<String>>("abc", set);
		byte[] tar = seriOne(p1);
		Method m = MethodUtil.getFirstMethod(this, "method1");
		@SuppressWarnings("unchecked")
        Pair<String, Set<String>> p2 = (Pair<String, Set<String>>) parseOne(tar, m.getParameterTypes()[0]);
		assertEquals(p1.a, p2.a);
		assertEquals(p1.b, p2.b);
		System.out.println(p2.b);
	}

	public void method2(Pair<String, List<String>> set) {}

	/**
	 * Test method for {@link org.apache.niolex.commons.seri.ProtoStuffUtil#seriOne(java.lang.Object)}.
	 */
	@Test
	public void testSeriOneList() {
		List<String> set = new ArrayList<String>();
		set.add("abc");
		set.add("cba");
		Pair<String, List<String>> p1 = new Pair<String, List<String>>("abc", set);
		byte[] tar = seriOne(p1);
		Method m = MethodUtil.getFirstMethod(this, "method2");
		@SuppressWarnings("unchecked")
		Pair<String, List<String>> p2 = (Pair<String, List<String>>) parseOne(tar, m.getParameterTypes()[0]);
		assertEquals(p1.a, p2.a);
		assertEquals(p1.b, p2.b);
		System.out.println(p2.b);
	}

	public void method3(Pair<String, Map<String, Integer>> set) {}

	/**
	 * Test method for {@link org.apache.niolex.commons.seri.ProtoStuffUtil#seriOne(java.lang.Object)}.
	 */
	@Test
	public void testSeriOneMap() {
		Map<String, Integer> set = new HashMap<String, Integer>();
		set.put("abc", 3);
		set.put("cba", 5);
		Pair<String, Map<String, Integer>> p1 = new Pair<String, Map<String, Integer>>("abc", set);
		byte[] tar = seriOne(p1);
		Method m = MethodUtil.getFirstMethod(this, "method3");
		@SuppressWarnings("unchecked")
		Pair<String, Map<String, Integer>> p2 = (Pair<String, Map<String, Integer>>) parseOne(tar, m.getParameterTypes()[0]);
		assertEquals(p1.a, p2.a);
		assertEquals(p1.b, p2.b);
		System.out.println(p2.b);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.seri.ProtoStuffUtil#seriOne(java.lang.Object)}.
	 */
	@Test
	public void testSeriOneInt() {
		Integer p1 = 3344;
		byte[] tar = seriOne(p1);
		Integer p2 = parseOne(tar, Integer.class);
		assertEquals(p1, p2);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.seri.ProtoStuffUtil#seriMulti(java.lang.Object[])}.
	 */
	@Test(expected=SeriException.class)
	public void testSeriMulti() {
		int i = 2345;
		Person p = Person.newBuilder().setEmail("kjdfjkdf" + i + "@xxx.com").setId(45 + i)
				.setName("Niolex [" + i + "]")
				.addPhone(PhoneNumber.newBuilder().setNumber("123122311" + i).setType(PhoneType.MOBILE).build())
				.build();
		byte[] ret = p.toByteArray();
		Set<String> set = new HashSet<String>();
		seriMulti(new Object[] {ret, set.getClass(), null});
	}

}
