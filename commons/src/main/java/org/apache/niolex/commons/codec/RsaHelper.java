/**
 * RsaHelper.java
 *
 * Copyright 2012 Baidu, Inc.
 *
 * Baidu licenses this file to you under the Apache License, version 2.0
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

/**
 * @author @author@ (@author-email@)
 * 
 * @version @version@, $Date: 2012-4-11$
 * 
 */

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;

import org.apache.commons.lang.ArrayUtils;

public class RsaHelper {

    public static String encodePublicKeyToXml(PublicKey key) throws UnsupportedEncodingException {
        if (!RSAPublicKey.class.isInstance(key)) {
            return null;
        }
        RSAPublicKey pubKey = (RSAPublicKey) key;
        StringBuilder sb = new StringBuilder();

        sb.append("<RSAKeyValue>");
        byte[] modulus = pubKey.getModulus().toByteArray();
        if (modulus[0] == 0) {
            modulus = ArrayUtils.subarray(modulus, 1, modulus.length);
        }
        sb.append("<Modulus>").append(Base64Util.byteToBase64(modulus)).append("</Modulus>");
        sb.append("<Exponent>").append(Base64Util.byteToBase64(pubKey.getPublicExponent().toByteArray()))
                .append("</Exponent>");
        sb.append("</RSAKeyValue>");
        return sb.toString();
    }

    public static String encodePrivateKeyToXml(PrivateKey key) throws UnsupportedEncodingException {
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

    public static PublicKey decodePublicKeyFromXml(String xml) throws UnsupportedEncodingException {
        xml = xml.replaceAll("\r", "").replaceAll("\n", "");
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

    public static PrivateKey decodePrivateKeyFromXml(String xml) throws UnsupportedEncodingException {
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

    public static String getMiddleString(String xml, String start, String end) {
        int s = xml.indexOf(start) + start.length();
        int e = xml.indexOf(end);
        return xml.substring(s, e);
    }

}
