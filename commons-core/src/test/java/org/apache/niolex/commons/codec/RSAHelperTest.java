/**
 * RSAHelperTest.java
 *
 * Copyright 2013 the original author or authors.
 *
 * We licenses this file to you under the Apache License, version 2.0
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
import static org.mockito.Mockito.mock;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-6-21
 */
public class RSAHelperTest extends RSAHelper {

    static PrivateKey PRIK;
    static PublicKey PUBK;

    @BeforeClass
    public static void testInitKey() throws Exception {
        KeyPair pair = initKey();
        PRIK = pair.getPrivate();
        PUBK = pair.getPublic();
    }

    @Test
    public void testencodeKeyToBase64() throws Exception {
        String k = encodeKeyToBase64(PRIK);
        PrivateKey key = getPrivateKey(k);
        assertEquals(key, PRIK);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetPrivateKeyInvalid() {
        getPrivateKey("Lex is not a Key!!!");
    }

    @Test
    public void testencodePublicKeyToBase64KeyString() throws Exception {
        String k = encodeKeyToBase64(PUBK);
        PublicKey key = getPublicKey(k);
        assertEquals(key, PUBK);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetPublicKeyInvalid() throws Exception {
        getPublicKey("Lex is not a Key!!!");
    }

    @Test
    public void testEncodePublicKeyToXml() throws Exception {
        String xml = encodePublicKeyToXml(PUBK);
        assertTrue(xml.startsWith("<RSAKeyValue><Modulus>"));
        assertEquals(243, xml.length());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testEncodePublicKeyToXmlInvalid() throws Exception {
        PublicKey key = mock(PublicKey.class);
        encodePublicKeyToXml(key);
    }

    @Test
    public void testEncodePrivateKeyToXml() throws Exception {
        String xml = encodePrivateKeyToXml(PRIK);
        System.out.print(xml);
        assertTrue(xml.startsWith("<RSAKeyValue><Modulus>"));
        assertEquals(915, xml.length());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testEncodePrivateKeyToXmlInvalid() throws Exception {
        PrivateKey key = mock(PrivateKey.class);
        encodePrivateKeyToXml(key);
    }

    @Test
    public void testDecodePublicKeyFromXml() throws Exception {
        String xml = encodePublicKeyToXml(PUBK);
        PublicKey key = decodePublicKeyFromXml(xml);
        assertEquals(key, PUBK);
    }

    @Test
    public void testDecodePrivateKeyFromXml() throws Exception {
        String xml = encodePrivateKeyToXml(PRIK);
        PrivateKey key = decodePrivateKeyFromXml(xml);
        assertEquals(key, PRIK);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testDecodePublicKeyFromXmlInvalidXml() throws Exception {
        String pubKey = "<RSAKeyValue><Modulus>AA==</Modulus><Exponent>AA==</Exponent></RSAKeyValue>";
        PublicKey key = decodePublicKeyFromXml(pubKey);
        assertNull(key);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testDecodePrivateKeyFromXmlInvalidXml() throws Exception {
        String priKey = "<RSAKeyValue><Modulus>AA==</Modulus><Exponent>AQAB</Exponent><P>ANc2rj5vCbcmisRJJyy/2XPgGLmPHIQW1wPry25BM0fTPchWzsr77SyRNa6gwBfBIm0NKmtiiHCOUExP9+vYeQM=</P><Q>AM7Vw6B5EXJa03qUW2j7L/hY+n3VzP50rGxT64PV6Lt/+PsFdGcQS0Yz2Jmb61t8oVpLNvwfrTr8zIzWQtVS6gk=</Q><DP>a+drp7wOl/jIHLA85w/t3E5gtzDM8GFvPvULk2U3a+y7DmaP2nBDs1O/IaZRidd5BkpSmXLWy/BezFRQDr0Dtw==</DP><DQ>Wp/o3igNz+gh3wSf5KiihRMfdgE2l4sxfSlr+NDB712MDxh9vyaxhKn0zqE1h1ldLT3lcqTCdyUKzu6WS/fPWQ==</DQ><InverseQ>AKgLMYdhQ24CCR3TSi6F90JZwFW3LjdCUE0enBCHzNp8ro2UIisL52oNVFF01wCwGAYrJnXbdhMSjsMEO1cZ7WM=</InverseQ><D>AJNcRoIyeFwVX+zrSULn17Udzk7KC/R2CEWqfNhY7tH3D+A9UXynxiKxY56Qmtlgj7YaZjwa7wMXKEUIKfCnJ7nto+6Bu6Ak9u4xPphkPXmYvyhyuWFGGMK+iYrbJ1BxG0Mbzdcv9HcXqGYJari8A2VFOfav9YGA19sUG9Dl0NtR</D></RSAKeyValue>";
        PrivateKey key = decodePrivateKeyFromXml(priKey);
        assertNull(key);
    }

}
