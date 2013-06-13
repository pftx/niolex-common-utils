/**
 * CompleteCodersTest.java
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
package org.apache.niolex.commons.coder;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-6-13
 */
public class CompleteCodersTest {

    public void encodeAndDecodeEmpty(Coder coder) throws Exception {
        final String in = "";
        final String mid = coder.encode(in);
        final String out = coder.decode(mid);
        System.out.println("MIDDLE " + mid);
        System.out.println(in + " => " + out);
        System.out.println("------------------");
    }

    public void encodeAndDecodeNormal(Coder coder) throws Exception {
        final String in = "无需预约，15 x 24小时随时购买";
        final String mid = coder.encode(in);
        final String out = coder.decode(mid);
        System.out.println("MIDDLE " + mid);
        System.out.println(in + " => " + out);
        System.out.println("------------------");
    }

    public void process(List<Coder> list) throws Exception {
        for (Coder c : list) {
            encodeAndDecodeEmpty(c);
            encodeAndDecodeNormal(c);
        }
    }

    @Test
    public void testAllCoders() throws Exception {
        List<Coder> list = Lists.newArrayList();
        Coder coder;
        // ---- AES 128
        coder = new AESCoder();
        coder.initKey(KeyUtil.genKey("AES"));
        list.add(coder);
        // ---- Blowfish2 192
        coder = new Blowfish2Coder();
        coder.initKey(KeyUtil.genKey("Blowfish", 0, 192));
        list.add(coder);
        // ---- Blowfish2 256
        coder = new Blowfish2Coder();
        coder.initKey(KeyUtil.genKey("Blowfish", 0, 256));
        list.add(coder);
        // ---- DESCoder 56
        coder = new DESCoder();
        coder.initKey(KeyUtil.genKey("DES", 0, 56));
        list.add(coder);
        // ---- RC2Coder 256
        coder = new RC2Coder();
        coder.initKey(KeyUtil.genKey("RC2", 128, 256));
        list.add(coder);
        // ---- RC2Coder 192
        coder = new RC2Coder();
        coder.initKey(KeyUtil.genKey("RC2", 128, 192));
        list.add(coder);
        // ---- TripleDESCoder 192
        coder = new TripleDESCoder();
        coder.initKey(KeyUtil.genKey("TripleDES", 128, 168));
        list.add(coder);

        // Ready for test.
        process(list);
    }

}
