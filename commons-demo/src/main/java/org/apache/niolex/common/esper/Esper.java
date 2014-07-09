/**
 * Esper.java
 *
 * Copyright 2013 the original author or authors.
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
package org.apache.niolex.common.esper;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-6-5
 */

import java.util.Date;
import java.util.Random;

import org.apache.niolex.commons.concurrent.ThreadUtil;
import org.apache.niolex.commons.test.ObjToStringUtil;
import org.apache.niolex.commons.util.DateTimeUtil;
import org.apache.niolex.commons.util.SystemUtil;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class Esper {

    public static class Tick {
        String symbol;
        Double price;
        Date timeStamp;

        public Tick(String s, double p, long t) {
            symbol = s;
            price = p;
            timeStamp = new Date(t);
        }

        public double getPrice() {
            return price;
        }

        public String getSymbol() {
            return symbol;
        }

        public Date getTimeStamp() {
            return timeStamp;
        }

        @Override
        public String toString() {
            return "Price: " + price.toString().substring(0, 5) + " time: " + DateTimeUtil.formatDate2TimeStr(timeStamp);
        }
    }

    private static Random generator = new Random();

    public static void generateRandomTick(EPRuntime cepRT) {
        double price = generator.nextDouble() * 100;
        long timeStamp = System.currentTimeMillis();
        String symbol = "AAPL";
        Tick tick = new Tick(symbol, price, timeStamp);
        System.out.println("Sending tick:" + tick);
        cepRT.sendEvent(tick);
    }

    public static class CEPListener implements UpdateListener {

        public void update(EventBean[] newData, EventBean[] oldData) {
            SystemUtil.println("Event received, old: %s, new: %s.", ObjToStringUtil.objToString(oldData), ObjToStringUtil.objToString(newData));
        }
    }

    public static void main(String[] args) {
        // The Configuration is meant only as an initialization-time object.
        Configuration cepConfig = new Configuration();
        cepConfig.addEventType("StockTick", Tick.class.getName());
        EPServiceProvider cep = EPServiceProviderManager.getProvider("myCEPEngine", cepConfig);
        EPRuntime cepRT = cep.getEPRuntime();

        EPAdministrator cepAdm = cep.getEPAdministrator();
        EPStatement cepStatement = cepAdm.createEPL("select symbol,price,avg(price) from " + "StockTick(symbol='AAPL').win:length(10) "
                + "having avg(price) > 60.0");

        cepStatement.addListener(new CEPListener());

        // We generate a few ticks...
        for (int i = 0; i < 6000; i++) {
            generateRandomTick(cepRT);
            ThreadUtil.sleep(500);
        }
    }
}