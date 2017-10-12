/**
 * Adjuster.java
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

import java.util.LinkedList;

import org.apache.niolex.commons.concurrent.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Adjuster used to dynamically adjust thread pool size of stages.
 * <br>
 * We will use an independent thread to call {@link Stage#adjustThreadPool()} periodically,
 * all the details of adjusting thread pool size are implemented there. This thread is running
 * in the daemon state, it will stop automatically if all the main threads were stopped.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2012-11-16$
 */
public class Adjuster implements Runnable {
    protected static final Logger LOG = LoggerFactory.getLogger(Adjuster.class);

	/**
	 * The list to save all the stages.
	 */
	private final LinkedList<Stage<?>> stageList = new LinkedList<Stage<?>>();

	/**
	 * The thread used to run this adjuster.
	 */
	private Thread thread;

	/**
	 * The current working status.
	 */
	private volatile boolean isWorking;

	/**
	 * The adjust interval.
	 */
	private int adjustInterval = 1000;

	/**
	 * Add a new stage to the adjuster.
	 *
	 * @param s the stage need to be adjusted.
	 */
	public synchronized void addStage(Stage<?> s) {
		stageList.add(s);
	}

	/**
	 * Start to adjust stages.
	 */
	public void startAdjust() {
		if (thread == null) {
			thread = new Thread(null, this, "seda-Adjuster");
			thread.setDaemon(true);
			isWorking = true;
			thread.start();
		}
	}

	/**
	 * Stop this adjuster.
	 */
	public void stopAdjust() {
		if (thread != null) {
			isWorking = false;
			thread.interrupt();
			thread = null;
		}
	}

	/**
	 * Adjust all the stages from time to time.
	 *
	 * Override super method
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		long in;
		while (isWorking) {
			in = System.currentTimeMillis();
			adjust();
			ThreadUtil.sleep(adjustInterval - in + System.currentTimeMillis());
		}
	}

	/**
	 * Adjust all the stages added to this adjuster.
	 */
	public synchronized void adjust() {
        for (Stage<?> s : stageList) {
            try {
                s.adjustThreadPool();
            } catch (Throwable t) {
                LOG.warn("Error occurred when adjust stage [{}].", s.getStageName(), t);
            }
        }
	}

	/**
	 * Get adjust interval
	 *
	 * @return current adjust interval
	 */
	public int getAdjustInterval() {
		return adjustInterval;
	}

	/**
	 * Set adjust interval
	 *
	 * @param adjustInterval the new internal to set
	 */
	public void setAdjustInterval(int adjustInterval) {
		this.adjustInterval = adjustInterval;
	}

}
