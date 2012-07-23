/**
 * BenchmarkTest.java
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
package org.apache.niolex.commons.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.Date;

import org.apache.niolex.commons.test.Benchmark.Bean;
import org.apache.niolex.commons.test.Benchmark.Group;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-7-23
 */
public class BenchmarkTest {

	Benchmark ben = Benchmark.makeBenchmark();

	/**
	 * Test method for {@link org.apache.niolex.commons.test.Benchmark#getClassId()}.
	 */
	@Test
	public void testGetClassId() {
		assertEquals("Not yet implemented", ben.getClassId(), 908123);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.Benchmark#setClassId(int)}.
	 */
	@Test
	public void testSetClassId() {
		ben.setClassId(12321);
		assertEquals("Not yet implemented", ben.getClassId(), 12321);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.Benchmark#setPersonId(long)}.
	 */
	@Test
	public void testSetPersonId() {
		ben.setPersonId(9082138904321l);
		assertEquals("Not yet implemented", ben.getPersonId(), 9082138904321l);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.Benchmark#setJoinId(long)}.
	 */
	@Test
	public void testSetJoinId() {
		ben.setJoinId(12321);
		assertEquals("Not yet implemented", ben.getJoinId(), 12321);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.Benchmark#getStatus()}.
	 */
	@Test
	public void testGetStatus() {
		assertEquals("Not yet implemented", ben.getStatus(), -1293);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.Benchmark#setStatus(int)}.
	 */
	@Test
	public void testSetStatus() {
		ben.setStatus(21312);
		assertEquals("Not yet implemented", ben.getStatus(), 21312);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.Benchmark#getPriv()}.
	 */
	@Test
	public void testGetPriv() {
		assertEquals("Not yet implemented", ben.getPriv(), 9128);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.Benchmark#setPriv(int)}.
	 */
	@Test
	public void testSetPriv() {
		ben.setPriv(90843);
		assertEquals("Not yet implemented", ben.getPriv(), 90843);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.Benchmark#getName()}.
	 */
	@Test
	public void testGetName() {
		assertEquals("Not yet implemented", ben.getName(), "This is the compress test benchmark.");
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.Benchmark#setName(java.lang.String)}.
	 */
	@Test
	public void testSetName() {
		ben.setName("Gook");
		assertEquals("Not yet implemented", ben.getName(), "Gook");
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.Benchmark#getClassName()}.
	 */
	@Test
	public void testGetClassName() {
		assertEquals("Not yet implemented", ben.getClassName(), "93209i;lads93209adfo0932awd");
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.Benchmark#setClassName(java.lang.String)}.
	 */
	@Test
	public void testSetClassName() {
		ben.setClassName("class.me");
		assertEquals("Not yet implemented", ben.getClassName(), "class.me");
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.Benchmark#isOk()}.
	 */
	@Test
	public void testIsOk() {
		assertEquals("Not yet implemented", ben.isOk(), true);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.Benchmark#setOk(boolean)}.
	 */
	@Test
	public void testSetOk() {
		ben.setOk(false);
		assertEquals("Not yet implemented", ben.isOk(), false);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.Benchmark#setCrDate(java.util.Date)}.
	 */
	@Test
	public void testSetCrDate() {
		ben.setCrDate(new Date(757583));
		assertEquals("Not yet implemented", ben.getCrDate().getTime(), 757583);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.Benchmark#setCurKick(long)}.
	 */
	@Test
	public void testSetCurKick() {
		ben.setCurKick(0);
		assertEquals("Not yet implemented", ben.getCurKick(), 0);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.Benchmark#getList()}.
	 */
	@Test
	public void testGetList() {
		assertEquals("Not yet implemented", ben.getList().size(), 22);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.test.Benchmark#equals(java.lang.Object)}.
	 */
	@Test
	public void testEqualsObject() {
		assertEquals("Not yet implemented", ben, ben);
		assertNotSame("Not yet implemented", ben, null);
		assertNotSame("Not yet implemented", ben, "dasf");
		Benchmark other = new Benchmark();
		other.setCurKick(ben.getCurKick());
		assertNotSame("Not yet implemented", ben, other);
		other.setPersonId(ben.getPersonId());
		assertEquals("Not yet implemented", ben, other);
	}

	@Test
	public void testGroup() {
		Group gr = Group.makeGroup();
		gr.setGroupName("");
		assertEquals("Not yet implemented", gr.getGroupName(), "");
		assertEquals("Not yet implemented", gr.getGroupStatus(), 1280934);
		gr.setGroupId(-1l);
		assertEquals("Not yet implemented", gr.getGroupId().longValue(), -1l);
		assertEquals("Not yet implemented", gr.getList().size(), 3);
	}

	@Test
	public void testBean() {
		Bean bean = new Bean(5, "G", 3, new Date(12345));
		assertEquals("Not yet implemented", bean.getId(), 3);
		assertEquals("Not yet implemented", bean.getLikely(), 5);
		assertEquals("Not yet implemented", bean.getName(), "G");
		assertEquals("Not yet implemented", bean.getBirth().getTime(), 12345);
	}
}
