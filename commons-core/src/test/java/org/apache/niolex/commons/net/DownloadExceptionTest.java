/**
 * DownloadExceptionTest.java
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
package org.apache.niolex.commons.net;

import static org.apache.niolex.commons.net.DownloadException.ExCode.*;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-6-20
 */
public class DownloadExceptionTest {

    @Test
    public void testFILE_TOO_LARGEGetCode() throws Exception {
        FILE_TOO_LARGE.toString();
        assertEquals(FILE_TOO_LARGE, valueOf("FILE_TOO_LARGE"));
    }

    @Test
    public void testINVALID_SERVER_RESPONSEGetCode() throws Exception {
        INVALID_SERVER_RESPONSE.toString();
        assertEquals("INVALID_SERVER_RESPONSE", INVALID_SERVER_RESPONSE.name());
    }

    @Test
    public void testFILE_TOO_LARGEGetMessage() throws Exception {
        assertEquals(-3, FILE_TOO_LARGE.compareTo(INVALID_SERVER_RESPONSE));
    }

    @Test
    public void testIOEXCEPTIONGetCode() throws Exception {
        IOEXCEPTION.toString();
        assertTrue(IOEXCEPTION.equals(valueOf("IOEXCEPTION")));
    }

    @Test
    public void testIOEXCEPTIONGetMessage() throws Exception {
        assertEquals(-1, IOEXCEPTION.ordinal() - INVALID_SERVER_RESPONSE.ordinal());
    }

}
