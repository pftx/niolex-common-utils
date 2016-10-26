/**
 * BaseCoderTest.java
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
package org.apache.niolex.commons.coder;

import static org.junit.Assert.assertEquals;

import java.nio.BufferOverflowException;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 *
 */
public class BaseCoderTest {

	private static BaseCoder coder;

	public static class Mock extends BaseCoder {

		@Override
		public void initKey(String key) {
		}

		@Override
        public byte[] internalEncrypt(byte[] data) throws Exception {
			return data;
		}

		@Override
        public byte[] internalDecrypt(byte[] data) throws Exception {
			return data;
		}

	}

    public static class Mock2 extends BaseCoder {

        @Override
        public void initKey(String key) {
        }

        @Override
        public byte[] internalEncrypt(byte[] data) throws Exception {
            throw new BufferOverflowException();
        }

        @Override
        public byte[] internalDecrypt(byte[] data) throws Exception {
            throw new BufferOverflowException();
        }

    }

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		coder = new Mock();
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.coder.BaseCoder#decode(java.lang.String)}.
	 */
	@Test
	public final void testDecode() throws Exception {
		String dec = coder.decode("6Iux5Zu9QVRN6ZSZ5ZCQ5Y+M5YCN546w6YeRIOmTtuihjOensOmUmeWcqOiHquW3seS4jeW/heW9kui/mCA=");
		assertEquals(dec, "英国ATM错吐双倍现金 银行称错在自己不必归还 ");
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.coder.BaseCoder#encode(java.lang.String)}.
	 */
	@Test
	public final void testEncode() throws Exception {
		String enc = coder.encode("英国ATM错吐双倍现金 银行称错在自己不必归还");
		System.out.println("enc => " + enc);
		assertEquals("6Iux5Zu9QVRN6ZSZ5ZCQ5Y+M5YCN546w6YeRIOmTtuihjOensOmUmeWcqOiHquW3seS4jeW/heW9kui/mA==",
				enc);
	}

    @Test(expected = BufferOverflowException.class)
    public void testEncrypt() throws Exception {
        BaseCoder coder = new Mock2();
        coder.encode("not yet implemented");
    }

    @Test(expected = BufferOverflowException.class)
    public void testDecrypt() throws Exception {
        BaseCoder coder = new Mock2();
        coder.decode("notyetimplemented");
    }

}
