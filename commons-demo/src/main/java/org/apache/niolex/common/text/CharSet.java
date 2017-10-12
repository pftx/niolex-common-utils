/**
 * CharSet.java
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
package org.apache.niolex.common.text;

import org.apache.niolex.commons.codec.Base16Util;
import org.apache.niolex.commons.codec.StringUtil;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-8-6
 */
public class CharSet {

    /**
     * @param args
     */
    public static void main(String[] args) throws Throwable {
        String str = "abc中国lex谢佶芸";
        byte[] utf = StringUtil.strToUtf8Byte(str);
        byte[] gbk = StringUtil.strToGbkByte(str);
        String out = Base16Util.byteToBase16(utf);
        String gbo = Base16Util.byteToBase16(gbk);
        System.out.println("Utf8 => " + out);
        // -----
        System.out.println("gbk0 => " + gbo);
    }

}
