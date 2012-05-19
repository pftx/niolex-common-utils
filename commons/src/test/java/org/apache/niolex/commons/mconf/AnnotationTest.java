package org.apache.niolex.commons.mconf;

import junit.framework.Assert;

import org.apache.niolex.commons.rpc.RpcConfig;
import org.apache.niolex.commons.rpc.RpcServiceFactory;
import org.junit.Test;


public class AnnotationTest {
    private static final RpcServiceFactory util = RpcServiceFactory.getInstance("/com/baidu/api/core/mconf/new-dr-api.properties");
    
    @Test
    public void toTest() {
        Campaign c = util.getService(Campaign.class);
        System.out.println(c.getClass().getCanonicalName());
        String simpleName = c.getClass().getInterfaces()[0].getSimpleName();
        System.out.println("simpleName => " + simpleName);
        Assert.assertEquals("Campaign", simpleName);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void toTestBad() {
        Fake c = util.getService(Fake.class);
        System.out.println(c.getClass().getCanonicalName());
    }
    
    @Test()
    public void toTestDefault() {
        try {
            Default c = util.getService(Default.class);
            System.out.println(c.getClass().getCanonicalName());
            Assert.assertTrue(false);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Rpc server config not found for your interface!", e.getMessage());
        }
    }
}

@RpcConfig("fc-adgp")
interface Campaign {
}

@RpcConfig("fc-fake")
interface Fake {
}

interface Default {
}