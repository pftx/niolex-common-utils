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
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;


public class FieldUtilTest extends FieldUtil {

    @Test
    public void testGetValue() throws Exception {
        FieldTestBean bean = new FieldTestBean();
        setValue(bean, "earned", 92831221.302902);
        Double d = getValue(bean, "earned");
        Assert.assertEquals(92831221.302902, d.doubleValue(), 0.000001);
    }

    @Test
    public void testGetStaticFieldValue() {
        FastBean bean = new FastBean();
        setValue(bean, "strName", "Lex tested it.");
        String fa = getValue(bean, "strName");
        System.out.println(fa);
        Assert.assertEquals(fa, "Lex tested it.");
    }

    @Test
    public void testSetValueAutoConvert() throws Exception {
        FieldTestBean bean = new FieldTestBean();
        setValueAutoConvert(bean, "earned", "92831221.302902");
        Double d = getValue(bean, "earned");
        Assert.assertEquals(92831221.302902, d.doubleValue(), 0.000001);
    }

    @Test
    public void testSetValueAutoConvertEnh() throws Exception {
        FieldTestBean bean = new FieldTestBean();
        setValueAutoConvert(bean, "earned", "9.2831221302902e7");
        Double d = getValue(bean, "earned");
        Assert.assertEquals(92831221.302902, d.doubleValue(), 0.000001);
    }

    @Test(expected=ItemNotFoundException.class)
    public void testSetValueAutoConvertEx() throws Exception {
        FieldTestBean bean = new FieldTestBean();
        setValueAutoConvert(bean, "earn", "9.2831221302902e7");
    }

    @Test(expected=ItemNotFoundException.class)
    public void testSetValue() throws Exception {
        FieldTestBean bean = new FieldTestBean();
        setValue(bean, "earnedIntx", 92831221.302902);
    }

    @Test(expected=ItemNotFoundException.class)
    public void testGetValueEx() throws Exception {
        FieldTestBean bean = new FieldTestBean();
        getValue(bean, "earn");
    }

    public static class I {
        public static int VER = 201;
        public int i = 101;
    }

    public static class F extends I {
        public static final long S_VER = 303;
        public final short f = 405;
    }

    @Test
    public void testGetFields() throws Exception {
        List<Field> fields = FieldUtil.getAllFields(F.class);
        int i = 0;
        while (i < fields.size()) {
            if (fields.get(i).getName().startsWith("$")) fields.remove(i);
            else ++i;
        }
        System.out.println("String fields => " + fields);
        Assert.assertEquals(4, fields.size());
    }

    @Test
    public void testGetAllFields() throws Exception {
        List<Field> list = FieldUtil.getFields(FieldTestBean.class, FieldFilter.create().noSynthetic());
        System.out.println("FieldTestBean fields => " + list);
        Assert.assertEquals(list.size(), 20);
    }

    @Test
    public void testGetFields2() throws Exception {
        List<Field> fields = FieldUtil.getFields(FieldTestBean.class, FieldFilter.exactType(long.class));
        FieldTestBean bean = new FieldTestBean();
        Assert.assertEquals(fields.size(), 1);
        long val;
        for (Field f : fields) {
            f.setAccessible(true);
            val = f.getLong(bean);
            System.out.println(f.getName() + " => " + val);
        }
    }

    @Test
    public void testGetFieldsInt() throws Exception {
        List<Field> fields = FieldUtil.getFields(FieldTestBean.class, FieldFilter.t(int.class));
        System.out.println("FieldTestBean int fields => " + fields);
        Assert.assertEquals(fields.size(), 5);
    }

