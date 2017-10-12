/**
 * BlockThreadTest.java
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
package org.apache.niolex.commons.concurrent;


import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2013-12-5
 */
public class BlockThreadTest {

    @Test
    public void testStart() throws Exception {
        BlockThread bt = new BlockThread();
        bt.start();
        bt.join();
    }

    @Test
    public void testRun() throws Exception {
        BlockThread bt = new BlockThread("not yet implemented");
        bt.start();
        bt.join();
    }

    @Test
    public void testBlockThread() throws Exception {
        BlockThread bt = new BlockThread("lex", 5);
        bt.start();
        bt.join();
    }

}
