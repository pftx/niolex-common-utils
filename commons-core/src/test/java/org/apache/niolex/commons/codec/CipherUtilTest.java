/**
 * CipherUtilTest.java
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

import static org.junit.Assert.assertEquals;

import java.security.MessageDigest;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NullCipher;
import javax.crypto.ShortBufferException;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 *
 */
public class CipherUtilTest {

	/**
	 * Test method for {@link org.apache.niolex.commons.codec.CipherUtil#process(javax.crypto.Cipher, int, byte[])}.
	 * @throws ShortBufferException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 */
	@Test
	public final void testProcess() throws IllegalBlockSizeException, BadPaddingException, ShortBufferException {
		NullCipher nc = new NullCipher();
		byte[] input = "I am God.".getBytes();
		byte[] outup = CipherUtil.process(nc, 123, input);
		Assert.assertArrayEquals(input, outup);
	}

	@Test
    public final void testGetInstance() {
	    try {
	        CipherUtil.getInstance("SHA2");
	    } catch (IllegalStateException e) {
	        System.out.println(e.getMessage());
	        return;
	    }
	    Assert.assertTrue(false);
	}

    @Test
    public void testDigest() throws Exception {
        MessageDigest md = CipherUtil.getInstance("SHA1");
        CipherUtil.digest(md, new String[0]);
        CipherUtil.digest(md, new String[] {"abc", null, "cde"});
        assertEquals("2be34a726839515b64c04cf9397381d82c2cc010", Base16Util.byteToBase16(md.digest()));
    }

}
