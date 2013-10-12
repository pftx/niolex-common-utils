/**
 * Runme.java
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
package org.apache.niolex.commons.util;

import org.apache.niolex.commons.concurrent.ThreadUtil;

/**
 * This Runme utility class is for run a job periodically.
 * If you want to run only once, do not use it.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, Date: 2012-6-28
 * @see org.apache.niolex.commons.util.Runner
 */
public abstract class Runme extends Thread {

	/**
	 * Current working status
	 */
	private volatile boolean isWorking = false;

	/**
	 * Sleep interval between each run.
	 */
	private long sleepInterval = 1000;

	/**
     * Constructor to create Runme.
     */
    public Runme() {
        super("Runme");
    }

    /**
	 * Start this thread.
	 *
	 * Override super method
	 * @see java.lang.Thread#start()
	 */
	public void start() {
		this.setDaemon(true);
		super.start();
	}

	/**
	 * We are run.
	 *
	 * Override super method
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		if (isWorking) {
			// Do initial sleep.
		    ThreadUtil.sleep(System.nanoTime() % sleepInterval);
		} else {
			isWorking = true;
		}
		while (isWorking) {
			try {
				runMe();
			} catch (Exception e) {}
			ThreadUtil.sleep(sleepInterval);
		}
	}

	/**
	 * The real working method. Subclass should implement this.
	 */
	public abstract void runMe();

	/**
	 * Stop this thread, but not immediately.
	 */
	public void stopMe() {
		isWorking = false;
	}

	/**
	 * Set the sleep interval between each run.
	 * @param sleepInterval
	 */
	public void setSleepInterval(long sleepInterval) {
		this.sleepInterval = sleepInterval;
	}

	/**
	 * Set whether you need we to sleep a little time before run.
	 * @param sleep
	 */
	public void setInitialSleep(boolean sleep) {
		this.isWorking = sleep;
	}

}
