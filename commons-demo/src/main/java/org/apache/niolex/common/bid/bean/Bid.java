/**
 * Bid.java
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

import java.util.ArrayList;
import java.util.List;

/**
 * The Bid bean, record all the information about a bid.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2015-7-9
 */
public class Bid {

    public static enum Type {
        BUY, SELL, CANCEL;

        public static Type fromChar(char c) {
            switch (c) {
                case 'b':
                case 'B':
                    return BUY;
                case 's':
                case 'S':
                    return SELL;
                default:
                    return CANCEL;
            }
        }
    }

    private final int stockCode;

    // The user who submitted this bid.
    private final long accountId;

    // The bid serial id.
    private final long bidId;

    private final Type type;

    // The bid price counted as cent.
    private final int price;

    // The stock amount.
    private final int amount;

    // One bid maybe traded multiple times.
    private final List<Trade> tradeList = new ArrayList<Trade>();

    private int tradedAmount;
    private int canceledAmount;

    /**
     * The only Constructor.
     *
     * @param stockCode
     * @param accountId
     * @param bidId
     * @param type
     * @param price
     * @param amount
     */
    public Bid(int stockCode, long accountId, long bidId, char type, int price, int amount) {
        super();
        this.stockCode = stockCode;
        this.accountId = accountId;
        this.bidId = bidId;
        this.type = Type.fromChar(type);
        this.price = price;
        this.amount = amount;

        tradedAmount = 0;
        canceledAmount = 0;
    }

    /**
     * @return the tradedAmount
     */
    public int getTradedAmount() {
        return tradedAmount;
    }

    /**
     * @return the canceledAmount
     */
    public int getCanceledAmount() {
        return canceledAmount;
    }

    /**
     * @return the stockCode
     */
    public int getStockCode() {
        return stockCode;
    }

    /**
     * @return the accountId
     */
    public long getAccountId() {
        return accountId;
    }

    /**
     * @return the bidId
     */
    public long getBidId() {
        return bidId;
    }

    /**
     * @return the type
     */
    public Type getType() {
        return type;
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

    /**
     * @return the amount
     */
    public int getRemainAmount() {
        return amount - tradedAmount - canceledAmount;
    }

    /**
     * @return the tradeList
     */
    public List<Trade> getTradeList() {
        return tradeList;
    }

    /**
     * Add a new trade to the trade list.
     *
     * @param t the new trade
     */
    public void addTrade(Trade t) {
        tradeList.add(t);
        tradedAmount += t.getAmount();
    }

    /**
     * @param canceledAmount the cancel amount to be added
     */
    public void addCanceledAmount(int canceledAmount) {
        this.canceledAmount += canceledAmount;
    }

    /**
     * The bid is done, notify bid owner.
     */
    public void done() {
        ;
    }

}
