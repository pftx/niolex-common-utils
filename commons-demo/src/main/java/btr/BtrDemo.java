/**
 * BtrDemo.java
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
package btr;

import static com.sun.btrace.BTraceUtils.*;

import com.sun.btrace.annotations.Kind;
import com.sun.btrace.annotations.Location;
import com.sun.btrace.annotations.OnMethod;
import com.sun.btrace.annotations.ProbeClassName;
import com.sun.btrace.annotations.ProbeMethodName;
import com.sun.btrace.annotations.Self;
import com.sun.btrace.annotations.TargetMethodOrField;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-8-28
 */
public class BtrDemo {

    @OnMethod(clazz="org.apache.niolex.common.btrace.BtraceDemo", method="/.*/",
            location=@Location(value=Kind.CALL, clazz="/.*/", method="/.*/"))
	public static void m(@Self Object self, @TargetMethodOrField String method,
			@ProbeMethodName String probeMethod) {
		// all calls to the methods with signature "()"
		println(strcat(method, strcat(" was called in ", probeMethod)));
	}

	@OnMethod(clazz = "java.lang.Thread",
			location = @Location(value = Kind.LINE, line = -1))
	public static void online(@ProbeClassName String pcn, @ProbeMethodName String pmn,
			int line) {
		print(strcat(pcn, "."));
		print(strcat(pmn, ":"));
		println(line);
	}
}
