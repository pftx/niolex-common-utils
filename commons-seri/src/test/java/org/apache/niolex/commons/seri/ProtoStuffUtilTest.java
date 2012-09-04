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

import static org.apache.niolex.commons.seri.ProtoStuffUtil.parseMulti;
import static org.apache.niolex.commons.seri.ProtoStuffUtil.parseOne;
import static org.apache.niolex.commons.seri.ProtoStuffUtil.seriMulti;
import static org.apache.niolex.commons.seri.ProtoStuffUtil.seriOne;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.niolex.commons.seri.PersonProtos.Person;
import org.apache.niolex.commons.seri.PersonProtos.Person.PhoneNumber;
import org.apache.niolex.commons.seri.PersonProtos.Person.PhoneType;
import org.apache.niolex.commons.test.Benchmark;
import org.apache.niolex.commons.test.Benchmark.Bean;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-8-7
 */
public class ProtoStuffUtilTest {

	/**
	 * Test method for {@link org.apache.niolex.commons.seri.ProtoStuffUtil#parseOne(byte[], java.lang.reflect.Type)}.
	 */
	@Test
	public void testParseOne() {
		ProtoStuffUtil pu = new ProtoStuffUtil();
		Bean t = new Bean(3, "Qute", 12212, new Date(1338008328709L));
		@SuppressWarnings("static-access")
		byte[] tar = pu.seriOne(t);
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
		Object[] r = parseMulti(tar, new Type[] {Bean.class, Benchmark.class, Bean.class});
		assertEquals(r[0], t);
		assertEquals(r[1], ben);
		assertEquals(r[2], q);
		assertEquals(((Benchmark)r[1]).getClassId(), 908123);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.seri.ProtoStuffUtil#seriOne(java.lang.Object)}.
	 */
	@Test(expected=SeriException.class)
	public void testSeriOne() {
		String s = "Not yet implemented";
		byte[] tar = seriOne(s);
		Set<String> set = new HashSet<String>();
		parseOne(tar, set.getClass().getGenericSuperclass());
	}

	@Test(expected=SeriException.class)
	public void thisIsGood() {
		int i = 2345;
		Person p = Person.newBuilder().setEmail("kjdfjkdf" + i + "@xxx.com").setId(45 + i)
				.setName("Niolex [" + i + "]")
				.addPhone(PhoneNumber.newBuilder().setNumber("123122311" + i).setType(PhoneType.MOBILE).build())
				.build();
		byte[] ret = p.toByteArray();
		Set<String> set = new HashSet<String>();
		parseMulti(ret, new Type[] {set.getClass().getGenericSuperclass(), Person.class});
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
