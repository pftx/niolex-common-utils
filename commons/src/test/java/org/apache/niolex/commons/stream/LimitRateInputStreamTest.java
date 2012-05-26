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

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import org.apache.niolex.commons.stream.LimitRateInputStream;
import org.junit.Test;


/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * 
 * @version 1.0.0, $Date: 2011-6-2$
 * 
 */
public class LimitRateInputStreamTest {
    
    @Test
    public void testRatesMoke() throws Exception {
        InputStream stub = new InputStreamStub();
        byte[] b = new byte[1024];
        double cnt = 0, rate;
        DecimalFormat myFormatter = new DecimalFormat("#,###.##");
        long init = System.currentTimeMillis();
        while (System.currentTimeMillis() - init < 100) {
            cnt += stub.read(b);
            rate = cnt / 1024 / 1024 * 1000 / (System.currentTimeMillis() - init);
            System.out.println("Current download rate: " + myFormatter.format(rate) + "MB/s.");
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
            if (cnt > 1048576) {
                rate = cnt / 1024 / 1024 * 1000 / (System.currentTimeMillis() - s);
                System.out.println("Current download rate: " + myFormatter.format(rate) + "MB/s.");
                cnt = 0;
                s = System.currentTimeMillis();
            }
        }
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
