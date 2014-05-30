/**
 * RsaHelperTest.java
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

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import org.junit.Assert;
import org.junit.Test;


/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 *
 * @version 1.0.0, $Date: 2012-4-11$
 *
 */
public class OldRSAHelperTest {

    private static KeyPair initKeys = null;

    static {
        try {
            initKeys = RSAUtil.initKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMiddleStringTest() {
        Assert.assertEquals(RSAHelper.getMiddleString("baabcdiedc", "ba", "dc"), "abcdie");
        Assert.assertEquals(RSAHelper.getMiddleString("<RSAKeyValue><Modulus>AK3TiBdwM9CJVQSWA6VrTJUU8NxB9uil7ByGQ+bSXNtecogAigvmiMF6QHg2QtgAApmwNFGfCFK6A+DEn9DroGVVThKpD3XkraQkq9i6y95FXX+GZKKFfC1fowGIFMaB5Wxns8pn+qMi3jagl/lin7Lohf3d0o0T7U//9vjiuzjT</Modulus><Exponent>AQAB</Exponent></RSAKeyValue>", "<Modulus>", "</Modulus>"),
                "AK3TiBdwM9CJVQSWA6VrTJUU8NxB9uil7ByGQ+bSXNtecogAigvmiMF6QHg2QtgAApmwNFGfCFK6A+DEn9DroGVVThKpD3XkraQkq9i6y95FXX+GZKKFfC1fowGIFMaB5Wxns8pn+qMi3jagl/lin7Lohf3d0o0T7U//9vjiuzjT");
    }

    @Test
    public void testPublicKey() {
        PublicKey key = initKeys.getPublic();
        String xml = RSAHelper.encodePublicKeyToXml(key);
        System.out.println("Pubk => " + xml);
        PublicKey key2 = RSAHelper.decodePublicKeyFromXml(xml);
        Assert.assertEquals(key, key2);
    }

    @Test
    public void testPrivateKey() {
        PrivateKey key = initKeys.getPrivate();
        String xml = RSAHelper.encodePrivateKeyToXml(key);
        System.out.println("Prik => " + xml);
        PrivateKey key2 = RSAHelper.decodePrivateKeyFromXml(xml);
        Assert.assertEquals(key, key2);
    }

    @Test
    public void testWork() throws Exception {
        String key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCVhipL6FX03wgyuKA2RlWiBLqQN+SGqlClYtC6DPN2omqG34+jBqFvkU8Khd" +
        		"BFBenx0xLZliTLTRT/xzhISDTwgdB3IE2Ae5nu6IE2D18qJaoBEoNFTRVOipyQ5Q8GuMzdmQKtXVVGlwTAStCXXCjKp1sCJSTNVYFTSMZfKH7TNwIDAQAB";
        byte[] keyBytes = Base64Util.base64toByte(key);
        byte[] as = RSAUtil.encryptByPublicKey(keyBytes, key);
        // 构造X509EncodedKeySpec对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(RSAUtil.ALGORITHM);
        PublicKey key1 = keyFactory.generatePublic(keySpec);
        String xmlKey = RSAHelper.encodePublicKeyToXml(key1);
        System.out.println("Pubk => " + xmlKey);
        // 取公钥匙对象
        PublicKey key2 = RSAHelper.decodePublicKeyFromXml(xmlKey);
        Assert.assertEquals(key1, key2);
        byte[] bs = RSAUtil.encrypt(keyBytes, key2);
        // !!The encryption with public key two times generate different results.
        Assert.assertEquals(as.length, bs.length);
    }

}
