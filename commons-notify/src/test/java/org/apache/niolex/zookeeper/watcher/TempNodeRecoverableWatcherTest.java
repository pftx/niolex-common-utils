package org.apache.niolex.zookeeper.watcher;

import static org.junit.Assert.assertNull;

import org.junit.BeforeClass;
import org.junit.Test;

public class TempNodeRecoverableWatcherTest {

    private static TempNodeRecoverableWatcher tnrw;
    
    @BeforeClass
    public static void createTempNodeRecoverableWatcher() throws Exception {
        tnrw = new TempNodeRecoverableWatcher(null, "/notify/test/tmp/TNRW-0001", null, false, null);
    }

    @Test
    public void testProcess() throws Exception {
        tnrw.process(null);
    }

    @Test
    public void testReconnected() throws Exception {
        tnrw.reconnected();
    }

    @Test
    public void testGetType() throws Exception {
        assertNull(tnrw.getType());
    }

}