    @Test
    public void testGetField() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "strName");
        System.out.println("FieldTestBean strName field => " + field);
        Assert.assertEquals(field.getName(), "strName");
        Assert.assertEquals(field.getType(), String.class);
    }

    @Test
    public void testGetFieldFronSuper() throws Exception {
        Field field = FieldUtil.getField(Sub.class, "isSuper");
        Assert.assertEquals(field.getName(), "isSuper");
        Assert.assertEquals(field.getType(), boolean.class);
    }

    @Test
    public void testSetFieldValue() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "strName");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldValue(bean, field, "Xie, Jiyun");
        Assert.assertEquals(bean.echoName(), "Xie, Jiyun");

        String fieldValue = FieldUtil.getFieldValue(bean, field);
        Assert.assertEquals(fieldValue, "Xie, Jiyun");

        field = FieldUtil.getField(FieldTestBean.class, "intLevel");
        FieldUtil.setFieldValue(bean, field, 5);
        Assert.assertEquals(bean.echoLevel(), 5);

        Integer intBeanLevel = FieldUtil.getFieldValue(bean, field);
        Assert.assertEquals(intBeanLevel.intValue(), 5);
    }

    @Test(expected=IllegalStateException.class)
    public void testUnsafeSetFieldValue() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "intId");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.unsafeSetFieldValue(bean, field, 3322);
    }

    @Test
    public void testSafeGetFieldValue() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "strName");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldValue(bean, field, "Xie, Jiyun");
        Assert.assertEquals(bean.echoName(), "Xie, Jiyun");

        Object fieldValue = FieldUtil.safeGetFieldValue(bean, field);
        Assert.assertEquals(fieldValue, "Xie, Jiyun");
    }

    @Test(expected=IllegalStateException.class)
    public void testUnsafeGetFieldValue() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "intId");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldValue(bean, field, 3322);
        field.setAccessible(false);
        Object fieldValue = FieldUtil.unsafeGetFieldValue(bean, field);
        Assert.assertEquals(fieldValue, "Xie, Jiyun");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSafeGetFieldValueInvalidHost() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "strName");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldValue(bean, field, "Xie, Jiyun");
        Assert.assertEquals(bean.echoName(), "Xie, Jiyun");

        Object fieldValue = FieldUtil.safeGetFieldValue("Invalid", field);
        Assert.assertEquals(fieldValue, "Xie, Jiyun");
    }

    @Test
    public void testValueLong() throws Exception {
    	Field field = FieldUtil.getField(FieldTestBean.class, "empno");
    	FieldTestBean bean = new FieldTestBean();
    	FieldUtil.setFieldValue(bean, field, 2130918321029L);
    	Long fieldValue = FieldUtil.getFieldValue(bean, field);
    	Assert.assertEquals(fieldValue.longValue(), 2130918321029L);
    }

    @Test
    public void testValueBool() throws Exception {
    	Field field = FieldUtil.getField(FieldTestBean.class, "gender");
    	FieldTestBean bean = new FieldTestBean();
    	FieldUtil.setFieldValue(bean, field, true);
    	Boolean fieldValue = FieldUtil.getFieldValue(bean, field);
    	Assert.assertEquals(fieldValue.booleanValue(), true);
    }

    @Test
    public void testValueByte() throws Exception {
    	Field field = FieldUtil.getField(FieldTestBean.class, "resvered");
    	FieldTestBean bean = new FieldTestBean();
    	FieldUtil.setFieldValue(bean, field, (byte)35);
    	Byte fieldValue = FieldUtil.getFieldValue(bean, field);
    	Assert.assertEquals(fieldValue.byteValue(), (byte)35);
    }

    @Test
    public void testValueShort() throws Exception {
    	Field field = FieldUtil.getField(FieldTestBean.class, "veridk");
    	FieldTestBean bean = new FieldTestBean();
    	FieldUtil.setFieldValue(bean, field, (short)33225);
    	Short fieldValue = FieldUtil.getFieldValue(bean, field);
    	Assert.assertEquals(fieldValue.shortValue(), (short)33225);
    }

    @Test
    public void testValueChar() throws Exception {
    	Field field = FieldUtil.getField(FieldTestBean.class, "chdier");
    	FieldTestBean bean = new FieldTestBean();
    	FieldUtil.setFieldValue(bean, field, 'c');
    	Character fieldValue = FieldUtil.getFieldValue(bean, field);
    	Assert.assertEquals(fieldValue.charValue(), 'c');
    }

    @Test
    public void testValueDouble() throws Exception {
    	Field field = FieldUtil.getField(FieldTestBean.class, "earned");
    	FieldTestBean bean = new FieldTestBean();
    	FieldUtil.setFieldValue(bean, field, 32212314.3134);
    	Double fieldValue = FieldUtil.getFieldValue(bean, field);
    	Assert.assertEquals(fieldValue, 32212314.3134, 0.0001);
    }

    @Test
    public void testValueFloat() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "tax");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldValue(bean, field, 312314.3134F);
        Float fieldValue = FieldUtil.getFieldValue(bean, field);
        Assert.assertEquals(fieldValue, 312314.3134F, 0.0001F);
    }

    @Test
    public void testSetFieldValueAutoConvertS() throws Exception {
    	Field field = FieldUtil.getField(FieldTestBean.class, "strName");
    	FieldTestBean bean = new FieldTestBean();
    	FieldUtil.setFieldValueAutoConvert(bean, field, "312314.3134F");
    	String fieldValue = FieldUtil.getFieldValue(bean, field);
    	Assert.assertEquals("312314.3134F", fieldValue);
    }

    @Test
    public void testSetFieldValueAutoConvertDate() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "time");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldValueAutoConvert(bean, field, "152531425");
        Date fieldValue = FieldUtil.getFieldValue(bean, field);
        Assert.assertEquals(152531425, fieldValue.getTime());
    }

    @Test
    public void testSetFieldValueAutoConvertIL() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "age");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldValueAutoConvert(bean, field, "65");
        Integer fieldValue = FieldUtil.getFieldValue(bean, field);
        Assert.assertEquals(65, fieldValue.intValue());
    }

    @Test
    public void testSetFieldValueAutoConvertI() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "intLevel");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldValueAutoConvert(bean, field, "34");
        int fieldValue = field.getInt(bean);
        Assert.assertEquals(34, fieldValue);
    }

    @Test
    public void testSetFieldValueAutoConvertLL() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "logno");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldValueAutoConvert(bean, field, "871658278165");
        Long fieldValue = FieldUtil.getFieldValue(bean, field);
        Assert.assertEquals(871658278165l, fieldValue.longValue());
    }

    @Test
    public void testSetFieldValueAutoConvertL() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "empno");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldValueAutoConvert(bean, field, "348281128282");
        long fieldValue = field.getLong(bean);
        Assert.assertEquals(348281128282l, fieldValue);
    }

    @Test
    public void testSetFieldValueAutoConvertSIL() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "dfijd");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldValueAutoConvert(bean, field, "5151");
        Short fieldValue = FieldUtil.getFieldValue(bean, field);
        Assert.assertEquals(5151, fieldValue.shortValue());
    }

    @Test
    public void testSetFieldValueAutoConvertSI() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "veridk");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldValueAutoConvert(bean, field, "13241");
        short fieldValue = field.getShort(bean);
        Assert.assertEquals(13241, fieldValue);
    }

    @Test
    public void testSetFieldValueAutoConvertBL() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "unused");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldValueAutoConvert(bean, field, "-66");
        Byte fieldValue = FieldUtil.getFieldValue(bean, field);
        Assert.assertEquals(-66, fieldValue.byteValue());
    }

    @Test
    public void testSetFieldValueAutoConvertB() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "resvered");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldValueAutoConvert(bean, field, "94");
        byte fieldValue = field.getByte(bean);
        Assert.assertEquals(94, fieldValue);
    }

    @Test
    public void testSetFieldValueAutoConvertBOOL() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "flag");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldValueAutoConvert(bean, field, "true");
        Boolean fieldValue = FieldUtil.getFieldValue(bean, field);
        Assert.assertEquals(true, fieldValue.booleanValue());
    }

    @Test
    public void testSetFieldValueAutoConvertBOO() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "gender");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldValueAutoConvert(bean, field, "true");
        boolean fieldValue = field.getBoolean(bean);
        Assert.assertEquals(true, fieldValue);
    }

    @Test
    public void testSetFieldValueAutoConvertCL() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "grade");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldValueAutoConvert(bean, field, "S");
        Character fieldValue = FieldUtil.getFieldValue(bean, field);
        Assert.assertEquals('S', fieldValue.charValue());
    }

    @Test
    public void testSetFieldValueAutoConvertC() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "chdier");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldValueAutoConvert(bean, field, "x");
        char fieldValue = field.getChar(bean);
        Assert.assertEquals('x', fieldValue);
    }

    @Test
    public void testSetFieldValueAutoConvertDL() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "sal");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldValueAutoConvert(bean, field, "9831091209843.4109434");
        Double fieldValue = FieldUtil.getFieldValue(bean, field);
        Assert.assertEquals(fieldValue, 9831091209843.4109434, 0.000000000001);
    }

    @Test
    public void testSetFieldValueAutoConvertD() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "earned");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldValueAutoConvert(bean, field, "8382389324.134901341309");
        double fieldValue = field.getDouble(bean);
        Assert.assertEquals(fieldValue, 8382389324.134901341309, 0.000000000001);
    }

    @Test
    public void testSetFieldValueAutoConvertFL() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "remain");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldValueAutoConvert(bean, field, "98496131.161691");
        Float fieldValue = FieldUtil.getFieldValue(bean, field);
        Assert.assertEquals(fieldValue, 98496131.161691f, 0.0000001f);
    }

    @Test
    public void testSetFieldValueAutoConvertF() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "tax");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldValueAutoConvert(bean, field, "8165.948651");
        float fieldValue = field.getFloat(bean);
        Assert.assertEquals(fieldValue, 8165.948651f, 0.0000001f);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testSetFieldValueAutoConvertNS() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "obj");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldValueAutoConvert(bean, field, "8165.948651");
        Float fieldValue = FieldUtil.getFieldValue(bean, field);
        Assert.assertEquals(fieldValue, 8165.948651f, 0.0000001f);
    }

    @Test(expected=IllegalStateException.class)
    public void testSetFieldValueAutoConvertIS() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "sal");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.unsafeSetFieldValueAutoConvert(bean, field, "8165.948651");
        Float fieldValue = FieldUtil.getFieldValue(bean, field);
        Assert.assertEquals(fieldValue, 8165.948651f, 0.0000001f);
    }

}
