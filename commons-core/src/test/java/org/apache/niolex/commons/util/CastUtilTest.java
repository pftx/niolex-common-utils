package org.apache.niolex.commons.util;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CastUtilTest extends CastUtil {

    @Test
    public void testToIntDouble() throws Exception {
        assertEquals(1, toInt(1.1));
        assertEquals(1, toInt(1.9));
        assertEquals(1, toInt(1.0));
        assertEquals(1, toInt(1.99999));
        assertEquals(-1, toInt(-1.1));
        assertEquals(-1, toInt(-1.99999));
        assertEquals(-2147483648, toInt(-2147483648.0));
        assertEquals(2147483647, toInt(2147483647.000));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testToIntEx_1() throws Exception {
        assertEquals(-1, toInt(-1.99999e40));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testToIntEx_2() throws Exception {
        assertEquals(-1, toInt(-2147483648.3));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testToIntEx_3() throws Exception {
        assertEquals(1, toInt(2147483647.3));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testToIntEx_4() throws Exception {
        assertEquals(1, toInt(2.3e20));
    }

    @Test
    public void testToIntLong() throws Exception {
        assertEquals(1, toInt(1l));
        assertEquals(1234234, toInt(1234234l));
        assertEquals(-6536351, toInt(-6536351l));
        assertEquals(-2147483648, toInt(-2147483648l));
        assertEquals(2147483647, toInt(2147483647l));
        assertEquals(0, toInt(0l));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testToIntLongEx_1() throws Exception {
        assertEquals(0, toInt(-2147483649l));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testToIntLongEx_2() throws Exception {
        assertEquals(0, toInt(2147483648l));
    }

}
