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

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PipedOutputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.niolex.commons.seri.Proto.Person;
import org.apache.niolex.commons.seri.Proto.PhoneNumber;
import org.apache.niolex.commons.seri.Proto.PhoneType;
import org.apache.niolex.commons.test.Performance;
import org.apache.niolex.commons.util.MathUtil;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-8-7
 */
public class ProtoUtilTest extends ProtoUtil {

    int i = MathUtil.randInt(9999);
    Person p = Person.newBuilder().setEmail("kjdfjkdf" + i + "@xxx.com").setId(45 + i)
            .setName("Niolex [" + i + "]")
            .addPhone(PhoneNumber.newBuilder().setNumber("123122311" + i).setType(PhoneType.MOBILE).build())
            .build();

    byte[] single_p_data = p.toByteArray();

    @Test
    public void testSetUseFasterAccess() throws Exception {
        System.out.print("Slower ");
        setUseFasterAccess(false);
        clearMethodsCache();
        Performance pf = new Performance(3000, 100) {

            @Override
            protected void run() {
                Person e = parseOne(single_p_data, Person.class);
                assertEquals(e.getId(), 45 + i);
            }
        };
        pf.start();
        System.out.print("Faster ");
        setUseFasterAccess(true);
        clearMethodsCache();
        pf = new Performance(3000, 100) {

            @Override
            protected void run() {
                Person e = parseOne(single_p_data, Person.class);
                assertEquals(e.getId(), 45 + i);
            }
        };
        pf.start();
    }

	/**
	 * Test method for {@link org.apache.niolex.commons.seri.ProtoUtil#parseOne(byte[], java.lang.reflect.Type)}.
	 */
	@Test
	public void testParseOne() {
		byte[] ret = p.toByteArray();
		byte[] tar = seriOne(p);
		assertArrayEquals(ret, tar);
		Person p2 = (Person)parseOne(tar, Person.class);
		assertEquals(p2.getEmail(), "kjdfjkdf" + i + "@xxx.com");
		assertEquals(p2.getId(), 45 + i);
		assertEquals(p2.getName(), "Niolex [" + i + "]");
		System.out.println("single.Tar.size " + tar.length);
		Person p3 = (Person)parseOne(tar, Person.class);
		assertEquals(p, p3);
	}

	/**
     * Test method for {@link org.apache.niolex.commons.seri.ProtoUtil#parseOne(byte[], java.lang.reflect.Type)}.
     */
    @Test(expected=SeriException.class)
    public void testParseOneError() {
        parseOne(new byte[100], Person.class);
    }

    @Test(expected=SeriException.class)
    public void testParseDelimitedOne() throws Exception {
        setUseFasterAccess(true);
        clearMethodsCache();
        ByteArrayInputStream binput = new ByteArrayInputStream(single_p_data);
        parseDelimitedOne(binput, Person.class);
    }

	/**
	 * Test method for {@link org.apache.niolex.commons.seri.ProtoUtil#parseMulti(byte[], java.lang.reflect.Type[])}.
	 */
	@Test
	public void testParseMulti() {
	    setUseFasterAccess(false);
        clearMethodsCache();
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
		System.out.println("multi.Tar.size " + tar.length);
		Object[] r = parseMulti(tar, new Class<?>[] {Person.class, Person.class, Person.class});
		assertEquals(r[0], p1);
		assertEquals(r[1], p2);
		assertEquals(r[2], p3);
		assertEquals(((Person)r[1]).getId(), 45 + 6967);
	}

	@Test
	public void testSeriOne() {
	    byte[] ret = seriOne(p);
	    assertArrayEquals(ret, single_p_data);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.seri.ProtoUtil#seriOne(java.lang.Object)}.
	 */
	@Test(expected=SeriException.class)
	public void testSeriOneBad() {
		seriOne("Not yet implemented");
	}


    @Test(expected=SeriException.class)
    public void testSeriDelimitedOneBadClass() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
        seriDelimitedOne("This is good.", out);
    }

    @Test(expected=SeriException.class)
    public void testSeriDelimitedOneBadStream() throws Exception {
        PipedOutputStream oo = new PipedOutputStream();
        seriDelimitedOne(p, oo);
    }

	/**
	 * Test method for {@link org.apache.niolex.commons.seri.ProtoUtil#seriMulti(java.lang.Object[])}.
	 */
	@Test(expected=SeriException.class)
	public void testSeriMulti() {
	    setUseFasterAccess(true);
        clearMethodsCache();
		Set<String> set = new HashSet<String>();
		seriMulti(new Object[] {p, p, set});
	}

}
