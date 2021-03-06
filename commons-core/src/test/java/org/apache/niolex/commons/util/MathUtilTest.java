package org.apache.niolex.commons.util;


import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.apache.niolex.commons.bean.Pair;
import org.junit.Test;

public class MathUtilTest extends MathUtil {

    @Test(expected=IllegalArgumentException.class)
    public void testCalcStandardDeviation() throws Exception {
        calcStandardDeviation();
    }

    @Test
    public void testCalcStandardDeviationIntArray() throws Exception {
        Pair<Integer,Double> pair = calcStandardDeviation(3, 4, 5);
        assertEquals(4, pair.a.intValue());
        assertEquals(0.816496580927726, pair.b.doubleValue(), 0.0001);
    }

    @Test
    public void testCalcStandardDeviationIntCollection() throws Exception {
        Pair<Integer,Double> pair = calcStandardDeviation(Arrays.asList(3, 4, 5, 6, 9, 1));
        assertEquals(4, pair.a.intValue());
        assertEquals(2.581988897471611, pair.b.doubleValue(), 0.0001);
    }

    @Test
    public void testMaxIntIntInt() throws Exception {
        assertEquals(6, max(6, 4, -3));
        assertEquals(-3, max(-6, -4, -3));
        assertEquals(4, max(2, 4, -3));
        assertEquals(6, max(6, 6, 6));
        assertEquals(Integer.MIN_VALUE, max(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE));
        assertEquals(0, max(0, 0, 0));
        assertEquals(6, max(2, 4, 6));
        assertEquals(6, max(5, 4, 6));
    }

    @Test
    public void testMaxIntArray() throws Exception {
        assertEquals(7, max(1,2,3,4,5,5,6,7));
        assertEquals(7, max(1,7,3,4,5,5,6,5));
        assertEquals(6, max(1,2,3,4,5,5,6,5));
    }

    @Test
    public void testMinIntIntInt() throws Exception {
        assertEquals(-3, min(6, 4, -3));
        assertEquals(-6, min(-6, -4, -3));
        assertEquals(1, min(2, 1, 3));
        assertEquals(6, min(6, 6, 6));
        assertEquals(Integer.MIN_VALUE, min(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE));
        assertEquals(Integer.MAX_VALUE, min(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE));
        assertEquals(0, min(0, 0, 0));
        assertEquals(2, min(2, 4, 6));
        assertEquals(2, min(4, 2, 6));
        assertEquals(2, min(4, 6, 2));
    }

    @Test
    public void testMinIntArray() throws Exception {
        assertEquals(1, min(1,2,3,4,5,5,6,7));
        assertEquals(3, min(8,7,3,4,5,5,6,5));
        assertEquals(5, min(5,5,6,5,6,7,8));
    }

    @Test
    public void testSum() throws Exception {
        assertEquals(66, sum(6,10,20,30));
        assertEquals(43, sum(8,7,3,4,5,5,6,5));
        assertEquals(21, sum(10,-10,Integer.MAX_VALUE, Integer.MIN_VALUE,22));
        assertEquals(6442450941l, sum(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE));
    }

    @Test
    public void testAvg() throws Exception {
        assertEquals(Integer.MAX_VALUE, CastUtil.toInt(avg(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE)));
        assertEquals(Integer.MIN_VALUE, CastUtil.toInt(avg(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE)));
        assertEquals((Integer.MAX_VALUE - 2) / 5, CastUtil.toInt(avg(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE)));
    }

    @Test
    public void testHalfUp1() throws Exception {
        assertEquals(0, halfUp(0, 7));
        assertEquals(0, halfUp(1, 7));
        assertEquals(0, halfUp(2, 7));
        assertEquals(0, halfUp(3, 7));
        assertEquals(7, halfUp(4, 7));
        assertEquals(7, halfUp(5, 7));
        assertEquals(7, halfUp(6, 7));
        assertEquals(7, halfUp(7, 7));
        assertEquals(7, halfUp(8, 7));
    }

    @Test
    public void testHalfUp2() throws Exception {
        for (int i = 0; i < 77; ++i)
            assertEquals(1001, halfUp(963 + i, 77));
    }

    @Test
    public void testHalfUp3() throws Exception {
        assertEquals(0, Math.round(-0.49D));
        assertEquals(0, Math.round(-0.50D));
        assertEquals(-1, Math.round(-0.51D));
        assertEquals(-21, halfUp(-18, 7));
        assertEquals(-14, halfUp(-17, 7));
        assertEquals(-14, halfUp(-16, 7));
        assertEquals(-14, halfUp(-15, 7));
        assertEquals(-14, halfUp(-14, 7));
        assertEquals(-14, halfUp(-13, 7));
        assertEquals(-14, halfUp(-12, 7));
        assertEquals(-14, halfUp(-11, 7));
        assertEquals(-7, halfUp(-10, 7));
        assertEquals(-7, halfUp(-9, 7));
        assertEquals(-7, halfUp(-8, 7));
        assertEquals(-7, halfUp(-7, 7));
        assertEquals(-7, halfUp(-6, 7));
        assertEquals(-7, halfUp(-5, 7));
        assertEquals(-7, halfUp(-4, 7));
        assertEquals(0, halfUp(-3, 8));
        assertEquals(0, halfUp(-4, 8));
        assertEquals(-8, halfUp(-5, 8));
        assertEquals(-8, halfUp(-9, 8));
    }

}
