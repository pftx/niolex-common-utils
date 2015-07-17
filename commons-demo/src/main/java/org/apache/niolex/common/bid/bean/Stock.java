/**
 * Stock.java
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
 * The stock bean represents a stock.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2015-7-16
 */
public class Stock {

    private final String companyName;
    private final String stockAbbr;
    private final int stockCode;
    private int openPrice;
    private int closePrice;
    private int currentPrice;
    private int tradedAmount;

    /**
     * The only Constructor.
     *
     * @param companyName the stock company name
     * @param stockAbbr the stock ABBR
     * @param stockCode the stock code
     */
    public Stock(String companyName, String stockAbbr, int stockCode) {
        super();
        this.companyName = companyName;
        this.stockAbbr = stockAbbr;
        this.stockCode = stockCode;
        this.tradedAmount = 0;
        this.openPrice = closePrice = currentPrice = 0;
    }

    /**
     * @return the openPrice
     */
    public int getOpenPrice() {
        return openPrice;
    }

    /**
     * @param openPrice the openPrice to set
     */
    public void setOpenPrice(int openPrice) {
        this.openPrice = openPrice;
    }

    /**
     * @return the closePrice
     */
    public int getClosePrice() {
        return closePrice;
    }

    /**
     * @param closePrice the closePrice to set
     */
    public void setClosePrice(int closePrice) {
        this.closePrice = closePrice;
    }

    /**
     * @return the currentPrice
     */
    public int getCurrentPrice() {
        return currentPrice;
    }

    /**
     * @param currentPrice the currentPrice to set
     */
    public void setCurrentPrice(int currentPrice) {
        this.currentPrice = currentPrice;
    }

    /**
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @return the stockAbbr
     */
    public String getStockAbbr() {
        return stockAbbr;
    }

    /**
     * @return the stockCode
     */
    public int getStockCode() {
        return stockCode;
    }

    /**
     * @return the tradedAmount
     */
    public int getTradedAmount() {
        return tradedAmount;
    }

    /**
     * Add traded amount to the stock.
     *
     * @param amt the new traded amount
     */
    public void addTradeAmount(int amt) {
        this.tradedAmount += amt;
    }

}
