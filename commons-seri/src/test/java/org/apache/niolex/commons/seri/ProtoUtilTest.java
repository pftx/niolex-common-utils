/**
 * ProtoUtilTest.java
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

import static org.apache.niolex.commons.seri.ProtoUtil.parseMulti;
import static org.apache.niolex.commons.seri.ProtoUtil.parseOne;
import static org.apache.niolex.commons.seri.ProtoUtil.seriMulti;
import static org.apache.niolex.commons.seri.ProtoUtil.seriOne;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import org.apache.niolex.commons.seri.PersonProtos.Person;
import org.apache.niolex.commons.seri.PersonProtos.Person.PhoneNumber;
import org.apache.niolex.commons.seri.PersonProtos.Person.PhoneType;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-8-7
 */
public class ProtoUtilTest {

	/**
	 * Test method for {@link org.apache.niolex.commons.seri.ProtoUtil#parseOne(byte[], java.lang.reflect.Type)}.
	 */
	@Test
	public void testParseOne() {
		int i = 2345;
		Person p = Person.newBuilder().setEmail("kjdfjkdf" + i + "@xxx.com").setId(45 + i)
				.setName("Niolex [" + i + "]")
				.addPhone(PhoneNumber.newBuilder().setNumber("123122311" + i).setType(PhoneType.MOBILE).build())
				.build();
		byte[] ret = p.toByteArray();
		byte[] tar = seriOne(p);
		assertArrayEquals(ret, tar);
		Person p2 = (Person)parseOne(tar, Person.class);
		assertEquals(p2.getEmail(), "kjdfjkdf" + i + "@xxx.com");
		assertEquals(p2.getId(), 45 + i);
		System.out.println("Tar.size " + tar.length);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.seri.ProtoUtil#parseMulti(byte[], java.lang.reflect.Type[])}.
	 */
	@Test
	public void testParseMulti() {
		int i = 2345;
		Person p1 = Person.newBuilder().setEmail("kjdfjkdf" + i + "@xxx.com").setId(45 + i)
				.setName("Niolex [" + i + "]")
				.addPhone(PhoneNumber.newBuilder().setNumber("123122311" + i).setType(PhoneType.MOBILE).build())
				.build();
		i = 6967;
		Person p2 = Person.newBuilder().setEmail("kjdfjkdf" + i + "@xxx.com").setId(45 + i)
				.setName("Niolex [" + i + "]")
				.addPhone(PhoneNumber.newBuilder().setNumber("123122311" + i).setType(PhoneType.MOBILE).build())
				.build();
		i = 9484;
		Person p3 = Person.newBuilder().setEmail("kjdfjkdf" + i + "@xxx.com").setId(45 + i)
				.setName("Niolex [" + i + "]")
				.addPhone(PhoneNumber.newBuilder().setNumber("123122311" + i).setType(PhoneType.MOBILE).build())
				.build();
		byte[] tar = seriMulti(new Object[] {p1, p2, p3});
		System.out.println("Tar.size " + tar.length);
		Object[] r = parseMulti(tar, new Type[] {Person.class, Person.class, Person.class});
		assertEquals(r[0], p1);
		assertEquals(r[1], p2);
		assertEquals(r[2], p3);
		assertEquals(((Person)r[1]).getId(), 45 + 6967);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.seri.ProtoUtil#seriOne(java.lang.Object)}.
	 */
	@Test(expected=SeriException.class)
	public void testSeriOne() {
		seriOne("Not yet implemented");
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
		parseOne(ret, set.getClass().getGenericSuperclass());
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.seri.ProtoUtil#seriMulti(java.lang.Object[])}.
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
		parseOne(ret, set.getClass());
	}

}
