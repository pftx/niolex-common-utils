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
package org.apache.niolex.common.lang;

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

        class InnerInFunc extends Abs implements Serializable {

            private static final long serialVersionUID = 65493165L;

        }

        return (int) (k + InnerInFunc.serialVersionUID >> 32);
    }

    /**
     * @param k the k to set
     */
    public void setK(int k) {
        this.k = k;
    }

    class InnerIn extends Abs implements Serializable {

        private static final long serialVersionUID = 1437330936153846177L;

        class InnerInIn extends Abs implements Serializable {

            private static final long serialVersionUID = -5504634539704153812L;

        }

    }

    static class InnerInS extends Abs implements Serializable {

        private static final long serialVersionUID = 5674965464l;

        static class InnerInSInS extends InnerInS implements Serializable {

            private static final long serialVersionUID = 2872742129149123019L;

        }

        class InnerInSIn extends InnerInS implements Serializable {

            private static final long serialVersionUID = 3968496184331651819L;

        }

    }

    public static void main(String[] args) {
        System.out.println("You can create inner class inside a function!!");
        System.out.println("But no static inner class inside a function!!");
    }

}

abstract class Abs extends Inner {

    /**
     * Constructor
     */
    public Abs() {
        super(9);
    }

}
