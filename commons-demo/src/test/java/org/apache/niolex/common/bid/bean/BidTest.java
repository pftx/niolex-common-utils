package org.apache.niolex.common.bid.bean;


import org.apache.niolex.common.bid.engine.IdGenerator;
import org.junit.Test;

public class BidTest {

    @Test
    public void testToString() throws Exception {
        Bid b = new Bid(300157, 653002198, 56, 'b', 6589, 45200);
        Trade t = new Trade(IdGenerator.nextTradeId(), Trade.Type.IN, 66, 56, 6589, 3200);
        b.addTrade(t);
        t = new Trade(IdGenerator.nextTradeId(), Trade.Type.OUT, 74, 56, 6582, 47400);
        b.addTrade(t);
        System.out.println(b);
    }

}
