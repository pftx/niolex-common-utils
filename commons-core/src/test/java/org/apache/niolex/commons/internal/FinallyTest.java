/**
 * FinallyTest.java
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


import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.OutputStream;

import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-6-26
 */
public class FinallyTest extends Finally {

    @Test(expected=IOException.class)
    public void testWriteAndClose() throws Exception {
        OutputStream out = mock(OutputStream.class);
        doThrow(new IOException("This")).when(out).write(null);
        writeAndClose(out, null);
    }

    @Test(expected=NullPointerException.class)
    public void testTransferAndClose() throws Exception {
        transferAndClose(null, mock(OutputStream.class), 1024);
    }

}
