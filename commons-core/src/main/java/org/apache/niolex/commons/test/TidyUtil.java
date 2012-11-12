/**
 * TidyUtil.java
 *
 * Copyright 2011 Niolex, Inc.
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
package org.apache.niolex.commons.test;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-5-27
 */
public abstract class TidyUtil {

	/**
	 * Remove the prefix at every line, from the input string.
	 * @param str
	 * @param prefix
	 * @return the result
	 */
	public static final String removePrefix(String str, int prefix) {
		String[] lines = str.split("\n");
		StringBuilder sb = new StringBuilder();
		for (String l : lines) {
			if (l.length() > prefix) {
				sb.append(l.substring(prefix));
			}
			sb.append('\n');
		}
		return sb.toString();
	}
}
