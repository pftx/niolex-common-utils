/**
 * PathTest.java
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
package org.apache.niolex.commons.remote;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-7-26
 */
public class PathTest {

	/**
	 * Test method for {@link org.apache.niolex.commons.remote.Path#parsePath(java.lang.String)}.
	 */
	@Test
	public void testParsePath() {
		Path p = Path.parsePath("abc.edf.eee.dd...ddtail");
		System.out.println(p);
		assertEquals(".abc => .edf => .eee => .dd => .ddtail", p.toString());
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.remote.Path#parsePath(java.lang.String)}.
	 */
	@Test
	public void testParsePathSimpleField() {
		Path p = Path.parsePath("ggg");
		System.out.println(p);
		assertEquals(".ggg", p.toString());
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.remote.Path#parsePath(java.lang.String)}.
	 */
	@Test
	public void testParsePathSimpleMap() {
		Path p = Path.parsePath("ggg{abc.def}");
		System.out.println(p);
		assertEquals("ggg{abc.def}", p.toString());
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.remote.Path#parsePath(java.lang.String)}.
	 */
	@Test
	public void testParsePathSimpleArray() {
		Path p = Path.parsePath("ggg[123]");
		System.out.println(p);
		assertEquals("ggg[123]", p.toString());
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.remote.Path#getType()}.
	 */
	@Test
	public void testGetType() {
		Path p = Path.parsePath("abc.good{this.is.map}.make[123].another{me}");
		System.out.println(p);
		assertEquals(".abc => good{this.is.map} => make[123] => another{me}", p.toString());
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.remote.Path#getName()}.
	 */
	@Test
	public void testGetName() {
		Path p = Path.parsePath("abc.good{this.[is].map}.make[123].another{m}e}");
		System.out.println(p);
		assertEquals("Invalid Path at abc.good{this.[is].map}.make[123].another{m}e => .abc => good{this.[is].map} => make[123] => another{m}", p.toString());
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.remote.Path#getKey()}.
	 */
	@Test
	public void testGetKey() {
		Path p = Path.parsePath("arr[123][456]");
		System.out.println(p);
		assertEquals("Invalid Path at arr[123][456 => arr[123]", p.toString());
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.remote.Path#getIdx()}.
	 */
	@Test
	public void testGetIdx() {
		Path p = Path.parsePath("arr[123]goodMap{abc}{dbc}");
		System.out.println(p);
		assertEquals("Invalid Path at arr[123]goodMap{abc}{dbc => arr[123] => goodMap{abc}", p.toString());
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.remote.Path#next()}.
	 */
	@Test
	public void testNext() {
		Path p = Path.parsePath("arr[123]goodMap{abc}.{dbc}");
		System.out.println(p);
		assertEquals("Invalid Path at arr[123]goodMap{abc}.{dbc => arr[123] => goodMap{abc}", p.toString());
	}

	@Test
	public void testMakePath()
	 throws Exception {
		Path p = Path.parsePath("benc[g]");
		System.out.println(p);
		assertEquals("Invalid Path at benc[g", p.toString());
	}

	@Test
	public void testMakePath2()
			throws Exception {
		Path p = Path.parsePath("benc{ggg]");
		System.out.println(p);
		assertEquals("Invalid Path at benc{ggg", p.toString());
	}

	@Test
	public void testMakePath3()
			throws Exception {
		Path p = Path.parsePath("benc[goodM");
		System.out.println(p);
		assertEquals("Invalid Path at benc[goodM", p.toString());
	}

}
