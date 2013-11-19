/**
 * SystemUtilTest.java
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
package org.apache.niolex.commons.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-8-1
 */
public class SystemUtilTest extends SystemUtil {

	@Test
	public void testSleep() throws Exception {
		SystemUtil.sleep(4);
	}

	@Test
	public void testSleepErr() throws Exception {
		SystemUtil.sleep(-1);
	}

    @Test
    public void testCloseOk() throws Exception {
        SystemUtil.close(mock(Closeable.class));
        Closeable ab = null;
        SystemUtil.close(ab);
    }

    @Test
    public void testCloseSo() throws Exception {
        SystemUtil.close(mock(Socket.class));
        Socket ab = null;
        SystemUtil.close(ab);
    }

    @Test
    public void testCloseErr() throws Exception {
        Closeable ab = mock(Closeable.class);
        doThrow(new IOException("This is from x.j.y")).when(ab).close();
        assertEquals("This is from x.j.y", SystemUtil.close(ab).getMessage());
    }

    @Test
    public void testCloseSoErr() throws Exception {
        Socket ab = mock(Socket.class);
        doThrow(new IOException("This is from x.j.y")).when(ab).close();
        assertEquals("This is from x.j.y", SystemUtil.close(ab).getMessage());
    }

    @Test
    public void testGetSystemProperty() throws Exception {
        String s = SystemUtil.getSystemProperty("user.home", "usr.home");
        System.out.println("home = " + s);
        s = SystemUtil.getSystemProperty("usr.home", "java.class.path");
        System.out.println("home = " + s);
    }

    @Test
    public void testGetSystemPropertyNull() throws Exception {
        String s = SystemUtil.getSystemProperty("evn.home", "usr.home");
        System.out.println("home = " + s);
    }

    @Test
    public void testDefined() throws Exception {
        assertTrue(defined("evn.home", "java.class.path"));
        assertFalse(defined("evn.home", "jdk.version"));
    }

    @Test
    public void testGetRootCause() throws Exception {
        Throwable t = SystemUtil.getRootCause(new RuntimeException("not yet implemented", new Exception("K")));
        assertEquals("K", t.getMessage());
        t = SystemUtil.getRootCause(new RuntimeException("Met"));
        assertEquals("Met", t.getMessage());
    }

    @Test
    public void testPrintln() throws Exception {
        SystemUtil.println("");
        SystemUtil.println("For: {%d}, This is your [%s].", 3, "Girl");
    }

}
