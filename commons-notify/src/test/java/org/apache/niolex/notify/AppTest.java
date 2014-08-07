package org.apache.niolex.notify;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {

    public static final String URL = "localhost:9181";
    public static final App APP;

    static {
        setUp();
        APP = App.instance();
        APP.makeSurePathExists("/notify/test/tmp");
    }

    public static void setUp() {
        try {
            App.init(URL, 10000);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to connect to ZK.", e);
        }
    }

    /**
     * Rigorous Test :-)
     * @throws IOException
     */
    @Test
    public void testStrEQ() throws IOException {
        String a = "Hello World extern time!";
        String b = new String("Hello World extern time!");
        System.out.println("Hash " + a.hashCode());
        System.out.println("Hash " + b.hashCode());
        System.out.println("Eq " + b.equals(a));
        System.out.println("== " + (b == a));
        assertEquals(a, b);
        assertFalse(a == b);
        assertTrue(a == b.intern());
    }


    /**
     * Rigorous Test :-)
     * @throws IOException
     */
    @Test
    public void testInstanceProp() throws IOException {
        App app = App.instance();
        Notify notify = app.getNotify("/notify/test");
        notify.replaceProperty("a".getBytes(), "I Love it.".getBytes());
        byte[] v = notify.getProperty("a".getBytes());
        System.out.println(new String(v));
    }

    /**
     * Rigorous Test :-)
     * @throws IOException
     */
    @Test
    public void testAppNull() throws IOException {
        App app = App.instance();
        Notify notify = app.getNotify("/notify/test/tmplevt");
        assertNull(notify);
    }

    @Test
    public void testInit() throws Exception {
        App.init("not yet implemented", 10000);
        assertEquals(APP, App.instance());
    }

    @Test
    public void testGetNotify() throws Exception {
        Notify notify1 = APP.getNotify("/notify/test");
        Notify notify2 = APP.getNotify("/notify/test/tmp");
        Notify notify3 = APP.getNotify(new String("/notify/test/tmp"));
        assertNotEquals(notify1, notify2);
        assertEquals(notify3, notify2);
    }

    @Test(expected=NullPointerException.class)
    public void testClose() throws Exception {
        App app = new App(URL, 10000);
        app.notifyMap = null;
        try {
            app.getNotify("/notify/test");
        } finally {
            app.close();
        }
    }

}
