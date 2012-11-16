/**
 * FastFieldUtilTest.java
 *
 * Copyright 2012 Niolex, Inc.
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

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Assert;

import org.junit.Test;

import com.esotericsoftware.reflectasm.FieldAccess;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-7-20
 */
public class FastFieldUtilTest {

	/**
	 * Test method for {@link org.apache.niolex.commons.reflect.FastFieldUtil#getFields(java.lang.Class)}.
	 */
	@Test
	public void testGetFields() {
		String[] fields = FastFieldUtil.getFields(FieldTestBean.class);
        System.out.println("FieldTestBean fields => " + Arrays.toString(fields));
        Assert.assertTrue(fields.length >= 3);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.reflect.FastFieldUtil#getFieldAccess(java.lang.Class)}.
	 */
	@Test
	public void testGetFieldAccess() {
		FieldAccess fa = FastFieldUtil.getFieldAccess(FieldTestBean.class);
		FieldTestBean bean = new FieldTestBean();
		fa.set(bean, 0, "Not yet implemented");
		Object name = fa.get(bean, 0);
		System.out.println(name);
		Assert.assertEquals(bean.echoName(), "Not yet implemented");
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.reflect.FastFieldUtil#getFieldAccess(java.lang.Class)}.
	 */
	@Test
	public void testGetFieldValue() {
		FieldTestBean bean = new FieldTestBean();
		FastFieldUtil.setFieldValue("strName", bean, "Not yet implemented");
		String fa = FastFieldUtil.getFieldValue("strName", bean);
		System.out.println(fa);
		Assert.assertEquals(bean.echoName(), "Not yet implemented");
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.reflect.FastFieldUtil#setFieldValue(java.lang.String, java.lang.Object, boolean)}.
	 */
	@Test
	public void testSetFieldValueStringObjectBoolean() {
		FastBean b = new FastBean();
		FastFieldUtil.setFieldValue("gender", b, true);
		assertTrue(b.gender);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.reflect.FastFieldUtil#setFieldValue(java.lang.String, java.lang.Object, byte)}.
	 */
	@Test
	public void testSetFieldValueStringObjectByte() {
		FastBean b = new FastBean();
		FastFieldUtil.setFieldValue("resvered", b, (byte)123);
		assertTrue(b.resvered == 123);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.reflect.FastFieldUtil#setFieldValue(java.lang.String, java.lang.Object, char)}.
	 */
	@Test
	public void testSetFieldValueStringObjectChar() {
		FastBean b = new FastBean();
		FastFieldUtil.setFieldValue("chdier", b, '#');
		assertTrue(b.chdier == '#');
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.reflect.FastFieldUtil#setFieldValue(java.lang.String, java.lang.Object, double)}.
	 */
	@Test
	public void testSetFieldValueStringObjectDouble() {
		FastBean b = new FastBean();
		FastFieldUtil.setFieldValue("earned", b, 123.456);
		assertTrue(b.earned == 123.456);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.reflect.FastFieldUtil#setFieldValue(java.lang.String, java.lang.Object, float)}.
	 */
	@Test
	public void testSetFieldValueStringObjectFloat() {
		FastBean b = new FastBean();
		FastFieldUtil.setFieldValue("tax", b, 123.456F);
		assertTrue(b.tax == 123.456F);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.reflect.FastFieldUtil#setFieldValue(java.lang.String, java.lang.Object, int)}.
	 */
	@Test
	public void testSetFieldValueStringObjectInt() {
		FastBean b = new FastBean();
		FastFieldUtil.setFieldValue("intId", b, 1312123);
		assertTrue(b.intId == 1312123);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.reflect.FastFieldUtil#setFieldValue(java.lang.String, java.lang.Object, long)}.
	 */
	@Test
	public void testSetFieldValueStringObjectLong() {
		FastBean b = new FastBean();
		FastFieldUtil.setFieldValue("empno", b, 1313134132432123l);
		assertTrue(b.empno == 1313134132432123l);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.reflect.FastFieldUtil#setFieldValue(java.lang.String, java.lang.Object, short)}.
	 */
	@Test
	public void testSetFieldValueStringObjectShort() {
		FastBean b = new FastBean();
		FastFieldUtil.setFieldValue("veridk", b, (short)5434);
		assertTrue(b.veridk == 5434);
	}

}

class FastBean {
    String strName;
    int intId;
    int intLevel;
    Integer age;
    long empno;
    boolean gender;
    byte resvered;
    short veridk;
    char chdier;
    double earned;
    float tax;
}
