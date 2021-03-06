/**
 * RsaHelper.java
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

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.niolex.commons.internal.IgnoreException;

/**
 * This class is for generate RSA Keys and transform keys to XML description file or base64 string.
 * The XML description file can be used to exchange RSA Keys, especially for .Net projects.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-4-11
 */
public abstract class RSAHelper {

    public static final String ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";

    /**
     * Initialize the Key pair with 1024bit size.
     *
     * @return the Key Pair storing both keys
     * @throws NoSuchAlgorithmException If Your JDK don't support RSA.
     */
    public static KeyPair initKey() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGen.initialize(1024);
        return keyPairGen.generateKeyPair();
    }

    /**
     * Get the RSA KeyFactory.
     *
     * @return the RSA KeyFactory
     * @throws IllegalStateException If Your JDK don't support RSA.
     */
    public static KeyFactory getKeyFactory() {
        return IgnoreException.getKeyFactory(ALGORITHM);
    }

    /**
     * Decode the private key from the base64 encoded private key specification.
     *
     * @param key the base64 encoded private key specification
     * @return the decoded private key
     * @throws IllegalStateException If Your JDK don't support RSA.
     * @throws IllegalArgumentException If the parameter key is damaged
     */
    public static PrivateKey getPrivateKey(String key) {
        // Decode from Base64.
        byte[] keyBytes = Base64Util.base64ToByte(key);
        // Create Key specification.
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);

        try {
            return getKeyFactory().generatePrivate(pkcs8KeySpec);
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("Invalid key specification");
        }
    }

    /**
     * Decode the public key from the base64 encoded public key specification.
     *
     * @param key the base64 encoded public key specification
     * @return the decoded public key
     * @throws IllegalStateException If Your JDK don't support RSA.
     * @throws IllegalArgumentException If the parameter key is damaged
     */
    public static PublicKey getPublicKey(String key) {
        // Decode from Base64.
        byte[] keyBytes = Base64Util.base64ToByte(key);
        // Create Key specification.
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);

        try {
            return getKeyFactory().generatePublic(x509KeySpec);
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("Invalid key specification");
        }
    }

    /**
     * Encode the (public/private) key into base64 string key specification.
     *
     * @param key the key to be encoded
     * @return the base64 encoded key specification
     */
    public static String encodeKeyToBase64(Key key) {
        return Base64Util.byteToBase64(key.getEncoded());
    }

	/**
	 * Encode public key into xml string.
	 * @param key the public key
	 * @return the encoded key
	 */
    public static String encodePublicKeyToXml(PublicKey key) {
        if (!RSAPublicKey.class.isInstance(key)) {
            throw new IllegalArgumentException("The key you provided is not RSAPublicKey");
        }
        RSAPublicKey pubKey = (RSAPublicKey) key;
        StringBuilder sb = new StringBuilder();

        sb.append("<RSAKeyValue>");
        sb.append("<Modulus>").append(Base64Util.byteToBase64(pubKey.getModulus().toByteArray())).append("</Modulus>");
        sb.append("<Exponent>").append(Base64Util.byteToBase64(pubKey.getPublicExponent().toByteArray()))
                .append("</Exponent>");
        sb.append("</RSAKeyValue>");
        return sb.toString();
    }

    /**
     * Encode private key to xml string.
     * @param key the private key
     * @return the encoded key
     */
    public static String encodePrivateKeyToXml(PrivateKey key) {
        if (!RSAPrivateCrtKey.class.isInstance(key)) {
            throw new IllegalArgumentException("The key you provided is not RSAPrivateCrtKey");
        }
        RSAPrivateCrtKey priKey = (RSAPrivateCrtKey) key;
        StringBuilder sb = new StringBuilder();

        sb.append("<RSAKeyValue>");
        sb.append("<Modulus>").append(Base64Util.byteToBase64(priKey.getModulus().toByteArray())).append("</Modulus>");
        sb.append("<Exponent>").append(Base64Util.byteToBase64(priKey.getPublicExponent().toByteArray()))
                .append("</Exponent>");
        sb.append("<P>").append(Base64Util.byteToBase64(priKey.getPrimeP().toByteArray())).append("</P>");
        sb.append("<Q>").append(Base64Util.byteToBase64(priKey.getPrimeQ().toByteArray())).append("</Q>");
        sb.append("<DP>").append(Base64Util.byteToBase64(priKey.getPrimeExponentP().toByteArray())).append("</DP>");
        sb.append("<DQ>").append(Base64Util.byteToBase64(priKey.getPrimeExponentQ().toByteArray())).append("</DQ>");
        sb.append("<InverseQ>").append(Base64Util.byteToBase64(priKey.getCrtCoefficient().toByteArray()))
                .append("</InverseQ>");
        sb.append("<D>").append(Base64Util.byteToBase64(priKey.getPrivateExponent().toByteArray())).append("</D>");
        sb.append("</RSAKeyValue>");
        return sb.toString();
    }

    /**
     * Decode public key from XML string.
     * @param xml the key encoding XML
     * @return the decoded key
     */
    public static PublicKey decodePublicKeyFromXml(String xml) {
        xml = xml.replaceAll("[\r\n ]", "");
        BigInteger modulus = new BigInteger(1, Base64Util.base64ToByte(getMiddleString(xml, "<Modulus>", "</Modulus>")));
        BigInteger publicExponent = new BigInteger(1, Base64Util.base64ToByte(getMiddleString(xml, "<Exponent>",
                "</Exponent>")));

        RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(modulus, publicExponent);

        try {
            return getKeyFactory().generatePublic(rsaPubKey);
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("Invalid key specification");
        }
    }

    /**
     * Decode private key from XML string.
     * @param xml the key encoding XML
     * @return the decoded key
     */
    public static PrivateKey decodePrivateKeyFromXml(String xml) {
        xml = xml.replaceAll("\r", "").replaceAll("\n", "");
        BigInteger modulus = new BigInteger(1, Base64Util.base64ToByte(getMiddleString(xml, "<Modulus>", "</Modulus>")));
        BigInteger publicExponent = new BigInteger(1, Base64Util.base64ToByte(getMiddleString(xml, "<Exponent>",
                "</Exponent>")));
        BigInteger privateExponent = new BigInteger(1, Base64Util.base64ToByte(getMiddleString(xml, "<D>", "</D>")));
        BigInteger primeP = new BigInteger(1, Base64Util.base64ToByte(getMiddleString(xml, "<P>", "</P>")));
        BigInteger primeQ = new BigInteger(1, Base64Util.base64ToByte(getMiddleString(xml, "<Q>", "</Q>")));
        BigInteger primeExponentP = new BigInteger(1, Base64Util.base64ToByte(getMiddleString(xml, "<DP>", "</DP>")));
        BigInteger primeExponentQ = new BigInteger(1, Base64Util.base64ToByte(getMiddleString(xml, "<DQ>", "</DQ>")));
        BigInteger crtCoefficient = new BigInteger(1, Base64Util.base64ToByte(getMiddleString(xml, "<InverseQ>",
                "</InverseQ>")));

        RSAPrivateCrtKeySpec rsaPriKey = new RSAPrivateCrtKeySpec(modulus, publicExponent, privateExponent, primeP,
                primeQ, primeExponentP, primeExponentQ, crtCoefficient);

        try {
            return getKeyFactory().generatePrivate(rsaPriKey);
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("Invalid key specification");
        }
    }

    /**
     * Get the real middle string between the tags.
     *
     * @param xml the XML string
     * @param start the start tag
     * @param end the end tag
     * @return the middle string
     */
    public static String getMiddleString(String xml, String start, String end) {
        int s = xml.indexOf(start) + start.length();
        int e = xml.indexOf(end);
        return xml.substring(s, e);
    }

}
