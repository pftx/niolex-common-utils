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
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;


/**
 * This class is for generate RSA XML description file.
 * The XML description file can be used to exchange RSA Keys, especially for .Net project.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 *
 * @version 1.0.0, $Date: 2012-4-11$
 *
 */
public abstract class RsaHelper {

	/**
	 * Encode public key into xml string.
	 * @param key
	 * @return the encoded key
	 */
    public static String encodePublicKeyToXml(PublicKey key) {
        if (!RSAPublicKey.class.isInstance(key)) {
            return null;
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
     * @param key
     * @return the encoded key
     */
    public static String encodePrivateKeyToXml(PrivateKey key) {
        if (!RSAPrivateCrtKey.class.isInstance(key)) {
            return null;
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
     * @param xml
     * @return the decoded key
     */
    public static PublicKey decodePublicKeyFromXml(String xml) {
        xml = xml.replaceAll("[\r\n ]", "");
        BigInteger modulus = new BigInteger(1, Base64Util.base64toByte(getMiddleString(xml, "<Modulus>", "</Modulus>")));
        BigInteger publicExponent = new BigInteger(1, Base64Util.base64toByte(getMiddleString(xml, "<Exponent>",
                "</Exponent>")));

        RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(modulus, publicExponent);

        KeyFactory keyf;
        try {
            keyf = KeyFactory.getInstance(RSAUtil.KEY_ALGORITHM);
            return keyf.generatePublic(rsaPubKey);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Decode private key from XML string.
     * @param xml
     * @return the decoded key
     */
    public static PrivateKey decodePrivateKeyFromXml(String xml) {
        xml = xml.replaceAll("\r", "").replaceAll("\n", "");
        BigInteger modulus = new BigInteger(1, Base64Util.base64toByte(getMiddleString(xml, "<Modulus>", "</Modulus>")));
        BigInteger publicExponent = new BigInteger(1, Base64Util.base64toByte(getMiddleString(xml, "<Exponent>",
                "</Exponent>")));
        BigInteger privateExponent = new BigInteger(1, Base64Util.base64toByte(getMiddleString(xml, "<D>", "</D>")));
        BigInteger primeP = new BigInteger(1, Base64Util.base64toByte(getMiddleString(xml, "<P>", "</P>")));
        BigInteger primeQ = new BigInteger(1, Base64Util.base64toByte(getMiddleString(xml, "<Q>", "</Q>")));
        BigInteger primeExponentP = new BigInteger(1, Base64Util.base64toByte(getMiddleString(xml, "<DP>", "</DP>")));
        BigInteger primeExponentQ = new BigInteger(1, Base64Util.base64toByte(getMiddleString(xml, "<DQ>", "</DQ>")));
        BigInteger crtCoefficient = new BigInteger(1, Base64Util.base64toByte(getMiddleString(xml, "<InverseQ>",
                "</InverseQ>")));

        RSAPrivateCrtKeySpec rsaPriKey = new RSAPrivateCrtKeySpec(modulus, publicExponent, privateExponent, primeP,
                primeQ, primeExponentP, primeExponentQ, crtCoefficient);

        KeyFactory keyf;
        try {
            keyf = KeyFactory.getInstance(RSAUtil.KEY_ALGORITHM);
            return keyf.generatePrivate(rsaPriKey);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get the real middle string between tags.
     * @param xml
     * @param start
     * @param end
     * @return the middle string
     */
    public static String getMiddleString(String xml, String start, String end) {
        int s = xml.indexOf(start) + start.length();
        int e = xml.indexOf(end);
        return xml.substring(s, e);
    }

}
