/**
 * Btrsc.java
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

import org.apache.niolex.common.btrace.BtraceDemo;

import com.sun.btrace.AnyType;
import com.sun.btrace.annotations.*;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-8-28
 */
@BTrace
public class Btrsc {

    @OnMethod(clazz="org.apache.niolex.common.btrace.BtraceDemo", method="/.*/",
    		location=@Location(value=Kind.CALL, clazz="/.*/", method="/.*/"))
    public static void call(@Self Object self, @TargetMethodOrField String method,
    		@ProbeMethodName String probeMethod, AnyType[] args) {
    	// all calls to the methods with signature "()"
    	print(strcat("in ", probeMethod));
    	print(strcat(strcat("\tcall\t", method), " args "));
    	printArray(args);
    }

    @OnMethod(clazz = "org.apache.niolex.common.btrace.BtraceDemo",
			location = @Location(value = Kind.LINE, line = -1))
	public static void line(@ProbeClassName String pcn, @ProbeMethodName String pmn,
			int line) {
		print(strcat(pcn, "."));
		print(strcat(pmn, ": "));
		println(line);
	}

	@OnMethod(
			clazz = "org.apache.niolex.common.btrace.BtraceDemo",
			method = "inputHashCode",
			location = @Location(Kind.RETURN))
	public static void traceExecute(@Self BtraceDemo instance, String s, @Return int result,
			@Duration long duration) {
		println("--------------------Calling BtraceDemo.inputHashCode--------------------");
		println(strcat("The input is:\t", s));
		println(strcat("The return is:\t", str(result)));
		println(strcat("The totalSleepTime is:\t", str(get(
				field("org.apache.niolex.common.btrace.BtraceDemo", "totalSleepTime"), instance))));
		println(strcat("The Execution Time is:\t", str(duration / 1000000)));
		println("------------------------------------------------------------------------");
	}
}
