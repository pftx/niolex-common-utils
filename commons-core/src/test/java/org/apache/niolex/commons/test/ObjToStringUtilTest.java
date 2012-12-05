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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;


/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 *
 * @version 1.0.0, $Date: 2011-1-24$
 *
 */
@SuppressWarnings("unused")
class ObjToStringUtilTestBean {
    private static final int IXI = 393;
    private String strName = "Xie, Jiyun";
    private int intId = 8575;
    private int intLevel = 60903;
    private Integer[] age = {1, 3, 5, 7};
    private ObjToStringUtilTestBean next = null;
    private String[] tag = {"But", "As", "Main"};
    private Object qq = new Object();
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

public class ObjToStringUtilTest {

    @Test
    public void testInit() {
        new ObjToStringUtil() {};
    }

    @Test
    public void testArray() {
        ObjToStringUtilTestBean t = new ObjToStringUtilTestBean("Xie, Jiyun");
        ObjToStringUtilTestBean p = new ObjToStringUtilTestBean("commons-Core");
        t.setNext(p);
        p.setNext(t);
        System.out.println("t => " + ObjToStringUtil.objToString(t));
        List<ObjToStringUtilTestBean> q = new ArrayList<ObjToStringUtilTestBean>();
        q.add(t);
        q.add(t);
        q.add(t);
        t.setNext(null);
        t.cleanAge();
        System.out.println("{t,t,t} => " + ObjToStringUtil.objToString(q));
    }
}
