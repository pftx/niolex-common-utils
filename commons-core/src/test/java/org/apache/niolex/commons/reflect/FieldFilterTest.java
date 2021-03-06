/**
 * FieldFilterTest.java
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
package org.apache.niolex.commons.reflect;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2014-1-6
 */
public class FieldFilterTest {

    @Test
    public void testCreate() throws Exception {
        FieldResult<Object> find = FieldFilter.create().clazz(FastBean.class).withName("strName").find();
        Field field = find.result();
        assertEquals("static java.lang.String org.apache.niolex.commons.reflect.FastBean.strName", field.toString());
    }

    @Test
    public void testIsValid() throws Exception {
        FieldResult<Integer> find = FieldFilter.exactType(int.class).clazz(FastBean.class).find();
        assertEquals(3, find.results().size());
    }

    @Test
    public void testExact() throws Exception {
        FieldResult<? extends Integer> find = FieldFilter.t(int.class).clazz(FastBean.class).find();
        System.out.println(find.results());
        assertEquals(3, find.results().size());
    }

    @Test
    public void testAdd() throws Exception {
        FieldResult<? extends Integer> find = FieldFilter.to(int.class).clazz(FastBean.class).find();
        List<Field> list = find.results();
        FastBean f = FastBean.c();
        System.out.println("Get");
        for (Field fi : list) {
            System.out.print("\t");
            System.out.print(fi);
            System.out.println("\t= " + TypeUtil.safeCast(fi.get(f), int.class));
        }
        assertEquals(6, find.results().size());
    }

    @Test
    public void testSet() throws Exception {
        FieldResult<? extends Integer> find = FieldFilter.setWith(int.class).clazz(FastBean.class).find();
        List<Field> list = find.results();
        FastBean f = FastBean.c();
        System.out.println("Set");
        int i = 6677;
        for (Field fi : list) {
            System.out.print("\t");
            System.out.println(fi);
            fi.set(f, i);
            System.out.println("\t= " + fi.get(f));
        }
        assertEquals(6, find.results().size());
    }

    @Test
    public void testHost() throws Exception {
        FastBean host = new FastBean();
        host.intLevel = 10015;
        FieldResult<Object> find = FieldFilter.create().host(host).name("intLevel").find();
        assertEquals("10015", find.get().toString());
    }

    @Test
    public void testClazz() throws Exception {
        FieldResult<Object> find = FieldFilter.c().clazz(FastBean.class).nameLike("e\\w*").find();
        assertEquals(2, find.results().size());
    }

    @Test(expected=IllegalStateException.class)
    public void testFind() throws Exception {
        FieldFilter.t(int.class).find();
    }

    @Test
    public void testForType() throws Exception {
        FieldResult<Object> find = FieldFilter.c().clazz(FastBean.class).onlyStatic().noSynthetic().find();
        assertEquals("static java.lang.String org.apache.niolex.commons.reflect.FastBean.strName", find.result().toString());
    }

    @Test
    public void testExactType() throws Exception {
        FieldResult<Object> find = FieldFilter.c().clazz(FastBean.class).noStatic().find();
        assertEquals(10, find.results().size());
    }

    @Test
    public void testWithName() throws Exception {
        FieldTestBean bean = new FieldTestBean();
        FieldUtil.setValue(bean, "empno", 273921);
        FieldResult<? extends Long> find = FieldFilter.t(long.class).host(bean).nameLike("e\\w+").find();
        Long l = find.get();
        assertEquals(273921, l.intValue());
    }

    @Test
    public void testNameLike() throws Exception {
        FieldTestBean bean = new FieldTestBean();
        FieldResult<Integer> find = FieldFilter.exactType(int.class).host(bean).nameLike("int\\w+").find();
        find.last().set(29322);
        assertEquals(29322, bean.echoLevel());
    }

    @Test
    public void testOnlyStatic() throws Exception {
        FieldResult<Boolean> find = FieldFilter.exactType(boolean.class).clazz(Sub.class).find();
        assertFalse(find.host(new Sub()).get());
        assertTrue(find.host(new Super()).get());
    }

    @Test
    public void testNoStatic() throws Exception {
        FieldResult<Object> find = FieldFilter.c().host(new Sub()).name("mark").find();
        assertEquals(3, find.last().get());
        assertEquals(8, find.first().get());
    }

    @Test
    public void testNoSynthetic() throws Exception {
        FieldResult<Object> find = FieldFilter.c().host(new MethodTestBean("Filter")).noSynthetic().find();
        assertEquals("Filter", find.first().get());
    }

}
