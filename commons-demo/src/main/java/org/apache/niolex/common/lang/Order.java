/**
 * Order.java
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
package org.apache.niolex.common.lang;

import static org.apache.niolex.commons.util.SystemUtil.*;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-3-25
 */
public class Order {

    static {
        println("Static %d", 1);
    }

    static {
        println("Static %d", 2);
    }

    {
        println("unname %d", 1);
    }

    {
        println("unname %d", 2);
    }

    public Order() {
        println("constructor %d", 1);
    }

    public Order(int k) {
        println("constructor %d", k);
    }

    public static void main(String[] args) {
        new Order();
        println("-----------------------");
        new Order(5);
    }

}
