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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.collection.CircularList;
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
public class Monitor {

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
	 * The max number of old items need to be stored.
	 */
	private final int maxOldItems;

	/**
	 * Constructs a new monitor with the specified max old items.
	 *
	 * @param maxOldItems the max number of old items need to be stored
	 */
	public Monitor(int maxOldItems) {
		super();
		this.maxOldItems = maxOldItems;
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
			CircularList<MonItem> newset = new CircularList<MonItem>(maxOldItems);
			set = dataMap.putIfAbsent(key, newset);
			if (set == null) {
				set = newset;
			}
		}
		set.add(new MonItem(value));
		// Process all the real time connections.
		ConcurrentLinkedQueue<OutputStream> que = realtimeMap.get(key);
		if (que != null) {
			StringBuilder sb = new StringBuilder();
			sb.append(key).append(',').append(System.currentTimeMillis()).append(',').append(value);
			sb.append(Executer.END_LINE);
			byte[] arr = StringUtil.strToUtf8Byte(sb.toString());
			Iterator<OutputStream> iter = que.iterator();
			while (iter.hasNext()) {
				try {
					OutputStream o = iter.next();
					o.write(arr);
				} catch (IOException e) {
					iter.remove();
				}
			}
		}
	}

	/**
	 * A connection wait to monitor the status of the specified key.
	 *
	 * @param out the output stream to write results.
	 * @param key the key to monitor
	 * @param parameter the parameter, with the following options:
	 * Option	Meaning
	 * Watch	Watch the historical and real time statistics.
	 * Real		Only need the real time statistics.
	 * History	(Default)Only need the history statistics.
	 */
	public void doMonitor(OutputStream out, String key, String parameter) throws IOException {
		if (parameter.charAt(0) == 'w' || parameter.charAt(0) == 'W') {
			printHistorical(out, key);
			attachReadTime(out, key);
			return;
		} else if (parameter.charAt(0) == 'r' || parameter.charAt(0) == 'R') {
			attachReadTime(out, key);
			return;
		}
		printHistorical(out, key);
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
			out.write(StringUtil.strToUtf8Byte("The Key not found in Monitor." + Executer.END_LINE));
		} else {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < set.size(); ++i) {
				MonItem item = set.get(i);
				sb.append(key).append('[').append(DateTimeUtil.formatDate2DateTimeStr(item.time));
				sb.append(']').append('=').append(item.value).append(Executer.END_LINE);
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
			ConcurrentLinkedQueue<OutputStream> newque = new ConcurrentLinkedQueue<OutputStream>();
			que = realtimeMap.putIfAbsent(key, newque);
			if (que == null) {
				que = newque;
			}
		}
		que.add(out);
	}

	/**
	 * The internal monitor item structure.
	 *
	 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
	 * @version 1.0.5, $Date: 2012-11-23$
	 */
	public static class MonItem {
		long time;
		int value;

		public MonItem(int value) {
			super();
			this.time = System.currentTimeMillis();
			this.value = value;
		}
	}
}
