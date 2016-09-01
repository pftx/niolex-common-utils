/**
 * GZipUtilTest.java
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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.niolex.commons.file.FileUtil;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 *
 * @version 1.0.0, $Date: 2011-6-28$
 *
 */
public class GZipUtilTest {

    static byte[] data = FileUtil.getBinaryFileContentFromClassPath("Data.txt", GZipUtilTest.class);

    @Test
    public void doSmoke() throws IOException {
        String in = "我是中国人好高骛远、南辕北辙、缘木求鱼、刻舟求剑→亡羊补牢→鞍前马后、承上启下、与时俱进、荣辱与共！";
        System.out.println("Original size => " + in.getBytes("utf-8").length);
        byte[] data = GZipUtil.compressString(in);
        System.out.println("Compressed size => " + data.length);
        String actual = GZipUtil.decompressString(data);
        Assert.assertEquals(in, actual);
    }

    @Test
    public void doSmoke2() throws IOException {
        String in = "杨朱的无君，实际是说人人皆为天子，世界不应该只有一个国王。其实是自由平等的一种古代表达形式。但是因为这是对封建社会权威与权力的一种解构。所以深为封建帝王所忌惮，以及儒生所歧视。作为王权的石头狮子，大声非议杨朱，目无集权。这就没有什么值得奇怪了。";
        System.out.println("Original size => " + in.getBytes("utf-8").length);
        byte[] data = GZipUtil.compressString(in);
        System.out.println("Compressed size => " + data.length);
        String actual = GZipUtil.decompressString(data);
        Assert.assertEquals(in, actual);
    }

    @Test
    public void doSmoke3() throws IOException {
        System.out.println("Original size => " + data.length);
        byte[] q = GZipUtil.compress(data);
        System.out.println("Compressed size => " + q.length);
        Assert.assertEquals(data.length, 54757);
        Assert.assertEquals(q.length, 18041);
        q = GZipUtil.decompress(q);
        assertArrayEquals(data, q);
    }

    @Test
    public void doCompress_1() throws IOException {
    	CTBean t = new CTBean(3, "Qute", 12212, new Date());
    	byte[] data = GZipUtil.compressObj(t);
    	System.out.println("Obj data size => " + data.length);
    	CTBean q = GZipUtil.decompressObj(data, CTBean.class);
    	assertEquals(q, t);
    }

	@Test
    public void testCompressArray() throws IOException {
        CTBean t = new CTBean(5, "Lex", 893244, new Date());
        byte[] data = GZipUtil.compressObj(new CTBean[] {t});
        System.out.println("Obj data size => " + data.length);
        JavaType s = JacksonUtil.getTypeFactory().constructCollectionType(ArrayList.class, CTBean.class);
        List<CTBean> m = GZipUtil.decompressObj(data, s);
        assertEquals(m.size(), 1);
        assertEquals(m.get(0), t);
    }

    @Test
    public void testCompressArrayList() throws IOException {
        CTBean t = new CTBean(5, "Lex", 893244, new Date());
        CTBean r = new CTBean(8, "Nio", 8162, new Date());
        byte[] data = GZipUtil.compressObj(new CTBean[] {t, r});
        System.out.println("Obj data size => " + data.length);
        JavaType s = JacksonUtil.getTypeFactory().constructCollectionType(ArrayList.class, CTBean.class);
        List<CTBean> m = GZipUtil.decompressObj(data, s);
        assertEquals(m.size(), 2);
        assertEquals(m.get(0), t);
        assertEquals(m.get(1), r);
    }

    @Test
    public void testGetInstance() throws Exception {
        assertTrue(GZipUtil.getInstance() instanceof GZiper);
    }
}
