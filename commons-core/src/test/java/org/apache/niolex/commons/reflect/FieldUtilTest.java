/**
 * FieldUtilTest.java
 *
 * Copyright 2011 Niolex, Inc.
 *
 * Niolex licenses this file to you under the Apache License, version 2.0
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
package org.apache.niolex.commons.reflect;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;


public class FieldUtilTest {

    @Test
    public void testFields() throws Exception {
        Field[] fields = FieldUtil.getFields(FieldTestBean.class);
        System.out.println("FieldTestBean fields => " + Arrays.toString(fields));
        Assert.assertTrue(fields.length >= 11);
        new FieldUtil(){};
    }

    @Test
    public void testGetFields() throws Exception {
        Field[] fields = FieldUtil.getFields(FieldTestBean.class, long.class);
        FieldTestBean bean = new FieldTestBean();
        long val;
        for (Field f : fields) {
            f.setAccessible(true);
            val = f.getLong(bean);
            System.out.println(f.getName() + " => " + val);
        }
    }

    @Test
    public void testFieldsInt() throws Exception {
        Field[] fields = FieldUtil.getFields(FieldTestBean.class, int.class);
        System.out.println("FieldTestBean int fields => " + Arrays.toString(fields));
        Assert.assertEquals(fields.length, 5);
    }

    @Test
    public void testField() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "strName");
        System.out.println("FieldTestBean strName field => " + field);
        Assert.assertEquals(field.getName(), "strName");
        Assert.assertEquals(field.getType(), String.class);
    }

    @Test
    public void testSetFieldValue() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "strName");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldValue(field, bean, "Xie, Jiyun");
        Assert.assertEquals(bean.echoName(), "Xie, Jiyun");

        String fieldValue = FieldUtil.getFieldValue(field, bean);
        Assert.assertEquals(fieldValue, "Xie, Jiyun");

        field = FieldUtil.getField(FieldTestBean.class, "intLevel");
        FieldUtil.setFieldValue(field, bean, 5);
        Assert.assertEquals(bean.echoLevel(), 5);

        Integer intBeanLevel = FieldUtil.getFieldValue(field, bean);
        Assert.assertEquals(intBeanLevel.intValue(), 5);
    }

    @Test
    public void testSafeGetFieldValue() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "strName");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldValue(field, bean, "Xie, Jiyun");
        Assert.assertEquals(bean.echoName(), "Xie, Jiyun");

        Object fieldValue = FieldUtil.safeGetFieldValue(field, bean);
        Assert.assertEquals(fieldValue, "Xie, Jiyun");
    }

    @Test(expected=IllegalStateException.class)
    public void testUnsafeGetFieldValue() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "intId");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldValue(field, bean, 3322);
        field.setAccessible(false);
        Object fieldValue = FieldUtil.unsafeGetFieldValue(field, bean);
        Assert.assertEquals(fieldValue, "Xie, Jiyun");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSafeGetFieldValueInvalidHost() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "strName");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldValue(field, bean, "Xie, Jiyun");
        Assert.assertEquals(bean.echoName(), "Xie, Jiyun");

        Object fieldValue = FieldUtil.safeGetFieldValue(field, "Invalid");
        Assert.assertEquals(fieldValue, "Xie, Jiyun");
    }

    @Test
    public void testValueLong() throws Exception {
    	Field field = FieldUtil.getField(FieldTestBean.class, "empno");
    	FieldTestBean bean = new FieldTestBean();
    	FieldUtil.setFieldValue(field, bean, 2130918321029L);
    	Long fieldValue = FieldUtil.getFieldValue(field, bean);
    	Assert.assertEquals(fieldValue.longValue(), 2130918321029L);
    }

    @Test
    public void testValueBool() throws Exception {
    	Field field = FieldUtil.getField(FieldTestBean.class, "gender");
    	FieldTestBean bean = new FieldTestBean();
    	FieldUtil.setFieldValue(field, bean, true);
    	Boolean fieldValue = FieldUtil.getFieldValue(field, bean);
    	Assert.assertEquals(fieldValue.booleanValue(), true);
    }

    @Test
    public void testValueByte() throws Exception {
    	Field field = FieldUtil.getField(FieldTestBean.class, "resvered");
    	FieldTestBean bean = new FieldTestBean();
    	FieldUtil.setFieldValue(field, bean, (byte)35);
    	Byte fieldValue = FieldUtil.getFieldValue(field, bean);
    	Assert.assertEquals(fieldValue.byteValue(), (byte)35);
    }

    @Test
    public void testValueShort() throws Exception {
    	Field field = FieldUtil.getField(FieldTestBean.class, "veridk");
    	FieldTestBean bean = new FieldTestBean();
    	FieldUtil.setFieldValue(field, bean, (short)33225);
    	Short fieldValue = FieldUtil.getFieldValue(field, bean);
    	Assert.assertEquals(fieldValue.shortValue(), (short)33225);
    }

    @Test
    public void testValueChar() throws Exception {
    	Field field = FieldUtil.getField(FieldTestBean.class, "chdier");
    	FieldTestBean bean = new FieldTestBean();
    	FieldUtil.setFieldValue(field, bean, 'c');
    	Character fieldValue = FieldUtil.getFieldValue(field, bean);
    	Assert.assertEquals(fieldValue.charValue(), 'c');
    }

    @Test
    public void testValueDouble() throws Exception {
    	Field field = FieldUtil.getField(FieldTestBean.class, "earned");
    	FieldTestBean bean = new FieldTestBean();
    	FieldUtil.setFieldValue(field, bean, 32212314.3134);
    	Double fieldValue = FieldUtil.getFieldValue(field, bean);
    	Assert.assertEquals(fieldValue, 32212314.3134, 0.0001);
    }

    @Test
    public void testValueFloat() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "tax");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldValue(field, bean, 312314.3134F);
        Float fieldValue = FieldUtil.getFieldValue(field, bean);
        Assert.assertEquals(fieldValue, 312314.3134F, 0.0001F);
    }

    @Test
    public void testSetFieldWithCorrectValueS() throws Exception {
    	Field field = FieldUtil.getField(FieldTestBean.class, "strName");
    	FieldTestBean bean = new FieldTestBean();
    	FieldUtil.setFieldWithCorrectValue(field, bean, "312314.3134F");
    	String fieldValue = FieldUtil.getFieldValue(field, bean);
    	Assert.assertEquals("312314.3134F", fieldValue);
    }

    @Test
    public void testSetFieldWithCorrectValueDate() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "time");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldWithCorrectValue(field, bean, "152531425");
        Date fieldValue = FieldUtil.getFieldValue(field, bean);
        Assert.assertEquals(152531425, fieldValue.getTime());
    }

    @Test
    public void testSetFieldWithCorrectValueIL() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "age");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldWithCorrectValue(field, bean, "65");
        Integer fieldValue = FieldUtil.getFieldValue(field, bean);
        Assert.assertEquals(65, fieldValue.intValue());
    }

    @Test
    public void testSetFieldWithCorrectValueI() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "intLevel");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldWithCorrectValue(field, bean, "34");
        int fieldValue = field.getInt(bean);
        Assert.assertEquals(34, fieldValue);
    }

    @Test
    public void testSetFieldWithCorrectValueLL() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "logno");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldWithCorrectValue(field, bean, "871658278165");
        Long fieldValue = FieldUtil.getFieldValue(field, bean);
        Assert.assertEquals(871658278165l, fieldValue.longValue());
    }

    @Test
    public void testSetFieldWithCorrectValueL() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "empno");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldWithCorrectValue(field, bean, "348281128282");
        long fieldValue = field.getLong(bean);
        Assert.assertEquals(348281128282l, fieldValue);
    }

    @Test
    public void testSetFieldWithCorrectValueSIL() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "dfijd");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldWithCorrectValue(field, bean, "5151");
        Short fieldValue = FieldUtil.getFieldValue(field, bean);
        Assert.assertEquals(5151, fieldValue.shortValue());
    }

    @Test
    public void testSetFieldWithCorrectValueSI() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "veridk");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldWithCorrectValue(field, bean, "13241");
        short fieldValue = field.getShort(bean);
        Assert.assertEquals(13241, fieldValue);
    }

    @Test
    public void testSetFieldWithCorrectValueBL() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "unused");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldWithCorrectValue(field, bean, "-66");
        Byte fieldValue = FieldUtil.getFieldValue(field, bean);
        Assert.assertEquals(-66, fieldValue.byteValue());
    }

    @Test
    public void testSetFieldWithCorrectValueB() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "resvered");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldWithCorrectValue(field, bean, "94");
        byte fieldValue = field.getByte(bean);
        Assert.assertEquals(94, fieldValue);
    }

    @Test
    public void testSetFieldWithCorrectValueBOOL() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "flag");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldWithCorrectValue(field, bean, "true");
        Boolean fieldValue = FieldUtil.getFieldValue(field, bean);
        Assert.assertEquals(true, fieldValue.booleanValue());
    }

    @Test
    public void testSetFieldWithCorrectValueBOO() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "gender");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldWithCorrectValue(field, bean, "true");
        boolean fieldValue = field.getBoolean(bean);
        Assert.assertEquals(true, fieldValue);
    }

    @Test
    public void testSetFieldWithCorrectValueCL() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "grade");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldWithCorrectValue(field, bean, "S");
        Character fieldValue = FieldUtil.getFieldValue(field, bean);
        Assert.assertEquals('S', fieldValue.charValue());
    }

    @Test
    public void testSetFieldWithCorrectValueC() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "chdier");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldWithCorrectValue(field, bean, "x");
        char fieldValue = field.getChar(bean);
        Assert.assertEquals('x', fieldValue);
    }

    @Test
    public void testSetFieldWithCorrectValueDL() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "sal");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldWithCorrectValue(field, bean, "9831091209843.4109434");
        Double fieldValue = FieldUtil.getFieldValue(field, bean);
        Assert.assertEquals(fieldValue, 9831091209843.4109434, 0.000000000001);
    }

    @Test
    public void testSetFieldWithCorrectValueD() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "earned");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldWithCorrectValue(field, bean, "8382389324.134901341309");
        double fieldValue = field.getDouble(bean);
        Assert.assertEquals(fieldValue, 8382389324.134901341309, 0.000000000001);
    }

    @Test
    public void testSetFieldWithCorrectValueFL() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "remain");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldWithCorrectValue(field, bean, "98496131.161691");
        Float fieldValue = FieldUtil.getFieldValue(field, bean);
        Assert.assertEquals(fieldValue, 98496131.161691f, 0.0000001f);
    }

    @Test
    public void testSetFieldWithCorrectValueF() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "tax");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldWithCorrectValue(field, bean, "8165.948651");
        float fieldValue = field.getFloat(bean);
        Assert.assertEquals(fieldValue, 8165.948651f, 0.0000001f);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testSetFieldWithCorrectValueNS() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "obj");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldWithCorrectValue(field, bean, "8165.948651");
        Float fieldValue = FieldUtil.getFieldValue(field, bean);
        Assert.assertEquals(fieldValue, 8165.948651f, 0.0000001f);
    }

}
