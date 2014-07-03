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
}
