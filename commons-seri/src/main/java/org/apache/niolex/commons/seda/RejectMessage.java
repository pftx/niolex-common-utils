/**
 * RejectMessage.java
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
package org.apache.niolex.commons.seda;

/**
 * The global reject message class, store all the information when a message is rejected.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2012-11-18$
 */
public class RejectMessage extends Message {

	/**
	 * The reject type
	 */
	private final RejectType type;

	/**
	 * The information send along with rejection
	 */
	private final Object info;

	/**
	 * The message been rejected
	 */
	private final Message rejected;

	/**
	 * The Constructor.
	 *
	 * @param type The reject type
	 * @param info The information send along with rejection
	 * @param rejected The message been rejected
	 */
	public RejectMessage(RejectType type, Object info, Message rejected) {
		super();
		this.type = type;
		this.info = info;
		this.rejected = rejected;
	}

	public RejectType getType() {
		return type;
	}

	public Object getInfo() {
		return info;
	}

	public Message getRejected() {
		return rejected;
	}

}
