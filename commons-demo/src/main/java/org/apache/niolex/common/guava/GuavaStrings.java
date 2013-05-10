/**
 * GuavaStrings.java
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
package org.apache.niolex.common.guava;

import com.google.common.base.CaseFormat;
import com.google.common.base.Joiner;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-5-4
 */
public class GuavaStrings {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String str = CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, "good-morning");
        System.out.println("lower camel => " + str);

        Joiner joiner = Joiner.on("; ").useForNull("null");
        str = joiner.join("nice", "talk", null, "name");
        System.out.println("join on ';' => " + str);
    }

}
