/**
 * Blowfish2Test.java
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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.apache.niolex.commons.codec.Base16Util;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-6-13
 */
public class Blowfish2Test {

    @Test
    public void testEncryptByteArray() throws Exception {
        Blowfish2 coder = new Blowfish2("ASIApKdj5HHBkb".getBytes());
        byte[] out = coder.encrypt("not".getBytes());
        String r = Base16Util.byteToBase16(out);
        System.out.println("r => " + r);
        assertEquals("0103007bd47750d6ba0135", r);
    }

    @Test(expected=NullPointerException.class)
    public void testDecrypt() {
        Blowfish2 coder = new Blowfish2(null);
        byte[] in = "not".getBytes();
        byte[] out = "g".getBytes();
        try {
            coder.processBlock(in, 0, out, 0, true);
        } catch (Exception e) {
            assertEquals("Blowfish not initialised", e.getMessage());
            return;
        }
        assertFalse(true);
    }

    @Test
    public void testEncryptByteArrayIntInt() throws Exception {
        Blowfish2 coder = new Blowfish2("ASIApKdj5HHBkb".getBytes());
        byte[] in = "not".getBytes();
        byte[] out = "g".getBytes();
        try {
            coder.processBlock(in, 0, out, 0, true);
        } catch (Exception e) {
            assertEquals("input buffer too short", e.getMessage());
            return;
        }
        assertFalse(true);
    }

    @Test
    public void testProcessBlock() throws Exception {
        Blowfish2 coder = new Blowfish2("ASIApKdj5HHBkb".getBytes());
        byte[] in = new byte[12];
        byte[] out = "g".getBytes();
        try {
            coder.processBlock(in, 0, out, 0, true);
        } catch (Exception e) {
            assertEquals("output buffer too short", e.getMessage());
            return;
        }
        assertFalse(true);
    }

}
