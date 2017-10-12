package org.apache.niolex.commons.test;

import org.junit.Test;

public class AssertTest extends Assert {

    @Test(expected = AssertionError.class)
    public void testAssertIntEquals() throws Exception {
        Assert.assertIntEquals(3, "not yet implemented");
    }

    @Test(expected = AssertionError.class)
    public void testAssertLongEquals() throws Exception {
        Assert.assertLongEquals(3, "not yet implemented");
    }

    @Test(expected = AssertionError.class)
    public void testAssertIntEqualsE() throws Exception {
        Assert.assertIntEquals(3, new Integer(4));
    }

    @Test(expected = AssertionError.class)
    public void testAssertLongEqualsE() throws Exception {
        Assert.assertLongEquals(3, new Long(6));
    }

    @Test
    public void testAssertIntEqualsEq() throws Exception {
        Assert.assertIntEquals(4, new Integer(4));
    }

    @Test(expected = AssertionError.class)
    public void testAssertIntEqualsEqL() throws Exception {
        Assert.assertIntEquals(4, new Long(4));
    }

    @Test
    public void testAssertLongEqualsEq() throws Exception {
        Assert.assertLongEquals(6, new Long(6));
    }

}
