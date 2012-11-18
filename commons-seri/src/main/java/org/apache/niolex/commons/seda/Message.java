/**
 * Message.java
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
 * This is the basic class for all the messages flow through the seda system.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5, $Date: 2012-11-15$
 */
public abstract class Message {

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
	 * Reject this message from a stage. This means this message will not get
	 * processed correctly. User need to deal with it.
	 *
	 * The default implementation will dispatch this message to the reject handler with
	 * in instance of {@link RejectMessage}
	 *
	 * User can override this method, but dealing this method need to be fast and
	 * effective, do not take too much time from the rejection thread.
	 *
	 * @param type the reject type
	 * @param info the related rejection information
	 * @param dispatcher the dispatcher used to dispatch this message
	 */
	public void reject(RejectType type, Object info, Dispatcher dispatcher) {
		dispatcher.dispatch(new RejectMessage(type, info, this));
	}

}
