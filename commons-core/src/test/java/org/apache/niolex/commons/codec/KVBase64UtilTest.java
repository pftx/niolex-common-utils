/**
 * KVBase64UtilTest.java
 *
 * Copyright 2012 The original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.niolex.commons.codec;

import static org.junit.Assert.*;

import org.apache.niolex.commons.bean.Pair;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2012-12-27
 */
public class KVBase64UtilTest {

    /**
     * Test method for {@link org.apache.niolex.commons.codec.KVBase64Util#kvToBase64(byte[], byte[])}.
     */
    @Test
    public void testKvToBase64() {
        byte[] key = "I am the key.".getBytes();
        byte[] value = "The mobile landscape today is all but monopolized by WebKit".getBytes();
        String s = KVBase64Util.kvToBase64(key, value);
        System.out.println(s);
        Pair<byte[], byte[]> p = KVBase64Util.base64toKV(s);
        assertArrayEquals(key, p.a);
        assertArrayEquals(value, p.b);
    }

    /**
     * Test method for {@link org.apache.niolex.commons.codec.KVBase64Util#kvToBase64(byte[], byte[])}.
     */
    @Test
    public void testKvToBase64_CHS() {
        byte[] key = "末日已然玩蛋去,就让我们与其哭死,不如笑死吧".getBytes();
        byte[] value = "一个阳光2B青年,一个屌丝青年,一场床戏使@黄渤 感叹@王宝强 不愧是练过的!".getBytes();
        String s = KVBase64Util.kvToBase64(key, value);
        System.out.println(s);
        Pair<byte[], byte[]> p = KVBase64Util.base64toKV(s);
        assertArrayEquals(key, p.a);
        assertArrayEquals(value, p.b);
    }

    /**
     * Test method for {@link org.apache.niolex.commons.codec.KVBase64Util#base64toKV(java.lang.String)}.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testBase64toKVErrNull_1() {
        byte[] value = new byte[1];
        KVBase64Util.kvToBase64(null, value);
    }

    /**
     * Test method for {@link org.apache.niolex.commons.codec.KVBase64Util#base64toKV(java.lang.String)}.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testBase64toKVErrNull_2() {
        byte[] value = new byte[1];
        KVBase64Util.kvToBase64(value, null);
    }

    /**
     * Test method for {@link org.apache.niolex.commons.codec.KVBase64Util#base64toKV(java.lang.String)}.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testBase64toKVErrNull_3() {
        KVBase64Util.kvToBase64(null, null);
    }

    /**
     * Test method for {@link org.apache.niolex.commons.codec.KVBase64Util#base64toKV(java.lang.String)}.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testBase64toKVErrNull_4() {
        KVBase64Util.base64toKV(null);
    }

    /**
     * Test method for {@link org.apache.niolex.commons.codec.KVBase64Util#base64toKV(java.lang.String)}.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testBase64toKVErrSize_1() {
        byte[] key = new byte[64];
        byte[] value = new byte[1];
        KVBase64Util.kvToBase64(key, value);
    }

    /**
     * Test method for {@link org.apache.niolex.commons.codec.KVBase64Util#base64toKV(java.lang.String)}.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testBase64toKVErrSize_2() {
        byte[] key = new byte[1];
        byte[] value = new byte[512];
        KVBase64Util.kvToBase64(key, value);
    }

    /**
     * Test method for {@link org.apache.niolex.commons.codec.KVBase64Util#base64toKV(java.lang.String)}.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testBase64toKVErrSize_3() {
        new KVBase64Util();
        KVBase64Util.base64toKV("zwAASSBhbSB0aGUga2V5LlRoZSBtb2JpbGUgbGFuZHNjYXBlI");
    }

}
