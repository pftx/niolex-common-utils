/**
 * Protected.java
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
package org.apache.niolex.common.grammar.inner;

import org.apache.niolex.common.grammar.inner.Wrapper.Base;
import org.apache.niolex.common.grammar.inner.Wrapper.Child;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-12-10
 */
public class Protected {

    public static void main(String[] args) {
        Base b = new Child();
        //System.out.println("private - " + b.pri);
        System.out.println("protected - " + b.pro);
        System.out.println("package - " + b.pkg);
    }

}
