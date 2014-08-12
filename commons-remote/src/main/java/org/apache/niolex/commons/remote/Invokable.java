/**
 * Invokable.java
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
package org.apache.niolex.commons.remote;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Implement this interface then your code can be invoked from Bean Server.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2012-7-26
 */
public interface Invokable {

	/**
	 * Do real work here.
	 *
	 * @param out the output stream for users to write invoke result
	 * @param args the arguments from remote client
	 * It's in the following format:<pre>
	 * Index	Explain
	 * 0		Command Name
	 * 1		Object Path
	 * 2		Extension Argument 1 (Optional)
	 * 3		Extension Argument 2 (Optional)</pre>
	 * @throws IOException
	 */
	public void invoke(OutputStream out, String[] args) throws IOException;

}
