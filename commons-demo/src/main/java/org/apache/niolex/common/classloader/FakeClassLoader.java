/**
 * FakeClassLoader.java
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

import org.apache.niolex.commons.file.FileUtil;

/**
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.5
 * @since 2013-1-4
 */
public class FakeClassLoader extends ClassLoader {

    @Override
    public Class<?> findClass(String name) {
        String e = name.substring(name.lastIndexOf('.') + 1);
        byte[] b = FileUtil.getBinaryFileContentFromClassPath("/" + e + ".apx", FakeClassLoader.class);
        return defineClass(name, b, 0, b.length);
    }

}
