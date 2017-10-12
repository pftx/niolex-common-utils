/**
 * FakeClassLoaderTest.java
 *
 * Copyright 2013 The original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.niolex.common.classloader;

import static org.junit.Assert.assertNotEquals;

import java.lang.reflect.Field;

import org.apache.niolex.commons.reflect.FieldUtil;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2013-1-4
 */
public class FakeClassLoaderTest {

    /**
     * Test method for {@link org.apache.niolex.common.classloader.FakeClassLoader#findClass(java.lang.String)}.
     * @throws Exception
     */
    @Test
    public void testFindClassString() throws Exception {
        FakeClassLoader fcl1 = new FakeClassLoader();
        Class<?> c1 = fcl1.loadClass("org.apache.niolex.common.classloader.Single");
        Field f1 = FieldUtil.getField(c1, "S");
        Object o1 = FieldUtil.getFieldValue(f1, f1);
        System.out.println(o1.toString());
        FakeClassLoader fcl2 = new FakeClassLoader();
        Class<?> c2 = fcl2.loadClass("org.apache.niolex.common.classloader.Single");
        Field f2 = FieldUtil.getField(c2, "S");
        Object o2 = FieldUtil.getFieldValue(f2, f2);
        System.out.println(o2.toString());
        assertNotEquals(o1, o2);
        // One class loader will load a class only one time. This is cached in super method.
        Class<?> c3 = fcl2.loadClass("org.apache.niolex.common.classloader.Single");
        Field f3 = FieldUtil.getField(c3, "S");
        Object o3 = FieldUtil.getFieldValue(f3, f3);
        System.out.println(o3.toString());
    }

}
