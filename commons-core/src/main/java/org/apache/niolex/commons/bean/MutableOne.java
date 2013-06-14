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
import java.util.Collections;
import java.util.List;

import org.apache.niolex.commons.internal.Synchronized;

/**
 * A Mutable One is to store a mutable data, when data changed, Application can notify
 * users about it.
 *
 * If user want to be notified when data change, please add a listener to it.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2013-1-6
 */
public class MutableOne<T> {

    /**
     * The listener list.
     */
    private final List<DataChangeListener<T>> list =
            Collections.synchronizedList(new ArrayList<DataChangeListener<T>>());
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
    public void addListener(DataChangeListener<T> li) {
        list.add(li);
    }

    /**
     * Remove this event listener from the listener list.
     *
     * @param li the listener
     * @return true if removed, false if not found
     */
    public boolean removeListener(DataChangeListener<T> li) {
        return list.remove(li);
    }

    /**
     * Get the current data.
     *
     * @return  the current data
     */
    public T data() {
        return one;
    }

    /**
     * Update the data and notify all the listeners if there is any.
     *
     * @param one the new data
     */
    public void updateData(T one) {
        this.one = one;
        Synchronized.notifyListeners(list, one);
    }

    /**
     * The listener interface for anyone want to be notified when data changed.
     *
     * @param <T>
     * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
     * @version 1.0.5
     * @since 2013-1-6
     */
    public static interface DataChangeListener<T> {

        /**
         * This method is invoked when data changed.
         *
         * @param newData the new data
         */
        public void onDataChange(T newData);

    }

}
