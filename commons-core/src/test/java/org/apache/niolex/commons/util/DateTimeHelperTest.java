package org.apache.niolex.commons.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.util.Date;

import org.junit.Test;

public class DateTimeHelperTest {

    @Test
    public void testSetTimezoneContext() throws Exception {
        DateFormat format = DateTimeUtil.getDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        String s1 = format.format(new Date());
        System.out.println("s1: " + s1);
        assertTrue(DateTimeHelper.isValidDate(s1));

        format = DateTimeHelper.getContextFormatter();
        String s2 = format.format(new Date());
        System.out.println("s2: " + s2);
        assertTrue(DateTimeHelper.isValidDate(s2));

        format = DateTimeHelper.getFormatter(DateTimeHelper.ISO_8601_FORMAT, DateTimeHelper.DEFAULT_TIME_ZONE);
        String s3 = format.format(new Date());
        System.out.println("s3: " + s3);
        assertTrue(DateTimeHelper.isValidDate(s3));

        format = DateTimeHelper.getFormatter(DateTimeHelper.LONG_FORMAT_WITH_TZ, "America/Los_Angeles");
        String s4 = format.format(new Date());
        System.out.println("s4: " + s4);
        assertTrue(DateTimeHelper.isValidDate(s4));
    }

    @Test
    public void testGetContextFormatter() throws Exception {
        DateFormat df = DateTimeHelper.getContextFormatter();
        assertEquals(1510329600000l, df.parse("2017-11-11 00:00:00").getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetContextFormatterErr() throws Exception {
        DateTimeHelper.setTimezoneContext(null);
        DateFormat df = DateTimeHelper.getContextFormatter();
        assertEquals(1510329600000l, df.parse("2017-11-11 00:00:00").getTime());
    }

    @Test
    public void testGetFormatterString() throws Exception {
        DateFormat df = DateTimeHelper.getFormatter("+09");
        assertEquals(1510326000000l, df.parse("2017-11-11 00:00:00").getTime());
    }

    @Test
    public void testGetFormatterStringString() throws Exception {
        DateFormat df = DateTimeHelper.getFormatter("yyyy-MM-dd", "+09");
        assertEquals(1510326000000l, df.parse("2017-11-11").getTime());
    }

    @Test
    public void testGetTimeZone() throws Exception {
        assertEquals(DateTimeUtil.CN_TZ, DateTimeHelper.getTimeZone("+8"));
    }

    @Test
    public void testIsValidTimezone() throws Exception {
        assertTrue(DateTimeHelper.isValidTimezone("+8"));
        assertTrue(DateTimeHelper.isValidTimezone("+08"));
        assertTrue(DateTimeHelper.isValidTimezone("+08:00"));
        assertTrue(DateTimeHelper.isValidTimezone("GMT"));
        assertTrue(DateTimeHelper.isValidTimezone("GMT-3"));
        assertFalse(DateTimeHelper.isValidTimezone("GMT-33"));
    }

    @Test
    public void testParseDateString() throws Exception {
        assertNull(DateTimeHelper.parseDate(null));
        assertEquals(1510329600000l, DateTimeHelper.parseDate("2017-11-11").getTime());
        assertEquals(1510369933000l, DateTimeHelper.parseDate("2017-11-11T11:12:13").getTime());
        assertEquals(1510369933000l, DateTimeHelper.parseDate("2017-11-11 11:12:13").getTime());
        assertEquals(1510369933456l, DateTimeHelper.parseDate("2017-11-11T11:12:13.456").getTime());
        assertEquals(1510369933123l, DateTimeHelper.parseDate("2017-11-11 11:12:13.123").getTime());
        assertEquals(1510369933000l, DateTimeHelper.parseDate("2017-11-11T12:12:13+09").getTime());
        assertEquals(1510369933000l, DateTimeHelper.parseDate("2017-11-11 10:12:13+07:00").getTime());
        assertEquals(1510369933456l, DateTimeHelper.parseDate("2017-11-11T10:12:13.456GMT+07").getTime());
        assertEquals(1510369933123l, DateTimeHelper.parseDate("2017-11-10 17:12:13.123-10").getTime());
        assertEquals("2018-02-05 21:32:35",
                DateTimeUtil.formatDate2DateTimeStr(DateTimeHelper.parseDate("2018-02-05T20:32:35+07:00")));
        assertEquals("1997-07-17 02:20:30",
                DateTimeUtil.formatDate2DateTimeStr(DateTimeHelper.parseDate("1997-07-16T19:20:30+01:00")));
        assertEquals("2018-04-23 17:18:51",
                DateTimeUtil.formatDate2DateTimeStr(DateTimeHelper.parseDate("2018-04-23T17:18:51.852")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDateStringString() throws Exception {
        assertEquals(1510369933123l, DateTimeHelper.parseDate("1997-07-16T19:20+01:00").getTime());
    }

    @Test
    public void testIsValidDate() throws Exception {
        assertFalse(DateTimeHelper.isValidDate("1997-07-16T19:20+01:00"));
        assertFalse(DateTimeHelper.isValidDate("1997-07-161"));
        assertFalse(DateTimeHelper.isValidDate("1997-07-16T19:20"));
        assertFalse(DateTimeHelper.isValidDate("1997-07-16T19:20:00.123.08"));
        assertTrue(DateTimeHelper.isValidDate("1997-07-16T19:20:00+01:00"));
    }

}
