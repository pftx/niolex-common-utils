/**
 * Reflect.java
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
package org.apache.niolex.common.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 3.0.1
 * @since Jan 20, 2017
 */
public final class Reflect {

    public abstract static class Holder {

        private Holder() {
            super();
        }

        public abstract int getCode();
    }

    static class ContextHolder extends Holder {


        /**
         * This is the override of super method.
         * 
         * @see org.apache.niolex.common.reflect.Reflect.Holder#getCode()
         */
        @Override
        public int getCode() {
            return 0;
        }

    }

    static class TextHolder extends Holder {

        /**
         * This is the override of super method.
         * 
         * @see org.apache.niolex.common.reflect.Reflect.Holder#getCode()
         */
        @Override
        public int getCode() {
            return 1;
        }

    }

    public static void main(String[] args)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        for (Class<?> c : Reflect.class.getDeclaredClasses()) {
            if (Holder.class.isAssignableFrom(c) && !Holder.class.equals(c)) {
                System.out.println(c);
                Constructor<?>[] constructors = c.getDeclaredConstructors();
                System.out.println(Arrays.toString(constructors));
                Constructor<?> cs = constructors[0];
                cs.setAccessible(true);
                Holder h = (Holder) cs.newInstance();
                System.out.println(h.getClass().getSimpleName() + ".getCode() = " + h.getCode());
            }
        }
    }

}
