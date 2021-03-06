/**
 * SystemUtil.java
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

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

import org.apache.niolex.commons.concurrent.ThreadUtil;
import org.apache.niolex.commons.test.TidyUtil;

/**
 * System Environment and JVM related utility class.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, Date: 2012-8-1
 */
public class SystemUtil {

	/**
	 * Safely close the Closeable, without throwing any exception.
	 *
	 * @param cl the object you want to close
	 * @return null if success, the exception if exception occurred
	 */
	public static final IOException close(Closeable cl) {
	    try {
	        if (cl != null) cl.close();
	        return null;
	    } catch (IOException e) {
	        return e;
	    }
	}

	/**
	 * Safely close the Socket, without throwing any exception.
	 *
	 * @param cl the socket you want to close
	 * @return null if success, the exception if exception occurred
	 */
	public static final IOException close(Socket cl) {
	    try {
	        if (cl != null) cl.close();
	        return null;
	    } catch (IOException e) {
	        return e;
	    }
	}

	/**
	 * Try to get system property according to the specified order of property keys.
	 *
	 * @param args the specified property keys
	 * @return the first found property, null if all keys not found
	 */
	public static final String getSystemProperty(String ...args) {
	    for (String s : args) {
	        String p = System.getProperty(s);
	        if (p != null) {
	            return p;
	        }
	    }
	    return null;
	}

	/**
	 * Try to get the system property by the specified property name, if not found or any error occurred,
	 * return the default value.
	 *
	 * @param propName the property name
	 * @param defaultValue the default value
	 * @return the property value or the default value if property not found or any error occurred
	 */
	public static final String getSystemPropertyWithDefault(String propName, String defaultValue) {
	    try {
	        String value = System.getProperty(propName);
            return value == null ? defaultValue : value;
        } catch (Exception e) {
            return defaultValue;
        }
	}

	/**
	 * Try to get the system property by the specified property name, if any error occurred,
	 * return the default value.
	 *
	 * @param propName the property name
	 * @param defaultValue the default value
	 * @return the property value or the default value if error occurred
	 */
	public static final int getSystemPropertyAsInt(String propName, int defaultValue) {
	    try {
            return Integer.parseInt(System.getProperty(propName));
        } catch (Exception e) {
            return defaultValue;
        }
	}

	/**
	 * Check whether the specified system properties were defined or not.
	 *
	 * @param args the specified property keys
	 * @return true if defined, false otherwise
	 */
	public static final boolean defined(String ...args) {
	    return getSystemProperty(args) != null;
	}

	/**
	 * Get the root cause from the exception chain.
	 *
	 * @param e the exception
	 * @return the root cause
	 */
	public static final Throwable getRootCause(Throwable e) {
        return ThrowableUtil.getRootCause(e);
	}

    /**
     * Make the current thread sleep, do not care about the exception.
     *
     * @param milliseconds the time to sleep in milliseconds
     */
    public static final void sleep(long milliseconds) {
        ThreadUtil.sleep(milliseconds);
    }

    /**
     * Format the output and print it into system out.
     *
     * @param s the format string
     * @param args the argument list
     */
    public static final void println(String s, Object ... args) {
        System.out.println(String.format(s, args));
    }

    /**
     * Format the inputs as a table and print it to the system standard output.
     *
     * @param colLen the columns length
     * @param titles the columns titles
     * @param values the table body
     * @see TidyUtil#generateTable(int[], String[], Object...)
     */
    public static final void printTable(int[] colLen, String[] titles, Object ...values) {
        System.out.println(TidyUtil.generateTable(colLen, titles, values));
    }

}
