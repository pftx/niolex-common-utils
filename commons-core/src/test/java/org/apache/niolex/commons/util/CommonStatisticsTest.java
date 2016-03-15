package org.apache.niolex.commons.util;


import static org.junit.Assert.*;

import org.apache.niolex.commons.test.ObjToStringUtil;
import org.apache.niolex.commons.test.Tester;
import org.apache.niolex.commons.util.CommonStatistics.Statistics;
import org.junit.Test;

public class CommonStatisticsTest {

    @Test
    public void testPow2() throws Exception {
        assertTrue(Tester.equal(4.0, CommonStatistics.pow2(2)));
        assertTrue(Tester.equal(4.0, CommonStatistics.pow2(-2)));
        assertTrue(Tester.equal(6.25, CommonStatistics.pow2(2.5)));
        assertTrue(Tester.equal(400.0, CommonStatistics.pow2(20)));
        assertTrue(Tester.equal(1.0, CommonStatistics.pow2(-1)));
    }

    @Test
    public void testCommonStatistics() throws Exception {
        CommonStatistics cs = new CommonStatistics();
        cs.addValue(5);
        cs.addValue(7);
        cs.addValue(6);
        cs.addValue(3);
        cs.addValue(4);
        Statistics s = cs.percentileStatistics(100);
        System.out.println(ObjToStringUtil.objToString(s));

        assertEquals(5.0, s.avg, Tester.DOUBLE_INACCURACY);
        assertEquals(2.0, s.variance, Tester.DOUBLE_INACCURACY);
        assertEquals(5.0, s.median, Tester.DOUBLE_INACCURACY);
        assertEquals(7.0, s.max, Tester.DOUBLE_INACCURACY);
        assertEquals(3.0, s.min, Tester.DOUBLE_INACCURACY);
        assertEquals(25.0, s.sum, Tester.DOUBLE_INACCURACY);
        assertEquals(5.0, s.size, Tester.DOUBLE_INACCURACY);

        assertTrue(Tester.equal(3.0, cs.percentileValue(0)));
        assertTrue(Tester.equal(5.0, cs.percentileValue(50)));
        assertTrue(Tester.equal(7.0, cs.percentileValue(100)));
    }

    @Test
    public void testCommonStatisticsDoubleArray() throws Exception {
        double[] d = new double[]{1.1,2.2,3.3,4.4,5.6,7.8,9.1,23.5,77.6,68.2};
        CommonStatistics cs = new CommonStatistics(d);

        Statistics s = cs.percentileStatistics(100);
        System.out.println(ObjToStringUtil.objToString(s));

        assertEquals(20.28, s.avg, Tester.DOUBLE_INACCURACY);
        assertEquals(732.3776, s.variance, Tester.DOUBLE_INACCURACY);
        assertEquals(6.7, s.median, Tester.DOUBLE_INACCURACY);
        assertEquals(77.6, s.max, Tester.DOUBLE_INACCURACY);
        assertEquals(1.1, s.min, Tester.DOUBLE_INACCURACY);
        assertEquals(202.8, s.sum, Tester.DOUBLE_INACCURACY);
        assertEquals(10.0, s.size, Tester.DOUBLE_INACCURACY);
        System.out.println(cs.percentileValue(10));

        assertTrue(Tester.equal(1.1, cs.percentileValue(0)));
        assertTrue(Tester.equal(77.6, cs.percentileValue(100)));
        assertTrue(Tester.equal(s.median, cs.percentileValue(50)));
    }

    @Test
    public void testAddValue() throws Exception {
        double[] d = new double[]{1.1};
        CommonStatistics cs = new CommonStatistics(d);
        cs.addValue(2.2);
        Statistics s = cs.percentileStatistics(0);
        System.out.println(ObjToStringUtil.objToString(s));
    }

    @Test
    public void testSortData() throws Exception {
        System.out.println("not yet implemented");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testPercentileStatisticsMin() throws Exception {
        CommonStatistics cs = new CommonStatistics();
        cs.percentileValue(-Tester.DOUBLE_INACCURACY);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testPercentileStatisticsMax() throws Exception {
        CommonStatistics cs = new CommonStatistics();
        cs.percentileValue(100 + Tester.DOUBLE_INACCURACY * 100);
    }

    @Test
    public void testPercentileValue() throws Exception {
        CommonStatistics cs = new CommonStatistics();
        for (int i = 1; i < 6; ++i)
            cs.addValue(i);

        for (int i = 0; i <= 100; ++i) {
            System.out.println(String.format("%2d%% - %.2f", i, cs.percentileValue(i)));
        }
    }

    @Test
    public void testPercentileValueSpecial() throws Exception {
        CommonStatistics cs = new CommonStatistics();
        cs.addValue(3);
        cs.addValue(5);
        cs.addValue(8);
        cs.addValue(100);
        cs.addValue(1000);
        cs.addValue(2);

        for (int i : new int[]{20, 50, 75, 80, 100}) {
            System.out.println(String.format("%2d%% - %.2f", i, cs.percentileValue(i)));
        }
        assertEquals(cs.percentileValue(20), 3, Tester.DOUBLE_INACCURACY);
        assertEquals(cs.percentileValue(0), 2, Tester.DOUBLE_INACCURACY);
        assertEquals(cs.percentileValue(50), 6.5, Tester.DOUBLE_INACCURACY);
    }

}
