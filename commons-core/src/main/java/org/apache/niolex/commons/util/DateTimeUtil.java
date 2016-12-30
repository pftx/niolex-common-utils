/**
 * DateTimeUtil.java
 *
 * Copyright 2010 Niolex, Inc.
 *
 * Niolex licenses this file to you under the Apache License, version 2.0
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Translate between string and Date Time.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2010-11-18$
 */
public abstract class DateTimeUtil {

    /**
     * The long data time format, including details to milliseconds.
     */
    public static final String LONG_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * The date time format, including details to seconds.
     */
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * The date only format.
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * The short date format.
     */
    public static final String SHORT_FORMAT = "yyyyMMdd";

    /**
     * The time format.
     */
    public static final String TIME_FORMAT = "HH:mm:ss";

    // One second specified in milliseconds.
    public static final int SECOND = 1000;

    // One minute specified in milliseconds.
    public static final int MINUTE = 60 * SECOND;

    // One hour specified in milliseconds.
    public static final int HOUR = 60 * MINUTE;

    // One day specified in milliseconds.
    public static final int DAY = 24 * HOUR;

    // The GMT time zone.
    public static final TimeZone GM_TZ = TimeZone.getTimeZone("GMT");

    // The China Standard time zone.
    public static final TimeZone CN_TZ = TimeZone.getTimeZone("GMT+08:00");

    // /////////////////////////////////////////////////////////////////////////////////////
    // FORMART DATE TO STRING
    // /////////////////////////////////////////////////////////////////////////////////////

    /**
     * Format the current date time to "yyyy-MM-dd HH:mm:ss.SSS" format string.
     *
     * @return the result
     */
    public static final String formatDate2LongStr() {
        return formatDate2LongStr(new Date());
    }

    /**
     * Format the given date time to "yyyy-MM-dd HH:mm:ss.SSS" format string.
     *
     * @param date the date you want to format
     * @return the result
     */
    public static final String formatDate2LongStr(Date date) {
        DateFormat s = getDateFormat(LONG_FORMAT);
        return s.format(date);
    }

    /**
     * Format the given date time to "yyyy-MM-dd HH:mm:ss.SSS" format string.
     *
     * @param date the date in milliseconds since January 1, 1970, 00:00:00 GMT.
     * @return the result
     */
    public static final String formatDate2LongStr(long date) {
        DateFormat s = getDateFormat(LONG_FORMAT);
        return s.format(new Date(date));
    }

    /**
     * Format the current date time to "yyyy-MM-dd HH:mm:ss" format string.
     *
     * @return the result
     */
    public static final String formatDate2DateTimeStr() {
        return formatDate2DateTimeStr(new Date());
    }

    /**
     * Format the given date time to "yyyy-MM-dd HH:mm:ss" format string.
     *
     * @param date the date you want to format
     * @return the result
     */
    public static final String formatDate2DateTimeStr(Date date) {
        DateFormat s = getDateFormat(DATE_TIME_FORMAT);
        return s.format(date);
    }

    /**
     * Format the given date time to "yyyy-MM-dd HH:mm:ss" format string.
     *
     * @param date the date in milliseconds since January 1, 1970, 00:00:00 GMT.
     * @return the result
     */
    public static final String formatDate2DateTimeStr(long date) {
        DateFormat s = getDateFormat(DATE_TIME_FORMAT);
        return s.format(new Date(date));
    }

    /**
     * Format the current date time to "yyyy-MM-dd" format string.
     *
     * @return the result
     */
    public static final String formatDate2DateStr() {
        return formatDate2DateStr(new Date());
    }

    /**
     * Format the given date time to "yyyy-MM-dd" format string.
     *
     * @param date the date you want to format
     * @return the result
     */
    public static final String formatDate2DateStr(Date date) {
        DateFormat s = getDateFormat(DATE_FORMAT);
        return s.format(date);
    }

    /**
     * Format the given date time to "yyyy-MM-dd" format string.
     *
     * @param date the date in milliseconds since January 1, 1970, 00:00:00 GMT.
     * @return the result
     */
    public static final String formatDate2DateStr(long date) {
        DateFormat s = getDateFormat(DATE_FORMAT);
        return s.format(new Date(date));
    }

    /**
     * Format the current date time to "HH:mm:ss" format string.
     *
     * @return the result
     */
    public static final String formatDate2TimeStr() {
        return formatDate2TimeStr(new Date());
    }

    /**
     * Format the given date time to "HH:mm:ss" format string.
     *
     * @param date the date you want to format
     * @return the result
     */
    public static final String formatDate2TimeStr(Date date) {
        DateFormat s = getDateFormat(TIME_FORMAT);
        return s.format(date);
    }

