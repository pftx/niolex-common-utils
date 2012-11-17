/**
 * Stage.java
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

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The base class for SEDA stage implementation. User will need to subclass this to
 * add their own business logic.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5, $Date: 2012-11-16$
 */
public abstract class Stage<Input extends Message> {
	protected static final Logger LOG = LoggerFactory.getLogger(Stage.class);

	/**
	 * The minimum adjust interval in milliseconds.
	 */
	protected static final int MIN_ADJUST_INTERVAL = 1000;

	/**
	 * The name of this stage.
	 */
	protected final String stageName;

	/**
	 * The input queue used to store all the input messages.
	 */
	protected final BlockingQueue<Input> inputQueue;

	/**
	 * The dispatcher used to dispatch all the output messages.
	 */
	protected final Dispatcher dispatcher;

	/**
	 * The minimum thread pool size.
	 */
	protected final int minPoolSize;

	/**
	 * The maximum thread pool size.
	 */
	protected final int maxPoolSize;

	/**
	 * The maximum input queue size. If the current queue size exceeds this number, we will add
	 * more threads into the thread pool.
	 */
	protected final int adjustFactor;

	/**
	 * The execution count, used to calculate thread pool efficiency.
	 */
	protected final AtomicInteger exeCnt = new AtomicInteger(0);

	/**
	 * The list to save all the workers.
	 */
	private final LinkedList<Worker> workerList = new LinkedList<Worker>();

	/**
	 * The thread group used to create all the threads.
	 */
	private final ThreadGroup group;

	/**
	 * The current status of this stage.
	 *
	 *   RUNNING:  Accept new tasks and process queued tasks
     *   SHUTDOWN: Don't accept new tasks, but process queued tasks
     *   STOP:     Don't accept new tasks, don't process queued tasks,
     *             and interrupt in-progress tasks
     *   TERMINATED: Same as STOP, plus all threads have terminated
	 */
	protected volatile int stageStatus;
	static final int RUNNING    = 0;
    static final int SHUTDOWN   = 1;
    static final int STOP       = 2;
    static final int TERMINATED = 3;

    /**
     * The current thread pool size.
     */
    protected volatile int currentPoolSize;

    /**
     * The last thread pool adjust time.
     */
    protected long lastAdjustTime;

    /**
     * The last input queue size.
     */
    protected int lastAdjustQueueSize;

	/**
	 * The recent stable consume rate.
	 */
    protected double stableRate;

    /**
     * The last adjust status.
     *
     * ADD_2:	Last time we added two threads.
     * SUB_2:	Last time we subtracted two threads.
     * ...
     */
    protected int lastAdjustStatus;
    static final int ADD_4 = 4;
    static final int ADD_3 = 3;
    static final int ADD_2 = 2;
    static final int ADD_1 = 1;
    static final int NO_OP = 0;
    static final int SUB_1 = -1;
    static final int SUB_2 = -2;

	/**
	 * Create a Stage with all default parameters.
	 *
	 * We will initialize this stage with the following parameters:
	 * use LinkedBlockingQueue for inputQueue
	 * use default instance of Dispatcher for dispatcher
	 * use 1 for minPoolSize
	 * use 100 for maxPoolSize
	 * use 4096 for adjustFactor
	 *
	 * @param stageName the name of this stage.
	 */
	public Stage(String stageName) {
		this(stageName, new LinkedBlockingQueue<Input>(), Dispatcher.getInstance(), 1, 100, 4096);
	}

