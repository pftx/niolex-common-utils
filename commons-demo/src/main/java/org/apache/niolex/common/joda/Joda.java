/**
 * Joda.java
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

import java.util.Locale;

import org.apache.niolex.commons.util.SystemUtil;
import org.joda.time.LocalDate;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-5-31
 */
public class Joda {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        // --- 计算上一个月的最后一天
        LocalDate now = new LocalDate();
        LocalDate lastDayOfPreviousMonth = now.minusMonths(1).dayOfMonth().withMaximumValue();
        // - Print
        SystemUtil.println(lastDayOfPreviousMonth.toString());
        // --- 计算 11 月中第一个星期一之后的第一个星期二
        LocalDate electionDate = now.monthOfYear().setCopy(11) // November
                .dayOfMonth() // Access Day Of Month Property
                .withMinimumValue() // Get its minimum value
                .plusDays(6) // Add 6 days
                .dayOfWeek() // Access Day Of Week Property
                .setCopy("Monday") // Set to Monday (it will round down)
                .plusDays(1); // Gives us Tuesday
        // - Print
        SystemUtil.println(electionDate.toString());
        // --- 从现在开始经过两个星期之后的日期
        LocalDate then = now.plusWeeks(2);
        // - Print
        SystemUtil.println(then.toString());
    }

}
