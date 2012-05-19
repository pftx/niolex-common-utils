/**
 * ObjToStringUtilTest.java
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

import org.apache.niolex.commons.reflect.ObjToStringUtil;
import org.junit.Test;


/**
 * @author @author@ (@author-email@)
 * 
 * @version @version@, $Date: 2011-1-24$
 * 
 */
@SuppressWarnings("unused")
class ObjToStringUtilTestBean {
    private String strName = "Xie, Jiyun";
    private int intId = 8501;
    private int intLevel = 10103;
    private Integer[] age = {1, 3, 5, 7};
    private ObjToStringUtilTestBean next = null;
    private String[] tag = {"But", "As", "Main"};
    private Object qq = new Object();
    
    public void setNext(ObjToStringUtilTestBean next) {
        this.next = next;
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
    public void testArray() {
        ObjToStringUtilTestBean t = new ObjToStringUtilTestBean("Xie, Jiyun");
        ObjToStringUtilTestBean p = new ObjToStringUtilTestBean("Api-Core");
        t.setNext(p);
        ObjToStringUtilTestBean[] q = {t,t,t};
        System.out.println("t => " + ObjToStringUtil.objToString(t));
        System.out.println("{t,t,t} => " + ObjToStringUtil.objToString(q));
    }
}
