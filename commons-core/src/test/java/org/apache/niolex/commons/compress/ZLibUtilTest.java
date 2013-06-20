/**
 * ZLibUtilTest.java
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
package org.apache.niolex.commons.compress;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.niolex.commons.file.FileUtil;
import org.codehaus.jackson.type.JavaType;
import org.junit.Assert;
import org.junit.Test;


/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 *
 * @version 1.0.0, $Date: 2011-6-28$
 *
 */
public class ZLibUtilTest {

    static byte[] data = FileUtil.getBinaryFileContentFromClassPath("Data.txt", GZipUtilTest.class);

    @Test
    public void doSmoke() throws IOException {
        String in = "我是中国人好高骛远、南辕北辙、缘木求鱼、刻舟求剑→亡羊补牢→鞍前马后、承上启下、与时俱进、荣辱与共！";
        System.out.println("Original size => " + in.getBytes("utf-8").length);
        byte[] data = ZLibUtil.compressString(in);
        System.out.println("Compressed size => " + data.length);
        String actual = ZLibUtil.decompressString(data);
        Assert.assertEquals(in, actual);
    }

    @Test
    public void doSmoke2() throws IOException {
        String in = "中国是儒家,中国人是儒家思想,因为中国崇尚祖先的教化,保守,专制,封建,不可以向权威和上级提出挑战,而是要顺从,提倡循规蹈矩,老成温厚.";
        System.out.println("Original size => " + in.getBytes("utf-8").length);
        byte[] data = ZLibUtil.compressString(in);
        System.out.println("Compressed size => " + data.length);
        String actual = ZLibUtil.decompressString(data);
        Assert.assertEquals(in, actual);
    }

    @Test
    public void doSmoke3() throws IOException {
        System.out.println("Original size => " + data.length);
        byte[] q = ZLibUtil.compress(data);
        System.out.println("Compressed size => " + q.length);
        Assert.assertEquals(data.length, 54757);
        Assert.assertEquals(q.length, 18029);
    }

    @Test
    public void testCompressObj() throws IOException {
    	CTBean t = new CTBean(3, "Qute", 12212, new Date());
    	byte[] data = ZLibUtil.compressObj(t);
    	System.out.println("Obj data size => " + data.length);
    	CTBean q = ZLibUtil.decompressObj(data, CTBean.class);
    	assertEquals(q, t);
    }

	@Test
    public void testCompressArray() throws IOException {
    	CTBean t = new CTBean(5, "Lex", 893244, new Date());
    	byte[] data = ZLibUtil.compressObj(new CTBean[] {t});
    	System.out.println("Obj data size => " + data.length);
    	JavaType s = JacksonUtil.getTypeFactory().constructCollectionType(ArrayList.class, CTBean.class);
    	List<CTBean> m = ZLibUtil.decompressObj(data, s);
    	assertEquals(m.size(), 1);
    	assertEquals(m.get(0), t);
    }

	@Test
	public void testCompressArrayList() throws IOException {
	    CTBean t = new CTBean(5, "Lex", 893244, new Date());
	    CTBean r = new CTBean(8, "Nio", 8162, new Date());
	    byte[] data = ZLibUtil.compressObj(new CTBean[] {t, r});
	    System.out.println("Obj data size => " + data.length);
	    JavaType s = JacksonUtil.getTypeFactory().constructCollectionType(ArrayList.class, CTBean.class);
	    List<CTBean> m = ZLibUtil.decompressObj(data, s);
	    assertEquals(m.size(), 2);
	    assertEquals(m.get(0), t);
	    assertEquals(m.get(1), r);
	}
}
