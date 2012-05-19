/**
 * DateTimeUtilTest.java
 *
 * Copyright 2011 Baidu, Inc.
 *
 * Baidu licenses this file to you under the Apache License, version 2.0
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

import java.util.Date;

import junit.framework.Assert;

import org.apache.niolex.commons.util.DateTimeUtil;
import org.junit.Test;


/**
 * @author @author@ (@author-email@)
 * 
 * @version @version@, $Date: 2011-7-11$
 * 
 */
public class DateTimeUtilTest {
    
    @Test
    public void formatDate2LongStr() throws Exception {
        String s = DateTimeUtil.formatDate2LongStr();
        System.out.println("s => " + s);
        s = DateTimeUtil.formatDate2LongStr(new Date());
        System.out.println("s => " + s);
        s = DateTimeUtil.formatDate2LongStr(System.currentTimeMillis());
        System.out.println("s => " + s);
        s = DateTimeUtil.formatDate2LongStr(123456);
        System.out.println("s => " + s);
        Assert.assertEquals("1970-01-01 08:02:03.456", s);
    }
    
    @Test
    public void formatDate2DateStr() throws Exception {
        String s = DateTimeUtil.formatDate2DateStr();
        System.out.println("s => " + s);
        s = DateTimeUtil.formatDate2DateStr(new Date());
        System.out.println("s => " + s);
        s = DateTimeUtil.formatDate2DateStr(System.currentTimeMillis());
        System.out.println("s => " + s);
        s = DateTimeUtil.formatDate2DateStr(123456);
        System.out.println("s => " + s);
        Assert.assertEquals("1970-01-01", s);
    }
    
    @Test
    public void formatDate2DateTimeStr() throws Exception {
        String s = DateTimeUtil.formatDate2DateTimeStr();
        System.out.println("s => " + s);
        s = DateTimeUtil.formatDate2DateTimeStr(new Date());
        System.out.println("s => " + s);
        s = DateTimeUtil.formatDate2DateTimeStr(System.currentTimeMillis());
        System.out.println("s => " + s);
        s = DateTimeUtil.formatDate2DateTimeStr(123456);
        System.out.println("s => " + s);
        Assert.assertEquals("1970-01-01 08:02:03", s);
    }
    
    
    @Test
    public void parseDateFromLongStr() throws Exception {
        String s = "2011-07-11 15:46:03.255";
        Date d = DateTimeUtil.parseDateFromLongStr(s);
        System.out.println("d => " + d);
        Assert.assertEquals(s, DateTimeUtil.formatDate2LongStr(d));
    }
    
    @Test
    public void parseDateFromDateTimeStr() throws Exception {
        String s = "2011-07-11 15:46:03";
        Date d = DateTimeUtil.parseDateFromDateTimeStr(s);
        System.out.println("d => " + d);
        Assert.assertEquals(s, DateTimeUtil.formatDate2DateTimeStr(d));
    }
    
    @Test
    public void parseDateFromDateStr() throws Exception {
        String s = "2011-07-11";
        Date d = DateTimeUtil.parseDateFromDateStr(s);
        System.out.println("d => " + d);
        Assert.assertEquals(s, DateTimeUtil.formatDate2DateStr(d));
    }
    
    @Test
    public void parseDateFromShortStr() throws Exception {
        String s = "20110911";
        Date d = DateTimeUtil.parseDateFromShortStr(s);
        System.out.println("d => " + d);
        Assert.assertEquals(s, DateTimeUtil.formatDate2ShortStr(d));
    }
    
    
    @Test
    public void getWeekDay() throws Exception {
        String s = "2011-07-11";
        Date d = DateTimeUtil.parseDateFromDateStr(s);
        int weekDay = DateTimeUtil.getWeekDay(d);
        Assert.assertEquals(weekDay, 2);
    }
    
    @Test
    public void getLastWeekDay() throws Exception {
        String s = "2011-07-11";
        Date d = DateTimeUtil.parseDateFromDateStr(s);
        d = DateTimeUtil.getLastWeekDay(d);
        System.out.println("d => " + d);
        Assert.assertEquals("2011-07-04", DateTimeUtil.formatDate2DateStr(d));
    }
    
    @Test
    public void getLastHour() throws Exception {
        Date d = DateTimeUtil.getLastHour();
        int hour = DateTimeUtil.getHour(d);
        System.out.println("d => " + d);
        System.out.println("Hour => " + hour);
        int hour2 = DateTimeUtil.getHour(new Date());
        Assert.assertEquals(1, hour2 - hour);
    }
    
    @Test
    public void getLastWeekDay2() throws Exception {
        String s = "2011-07-11";
        Date d = DateTimeUtil.parseDateFromDateStr(s);
        d = DateTimeUtil.getLastWeekDay(3, d);
        System.out.println("d => " + d);
        Assert.assertEquals("2011-07-05", DateTimeUtil.formatDate2DateStr(d));
    }
    
    @Test
    public void getDayPart() throws Exception {
        String s = "2011-09-21 15:46:53";
        Date d = DateTimeUtil.parseDateFromDateTimeStr(s);
        int i = DateTimeUtil.getHour(d);
        Assert.assertEquals(i, 15);
        i = DateTimeUtil.getMinute(d);
        Assert.assertEquals(i, 46);
        i = DateTimeUtil.getWeekDay(d);
        Assert.assertEquals(i, 4);
        i = DateTimeUtil.getDayofMonth(d);
        Assert.assertEquals(i, 21);
        i = DateTimeUtil.getMonth(d);
        Assert.assertEquals(i, 8);
    }
}