    /**
     * Format the given date time to "HH:mm:ss" format string.
     *
     * @param date the date in milliseconds since January 1, 1970, 00:00:00 GMT.
     * @return the result
     */
    public static final String formatDate2TimeStr(long date) {
        DateFormat s = getDateFormat(TIME_FORMAT);
        return s.format(new Date(date));
    }

    /**
     * Format the current date time to "yyyyMMdd" format string.
     *
     * @return the result
     */
    public static final String formatDate2ShortStr() {
        return formatDate2ShortStr(new Date());
    }

    /**
     * Format the given date time to "yyyyMMdd" format string.
     *
     * @param date the date you want to format
     * @return the result
     */
    public static final String formatDate2ShortStr(Date date) {
        DateFormat s = getDateFormat(SHORT_FORMAT);
        return s.format(date);
    }

    /**
     * Format the given date time to "yyyyMMdd" format string.
     *
     * @param date the date in milliseconds since January 1, 1970, 00:00:00 GMT.
     * @return the result
     */
    public static final String formatDate2ShortStr(long date) {
        DateFormat s = getDateFormat(SHORT_FORMAT);
    	return s.format(new Date(date));
    }

    // /////////////////////////////////////////////////////////////////////////////////////
    // PARSE DATE FROM STRING
    // /////////////////////////////////////////////////////////////////////////////////////

    /**
     * Parse date from the string in "yyyy-MM-dd HH:mm:ss.SSS" format.
     *
     * @param date the formatted date string
     * @return the parsed date
     * @throws ParseException if the specified string cannot be parsed
     */
    public static final Date parseDateFromLongStr(String date) throws ParseException {
        DateFormat s = getDateFormat(LONG_FORMAT);
        return s.parse(date);
    }

    /**
     * Parse date from the string in "yyyy-MM-dd HH:mm:ss" format.
     *
     * @param date the formatted date string
     * @return the parsed date
     * @throws ParseException if the specified string cannot be parsed
     */
    public static final Date parseDateFromDateTimeStr(String date) throws ParseException {
        DateFormat s = getDateFormat(DATE_TIME_FORMAT);
        return s.parse(date);
    }

    /**
     * Parse date from the string in "yyyy-MM-dd" format.
     *
     * @param date the formatted date string
     * @return the parsed date
     * @throws ParseException if the specified string cannot be parsed
     */
    public static final Date parseDateFromDateStr(String date) throws ParseException {
        DateFormat s = getDateFormat(DATE_FORMAT);
        return s.parse(date);
    }

    /**
     * Parse date from the string in "yyyyMMdd" format.
     *
     * @param date the formatted date string
     * @return the parsed date
     * @throws ParseException if the specified string cannot be parsed
     */
    public static final Date parseDateFromShortStr(String date) throws ParseException {
        DateFormat s = getDateFormat(SHORT_FORMAT);
        return s.parse(date);
    }

    // /////////////////////////////////////////////////////////////////////////////////////
    // GET SPECIAL DATE BY PARAM
    // /////////////////////////////////////////////////////////////////////////////////////

    /**
     * Get the last date in the specified week day counted from today.
     * You can use {@link Calendar#DAY_OF_WEEK} to specify the week day.
     *
     * @param weekDay the week day you want, from 1(SUNDAY) to 7(SATURDAY)
     * @return the date
     * @see Calendar#DAY_OF_WEEK
     */
    public static final Date getLastWeekDay(int weekDay) {
        return getLastWeekDay(weekDay, new Date());
    }

    /**
     * Get the last date has the same week day as the specified date.
     *
     * @param date the specified date
     * @return the date
     * @see Calendar#DAY_OF_WEEK
     */
    public static final Date getLastWeekDay(Date date) {
        if (date == null)
            throw new IllegalArgumentException("The parameter [date] should not be null!");
        GregorianCalendar cal = getCalender(date, true);
        cal.add(Calendar.DAY_OF_MONTH, -7);

        return cal.getTime();
    }

    /**
     * Get the last date in the specified week day counted from the specified end date.
     * You can use {@link Calendar#DAY_OF_WEEK} to specify the week day.
     *
     * @param weekDay the week day you want, from 1(SUNDAY) to 7(SATURDAY)
     * @param end the specified end date
     * @return the date
     * @see Calendar#DAY_OF_WEEK
     */
    public static final Date getLastWeekDay(int weekDay, Date end) {
        if (end == null)
            throw new IllegalArgumentException("The parameter [end] should not be null!");
        GregorianCalendar cal = getCalender(end, true);
        cal.set(Calendar.DAY_OF_WEEK, weekDay);

        if (!cal.getTime().before(end)) {
            cal.add(Calendar.DAY_OF_MONTH, -7);
        }
        return cal.getTime();
    }

