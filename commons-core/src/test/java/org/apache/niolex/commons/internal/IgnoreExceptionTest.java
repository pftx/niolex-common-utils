/**
 * IgnoreExceptionTest.java
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
package org.apache.niolex.commons.internal;


import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-6-26
 */
public class IgnoreExceptionTest extends IgnoreException {

    @Test(expected=IllegalStateException.class)
    public void testGetKeyFactory() throws Exception {
        getKeyFactory("Lex");
    }

    @Test
    public void testGetCharset() {
        assertNotNull(getCharset("utf8"));
    }

    @Test
    public void testGetCharsetNull() {
        assertNull(getCharset("utf9"));
    }

    @Test
    public void testIsNetworkInterfaceUpNull() throws Exception {
        assertFalse(isNetworkInterfaceUp(null));
    }

    @Test
    public void testPopulateLocalAddresses() throws Exception {
        populateLocalAddresses(null);
    }

    @Test
    public void testNewGZIPOutputStream() throws Exception {
        newGZIPOutputStream(null);
    }

    @Test
    public void testNewGZIPInputStream() throws Exception {
        newGZIPInputStream(null);
    }

    @Test
    public void testTransferAndClose() throws Exception {
        int i = transferAndClose(null, null, -1);
        assertEquals(-1, i);
    }

}
