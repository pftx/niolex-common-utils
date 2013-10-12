/**
 * Runner.java
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

import java.lang.reflect.Method;

import org.apache.commons.lang.ClassUtils;
import org.apache.niolex.commons.reflect.MethodUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the utility class to run a method in a different thread.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, Date: 2012-7-12
 */
public class Runner {
	private static final Logger LOG = LoggerFactory.getLogger(Runner.class);

	/**
	 * Run the given method in that object once.
	 *
	 * @param host
	 * @param methodName
	 * @param args The argument of the given method.
	 * @return The thread which is used to run this method.
	 */
	public static Thread run(final Object host, final String methodName, final Object ...args) {
		Thread t = new Thread(){
			public void run() {
				try {
				    Method[] ms = MethodUtil.getMethods(host, methodName);
				    // Prepare ARGS class.
				    Class<?>[] argsTypes = new Class<?>[args.length];
				    for (int i = 0; i < args.length; ++i) {
				        argsTypes[i] = args[i].getClass();
				    }
				    // Check all methods to find the correct one.
					for (Method m : ms) {
					    if (!ClassUtils.isAssignable(argsTypes, m.getParameterTypes(), true)) {
					        continue;
					    }
					    m.setAccessible(true);
					    m.invoke(host, args);
					    break;
					}
				} catch (Exception e) {
					LOG.warn("Error occured in Runner#run method.", e);
				}
			}
		};
		t.start();
		return t;
	}

	/**
	 * Run the given runnable once.
	 *
	 * @param run
	 * @return The thread which is used to run this method.
	 */
	public static Thread run(Runnable run) {
		Thread t = new Thread(run);
		t.start();
		return t;
	}
}