	/**
	 * Create a Stage with these parameters you passed in.
	 *
	 * When the queue exceeds 4 times adjustFactor, we will start to reject messages.
	 * User can change this behavior by override the method {@link #dropMessage()}
	 *
	 * @param stageName the name of this stage.
	 * @param inputQueue the input queue user want to use.
	 * @param dispatcher the dispatcher used to dispatch output.
	 * @param minPoolSize the minimum thread pool size.
	 * @param maxPoolSize the maximum thread pool size.
	 * @param adjustFactor the maximum input queue size when we need to add threads.
	 */
	public Stage(String stageName, BlockingQueue<Input> inputQueue, Dispatcher dispatcher,
			int minPoolSize, int maxPoolSize, int adjustFactor) {
		super();
		this.stageName = stageName;
		this.inputQueue = inputQueue;
		this.dispatcher = dispatcher;
		this.minPoolSize = minPoolSize;
		this.maxPoolSize = maxPoolSize;
		this.adjustFactor = adjustFactor;
		this.group = new ThreadGroup(stageName);
		startPool();
	}

	/**
	 * The inner class to work in thread pool.
	 *
	 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
	 * @version 1.0.5, $Date: 2012-11-16$
	 */
	protected class Worker implements Runnable {

		/**
		 * The thread used to run this worker.
		 */
		private final Thread thread;

		/**
		 * Create a new worker.
		 */
		private Worker() {
			super();
			this.thread = new Thread(group, this);
			thread.start();
		}

		/**
		 * The current worker working status.
		 */
		private boolean isWorking = true;

		/**
		 * All the threads inside thread pool will run this method.
		 * We make this method final, to prevent subclass override it.
		 *
		 * Override super method
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public final void run() {
			// We run this loop endlessly.
			while (stageStatus < Stage.STOP && isWorking) {
				Input in = null;
				try {
					if (stageStatus == RUNNING) {
						// Take an element from input queue, wait if necessary.
						in = inputQueue.take();
					} else if (stageStatus == SHUTDOWN) {
						// We are already in shutdown state, do not wait.
						in = inputQueue.poll();
					}
					if (in == null) {
						// No input data to process, we break this loop.
						break;
					}
					process(in, dispatcher);
				} catch (Throwable t) {
					// Reject the message if it's not null.
					if (in != null) {
						LOG.error("Error occured when process message in stage[{}]:", stageName, t);
						in.reject(Message.RejectType.PROCESS_ERROR, t);
					}
				} finally {
					// Add the execution count.
					exeCnt.incrementAndGet();
				}
			}
			if (stageStatus == SHUTDOWN) {
				// Try to terminate this stage if it has been shutdown.
				tryTerminate();
			}
		}

		/**
		 * Get The current worker working status.
		 *
		 * @return The current worker working status.
		 */
		public boolean isWorking() {
			return isWorking;
		}

		/**
		 * Set The current worker working status.
		 *
		 * @param isWorking the new worker working status.
		 */
		public void setWorking(boolean isWorking) {
			this.isWorking = isWorking;
		}

		/**
		 * Interrupt this thread, to make sure it's not waiting on the input queue.
		 */
		public void interrupt() {
			thread.interrupt();
		}

	}

	/**
	 * Construct the stage map in this method. We do nothing here, just a hook for subclass.
	 *
	 * Any subclass can override this method to get their output stage from the dispatcher and
	 * save it to local fields for faster dispatch speed.
	 * User can also use this method to check whether or not all the mandatory stages are
	 * registered and do appropriate action about it.
	 */
	public void construct() {
		// We do nothing here.
	}

	/**
	 * We will call this method when the input queue size is greater than 4 times adjustFactor.
	 * User can override this method to change the default behavior.
	 *
	 * @return the number of messages dropped.
	 */
	protected int dropMessage() {
		int size = inputQueue.size() - 2 * adjustFactor;
		Input in;
		for (int i = 0; i < size; ++i) {
			in = inputQueue.poll();
			if (in != null) {
				in.reject(Message.RejectType.STAGE_BUSY, this);
			} else {
				return i;
			}
		}
		return size;
	}

	/**
	 * Start the internal thread pool and initialize parameters, make this
	 * stage ready for process inputs.
	 */
	protected void startPool() {
		// Add threads to make this pool at least contains the number of minPoolSize threads.
		while (currentPoolSize < this.minPoolSize) {
			addThread();
		}
		lastAdjustTime = System.currentTimeMillis();
	}

