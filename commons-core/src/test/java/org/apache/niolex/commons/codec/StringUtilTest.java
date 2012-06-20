/**
 * StringUtilTest.java
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
package org.apache.niolex.commons.codec;

import static org.junit.Assert.*;

import java.nio.charset.Charset;

import org.apache.niolex.commons.test.MockUtil;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-6-20
 */
public class StringUtilTest {

	/**
	 * Test method for {@link org.apache.niolex.commons.codec.StringUtil#utf8ByteToStr(byte[])}.
	 */
	@Test
	public void testUtf8ByteToStr() {
		byte[] a = MockUtil.randByteArray(16);
		String s = StringUtil.asciiByteToStr(a);
		System.out.println(s);
		for (Charset c : Charset.availableCharsets().values()) {
			System.out.println(c.name() + " " + c.aliases());
		}
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.codec.StringUtil#strToUTF8Byte(java.lang.String)}.
	 */
	@Test
	public void testStrToUTF8Byte() {
		String s = "初始化密钥,产生1024bit的密钥对";
		byte[] b = StringUtil.strToUTF8Byte(s);
		String c = StringUtil.utf8ByteToStr(b);
		System.out.println(c);
		assertEquals(c, s);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.codec.StringUtil#asciiByteToStr(byte[])}.
	 */
	@Test
	public void testAsciiByteToStr() {
		String s = "初始化密钥,产生1024bit的密钥对";
		byte[] b = StringUtil.strToASCIIByte(s);
		String c = StringUtil.utf8ByteToStr(b);
		System.out.println(c);
		assertNotSame(c, s);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.codec.StringUtil#strToASCIIByte(java.lang.String)}.
	 */
	@Test
	public void testStrToASCIIByte() {
		String s = "Randomly reorder the int array, with all data stay the same.";
		byte[] b = StringUtil.strToUTF8Byte(s);
		String c = StringUtil.utf8ByteToStr(b);
		System.out.println(c);
		assertEquals(c, s);
	}

}
