/**
 * LimitRateInputStreamTest.java
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
package org.apache.niolex.commons.stream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import org.apache.niolex.commons.reflect.FieldUtil;
import org.junit.Test;
import org.mockito.Mockito;


/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 *
 * @version 1.0.0, $Date: 2011-6-2$
 *
 */
public class LimitRateInputStreamTest {
    byte[] b = new byte[1024];

    private void commonRateTest(InputStream in, int exRate) throws IOException {
        DecimalFormat myFormatter = new DecimalFormat("#,###.##");
        int tick = 50;
        double cnt = 0, rate, maxRate = exRate * 1.1 + 3;
        long init = System.currentTimeMillis();
        long end = init;
        while (tick-- > 0) {
            end += 10;
            while (System.currentTimeMillis() < end) {
                cnt += in.read(b);
            }
            rate = cnt / 1024 / 1024 * 1000 / (System.currentTimeMillis() - init);
            System.out.println("(" + exRate + ") download rate: " + myFormatter.format(rate) + "MB/s.");
            assertTrue(rate < maxRate);
        }
    }

    @Test
    public void testRatesSmall() throws Exception {
        InputStream stub = new LimitRateInputStream(new InputStreamStub(), 5);
        commonRateTest(stub, 5);
    }

    @Test
    public void testRatesMoke() throws Exception {
        InputStream stub = new LimitRateInputStream(new InputStreamStub());
        commonRateTest(stub, 20);
    }

    @Test
    public void testRatesReal() throws Exception {
        InputStream stub = new LimitRateInputStream(new InputStreamStub(), 45);
        commonRateTest(stub, 45);
    }

    @Test
    public void testRates100() throws Exception {
        InputStream stub = new LimitRateInputStream(new InputStreamStub(), 100);
        commonRateTest(stub, 100);
    }

    @Test
    public void testRates200() throws Exception {
        InputStream stub = new LimitRateInputStream(new InputStreamStub(), 200);
        commonRateTest(stub, 200);
    }

    @Test
    public void testOther() throws Exception {
    	InputStream mock = Mockito.mock(InputStream.class);
    	InputStream test = new LimitRateInputStream(mock);
    	test.available();
    	assertTrue(test.equals(mock));
    	test.mark(5);
    	when(mock.markSupported()).thenReturn(true);
    	assertTrue(test.markSupported());
    	test.reset();
    	test.skip(123);
    	when(test.toString()).thenReturn("DUEj IEf OIEfOJ");
    	assertEquals("DUEj IEf OIEfOJ", test.toString());
    	test.close();
    	test.hashCode();
    	verify(mock).available();
    	verify(mock).close();
    	verify(mock).mark(5);
    	verify(mock).reset();
    	verify(mock).skip(123);
    }

    @Test
    public void testCheckSize() throws Exception {
        InputStream stub = new LimitRateInputStream(new InputStreamStub(), 123);
        long time = System.nanoTime() - 3600000000000l;
        FieldUtil.setValue(stub, "startedTime", time);
        stub.read(new byte[10240], 0, 10240);
        Long v = FieldUtil.getValue(stub, "startedTime");
        assertEquals(time, v.longValue());
        stub.read();
        Long v2 = FieldUtil.getValue(stub, "startedTime");
        assertNotEquals(time, v2.longValue());
    }

}

class InputStreamStub extends InputStream {

    @Override
    public int read() throws IOException {
        return 0;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return b.length;
    }

}
