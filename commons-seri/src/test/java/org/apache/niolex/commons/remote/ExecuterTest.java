/**
 * ExecuterTest.java
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
package org.apache.niolex.commons.remote;


import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.niolex.commons.bean.One;
import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.util.DateTimeUtil;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-11-5
 */
public class ExecuterTest {

    ByteArrayOutputStream out = new ByteArrayOutputStream();

    public String getStr() throws Exception {
        String s = out.toString("utf-8");
        out.reset();
        return s.replaceAll("\r?\n", "^").replace("\"", "`");
    }

    @Test
    public void testGetter() throws Exception {
        new Executer.Getter().execute(new ReentrantLock(), out, null);
        assertEquals("{^  `holdCount` : 0,^  `queueLength` : 0,^  `fair` : false,^  `heldByCurrentThread` : false,^  `locked` : false^}^", getStr());
    }

    @Test
    public void testLister() throws Exception {
        new Executer.Lister().execute(new Date(), out, null);
        assertEquals("All Fields Of Date^    gcal^    jcal^    fastTime^    cdate^    defaultCenturyStart^    serialVersionUID^    wtb^    ttb^---^", getStr());
    }

    @Test
    public void testSetterParam() throws Exception {
        new Executer.Setter().execute(new Date(), out, new String[] {"a", "b"});
        assertEquals("Invalid Command.^", getStr());
    }

    @Test
    public void testSetter() throws Exception {
        Date d = new Date();
        new Executer.Setter().execute(d, out, new String[] {"a", "b", "fastTime", "9283901232"});
        assertEquals("1970-04-18 18:51:41.232", DateTimeUtil.formatDate2LongStr(d));
        assertEquals("Set Field Success.^", getStr());
    }

    @Test
    public void testSetterNSF() throws Exception {
        Date d = new Date();
        new Executer.Setter().execute(d, out, new String[] {"a", "b", "time", "9283901232"});
        assertEquals("Field Not Found.^", getStr());
    }

    @Test
    public void testSetterNSP() throws Exception {
        Date d = new Date();
        new Executer.Setter().execute(d, out, new String[] {"a", "b", "cdate", "9283901232"});
        assertEquals("The Field Type [Date] Is Not Supported.^", getStr());
    }

    @Test
    public void testSetterINV() throws Exception {
        Date d = new Date();
        new Executer.Setter().execute(d, out, new String[] {"a", "b", "fastTime", "lex"});
        assertEquals("Failed to Set Field:For input string: `lex`.^", getStr());
    }

    @Test
    public void testInvoker() throws Exception {
        Date d = new Date();
        new Executer.Invoker().execute(d, out, new String[] {"a", "b", "fastTime", "lex"});
        assertEquals("Target Date Is not Allowed to Invoke.^", getStr());
    }

    @Test
    public void testInvokerInvokable() throws Exception {
        Invokable d = new Invokable(){
            @Override
            public void invoke(OutputStream out, String[] args) throws IOException {
                out.write(StringUtil.strToAsciiByte("Here comes me!"));
            }};
        new Executer.Invoker().execute(d, out, null);
        assertEquals("Here comes me!---Invoke Success---^", getStr());
    }

    @Test
    public void testInvokerRunnable() throws Exception {
        final One<String> one = One.create("no");
        Runnable d = new Runnable(){
            @Override
            public void run() {
                one.a = "yes";
            }};
        new Executer.Invoker().execute(d, out, null);
        assertEquals("---Invoke Success---^", getStr());
        assertEquals("yes", one.a);
    }

    @Test
    public void testInvoMonitorNSP() throws Exception {
        Date d = new Date();
        new Executer.InvoMonitor().execute(d, out, new String[] {"a", "b", "fastTime", "lex"});
        assertEquals("Object is not a Monitor.^", getStr());
    }

    @Test
    public void testInvoMonitor() throws Exception {
        Monitor d = new Monitor(10);
        d.addValue("fastTime", 61);
        d.addValue("fastTime", 22);
        d.addValue("fastTime", 12);
        new Executer.InvoMonitor().execute(d, out, new String[] {"a", "b", "fastTime", "lex"});
        String s = getStr();
        System.out.println(s);
        assertEquals(29, s.indexOf("=61"));
        assertEquals(62, s.indexOf("=22"));
        assertEquals(95, s.indexOf("=12"));
    }

    @Test
    public void testInvoMonitorArg() throws Exception {
        Monitor d = new Monitor(10);
        d.addValue("fastTime", 61);
        d.addValue("fastTime", 22);
        d.addValue("fastTime", 12);
        new Executer.InvoMonitor().execute(d, out, new String[] {"a", "b", "fastTime"});
        String s = getStr();
        System.out.println(s);
        assertEquals(29, s.indexOf("=61"));
        assertEquals(62, s.indexOf("=22"));
        assertEquals(95, s.indexOf("=12"));
    }

    @Test
    public void testInvoMonitorKey() throws Exception {
        Monitor d = new Monitor(10);
        new Executer.InvoMonitor().execute(d, out, new String[] {"a", "b"});
        assertEquals("Please specify the Key to Monitor.^", getStr());
    }

}
