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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Tanslate bewteen string and Date.
 * 
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * 
 * @version 1.0.0, $Date: 2010-11-18$
 * 
 */
public abstract class DateTimeUtil {
    
    // One second specified in milliseconds.
    public static final long SECOND = 1000;
    
    // One minute specified in milliseconds.
    public static final long MINUTE = 60 * SECOND;
    
    // /////////////////////////////////////////////////////////////////////////////////////
    // FORMART DATE TO STRING
    // /////////////////////////////////////////////////////////////////////////////////////

    /**
     * Format the current date time to "yyyy-MM-dd HH:mm:ss.SSS" format string.
     */
    public static final String formatDate2LongStr() {
        return formatDate2LongStr(new Date());
    }

    /**
     * Format the given date time to "yyyy-MM-dd HH:mm:ss.SSS" format string.
     * @param date
     * @return
     */
    public static final String formatDate2LongStr(Date date) {
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return s.format(date);
    }
    
    /**
     * Format the given date time to "yyyy-MM-dd HH:mm:ss.SSS" format string.
     * @param date
     * @return
     */
    public static final String formatDate2LongStr(long date) {
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return s.format(new Date(date));
    }
    
    /**
     * Format the current date time to "yyyy-MM-dd HH:mm:ss" format string.
     */
    public static final String formatDate2DateTimeStr() {
        return formatDate2DateTimeStr(new Date());
    }
    
    /**
     * Format the given date time to "yyyy-MM-dd HH:mm:ss" format string.
     * @param date
     * @return
     */
    public static final String formatDate2DateTimeStr(Date date) {
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return s.format(date);
    }
    
    /**
     * Format the given date time to "yyyy-MM-dd HH:mm:ss" format string.
     * @param date
     * @return
     */
    public static final String formatDate2DateTimeStr(long date) {
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return s.format(new Date(date));
    }
    
    /**
     * Format the current date time to "yyyy-MM-dd" format string.
     * @return
     */
    public static final String formatDate2DateStr() {
        return formatDate2DateStr(new Date());
    }
    
    /**
     * Format the given date time to "yyyy-MM-dd" format string.
     * @param date
     * @return
     */
    public static final String formatDate2DateStr(Date date) {
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        return s.format(date);
    }
    
    /**
     * Format the given date time to "yyyy-MM-dd" format string.
     * @param date
     * @return
     */
    public static final String formatDate2DateStr(long date) {
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        return s.format(new Date(date));
    }
    
    /**
     * Format the current date time to "yyyyMMdd" format string.
     * @return
     */
    public static final String formatDate2ShortStr() {
        return formatDate2ShortStr(new Date());
    }
    
    /**
     * Format the given date time to "yyyyMMdd" format string.
     * @param date
     * @return
     */
    public static final String formatDate2ShortStr(Date date) {
        SimpleDateFormat s = new SimpleDateFormat("yyyyMMdd");
        return s.format(date);
    }
    
    // /////////////////////////////////////////////////////////////////////////////////////
    // PARSE DATE FROM STRING
    // /////////////////////////////////////////////////////////////////////////////////////

    public static final Date parseDateFromLongStr(String date) throws ParseException {
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return s.parse(date);
    }

    public static final Date parseDateFromDateTimeStr(String date) throws ParseException {
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return s.parse(date);
    }
    
    public static final Date parseDateFromDateStr(String date) throws ParseException {
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        return s.parse(date);
    }
    
    public static final Date parseDateFromShortStr(String date) throws ParseException {
        SimpleDateFormat s = new SimpleDateFormat("yyyyMMdd");
        return s.parse(date);
    }
    
    // /////////////////////////////////////////////////////////////////////////////////////
    // GET SPECIAL DATE BY PARAM
    // /////////////////////////////////////////////////////////////////////////////////////
    
    public static final Date getLastWeekDay(int weekDay) {
        return getLastWeekDay(weekDay, new Date());
    }

    public static final Date getLastWeekDay(Date end) {
        if (end == null)
            throw new IllegalArgumentException("The parameter [end date] should not be null!");
        GregorianCalendar cal = getCalender(end);
        cal.add(Calendar.DAY_OF_MONTH, -7);
        
        return cal.getTime();
    }
    
    public static final Date getLastWeekDay(int weekDay, Date end) {
        if (end == null)
            throw new IllegalArgumentException("The parameter [end date] should not be null!");
        GregorianCalendar cal = getCalender(end);
        cal.set(Calendar.DAY_OF_WEEK, weekDay);
        
        if (!cal.getTime().before(end)) {
            cal.add(Calendar.DAY_OF_MONTH, -7);
        }
        return cal.getTime();
    }
    
    public static final Date getYesterday() {
        GregorianCalendar cal = getCalender(new Date());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }
    
    public static final Date getLastHour() {
        GregorianCalendar cal = new GregorianCalendar();
        cal.add(Calendar.HOUR_OF_DAY, -1);
        return cal.getTime();
    }
    
    // /////////////////////////////////////////////////////////////////////////////////////
    // GET DATE PART
    // /////////////////////////////////////////////////////////////////////////////////////

    public static final int getHour(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal.get(Calendar.HOUR_OF_DAY);
    }
    
    public static final int getMinute(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal.get(Calendar.MINUTE);
    }
    
    public static final int getWeekDay(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }
    
    public static final int getDayofMonth(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }
    
    public static final int getMonth(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    private static final GregorianCalendar getCalender(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);

        return cal;
    }

}
