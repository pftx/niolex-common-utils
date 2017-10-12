/**
 * IdGenerator.java
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
package org.apache.niolex.common.bid.engine;

import java.util.concurrent.atomic.AtomicLong;

/**
 * The bid engine ID generator.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2015-7-15
 */
public class IdGenerator {

    private static final AtomicLong BID_ID = new AtomicLong();
    private static final AtomicLong TRADE_ID = new AtomicLong();

    /**
     * Generate the next bid id.
     *
     * @return the new bid id
     */
    public static final long nextBidId() {
        return BID_ID.incrementAndGet();
    }

    /**
     * Generate the next trade id.
     *
     * @return the new trade id
     */
    public static final long nextTradeId() {
        return TRADE_ID.incrementAndGet();
    }
}
