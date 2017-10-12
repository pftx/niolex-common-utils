/**
 * BidQueue.java
 *
 * Copyright 2015 the original author or authors.
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
package org.apache.niolex.common.bid.bean;

import java.util.LinkedList;
import java.util.Queue;

/**
 * The Bid queue, store all the bids at the same price.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2015-7-10
 */
public class BidQueue {

    private final Queue<Bid> list = new LinkedList<Bid>();

    /**
     * Offer a new bid to the queue.
     *
     * @param e the new bid
     * @return true if the element was added to this queue, else false
     * @see java.util.Queue#offer(java.lang.Object)
     */
    public boolean offer(Bid e) {
        return list.offer(e);
    }

    /**
     * Take the eldest bid from the queue.
     *
     * @return the eldest bid, or null if the queue is empty
     * @see java.util.Queue#poll()
     */
    public Bid poll() {
        return list.poll();
    }

    /**
     * Retrieves, but does not remove, the head of this queue, or returns null if this queue is empty.
     *
     * @return the eldest bid, or null if the queue is empty
     * @see java.util.Queue#peek()
     */
    public Bid peek() {
        return list.peek();
    }

    /**
     * Check whether this queue is empty.
     *
     * @return true if this queue contains no elements
     * @see java.util.Collection#isEmpty()
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

}
