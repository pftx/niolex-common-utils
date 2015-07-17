/**
 * Trade.java
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

/**
 * The trade bean.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2015-7-9
 */
public class Trade {

    public static enum Type {
        IN, OUT;
    }

    // The serial trade id.
    private final long tradeId;
    private final Type tradeType;

    private final long sellBidId;
    private final long buyBidId;

    // The bid price counted as cent.
    private final int price;

    // The stock amount.
    private final int amount;

    /**
     * The only Constructor.
     *
     * @param tradeId
     * @param tradeType
     * @param sellBidId
     * @param buyBidId
     * @param price
     * @param amount
     */
    public Trade(long tradeId, Type tradeType, long sellBidId, long buyBidId, int price, int amount) {
        super();
        this.tradeId = tradeId;
        this.tradeType = tradeType;
        this.sellBidId = sellBidId;
        this.buyBidId = buyBidId;
        this.price = price;
        this.amount = amount;
    }

    /**
     * @return the tradeId
     */
    public long getTradeId() {
        return tradeId;
    }

    /**
     * @return the tradeType
     */
    public Type getTradeType() {
        return tradeType;
    }

    /**
     * @return the sellBidId
     */
    public long getSellBidId() {
        return sellBidId;
    }

    /**
     * @return the buyBidId
     */
    public long getBuyBidId() {
        return buyBidId;
    }

    /**
     * @return the price
     */
    public int getPrice() {
        return price;
    }

    /**
     * @return the amount
     */
    public int getAmount() {
        return amount;
    }

}
