/**
 * ThrowableUtilTest.java
 *
 * Copyright 2014 the original author or authors.
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
package org.apache.niolex.commons.util;


import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.niolex.commons.reflect.FieldUtil;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-1-20
 */
public class ThrowableUtilTest extends ThrowableUtil {

    public void makeEx() {
        throw new RuntimeException("Go away!");
    }

    public void wrapEx() throws IOException {
        try {
            makeEx();
        } catch (Exception e) {
            throw new IOException("Wrap it.", e);
        }
    }

    @Test(expected=IOException.class)
    public void testGetRootCause() throws Exception {
        try {
            wrapEx();
        } catch (Exception e) {
            Throwable m = getRootCause(e);
            assertTrue(m instanceof RuntimeException);
            throw e;
        }
    }

    @Test
    public void testThrowableToString() throws Exception {
        try {
            wrapEx();
        } catch (Exception e) {
            String s = throwableToString(e);
            System.out.println(s);
            Throwable t = strToThrowable(s);
            System.out.println(t);
            assertTrue(t instanceof IOException);
            assertTrue(t.getCause() instanceof RuntimeException);
        }
    }

    @Test
    public void testStrToThrowable() throws Exception {
        Exception e = new IOException("Wrap it.", new NoTraceException("Go away!"));
        FieldUtil.setValue(e.getCause(), "stackTrace", null);
        String s = throwableToString(e);
        System.out.println(s);
        Throwable t = strToThrowable(s);
        System.out.println(t);
        assertTrue(t instanceof IOException);
        assertTrue(t.getCause() instanceof NoTraceException);
    }

    @Test
    public void testStrToThrowableNoRoot() throws Exception {
        Exception e = new RuntimeException("Wrap it.");
        String s = throwableToString(e);
        System.out.println(s);
        Throwable t = strToThrowable(s);
        System.out.println(t);
        assertTrue(t instanceof RuntimeException);
    }

    @Test
    public void testStrToThrowableInvalid() throws Exception {
        assertNull(strToThrowable("Love This $)%*"));
    }

}

class NoTraceException extends Exception {

    private static final long serialVersionUID = 8229833515493632661L;

    public NoTraceException() {
        super();
    }

    public NoTraceException(String message) {
        super(message);
    }

    public StackTraceElement[] getStackTrace() {
        return null;
    }

}