	/**
	 * Add a new thread to this thread pool.
	 */
	protected synchronized void addThread() {
		Worker worker = new Worker();
		// A new thread ready, so we update status.
		++currentPoolSize;
		workerList.add(worker);
	}

	/**
	 * Adjust the thread pool from time to time.
	 * The {@link Adjuster} will use this method to dynamically adjust thread poll size.
	 *
	 * @return current thread pool size.
	 */
	public int adjustThreadPool() {
		if (System.currentTimeMillis() - lastAdjustTime < MIN_ADJUST_INTERVAL || stageStatus > RUNNING) {
			// We will not adjust the thread pool too fast.
			// We do not adjust a thread pool when it's not running.
			return currentPoolSize;
		}
		// This is the number of messages processed in this interval.
		double intervalCnt = exeCnt.getAndSet(0);
		// the time between last adjust and this adjust.
		long intervalTime = System.currentTimeMillis() - lastAdjustTime;
		// The data consume rate.
		double consumeRate = intervalCnt / intervalTime;
		// Reset the adjust time.
		lastAdjustTime = System.currentTimeMillis();
		// The current input queue size.
		int currentQueueSize = inputQueue.size();
		int dropSize = 0;
		if (currentQueueSize > 4 * adjustFactor) {
			// Too many messages, we will drop some of them.
			dropSize = dropMessage();
			LOG.info("Too many messages in stage [{}], we droped {} messages.", stageName, dropSize);
		}
		// Now compute the input rate.
		double inputRate = (currentQueueSize + intervalCnt - lastAdjustQueueSize) / intervalTime;
		// Reset the last queue size.
		lastAdjustQueueSize = currentQueueSize - dropSize;
		if (currentQueueSize > 256) {
			stableRate = consumeRate / currentPoolSize;
		}
		if (inputRate - consumeRate <= 0.01 && stableRate > 0.001) {
			// When the two rates are very close, we need to compute the consumeRate
			// By another method.
			consumeRate = stableRate * currentPoolSize;
		}
		// Invoke the real adjust method.
		adjustThreadPool(consumeRate, inputRate, currentQueueSize);
		return currentPoolSize;
	}

	/**
	 * Adjust the thread pool size.
	 * User can override this method to provide their own implementation.
	 *
	 * @param consumeRate the data consume rate
	 * @param inputRate the data input rate
	 * @param queueSize the current input queue size.
	 */
	protected void adjustThreadPool(double consumeRate, double inputRate, int queueSize) {
		// The rate: CNT/millisecond
		if (LOG.isDebugEnabled()) {
			// The total rate.
			String crate = new DecimalFormat("#,##0.0000").format(consumeRate);
			String irate = new DecimalFormat("#,##0.0000").format(inputRate);
			LOG.debug("Stage [{}] crate: {}, irate: {}, psize: {}, qsize: {}.", stageName, crate,
					irate, currentPoolSize, queueSize);
		}

		// We check whether we need to add, or subtract threads.
		double thisStatus = inputRate * currentPoolSize / consumeRate - currentPoolSize;
		// This is the core part of adjust thread pool size.
		if (thisStatus > 0.8 || (thisStatus > 0 && queueSize > adjustFactor)) {
			if (currentPoolSize >= maxPoolSize) {
				// We can only adjust to max pool size.
				return;
			}
			// We still have some root for Add threads here.
			if (lastAdjustStatus >= 0) {
				// We are in an rise phase
				addThread();
				// Rise faster if we need more.
				if (thisStatus > 5.5 && currentPoolSize + 3 <= maxPoolSize) {
					addThread();
					addThread();
					addThread();
					lastAdjustStatus = ADD_4;
				} else if (thisStatus > 3.5 && currentPoolSize + 2 <= maxPoolSize) {
					addThread();
					addThread();
					lastAdjustStatus = ADD_3;
				} else if (thisStatus > 2 && currentPoolSize + 1 <= maxPoolSize) {
					addThread();
					lastAdjustStatus = ADD_2;
				} else {
					lastAdjustStatus = ADD_1;
				}
			} else {
				// We are in drop rise phase, so there maybe some tremble here, we need to
				// make it flat.
				if (thisStatus > 2) {
					addThread();
					lastAdjustStatus = ADD_1;
				} else {
					lastAdjustStatus = NO_OP;
				}
			}
		} else if (thisStatus < -0.8 && queueSize < 256) {
			if (currentPoolSize <= minPoolSize) {
				// We can only adjust to min pool size.
				return;
			}
			// Subtract some threads here.
			if (lastAdjustStatus > 0) {
				// We are in rise drop phase, so there maybe some tremble here, we need to
				// make it flat.
				if (thisStatus < -2) {
					subtractThread();
					lastAdjustStatus = SUB_1;
				} else {
					lastAdjustStatus = NO_OP;
				}
			} else {
				// We are in drop phase.
				subtractThread();
				if (thisStatus < -2.5) {
					subtractThread();
					lastAdjustStatus = SUB_2;
				} else {
					lastAdjustStatus = SUB_1;
				}
			}
		} else {
			// We do not adjust the pool size.
			lastAdjustStatus = NO_OP;
		}
	}

