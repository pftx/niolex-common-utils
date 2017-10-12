/**
 * StockBoard.java
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

import java.util.HashMap;
import java.util.Map;

import org.apache.niolex.common.bid.bean.Bid;
import org.apache.niolex.common.bid.bean.Stock;

/**
 * The stock board is the core class for the stock exchange market.
 * All the bids will be processed in this class.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2015-7-15
 */
public class StockBoard {

    // Map StockCode => Stock
    private Map<Integer, Stock> stockMap = new HashMap<Integer, Stock>();
    // Map StockCode => BidEngine
    private Map<Integer, BidEngine> engineMap = new HashMap<Integer, BidEngine>();

    /**
     * Add a new stock into this stock board.
     *
     * @param stockCode the stock code
     * @param stockAbbr the stock abbreviation
     * @param companyName the stock owner company name
     */
    public void addStock(int stockCode, String stockAbbr, String companyName) {
        Stock st = new Stock(companyName, stockAbbr, stockCode);
        BidEngine be = new BidEngine(st, this);

        stockMap.put(stockCode, st);
        engineMap.put(stockCode, be);
        be.startEngine();
    }

    /**
     * Get the stock by the given stock code.
     *
     * @param stockCode the stock code of the stock you want to get
     * @return the stock if exist
     */
    public Stock getStock(int stockCode) {
        return stockMap.get(stockCode);
    }

    /**
     * Submit a new bid into this stock board.
     *
     * @param stockCode the stock code
     * @param accountId the user account Id
     * @param price the bid price
     * @param amount the bid amount
     * @param type the bid type
     * @return the generated bid Id
     */
    public long submitBid(int stockCode, long accountId, int price, int amount, char type) {
        BidEngine be = engineMap.get(stockCode);
        long bidId = IdGenerator.nextBidId();
        Bid bid = new Bid(stockCode, accountId, bidId, type, price, amount);
        be.putBid(bid);

        return bidId;
    }

    /**
     * Query the bid by the specified bid Id.
     *
     * @param stockCode
     * @param accountId
     * @param bidId
     * @return the specified bid if exists
     */
    public Bid queryBid(int stockCode, long accountId, long bidId) {
        BidEngine be = engineMap.get(stockCode);
        Bid b = be.queryBid(bidId);

        if (b != null && b.getAccountId() == accountId) {
            return b;
        }
        return null;
    }


    /**
     * The bid is done, notify bid owner.
     */
    protected void bidDone(Bid bid) {
        System.out.println(bid);
    }

    /**
     * Print the internal status into the console.
     */
    public void printStatus() {
        for (Stock s : stockMap.values()) {
            System.out.print(s);
            BidEngine be = engineMap.get(s.getStockCode());
            System.out.println(be);
        }
    }
}
