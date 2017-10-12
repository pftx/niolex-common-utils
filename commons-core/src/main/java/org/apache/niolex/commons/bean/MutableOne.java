/**
 * MutableOne.java
 *
 * Copyright 2013 The original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.niolex.commons.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * A Mutable One is to store a mutable data, when data changed, Application can notify
 * users about it.
 * <br>
 * If user want to be notified when data changed, please add a listener to it.
 *
 * @param <T> the data type
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2013-1-6
 * @see DataChangeListener
 */
public class MutableOne<T> {

    /**
     * The listener interface for anyone he who want to be notified when data changed.
     *
     * @param <T> the data type
     * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
     * @version 1.0.5
     * @since 2013-1-6
     */
    public static interface DataChangeListener<T> {

        /**
         * This method is invoked when data changed.
         *
         * @param oldData the old data
         * @param newData the new data
         */
        public void onDataChange(T oldData, T newData);

    }

    /**
     * The listener list.
     */
    private final List<DataChangeListener<T>> list = new ArrayList<DataChangeListener<T>>();

    /**
     * The real data.
     */
    private T one;

    /**
     * The blank constructor.
     */
    public MutableOne() {
        super();
    }

    /**
     * The initialized constructor.
     *
     * @param one the data
     */
    public MutableOne(T one) {
        super();
        this.one = one;
    }

    /**
     * Add an event listener to listen to the event when data change.
     *
     * @param li the listener
     */
    public synchronized void addListener(DataChangeListener<T> li) {
        list.add(li);
    }

    /**
     * Remove this event listener from the listener list.
     *
     * @param li the listener
     * @return true if removed, false if not found
     */
    public synchronized boolean removeListener(DataChangeListener<T> li) {
        return list.remove(li);
    }

    /**
     * Update the data and notify all the listeners if there is any.
     *
     * @param one the new data
     */
    public synchronized void updateData(T one) {
        for (DataChangeListener<T> li : list) {
            li.onDataChange(this.one, one);
        }
        this.one = one;
    }

    /**
     * Get the current data.
     *
     * @return the current data
     */
    public T data() {
        return one;
    }

}
