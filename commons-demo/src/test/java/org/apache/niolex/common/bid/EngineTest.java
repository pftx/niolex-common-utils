/**
 * EngineTest.java
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
package org.apache.niolex.common.bid;

import java.io.FileWriter;

import org.apache.niolex.common.bid.bean.Stock;
import org.apache.niolex.common.bid.engine.StockBoard;
import org.apache.niolex.commons.test.MockUtil;
import org.apache.niolex.commons.util.SystemUtil;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2015-8-12
 */
public class EngineTest {

    private static final int STOCK_CODE = 100215;
    private static FileWriter PIC;

    public static void main(String[] args) throws Exception {
        PIC = new FileWriter("price.txt");

        StockBoard b = new StockBoard();
        b.addStock(STOCK_CODE, "BTDU", "Beijing Tech Duplicated Ltd.");
        Stock s = b.getStock(STOCK_CODE);
        int price = s.getCurrentPrice();

        int i = 0;
        while (true) {
            ++i;
            b.submitBid(STOCK_CODE, MockUtil.randInt(3002198, 9002198), MockUtil.randInt(900, 1100), MockUtil.randInt(100, 200000), i % 2 == 0 ? 'b' : 's');

            if (price != s.getCurrentPrice()) {
                price = s.getCurrentPrice();
                b.printStatus();
            }

            if (i % 1000 == 998) {
                b.submitBid(STOCK_CODE, 1001001, 900, 200000000, 's');
            }

            if (i % 1000 == 498) {
                b.submitBid(STOCK_CODE, 1001001, 1100, 200000000, 'b');
            }

            SystemUtil.sleep(50);
        }
    }

    public static void printDot(int sps) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sps; ++i) {
            sb.append(' ');
        }
        sb.append("|\n");

        PIC.append(sb.toString());
        PIC.flush();
    }
}
