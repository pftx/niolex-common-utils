/**
 * FieldUtilTest.java
 *
 * Copyright 2011 Baidu, Inc.
 *
 * Baidu licenses this file to you under the Apache License, version 2.0
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

import junit.framework.Assert;

import org.apache.niolex.commons.reflect.FieldUtil;
import org.junit.Test;


class FieldTestBean {
    private String strName;
    @SuppressWarnings("unused")
    private int intId;
    private int intLevel;
    @SuppressWarnings("unused")
    private Integer age;
    
    public String echoName() {
        System.out.println("My Name IS " + strName + ", Welcome to use FieldUtil!");
        return strName;
    }
    
    public int echoLevel() {
        System.out.println("My English IS " + intLevel + ", Welcome to use FieldUtil!");
        return intLevel;
    }
}
public class FieldUtilTest {
    
    @Test
    public void testFields() throws Exception {
        Field[] fields = FieldUtil.getFields(FieldTestBean.class);
        System.out.println("FieldTestBean fields => " + Arrays.toString(fields));
        Assert.assertEquals(fields.length, 4);
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
    }

    @Test
    public void testSetFieldValue() throws Exception {
        Field field = FieldUtil.getField(FieldTestBean.class, "strName");
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setFieldValue(field, bean, "Xie, Jiyun");
        Assert.assertEquals(bean.echoName(), "Xie, Jiyun");
        
        String strBeanName = FieldUtil.getFieldValue(field, bean);
        Assert.assertEquals(strBeanName, "Xie, Jiyun");
        
        field = FieldUtil.getField(FieldTestBean.class, "intLevel");
        FieldUtil.setFieldValue(field, bean, 5);
        Assert.assertEquals(bean.echoLevel(), 5);
        
        int intBeanLevel = FieldUtil.getFieldValue(field, bean);
        Assert.assertEquals(intBeanLevel, 5);
    }
    
}
