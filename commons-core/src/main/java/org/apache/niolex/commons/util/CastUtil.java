/**
 * CastUtil.java
 *
 * Copyright 2014 the original author or authors.
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

/**
 * This utility is used to cast values from a bigger range into a smaller range.
 *
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-11-27
 */
public abstract class CastUtil {

    /**
     * Cast double values into integer.
     *
     * @param d the double value to be casted
     * @return the integer
     */
    public static int toInt(double d) {
        if (d > Integer.MAX_VALUE || d < Integer.MIN_VALUE) {
            throw new IllegalArgumentException("Value out of int range - " + d);
        }

        return (int)d;
    }

    /**
     * Cast long values into integer.
     *
     * @param d the long value to be casted
     * @return the integer
     */
    public static int toInt(long d) {
        if (d > Integer.MAX_VALUE || d < Integer.MIN_VALUE) {
            throw new IllegalArgumentException("Value out of int range - " + d);
        }

        return (int)d;
    }

}
