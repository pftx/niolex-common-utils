package org.apache.niolex.commons.test;

import static org.junit.Assert.*;

import org.junit.Test;

public class TesterTest extends Tester {

    @Test
    public void testEqualIntInt() throws Exception {
        assertTrue(equal(3, 3));
        assertFalse(equal(3, 2));
    }

    @Test
    public void testEqualFloat() throws Exception {
        assertTrue(equal(3.0f, 3.0f));
        assertTrue(equal(3.0f,  3.0000001f));
        assertTrue(equal(3.0f,  3.0000002f));
        assertTrue(equal(3.0f,  3.0000003f));
        assertFalse(equal(3.0f, 3.000002f));
    }

    @Test
    public void testEqualDoubleDouble() throws Exception {
        assertTrue(equal(3.0, 3.0));
        assertTrue(equal(3.0,  3.000000000000001));
        assertTrue(equal(3.0,  3.0000000000000002));
        assertTrue(equal(3.0,  3.0000000000000003));
        assertFalse(equal(3.0, 3.000000000000002));
        assertTrue(equal(3 * 0.1, 0.3));
        assertFalse(3 * 0.1 == 0.3);
    }


    @Test
    public void testEqualDD() throws Exception {
        assertTrue(equal(1/3.0, 2/6.0));
        assertTrue(equal(1/10.0, 0.1));
    }

    @Test
    public void testBetweenIntIntInt() throws Exception {
        assertTrue(between(4, 4, 4));
        assertTrue(between(4, 4, 5));
        assertTrue(between(3, 4, 4));
        assertFalse(between(5, 4, 4));
        assertFalse(between(4, 4, 3));
    }

    @Test
    public void testBetweenDoubleDoubleDouble() throws Exception {
        assertTrue(between(4.0, 4.0, 4.0));
        assertTrue(between(4.0, 4.00000000000001, 4.000000000000011));
        assertFalse(between(4.0, 4.00000000000001d, 4.0));
        assertFalse(between(4.00000000000001, 4.00000000000001, 4.0));
        assertFalse(between(4.0, 4.00000000000002, 4.00000000000001));
    }

}
