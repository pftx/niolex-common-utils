/**
 * Monitor.java
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
package org.apache.niolex.commons.remote;

import static org.apache.niolex.commons.remote.ConnectionWorker.endl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.collection.CircularList;
import org.apache.niolex.commons.concurrent.ConcurrentUtil;
import org.apache.niolex.commons.util.DateTimeUtil;

/**
 * Monitor the system internal status.
 *
 * User application can add the internal status as KV pair into this system,
 * and we will attach a time stamp with every value and store the latest M
 * items in memory.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5, $Date: 2012-11-22$
 */
public class Monitor implements Runnable {

	/**
	 * Store all the monitor informations.
	 */
	private final ConcurrentHashMap<String, CircularList<MonItem>> dataMap =
			new ConcurrentHashMap<String, CircularList<MonItem>>();

	/**
	 * Store all the connections need real time update.
	 */
	private final ConcurrentHashMap<String, ConcurrentLinkedQueue<OutputStream>> realtimeMap =
			new ConcurrentHashMap<String, ConcurrentLinkedQueue<OutputStream>>();

	/**
	 * Store all the messages pending to send to real time client.
	 */
	private final LinkedBlockingQueue<QueItem> realtimeQueue = new LinkedBlockingQueue<QueItem>();

	/**
	 * The max number of old items need to be stored.
	 */
	private final int maxOldItems;

	/**
	 * The internal thread to send real time data.
	 */
	private final Thread thread;

	/**
	 * The internal working status.
	 */
	private volatile boolean isWorking;

	/**
	 * Constructs a new monitor with the specified max old items.
	 *
	 * @param maxOldItems the max number of old items need to be stored
	 */
	public Monitor(int maxOldItems) {
		super();
		this.maxOldItems = maxOldItems;
		this.isWorking = true;
		this.thread = new Thread(this);
		this.thread.setDaemon(true);
		this.thread.start();
	}

	/**
	 * Add a new monitor value into the internal map.
	 *
	 * @param key the monitor key
	 * @param value the current value
	 */
	public void addValue(String key, int value) {
		CircularList<MonItem> set = dataMap.get(key);
		if (set == null) {
		    set = ConcurrentUtil.initMap(dataMap, key, new CircularList<MonItem>(maxOldItems));
		}
		MonItem e = new MonItem(value);
		set.add(e);
		// Process all the real time connections.
		ConcurrentLinkedQueue<OutputStream> que = realtimeMap.get(key);
		if (que != null && !que.isEmpty()) {
		    realtimeQueue.add(new QueItem(key, e, que));
		}
	}

    /**
     * Process all the messages.
     *
     * Override super method
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        while (isWorking) {
            // Wait on the queue for an item.
            try {
                QueItem q = realtimeQueue.take();
                // Process all the real time connections.
                if (!q.que.isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(q.key).append('[').append(q.time).append("]=").append(q.value);
                    sb.append('\n');
                    byte[] arr = StringUtil.strToUtf8Byte(sb.toString());
                    Iterator<OutputStream> iter = q.que.iterator();
                    while (iter.hasNext()) {
                        try {
                            OutputStream o = iter.next();
                            o.write(arr);
                        } catch (Exception e) {
                            iter.remove();
                        }
                    }
                }
            } catch (Exception e) {}
        }

    }

	/**
	 * A connection use this method to monitor the status of the specified key.
	 *
	 * @param out the output stream to write results.
	 * @param key the key to monitor
	 * @param parameter the parameter, with the following options:<pre>
	 * Option	Meaning
	 * Watch	Watch the historical and real time statistics.
	 * Real		Only need the real time statistics.
	 * History	(Default)Only need the historical statistics.</pre>
	 */
	public void doMonitor(OutputStream out, String key, String parameter) throws IOException {
	    switch (parameter.charAt(0)) {
	        case 'w':
	        case 'W':
	            // Watch the historical [and] real time statistics.
	            printHistorical(out, key);
	        case 'r':
	        case 'R':
	            // Only need the real time statistics.
	            attachReadTime(out, key);
	            break;
            default:
                // (Default)Only need the historical statistics.
                printHistorical(out, key);
                break;
	    }
	}

	/**
	 * Print the historical information into this output stream.
	 *
	 * @param out the output stream to write results.
	 * @param key the key to monitor
	 */
	private void printHistorical(OutputStream out, String key) throws IOException {
		CircularList<MonItem> set = dataMap.get(key);
		if (set == null) {
			out.write(StringUtil.strToUtf8Byte("The Key not found in Monitor." + endl()));
		} else {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < set.size(); ++i) {
				MonItem item = set.get(i);
				sb.append(key).append('[').append(DateTimeUtil.formatDate2DateTimeStr(item.time));
				sb.append(']').append('=').append(item.value).append(endl());
			}
			out.write(StringUtil.strToUtf8Byte(sb.toString()));
		}
	}

	/**
	 * Attach the output stream for real time monitor status update.
	 *
	 * @param out the output stream to write results.
	 * @param key the key to monitor
	 */
	private void attachReadTime(OutputStream out, String key) {
		ConcurrentLinkedQueue<OutputStream> que = realtimeMap.get(key);
		if (que == null) {
			que = ConcurrentUtil.initMap(realtimeMap, key, new ConcurrentLinkedQueue<OutputStream>());
		}
		que.add(out);
	}

	/**
	 * Stop the internal thread.
	 */
	public void stop() {
	    this.isWorking = false;
	    try {
	        this.thread.interrupt();
            this.thread.join();
        } catch (InterruptedException e) { }
	}

	/**
	 * The internal monitor item structure.
	 *
	 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
	 * @version 1.0.5, $Date: 2012-11-23$
	 */
	public static class MonItem {
	    final long time;
	    final int value;

		public MonItem(int value) {
			super();
			this.time = System.currentTimeMillis();
			this.value = value;
		}
	}

	/**
	 * The internal queue item structure.
	 *
	 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
	 * @version 1.0.5, $Date: 2012-12-3$
	 */
	public static class QueItem {
	    final String key;
	    final long time;
	    final int value;
	    final ConcurrentLinkedQueue<OutputStream> que;

        public QueItem(String key, MonItem e, ConcurrentLinkedQueue<OutputStream> que) {
            super();
            this.key = key;
            this.time = e.time;
            this.value = e.value;
            this.que = que;
        }
	}

}
