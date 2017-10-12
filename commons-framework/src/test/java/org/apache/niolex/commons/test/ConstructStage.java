/**
 * ConstructStage.java
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
package org.apache.niolex.commons.test;

import java.util.Map;

import org.apache.niolex.commons.seda.Dispatcher;
import org.apache.niolex.commons.seda.RejectMessage.RejectType;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2012-11-17$
 */
public class ConstructStage extends SleepStage {

	private Map<String, String> consMap;

	/**
	 * Constructor
	 * @param stageName
	 */
	public ConstructStage(String stageName, Map<String, String> consMap) {
		super(stageName, 1);
		this.consMap = consMap;
	}

	/**
	 * This is the override of super method.
	 * @see org.apache.niolex.commons.seda.Stage#process(org.apache.niolex.commons.seda.Message, org.apache.niolex.commons.seda.Dispatcher)
	 */
	@Override
	protected void process(TInput in, Dispatcher dispatcher) {
		String tag = consMap.get(in.getTag());
		if (tag == null) {
			reject(RejectType.USER_REJECT, "No way to dispatch.", in);
		}
		dispatcher.dispatch(tag, in);
	}

}
