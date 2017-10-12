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
    public void testEqualLongInt() throws Exception {
        assertTrue(equal(3l, 3l));
        assertFalse(equal(0x3fd3333333333334l, 0x3fd3333333333333l));
    }

    @Test
    public void testEqualFloat() throws Exception {
        assertTrue(equal(3.0f, 3.0f));
        assertTrue(equal(3.0f,  3.0000001f));
        assertTrue(equal(3.0f,  3.0000002f));
        assertTrue(equal(3.0f,  3.0000003f));
        assertFalse(equal(3.0f, 3.000002f));
        assertTrue(equal(3 * 0.1f, 0.3f));
        assertTrue(equal(2 * 0.1f, 0.2f));
        assertTrue(equal(7 * 0.1f, 0.7f));
    }

    @Test
    public void testNotEqualFloat() throws Exception {
        assertFalse(equal(3.0f, 3.00002f));
        assertFalse(equal(3.0f, 3.00001f));
        assertFalse(equal(1.0f, 1.0f - FLOAT_INACCURACY));
        assertFalse(equal(0.1f, 0.1f - FLOAT_INACCURACY));
        assertFalse(equal(0f / 0.0f, 0f / 0.0f));
        assertFalse(equal(0f, 0f / 0.0f));
        assertFalse(equal(0f / 0.0f, 0.0f));
    }

    @Test
    public void testEqualDoubleDouble() throws Exception {
        assertTrue(equal(3.0, 3.0));
        assertTrue(equal(3.0,  0.3 * 10));
        assertTrue(equal(3.0,  3.0000000000000002));
        assertTrue(equal(3.0,  3.0000000000000003));
        assertFalse(equal(3.0, 3.000000000000002));
        assertTrue(equal(3 * 0.1, 0.3));
        assertTrue(equal(3 * 0.1e-100, 0.3e-100));
        assertFalse(3 * 0.1 == 0.3);
    }

    @Test
    public void testNotEqualDoubleDouble() throws Exception {
        assertFalse(equal(3e-100, 4e-100));
        assertFalse(equal(1.0e-100, 1.1e-100));
        assertFalse(equal(0.3e-100,0.2999999999999e-100));
        assertFalse(equal(1d, 1d - DOUBLE_INACCURACY));
        assertFalse(equal(0.1d, 0.1d - DOUBLE_INACCURACY));
        assertFalse(equal(0f / 0.0d, 0f / 0.0d));
        assertFalse(equal(0.0d, 0f / 0.0d));
        assertFalse(equal(0f / 0.0d, 0.0d));
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
