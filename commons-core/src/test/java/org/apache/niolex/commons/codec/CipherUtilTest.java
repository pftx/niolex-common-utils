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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.security.MessageDigest;
import java.security.PrivateKey;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NullCipher;
import javax.crypto.ShortBufferException;

import org.apache.niolex.commons.test.MockUtil;
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

    @Test
    public void testProcessCBC() throws Exception {
        String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALWqzlFYBqhtrZ8bhTxpcGrZeLLf7IU5kqxCgyJw2vwu8haBSxYRjE8LX6pY8C3hDPd12czdmh55L2h3o/zAyO16vsL8gzJqPZ4CpLJuE9S6SL3wS11b3MzgMNhX9pn1QEMBVDj8A76I0cfBwYx8z526dTjvIN8uwx0l3tmXvj5NAgMBAAECgYBua0VAF/rkANYY8UdIcuYLa+d7AbPnPhkybrL6ChJwWbB3kVqsLTpVCRq6lZhWqoWRG6aoaME2aH4yRxX7mMoG1F7X1VxMzjVJkWUmPfU/dbn2Pb0b9/TevRnwT3vvf3u5yJR4aNPgaZJwfPhVlagqabZdasRoLh7/sgGUVkhggQJBAP6bvcCrlaeDcA+WyW+yvi6Mqn3gKfs7JvSEy0W1NKKuwecyINMzb0Tv/0AS0f6iBf241vOnu678z6CABF4/9t0CQQC2qQCa8XjiOu7PNvC5aVf51xF05AluHz8bXdEJfOaD5EJCmAfADqCkqYLLOVxuo9fP20z1t03fiYvXIpzgXRYxAkEA5aXD+UzKp1U+dlEjT8SBFat6/B58v0YTVOmSD0XqO/I0ozvrr5PtANkX+cr/7hRmIvvdpdfcyXDuNW5CgmBfYQJAEkwpsFvCFT98DqvdP2WLF47ww7nYK/zbUH18ZCvr14h1DsC1/go5E2WboYn0dWzaQIsiUXb0SRE5PerMtjj88QI/DfYJSATb49TVcpOD2fO+XXBnE817d+wHHMh+oR4Bvi/t+CBaWLQwJg350Ou9g+LQzduoW+e5ooBeS0dkETPP";

        byte[] data = MockUtil.randByteArray(200);
        PrivateKey privateKey2 = RSAUtil.getPrivateKey(privateKey);
        byte[] out1 = RSAUtil.encrypt(data, privateKey2);
        byte[] data2 = Arrays.copyOfRange(data, 117, 200);
        byte[] out2 = RSAUtil.encrypt(data2, privateKey2);
        byte[] out3 = Arrays.copyOfRange(out1, 128, 256);

        assertEquals(256, out1.length);
        assertArrayEquals(out3, out2);
    }
}
