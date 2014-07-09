/**
 * Calculate.java
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
package org.apache.niolex.common.joda;

import java.util.Date;
import java.util.Locale;

import org.apache.niolex.commons.util.SystemUtil;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Hours;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.joda.time.format.PeriodPrinter;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-5-31
 */
public class Calculate {

    private static String TIME_PATTERN = "M/d/yyyy h:mm:ss a";
    private static Hours FORMAL_TIME = Hours.EIGHT;
    private static Hours RESET_TIME = Hours.ONE;
    private static DateTimeFormatter FORMATTER;
    static PeriodPrinter PRINTER;

    static String simpleTime1 = "4/28/2010 8:50:26 AM";
    static String simpleTime2 = "4/28/2010 4:35:25 PM";
    static String simpleTime3 = "4/28/2010 5:51:54 PM";


    static {
        FORMATTER = DateTimeFormat.forPattern(TIME_PATTERN);
        PRINTER = new PeriodFormatterBuilder().appendHours().appendSuffix(
                " hour", " hours").appendSeparator(" and ").appendMinutes()
                .appendSuffix(" min", " minutes").appendSeparator(" ").appendSeconds()
                .appendSuffix(" sec", " seconds").toPrinter();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        //- Print
        SystemUtil.println(FORMATTER.print((new Date()).getTime()));
        //- Parse
        DateTime start = FORMATTER.parseDateTime(simpleTime1);
        DateTime end = FORMATTER.parseDateTime(simpleTime2);
        calcWorkTime(start, end);
        DateTime dead = FORMATTER.parseDateTime(simpleTime3);
        calcWorkTime(start, dead);
    }

    public static void calcWorkTime(DateTime start, DateTime end) {
        long worked = new Duration(start, end).minus(FORMAL_TIME.plus(RESET_TIME).toStandardDuration()).getMillis();

        StringBuffer sb = new StringBuffer();

        if (worked < 0) {
            sb.append("Worked not enough! Still need work : ");
            PRINTER.printTo(sb, new Period(Math.abs(worked)), Locale.CHINESE);
        } else {
            sb.append("Good job! Worked : ");
            PRINTER.printTo(sb, new Period(Math.abs(worked)).plusHours(FORMAL_TIME.getHours()), Locale.CHINESE);
        }

        System.out.println(sb.toString());
    }

}
