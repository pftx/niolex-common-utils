package org.apache.niolex.notify;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.niolex.notify.core.Notify;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {

    public static final String URL = "10.34.130.84:9181";

    @BeforeClass
    public static void setUp() throws IOException {
        App.init(URL, 10000);
        App.instance().makeSurePathExists("/notify/test");
    }

    /**
     * Rigorous Test :-)
     * @throws IOException
     */
    @Test
    public void testApp() throws IOException {
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
    public void testProp() throws IOException {
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
    public void testNull() throws IOException {
        App app = App.instance();
        Notify notify = app.getNotify("/notify/test/tmplevt");
        assertNull(notify);
    }

}