	/**
	 * Subtract a thread from the thread pool.
	 */
	protected synchronized void subtractThread() {
		Worker wo = workerList.poll();
		if (wo != null) {
			wo.setWorking(false);
			// This worker is now end of service, we clear it from worker list.
			--currentPoolSize;
		} else {
			LOG.warn("There is no more thread to subtract in stage: {}.", stageName);
		}
	}

	/**
	 * Shutdown this stage and the internal pool.
	 */
	public synchronized void shutdown() {
		stageStatus = SHUTDOWN;
		if (inputQueue.size() <= 2 * currentPoolSize) {
			// We have only a small number of messages, we must wait until it's down.
			// We sleep as most 5 seconds.
			int i = 500;
			while (i-- > 0) {
				try {
					Thread.sleep(10);
					if (inputQueue.size() == 0)
						break;
				} catch (InterruptedException e) {}
			}
			ListIterator<Worker> it = workerList.listIterator();
			// Signal all threads try to terminate.
			while (it.hasNext()) {
				it.next().interrupt();
			}
		}
	}

	/**
	 * Try to terminate this stage.
	 */
	private synchronized void tryTerminate() {
		if (inputQueue.size() == 0) {
			stageStatus = TERMINATED;
			currentPoolSize = 0;
			workerList.clear();
		}
	}

	/**
	 * Add a new input message to this stage.
	 *
	 * @param in the message need to be processed.
	 */
	public void addInput(Input in) {
		if (stageStatus < SHUTDOWN) {
			inputQueue.add(in);
		} else {
			in.reject(Message.RejectType.STAGE_SHUTDOWN, stageName);
		}
	}

	/**
	 * Get the current input queue element size.
	 *
	 * @return the number of elements in the input queue.
	 */
	public int getInputSize() {
		return inputQueue.size();
	}

	/**
	 * Get the current stage name.
	 *
	 * @return the current stage name.
	 */
	public String getStageName() {
		return stageName;
	}

	/**
	 * Get the current stage status.
	 *
	 * @return the current stage status.
	 */
	public int getStageStatus() {
		return stageStatus;
	}

	/**
	 * Process the input, and dispatch output to the next stage if needed.
	 * User need to implement this method in subclass.
	 *
	 * @param in the input message.
	 * @param dispatcher the dispatcher used to dispatch output.
	 */
	protected abstract void process(Input in, Dispatcher dispatcher);

}
