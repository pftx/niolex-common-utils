/**
 * BidEngine.java
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
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.niolex.common.bid.bean.Bid;
import org.apache.niolex.common.bid.bean.BidQueue;
import org.apache.niolex.common.bid.bean.Stock;
import org.apache.niolex.common.bid.bean.Trade;
import org.apache.niolex.commons.collection.CollectionUtil;

/**
 * The bid engine class process all the bids for one stock.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2015-7-9
 */
public class BidEngine implements Runnable {

    private static final int BUY = 0;
    private static final int SELL = 1;

    // Map BidPrice => BidQueue
    @SuppressWarnings("unchecked")
    private final TreeMap<Integer, BidQueue>[] queueMaps = new TreeMap[] { new TreeMap<Integer, BidQueue>(),
            new TreeMap<Integer, BidQueue>() };

    private final BlockingQueue<Bid> inputQueue = new LinkedBlockingQueue<Bid>();

    // Map BidID => Bid
    private final Map<Long, Bid> tradeMap = new HashMap<Long, Bid>();
    private final Stock stock;
    private final StockBoard board;

    private volatile boolean isWorking = true;
    private Thread runner = null;

    /**
     * Construct a new bid engine.
     *
     * @param stock the stock this bid engine is processing
     * @param board the stock board
     */
    public BidEngine(Stock stock, StockBoard board) {
        super();
        this.stock = stock;
        this.board = board;
    }

    /**
     * Put a new Bid into this Bid engine.
     *
     * @param b
     *            the new Bid
     */
    public void putBid(Bid b) {
        inputQueue.add(b);
    }

    /**
     * Query the Bid Id from this Bid engine.
     *
     * @param bidId the bid Id
     * @return the Bid if it's being processing, null if it's done or not exist or in the input queue.
     */
    public Bid queryBid(long bidId) {
        return tradeMap.get(bidId);
    }

    /**
     * Start this bid engine.
     */
    public synchronized void startEngine() {
        if (runner != null)
            return;

        isWorking = true;
        runner = new Thread(this);
        runner.start();
    }

    /**
     * Stop this bid engine.
     */
    public synchronized void stopEngine() {
        if (runner == null)
            return;

        isWorking = false;
        runner.interrupt();
        runner = null;
    }

    /**
     * The working method, processing the input queue, and generate trade.
     * This is the override of super method.
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        Bid b = null;

        while (isWorking) {
            // Take out a new bid.
            try {
                b = inputQueue.take();
            } catch (InterruptedException e) {
                continue;
            }

            // Process the new bid.
            processBid(b);

            while (processTrade()) {
                ;
            }
        }

    }

    /**
     * Process this Bid.
     *
     * @param b
     *            the bid
     */
    private void processBid(Bid b) {
        Long bidId = b.getBidId();
        Integer price = b.getPrice();
        TreeMap<Integer, BidQueue> queueMap = null;

        switch (b.getType()) {
            case BUY:
                queueMap = queueMaps[BUY];
                break;
            case SELL:
                queueMap = queueMaps[SELL];
                break;
            case CANCEL:
                Bid c = tradeMap.get(bidId);
                cancelBid(c, b.getAmount());
                break;
        }

        if (queueMap != null) {
            BidQueue queue = CollectionUtil.getOrInit(queueMap, price, BidQueue.class);
            queue.offer(b);
            tradeMap.put(bidId, b);
        }
    }

    /**
     * Cancel some amount from the target bid.
     *
     * @param target
     *            the bid to be canceled
     * @param cancelAmount
     *            the amount to be canceled from the target
     */
    private void cancelBid(Bid target, int cancelAmount) {
        int remain = target.getRemainAmount();

        target.addCanceledAmount(remain > cancelAmount ? cancelAmount : remain);
    }

    /**
     * Process trade across buyers and sellers.
     *
     * @return true if maybe there is some more trade
     */
    private boolean processTrade() {
        // Take the highest buyer.
        Entry<Integer, BidQueue> buyEntry = queueMaps[BUY].lastEntry();
        // Take the lowest seller.
        Entry<Integer, BidQueue> sellEntry = queueMaps[SELL].firstEntry();

        if (buyEntry == null || sellEntry == null) {
            return false;
        }

        if (buyEntry.getKey().intValue() < sellEntry.getKey().intValue()) {
            return false;
        }

        BidQueue buyQueue = buyEntry.getValue();
        BidQueue sellQueue = sellEntry.getValue();

        while (!buyQueue.isEmpty()) {
            Bid buy = buyQueue.peek();

            if (buy.getRemainAmount() <= 0) {
                buyQueue.poll();
                bidDone(buy);
                continue;
            }

            if (sellQueue.isEmpty()) {
                break;
            }

            while (!sellQueue.isEmpty()) {
                Bid sell = sellQueue.peek();

                if (sell.getRemainAmount() <= 0) {
                    sellQueue.poll();
                    bidDone(sell);
                    continue;
                }

                // Start to generate trade.
                long buyBidId = buy.getBidId();
                long sellBidId = sell.getBidId();
                int buyAmount = buy.getRemainAmount();
                int sellAmount = sell.getRemainAmount();
                Trade.Type tradeType;
                int tradePrice;
                int tradeAmount;

                if (buyBidId < sellBidId) {
                    tradeType = Trade.Type.IN;
                    tradePrice = buy.getPrice();
                } else {
                    tradeType = Trade.Type.OUT;
                    tradePrice = sell.getPrice();
                }

                boolean buyDone = false;
                boolean sellDone = false;

                if (buyAmount > sellAmount) {
                    tradeAmount = sellAmount;
                    // Sell bid will be done, remove it.
                    sellDone = true;
                } else if (sellAmount > buyAmount) {
                    tradeAmount = buyAmount;
                    // Buy bid will be done, remove it.
                    buyDone = true;
                } else {
                    tradeAmount = sellAmount;
                    // Both the buy bid and sell bid will be done.
                    buyDone = sellDone = true;
                }

                Trade t = new Trade(IdGenerator.nextTradeId(), tradeType, sellBidId, buyBidId, tradePrice, tradeAmount);
                buy.addTrade(t);
                sell.addTrade(t);
                stock.addTradeAmount(tradeAmount);
                stock.setCurrentPrice(tradePrice);

                if (sellDone) {
                    sellQueue.poll();
                    bidDone(sell);
                }

                if (buyDone) {
                    buyQueue.poll();
                    bidDone(buy);
                    break;
                }
            }
        }

        return true;
    }

    /**
     * The target bid is done, doing bookkeeping and notify the bid owner.
     *
     * @param target
     *            the target bid
     */
    private void bidDone(Bid target) {
        TreeMap<Integer, BidQueue> queueMap = queueMaps[target.getType() == Bid.Type.BUY ? BUY : SELL];

        Integer price = target.getPrice();
        BidQueue queue = queueMap.get(price);
        if (queue.isEmpty()) {
            // clean the queue map.
            queueMap.remove(price);
        }

        Long bidId = target.getBidId();
        // Clean the trade map.
        tradeMap.remove(bidId);

        // Notify the board.
        board.bidDone(target);
    }


    /**
     * Get the current Bid Engine internal status.
     *
     * @return the status as string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Is working? ").append(isWorking).append(" ");
        sb.append("B[").append(queueMaps[BUY].size()).append("] ");
        sb.append("S[").append(queueMaps[SELL].size()).append("] ");
        sb.append("I[").append(inputQueue.size()).append("] ");
        sb.append("P[").append(tradeMap.size()).append("]");

        return sb.toString();
    }
}
