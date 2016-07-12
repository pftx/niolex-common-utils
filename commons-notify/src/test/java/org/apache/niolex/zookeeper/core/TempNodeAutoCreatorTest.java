package org.apache.niolex.zookeeper.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.niolex.commons.bean.One;
import org.apache.niolex.commons.reflect.FieldUtil;
import org.apache.niolex.notify.AppTest;
import org.junit.Test;

public class TempNodeAutoCreatorTest {
    
    private static final String BS = "/notify/zkc/tmp-auto";
    
    static {
        AppTest.cleanZK(BS);
    }

    @Test
    public void testReconnectedEmpty() throws Exception {
        TempNodeAutoCreator auto = new TempNodeAutoCreator(AppTest.URL, 10000);
        auto.close();
        auto.reconnect();
        assertFalse(auto.releaseTempNode());
        assertNull(auto.getSelfPath());
        auto.close();
    }

    @Test
    public void testReconnected() throws Exception {
        TempNodeAutoCreator auto = new TempNodeAutoCreator(AppTest.URL, 10000);
        auto.autoCreateTempNode(BS + "/auto-", "Geat! World!".getBytes(), true);
        String s = auto.getSelfPath();
        auto.close();
        auto.reconnect();
        
        String n = auto.selfPath;
        assertNotEquals(s, n);
        assertEquals("Geat! World!", auto.getDataAsStr(n));
        assertFalse(auto.exists(s));
        
        assertFalse(auto.autoCreateTempNode(BS + "/auto-fake-", "Geat! World!".getBytes(), true));
        assertTrue(auto.releaseTempNode());
        assertNull(auto.getSelfPath());
        auto.close();
    }

    @Test
    public void testAutoCreateTempNode() throws Exception {
        TempNodeAutoCreator auto = new TempNodeAutoCreator(AppTest.URL, 10000);
        FieldUtil.setValue(auto, "selfPath", "/a/b/c");
        auto.reconnected();
    }
    
    @Test
    public void testAutoRecover() throws Exception {
        TempNodeAutoCreator auto = new TempNodeAutoCreator(AppTest.URL, 10000);
        
        One<String> pathHolder = new One<String>();
        String p = auto.createTempNodeAutoRecover(BS + "/auto-zkc-", "Hello".getBytes(), true, pathHolder);
        
        auto.close();
        auto.reconnect();
        
        String s = pathHolder.a;
        assertNotEquals(p, s);
        assertFalse(auto.exists(p));
        assertTrue(auto.exists(s));
        assertEquals("Hello", auto.getDataAsStr(s));
        
        auto.close();
    }

}
