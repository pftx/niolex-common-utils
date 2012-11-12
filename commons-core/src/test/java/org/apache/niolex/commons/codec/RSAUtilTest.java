/**
 * RSAUtilTest.java
 *
 * Copyright 2011 Niolex, Inc.
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

import java.security.Key;
import java.util.Map;

import junit.framework.Assert;

import org.apache.niolex.commons.codec.Base64Util;
import org.apache.niolex.commons.codec.RSAUtil;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 *
 * @version 1.0.0, $Date: 2011-1-12$
 *
 */
public class RSAUtilTest {

    public static String privateKey = null;
    public static String publicKey = null;

    @BeforeClass
    public static final void init() throws Exception {
        Map<String, Object> keyPair = RSAUtil.initKey();
        privateKey = RSAUtil.getPrivateKey(keyPair);
        publicKey = RSAUtil.getPublicKey(keyPair);
    }

    @Test
    public void testSignature() throws Exception {
        byte[] data = "中国制造，惠及全球！".getBytes();
        String sign = RSAUtil.sign(data, privateKey);
        System.out.println("sign => " + sign);
        Assert.assertTrue(RSAUtil.verify(data, publicKey, sign));
    }

    @Test
    public void testPublicEncription() throws Exception {
        String in = "问天下苍穹，谁敢不从！";
        byte[] data = in.getBytes();
        byte[] encr = RSAUtil.encryptByPublicKey(data, publicKey);
        System.out.println("encr => " + Base64Util.byteToBase64(encr));
        byte[] outp = RSAUtil.decryptByPrivateKey(encr, privateKey);
        String out = new String(outp);
        System.out.println("out => " + out);
        Assert.assertEquals(in, out);
    }

    @Test
    public void testPublicEncription_2() throws Exception {
    	String in = "问天下苍穹，谁敢不从！";
    	byte[] data = in.getBytes();
    	Key pubkey = RSAUtil.getPublicKey(publicKey);
    	byte[] encr = RSAUtil.encryptByPublicKey(data, pubkey);
    	System.out.println("encr => " + Base64Util.byteToBase64(encr));
    	Key prikey = RSAUtil.getPrivateKey(privateKey);
    	byte[] outp = RSAUtil.decryptByPrivateKey(encr, prikey);
    	String out = new String(outp);
    	System.out.println("out => " + out);
    	Assert.assertEquals(in, out);
    }

    @Test
    public void testPublicEncriptionLong() throws Exception {
        String in = "问天下苍穹，谁敢不从！背叛伤害不了你，能伤你的，是你太在乎。分手伤害不了你，能伤你的，是回忆。无疾而终的恋情伤害不了你，能伤你的，是希望。你总以为是感情伤害了你，其实伤到你的人，永远是自己。所以，做人最高的境界，不是全心全意去爱，而是过好自己的人生，让别人来死缠烂打吧。";
        byte[] data = in.getBytes();
        byte[] encr = RSAUtil.encryptByPublicKey(data, publicKey);
        System.out.println("encr => " + Base64Util.byteToBase64(encr));
        byte[] outp = RSAUtil.decryptByPrivateKey(encr, privateKey);
        String out = new String(outp);
        System.out.println("out => " + out);
        Assert.assertEquals(in, out);
    }

    @Test
    public void testPrivateEncription() throws Exception {
        String in = "人大委员建议20岁的女孩找40的！20的男的等到40再找个20的女的！";
        byte[] data = in.getBytes();
        byte[] encr = RSAUtil.encryptByPrivateKey(data, privateKey);
        System.out.println("encr => " + Base64Util.byteToBase64(encr));
        byte[] outp = RSAUtil.decryptByPublicKey(encr, publicKey);
        String out = new String(outp);
        System.out.println("out => " + out);
        Assert.assertEquals(in, out);
    }

    @Test
    public void testPrivateEncription_2() throws Exception {
    	String in = "人大委员建议20岁的女孩找40的！20的男的等到40再找个20的女的！";
    	byte[] data = in.getBytes();
    	Key key = RSAUtil.getPrivateKey(privateKey);
    	byte[] encr = RSAUtil.encryptByPrivateKey(data, key);
    	System.out.println("encr => " + Base64Util.byteToBase64(encr));
    	byte[] outp = RSAUtil.decryptByPublicKey(encr, publicKey);
    	String out = new String(outp);
    	System.out.println("out => " + out);
    	Assert.assertEquals(in, out);
    }

    @Test
    public void testPrivateEncriptionLong() throws Exception {
        String in = "人大委员建议20岁的女孩找40的！20的男的等到40再找个20的女的！背叛伤害不了你，能伤你的，是你太在乎。分手伤害不了你，能伤你的，是回忆。无疾而终的恋情伤害不了你，能伤你的，是希望。你总以为是感情伤害了你，其实伤到你的人，永远是自己。所以，做人最高的境界，不是全心全意去爱，而是过好自己的人生，让别人来死缠烂打吧。";
        byte[] data = in.getBytes();
        byte[] encr = RSAUtil.encryptByPrivateKey(data, privateKey);
        System.out.println("encr => " + Base64Util.byteToBase64(encr));
        byte[] outp = RSAUtil.decryptByPublicKey(encr, publicKey);
        String out = new String(outp);
        System.out.println("out => " + out);
        Assert.assertEquals(in, out);
    }
}
