/**
 * DateTimeHelper.java
 *
 * Copyright 2017 the original author or authors.
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

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 15, 2017
 */
public class DateTimeHelper {

    /**
     * The long data time format, including time zone info.
     */
    public static final String LONG_FORMAT_WITH_TZ = "yyyy-MM-dd HH:mm:ss.SSSXXX";

    /**
     * The date time format, including time zone info.
     */
    public static final String ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX";

    public static final String DEFAULT_TIME = "2017-11-11 00:00:00";

    public static final String DEFAULT_TIME_ZONE = "GMT+08:00";

    public static final String GMT = "GMT";

    private static final int DATE_LEN = DateTimeUtil.DATE_FORMAT.length();
    private static final int TIME_LEN = DateTimeUtil.TIME_FORMAT.length();

    private static final ThreadLocal<String> timezoneContext = new ThreadLocal<String>() {

        @Override
        protected String initialValue() {
            return DEFAULT_TIME_ZONE;
        }

    };

    public static void setTimezoneContext(String timezone) {
        timezoneContext.set(timezone);
    }

    public static DateFormat getContextFormatter() {
        String timezone = timezoneContext.get();
        if (timezone == null) {
            throw new IllegalStateException("The timezone context is null.");
        }
        return getFormatter(timezone);
    }

    public static DateFormat getFormatter(String timezone) {
        return getFormatter(DateTimeUtil.DATE_TIME_FORMAT, timezone);
    }

    public static DateFormat getFormatter(String format, String timezone) {
        TimeZone zone = getTimeZone(timezone);
        DateFormat dateFormat = DateTimeUtil.getDateFormat(format);
        dateFormat.setTimeZone(zone);

        return dateFormat;
    }

    public static TimeZone getTimeZone(String value) {
        value = value.trim();
        if (value.startsWith("+") || value.startsWith("-")) {
            value = GMT + value;
        }

        TimeZone zone = TimeZone.getTimeZone(value);
        if (zone.getID().equals(GMT) && !GMT.equalsIgnoreCase(value)) {
            throw new IllegalArgumentException("Invalid timezone: '" + value + "'.");
        }

        return zone;
    }

    public static boolean isValidTimezone(String value) {
        try {
            getTimeZone(value);
            return true;
        } catch (Exception e) { // NOSONAR
            return false;
        }
    }

    public static Date parseDate(String date) {
        return parseDate(date, DEFAULT_TIME_ZONE);
    }

    public static Date parseDate(String date, String timezone) {
        if (date == null) {
            return null;
        }

        String[] parts = date.split("[ T]", 2);
        String format = DateTimeUtil.DATE_TIME_FORMAT;
        if (parts[0].length() > DATE_LEN) {
            throw new IllegalArgumentException("Invalid date: '" + date + "'");
        }
        if (parts.length == 1) {
            date = parts[0] + DEFAULT_TIME.substring(date.length());
        } else {
            String time = parts[1];
            String[] extras = time.split("\\.");
            if (extras.length == 1) {
                if (time.length() < TIME_LEN) {
                    throw new IllegalArgumentException("Invalid date: '" + date + "'");
                } else if (time.length() > TIME_LEN) {
                    date = parts[0] + " " + time.substring(0, TIME_LEN);
                    timezone = time.substring(TIME_LEN);
                } else {
                    date = parts[0] + " " + parts[1];
                }
            } else if (extras.length == 2) {
                if (extras[1].length() > 3) {
                    String frag = extras[1];
                    date = parts[0] + " " + extras[0] + "." + frag.substring(0, 3);
                    format = DateTimeUtil.LONG_FORMAT;
                    timezone = frag.substring(3);
                } else {
                    date = parts[0] + " " + parts[1];
                    format = DateTimeUtil.LONG_FORMAT;
                }
            } else {
                throw new IllegalArgumentException("Invalid date: '" + date + "'");
            }
        }

        DateFormat formatter = getFormatter(format, timezone);

        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date: '" + date + "' start at " + e.getErrorOffset());
        }
    }

    public static boolean isValidDate(String value) {
        try {
            parseDate(value);
            return true;
        } catch (Exception e) { // NOSONAR
            return false;
        }
    }

}
