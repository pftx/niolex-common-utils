/**
 * ReloadablePropertiesWrapperTest.java
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
package org.apache.niolex.commons.config;


import static org.junit.Assert.*;

import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.file.DirUtil;
import org.apache.niolex.commons.file.FileMonitor.EventType;
import org.apache.niolex.commons.file.FileUtil;
import org.apache.niolex.commons.test.AnnotationOrderedRunner;
import org.apache.niolex.commons.test.AnnotationOrderedRunner.Order;
import org.apache.niolex.commons.test.MockUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-8-12
 */
@RunWith(AnnotationOrderedRunner.class)
public class ReloadablePropertiesWrapperTest {

    static ReloadablePropertiesWrapper props;
    static final String TMP = System.getProperty("user.home") + "/tmp";
    static String PATH = TMP + "/props/" + MockUtil.randString(8) + ".properties";

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        DirUtil.mkdirsIfAbsent(TMP + "/props");
        FileUtil.setCharacterFileContentToFileSystem(PATH, "reload=true", StringUtil.US_ASCII);
        props = new ReloadablePropertiesWrapper(PATH, 2);
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        props.stopRefresh();
    }

    @Test
    @Order(1)
    public void testNotify() throws Exception {
        assertTrue(props.getBoolean("reload"));
        FileUtil.setCharacterFileContentToFileSystem(PATH, "reload=haha", StringUtil.US_ASCII);
        props.notify(EventType.UPDATE, 123112);
        assertFalse(props.getBoolean("reload"));
    }

    @Test
    @Order(2)
    public void testStopRefresh() throws Exception {
        DirUtil.delete(TMP + "/props", true);
        props.notify(EventType.CREATE, 0);
        props.notify(EventType.UPDATE, 123112);
    }

}
