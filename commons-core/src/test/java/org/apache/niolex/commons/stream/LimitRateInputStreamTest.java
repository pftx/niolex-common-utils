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

import org.junit.Test;
import org.mockito.Mockito;


/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 *
 * @version 1.0.0, $Date: 2011-6-2$
 *
 */
public class LimitRateInputStreamTest {

    @Test
    public void testRatesMoke() throws Exception {
        InputStream stub = new LimitRateInputStream(new InputStreamStub());
        byte[] b = new byte[1024];
        double cnt = 0, rate;
        DecimalFormat myFormatter = new DecimalFormat("#,###.##");
        long init = System.currentTimeMillis();
        while (System.currentTimeMillis() - init < 100) {
            cnt += stub.read(b);
            ++cnt;
            stub.read();
            if (cnt % (1025 * 120) == 0) {
            	rate = cnt / 1024 / 1024 * 1000 / (System.currentTimeMillis() - init);
            	System.out.println("Current download rate: " + myFormatter.format(rate) + "MB/s.");
            	assertTrue(rate < 21);
            }
        }
    }

    @Test
    public void testRatesReal() throws Exception {
        System.out.println("Current testRatesReal");
        InputStream stub = new LimitRateInputStream(new InputStreamStub(), 45);
        byte[] b = new byte[1024];
        double cnt = 0, rate;
        long s = System.currentTimeMillis(), t = 10000;
        DecimalFormat myFormatter = new DecimalFormat("#,###.##");
        while (t-- > 0) {
            cnt += stub.read(b);
            cnt += stub.read(b, 0, 1023);
            ++cnt;
            stub.read();
            if (cnt % (1024 * 200) == 0) {
                rate = cnt / 1024 / 1024 * 1000 / (System.currentTimeMillis() - s);
                System.out.println("Current download rate: " + myFormatter.format(rate) + "MB/s.");
                assertTrue(rate < 60);
            }
        }
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
