/**
 * BeanUtilTest.java
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
package org.apache.niolex.commons.bean;


import static org.junit.Assert.*;

import java.util.Map;

import org.apache.niolex.commons.util.Const;
import org.junit.Test;

import com.google.common.collect.Maps;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-6-20
 */
public class BeanUtilTest extends BeanUtil {

    @Test
    public void testToString() throws Exception {
        assertEquals("{,  i=123,  name=A,  b=true,  num=321,  fmt=0.0,  any=null,}",
                toString(new A()).replaceAll(Const.LINE_SP, ","));
    }

    @Test
    public void testMerge() throws Exception {
        A a = new A();
        B b = new B();
        assertEquals(a.i, 123);
        assertEquals(b.i, 665);
        merge(a, b);
        assertTrue(a.b);
        assertEquals(a.i, 665);
        assertEquals(a.name, "B");
    }

    @Test
    public void testMergeFromMap() throws Exception {
        A a = new A();
        Map<String, Object> from = Maps.newHashMap();
        from.put("i", 1231);
        from.put("name", "Lex");
        from.put("b", false);
        from.put("num", 98435);
        from.put("fmt", 6653);
        assertEquals(a.i, 123);
        assertTrue(a.b);
        merge(a, from);
        assertFalse(a.b);
        assertEquals(a.i, 1231);
        assertEquals(a.name, "Lex");
        assertEquals(a.num, 98435);
        assertEquals(a.fmt, 6653, 0.00001);
    }

    @Test
    public void testMergeFromMapNotCompatible() throws Exception {
        A a = new A();
        Map<String, Object> from = Maps.newHashMap();
        from.put("i", 1231.6);
        from.put("name", "Lex");
        from.put("good", "Lex");
        assertEquals(a.i, 123);
        assertTrue(a.b);
        merge(a, from);
        assertEquals(a.i, 123);
        assertEquals(a.name, "Lex");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testMergeFromMapThrowException() throws Exception {
        A a = new A();
        Map<String, Object> from = null;
        merge(a, from);
        assertEquals(a.i, 123);
        assertTrue(a.b);
    }

    @Test
    public void testMergeIntToLong() throws Exception {
        A a = new A();
        B b = new B();
        assertEquals(a.num, 321);
        assertEquals(b.num.intValue(), 3721);
        merge(a, b);
        assertEquals(a.num, 3721);
        assertEquals(a.name, "B");
    }

    @Test
    public void testMergeLongToDouble() throws Exception {
        A a = new A();
        B b = new B();
        assertEquals(a.fmt, 0, 0.00001);
        assertEquals(b.fmt.longValue(), 102929);
        merge(a, b);
        assertEquals(a.fmt, 102929, 0.00001);
        assertEquals(a.name, "B");
    }

    @Test
    public void testMergeDoubleToLongFail() throws Exception {
        A a = new A();
        B b = new B();
        assertEquals(a.num, 321);
        b.num = new Double(32.133);
        assertEquals(b.num.doubleValue(), 32.133, 0.0001);
        merge(a, b);
        assertEquals(a.num, 321);
        assertEquals(a.name, "B");
    }

    @Test
    public void testMergeAnythingToObject() throws Exception {
        A a = new A();
        B b = new B();
        b.any = new Double(32.133);
        assertEquals(((Double)b.any).doubleValue(), 32.133, 0.0001);
        merge(a, b);
        assertEquals(((Double)a.any).doubleValue(), 32.133, 0.0001);
        b.any = "Have a nice Day!";
        merge(a, b);
        assertEquals(a.any, "Have a nice Day!");
        assertEquals(a.name, "B");
    }

    @Test
    public void testMergeDontDefault() throws Exception {
        A a = new A();
        B b = new B();
        b.i = 0;
        assertEquals(a.i, 123);
        merge(a, b);
        assertEquals(a.i, 123);
    }

    @Test
    public void testMergeDefault() throws Exception {
        A a = new A();
        B b = new B();
        b.i = 0;
        assertEquals(a.i, 123);
        merge(a, b, true);
        assertEquals(a.i, 0);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testMergeFailure() throws Exception {
        A a = new A();
        merge(a, null, true);
    }

    static class A {
        private int i = 123;
        private String name = "A";
        private boolean b = true;
        private long num = 321;
        private double fmt;
        private Object any;

        /**
         * @return the i
         */
        public int getI() {
            return i;
        }
        /**
         * @param i the i to set
         */
        public void setI(int i) {
            this.i = i;
        }
        /**
         * @return the name
         */
        public String getName() {
            return name;
        }
        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }
        /**
         * @return the b
         */
        public boolean isB() {
            return b;
        }
        /**
         * @param b the b to set
         */
        public void setB(boolean b) {
            this.b = b;
        }
        /**
         * @return the num
         */
        public long getNum() {
            return num;
        }
        /**
         * @param num the num to set
         */
        public void setNum(long num) {
            this.num = num;
        }
        /**
         * @return the fmt
         */
        public double getFmt() {
            return fmt;
        }
        /**
         * @param fmt the fmt to set
         */
        public void setFmt(double fmt) {
            this.fmt = fmt;
        }
        /**
         * @return the any
         */
        public Object getAny() {
            return any;
        }
        /**
         * @param any the any to set
         */
        public void setAny(Object any) {
            this.any = any;
        }

    }

    static class B {
        private int i = 665;
        private String name = "B";
        private Boolean b = null;
        private Number num = new Integer(3721);
        private Number fmt = new Long(102929);
        private Object any;

        /**
         * @return the i
         */
        public int getI() {
            return i;
        }
        /**
         * @param i the i to set
         */
        public void setI(int i) {
            this.i = i;
        }
        /**
         * @return the name
         */
        public String getName() {
            return name;
        }
        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }
        /**
         * @return the b
         */
        public Boolean getB() {
            return b;
        }
        /**
         * @param b the b to set
         */
        public void setB(Boolean b) {
            this.b = b;
        }
        /**
         * @return the num
         */
        public Number getNum() {
            return num;
        }
        /**
         * @param num the num to set
         */
        public void setNum(Number num) {
            this.num = num;
        }
        /**
         * @return the fmt
         */
        public Number getFmt() {
            return fmt;
        }
        /**
         * @param fmt the fmt to set
         */
        public void setFmt(Number fmt) {
            this.fmt = fmt;
        }
        /**
         * @return the any
         */
        public Object getAny() {
            return any;
        }
        /**
         * @param any the any to set
         */
        public void setAny(Object any) {
            this.any = any;
        }
        /**
         * @param any the any to set
         */
        public void setDummy(Object any) {
            this.any = any;
        }

    }

