/**
 * Dispatcher.java
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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * The dispatcher used to dispatch messages into the next stage.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5, $Date: 2012-11-16$
 */
public class Dispatcher {

	/**
	 * the global instance.
	 */
	private static final Dispatcher INSTANCE = new Dispatcher();

	/**
	 * The internal stage map.
	 */
	private final Map<String, Stage<?>> stageMap = new HashMap<String, Stage<?>>();

	/**
	 * Get the global instance.
	 *
	 * @return the global instance.
	 */
	public static final Dispatcher getInstance() {
		return INSTANCE;
	}

	/**
	 * Register a stage with it's stage name.
	 *
	 * @param stage the stage to register.
	 * @return the previous value associated with this stage name, or null if there was no mapping
	 * for this stage name.
	 */
	public Stage<?> register(Stage<?> stage) {
		return stageMap.put(stage.getStageName(), stage);
	}

	/**
	 * Get the stage with this stage name.
	 *
	 * @param stageName the stage name of the stage you want to get.
	 * @return the stage instance, null if stage not found.
	 * @throws ClassCastException If the type of the corresponding stage is not the same
	 * as your parameter class.
	 */
	@SuppressWarnings("unchecked")
	public <K extends Stage<?>> K getStage(String stageName) {
		return (K) stageMap.get(stageName);
	}

	/**
	 * When all the stages are set, user need to call this method to construct
	 * the stage network.
	 */
	public void construction() {
		for (Stage<?> s : this.getAllStages()) {
			s.construct();
		}
	}

	/**
	 * Dispatch the message to the stage with this stage name.
	 *
	 * @param stageName the stage name of the stage you want to dispatch.
	 * @param msg the message need to dispatch.
	 * @return true if dispatch success, false if stage not found.
	 * @throws ClassCastException If this message can not be processed by the corresponding stage.
	 */
	public <T extends Message> boolean dispatch(String stageName, T msg) {
		@SuppressWarnings("unchecked")
		Stage<T> stage = (Stage<T>) stageMap.get(stageName);
		if (stage != null) {
			stage.addInput(msg);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Dispatch the message to the stage with the class name of this message
	 * as the stage name.
	 *
	 * @param msg the message need to dispatch.
	 * @return true if dispatch success, false if stage not found.
	 * @throws ClassCastException If this message can not be processed by the corresponding stage.
	 */
	public <T extends Message> boolean dispatch(T msg) {
		return dispatch(msg.getClass().toString(), msg);
	}

	/**
	 * Get all the stages in this dispatcher.
	 *
	 * @return the stages collection
	 */
	public Collection<Stage<?>> getAllStages() {
		return stageMap.values();
	}

}
