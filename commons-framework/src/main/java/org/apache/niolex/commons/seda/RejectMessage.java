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
public class RejectMessage implements Message {

    /**
     * The message rejection event type.
     *
     * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
     * @version 1.0.5, $Date: 2012-11-16$
     */
    public static enum RejectType {
        PROCESS_ERROR,
        USER_REJECT,
        STAGE_SHUTDOWN,
        STAGE_BUSY;
    }

	/**
	 * The reject type
	 */
	private final RejectType type;

	/**
	 * The information send along with rejection, explained in detail:<pre>
     *     When reject type is:
     *         PROCESS_ERROR then info is an instance of Throwable
     *         USER_REJECT then info is defined by user application
     *         STAGE_SHUTDOWN then info is the stage name
     *         STAGE_BUSY then info is a reference to the stage object
     *     User can use this parameter accordingly.</pre>
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

	/**
	 * @return the reject type of this message
	 */
	public RejectType getType() {
		return type;
	}

	/**
	 * @return the information send along with rejection, explained in detail:<pre>
     *     When reject type is:
     *         PROCESS_ERROR then info is an instance of Throwable
     *         USER_REJECT then info is defined by user application
     *         STAGE_SHUTDOWN then info is the stage name
     *         STAGE_BUSY then info is a reference to the stage object
     *     User can use this parameter accordingly.</pre>
	 */
	public Object getInfo() {
		return info;
	}

	/**
	 * @return the message been rejected
	 */
	public Message getRejected() {
		return rejected;
	}

}
