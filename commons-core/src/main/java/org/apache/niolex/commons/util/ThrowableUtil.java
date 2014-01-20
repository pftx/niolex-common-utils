/**
 * ThrowableUtil.java
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

import static org.apache.niolex.commons.reflect.FieldUtil.*;

import org.apache.niolex.commons.codec.StringUtil;

/**
 * The utility class for throwable related functions.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-1-20
 */
public class ThrowableUtil {

    /**
     * Get the root cause from the exception chain.
     *
     * @param e the exception
     * @return the root cause
     */
    public static final Throwable getRootCause(Throwable e) {
        Throwable p = e.getCause();
        while (p != null) {
            e = p;
            p = e.getCause();
        }
        return e;
    }

    /**
     * The field separator used for string a throwable object.
     */
    public static final String FIELD_SEPARATOR = "/^*(V)*^/";

    /**
     * Translate the throwable into string.
     * <pre>
     * We have 5 elements in the string:
     * [0] throwable class name
     * [1] throwable message
     * [2] throwable root cause class name
     * [3] throwable root cause message
     * [4] throwable root cause throw position
     * </pre>
     *
     * @param e the throwable to be translated
     * @return the result string
     */
    public static final String throwableToString(Throwable e) {
        StringBuilder sb = new StringBuilder();
        sb.append(e.getClass().getName()).append(FIELD_SEPARATOR).append(e.getMessage());

        Throwable r = getRootCause(e);
        if (r != e) {
            sb.append(FIELD_SEPARATOR).append(r.getClass().getName());
            sb.append(FIELD_SEPARATOR).append(r.getMessage());
        }

        sb.append(FIELD_SEPARATOR);

        StackTraceElement[] elements = r.getStackTrace();
        if (elements != null && elements.length > 0) {
            StackTraceElement el = elements[0];
            sb.append(el.getFileName()).append(":").append(el.getLineNumber());
            sb.append(" => ").append(el.getClassName()).append("#");
            sb.append(el.getMethodName()).append("(..)");
        } else {
            sb.append("NO_STACK_STRACE");
        }
        return sb.toString();
    }

    /**
     * Translate this string into throwable.
     *
     * @param s the string must be generated by {@link #throwableToString(Throwable)}
     * @return the throwable
     * @throws Exception if necessary
     */
    public static final Throwable strToThrowable(String s) throws Exception {
        String[] items = StringUtil.split(s, FIELD_SEPARATOR, true);
        if (items.length != 3 && items.length != 5) {
            return null;
        }

        Throwable e = (Throwable)Class.forName(items[0]).newInstance();
        setValue(e, "detailMessage", items[1]);

        Throwable r = null;
        if (items.length == 5) {
            r = (Throwable)Class.forName(items[2]).newInstance();
            setValue(r, "detailMessage", items[3]);
            setValue(r, "cause", new Throwable(items[4]));
        } else {
            r = new Throwable(items[2]);
        }

        setValue(e, "cause", r);
        return e;
    }

}
