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

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;


/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 *
 * @version 1.0.0, $Date: 2012-4-11$
 *
 */
public class RSAHelperTest {

    private static Map<String, Object> initKeys = null;

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
    public void testEncodePublicKeyToXml() {
        PublicKey key = mock(PublicKey.class);
        String xml = RSAHelper.encodePublicKeyToXml(key);
        assertNull(xml);
    }

    @Test
    public void testEncodePrivateKeyToXml() {
        PrivateKey key = mock(PrivateKey.class);
        String xml = RSAHelper.encodePrivateKeyToXml(key);
        assertNull(xml);
    }

    @Test
    public void testPublicKey() {
        PublicKey key = (PublicKey)initKeys.get(RSAUtil.PUBLIC_KEY);
        String xml = RSAHelper.encodePublicKeyToXml(key);
        System.out.println("Pubk => " + xml);
        PublicKey key2 = RSAHelper.decodePublicKeyFromXml(xml);
        Assert.assertEquals(key, key2);
    }

    @Test
    public void testPrivateKey() {
        PrivateKey key = (PrivateKey)initKeys.get(RSAUtil.PRIVATE_KEY);
        String xml = RSAHelper.encodePrivateKeyToXml(key);
        System.out.println("Prik => " + xml);
        PrivateKey key2 = RSAHelper.decodePrivateKeyFromXml(xml);
        Assert.assertEquals(key, key2);
    }

    @Test
    public void testWork() throws Exception {
        String key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCVhipL6FX03wgyuKA2RlWiBLqQN+SGqlClYtC6DPN2omqG34+jBqFvkU8KhdBFBenx0xLZliTLTRT/xzhISDTwgdB3IE2Ae5nu6IE2D18qJaoBEoNFTRVOipyQ5Q8GuMzdmQKtXVVGlwTAStCXXCjKp1sCJSTNVYFTSMZfKH7TNwIDAQAB";
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
        byte[] bs = RSAUtil.encryptByPublicKey(keyBytes, key2);
        Assert.assertEquals(as.length, bs.length);
        System.out.println("OK: " + keyBytes.length);
    }

    @Test
    public void testDecodePublicKeyFromXml() throws Exception {
        String pubKey = "<RSAKeyValue><Modulus>AA==</Modulus><Exponent>AA==</Exponent></RSAKeyValue>";
        PublicKey key = RSAHelper.decodePublicKeyFromXml(pubKey);
        assertNull(key);
    }

    @Test
    public void testDecodePrivateKeyFromXml() throws Exception {
        String priKey = "<RSAKeyValue><Modulus>AA==</Modulus><Exponent>AQAB</Exponent><P>ANc2rj5vCbcmisRJJyy/2XPgGLmPHIQW1wPry25BM0fTPchWzsr77SyRNa6gwBfBIm0NKmtiiHCOUExP9+vYeQM=</P><Q>AM7Vw6B5EXJa03qUW2j7L/hY+n3VzP50rGxT64PV6Lt/+PsFdGcQS0Yz2Jmb61t8oVpLNvwfrTr8zIzWQtVS6gk=</Q><DP>a+drp7wOl/jIHLA85w/t3E5gtzDM8GFvPvULk2U3a+y7DmaP2nBDs1O/IaZRidd5BkpSmXLWy/BezFRQDr0Dtw==</DP><DQ>Wp/o3igNz+gh3wSf5KiihRMfdgE2l4sxfSlr+NDB712MDxh9vyaxhKn0zqE1h1ldLT3lcqTCdyUKzu6WS/fPWQ==</DQ><InverseQ>AKgLMYdhQ24CCR3TSi6F90JZwFW3LjdCUE0enBCHzNp8ro2UIisL52oNVFF01wCwGAYrJnXbdhMSjsMEO1cZ7WM=</InverseQ><D>AJNcRoIyeFwVX+zrSULn17Udzk7KC/R2CEWqfNhY7tH3D+A9UXynxiKxY56Qmtlgj7YaZjwa7wMXKEUIKfCnJ7nto+6Bu6Ak9u4xPphkPXmYvyhyuWFGGMK+iYrbJ1BxG0Mbzdcv9HcXqGYJari8A2VFOfav9YGA19sUG9Dl0NtR</D></RSAKeyValue>";
        PrivateKey key = RSAHelper.decodePrivateKeyFromXml(priKey);
        assertNull(key);
    }
}