    /**
     * Get yesterday.
     *
     * @return the date
     */
    public static final Date getYesterday() {
        GregorianCalendar cal = getCalender();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    /**
     * Get the date of today's mid night.
     *
     * @return the date
     */
    public static final Date getTodayMidnight() {
        return getCalender().getTime();
    }

    /**
     * Get the date representing last hour of now, with the same minutes and seconds.
     *
     * @return the date
     */
    public static final Date getLastHour() {
        GregorianCalendar cal = getCalender(new Date(), false);
        cal.add(Calendar.HOUR_OF_DAY, -1);
        return cal.getTime();
    }

    // /////////////////////////////////////////////////////////////////////////////////////
    // GET DATE PART
    // /////////////////////////////////////////////////////////////////////////////////////

    public static final int getHour(Date date) {
        GregorianCalendar cal = getCalender(date, false);
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    public static final int getMinute(Date date) {
        GregorianCalendar cal = getCalender(date, false);
        return cal.get(Calendar.MINUTE);
    }

    public static final int getSecond(Date date) {
        GregorianCalendar cal = getCalender(date, false);
        return cal.get(Calendar.SECOND);
    }

    /**
     * Get the week day of the specified date.
     * The week day is specified by {@link Calendar#DAY_OF_WEEK}, from 1(SUNDAY) to 7(SATURDAY)
     *
     * @param date the specified date
     * @return the week day
     * @see Calendar#DAY_OF_WEEK
     */
    public static final int getWeekDay(Date date) {
        GregorianCalendar cal = getCalender(date, false);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static final int getDayofMonth(Date date) {
        GregorianCalendar cal = getCalender(date, false);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Get the month of the specified date.
     * The month is specified by {@link Calendar#MONTH}, from 0(JANUARY) to 11(DECEMBER)
     *
     * @param date the specified date
     * @return the month
     * @see Calendar#MONTH
     */
    public static final int getMonth(Date date) {
        GregorianCalendar cal = getCalender(date, false);
        return cal.get(Calendar.MONTH);
    }

    public static final int getYear(Date date) {
        GregorianCalendar cal = getCalender(date, false);
        return cal.get(Calendar.YEAR);
    }

    // /////////////////////////////////////////////////////////////////////////////////////
    // BASIC DATE TIME OPERATION
    // /////////////////////////////////////////////////////////////////////////////////////

    /**
     * Get a newly created calendar stands for today with the time part cleaned.
     * 
     * @return the newly created calendar
     */
    public static final GregorianCalendar getCalender() {
        return getCalender(new Date(), true);
    }

    /**
     * Get a newly created calendar by the specified date instance. If the <code>cleanTime</code> is true, we will clean
     * the time part from the calendar to be returned.
     * 
     * @param date the date instance to be used to create calendar
     * @param cleanTime if true, we clean the time part, if false, we keep the specified date not changed
     * @return the newly created calendar
     */
    public static final GregorianCalendar getCalender(Date date, boolean cleanTime) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        if (TIME_ZONE != null) {
            cal.setTimeZone(TIME_ZONE);
        }
        if (cleanTime) {
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.HOUR_OF_DAY, 0);
        }
        return cal;
    }

    /**
     * The thread local cache to cache all the created SimpleDateFormat for re-use.
     */
    private static final ThreadLocal<SimpleDateFormat> dateFormatterCache = new ThreadLocal<SimpleDateFormat>() {

        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat();
        }

    };

    /**
     * Get an instance of DateFormat with the specified format pattern.
     * 
     * @param format the date format pattern
     * @return an instance of DateFormat
     */
    public static final DateFormat getDateFormat(String format) {
        SimpleDateFormat simpleF = dateFormatterCache.get();
        simpleF.applyPattern(format);
        if (TIME_ZONE != null) {
            simpleF.setTimeZone(TIME_ZONE);
        }
        return simpleF;
    }

    /**
     * The time zone to be used in this utility.
     */
    private static TimeZone TIME_ZONE;

    /**
     * Set the time zone to be used to format dates.
     * 
     * @param timeZone the new time zone
     */
    public static final void setTimeZone(TimeZone timeZone) {
        TIME_ZONE = timeZone;
    }

    /**
     * @return the current time zone used to format dates.
     */
    public static final TimeZone getTimeZone() {
        return TIME_ZONE;
    }

}
