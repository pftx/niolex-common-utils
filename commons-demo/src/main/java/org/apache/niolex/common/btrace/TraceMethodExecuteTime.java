/**
 * TraceMethodExecuteTime.java
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
package org.apache.niolex.common.btrace;

import static com.sun.btrace.BTraceUtils.*;
import com.sun.btrace.annotations.Kind;
import com.sun.btrace.annotations.Location;
import com.sun.btrace.annotations.OnMethod;
import com.sun.btrace.annotations.Return;
import com.sun.btrace.annotations.TLS;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-8-29
 */
public class TraceMethodExecuteTime {

	@TLS
	static long beginTime;

	@OnMethod(clazz = "CaseObject", method = "execute")
	public static void traceExecuteBegin() {
		beginTime = timeMillis();
	}

	@OnMethod(clazz = "CaseObject", method = "execute",
			location = @Location(Kind.RETURN))
	public static void traceExecute(int sleepTime, @Return boolean result) {
		println(strcat(strcat("CaseObject.execute time is:",
				str(timeMillis() - beginTime)), "ms"));
	}
}
