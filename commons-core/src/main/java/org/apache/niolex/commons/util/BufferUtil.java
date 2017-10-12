/**
 * BufferUtil.java
 *
 * Copyright 2016 the original author or authors.
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
package org.apache.niolex.commons.util;

/**
 * The Java NIO buffer related functionalities.
 * 
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 3.0.1
 * @since Nov 22, 2016
 */
public abstract class BufferUtil {

    /**
     * Clean the native memory of the specified direct buffer. If the specified parameter is not a direct buffer, we do
     * nothing.
     * 
     * @param bb the byte buffer to be cleaned
     */
    @SuppressWarnings("restriction")
    public static void cleanNativeMem(Object bb) {
        if (bb instanceof sun.nio.ch.DirectBuffer) {
            sun.misc.Cleaner cleaner = ((sun.nio.ch.DirectBuffer) bb).cleaner();
            if (cleaner != null) {
                cleaner.clean();
            }
        }
    }

}
