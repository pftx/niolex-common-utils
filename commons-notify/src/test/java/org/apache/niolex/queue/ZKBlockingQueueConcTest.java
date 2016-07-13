/**
 * ZKBlockingQueueConcTest.java
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
package org.apache.niolex.queue;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.niolex.commons.test.MockUtil;
import org.apache.niolex.commons.util.Runner;
import org.apache.niolex.notify.AppTest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 2.1.2
 * @since Jul 13, 2016
 */
public class ZKBlockingQueueConcTest {

    private static String BS = "/queue/zkc/tmp-" + MockUtil.randInt(100, 999);

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        if (AppTest.APP.exists(BS))
            AppTest.APP.deleteTree(BS);
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        AppTest.APP.deleteTree(BS);
    }

    ZKBlockingQueue<String> queue1;
    ZKBlockingQueue<String> queue2;
    
    @Before
    public void setup() throws IOException {
        queue1 = new ZKBlockingQueue<String>(AppTest.URL, 10000, BS);
        queue2 = new ZKBlockingQueue<String>(AppTest.URL, 10000, BS + "/");
    }
    
    @After
    public void clean() throws IOException {
        queue1.close();
        queue2.close();
    }

    @Test
    public void testWholeQueue1() throws Exception {
        queue1.offer("Hello");
        queue1.offer("World");
        queue1.offer("Lex");
        queue1.offer("Nio");
        
        assertEquals("Hello", queue2.poll(100, TimeUnit.MILLISECONDS));
        assertEquals("World", queue2.poll());
        assertEquals("Lex", queue1.peek());
        assertEquals("Lex", queue2.peek());
        assertEquals("Lex", queue2.poll());
        assertEquals("Nio", queue1.poll());
        
        assertEquals(0, queue1.size());
        assertEquals(0, queue2.size());
    }
    
    public void generate(ZKBlockingQueue<String> queue, int size) throws InterruptedException {
        while (size -- > 0) {
            queue.put(MockUtil.randString(10));
        }
    }
    
    private AtomicInteger cnt = new AtomicInteger(0);
    
    public void take(ZKBlockingQueue<String> queue) throws InterruptedException {
        String item;
        int i = -1;
        do {
            item = queue.poll(10, TimeUnit.MILLISECONDS);
            ++i;
        } while (item != null);
        
        cnt.addAndGet(i);
    }
    
    @Test
    public void testConcurrent() throws Exception {
        final int ConcLevel = 8;
        final int ItemCount = 100;
        Thread[] tt = new Thread[ConcLevel];
        for (int i = 0; i < ConcLevel; ++i) {
            tt[i] = Runner.run(this, "generate", i % 2 == 0 ? queue1 : queue2, ItemCount);
        }
        
        for (int i = 0; i < ConcLevel; ++i) {
            tt[i].join();
        }
        
        assertEquals(ConcLevel * ItemCount, queue1.size());
        assertEquals(ConcLevel * ItemCount, queue2.size());
        
        for (int i = 0; i < ConcLevel; ++i) {
            tt[i] = Runner.run(this, "take", i % 2 == 0 ? queue1 : queue2);
        }
        
        for (int i = 0; i < ConcLevel; ++i) {
            tt[i].join();
        }
        
        assertEquals(0, queue1.size());
        assertEquals(0, queue2.size());
        assertEquals(ConcLevel * ItemCount, cnt.get());
    }
}
