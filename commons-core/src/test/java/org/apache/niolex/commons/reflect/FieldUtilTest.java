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

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.junit.Assert;

import org.apache.niolex.commons.reflect.FieldUtil;
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
    public void testFieldsInt() throws Exception {
        Field[] fields = FieldUtil.getFields(FieldTestBean.class, int.class);
        System.out.println("FieldTestBean int fields => " + Arrays.toString(fields));
        Assert.assertEquals(fields.length, 2);
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
    public void testSafeFieldValue() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "strName");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldValue(field, bean, "Xie, Jiyun");
        Assert.assertEquals(bean.echoName(), "Xie, Jiyun");

        String fieldValue = FieldUtil.safeFieldValue(field, bean);
        Assert.assertEquals(fieldValue, "Xie, Jiyun");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSafeFieldValueInvalidHost() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "strName");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldValue(field, bean, "Xie, Jiyun");
        Assert.assertEquals(bean.echoName(), "Xie, Jiyun");

        String fieldValue = FieldUtil.safeFieldValue(field, "Invalid");
        Assert.assertEquals(fieldValue, "Xie, Jiyun");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSafeFieldValueInvalidAccess() throws Exception {
        Field field = spy(FieldUtil.getField(FieldTestBean.class, "strName"));
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldValue(field, bean, "Xie, Jiyun");
        Assert.assertEquals(bean.echoName(), "Xie, Jiyun");

        when(field.get(bean)).thenThrow(new IllegalAccessException("This is very good"));

        try {
            String fieldValue = FieldUtil.safeFieldValue(field, "Invalid");
            Assert.assertEquals(fieldValue, "Xie, Jiyun");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(e.getMessage(), "Failed to access the field.");
            throw e;
        }
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

}
