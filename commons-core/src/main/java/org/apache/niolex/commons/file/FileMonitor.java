/**
 * FileMonitor.java
 *
 * Copyright 2013 the original author or authors.
 *
 * We licenses this file to you under the Apache License, version 2.0
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
package org.apache.niolex.commons.file;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.niolex.commons.concurrent.SimpleThreadFactory;

import com.google.common.collect.Lists;

/**
 * Monitoring the file changes from the file system and notify users.
 * <p>
 * Since JDK 1.7, there are official WatchService, so I will make this class simple and clean, user
 * can switch to JDK solution in higher JDK environment.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-7-22
 */
public class FileMonitor implements Runnable {

    /**
     * The internal thread pool.
     */
    protected static final ScheduledExecutorService EXECUTOR = Executors.newScheduledThreadPool(1, new SimpleThreadFactory("file-monitor"));

    /**
     * The event type to be used when notify.
     *
     * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
     * @version 1.0.0
     * @since 2013-7-23
     */
    public static enum EventType {
        CREATE, DELETE, UPDATE, ADD_CHILDREN, REMOVE_CHILDREN, NOT_DIR;
    }

    /**
     * The interface the EventListener need to implement for file change events.
     *
     * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
     * @version 1.0.0
     * @since 2013-7-23
     */
    public static interface EventListener {

        /**
         * Notify the happen of this event.
         *
         * @param type the event type
         * @param happenTime the event happen time. -1 for delete event, because we don't know the happen time.
         */
        public void notify(EventType type, long happenTime);

    }

    /**
     * The monitor interval in milliseconds.
     */
    protected final int monitorInterval;

    /**
     * The file path name.
     */
    protected final String filePathName;

    /**
     * The listener list.
     */
    protected final List<EventListener> list = Lists.newArrayList();

    /**
     * The holder of this scheduled task, used to cancel this task.
     */
    protected final ScheduledFuture<?> holder;

    /**
     * The file to be monitored.
     */
    protected File file;

    /**
     * The existence of this file.
     */
    protected boolean fileExists;

    /**
     * The last modified time of this file.
     */
    protected long lastModified;

    /**
     * The Constructor to create FileMonitor.
     *
     * @param monitorInterval The monitor interval in milliseconds.
     * @param filePathName The file path name.
     */
    public FileMonitor(int monitorInterval, String filePathName) {
        super();
        this.monitorInterval = monitorInterval;
        this.filePathName = filePathName;
        this.file = new File(filePathName);
        holder = EXECUTOR.scheduleWithFixedDelay(this, 1, monitorInterval, TimeUnit.MILLISECONDS);
    }

    /**
     * This is the override of super method.
     * @see java.lang.Runnable#run()
     */
    @Override
    public synchronized void run() {
        // Check file exists.
        if (!file.exists()) {
            if (fileExists) {
                fileExists = false;
                notifyListeners(EventType.DELETE, -1);
            }
            return;
        }
        // Check file creation.
        long time = file.lastModified();
        if (!fileExists) {
            fileExists = true;
            notifyListeners(EventType.CREATE, time);
            lastModified = time;
        }
        // Check update.
        if (lastModified < time) {
            notifyListeners(EventType.UPDATE, time);
            lastModified = time;
        }
    }

    /**
     * Notify the event listeners of the event.
     *
     * @param type the event type
     * @param happenTime the event happen time. -1 for delete event, because we don't know the happen time.
     */
    protected synchronized void notifyListeners(EventType type, long happenTime) {
        for (EventListener li : list) {
            li.notify(type, happenTime);
        }
    }

    /**
     * Appends the specified event listener to the end of the internal list.
     *
     * @param li the specified event listener to be added.
     */
    public synchronized void addListener(EventListener li) {
        list.add(li);
    }

    /**
     * Removes the first occurrence of the specified event listener from the internal list, if it is present.
     * If this list does not contain the element, it is unchanged.
     *
     * @param li the specified event listener to be removed.
     * @return returns true if this list contained the specified element (or equivalently, if this list changed as a result of the call).
     */
    public synchronized boolean removeListener(EventListener li) {
        return list.remove(li);
    }

    /**
     * Stop monitoring this file.
     */
    public void stop() {
        holder.cancel(false);
    }

}
