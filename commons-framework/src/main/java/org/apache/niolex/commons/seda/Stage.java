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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.niolex.commons.concurrent.ThreadUtil;
import org.apache.niolex.commons.seda.RejectMessage.RejectType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The base class for SEDA stage implementation. User will need to subclass this to
 * add their own business logic.<br>
 * Every stage is a thread pool. So please be cautious about the number of stages
 * in your system. Too many stages will overwhelm your JVM.
 * <p>
 * If there are too many messages backlogging in the input queue, we will drop some
 * messages to reduce the waiting time of all the messages. The dropping process is
 * controlled by the maxTolerableDelay. If we found we can not process the input queue
 * in (maxTolerableDelay * DROP_MSG_COEFFICIENT), we will drop more than half of messages
 * and keep only those messages can be processed in (maxTolerableDelay / 2) in order to
 * leave some room for newly coming messages.
 * </p>
 * 
 * Subclasses need to override these methods:
 * 
 * <pre>
 * 1. {@link #construct()} After all the stages are initialized, the dispatcher will call
 *  this methods to give every stage a chance to check there internal status and do some
 *  final work to make it ready for processing inputs.
 * 2. {@link #process(Message, Dispatcher)} Process the message.
 * </pre>
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2012-11-16$
 */
public abstract class Stage<Input extends Message> {
	protected static final Logger LOG = LoggerFactory.getLogger(Stage.class);

	/**
	 * The minimum adjust interval in milliseconds.
	 */
	protected static final int MIN_ADJUST_INTERVAL = 1000;

	protected static final int DROP_MSG_COEFFICIENT = 2;

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
	 * The maximum tolerable delay in milliseconds.
	 * <br>
	 * If the time to process current queue size exceeds {@value #DROP_MSG_COEFFICIENT}
	 * times this number, we will start to drop messages. User can handle the dropped
	 * messages by a special stage if necessary.
	 */
	protected final int maxTolerableDelay;

	/**
	 * The execution count, used to calculate thread pool efficiency.
	 */
	protected final AtomicInteger exeCnt = new AtomicInteger(0);

	/**
	 * The execution time in us, used to calculate thread consume rate.
	 */
	protected final AtomicLong exeTime = new AtomicLong(0);

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
     *   TERMINATED: Same as STOP, plus all threads have been terminated
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
	 * The recent process rate.
	 */
    protected double processRate = 1.01;

    /**
     * The last adjust status.
     * -1 for subtract threads
     * 0 for no operation
     * 1 for add threads
     */
    protected int lastAdjustStatus;
    static final int SUBTRACT   = -1;
    static final int NO_OP      =  0;
    static final int ADD        =  1;

	/**
	 * Create a Stage with some default parameters.
	 * <pre>
	 * We will initialize this stage with the following parameters:
	 * use LinkedBlockingQueue for inputQueue
	 * use the global instance of Dispatcher for dispatcher
	 * use 1 for minPoolSize
	 * use 100 for maxPoolSize
	 * use 1000ms for maxTolerableDelay</pre>
	 *
	 * @param stageName the name of this stage
	 */
	public Stage(String stageName) {
		this(stageName, Dispatcher.getInstance());
	}

	/**
     * Create a Stage with some default parameters.
     * <pre>
     * We will initialize this stage with the following parameters:
     * use LinkedBlockingQueue for inputQueue
     * use 1 for minPoolSize
     * use 100 for maxPoolSize
     * use 1000ms for maxTolerableDelay</pre>
     *
     * @param stageName the name of this stage
     * @param dispatcher the dispatcher used to dispatch outputs
     */
    public Stage(String stageName, Dispatcher dispatcher) {
        this(stageName, new LinkedBlockingQueue<Input>(), dispatcher, 1, 100, 1000);
    }

	/**
     * Create a Stage with some default parameters.
     * <pre>
     * We will initialize this stage with the following parameters:
     * use LinkedBlockingQueue for inputQueue
     * use the global instance of Dispatcher for dispatcher
     * use 1 for minPoolSize
     * use 100 for maxPoolSize</pre>
     *
     * @param stageName the name of this stage
     * @param maxTolerableDelay The maximum tolerable delay in milliseconds.
     * If the time to process current queue size exceeds {@value #DROP_MSG_COEFFICIENT} times this number,
     * we will start to drop messages.
     */
	public Stage(String stageName, int maxTolerableDelay) {
        this(stageName, new LinkedBlockingQueue<Input>(), Dispatcher.getInstance(), 1, 100, maxTolerableDelay);
    }

	/**
	 * Create a Stage with the specified parameters you passed in.
	 *
	 * @param stageName the name of this stage
	 * @param inputQueue the input queue used to store messages
	 * @param dispatcher the dispatcher used to dispatch outputs
	 * @param minPoolSize the minimum thread pool size
	 * @param maxPoolSize the maximum thread pool size
	 * @param maxTolerableDelay The maximum tolerable delay in milliseconds.
     * If the time to process current queue size exceeds {@value #DROP_MSG_COEFFICIENT} times this number,
     * we will start to drop messages. User can change this behavior by override the method {@link #dropMessage(int)}
	 */
	public Stage(String stageName, BlockingQueue<Input> inputQueue, Dispatcher dispatcher,
			int minPoolSize, int maxPoolSize, int maxTolerableDelay) {
		super();
		this.stageName = stageName;
		this.inputQueue = inputQueue;
		this.dispatcher = dispatcher;
		this.minPoolSize = minPoolSize;
		this.maxPoolSize = maxPoolSize;
		this.maxTolerableDelay = maxTolerableDelay;
		this.group = new ThreadGroup(stageName);
		startPool();
	}

	/**
	 * The inner class to process messages. It will create a new thread when a new instance
	 * is created, and work as a thread pool item.
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
		protected Worker() {
			super();
			this.thread = new Thread(group, this);
			thread.start();
		}

		/**
		 * The current working status for this worker.
		 */
		private volatile boolean isWorking = true;

		/**
		 * Get messages from the input queue, and process it.
		 * We make this method final, to prevent subclass from override it.
		 *
		 * Override super method
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public final void run() {
		    Input in = null;
		    long inTime = 0;

		    // We run this loop endlessly.
			while (stageStatus < STOP && isWorking) {
			    // 1. Take an element from input queue, wait if necessary.
			    try {
			        if ((in = takeMessage()) == null) {
			            // No input data to process, we break this loop.
			            break;
			        }
	            } catch (InterruptedException e) {
	                // Check the loop status again when interrupted.
	                continue;
	            }

			    // 2. Process the message.
				try {
					// Calculate process time here. We minus 5us for take object time.
					inTime = System.nanoTime() - 5000;

					process(in, dispatcher);

					// Add the execution count.
                    exeCnt.incrementAndGet();
                    // Add the execution time in us.
                    long delta = (System.nanoTime() - inTime) / 1000;
                    exeTime.addAndGet(delta);
				} catch (Throwable t) {
					LOG.error("Error occured when process message in stage [{}].", stageName, t);
					reject(RejectType.PROCESS_ERROR, t, in);
				}
			}

			if (stageStatus == SHUTDOWN) {
				// Try to terminate this stage if the thread pool has been shutdown.
				tryTerminate();
			}
		}

		/**
		 * Get The current worker working status.
		 *
		 * @return The current worker working status
		 */
		public boolean isWorking() {
			return isWorking;
		}

		/**
		 * Set The current worker working status.
		 *
		 * @param isWorking the new worker working status
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

	} /* End of Worker */

	/**
	 * Take a new message from the input queue.
	 *
	 * @return a input message
	 * @throws InterruptedException if interrupted by others
	 */
	protected Input takeMessage() throws InterruptedException {
	    if (stageStatus == RUNNING) {
            // Take an element from input queue, wait if necessary until an element becomes available.
            return inputQueue.take();
        } else if (stageStatus == SHUTDOWN) {
            // We are already in shutdown state, do not wait.
            return inputQueue.poll();
        } else {
            // Other status, means error.
            return null;
        }
	}

	/**
	 * Construct the stage map in this method. We do nothing here, just a hook for subclass.
	 * <br>
	 * Any subclass can override this method to get their output stages from the dispatcher and
	 * save them to its local fields for faster dispatch speed.<br>
	 * User can also use this method to check whether or not all the mandatory stages have been
	 * registered and do appropriate action accordingly.
	 */
	public void construct() {
		// We do nothing here.
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
	 * Adjust the thread pool from time to time.
	 * The {@link Adjuster} will use this method to dynamically adjust thread poll size.
	 *
	 * @return current thread pool size
	 */
	public int adjustThreadPool() {
		if (System.currentTimeMillis() - lastAdjustTime < MIN_ADJUST_INTERVAL || stageStatus > RUNNING) {
			// We will not adjust the thread pool too fast, and
			// we do not adjust a thread pool when it's not running.
			return currentPoolSize;
		}

		// This is the number of messages processed in this interval.
		double intervalCnt = exeCnt.getAndSet(0);
		// Time is the total process time in us.
		long processTime = exeTime.getAndSet(0);
		// the time between last adjust and this adjust.
		long intervalTime = System.currentTimeMillis() - lastAdjustTime;

		// Reset the adjust time.
		lastAdjustTime = System.currentTimeMillis();

		// The current input queue size.
		int currentQueueSize = inputQueue.size();

		// Now compute the input rate.
		double inputRate = (currentQueueSize + intervalCnt - lastAdjustQueueSize) / intervalTime;
		// The data consume rate.
		double consumeRate = intervalCnt / intervalTime;
		// Calculate the process rate per thread per millisecond.
		if (processTime > 0) {
		    processRate = intervalCnt * 1000 / processTime;
		}

		// Check drop message if necessary.
		int dropSize = 0;
		// The max queue size is {@value #DROP_MSG_COEFFICIENT} times the max messages we can process in the max tolerable delay.
		int maxQueueSize = (int)(DROP_MSG_COEFFICIENT * maxTolerableDelay * processRate * currentPoolSize);
		if (currentQueueSize >  maxQueueSize) {
			// Too many messages, we will drop some of them, keep only half of our capacity.
			dropSize = dropMessage(maxQueueSize / (DROP_MSG_COEFFICIENT * 2));
			LOG.info("Too many messages in stage [{}], we droped {} messages.", stageName, dropSize);
		}

		// Reset the last queue size.
		lastAdjustQueueSize = currentQueueSize - dropSize;
		// Invoke the real adjust method.
		adjustThreadPool(consumeRate, inputRate, currentQueueSize);

		return currentPoolSize;
	}

    /**
     * We will call this method when the input queue size is greater than {@value #DROP_MSG_COEFFICIENT}
     * times the max messages we can process in the max tolerable delay.
     * <br>
     * The default implementation will dispatch this message to the reject handler with
     * an instance of {@link RejectMessage}, user need to add his own reject handler to
     * deal with this message by register a stage with the name
     * "org.apache.niolex.commons.seda.RejectMessage", or this message will be ignored.
     * <br><b>
     * The stage dealing the rejected messages need to be fast and effective, do not take too much time
     * from the rejection thread pool, or that stage will reject messages, too. Those messages will be
     * discarded to avoid an infinite loop.
     * </b><br>
     * User can override this method to change the default behavior.
     *
     * @param keepCnt the number of messages to be kept after the drop
     * @return the number of messages been dropped.
     */
    protected int dropMessage(int keepCnt) {
        int dropCnt = inputQueue.size() - keepCnt;
        int rejectCnt = 0;
        Input in;
        for (int i = 0; i < dropCnt; ++i) {
            in = inputQueue.poll();
            if (in == null) {
                break;
            }
            if (in instanceof RejectMessage) {
                ++rejectCnt;
            } else {
                reject(RejectType.STAGE_BUSY, this, in);
            }
        }

        if (rejectCnt > 0) {
            LOG.warn("#{} RejectMessage droped from stage [{}], this probably means bad program design.",
                    rejectCnt, stageName);
        }

        return dropCnt;
    }

    /**
     * Reject this message from the current stage. This means this message will not get
     * processed correctly. User need to deal with it if necessary.
     * <br>
     * User can override this method to change the default behavior.
     *
     * @param type the reject type
     * @param info the related rejection information, explained in detail:<pre>
     *     When reject type is:
     *         PROCESS_ERROR then info is an instance of Throwable
     *         USER_REJECT then info is defined by user application
     *         STAGE_SHUTDOWN then info is the stage name
     *         STAGE_BUSY then info is a reference to the stage object
     *     User can use this parameter accordingly.</pre>
     * @param msg the message been rejected
     * @see #dropMessage(int)
     * @see #addInput(Message)
     */
    protected void reject(RejectType type, Object info, Message msg) {
        dispatcher.dispatch(new RejectMessage(type, info, msg));
    }

	/**
	 * Adjust the thread pool size.
	 * <br>
	 * User can override this method to provide their own implementation.
	 *
	 * @param consumeRate the data consume rate in CNT/millisecond
	 * @param inputRate the data input rate in CNT/millisecond
	 * @param queueSize the current input queue size.
	 */
	protected void adjustThreadPool(double consumeRate, double inputRate, int queueSize) {
		// The rate: CNT/millisecond
		if (LOG.isDebugEnabled()) {
			// The total rate.
		    DecimalFormat f = new DecimalFormat("#,##0.0000");
		    String irate = f.format(inputRate);
			String crate = f.format(consumeRate);
			String prate = f.format(processRate);
			LOG.debug("Stage [{}] input: {}, consume: {}, process: {}, pool: {}, queue: {}.", stageName,
			        irate, crate, prate, currentPoolSize, queueSize);
		}

		// We check whether we need to add, or subtract threads.
		double thisStatus = inputRate / processRate - currentPoolSize;

		// This is the core part of adjust thread pool size.
		// We use thisStatus and the lastAdjustStatus to avoid tremble.
		if (thisStatus > 0.6 || (thisStatus > 0 && queueSize > maxTolerableDelay * processRate * currentPoolSize)) {
		    // We will need add threads here.
		    int addNumber = 0;

		    // Rule 1. If lastAdjustStatus < 0, then we will try not add so many threads here.
		    if (lastAdjustStatus < 0) {
		        addNumber = (int) (thisStatus * 0.6);
		    } else {
		        addNumber = (int) (thisStatus * 0.8);
		    }

		    // Rule 2. We will try to add at least one thread.
		    if (addNumber == 0) {
		        addNumber = 1;
		    }

		    while (addNumber-- > 0 && currentPoolSize < maxPoolSize) {
	            addThread();
	        }
	        lastAdjustStatus = ADD;
		} else if (thisStatus < -0.8) {
		    // We will need subtract threads here.
		    int subNumber = (int) (thisStatus - 0.2);
		    if (lastAdjustStatus > 0) {
		        ++subNumber;
		    }

		    while (subNumber++ < 0 && currentPoolSize > minPoolSize) {
	            subtractThread();
	        }
	        lastAdjustStatus = SUBTRACT;
		} else {
		    lastAdjustStatus = NO_OP;
		}
	}

	/**
	 * Shutdown this stage and the internal pool. This method will try to terminate the internal
	 * thread pool if the input queue can be processed in a short time, otherwise it will return
	 * immediately and let the pool to terminate itself at an appropriate time.
	 */
	public void shutdown() {
		stageStatus = SHUTDOWN;

		if (inputQueue.size() <= 100 * processRate * currentPoolSize) {
			// We have only a small number of messages, we must wait until it's down.
			// We sleep as most 5 seconds.
			int i = 500;
			while (i-- > 0) {
				ThreadUtil.sleep(10);
				if (inputQueue.size() == 0)
					break;
			}

			// Interrupt all the threads.
            @SuppressWarnings("unchecked")
            LinkedList<Worker> clone = (LinkedList<Worker>) workerList.clone();
			// Signal all threads try to terminate.
			for (Worker w : clone) {
				w.interrupt();
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
	 * Add a new input message to this stage. If the current stage has been shutdown, this method
	 * will reject this message with reject type {@link RejectType#STAGE_SHUTDOWN}.
	 *
	 * @param in the message needs to be processed
	 */
	public void addInput(Input in) {
		if (stageStatus < SHUTDOWN) {
			inputQueue.add(in);
		} else {
			reject(RejectType.STAGE_SHUTDOWN, stageName, in);
		}
	}

	/**
	 * Get the current input queue size.
	 *
	 * @return the number of elements in the input queue
	 */
	public int getInputSize() {
		return inputQueue.size();
	}

	/**
	 * Get the current stage name.
	 *
	 * @return the current stage name
	 */
	public String getStageName() {
		return stageName;
	}

	/**
	 * Get the current stage status.
	 *
	 * @return the current stage status
	 */
	public int getStageStatus() {
		return stageStatus;
	}

	/**
	 * Process the input, and dispatch output to the next stage if needed.
	 * User need to implement this method in subclass.
	 *
	 * @param in the input message
	 * @param dispatcher the dispatcher used to dispatch outputs
	 */
	protected abstract void process(Input in, Dispatcher dispatcher);

}