    @Test
    public void testIsNumericPrimitiveDefaultValue() throws Exception {
        assertTrue(isNumericPrimitiveDefaultValue(long.class, 0l));
        assertFalse(isNumericPrimitiveDefaultValue(long.class, -1l));
        assertFalse(isNumericPrimitiveDefaultValue(long.class, 1l));

        assertTrue(isNumericPrimitiveDefaultValue(int.class, 0));
        assertFalse(isNumericPrimitiveDefaultValue(int.class, 1));
        assertFalse(isNumericPrimitiveDefaultValue(int.class, -1));

        assertTrue(isNumericPrimitiveDefaultValue(byte.class, (byte)0));
        assertFalse(isNumericPrimitiveDefaultValue(byte.class, (byte)1));
        assertFalse(isNumericPrimitiveDefaultValue(byte.class, (byte)-1));

        assertTrue(isNumericPrimitiveDefaultValue(double.class, 0.0));
        assertFalse(isNumericPrimitiveDefaultValue(double.class, 0.00001));
        assertFalse(isNumericPrimitiveDefaultValue(double.class, -0.00000001));

        assertTrue(isNumericPrimitiveDefaultValue(float.class, 0.0));
        assertFalse(isNumericPrimitiveDefaultValue(float.class, 0.0000001f));
        assertFalse(isNumericPrimitiveDefaultValue(float.class, -0.0000001f));

        assertTrue(isNumericPrimitiveDefaultValue(short.class, (short)0));
        assertFalse(isNumericPrimitiveDefaultValue(short.class, (short)1));
        assertFalse(isNumericPrimitiveDefaultValue(short.class, (short)-1));
    }

    @Test
    public void testIsNumericPrimitiveDefaultValueNotNumeric() throws Exception {
        assertFalse(isNumericPrimitiveDefaultValue(boolean.class, false));
        assertFalse(isNumericPrimitiveDefaultValue(boolean.class, true));
    }

    @Test
    public void testIsNumericPrimitiveDefaultValueNotPrimitive() throws Exception {
        assertFalse(isNumericPrimitiveDefaultValue(Integer.class, 1));
        assertFalse(isNumericPrimitiveDefaultValue(Long.class, 1));
        assertFalse(isNumericPrimitiveDefaultValue(Object.class, 1));
        assertFalse(isNumericPrimitiveDefaultValue(String.class, "Lex"));
    }
}
