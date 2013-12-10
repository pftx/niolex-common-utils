/**
 * ObjToStringUtilTest.java
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
package org.apache.niolex.commons.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.file.FileUtil;
import org.junit.Test;

import com.google.common.collect.Maps;


/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 *
 * @version 1.0.0, $Date: 2011-1-24$
 *
 */
@SuppressWarnings("unused")
class ObjToStringUtilTestBean {
    private static final int IXI = 393;
    private String strName;
    private int intId = 8575;
    private int intLevel = 60903;
    private Integer[] age = {1, 3, 5, 7};
    private ObjToStringUtilTestBean next = null;
    private String[] tag = {"But", "As", "Main"};
    private List<String> list = Collections.emptyList();

    public void setNext(ObjToStringUtilTestBean next) {
        this.next = next;
    }

    public void cleanAge() {
    	age = new Integer[0];
    }

    /**
     * @param strName
     */
    public ObjToStringUtilTestBean(String strName) {
        super();
        this.strName = strName;
    }

}

public class ObjToStringUtilTest extends ObjToStringUtil {

    @Test
    public void testInit() {
        Object o;
        o = (new int[1]);
        assertFalse(o instanceof Object[]);
        o = (new Integer[1]);
        assertTrue(o instanceof Object[]);
        o = new String[0];
        assertTrue(o instanceof Object[]);
    }

    @Test
    public void testBytes() {
        byte[] bytes = new byte[] {-1, -2, -3, -66, 127, 35, 98};
        String s = objToString(bytes);
        assertEquals("(7)[fffefdbe7f2362]", s);
    }

    @Test
    public void testInts() {
        int[] bytes = new int[] {-1, -2, -3, -66, 127, 35, 98};
        String s = objToString(bytes).replace(LINE_SP, "");
        assertEquals("(7)[  0 => -1  1 => -2  2 => -3  3 => -66  4 => 127  5 => 35  6 => 98]", s);
    }

    @Test
    public void testArray0() {
        String s = objToString(new Object[0]);
        assertEquals("(0)[]", s);
    }

    @Test
    public void testObjectNull() {
        String s = objToString(new Object[]{null}).replace(LINE_SP, "");
        assertEquals("(1)[  0 => null]", s);
    }

    @Test
    public void testObjectQueue() {
        Queue<ObjToStringUtilTestBean> q = new LinkedList<ObjToStringUtilTestBean>();
        q.add(new ObjToStringUtilTestBean("Lex"));
        q.add(new ObjToStringUtilTestBean("Joy"));
        q.add(new ObjToStringUtilTestBean("Jen"));
        String s = objToString(q).replace(LINE_SP, "\n");
        String e = FileUtil.getCharacterFileContentFromClassPath("queue.txt", ObjToStringUtilTest.class, StringUtil.UTF_8);
        assertEquals(e, s);
    }

    @Test
    public void testMap() {
        Map<String, String> map = Maps.newHashMap();
        map.put("a", "1");
        map.put("b", "2");
        map.put("author", "Xie, Jiyun");
        String s = objToString(map).replace(LINE_SP, "#");
        String e = "{#  author=Xie, Jiyun#  b=2#  a=1#}";
        assertEquals(e, s);
    }

    @Test
    public void testMap0() {
        Map<String, String> map = Maps.newHashMap();
        String s = objToString(map).replace(LINE_SP, "#");
        String e = "{}";
        assertEquals(e, s);
    }

    @Test
    public void testObject() {
        ObjToStringUtilTestBean t = new ObjToStringUtilTestBean("Xie, Jiyun");
        ObjToStringUtilTestBean p = new ObjToStringUtilTestBean("commons-Core");
        t.setNext(p);
        p.setNext(t);
        t.cleanAge();
        System.out.println("t => " + ObjToStringUtil.objToString(t));
    }

}
