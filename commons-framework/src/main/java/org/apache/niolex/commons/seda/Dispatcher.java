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
 * Every dispatcher is independent from each other, construct the stages
 * before start to use the dispatcher. Once it's running, do not add any
 * stage into the dispatcher.
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
	 * The adjuster used to adjust all the stages in this dispatcher.
	 */
	private Adjuster adjuster;

	/**
	 * Get the global instance.
	 *
	 * @return the global instance
	 */
	public static final Dispatcher getInstance() {
		return INSTANCE;
	}

	/**
	 * Register a stage with it's stage name.
	 * <br><b>
	 * Call this method before dispatch messages.
	 * </b>
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
	 * @param <K> the stage type
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
	 * <br><b>
     * Call this method before dispatch messages.
     * </b>
	 */
	public void construction() {
		for (Stage<?> s : this.getAllStages()) {
			s.construct();
		}
	}

	/**
	 * Start to adjust all the stages in this dispatcher.
	 *
	 * @param adjustInterval the adjust interval.
	 */
	public synchronized void startAdjust(int adjustInterval) {
		if (adjuster == null) {
			adjuster = new Adjuster();
			for (Stage<?> s : this.getAllStages()) {
				adjuster.addStage(s);
			}
		}
		adjuster.setAdjustInterval(adjustInterval);
		adjuster.startAdjust();
	}

	/**
	 * Shutdown all the stages in this dispatcher. We will stop the
	 * adjuster if there is one.
	 */
	public synchronized void shutdown() {
		for (Stage<?> s : this.getAllStages()) {
			s.shutdown();
		}
		if (adjuster != null) {
			adjuster.stopAdjust();
			adjuster = null;
		}
	}

	/**
	 * Clear all the stages in this dispatcher.
	 */
	public void clear() {
		stageMap.clear();
	}

	/**
	 * Dispatch the message to the stage with this stage name.
	 *
	 * @param <T> the message type
	 * @param stageName the name of the stage you want to dispatch this message to
	 * @param msg the message to be dispatched
	 * @return true if dispatch success, false if stage not found
	 * @throws ClassCastException if this message can not be processed by the corresponding stage
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
	 * @param <T> the message type
	 * @param msg the message to be dispatched
	 * @return true if dispatch success, false if stage not found
	 * @throws ClassCastException If this message can not be processed by the corresponding stage
	 */
	public <T extends Message> boolean dispatch(T msg) {
		return dispatch(msg.getClass().getName(), msg);
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
