/**
 * TimeZoneTest.java
 *
 * Copyright 2013 the original author or authors.
 *
 * We licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.apache.niolex.commons.util;

import static org.apache.niolex.commons.util.DateTimeUtil.*;
import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-6-25
 */
public class TimeZoneTest {

    @Test
    public void testDefaultTimezone() {
        System.out.println(CN_TZ);
        System.out.println(GM_TZ);
        String s;
        setTimeZone(CN_TZ);
        s = formatDate2LongStr(123456);
        assertEquals("1970-01-01 08:02:03.456", s);
        setTimeZone(GM_TZ);
        assertEquals(GM_TZ, getTimeZone());
        s = formatDate2LongStr(123456);
        assertEquals("1970-01-01 00:02:03.456", s);
    }

    @Test
    public void testParseTimezone() throws ParseException {
        Date d;
        setTimeZone(CN_TZ);
        d = parseDateFromDateTimeStr("2011-07-11 15:46:03.255");
        long t = d.getTime();
        assertEquals(1310370363000l, t);
        setTimeZone(GM_TZ);
        d = parseDateFromDateTimeStr("2011-07-11 15:46:03.255");
        assertEquals(1310399163000l, d.getTime());
        assertEquals(d.getTime() - t, HOUR * 8);
    }

    @Test
    public void testDatePartGMT() throws ParseException {
        Date d;
        setTimeZone(GM_TZ);
        d = parseDateFromDateTimeStr("2013-07-11 21:12:53.255");
        int year = getYear(d);
        assertEquals(2013, year);
        int hour = getHour(d);
        assertEquals(21, hour);
        int sec = getSecond(d);
        assertEquals(53, sec);
        setTimeZone(CN_TZ);
        hour = getHour(d);
        assertEquals(5, hour);
    }

    @Test
    public void testDatePart() throws ParseException {
        Date d;
        setTimeZone(CN_TZ);
        d = parseDateFromDateTimeStr("2013-07-11 21:12:53.255");
        int year = getYear(d);
        assertEquals(2013, year);
        int hour = getHour(d);
        assertEquals(21, hour);
        int sec = getSecond(d);
        assertEquals(53, sec);
    }

    @Test
    public void testDatePartDefault() throws ParseException {
        Date d;
        setTimeZone(null);
        d = parseDateFromDateTimeStr("2013-07-11 21:12:53.255");
        int year = getYear(d);
        assertEquals(2013, year);
        int hour = getHour(d);
        assertEquals(21, hour);
        int sec = getSecond(d);
        assertEquals(53, sec);
    }
}
