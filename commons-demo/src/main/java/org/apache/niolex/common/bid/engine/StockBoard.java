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

    private Map<Integer, Stock> stockMap = new HashMap<Integer, Stock>();
    private Map<Integer, BidEngine> engineMap = new HashMap<Integer, BidEngine>();

    public void addStock(int stockCode, String stockAbbr, String companyName) {
        Stock st = new Stock(companyName, stockAbbr, stockCode);
        BidEngine be = new BidEngine(st);

        stockMap.put(stockCode, st);
        engineMap.put(stockCode, be);
        be.startEngine();
    }

    public long submitBid(int stockCode, long accountId, int price, int amount, char type) {
        BidEngine be = engineMap.get(stockCode);
        long bidId = IdGenerator.nextBidId();
        Bid bid = new Bid(stockCode, accountId, bidId, type, price, amount);
        be.putBid(bid);

        return bidId;
    }


}
