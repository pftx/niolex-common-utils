/**
 * Inner.java
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
package org.apache.niolex.common.demo;

import java.io.Serializable;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-3-25
 */
public class Inner {

    private int k;

    /**
     * Constructor
     * @param k
     */
    public Inner(int k) {
        super();
        this.k = k;
    }

    /**
     * @return the k
     */
    public int getK() {

        class InnerIn2 extends Abs implements Serializable {

            private static final long serialVersionUID = 1437330936153846177L;

        }

        return (int) (k + InnerIn2.serialVersionUID >> 32);
    }

    /**
     * @param k the k to set
     */
    public void setK(int k) {
        this.k = k;
    }

    class InnerIn extends Abs implements Serializable {

        private static final long serialVersionUID = 1437330936153846177L;

    }

    public static void main(String[] args) {}

}

abstract class Abs extends Inner {

    /**
     * Constructor
     */
    public Abs() {
        super(9);
    }

}
