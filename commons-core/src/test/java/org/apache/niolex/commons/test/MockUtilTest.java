/**
 * RandomUtilTest.java
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
package org.apache.niolex.commons.test;

import org.junit.Assert;

import org.apache.niolex.commons.test.MockUtil;
import org.junit.Test;


/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 *
 * @version 1.0.0, $Date: 2011-9-2$
 *
 */
public class MockUtilTest {
	static int i1, i2, i3, i4, i5, i6, i7, i8;

    @Test
    public void doTestRanInt() {
        int a = MockUtil.ranInt(128);
        Assert.assertTrue(a < 128);
    }

    @Test
    public void doTestInt() {
    	int[] a = MockUtil.randIntArray(8);
    	Assert.assertEquals(8, a.length);
    }

    @Test
    public void doTestByte() {
    	byte[] a = MockUtil.randByteArray(12);
    	Assert.assertEquals(12, a.length);
    }

    @Test
    public void reorder() {
        long start = System.currentTimeMillis();
        int i = 1000000, j = 3;
        while (i-- > 0) {
            int[] a = MockUtil.reorderIntArray(8);
            if (a[0] == j) ++i1;
            if (a[2] == j) ++i2;
            if (a[3] == j) ++i3;
            if (a[4] == j) ++i4;
            if (a[5] == j) ++i5;
            if (a[6] == j) ++i6;
            if (a[7] == j) ++i7;
            if (a[1] == j) ++i8;
        }
        System.out.println("reorder " + (System.currentTimeMillis() - start));
        System.out.println(i1);
        System.out.println(i2);
        System.out.println(i3);
        System.out.println(i4);
        System.out.println(i5);
        System.out.println(i6);
        System.out.println(i7);
        System.out.println(i8);
        Assert.assertTrue(Math.abs(i1 - i2) / (double)i1 < 0.015);
        Assert.assertTrue(Math.abs(i3 - i4) / (double)i1 < 0.015);
        Assert.assertTrue(Math.abs(i5 - i6) / (double)i1 < 0.015);
        Assert.assertTrue(Math.abs(i7 - i8) / (double)i1 < 0.015);
    }

    @Test
    public void reinternal() {
    	int i = 1000, j = 3;
    	int[] o = new int[8];
    	while (i-- > 0) {
    		int[] a = MockUtil.reorderIntArray(8);
    		j = 0;
    		++o[a[j++]];
    		++o[a[j++]];
    		++o[a[j++]];
    		++o[a[j++]];
    		++o[a[j++]];
    		++o[a[j++]];
    		++o[a[j++]];
    		++o[a[j++]];
    	}
    	System.out.println("internal");
    	for (i = 0; i < 8; ++i) {
    		System.out.println("internal " + o[i]);
    		Assert.assertEquals(1000, o[i]);
    	}
    }

}
