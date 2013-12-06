/**
 * ElectorTest.java
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
package org.apache.niolex.election;


import static org.mockito.Mockito.mock;

import org.apache.niolex.notify.AppTest;
import org.apache.niolex.zookeeper.core.ZKConnector;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-12-6
 */
public class ElectorTest {

    private static Elector EL;
    private static String BS = "/election/zkc/tmp";
    private static Elector.Listener LI = mock(Elector.Listener.class);

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        ZKConnector zkc = new ZKConnector(AppTest.URL, 10000);
        zkc.makeSurePathExists(BS);
        EL = new Elector(AppTest.URL, 10000, BS, "localhost:1000", LI);
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testElector() throws Exception {
        System.out.println("not yet implemented");
    }

    @Test
    public void testRegister() throws Exception {
        System.out.println("not yet implemented");
    }

    @Test
    public void testOnDataChange() throws Exception {
        System.out.println("not yet implemented");
    }

    @Test
    public void testOnChildrenChange() throws Exception {
        System.out.println("not yet implemented");
    }

}
